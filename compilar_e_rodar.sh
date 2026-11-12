#!/bin/bash
# Script para compilar e rodar o sistema supervisório SENAI
# Uso: bash compilar_e_rodar.sh

echo "=== Sistema Supervisório SENAI ==="
echo ""

# Cria pasta de saída para os .class
mkdir -p out

# Compila todos os arquivos .java da pasta src
echo "[1/2] Compilando..."
javac -d out src/*.java

if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilação. Verifique o Java JDK instalado."
    exit 1
fi

echo "[2/2] Iniciando servidor..."
echo ""

# Roda o servidor
java -cp out Servidor
