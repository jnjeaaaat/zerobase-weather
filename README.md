# zerobase-weather
> /src/main/resources/application.yml
```java
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/weather?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: root
    password: password

  jpa:
    show-sql: true
    database: mysql
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

openweathermap:
  key: "openApi Key"

logging:
  config: classpath:logback-spring.xml

```
