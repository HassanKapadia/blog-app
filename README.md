# Blogging app using Spring Boot

A simple blogging app where authors can write articles and users can comment on the same.

## Requirements

* JDK-21
* Gradle 8.14.3
* Postgres 14 (or higher)

## Technical Specifications

This project is built entirely on the Spring Boot ecosystem with Thymeleaf for the frontend.
Key technologies and features include:

* Spring Boot Web – REST API development
* JWT-based Authentication – secure login and session management
* Spring Security + Password Encryption – for user credentials
* Spring Data JPA (Hibernate) – ORM for PostgreSQL
* Gradle – build and dependency management

## Functional Overview

1. User Registration & Login
    * New users can sign up with basic details.
    * JWT is issued upon successful login.

2. Articles Management
    * Users can create, update, and delete articles.
    * Articles from all authors are listed on the home page.

3. Comments Feature
    * Users can view, add, edit, and delete comments on articles.

4. Account Management
    * View and edit personal details or delete the account permanently.

5. Logout
    * Secure logout functionality provided.

## Installation & Setup

To run the project locally:

```bash
$ git clone git@github.com:HassanKapadia/blog-app.git
$ cd blog-app
$ ./gradlew clean build
$ ./gradlew bootRun
```
Once the server starts, open your browser and visit:
[http://localhost:8080/blog-app](http://localhost:8080/blog-app)

Note:
Before running the app, update your database credentials in:
`src/main/resources/application.properties`
