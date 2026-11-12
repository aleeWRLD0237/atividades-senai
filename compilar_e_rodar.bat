@echo off
echo === Sistema Supervisorio SENAI ===
echo.

:: Cria pasta de saida
if not exist out mkdir out

:: Compila
echo [1/2] Compilando...
javac -d out src\*.java

if errorlevel 1 (
    echo ERRO: Falha na compilacao. Verifique o Java JDK instalado.
    pause
    exit /b 1
)

:: Roda
echo [2/2] Iniciando servidor...
echo.
java -cp out Servidor
pause
