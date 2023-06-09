# Отправка данных в Elasticsearch

Elasticsearch — масштабируемая поисковая система, которую также можно отнести к нереляционным (noSQL) базам данных. Используется для полнотекстового поиска с фильтрами и анализаторами.

В этом проекте Elasticsearch используется для сбора метрик с помощью пакета micrometer, который сам отправляет нужные данные в Elasticsearch.

Elasticsearch использует [JSON REST API](https://www.elastic.co/guide/en/elasticsearch/reference/current/cluster-nodes-stats.html) для работы с данными.

## Подключение Micrometer

Micrometer предоставляет метрический фасад, который отображает метрические данные в независимом от поставщика формате, понятном системе мониторинга.

Micrometer не является частью экосистемы Spring и должен быть добавлен в качестве зависимости.

Файл **pom.xml**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-core</artifactId>
</dependency>

<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-elastic</artifactId>
</dependency>
```

Настраиваем подключение к Elasticsearch, файл **application.properties**
```ini
management.metrics.distribution.percentiles-histogram.http.server.requests=true
management.metrics.distribution.sla.http.server.requests=100ms, 400ms, 500ms, 2000ms
management.metrics.distribution.percentiles.http.server.requests=0.5, 0.9, 0.95, 0.99
management.elastic.metrics.export.host=http://springboot-elasticsearch:9200
management.elastic.metrics.export.index=metricsapp
```
- **management.metrics.distribution.percentiles-histogram**.http.server.requests — публиковать ли процентные диаграммы для данных из раздела **http.server.requests**
- **management.metrics.distribution.sla**.http.server.requests — конкретные целевые границы уровня обслуживания для данных из раздела **http.server.requests**
- **management.metrics.distribution.percentiles**.http.server.requests — конкретные вычисляемые неагрегируемые процентили для отправки в серверную часть для данных из раздела **http.server.requests**
- **management.elastic.metrics.export.host** указывает на путь к сервису относительно бекенда, у нас он внутри докера в одной сети, обращение идет по имени сервиса Elasticsearch.
- **management.elastic.metrics.export.index** — префикс наименования индекса для Elasticsearch, сам индекс будет в имени содержать год и месяц, например: **metricsapp-2023-06**.

[Список метрик Micrometer](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator.metrics)

## Дополнительная настройка Elasticsearch

Для корректноой работы с датами и временными зонами добавляем файл конфигурации **configuration/ElasticsearchConfiguration.java**. Пример настройки был взят из системы **JHipster**.

```java
@Configuration
public class ElasticsearchConfiguration extends ElasticsearchConfigurationSupport {
    @Bean
    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(
                Arrays.asList(
                        new ZonedDateTimeWritingConverter(),
                        new ZonedDateTimeReadingConverter(),
                        new LocalDateWritingConverter(),
                        new LocalDateReadingConverter()
                )
        );
    }

    @WritingConverter
    static class ZonedDateTimeWritingConverter implements Converter<ZonedDateTime, String> {
        @Override
        public String convert(ZonedDateTime source) {
            if (source == null) {
                return null;
            }
            return source.toInstant().toString();
        }
    }

    @ReadingConverter
    static class ZonedDateTimeReadingConverter implements Converter<String, ZonedDateTime> {
        @Override
        public ZonedDateTime convert(String source) {
            if (source == null) {
                return null;
            }
            return Instant.parse(source).atZone(ZoneId.systemDefault());
        }
    }

    @WritingConverter
    static class LocalDateWritingConverter implements Converter<LocalDate, String> {
        @Override
        public String convert(LocalDate source) {
            if (source == null) {
                return null;
            }
            return source.toString();
        }
    }

    @ReadingConverter
    static class LocalDateReadingConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(String source) {
            if (source == null) {
                return null;
            }
            return LocalDate.parse(source);
        }
    }
}
```

## Примеры запросов к REST API Elasticsearch для контроля работоспособности

1. Список метрик с синонимами
```javascript
GET http://localhost:9200/_aliases
```
Ответ:
```json
{
    "metricsapp-2023-06": {
        "aliases": {}
    }
}
```

2. Статистика индексов
```javascript
GET http://localhost:9200/_cat/indices?v
```
Ответ:
```
health status index              uuid                   pri rep docs.count docs.deleted store.size pri.store.size
green  open   .geoip_databases   A-V2hZRISN2g1uuO1nAaDQ   1   0         42            0     40.2mb         40.2mb
yellow open   metricsapp-2023-06 9K7KjaWdRluES-97XANX7g   1   1         91            0    143.5kb        143.5kb
```

3. Статистика конкретной метрики
```javascript
http://localhost:9200/metricsapp-2023-06/_stats
```
Ответ большой JSON с подробной статистикой.