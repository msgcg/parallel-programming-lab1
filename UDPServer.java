import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class UDPServer {
    private DatagramSocket socket;
    private BufferedReader in;
    private volatile boolean running = true;

    public UDPServer(int port) {
        try {
            socket = new DatagramSocket(port);
            socket.setSoTimeout(3000); // 3 sec timeout for non-blocking shutdown check
            System.out.println("=================================================");
            System.out.println("  UDP-СЕРВЕР ЗАПУЩЕН НА ПОРТУ: " + port);
            System.out.println("  Ожидание входящих датаграмм от клиентов...");
            System.out.println("=================================================");
            call();
        } catch (Exception e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Сервер успешно остановлен.");
        }
    }

    public void call() {
        try {
            in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            byte[] buffer = new byte[512];

            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                } catch (SocketTimeoutException e) {
                    // Check if stdin has anything or continue listening
                    continue;
                }

                String clientMsg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                System.out.println("\n[СЕРВЕР: Входящая датаграмма от " + clientAddress.getHostAddress() + ":" + clientPort + "]");
                System.out.println("Содержимое запроса: \"" + clientMsg + "\"");

                if (clientMsg.equalsIgnoreCase("SHUTDOWN")) {
                    System.out.println("Получен сигнал завершения работы сервера.");
                    break;
                }

                System.out.print("Введите строку ответа клиенту: ");
                String responseStr = in.readLine();
                if (responseStr == null) {
                    responseStr = "Ответ сервера по умолчанию: OK";
                }

                byte[] sendBuffer = responseStr.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                socket.send(sendPacket);
                System.out.println("[СЕРВЕР: Ответ \"" + responseStr + "\" успешно отправлен]");
            }
        } catch (Exception e) {
            System.err.println("Ошибка в цикле сервера: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = 1501;
        new UDPServer(port);
    }
}
