package cn.jingqingyun.chatter.client;

import java.util.Scanner;

import io.netty.channel.Channel;

public class MessageListener implements Runnable {
    private Channel channel;

    public MessageListener(Channel channel) {
        super();
        this.channel = channel;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                channel.writeAndFlush(scanner.nextLine());
            }
        }
    }

}
