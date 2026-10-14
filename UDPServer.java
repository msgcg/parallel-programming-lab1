import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class UDPServer {
    private static final int PORT = 1501;
    private static final int BUFFER_SIZE = 1024;
    private static final int THREAD_POOL_SIZE = 8;

    private final DatagramSocket socket;
    private final ExecutorService executor;
    private volatile boolean running = true;
    private final AtomicInteger requestCounter = new AtomicInteger(0);

    public UDPServer(int port) throws Exception {
        this.socket = new DatagramSocket(port);
        this.executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void start() {
        System.out.println("UDP-сервер запущен на порту " + PORT);
        System.out.println("Пул рабочих потоков: " + THREAD_POOL_SIZE);
        System.out.println("Ожидание входящих пакетов от клиентов...");

        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                byte[] data = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                executor.submit(() -> handleRequest(data, clientAddress, clientPort));
            }
        } catch (Exception e) {
            if (running) {
                System.err.println("Ошибка сервера: " + e.getMessage());
            }
        } finally {
            stop();
        }
    }

    private void handleRequest(byte[] data, InetAddress address, int port) {
        int reqId = requestCounter.incrementAndGet();
        String threadName = Thread.currentThread().getName();
        String message = new String(data, StandardCharsets.UTF_8).trim();

        System.out.printf("[%s] Получен запрос #%d от %s:%d: \"%s\"%n",
                threadName, reqId, address.getHostAddress(), port, message);

        String timeStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        String responseText = String.format("Ответ сервера [%s, поток: %s, запрос: #%d]: Принято \"%s\"",
                timeStr, threadName, reqId, message);

        try {
            byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
            DatagramPacket replyPacket = new DatagramPacket(responseBytes, responseBytes.length, address, port);
            synchronized (socket) {
                if (!socket.isClosed()) {
                    socket.send(replyPacket);
                }
            }
            System.out.printf("[%s] Отправлен ответ клиенту %s:%d%n", threadName, address.getHostAddress(), port);
        } catch (Exception e) {
            System.err.printf("[%s] Ошибка отправки ответа: %s%n", threadName, e.getMessage());
        }
    }

    public void stop() {
        running = false;
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        System.out.println("Сервер остановлен.");
    }

    public static void main(String[] args) throws Exception {
        UDPServer server = new UDPServer(PORT);
        server.start();
    }
}
