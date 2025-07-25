# Identity Service API

Spring framework

## Technologies

Spring Validation

Hibernate Object-Relational Mapping (ORM) framework 

Database: mysql-8.0.36 docker image

Other: Lombok, MapStruct, Bcrypt

## Details

Controller -> Service -> repository

Normalize API response with a custom class including: code, message and result

Global exception handler with `@ControllerAdvice` class

Hash user password with Bcrypt before saving to Database

DTO classes for:

- User creat, user update
- Api response

