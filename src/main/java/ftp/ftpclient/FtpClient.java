package ftp.ftpclient;

import ftp.codec.TransStructDecoer;
import ftp.codec.TransStructEncoder;
import ftp.enums.InstructionEnum;
import ftp.ftpclient.trans.FileTransTaskQueue;
import ftp.ftpclient.trans.TaskExecutor;
import ftp.handler.FtpClientHandler;
import ftp.instruction.Instruction;
import ftp.instruction.InstructionResolver;
import ftp.instruction.availableinstruciton.UpDirInstruction;
import ftp.status.ClientStatus;
import ftp.util.ByteUtil;
import ftp.util.TransStructUtil;
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
    /**
     * 服务端端口
     */
    private final int serverPort;
    /**
     * 服务端地址
     */
    private final String serverAddress;
    /**
     * 工作信道数量
     */
    private final int workerChannelNum;

    public FtpClient(int serverPort, String serverAddress, int workerChannelNum) {
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
        this.workerChannelNum = workerChannelNum;
    }

    public static void main(String[] args) throws Exception {
        new FtpClient(9900, "localhost", 5).start();
    }

    private void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(4);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress(serverAddress, serverPort);
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
            Channel communicateChannel = bootstrap.connect().sync().channel();
            ClientStatus clientStatus = (ClientStatus)communicateChannel.pipeline().get(FtpClientHandler.class).getStatus();
            ClientContext.setCommunicateChannel(communicateChannel);
            ClientContext.setClientStatus(clientStatus);

            List<ClientStatus> clientStatuses = new ArrayList<>();
            //创建工作信道，用于传输文件
            for (int i = 0; i < workerChannelNum; i++) {
                ChannelFuture tempFuture = bootstrap.connect().sync();
                Channel transChannel = tempFuture.channel();
                FtpClientHandler handler = transChannel.pipeline().get(FtpClientHandler.class);
                ClientStatus transClientStatus =(ClientStatus)handler.getStatus();
                transClientStatus.setChannel(transChannel);
                clientStatuses.add(transClientStatus);
            }
            ClientContext.setTransClientStatus(clientStatuses);
            FileTransTaskQueue taskQueue = new FileTransTaskQueue();
            ClientContext.setTaskQueue(taskQueue);
            //启动文件传输执行器
            TaskExecutor executor = new TaskExecutor(taskQueue);
            executor.start();
            System.out.println("客户端启动完成·");
            //启动客户端命令行工具
            sendInstruction();
            //阻塞直到连接关闭
            ClientContext.getCommunicateChannel().closeFuture().sync();
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
        InstructionEnum check = InstructionResolver.checkExist(ins);
        if(check == null){
            System.out.println("指令不存在");
            return;
        }
        //@todo 这块的逻辑有点乱
        // 如果是传输命令
        if (InstructionResolver.isTrans(check)) {
            //如果是传输单个文件的命令，直接丢到任务队列里面
            if (InstructionEnum.UP == check || InstructionEnum.DOWN == check) {
                ClientContext.getTaskQueue().addFileTransTask(ins);
            } else {
                //传输目录的命令
                Instruction instruction = InstructionResolver.resolver(check, ClientContext.getClientStatus(),ins);
                if (check == InstructionEnum.UP_DIR) {
                    instruction.preProcess();
                } else if(check == InstructionEnum.DOWN_DIR) {
                    //目录下载命令，直接发送给服务端
                    ClientContext.getCommunicateChannel().writeAndFlush(TransStructUtil.generateInsStruct(ins));
                }
            }
        } else {
            Instruction instruction = InstructionResolver.resolver(check, ClientContext.getClientStatus(),ins);
            instruction.preProcess();
            //如果是本地执行指令，直接执行，直接执行，否则发送到服务端
            if (InstructionResolver.localExec(check)) {
                instruction.execute();
            } else {
                //使用通信channel发送到指令到服务端
                ClientContext.getCommunicateChannel().writeAndFlush(TransStructUtil.generateInsStruct(ins));
            }
        }
    }
}
