package cn.jingqingyun.chatter.server;

import com.sun.istack.internal.logging.Logger;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyChatterServerHandler extends ChannelInboundHandlerAdapter {
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private Logger logger = Logger.getLogger(NettyChatterServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("New client connected.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        channels.writeAndFlush(String.format("[%s]: %s", ctx.channel().localAddress(), msg));
        logger.info("A message transmitted.");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info(ctx.channel().remoteAddress().toString());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        logger.info("A new client joined.");
        for (Channel ch : channels) {
            System.out.print(ch.remoteAddress() + ", ");
        }
        System.out.println();
    }

}
