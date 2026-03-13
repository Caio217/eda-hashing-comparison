#!/bin/bash
echo "⚙️ Passo 1: Compilando o projeto..."
./gradlew clean jar

echo "🔥 Passo 2: Iniciando o Benchmark com os dados existentes..."
java -jar build/libs/*.jar