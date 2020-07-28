package com.colin.games.checkers.common.networks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles incoming messages.
 */
public class MessageHandler extends ChannelInboundHandlerAdapter {
    private static final Logger allMsg = LogManager.getFormatterLogger("MessageHandler");

    /**
     * Constructs a new ClientMessageHandler.
     */
    public MessageHandler(){
        super();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Message m = (Message) msg;
        allMsg.debug("Received message " + m.getType() + " , content " + m.getContent());
        MessageDispatch.dispatch(ctx,m);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        System.exit(2);
    }
}
