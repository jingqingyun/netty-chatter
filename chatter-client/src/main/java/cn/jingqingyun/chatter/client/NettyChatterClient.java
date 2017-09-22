package cn.jingqingyun.chatter.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger logger = LogManager.getLogger(NettyChatterClient.class);
    private static final String REMOTE_ADDR = "localhost";
    private static final int REMOTE_PORT = 8888;

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(REMOTE_ADDR, REMOTE_PORT)
                .handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder()).addLast(new StringEncoder())
                                .addLast(new NettyChatterClientHandler());
                    }

                });

        try {
            ChannelFuture future = bootstrap.connect().sync();
            logger.info(String.format("NettyChatter(%s) conneted.", future.channel().localAddress().toString()));
            future.channel().close().sync();
            logger.info("NettyChatter closed.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}
