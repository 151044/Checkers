package com.colin.games.checkers;

import com.colin.games.checkers.common.networks.MessageDecoder;
import com.colin.games.checkers.common.networks.MessageEncoder;
import com.colin.games.checkers.common.networks.MessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;

/**
 * The main client class.
 */
public class Client {
    private final InetAddress addr;
    private final int port;
    private Channel chan;
    private static Client current;
    private String name;
    private ChannelFuture connect;

    /**
     * Constructs a new Client.
     *
     * @param addr The IP to connect to
     * @param port The port to connect to
     */
    public Client(InetAddress addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    /**
     * Connects to the server on a separate thread.
     */
    public void run() {
        Thread t = new Thread(() -> {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap boot = new Bootstrap();
            boot.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new LineBasedFrameDecoder(3000));
                            p.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            p.addLast(new MessageDecoder());
                            p.addLast(new MessageHandler());
                            p.addLast(new StringEncoder());
                            p.addLast(new MessageEncoder());
                        }
                    });
            try {
                connect = boot.connect(addr, port);
                connect.sync();
                chan = connect.channel();
                connect.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "client");
        t.start();
    }

    /**
     * Gets the communication channel with the server.
     *
     * @return The channel requested
     */
    public Channel getChannel() {
        return chan;
    }

    /**
     * Sets the name of the client.
     *
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the client.
     *
     * @return The name of this client
     */
    public String getName() {
        return name;
    }

    /**
     * Writes and flushes an object to the channel.
     *
     * @param target The object to write
     */
    public void writeAndFlush(Object target) {
        chan.write(target);
        chan.flush();
    }

    /**
     * Sets the current client.
     *
     * @param cli The client to set
     */
    public static void setCurrent(Client cli) {
        current = cli;
    }

    /**
     * Gets the current running client.
     *
     * @return The client instance
     */
    public static Client getCurrent() {
        return current;
    }
}
