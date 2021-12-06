package ftp.codec;

import ftp.ftpexchanger.ChannelExchanger;
import ftp.ftpexchanger.ExchangerContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

/**
 *  exchange使用 byteBuf解码器
 **/
@ChannelHandler.Sharable
public class ExchangeByteBufDecoder extends SimpleChannelInboundHandler<ByteBuf> {
    private final ChannelExchanger exchanger;

    public ExchangeByteBufDecoder(ChannelExchanger exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        Channel src = ctx.channel();
        if (!exchanger.contains(src)) {
            ChannelFuture future = ExchangerContext.getBootstrap().connect().sync();
            exchanger.addChannel(src, future.channel());
        }
        Channel target = exchanger.getChannel(src);
        ByteBuf msg2 = msg.copy();
        //进行转发
        target.writeAndFlush(msg2);
    }
}
