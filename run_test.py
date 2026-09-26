import subprocess
import time

server_proc = subprocess.Popen(
    ["java", "UDPServer"],
    stdin=subprocess.PIPE,
    stdout=subprocess.PIPE,
    stderr=subprocess.PIPE,
    text=True,
    cwd="/home/maxim/Лабы/pp_labs/lab1"
)

time.sleep(1)

client_proc = subprocess.Popen(
    ["java", "UDPClient"],
    stdin=subprocess.PIPE,
    stdout=subprocess.PIPE,
    stderr=subprocess.PIPE,
    text=True,
    cwd="/home/maxim/Лабы/pp_labs/lab1"
)

# Interaction 1
time.sleep(0.5)
client_proc.stdin.write("Запрос №1: Проверка передачи UDP датаграмм (Эрденко М. Д., Разгуляев Е. О.)\n")
client_proc.stdin.flush()
time.sleep(0.5)

server_proc.stdin.write("Ответ №1: Датаграмма успешно принята сервером на порту 1501. Статус OK.\n")
server_proc.stdin.flush()
time.sleep(0.5)

client_proc.stdin.write("Yes\n")
client_proc.stdin.flush()
time.sleep(0.5)

# Interaction 2
client_proc.stdin.write("Запрос №2: Запрос параметров сетевого взаимодействия\n")
client_proc.stdin.flush()
time.sleep(0.5)

server_proc.stdin.write("Ответ №2: Сервер функционирует в штатном режиме, буфер 512 байт.\n")
server_proc.stdin.flush()
time.sleep(0.5)

client_proc.stdin.write("No\n")
client_proc.stdin.flush()
time.sleep(1)

s_out, s_err = server_proc.communicate(timeout=5)
c_out, c_err = client_proc.communicate(timeout=5)

print("=== SERVER OUTPUT ===")
print(s_out)
print("=== CLIENT OUTPUT ===")
print(c_out)

with open("/home/maxim/Лабы/pp_labs/lab1/server_log.txt", "w", encoding="utf-8") as f:
    f.write(s_out)

with open("/home/maxim/Лабы/pp_labs/lab1/client_log.txt", "w", encoding="utf-8") as f:
    f.write(c_out)

