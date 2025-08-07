#!/bin/bash

# Variáveis
CONTAINER_NAME=postgres-transfer
POSTGRES_DB=transferdb
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin123
POSTGRES_PORT=5432

# Verifica se o container já existe
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    echo "Parando e removendo o container existente..."
    docker stop $CONTAINER_NAME
    docker rm $CONTAINER_NAME
fi

# Rodando o PostgreSQL
echo "Iniciando o PostgreSQL no Docker..."
docker run --name $CONTAINER_NAME \
  -e POSTGRES_DB=$POSTGRES_DB \
  -e POSTGRES_USER=$POSTGRES_USER \
  -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
  -p $POSTGRES_PORT:5432 \
  -d postgres:15

echo "PostgreSQL iniciado com sucesso!"
echo "Banco de dados: $POSTGRES_DB"
echo "Usuário: $POSTGRES_USER"
echo "Senha: $POSTGRES_PASSWORD"
