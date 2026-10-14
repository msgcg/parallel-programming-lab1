@echo off

chcp 65001 > nul

setlocal

echo ====================================================================

echo   ВАНКЛИК ЗАПУСК: ЛАБОРАТОРНАЯ РАБОТА №1

echo   Сетевой UDP-сервер и параллельные клиенты

echo   Автор: Эрденко М. Д. (ПО-32б) -- Преподаватель: Степаков В. И.

echo ====================================================================

echo.

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


echo [1/2] Компиляция всех исходных файлов...

"%JAVAC_BIN%" -encoding UTF-8 UDPServer.java UDPClient.java MultiClientTest.java OneClickLab1.java

if %errorlevel% neq 0 (

echo [ОШИБКА] Ошибка компиляции!

pause

exit /b 1

)

echo.

echo [2/2] Запуск комплексной демонстрации...

echo --------------------------------------------------------------------

"%JAVA_BIN%" -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 OneClickLab1

echo.

echo ====================================================================

echo   ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА!

echo ====================================================================

pause


