package ru.runov.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class MainChHandler //extends ChannelInboundHandlerAdapter {
                            extends SimpleChannelInboundHandler<String> {
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

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s);
        if (s.startsWith("/")) {
            if (s.startsWith("/getdir")) {  // get dir hierarchy from srv (in xml)
                String commsnd = s.split("\\s", 2)[1];
                channels.get(clientIndex-1-1).writeAndFlush(commsnd);
                return;
            }
        }
        String out = "rep " + s;
        channels.get(clientIndex-1-1).writeAndFlush(out);
    }


  /*  @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        while (buf.readableBytes() > 0) {
            System.out.print((char)buf.readByte());
        }
        buf.release();
        //super.channelRead(ctx, msg);
    }  */


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        channels.remove(ctx.channel());
        ctx.close();
    }

}
