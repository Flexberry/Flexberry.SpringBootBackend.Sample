# Отправка логов из бэкенда Java-приложения в Loki

Для отправки логов из Java-приложения в Loki выбран [Loki4j Logback](https://loki4j.github.io/loki-logback-appender/).

Loki4j – быстрая и легкая реализация Logback-передатчика данных для Loki. Этот проект является неофициальным и поддерживается сообществом.

Последняя версия Loki4j Logback, на момент написания: [v1.4.1](https://github.com/loki4j/loki-logback-appender), требует наличие Java 11 или более поздних версий. Старые версии поддерживают более поздние версии Java.

Для работы с Loki4j Logback необходимо подключить его в зависимостях:

Maven:
```xml
<dependency>
    <groupId>com.github.loki4j</groupId>
    <artifactId>loki-logback-appender</artifactId>
    <version>1.4.1</version>
</dependency>
```


Gradle:
```xml
implementation 'com.github.loki4j:loki-logback-appender:1.4.1'
```


Настройки передатчика для Loki нужно прописать в logback.xml:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/base.xml" />
    <springProperty scope="context" name="appName" source="spring.application.name"/>

    <appender name="LOKI" class="com.github.loki4j.logback.Loki4jAppender">
        <http>
            <url>http://loki:3100/loki/api/v1/push</url>
        </http>
        <format>
            <label>
                <pattern>app=${appName},host=${HOSTNAME},traceID=%X{traceId:-NONE},level=%level</pattern>
            </label>
            <message>
                <pattern>${FILE_LOG_PATTERN}</pattern>
            </message>
            <sortByTime>true</sortByTime>
        </format>
    </appender>

    <root level="INFO">
        <appender-ref ref="LOKI"/>
    </root>
</configuration>
```

где ```http://loki:3100``` – путь к серверу с Loki.

Для отправки логов в Loki необходимо воспользоваться инструментом логирования Logback:

```java
org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FlexberrySampleSpringApplication.class);
String startMessage = "Hello, world!";

logger.info("A INFO Message: {}", startMessage);
logger.warn("A WARN Message: {}", startMessage);
logger.error("A ERROR Message: {}", startMessage);
```

## Источники
1. [Loki4j Configuration](https://loki4j.github.io/loki-logback-appender/docs/configuration)
2. [Logback Project](https://logback.qos.ch/)
3. [Observability with Spring Boot 3](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3)