# Identity Service API

Spring framework

## Technologies

Spring Boot, Spring Data JPA, Spring Validation, Spring Security, JWT-based authentication & authorization

Hibernate Object-Relational Mapping (ORM) framework 

Database: mysql-8.0.36 docker image

Other: Lombok, MapStruct, Bcrypt, oauth2

## Details

Normalize API response with a custom class including: code, message and result

Global exception handler with `@ControllerAdvice` class

Hash user password with Bcrypt before saving to Database

JWT-based authentication with oauth2 standard

JWT-based authorization with two roles: USER and ADMIN

Spring Security configs protected endpoints with JWT token

