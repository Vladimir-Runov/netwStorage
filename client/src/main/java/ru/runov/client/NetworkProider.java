package ru.runov.client;
import com.google.protobuf.ByteString;
import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.io.*;
import java.nio.file.Path;


public class NetworkProider {
    private static final String HOST = System.getProperty("host", "127.0.0.1");             //    private static final String ADDRESS = "localhost";
    private static final int PORT = Integer.parseInt(System.getProperty("port", "8189"));  //    private static final int PORT = 8189;

    private SocketChannel xchannel;

    public NetworkProider(CallBackInterface onMessageReceiedCallback) {
        Thread th = new Thread(()->{
             EventLoopGroup elgWorker = new NioEventLoopGroup();
             try {
                 Bootstrap bs = new io.netty.bootstrap.Bootstrap();
                 bs.group(elgWorker).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel socketChannel) throws Exception {
                         xchannel = socketChannel;
                         socketChannel.pipeline().addLast(
                                 new ProtobufVarint32FrameDecoder(),
                                 new ProtobufDecoder(TheMessages.TheResponse.getDefaultInstance()),

                                 new ProtobufVarint32LengthFieldPrepender(),
                                 new ProtobufEncoder(),
                                 new ClientHandler(onMessageReceiedCallback));  // onMessageReceiedCallback
                             //    new StringDecoder(),
                             //    new StringEncoder(),
                             //    new ClientMsgHandlerString(onMessageReceiedCallback));
                     }
                 });

                ChannelFuture cf = bs.connect(HOST, PORT).sync();
                cf.channel().closeFuture().sync();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             finally {
                 elgWorker.shutdownGracefully();
                 System.out.println("fin");
             }
         });
        th.setDaemon(true);
        th.start();
    }
    public void uploadFile(Path p) {
        System.out.println("x-> " + p.toFile().toString());
        TheMessages.Type type = TheMessages.Type.FILE;
        // xchannel.writeAndFlush(TheMessages.Type.FILE, p);
        TheMessages.TheRequest req = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(p.toFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // BufferedInputStream
        InputStream inputStream = new BufferedInputStream(fis);

        // send File request
        if (TheMessages.Type.FILE == type) {
           // InputStream inputStream = null;
            try {
               // inputStream = getClass().getResourceAsStream(p.toFile().toString());

                TheMessages.FileMsg fileMsg = TheMessages.FileMsg.newBuilder()
                        .setFileBytes(ByteString.readFrom(inputStream))
                        .setFilename(p.toFile().getName())
                        .build();
                req = TheMessages.TheRequest.newBuilder()
                        .setType(TheMessages.Type.FILE)
                        .setFile(fileMsg)
                        .build();
                // Send request
                xchannel.writeAndFlush(req);
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
        xchannel.writeAndFlush(req);

        // Now wait for response from server
//        boolean interrupted = false;
//        for (; ; ) {
//            try {
//                resp = resps.take();
//                break;
//            } catch (InterruptedException ignore) {
//                interrupted = true;
//            }
//        }
//        if (interrupted) {
//            Thread.currentThread().interrupt();
//        }
//        return resp;
        return;
    }

    //   public void sendCommand(String msg) {
    //        System.out.println("sendCommand");
    //        xchannel.writeAndFlush(msg);
    //    }
}
