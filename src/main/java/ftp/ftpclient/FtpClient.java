package ftp.ftpclient;

import ftp.codec.TransStructDecoer;
import ftp.codec.TransStructEncoder;
import ftp.ftpclient.trans.ChannelPool;
import ftp.ftpclient.trans.ChannelStatus;
import ftp.ftpclient.trans.FileTransTaskQueue;
import ftp.ftpclient.trans.TaskExecutor;
import ftp.handler.FtpClientHandler;
import ftp.instruction.Instruction;
import ftp.instruction.InstructionResolver;
import ftp.instruction.availableinstruciton.UpDirInstruction;
import ftp.status.ClientStatus;
import ftp.util.ByteUtil;
import ftp.util.StructTransUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ftp客户端
 **/
public class FtpClient {
    private final int port;
    private final String address;
    /**
     * 工作信道数量
     */
    private final int workerChannelNum;

    public FtpClient(int port, String address, int workerChannelNum) {
        this.port = port;
        this.address = address;
        this.workerChannelNum = workerChannelNum;
    }

    public static void main(String[] args) throws Exception {
        new FtpClient(8888, "localhost", 5).start();
    }

    private void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(4);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress(address, port);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(ByteUtil.MB * 5, 0, 4));
                    ch.pipeline().addLast("decoder", new TransStructDecoer());
                    ch.pipeline().addLast("encoder", new TransStructEncoder());
                    ch.pipeline().addLast("structDecoder", new FtpClientHandler(new ClientStatus()));
                }
            });
            //连接服务端，并设置通信信道，信道状态到 客户端上下文对象中
            ClientContext.setCommunicate(bootstrap.connect().sync().channel());
            ClientContext.setClientStatus((ClientStatus) ClientContext.getCommunicate().pipeline().get(FtpClientHandler.class).getStatus());
            List<ChannelStatus> statuses = new ArrayList<>();
            //创建工作信道
            for (int i = 0; i < workerChannelNum; i++) {
                ChannelFuture tempFuture = bootstrap.connect().sync();
                Channel tempChannel = tempFuture.channel();
                FtpClientHandler handler = tempChannel.pipeline().get(FtpClientHandler.class);
                statuses.add(new ChannelStatus((ClientStatus) handler.getStatus(), tempChannel));
            }
            ChannelPool channelPool = new ChannelPool(statuses);
            FileTransTaskQueue taskQueue = new FileTransTaskQueue();
            ClientContext.setTaskQueue(taskQueue);
            //启动文件传输执行器
            TaskExecutor executor = new TaskExecutor(taskQueue, channelPool);
            executor.start();
            //启动客户端命令行工具
            sendInstruction();
            //阻塞直到连接关闭
            ClientContext.getCommunicate().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     * 用于向服务的发送指令
     */
    private void sendInstruction() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String ins = sc.nextLine();
            execute(ins);
        }
    }

    public void execute(String ins) {
        //检查指令是否存在
        if (InstructionResolver.checkExist(ins)) {
            // 如果是传输命令
            if (InstructionResolver.isTrans(ins)) {
                //如果是传输文件的命令，直接丢到任务队列里面
                if (InstructionResolver.isFileTrans(ins)) {
                    ClientContext.getTaskQueue().addFileTransTask(ins);
                } else {
                    //传输目录的命令
                    Instruction instruction = InstructionResolver.resolver(ins, ClientContext.getClientStatus());
                    if (instruction instanceof UpDirInstruction) {
                        instruction.process();
                    } else {

                        //目录下载命令，直接发送给服务端
                        ClientContext.getCommunicate().writeAndFlush(StructTransUtil.generateInsStruct(ins));
                    }
                }
            } else {
                Instruction instruction = InstructionResolver.resolver(ins, ClientContext.getClientStatus());
                instruction.process();
                //如果是本地执行指令，直接执行，直接执行，否则发送到服务端
                if (InstructionResolver.localExec(ins)) {
                    instruction.execute();
                } else {
                    ClientContext.getCommunicate().writeAndFlush(StructTransUtil.generateInsStruct(ins));
                }
            }
        } else {
            System.out.println("指令不存在");
        }
    }
}
