#!/bin/bash

echo "🚀 Passo 1: Gerando os arquivos de entrada..."
./gradlew gerarDados

echo "⚙️ Passo 2: Compilando o projeto..."
./gradlew clean jar

echo "🔥 Passo 3: Iniciando o Benchmark..."
java -jar build/libs/*.jar