# Identity Service API

Spring framework

## Technologies

Spring Validation

Hibernate Object-Relational Mapping (ORM) framework 

Database: mysql-8.0.36 docker image

Other: Lombok, MapStruct

## Details

Controller -> Service -> repository

Normalize API response with a custom class including: code, message and result

Global exception handler with `@ControllerAdvice`

DTO classes for:

- User creat, user update
- Api response

