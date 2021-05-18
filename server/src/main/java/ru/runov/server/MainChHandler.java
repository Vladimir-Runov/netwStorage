package ru.runov.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import ru.runov.*;

public class MainChHandler {}

    //extends ChannelInboundHandlerAdapter {
                        //    extends SimpleChannelInboundHandler<String> {
                        //  extends SimpleChannelHandler {

 //       // The number of bytes processed each time
 //       private int readLength = 8;
 //       @Override
 //       public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
 //           // send the file sendFile(e.getChannel());
 //
//        private void sendToServer(byte[] bytes, Channel channel, int length) throws IOException {
//            ChannelBuffer buffer = ChannelBuffers.copiedBuffer(bytes, 0, length);
//            channel.write(buffer);
//        }
/*    }

    private static final List<Channel> channels = new ArrayList<>();
    private static int clientIndex = 1;
    private String name;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
        System.out.println("new cloent " + ctx);
        channels.add(ctx.channel());
        name = "@" + clientIndex;
        clientIndex++;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

 //   @Override
    protected void _channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
 /*       System.out.println(msg);
        if (msg.startsWith("/")) {
            if (msg.startsWith("/getdir")) {  // get dir hierarchy from srv (in xml)
                String commsnd = msg.split("\\s", 2)[1];
                channels.get(clientIndex-1-1).writeAndFlush(commsnd);
                return;
            }
        }
        String out = "rep " + s;
        channels.get(clientIndex-1-1).writeAndFlush(out);  *

        RandomAccessFile raf = null;
        long length = -1;
        try {
            raf = new RandomAccessFile(msg, "r");
            length = raf.length();
        } catch (Exception e) {
            ctx.writeAndFlush("ERR: " + e.getClass().getSimpleName() + ": " + e.getMessage() + '\n');
            return;
        } finally {
            if (length < 0 && raf != null) {
                raf.close();
            }
        }

        ctx.write("OK: " + raf.length() + '\n');
        if (ctx.pipeline().get(SslHandler.class) == null) {
            // SSL not enabled - can use zero-copy file transfer.
            ctx.write(new DefaultFileRegion(raf.getChannel(), 0, length));
        } else {
            // SSL enabled - cannot use zero-copy file transfer.
            ctx.write(new ChunkedFile(raf));
        }
        ctx.writeAndFlush("\n");
    }

  /*  @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        while (buf.readableBytes() > 0) {
            System.out.print((char)buf.readByte());
        }
        buf.release();
        //super.channelRead(ctx, msg);
    }  *


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // super.exceptionCaught(ctx, cause);
        cause.printStackTrace();

        if (ctx.channel().isActive()) {
            ctx.writeAndFlush("ERR: " + cause.getClass().getSimpleName() + ": " + cause.getMessage() + '\n').addListener(ChannelFutureListener.CLOSE);
        }
        channels.remove(ctx.channel());
        ctx.close();
    }

}
*/