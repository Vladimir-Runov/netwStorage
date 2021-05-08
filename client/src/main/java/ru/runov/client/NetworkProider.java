package ru.runov.client;
import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;


public class NetworkProider {
    private SocketChannel xchannel;
//    private static final String ADDRESS = "localhost";
//    private static final int PORT = 8189;
    private static final String HOST = System.getProperty("host", "127.0.0.1");
    private static final int PORT = Integer.parseInt(System.getProperty("port", "8189"));

    private CallBackInterface onMessageReceiedCallback;

    public NetworkProider(CallBackInterface onMessageReceiedCallback) {

        this.onMessageReceiedCallback = onMessageReceiedCallback;

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
                                 new ClientHandler());  // onMessageReceiedCallback
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
             }
         });
        th.setDaemon(true);
        th.start();
    }
    public void sendCommand(String msg) {
        xchannel.writeAndFlush(msg);
    }
}
