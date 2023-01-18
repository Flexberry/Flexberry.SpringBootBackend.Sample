
## Spring Data JPA (Аудит)

### Envers аудит

В Spring бэкенде, есть встроенный механизм аудирования записей: Envers.

Для каждой таблицы, чьи изменения мы хотим отслеживать Envers создаст нам по дополнительной табличке, в которую будет класть запись каждый раз, когда мы что-то сделали с аудированной таблицей. Например:

Таблица тестовой модели (testentity):

|ID            |name            |points       | enabled   |
|--------------|----------------|-------------|-----------|
|1             |Test1           |100          |True       |
|2             |Test2           |200          |False      |
|3             |Test3           |300          |True       |


Таблица аудита для тестовой модели: (testentity_aud)
|ID       | REV	  |REVTYPE	 |name            |points       | enabled   |
|---------|-------|----------|----------------|-------------|-----------|
|1        |1      |0         |Test1           |100          |False      |
|2        |2      |0         |Test2           |200          |False      |
|3        |3      |0         |Test3           |300          |False      |
|4        |4      |0         |Test4           |400          |False      |
|1        |5      |1         |Test1           |100          |True       |
|4        |6      |1         |null            |null         |null       |

В таблице аудита, те же поля что и в таблице тестовой модели (ID и остальные свойства), а так же 2 новых:
`REV` - порядок операции.
`REVTYPE`	- ссылка на таблицу REVINFO (таблица которую генерирует Envers), в которую кладется timestamp

  ### Подключение аудита
1) Для начала нужно добавить в зависимости соотв starter в dependency

```plaintext
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-jpa</artifactId>
```

2) Добавить в application аннотацию `@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)`

```java
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class FlexberrySampleSpringApplication {
```

3) Добавить в модели которые надо аудировать аннотацию `@Audited`

```java
@Entity
@Audited
@Table(schema = "public", name = "testentity")
public class TestEntity {
```

4) Добавить в репозитории моделей которые надо аудировать наследование от `RevisionRepository<TestEntity, Integer, Integer>`

```java
@Repository
public interface TestEntityRepository extends RevisionRepository<TestEntity, Integer, Integer>, JpaRepository<TestEntity, Integer> {
```

5) Далее надо создать таблицы для аудита или разрешить приложению вносить изменения в бд добавив в `application.properties` настройку `spring.jpa.hibernate.ddl-auto= update`.

  ### Альтернатива

Помимо аудита на envers, еще распространен аудит на привязке к хукам жизненного цикла моделей. Для этого надо создавать интерфейс с полями аудит и наследовать от них модели. Но в этом случае не получится аудировать операцию удаления тк вместе с удаление объекта будет удалятся и весь его аудит. Готового решения для подключения такого аудита нет, но в интернете много гайдов (один из таких находится в полезных ссылках)

  ### Полезные ссылки

https://www.baeldung.com/database-auditing-jpa
https://youtu.be/W1Rtn28lHU8
https://www.techgeeknext.com/spring-boot/spring-boot-audit-log-jpa
https://sunitc.dev/2020/01/21/spring-boot-how-to-add-jpa-hibernate-envers-auditing/
https://habr.com/ru/post/120631/
