# Синхронизация Elasticsearch с базой данных PostgreSQL

Синхронизация Elasticsearch с базой данных PostgreSQL происходит с помощью сервисов PGSync и Redis.

## PGSync

PGSync позволяет использовать Postgres в качестве источника достоверной информации и предоставлять структурированные денормализованные документы в Elasticsearch.

Каждая выбранная таблица из базы Postgres будет попадать в Elasticsearch как отдельный индекс (раздел доукментов). На этом шаге каждая запись таблицы превращается в денормализированный документ.

## Redis

Redis (Remote Dictionary Service) — это опенсорсный сервер баз данных типа ключ-значение.

Redis это — сервер структур данных, база данных, размещаемая в памяти, которая используется, в основном, в роли кеша, находящегося перед другой, «настоящей» базой данных, вроде MySQL или PostgreSQL.

## Настройка Docker

Для начала настраиваем Redis, для этого надо поднять этот сервис без дополнительных настроек кроме порта.

```yaml
redis:
    image: springbootsample/redis
    ports:
        - '6379:6379'
```
Dockerfile:
```dockerfile
FROM redis:alpine
```

Затем настраиваем PGSync:
```yaml
pgsync:
    image: springbootsample/pgsync
    environment:
    - PG_USER=flexberryuser
    - PG_PASSWORD=jhv
    - PG_DATABASE=appdb
    - PG_HOST=springboot-postgres-db
    - ELASTICSEARCH_HOST=springboot-elasticsearch
    - REDIS_HOST=redis
```

- **PG_HOST** - адрес сервера Postgres;
- **PG_DATABASE** - имя базы Postgres;
- **PG_USER** - имя пользователя для входа в БД Postgres;
- **PG_PASSWORD** - пароль пользователя для входа в БД Postgres;
- **ELASTICSEARCH_HOST** - адрес сервера Elasticsearch;
- **REDIS_HOST** - адрес сервера Redis.

Dockerfile:
```dockerfile
FROM python:3.7-slim

WORKDIR /usr/src/app

COPY ./PGSync ./

RUN chmod +x ./entrypoint.sh

RUN pip install pgsync

RUN apt update \
    && apt install -y moreutils \
    && apt install -y jq \
    && apt install -y wait-for-it

ENTRYPOINT ["bash", "./entrypoint.sh"]
```

- Сначала копируются файлы **entrypoint.sh** и **schema.json** в рабочий каталог **/usr/src/app**.
- Устанавливается **pgsync**.
- Затем настройка происходит с помощью python-скрипта **entrypoint.sh**.

```bash
#!/usr/bin/env bash

wait-for-it $PG_HOST:5432 -t 60
wait-for-it $REDIS_HOST:6379 -t 60
wait-for-it $ELASTICSEARCH_HOST:9200 -t 60

jq '.[].database = env.PG_DATABASE' schema.json | sponge schema.json

bootstrap --config ./schema.json
pgsync --config ./schema.json -d
```
- Ждем пока поднимутся сервисы Postgres, Redis и Elasticsearch.
- Если имя БД совпадает с именем БД в настройках, то применяем их.

Файл настроек синхронизации данных PGSync:
```json
[
    {
      "database": "appdb",
      "index": "testentity",
      "nodes": {
        "table": "testentity",
        "schema": "public",
        "columns": [
          "primarykey", 
          "name", 
          "points", 
          "enabled"
        ]
      }
    },
    
    ...

    {
      "database": "appdb",
      "index": "comment",
      "nodes": {
        "table": "comment",
        "schema": "public",
        "columns": [
          "primarykey", 
          "commentdate", 
          "commenttext"
        ]
      }
    }
  ]
```

- **database** - имя БД Postgres.
- **index** - имя индекса для документов этого типа в Elasticsearch.
- **table** - имя таблицы в БД Postgres.
- **schema** - имя схемы для таблицы.
- **columns** - перечень колонок для синхронизации с Elasticsearch. Если указать пустой массив **[]**, то будут браться все клолнки таблицы.

[Примеры настроек для PGSync](https://pgsync.com/schema/)