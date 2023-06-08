# Spring Cloud Gateway

Это прокси-сервис, выполненный в виде Spring-boot приложения. Все запросы проходят через него, поэтому он выступает единственной точкой входа для пользователя и координатором для сервисов, 
благодаря чему, сервисы не должны знать друг о друге.

## Создаем сервис

Инициализация проекта не отличается от обычного проекта-бэкенда

[Как создать проект можно посмотреть тут](Создание%20SpringBoot%20проекта.md)

## Настраиваем зависимости в pom.xml

Добавим переменную с версией spring cloud

```xml
<properties>
	<spring-cloud.version>2022.0.3</spring-cloud.version>
</properties>
```

Набор зависимостей следующий:

```xml
<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-gateway</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
		</dependency>
	</dependencies>
```

**spring-boot-starter-actuator** - это инструмент мониторинга и метрик, для контроля работы шлюза.

**spring-boot-starter-webflux** - для поддержки реактивного программирования в web-сервисах. Необходим для spring cloud gateway

Также нужно добавить dependencyManagement

```xml
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-dependencies</artifactId>
			<version>${spring-cloud.version}</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```

## Настройка маршрутизации

В маршрутизации Spring Cloud Gateway есть три основных понятия:

**Route** - это базовый элемент шлюза. Роут задается идентификатором (ID), URL назначения, коллекцией предикатор и фильтров

**Predicate** - Java 8 Function Predicate. Задают шаблон HTTP запроса, на основе которого активируется роут

**Filter** - интсансы класса GatewayFilter, которые создаются по принципу фабрики. С помощью них можно менять содержимое запроса и ответа

Роуты можно задавать в коде (обычно в классе @SpringBootApplication). Метод, формирующий роуты, должен иметь аннотацию @Bean и возвращать тип RouteLocator.

Пример (сторонний):

```java
@Bean
public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
      .route("r1", r -> r.host("**.baeldung.com")
        .and()
        .path("/baeldung")
        .uri("http://baeldung.com"))
      .route(r -> r.host("**.baeldung.com")
        .and()
        .path("/myOtherRouting")
        .filters(f -> f.prefixPath("/myPrefix"))
        .uri("http://othersite.com")
        .id("myOtherID"))
    .build();
}
```

Но роуты также можно задавать через конфиг-файл src\main\resources\application.yml.
В проекте стенда выбран именно такой способ.
Пример application.yml  с одним маршрутом:

```xml
server:
  port: 8760

spring:
  cloud:
    gateway:
      routes:
        - id: backend
          uri: http://springboot-backend:8080
          predicates:
            - Path=/service/backend/**
          filters:
            - RewritePath=/service/backend/(?<RID>.*), /$\{RID}
```

В данном случае шлюз принимает запросы по порту 8760. Запрос вида http://localhost:8760/service/backend/api/customers он превратит в http://springboot-backend:8080/api/customers




