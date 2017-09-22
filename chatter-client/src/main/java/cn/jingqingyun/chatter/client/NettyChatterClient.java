package cn.jingqingyun.chatter.client;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Scanner;

import org.apache.log4j.Logger;

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
    private static final Logger logger = Logger.getLogger(NettyChatterClient.class);
    private static final String REMOTE_ADDR = "localhost";
    private static final int REMOTE_PORT = 8888;

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).localAddress(new InetSocketAddress(REMOTE_ADDR, new Random().nextInt(1000) + 50000))
                .channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder()).addLast(new StringEncoder())
                                .addLast(new NettyChatterClientHandler());
                    }
                });

        try {
            ChannelFuture future = bootstrap.connect(REMOTE_ADDR, REMOTE_PORT).sync();
            logger.info(String.format("NettyChatter(%s) conneted.", future.channel().localAddress().toString()));

            try (Scanner scanner = new Scanner(System.in)) {
                while (scanner.hasNextLine()) {
                    future.channel().pipeline().writeAndFlush(scanner.nextLine());
                }
            }

            future.channel().close().sync();
            logger.info("NettyChatter server closed.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}
