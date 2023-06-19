Java бэкенд на основе фреймворка Spring Boot
Проект получен с помощью сервиса https://start.spring.io/
Для работы с проектом рекомендуется использовать IntelliJ IDEA не ниже версии 2022.1

Возможности, реализованные на данный момент:

1) Работа с БД Postgres. Для примера созданы сущности Customer и Comment.
2) Выполнение простых REST запросов к сущностям - GET, POST, PUT, DELETE.
3) Связи между сущностями. Comment является детейлом Customer. Решена цикличность запроса Детейл-Мастер-Детейл-Мастер....
4) Аудит операций на основе Envers. Для каждой таблицы автоматически создается таблица аудита в этой же БД + одна общая служебная таблица.
5) Сложный запрос на основе нескольких параметров - выборка комментариев в заданном периоде времени.
6) Система логировани с возможностью назначать свои тексты на разные типы ошибок.
7) Работа с файлами - Загрузка/Выгрузка.
8) Реализация фильтра по нескольким параметрам. Число параметров модели для фильтра любое, но в качестве примера реализован только тип сравнения "="(equal).
9) Запуск в Docker.

### Запуск в Docker

Для запуска бэкенда в Docker необходимо выполнить

`Flexberry.SpringReactApplication.Sample\Docker\build.sh`

`Flexberry.SpringReactApplication.Sample\Docker\run.cmd`

Для остановки контейнера

`Flexberry.SpringReactApplication.Sample\Docker\stop.cmd`

### Примеры запросов

Запросы выполняются в Postman

![image](https://github.com/Flexberry/Flexberry.SpringBootBackend.Sample/assets/13151962/d9420b10-e793-4e33-b533-219641a0073c)


![image](https://github.com/Flexberry/Flexberry.SpringBootBackend.Sample/assets/13151962/f099463b-aaf2-4885-a6de-8aa7ab070e1e)


![image](https://github.com/Flexberry/Flexberry.SpringBootBackend.Sample/assets/13151962/e792c018-18e4-4abc-957c-955c64c4e18d)

![image](https://github.com/Flexberry/Flexberry.SpringBootBackend.Sample/assets/13151962/38486722-c805-48b6-be35-f4aab95aef13)

![image](https://github.com/Flexberry/Flexberry.SpringBootBackend.Sample/assets/13151962/d9b4cb05-5a2f-49fd-b612-293a9d1da766)


### Пример POST для детейла

![image](https://github.com/Flexberry/Flexberry.SpringBootBackend.Sample/assets/13151962/52517cfc-1b80-4210-b920-d7bcf069fa29)

### Запрос на выборку комментариев в диапазоне дат

![image](https://github.com/Flexberry/Flexberry.SpringBootBackend.Sample/assets/13151962/0ac18d03-f9ea-499d-bf59-d40141d04a91)

![image](https://github.com/Flexberry/Flexberry.SpringBootBackend.Sample/assets/13151962/7aa7b2fe-9732-4c1a-99e3-819c91022bd5)

### Запрос с фильтром

![image](https://github.com/Flexberry/Flexberry.SpringBootBackend.Sample/assets/13151962/0d7fe4e6-8623-4096-a11c-1f11dee83267)

### Примеры запросов для работы с файлами

Загрузить файл:

![PostFile](docs/images/filePost.jpg)

Получить список файлов:

![GetFiles](docs/images/fileGet.jpg)

Скачать указанный файл:

![GetFilesByName](docs/images/fileGetByName.jpg)

### Сервис документации Swagger

localhost:8081/service/docs
