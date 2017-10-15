package cn.jingqingyun.chatter.protocol;

import java.nio.charset.Charset;
import java.util.List;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(JSON.parseObject(new String(in.array(), Charset.forName("UTF-8"))));
    }

}
