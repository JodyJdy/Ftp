package ftp.codec;

import ftp.util.StructTransUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * *TransStruct解码器
 **/
@ChannelHandler.Sharable
public class TransStructDecoer extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        out.add(StructTransUtil.transToStruct(msg));
    }
}
