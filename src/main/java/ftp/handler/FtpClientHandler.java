package ftp.handler;

import ftp.status.Status;
import ftp.util.TransStructUtil;
import io.netty.channel.ChannelHandlerContext;

/**
 * * 客户端使用的 Handler * * @date 2021/12/3
 **/
public class FtpClientHandler extends BaseHandler {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(TransStructUtil.generateIdStruct());
    }

    public FtpClientHandler(Status status) {
        super(status);
    }
}
