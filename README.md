# Identity Service API

Spring framework

## Technologies

Spring Boot, Spring Validation, Spring Security, JWT-based authentication

Hibernate Object-Relational Mapping (ORM) framework 

Database: mysql-8.0.36 docker image

Other: Lombok, MapStruct, Bcrypt

## Details

Controller -> Service -> repository

Normalize API response with a custom class including: code, message and result

Global exception handler with `@ControllerAdvice` class

Hash user password with Bcrypt before saving to Database

JWT-based authentication with [Nimbus JOSE + JWT](https://connect2id.com/products/nimbus-jose-jwt) library

Spring Security configs protected endpoints with JWT token

DTO classes for:

- User creat, user update
- Api response

