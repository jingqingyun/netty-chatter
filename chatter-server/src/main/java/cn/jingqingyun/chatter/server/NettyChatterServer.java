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
    private volatile static NettyChatterServer instance;

    private int port = DEFAULT_PORT;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture future;

    private NettyChatterServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).localAddress(port)
                .childHandler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder()).addLast(new StringEncoder())
                                .addLast(new NettyChatterServerHandler());
                    }

                }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    public static NettyChatterServer getInstance() {
        if (instance == null) {
            // 只有在实例为空时才需要实例化
            synchronized (NettyChatterServer.class) {
                // 通过同步保证实例化为院子操作
                if (instance == null) {
                    instance = new NettyChatterServer();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        NettyChatterServer server = NettyChatterServer.getInstance();
        if (args.length > 0) {
            server.setPort(Integer.valueOf(args[0]));
        }
        try {
            server.serve();
        } finally {
            server.stop();
        }
    }

    public void serve() {
        try {
            future = serverBootstrap.bind().sync();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        logger.info("NettyChatter server started.");

    }

    public void setPort(int port) {
        this.port = port;
    }

    public void stop() {
        try {
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        logger.info("NettyChatter server closed.");
    }

}
