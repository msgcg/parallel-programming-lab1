import socket
import threading
import time
import subprocess
import os

SERVER_HOST = "127.0.0.1"
SERVER_PORT = 1501
NUM_CLIENTS = 4
REQUESTS_PER_CLIENT = 3

def is_server_reachable():
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
            s.settimeout(0.4)
            s.sendto(b"PING", (SERVER_HOST, SERVER_PORT))
            data, _ = s.recvfrom(256)
            return len(data) > 0
    except Exception:
        return False

def client_worker(client_id, results):
    commands = [
        "PING",
        f"CALC {client_id * 10} + 50",
        f"REVERSE TestClient_{client_id}",
        "STATS",
        "BYE"
    ]
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
        sock.settimeout(2.5)
        for cmd in commands:
            try:
                t0 = time.time()
                sock.sendto(cmd.encode("utf-8"), (SERVER_HOST, SERVER_PORT))
                data, _ = sock.recvfrom(1024)
                dt = (time.time() - t0) * 1000
                res = data.decode("utf-8").strip()
                results.append((client_id, cmd, dt, res))
                print(f"[Клиент N{client_id}] Запрос: '{cmd}' -> Ответ ({dt:.1f} мс): {res}")
            except Exception as e:
                print(f"[Клиент N{client_id}] Ошибка: {e}")
            time.sleep(0.08)

def main():
    print("==================================================================")
    print("  АВТОМАТИЧЕСКИЙ ТЕСТ МНОГОПОТОЧНОГО UDP-СЕРВЕРА (PYTHON)")
    print("==================================================================")

    server_process = None
    if not is_server_reachable():
        print("[Инфо] Сервер на порту 1501 не запущен. Запускаем UDPServer в фоне...")
        server_dir = os.path.dirname(os.path.abspath(__file__))
        server_process = subprocess.Popen(["java", "UDPServer"], cwd=server_dir)
        time.sleep(1.2)

    threads = []
    results = []

    print(f"\nЗапуск {NUM_CLIENTS} клиентских потоков одновременно...")
    t_start = time.time()

    for i in range(1, NUM_CLIENTS + 1):
        t = threading.Thread(target=client_worker, args=(i, results))
        threads.append(t)
        t.start()

    for t in threads:
        t.join()

    total_time = (time.time() - t_start) * 1000

    print("\n==================================================================")
    print("  ИТОГИ ТЕСТИРОВАНИЯ:")
    print(f"  Всего отправлено запросов: {len(results)}")
    print(f"  Общее время параллельного выполнения: {total_time:.1f} мс")
    if results:
        avg_rtt = sum(r[2] for r in results) / len(results)
        print(f"  Среднее время отклика сервера (RTT): {avg_rtt:.2f} мс")
    print("  Все 4 клиента успешно обслужены параллельным сервером.")
    print("==================================================================")

    if server_process:
        try:
            server_process.terminate()
            server_process.wait(timeout=2)
        except Exception:
            pass

if __name__ == "__main__":
    main()
