package ftp.handler;

import ftp.TransStruct;
import ftp.enums.DataTransStatus;
import ftp.enums.InstructionEnum;
import ftp.enums.TransType;
import ftp.ftpclient.ClientContext;
import ftp.instruction.Instruction;
import ftp.instruction.InstructionResolver;
import ftp.instruction.availableinstruciton.InstructionResponse;
import ftp.status.ClientStatus;
import ftp.status.Status;
import ftp.util.ByteUtil;
import ftp.util.FileUtil;
import ftp.util.TransStructUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.io.IOException;

/**
 * 文件上传下载是双向的， 复用BaseHandler，只需要考虑数据进出
 **/
public class BaseHandler extends SimpleChannelInboundHandler<TransStruct> {
    private final Status status;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    BaseHandler(Status status) {
        this.status = status;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransStruct msg) throws Exception {
        //指令
        if (TransType.INSTRUCTION.getValue() == msg.getType()) {
            Instruction instruction = InstructionResolver.resolver(new String(msg.getContents(), CharsetUtil.UTF_8), status);
            //接受到ls，cd这类命令的响应，直接输出
            if (instruction instanceof InstructionResponse) {
                outResponse(msg);
                return;
            }
            //接受到指令，执行后将结果返回回去
            TransStruct response = instruction.execute();
            // response == null 代表指令执行后，不需要响应给对方
            if (response != null) {
                ctx.writeAndFlush(response).sync();
            }
        //数据传输
        } else if (TransType.DATA.getValue() == msg.getType()) {
            //本地读取向外发送
            if (status.getInputStream() != null) {
                switch (DataTransStatus.getStatus(msg.getStatus())) {
                    case LAST_SUCC:
                        transFileToOut(ctx);
                        break;
                    case TRANS_DONE:
                        closeFileTrans();
                        break;
                    default:
                        break;
                }
            //外部读取保存到本地
            } else if (status.getOutputStream() != null) {
                //将数据存入本地
                if (msg.getContents() != null) {
                    status.getOutputStream().write(msg.getContents());
                }
                switch (DataTransStatus.getStatus(msg.getStatus())) {
                    case TRANSING:
                        //告诉对方，本次传输成功
                        ctx.writeAndFlush(TransStructUtil.generateLastSucc());
                        break;
                    case TRANS_DONE:
                        //告诉对方文件已经传输完成
                        ctx.writeAndFlush(TransStructUtil.generateTransDone()).sync();
                        closeFileTrans();
                        break;
                    default:
                        break;
                }
            }
        } else {
            System.out.println("入参异常，缺少 DataType");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //遇到异常，关闭所有打开的流
        closeFileTrans();
    }

    /**
     * 读取本地文件发送，为了减少通信次数，1次传输1MB
     */
    private void transFileToOut(ChannelHandlerContext ctx) {
        byte[] bytes;
        if (status.getEnd() - status.getPos() >= ByteUtil.MB) {
            bytes = new byte[ByteUtil.MB];
        } else {
            bytes = new byte[(int) (status.getEnd() - status.getPos())];
        }
        int len = 0;
        try {
            len = status.getInputStream().read(bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TransStruct t;
        //传输完毕
        if (status.getPos() + bytes.length == status.getEnd()) {
            //未传输完毕
            t = TransStructUtil.generateTransDone(bytes);
        } else {
            t = TransStructUtil.generateTransing(bytes);
        }
        status.setPos(status.getPos() + len);
        ctx.writeAndFlush(t);
    }

    /**
     * 文件传输完毕后，关闭流，初始化各个参数
     */
    private void closeFileTrans() {
        try {
            if (status.getInputStream() != null) {
                status.getInputStream().close();
            }
            if (status.getOutputStream() != null) {
                status.getOutputStream().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        status.setInputStream(null);
        status.setOutputStream(null);
        status.setPos(0);
        status.setEnd(0);
        if (status instanceof ClientStatus) {
            ClientStatus clientStatus = (ClientStatus) status;
            //传输完成后，执行后置任务
            clientStatus.getTransDoneTask().run();
            clientStatus.setTransDoneTask(null);
            //任务传输完毕，将客户端归还到池子里去
            ClientContext.backClient(clientStatus);
        }
    }

    private void outResponse(TransStruct response) {
        String content = new String(response.getContents(), CharsetUtil.UTF_8);
        String msg = content.substring(InstructionEnum.INSTRUCTION_RESPONSE.getIns().length() + FileUtil.SPLIT.length());
        if (msg.trim().length() == 0) {
            msg = "\n";
        }
        System.out.print(msg);
    }

    public Status getStatus() {
        return status;
    }
}

