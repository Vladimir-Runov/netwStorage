import io.netty.channel.Channel;
import javafx.scene.shape.Path;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CannelFileOperation {
    public static void client_sendFile(Channel  channel, Path p, long readLength)  throws IOException {
/*
            File file = new File(p);
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
  */
        }

    private static void sendToServer(byte[] bytes, Channel channel, int length) throws IOException {
      //  ChannelBuffer buffer = ChannelBuffers.copiedBuffer(bytes, 0, length);
      //  channel.write(buffer);
    }

}

