## Spring Data JPA (ORM)

В Spring бэкенде работа с базами данных на основе ORM выполняется с помощью Spring Data JPA.

Spring Data JPA - это название для инструментария работы с БД в Spring бэкендах. В его основе находится фреймворк Hibernate, который построен на основе JPA 2.1

Java Persistence API (JPA) - это java-спецификация, определяющая доступ, сохранение и управления данными между Java-объектами (Entity классами) и реляционной базой данных, на основе технологии ORM.

В случае Spring boost все необходимое для работы базой данных упаковано в starter.

Рассмотрим на примере, как сделать Spring boost бэкенд, который будет работать с БД Postgres

1) Для начала нужно добавить в зависимости соотв starter в dependency

```plaintext
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-jpa</artifactId>
```

2) И зависимость для выбранного типа БД. В данном случае postgres

```plaintext
<groupId>org.postgresql</groupId>
<artifactId>postgresql</artifactId>
<scope>runtime</scope>
```

  
3) Строка подключения к БД прописывается в src/main/resources/application.properties

```plaintext
spring.datasource.url= jdbc:postgresql://localhost:5432/appdb 
spring.datasource.username= flexberryuser spring.datasource.password= jhv

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation= true 
spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.PostgreSQLDialect
```

4) Создадим класс-модель (сущность). Модели хранятся внутри пакета model. Эти классы описывают объект данных и соответствие параметров объекта таблице в которой этот объект будет храниться в бд. Важно соблюдать регистры в названии методов доступа. Если мы хотим получать в запросе параметр Speed, то метод доступа должен называться getSpeed.

```plaintext
package net.flexberry.flexberrySampleSpring.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(schema = "public", name = "testentity")
public class TestEntity {
    @Id
    @Column(name = "primarykey")
    private int primarykey;

    @Column(name = "name")
    private String name;

    @Column(name = "points")
    private int points;

    @Column(name = "enabled")
    private boolean enabled;

    public TestEntity() {
        super();
    }
    public void setprimaryKey(int primarykey) {
        this.primarykey = primarykey;
    }
    public int getprimarykey() {
        return primarykey;
    }
    public String getname() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }
    public int getpoints() {
        return points;
    }
    public void setpoints(int points) {
        this.points = points;
    }
    public boolean getenabled() {
        return enabled;
    }
    public void setenabled(boolean enabled) {
        this.enabled = enabled;
    }
}
```

5) Создадим класс-репозитория. (repository/TestRepository.java)  
Репозиторий это интерфейс к БД. Они включают в себя основные методы доступа к данным save(), findOne(), findById(), findAll(), count(), delete(), deleteById()… (они подтянутся сами, их не нужно прописывать). Кроме стандартных в репозиторий можно добавлять свои методы. Репозиторий называется по имени сущности(обязательно) и предоставляет доступ к ней. 

```plaintext
package net.flexberry.flexberrySampleSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import net.flexberry.flexberrySampleSpring.model.TestEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/*
    Класс репозитория. Этот класс реализует базовые методы работы с сущностями:
    save(), findOne(), findById(), findAll(), count(), delete(), deleteById()…
    без дополнительного их объявления.
 */
@Repository
public interface TestRepository extends JpaRepository<TestEntity, UUID> {

    // Кроме базовых методов можно добавлять кастомные. Этот метод возвращает сущности с указанным типом enabled.
    List<TestEntity> findByEnabled(boolean enabled);

    // А этот возвращает сущности с заданным именем.
    List<TestEntity> findByName(String name);
}
```

6) Создаем класс сервиса (service/TestEntityService.java) Сервисы содержат логику обработки запросов и предоставляют обработчикам запросов методы в которых она содержится. Сервис использует репозиторий для доступа к БД.  
  
 

```plaintext
package net.flexberry.flexberrySampleSpring.services;

import net.flexberry.flexberrySampleSpring.model.TestEntity;
import net.flexberry.flexberrySampleSpring.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TestEntityService {
    @Autowired
    TestRepository repository;

    public TestEntity getTestEntityById(UUID id) {
        return repository.findById(id).get();
    }

    public void saveOrUpdateTestEntity(TestEntity testEntity) {
        repository.save(testEntity);
    }

    public void deleteTestEntityById(UUID id) {
        repository.deleteById(id);
    }

    public List<TestEntity> getTestEntitiesByName(String name) {
        return repository.findByName(name);
    }

    public List<TestEntity> getTestEntitiesByEnabled(boolean enabled) {
        return repository.findByEnabled(enabled);
    }
}
```