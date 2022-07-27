package ftp.codec;

import ftp.TransStruct;
import ftp.util.TransStructUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 *  TransStruct编码器
 **/
@ChannelHandler.Sharable
public class TransStructEncoder extends MessageToMessageEncoder<TransStruct> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TransStruct msg, List<Object> out) {
        out.add(TransStructUtil.transToByteBuf(msg));
    }
}
