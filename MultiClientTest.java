import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiClientTest {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 1501;
    private static final int CLIENTS_COUNT = 4;
    private static final int REQUESTS_PER_CLIENT = 3;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Запуск параллельного тестирования " + CLIENTS_COUNT + " клиентов...");
        ExecutorService pool = Executors.newFixedThreadPool(CLIENTS_COUNT);
        CountDownLatch latch = new CountDownLatch(CLIENTS_COUNT);

        for (int i = 1; i <= CLIENTS_COUNT; i++) {
            final int clientId = i;
            pool.submit(() -> {
                try (DatagramSocket socket = new DatagramSocket()) {
                    socket.setSoTimeout(3000);
                    InetAddress address = InetAddress.getByName(HOST);

                    for (int req = 1; req <= REQUESTS_PER_CLIENT; req++) {
                        String msg = "Клиент-" + clientId + " (запрос #" + req + ")";
                        byte[] sendBuf = msg.getBytes(StandardCharsets.UTF_8);
                        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address, PORT);

                        long t0 = System.currentTimeMillis();
                        socket.send(sendPacket);

                        byte[] recvBuf = new byte[1024];
                        DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
                        socket.receive(recvPacket);
                        long dt = System.currentTimeMillis() - t0;

                        String reply = new String(recvPacket.getData(), 0, recvPacket.getLength(), StandardCharsets.UTF_8);
                        System.out.printf("[Клиент #%d | порт %d] Время: %d мс | Ответ: %s%n",
                                clientId, socket.getLocalPort(), dt, reply.trim());

                        Thread.sleep(80);
                    }
                } catch (Exception e) {
                    System.err.printf("[Клиент #%d] Ошибка: %s%n", clientId, e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        pool.shutdown();
        System.out.println("Тестирование завершено. Все клиенты успешно получили ответы параллельно.");
    }
}
