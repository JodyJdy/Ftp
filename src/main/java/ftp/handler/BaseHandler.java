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
import ftp.util.StructTransUtil;
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
            if (instruction instanceof InstructionResponse) {
                outResponse(msg);
                return;
            }
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
                if (msg.getContents() != null) {
                    status.getOutputStream().write(msg.getContents());
                }
                switch (DataTransStatus.getStatus(msg.getStatus())) {
                    case TRANSING:
                        ctx.writeAndFlush(StructTransUtil.generateLastSucc());
                        break;
                    case TRANS_DONE:
                        //告诉对方已经收到
                        ctx.writeAndFlush(StructTransUtil.generateTransDone()).sync();
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
            t = StructTransUtil.generateTransDone(bytes);
        } else {
            t = StructTransUtil.generateTransing(bytes);
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
            clientStatus.getTask().run();
            clientStatus.setTask(null);
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

