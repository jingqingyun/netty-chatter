package cn.jingqingyun.chatter.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyChatterServer {
    public static final int DEFAULT_PORT = 8888;
    private static final Logger logger = LogManager.getLogger();
    private int port = DEFAULT_PORT;

    public static void main(String[] args) {
        NettyChatterServer server = new NettyChatterServer();
        if (args.length > 0) {
            server.setPort(Integer.valueOf(args[0]));
        }
        server.serve();
    }

    public void setPort(int port) {
        this.port = port;
    }

    private void serve() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).localAddress(port)
                .childHandler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder()).addLast(new StringEncoder())
                                .addLast(new NettyChatterServerHandler());
                    }

                }).option(ChannelOption.SO_BACKLOG, 128) // (5)
                .childOption(ChannelOption.SO_KEEPALIVE, true); // (6);
        try {
            ChannelFuture future = serverBootstrap.bind().sync();
            logger.info("NettyChatter server started.");
            future.channel().closeFuture().sync();
            logger.info("NettyChatter server closed.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
