package ru.runov.client;

//import com.google.protobuf.ByteString;
//import TheMessages;
import com.google.protobuf.ByteString;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
//import ru.runov.server.TheMessages;
//import ru.runov.TheMessages;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//import ru.runov.clientcommon.* protobuf.*;

public class ClientHandler extends SimpleChannelInboundHandler<TheMessages.TheResponse> {
    private CallBackInterface onMessageReceiedCallback;
    private Channel channel;

    private TheMessages.TheResponse resp;
    private final BlockingQueue<TheMessages.TheResponse> resps = new LinkedBlockingQueue<TheMessages.TheResponse>();

    public ClientHandler(CallBackInterface onMessageReceiedCallback) {
        this.onMessageReceiedCallback = onMessageReceiedCallback;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TheMessages.TheResponse msg) throws Exception {
        System.out.println(msg.toString());
        resps.add(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public TheMessages.TheResponse sendRequest(TheMessages.Type type, Path p) {

        TheMessages.TheRequest req = null;
        // send File request
        if (TheMessages.Type.FILE == type) {
            InputStream inputStream = null;
            try {
                inputStream = getClass().getResourceAsStream("f:/components.png");

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

}

