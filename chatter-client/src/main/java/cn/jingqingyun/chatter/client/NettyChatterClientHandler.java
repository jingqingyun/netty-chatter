package cn.jingqingyun.chatter.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyChatterClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Runnable msgListener = new MessageListener(ctx.channel());
        new Thread(msgListener, "msgListener-" + ctx.channel().localAddress().toString()).start();
        logger.debug("Message listener started.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
    }

}
