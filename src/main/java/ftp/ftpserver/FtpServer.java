package ftp.ftpserver;

import ftp.codec.TransStructDecoer;
import ftp.codec.TransStructEncoder;
import ftp.handler.FtpServerHandler;
import ftp.util.ByteUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * ftp服务端
 **/
public class FtpServer {
    private final int port;

    public FtpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new FtpServer(9999).start();
    }

    private void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);
        try {
            //创建服务器端的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(ByteUtil.MB * 5, 0, 4));
                    ch.pipeline().addLast("decoder", new TransStructDecoer());
                    ch.pipeline().addLast("encoder", new TransStructEncoder());
                    ch.pipeline().addLast("structDecoder", new FtpServerHandler());
                }
            });
            System.out.println("服务端启动完成");
            ChannelFuture cf = bootstrap.bind(port).sync();
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
