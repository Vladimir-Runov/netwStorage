package ru.runov.client;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

//  https://examples.javacodegeeks.com/core-java/nio/java-nio-large-file-transfer-tutorial/
// https://docs.jboss.org/netty/3.2/guide/html/index.html
//  https://www.programmersought.com/article/1245432386/
// https://github.com/netty/netty/tree/master/example/src/main/java/io/netty/example/memcache/binary
// https://github.com/netty/netty/blob/master/example/src/main/java/io/netty/example/http/upload/HttpUploadServerHandler.java

public class SimpleChannelFileHandler {}
/*
 ChannelInboundHandler {

    // The number of bytes processed each time
    private int readLength = 8;

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        // send the file sendFile(e.getChannel());
    }

    private void sendFile(Channel channel) throws IOException {
        File file = new File("F:/test/Serv/1.txt");
        FileInputStream fis = new FileInputStream(file);
        int count = 0;
        for (;;) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] bytes = new byte[readLength];
            int readNum = bis.read(bytes, 0, readLength);
            if (readNum == -1) {
                return;
            }
            sendToServer(bytes, channel, readNum);
            System.out.println("Send count: " + ++count);
        }
    }

    private void sendToServer(byte[] bytes, Channel channel, int length) throws IOException {
//        ChannelBuffer buffer = ChannelBuffers.copiedBuffer(bytes, 0, length);
  //      channel.write(buffer);
    }

    private static final int BLOCKSIZE = 456;
    private void Send2(String inputFileName )throws Exception {
        byte[] bytes = new byte[BLOCKSIZE];
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(65536, 4, 4, -8, 0),
                );
        FileInputStream fis = new FileInputStream(new File(inputFileName));
        int bytesRead = 0;
        while ((bytesRead = fis.read(bytes)) != -1) {
            ByteBuf buf = Unpooled.wrappedBuffer(bytes, 0, bytesRead);
            channel.writeInbound(buf);
        }
        channel.flush();
    }

}
*/