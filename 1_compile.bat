@echo off

chcp 65001 > nul

setlocal

echo ====================================================================

echo   КОМПИЛЯЦИЯ ИСХОДНОГО КОДА: ЛАБОРАТОРНАЯ РАБОТА №1

echo   Сетевой UDP-сервер параллельной обработки клиентов

echo   Автор: Эрденко М. Д. (ПО-32б) -- Преподаватель: Степаков В. И.

echo ====================================================================

set "JAVA_BIN=java"
set "JAVAC_BIN=javac"
if exist "C:\Users\maxxf\AppData\Local\Programs\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe" (
set "JAVA_BIN=C:\Users\maxxf\AppData\Local\Programs\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\java.exe"
set "JAVAC_BIN=C:\Users\maxxf\AppData\Local\Programs\Eclipse Adoptium\jdk-21.0.8.9-hotspot\bin\javac.exe"
)
if exist "C:\Users\m7036\tools\jdk-21\bin\java.exe" (
set "JAVA_BIN=C:\Users\m7036\tools\jdk-21\bin\java.exe"
set "JAVAC_BIN=C:\Users\m7036\tools\jdk-21\bin\javac.exe"
)


echo.

echo Запуск компиляции с кодировкой UTF-8...

"%JAVAC_BIN%" -encoding UTF-8 UDPServer.java UDPClient.java MultiClientTest.java

if %errorlevel% equ 0 (

echo.

echo [УСПЕХ] Все классы успешно скомпилированы:

echo   - UDPServer.class

echo   - UDPClient.class

echo   - MultiClientTest.class

echo.

echo Теперь вы можете запустить:

echo   - "2_Запуск_Сервера.bat" для запуска сервера

echo   - "3_Запуск_Клиента.bat" для ручного ввода команд

echo   - "4_Автотест_Мультиклиент_Java.bat" для проверки параллельности

) else (

echo.

echo [ОШИБКА] Во время компиляции произошла ошибка , код ошибки: %errorlevel%.

)

echo.

pause


