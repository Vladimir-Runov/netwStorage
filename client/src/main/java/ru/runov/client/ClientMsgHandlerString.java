package ru.runov.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientMsgHandlerString extends SimpleChannelInboundHandler<String> {
    private CallBackInterface onMessageReceiedCallback;
    public ClientMsgHandlerString(CallBackInterface onMessageReceiedCallback) {
        this.onMessageReceiedCallback = onMessageReceiedCallback;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        if (onMessageReceiedCallback != null)
            onMessageReceiedCallback.callback(s);
    }
}
