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

![image](https://user-images.githubusercontent.com/13151962/209081633-c5b2a222-7a23-434c-99a3-1712e61fb343.png)


![image](https://user-images.githubusercontent.com/13151962/209081873-5729dc43-6848-4dbb-b613-379ad0c134e3.png)


![image](https://user-images.githubusercontent.com/13151962/209081952-4b9a5006-1b49-4293-bf29-55f02cb6efee.png)


![image](https://user-images.githubusercontent.com/13151962/209082221-be3192ae-11d7-44d7-b693-32e83418952c.png)

### Пример POST для детейла

![image](https://user-images.githubusercontent.com/13151962/209082409-aa426f01-ac34-4913-a5f6-823edab5484c.png)

### Запрос на выборку комментариев в диапазоне дат

![image](https://user-images.githubusercontent.com/13151962/209084489-e4765321-3fc9-4784-8080-0bb39c43a3e4.png)
![image](https://user-images.githubusercontent.com/13151962/209084537-b25fbad7-0fc5-446d-b43c-4fd23d38635c.png)

### Запрос с фильтром

![image](https://user-images.githubusercontent.com/13151962/209091994-9933c37d-6ad5-478c-8c62-ebac30aef858.png)

### Примеры запросов для работы с файлами

Загрузить файл:
```console
curl --location --request POST 'http://localhost:8080/files/' \
--form 'file=@"/home/user/test.txt"'
```

Получить список файлов:
```console
curl --location --request GET 'http://localhost:8080/files/'
```

Скачать указанный файл:
```console
curl --location --request GET 'http://localhost:8080/files/test.txt'
```
