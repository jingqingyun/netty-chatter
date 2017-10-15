package cn.jingqingyun.chatter.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.jingqingyun.chatter.model.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyChatterClient {
    private static final Logger logger = LogManager.getLogger();
    private static final String REMOTE_ADDR = "localhost";
    private static final int REMOTE_PORT = 8888;
    private static final int DEFAULT_MAX_SIZE = 10;

    private EventLoopGroup group;
    private ChannelFuture future;
    private Bootstrap bootstrap;
    private BlockingQueue<Message> msgQueue;

    public NettyChatterClient() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        msgQueue = new ArrayBlockingQueue<>(DEFAULT_MAX_SIZE);

        bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(REMOTE_ADDR, REMOTE_PORT)
                .handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder()).addLast(new StringEncoder())
                                .addLast(new NettyChatterClientHandler(msgQueue));
                    }

                });
    }

    public static void main(String[] args) {
        NettyChatterClient client = new NettyChatterClient();
        try {
            client.serve();
        } finally {
            client.stop();
        }
    }

    public Message getMessage() {
        Message msg = null;
        try {
            msg = msgQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public void sendMsg(String msg) {
        future.channel().write(msg);
    }

    public void serve() {
        try {
            future = bootstrap.connect().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info(String.format("NettyChatter(%s) conneted.", future.channel().localAddress().toString()));
    }

    public void stop() {
        try {
            future.channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        logger.info("NettyChatter closed.");
    }

}
