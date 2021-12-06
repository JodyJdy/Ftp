package ftp.ftpexchanger;

import ftp.codec.ExchangeByteBufDecoder;
import ftp.util.ByteUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * ftp交换器类，支持ftpclient经过exchange拿到server的数据
 *
 *  client < - > exchange < - > exchange < - > server
 **/
public class FtpExchanger {
    private final int bindPort;
    private final int connectPort;
    private final String connectAddress;

    public FtpExchanger(int bindPort, int connectPort, String connectAddress) {
        this.bindPort = bindPort;
        this.connectPort = connectPort;
        this.connectAddress = connectAddress;
    }

    public static void main(String[] args) throws Exception {
        new FtpExchanger(8888, 9999, "localhost").start();
    }

    private void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);
        ExchangeByteBufDecoder bufDecoder = new ExchangeByteBufDecoder(new ChannelExchanger());
        try {
            //用于监听bindPort上的连接
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(ByteUtil.MB * 5, 0, 4));
                    ch.pipeline().addLast(bufDecoder);
                }
            });            //用于连接 connectAddress:connectPort
            EventLoopGroup group = new NioEventLoopGroup(4);
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.remoteAddress(connectAddress, connectPort);
            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(ByteUtil.MB * 5, 0, 4));
                    ch.pipeline().addLast(bufDecoder);
                }
            });
            ChannelFuture cf = serverBootstrap.bind(bindPort).sync();
            ExchangerContext.setBootstrap(clientBootstrap);
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
