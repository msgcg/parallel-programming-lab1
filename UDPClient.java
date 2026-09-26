import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPClient {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            InetAddress address = InetAddress.getByName("127.0.0.1");
            int port = 1501;

            System.out.println("=================================================");
            System.out.println("  UDP-КЛИЕНТ ЗАПУЩЕН");
            System.out.println("  Подключение к серверу " + address.getHostAddress() + ":" + port);
            System.out.println("=================================================");

            while (true) {
                socket = new DatagramSocket();
                System.out.print("\nВведите текст запроса для сервера: ");
                String sendText = br.readLine();
                if (sendText == null || sendText.trim().isEmpty()) {
                    sendText = "Тестовый запрос по протоколу UDP";
                }

                byte[] sendBuffer = sendText.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
                socket.send(sendPacket);
                System.out.println("[КЛИЕНТ: Отправлен пакет (" + sendBuffer.length + " байт)]");

                byte[] recvBuffer = new byte[512];
                DatagramPacket recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
                socket.receive(recvPacket);

                String receivedStr = new String(recvPacket.getData(), 0, recvPacket.getLength(), StandardCharsets.UTF_8);
                System.out.println("[КЛИЕНТ: Получен ответ от сервера]: " + receivedStr.trim());

                System.out.print("Продолжить обмен данными? (Yes/No): ");
                String answer = br.readLine();
                if (answer != null && answer.trim().equalsIgnoreCase("No")) {
                    System.out.println("Завершение сеанса клиентом.");
                    // send shutdown signal to server for clean exit
                    byte[] shutBuffer = "SHUTDOWN".getBytes(StandardCharsets.UTF_8);
                    DatagramPacket shutPacket = new DatagramPacket(shutBuffer, shutBuffer.length, address, port);
                    socket.send(shutPacket);
                    socket.close();
                    break;
                }
                socket.close();
            }
        } catch (Exception e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
