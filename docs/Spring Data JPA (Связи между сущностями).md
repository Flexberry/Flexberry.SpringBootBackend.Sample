## Relationships в Spring Data JPA

В Spring Data реализованы различные связи между сущностями. :  
  
**ManyToMany** - многие ко многим. Когда сущность А может быть связана с множеством сущностей B и наоборот. Например Студент и Предмет. Студент может изучать много предметов и наоборот - на предмет могут быть записаны разные студенты. Кроме того, таким типом связи одинаковые сущности могу быть связаны друг с другом. Например если мы описываем генеалогическое дерево.  
  
Как это реализовать на примере Student и Course:  
  
Для начала, нам нужна третья таблица _**studentsCourses**_, которая будет выступат в роли связующей. В ней будут внешние ключи студентов и предметов (studentsCourses\_student\_id, studentsCourses\_course\_id). Одна сущность будет исходной, а вторая целевой. Также нужно учитывать, что при таком виде связи, коллекция должны иметь тип Set  
 

```plaintext
@Entity
@Table(name = "students")
public class Student implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	...

	@ManyToMany
	@JoinTable(
  		name = "studentsCourses", 
  		joinColumns = @JoinColumn(name = "studentsCourses_student_id"), 
  		inverseJoinColumns = @JoinColumn(name = "studentsCourses_course_id"))
		Set<Course> studentCources;
```

И целевая сущность

```plaintext
@Entity
@Table(name = "courses")
public class Course implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	...

	@ManyToMany(mappedBy = "studentCources", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();
```

**OneToMany** - один ко многим. Это такой вид связи, когда сущность А может быть связана с множеством сущностей B, но у сущности B есть всегда только одна сущность А. Например Книга и Страница. 

Как это реализовать на примере Book и Page:  
  
В данном случае третья, связывающая таблица, не нужна.  
  
Опишем связь в сущности Book

```plaintext
@Entity
@Table(name = "books")
public class Book implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	...

	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Page> pages;
```

И сущность Page

```plaintext
@Entity
@Table(name = "pages")
public class Page implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	...

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false) // book_id это поле в таблице Book
    private Book book;
```

  
**OneToOne** - один к одному. Это такой вид связи, когда сущность А может быть связана только с одной сущностью B и наоборот. Например система в которой у каждого Заказчика может быть только один юридический Адрес. 

Опишем связь в сущности Address

```plaintext
@Entity
@Table(name = "addresses")
public class Address implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	...

	@OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
```

И сущность `Customer`

```plaintext
@Entity
@Table(name = "сustomers")
public class Customer implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	...

	@OneToOne(mappedBy = "customer", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Address address;
```

`Ссылки`

[https://attacomsian.com/blog/spring-data-jpa-many-to-many-mapping](https://attacomsian.com/blog/spring-data-jpa-many-to-many-mapping)

[https://www.baeldung.com/jpa-many-to-many](https://www.baeldung.com/jpa-many-to-many)  
[https://attacomsian.com/blog/spring-data-jpa-one-to-many-mapping](https://attacomsian.com/blog/spring-data-jpa-one-to-many-mapping)

[https://attacomsian.com/blog/spring-data-jpa-one-to-one-mapping](https://attacomsian.com/blog/spring-data-jpa-one-to-one-mapping)

https://www.baeldung.com/jpa-one-to-one