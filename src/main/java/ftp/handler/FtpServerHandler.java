package ftp.handler;

import ftp.status.ServerClientStatus;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务端使用的 Handler * * @date 2021/12/3
 **/
public class FtpServerHandler extends BaseHandler {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("客户端接入:" + ctx.channel().remoteAddress());
    }

    public FtpServerHandler() {
        super(new ServerClientStatus());
    }
}
