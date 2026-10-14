@echo off

chcp 65001 > nul

setlocal

echo ====================================================================

echo   АВТОМАТИЧЕСКИЙ ТЕСТ МУЛЬТИКЛИЕНТСКОГО UDP-СЕРВЕРА (JAVA)

echo   Лабораторная работа №1 по дисциплине «Параллельное программирование»

echo   Автор: Эрденко М. Д. (ПО-32б) -- Преподаватель: Степаков В. И.

echo ====================================================================

echo.

echo Внимание: Перед запуском данного теста в отдельном окне должен

echo быть запущен сервер через "2_Запуск_Сервера.bat".

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


if not exist "MultiClientTest.class" (

echo [ИНФО] Файл MultiClientTest.class не найден. Запуск компиляции...

call "1_Скомпилировать.bat"

)

echo Запуск MultiClientTest (4 параллельных клиента отправляют запросы)...

echo --------------------------------------------------------------------

"%JAVA_BIN%" -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 MultiClientTest

echo.

echo [ИНФО] Тестирование завершено.

pause


