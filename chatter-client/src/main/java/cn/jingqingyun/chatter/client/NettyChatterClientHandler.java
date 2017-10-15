package cn.jingqingyun.chatter.client;

import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.jingqingyun.chatter.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyChatterClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger();
    private BlockingQueue<Message> msgQueue;

    public NettyChatterClientHandler(BlockingQueue<Message> msgQueue) {
        this.msgQueue = msgQueue;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Runnable msgListener = new MessageListener(ctx.channel());
        new Thread(msgListener, "msgListener-" + ctx.channel().localAddress().toString()).start();
        logger.debug("Message listener started.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        msgQueue.put((Message) msg);
    }

}
