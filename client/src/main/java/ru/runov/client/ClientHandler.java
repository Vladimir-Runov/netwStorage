package ru.runov.client;

//import com.google.protobuf.ByteString;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ru.runov.protobuf.*;

public class ClientHandler extends SimpleChannelInboundHandler<TheMessages.DemoResponse> {

    private Channel channel;
    private TheMessages.TheResponse resp;
    private final BlockingQueue<TheMessages.TheResponse> resps = new LinkedBlockingQueue<TheMessages.TheResponse>();

    public TheMessages.TheResponse sendRequest(TheMessages.Type type) {

        TheMessages.TheRequest req = null;
        // send File request
        if (TheMessages.Type.FILE == type) {
            InputStream inputStream = null;
            try {
                inputStream = getClass().getResourceAsStream("/components.png");

                TheMessages.FileMsg fileMsg = TheMessages.FileMsg.newBuilder()
                        .setFileBytes(ByteString.readFrom(inputStream))
                        .setFilename("components.png")
                        .build();
                req = TheMessages.TheRequest.newBuilder()
                        .setType(TheMessages.Type.FILE)
                        .setFile(fileMsg)
                        .build();
                // Send request
                channel.writeAndFlush(req);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // send message request.
            req = TheMessages.TheRequest.newBuilder()
                    .setType(TheMessages.Type.MSG)
                    .setRequestMsg("From Client").build();
        }
        // Send request
        channel.writeAndFlush(req);

        // Now wait for response from server
        boolean interrupted = false;
        for (; ; ) {
            try {
                resp = resps.take();
                break;
            } catch (InterruptedException ignore) {
                interrupted = true;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return resp;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TheMessages.DemoResponse msg)
            throws Exception {
        resps.add(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
