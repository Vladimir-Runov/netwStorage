package ru.runov.server;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import ru.runov.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.FileOutputStream;
import java.io.IOException;

// https://github.com/netty/netty/tree/4.1/example/src/main/java/io/netty/example

public class ProtocolServerHandler //implements ChannelHandler {
                                     extends SimpleChannelInboundHandler<TheMessages.TheRequest> {

    private static final String FILE_DIR = "/tmp/";

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //关闭channel
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TheMessages.TheRequest theRequest) throws Exception {
        if (msg.getType() == TheMessages.Type.MSG) {
            TheMessages.TheResponse.Builder builder = TheMessages.TheResponse.newBuilder();
            String message = "Accepted from Server, returning response";
            System.out.println(message);
            builder.setResponseMsg(message)
                    .setCode(0);
            ctx.write(builder.build());
        } else if (msg.getType() == TheMessages.Type.FILE) {

            byte[] bFile = msg.getFile().toByteArray();
            FileOutputStream fileOuputStream = null;
            try {
                fileOuputStream = new FileOutputStream(FILE_DIR + msg.getFile().getFilename());
                fileOuputStream.write(bFile);
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                try {
                    if (fileOuputStream != null) {
                        fileOuputStream.close();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            TheMessages.TheResponse.Builder builder = TheMessages.TheResponse.newBuilder();
            String message = "File saved to: " + FILE_DIR;
            System.out.println(message);
            builder.setResponseMsg(message)
                    .setCode(0);
            ctx.write(builder.build());
        } else {
            TheMessages.TheResponse.Builder builder = TheMessages.TheResponse.newBuilder();
            String message = "Unsupported message type " + msg.getType();
            System.out.println(message);
            builder.setResponseMsg(message)
                    .setCode(1);
            ctx.write(builder.build());
        }if (msg.getType() == TheMessages.Type.MSG) {
            TheMessages.TheResponse.Builder builder = TheMessages.TheResponse.newBuilder();
            String message = "Accepted from Server, returning response";
            System.out.println(message);
            builder.setResponseMsg(message)
                    .setCode(0);
            ctx.write(builder.build());
        } else if (msg.getType() == TheMessages.Type.FILE) {

            byte[] bFile = msg.getFile().toByteArray();
            FileOutputStream fileOuputStream = null;
            try {
                fileOuputStream = new FileOutputStream(FILE_DIR + msg.getFile().getFilename());
                fileOuputStream.write(bFile);
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                try {
                    if (fileOuputStream != null) {
                        fileOuputStream.close();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            TheMessages.TheResponse.Builder builder = TheMessages.TheResponse.newBuilder();
            String message = "File saved to: " + FILE_DIR;
            System.out.println(message);
            builder.setResponseMsg(message)
                    .setCode(0);
            ctx.write(builder.build());
        } else {
            TheMessages.TheResponse.Builder builder = TheMessages.TheResponse.newBuilder();
            String message = "Unsupported message type " + msg.getType();
            System.out.println(message);
            builder.setResponseMsg(message)
                    .setCode(1);
            ctx.write(builder.build());
        }
    }
}
