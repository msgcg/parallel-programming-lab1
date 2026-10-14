@echo off

chcp 65001 > nul

setlocal

echo ====================================================================

echo   ИНТЕРАКТИВНЫЙ КЛИЕНТ UDP (КОМАНДНЫЙ РЕЖИМ)

echo   Лабораторная работа №1 по дисциплине «Параллельное программирование»

echo   Автор: Эрденко М. Д. (ПО-32б) -- Преподаватель: Степаков В. И.

echo ====================================================================

echo.

echo Подключение к серверу 127.0.0.1:1501...

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


if not exist "UDPClient.class" (

echo [ИНФО] Файл UDPClient.class не найден. Запуск компиляции...

call "1_Скомпилировать.bat"

)

echo Запуск UDPClient...

echo --------------------------------------------------------------------

"%JAVA_BIN%" -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 UDPClient 127.0.0.1 1501

echo.

echo [ИНФО] Сессия клиента завершена.

pause


