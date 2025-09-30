# ЛР-1: Разработка защищенного REST API с интеграцией CI/CD

---

### Выполнила:
- **Студентка:** Копалина Майя Алексеевна  
- **Группа:** P3432  
- **Язык программирования:** Java / Spring Boot  

---

## 📌 Описание проекта и API

В проекте реализована простая предметная область с двумя сущностями:

- **Пользователь (User)** — регистрируется в системе, может создавать посты.  
- **Пост (Post)** — контент, создаваемый пользователем.  

Связь: **один пользователь → много постов**.

Данные сущности связаны отношение Один-ко-Многим:

- У пользователя может быть 0 или более постов;
- У поста должен быть один пользователь (т.н. автор).

---

## 🔑 Основные эндпоинты API

### 1. Регистрация пользователя
**POST** `/api/auth/sign-up`  
Тело запроса:
```json
{
  "email": "test@test.com",
  "password": "123456",
  "nickname": "tester"
}
```
Пример ответа (201 Created):
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```
### 2. Вход пользователя
**POST** /api/auth/sign-in
Тело запроса:
```json
{
  "email": "test@test.com",
  "password": "123456"
}
```
Пример ответа:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```
### 3. Получение списка пользователей
**GET** /api/users
Требуется авторизация: Bearer <JWT>

Пример ответа:
```json
[
  {
    "email": "test@test.com",
    "nickname": "tester"
  }
]
```
### 4. Создание поста
**POST** /api/posts
Требуется авторизация: Bearer <JWT>

Тело запроса:
```json
{
  "title": "Мой первый пост",
  "content": "Этот пост создан через API!"
}
```
Пример ответа (201 Created):
```json
{
  "id": 1,
  "title": "Мой первый пост",
  "content": "Этот пост создан через API!"
}
```
### 5. Получение постов пользователя
**GET** /api/posts
Требуется авторизация: Bearer <JWT>

Пример ответа:
```json
[
  {
    "id": 1,
    "title": "Мой первый пост",
    "content": "Этот пост создан через API!"
  }
]
```

### Подробное описание реализованных мер защиты

#### Защита от SQL Injection:

Для обращения к базе данных используются средства стартера spring-boot-starter-data-jpa:3.5.5 и встроенный в данный 
стартер ORM Hibernate 6.6.26.Final
  - query-методы:
    ```java
    @Repository
    public interface UserRepository extends JpaRepository<User, Long> {
    
        User findByEmail(String email);
    
    }
    ```
  - стандартный метод save:
    ```java
    @Transactional
    public PostDto createPost(PostDto postDto) {
        String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        User user = userRepository.findByEmail(email);

        Post post = Post.builder()
                .content(postDto.getContent())
                .user(user)
                .build();

        postRepository.save(post);
        return postDto;
    }
    ```

За счет того, что "под капотом" используются Prepared Statements, наше приложение защищено от SQL Injection.

#### Защита от XSS:

Для защиты от XSS используются средства стартера spring-boot-starter-security:3.5.5:
  - Используется OWASP Java HTML санитайзер, который преобразует входные значения к безопасному виду
(заменяет спецсимволы, удаляет теги и т.д.):
    ```java
    private static final PolicyFactory POLICY = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

    private String sanitize(String input) {
        return POLICY.sanitize(input);
    }
    ```
  - Для большей уверенности были включены политики безопасности контента (Content-Security-Policy; CSP), которые
указывают бразуерам безопасные истоничники, откуда можно загружать ресурсы (скрипты, стили, изображения):
    ```java
    contentSecurityPolicy(
      csp -> csp.policyDirectives("script-src 'self'; " +
                                            "style-src 'self'; " +
                                            "img-src: 'self'")
    )
    ```

#### Как реализована аутентификация:

Аутентификация реализована при помощи стартера spring-boot-starter-security:3.5.5 с использованием JWT. Ключ подписи и 
время жизни, выраженное в секундах, передаются через конфигуграцию application.yml:
```yml
spring:
  application:
    name: sec-lab1
  datasource:
    url: jdbc:postgresql://localhost:5432/sec_lab1
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  flyway:
    schemas: public
    default-schema: public
security:
  token:
    signing-key: ${TOKEN-SIGNING-KEY}
    ttl-seconds: 600
```
Подпись осуществляется алгоритмом HS256. Пароли пользователей хэшируются алгоритмом bcrypt.

Получение токена реализовано через эндпоинты /auth/sign-up /auth/sign-in. Для аутентификации пользователя по jwt
испольуется middleware JwtAuthenticationFilter, где производится:
- Проверка целостности токена
- Проверка срока годности токена
- Заполнение обновление SpringSecurityContext-а

В случае, если JWT валиден, запрос пользователя пропускается далее, иначе возвращется ошибка 403.

### Скриншоты CI/CD

<img width="1468" height="780" alt="image" src="https://github.com/user-attachments/assets/83fcfe97-24a4-48f3-b52b-04c046bdbd85" />

<img width="1006" height="637" alt="image" src="https://github.com/user-attachments/assets/7c7de1f8-7c12-463c-ac75-ad8bd52be394" />

<img width="1468" height="790" alt="image" src="https://github.com/user-attachments/assets/64a178d2-db55-41ea-9596-651df3e9ec5f" />




