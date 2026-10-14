import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 1501;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

            socket.setSoTimeout(5000);
            InetAddress address = InetAddress.getByName(HOST);

            System.out.println("UDP-клиент запущен.");
            System.out.printf("Подключение к серверу %s:%d (локальный порт клиента: %d)%n", HOST, PORT, socket.getLocalPort());

            while (true) {
                System.out.print("\nВведите строку для отправки на сервер: ");
                String message = reader.readLine();
                if (message == null || message.trim().isEmpty()) {
                    message = "Тестовое сообщение от клиента";
                }

                byte[] sendBuffer = message.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, PORT);
                socket.send(sendPacket);
                System.out.println("Запрос отправлен на сервер.");

                byte[] recvBuffer = new byte[BUFFER_SIZE];
                DatagramPacket recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
                try {
                    socket.receive(recvPacket);
                    String receivedStr = new String(recvPacket.getData(), 0, recvPacket.getLength(), StandardCharsets.UTF_8);
                    System.out.println("Принятое сообщение : " + receivedStr.trim());
                } catch (Exception e) {
                    System.out.println("Ошибка ожидания ответа: " + e.getMessage());
                }

                System.out.print("Do you want continue (Yes/No) : ");
                String answer = reader.readLine();
                if (answer != null && answer.trim().equalsIgnoreCase("No")) {
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        }
    }
}
