# Создание SpringBoot проекта. Структура проекта.

## Что такое SpringBoot

Spring boot это фрэймворк, расширяющий возможности стандартного Spring. Основной целью создания Spring Boot было упрощение создания приложений на основе Spring. Он позволяет наиболее простым способом создать web-приложение, требуя от разработчиков минимум усилий по его настройке и написанию кода.

Spring Boot обладает большим функционалом, но его наиболее значимыми особенностями являются: управление зависимостями, автоматическая конфигурация и встроенные контейнеры сервлетов.

Чтобы ускорить процесс управления зависимостями, Spring Boot неявно упаковывает необходимые сторонние зависимости для каждого типа приложения на основе Spring и предоставляет их разработчику посредством так называемых starter-пакетов (spring-boot-starter-web, spring-boot-starter-data-jpa и т. д.).

Например, если вы хотите начать использовать Spring Data JPA для доступа к базе данных, просто включите в свой проект зависимость spring-boot-starter-data-jpa и все будет готово (вам не придется искать совместимые драйверы баз данных и библиотеки Hibernate).

Если вы хотите создать Spring web-приложение, просто добавьте зависимость spring-boot-starter-web, которая подтянет в проект все библиотеки, необходимые для разработки Spring MVC-приложений, таких как spring-webmvc, jackson-json, validation-api и Tomcat.

## Создание проекта

#### IntelliJ IDEA

Необходима версия Ultimate

![](https://github.com/Flexberry/Flexberry.SpringReactApplication.Sample/blob/feature-230292-docs-create-springboot-project/docs/images/create1.jpg)

#### Сервис
_https://start.spring.io/_ - специальный сервис для быстрого создания проекта. Скачивается архивом

![](https://github.com/Flexberry/Flexberry.SpringReactApplication.Sample/blob/feature-230292-docs-create-springboot-project/docs/images/create2.jpg)

Формат пакета – jar, т.к War формат используется больше для web приложений.
Java проекты бывают разных типов (в плане сброрки). 
Наиболее популярные это

- **Gradle** – используется для андроидаMaven – почти стандарт для Spring.
- **Apache Maven** — фреймворк для автоматизации сборки проектов на основе описания их структуры в файлах на языке POM (англ. Project Object Model).

Нажимаем generate и получаем Zip-архив с проектом.В качестве IDE можно использовать IntelliJ IDEA (рекомендуется использовать 2022.1)

## Структура проекта

![](https://github.com/Flexberry/Flexberry.SpringReactApplication.Sample/blob/feature-230292-docs-create-springboot-project/docs/images/create3.jpg)

- **pom.xml** – описывает структуру проекта, имя, идентификатор, зависимости. В случае со spring boot также будет указан parent проект spring-boot-starter-parent.
- **.idea** – служебная папка IDE.
- **.mvn** – тоже служебная папка, мавена.
- **src/main/java** – основная папка проекта. В ней весь код будет. После инициализации в ней находится только стартовый класс с методом main(), который является входной точкой приложения. Внутри него вызывается SpringApplication.run. Так стартуют все Spring приложения.
- **src/main/resources** - Ресурсы, которые использует приложение (HTML-страницы, картинки, таблицы стилей и тд).
В application.properties можно задавать специальные переменные приложения и их значения. Потом в коде обращаться к ним
- **src/test** – тесты строятся на базе библиотеки jUnit. Тесты запускаются автоматически каждый раз при компиляции приложения (это когда jar файл получается)

## Организация кода в src/main/java
Имена пакетов и пути к файлам преобразуются друг в друга. Каждая папка автоматически формирует соответствующий namespace, прибавляя часть к имени пакета.
К примеру проект с packageName net.flexberry.flexberrySampleSpring сформирует следующую структуру папок -
src/main/java/net/flexberry/flexberrySampleSpring/...

А создав внутри, например папку model, получим для ее классов package name (namespace) net.flexberry.flexberrySampleSpring.model

В какие пакеты организуются классы:

- **model** - тут хранятся файлы сущностей.
- **repository** - интерфейсы с описанием общих методов, которые будут использоваться для доступа к данным. Репозиторий называется по имени сущности(обязательно) и предоставляет доступ к ней. 
Репозиторий называется по имени сущности(обязательно) и предоставляет доступ к ней. 
- **controller** - классы-контроллеры. ВЫполняют обработку запросов.
- **service** - сервисы. Содержат бизнес-логику
- **utils** - вспомогательные классы.


*ссылки на статьи*

https://medium.com/the-resonant-web/spring-boot-2-0-project-structure-and-best-practices-part-2-7137bdcba7d3

https://studygyaan.com/spring-boot/spring-boot-project-folder-structure-and-best-practices

https://github.com/jhipster/jhipster-sample-app/tree/main/src/main/java/io/github/jhipster/sample

