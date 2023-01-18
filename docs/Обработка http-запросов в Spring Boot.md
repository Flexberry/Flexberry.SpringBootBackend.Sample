# Обработка http-запросов в Spring Boot.

## REST сервис

Подход Spring при построении RESTful web-сервиса заключается в обработке HTTP-запросов контроллером. Эти компоненты опознаются по `@RestController` аннотации, помечая класс как контроллер, где каждый метод возвращает объект вместо представления(view). Это сокращение для `@Controller` и `@ResponseBody` вместе взятых. Ключевое отличие между традиционным MVC контроллером и контроллером RESTful web-сервиса заключается в создании тела HTTP-ответа. Вместо того, чтобы опираться на view-технологию для рендеринга на серверной стороне сообщения приветствия в HTML, RESTful web-сервис контроллер просто заполняет и возвращает объект. Данные объекта будут записаны напрямую в HTTP-ответ как JSON. `@RestController` представляет аннотированные методы бина как HTTP точки выхода. Аннотация `@RequestMapping` предоставляет метаданные к каждому из этих методов. Метод будет вызываться в том случае, если входящий HTTP запрос соответствует требованиям, предусмотренным аннотацией `@RequestMapping` этого метода.

`@RestController`, когда он расположен на уровне типа, предоставляет значения по умолчанию для всех методов типа. Каждый отдельный метод может переопределять большинство аннотаций на уровне типа.
К примеру:
```java
@RestController
@RequestMapping("/comments")
public class CommentController { 
  @GetMapping("/{id}")
    public Comment getOneComment(@PathVariable int id){
        return getComment(id);
    }
 }
```
Здесь `CommentController` обрабатывает все запросы типа `/comments`. Однако, любые методы в типе, которые затем определяются как URI (как метод `getOneComment`), добавляются к корневому запросу. В данном примере путь метода `getOneComment` будет соответствовать `/comments/{id}` Методы, в которых не указан путь, просто наследуют путь уровня типа. 

`@RequestMapping` сопоставляет все HTTP-операции по умолчанию., Если не указывать отдельно методы GET, PUT, POST и т.д., то метод будет триггериться на все HTTP запросы соответственно. Используя `@RequestMapping(method=GET)` можно определить конкретные методы. Или используя `@PostMapping`, `@GetMapping`, `@PutMapping`, `@DeleteMapping` на уровне методов. 

Слова в фигурных скобках, например `{id}` являются переменными пути, подстановочными символами. Spring MVC будет извлекать их из URI и делать их доступными как аргументы под тем именем переменной, которое передается методу контроллера и аннотацией `@PathVariable`.

`@RequestParam` связывает значение строкового параметра запроса с параметром метода. Этот строковый параметр запроса не обязателен; если он отсутствует в запросе, то будет использовано defaultValue.

## Создание REST функционала

1. Создаем сущность клиента

```java
public class User {
    private int userId;
    private String userName;
    private int age;

    public User(){}
    public User(int userId, String userName, int age){
        this.userId = userId;
        this.userName = userName;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
```

2. В контроллере будут реализованы CRUD операции над объектом, реализуем эти операции. 

```java
public class UserController {

    public static List<User> usersList = new ArrayList<>();

    public User createUser(int id, String name, int age){
        User user = new User();
        user.setUserId(id);
        user.setUserName(name);
        user.setAge(age);
        usersList.add(user);
        return user;
    }

    public User getUser(int id){
        return usersList.stream()
                .filter(user -> user.getUserId() == id)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public synchronized User updateUser(int id, String name, int age) {
        User oldUser = getUser(id);
        String oldName = oldUser.getUserName();
        int oldAge = oldUser.getAge();
        deleteUser(id);

        if (name.equals("emptyName")) {
            name = oldName;
        }

        if (age == 0) {
            age = oldAge;
        }

        return createUser(id, name, age);
    }

    public static List<User> getAllUsers(){
        return usersList;
    }

    public void deleteUser( int id){
        User user = getUser(id);
        usersList.remove(user);
    }}
```

3. С использованием описанных выше аннотаций, реализуем логику обработки клиентских запросов на эндпоинты (URI).

```java
@RestController
@RequestMapping("/users")
public class UserController {

    public static List<User> usersList = new ArrayList<>();

    public User createUser(int id, String name, int age){
        User user = new User();
        user.setUserId(id);
        user.setUserName(name);
        user.setAge(age);
        usersList.add(user);
        return user;
    }

    @PostMapping
    public User create(@RequestParam(value = "id", defaultValue = "0") int id,
                       @RequestParam(value = "name", defaultValue = "emptyName") String name,
                       @RequestParam(value = "age", defaultValue = "0") int age) {
        User user = new User();
        user.setUserId(id);
        user.setUserName(name);
        user.setAge(age);
        usersList.add(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getOneUser(@PathVariable int id){
        return getUser(id);
    }

    public User getUser(@PathVariable int id){
        return usersList.stream()
                .filter(user -> user.getUserId() == id)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @PutMapping("/{id}")
    public synchronized User updateUser(@PathVariable int id,
                                        @RequestParam(value = "name", defaultValue = "emptyName") String name,
                                        @RequestParam(value = "age", defaultValue = "0") int age) {
        User oldUser = getUser(id);
        String oldName = oldUser.getUserName();
        int oldAge = oldUser.getAge();
        deleteUser(id);

        if (name.equals("emptyName")) {
            name = oldName;
        }

        if (age == 0) {
            age = oldAge;
        }

        return createUser(id, name, age);
    }

    @GetMapping
    public static List<User> getAllUsers(){
        return usersList;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id){
        User user = getUser(id);
        usersList.remove(user);
    }}
```


*ссылки на статьи*

https://spring-projects.ru/guides/rest-service/

http://spring-projects.ru/guides/tutorials-bookmarks/

https://javarush.com/groups/posts/2488-obzor-rest-chastjh-3-sozdanie-restful-servisa-na-spring-boot

