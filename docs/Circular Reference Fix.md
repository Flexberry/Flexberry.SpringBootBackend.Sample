
Можно добавить спецификатор первичного ключа **JsonIdentityInfo** для того чтобы сериализатор мог отслеживать циклические зависимости (см. https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion):
```cs
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "primarykey")
public class Comment {
```

Тогда получается дерево, но при обнаружении цикла элемент заменяется на ИД, для структуры, описаной в **bidirectionalCircularRelationThrowNoStackOverflow** получается слкдующая структура:
```json
{
    "primarykey": 101,
    "commentDate": null,
    "commentText": "TestText5",
    "customer": {
        "primarykey": 100,
        "name": "Jack",
        "age": 30,
        "comments": [
            101,
            {
                "primarykey": 102,
                "commentDate": null,
                "commentText": "TestText6",
                "customer": {
                    "primarykey": 100,
                    "name": "Jack",
                    "age": 30,
                    "comments": [
                        101,
                        102
                    ]
                }
            }
        ]
    }
}
```

Также можно добавить в детэйл-свойство **comments** класса **Customer** атрибут **JsonIdentityReference** для указания что надо выводить их в виде списка ИД:
```cs
@JsonIdentityReference(alwaysAsId=true)
private List<Comment> comments;
```

При этом список комментариев выводится в виде массива ИД:
```json
{
    "primarykey": 101,
    "commentDate": null,
    "commentText": "TestText5",
    "customer": {
        "primarykey": 100,
        "name": "Jack",
        "age": 30,
        "comments": [
            101,
            102
        ]
    }
}
```

Сам **Customer** возвращается так:
```json
{
    "primarykey": 100,
    "name": "Jack",
    "age": 30,
    "comments": [
        101,
        102
    ]
}
```


Как промежуточный вариант можно добавить **JsonIdentityInfo** на **Customer**:
```cs
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "primarykey")
public class Customer {
```
и  **JsonManagedReference** на **comments**, вместо **JsonIdentityReference**:
```cs
@JsonManagedReference
private List<Comment> comments;
```
тогда зацикленные ссылки обрезаются так:
```json
{
    "primarykey": 101,
    "commentDate": null,
    "commentText": "TestText5",
    "customer": {
        "primarykey": 100,
        "name": "Jack",
        "age": 30,
        "comments": [
            101,
            {
                "primarykey": 102,
                "commentDate": null,
                "commentText": "TestText6",
                "customer": 100
            }
        ]
    }
}
```

Сам **Customer** возвращается так, этот вариант оставлен как рабочий:
```json
{
    "primarykey": 100,
    "name": "Jack",
    "age": 30,
    "comments": [
        {
            "primarykey": 101,
            "commentDate": null,
            "commentText": "TestText5",
            "customer": 100
        },
        {
            "primarykey": 102,
            "commentDate": null,
            "commentText": "TestText6",
            "customer": 100
        }
    ]
}
```