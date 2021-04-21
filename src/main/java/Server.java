import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class Server {
    final static int PORT = 20000;

    public static void main(String[] args) throws IOException {
        startServer();
    }

    static void startServer() throws IOException {
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));


        while (true) {
            try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1) break;

                    String msgTo = "";
                    final String msgFrom = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    System.out.println("Получено от клиента: " + msgFrom);
                    msgTo = "Результат: " + msgFrom.trim().replace(" ", "");

                    socketChannel.write(ByteBuffer.wrap((msgTo).getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

}
