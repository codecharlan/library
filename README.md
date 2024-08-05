# Library Management System API
###### By Nwatu Ifeanyichukwu Ernest

![CI/CD Workflow](https://github.com/codecharlan/library-management-system/actions/workflows/main.yml/badge.svg)

## Overview

A RESTful API built with Java and Spring Boot for managing a library's book inventory and lending system. Users can perform various operations such as managing books, users, and book loans.

### Table of Contents
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Testing the Application](#testing-the-application)
- [API Endpoints](#api-endpoints)
- [Data Structures](#data-structures)
- [Algorithm](#algorithm)
- [Technologies Used](#technologies-used)
- [License](#license)

### Installation
To set up the Library Management System API, follow these steps:
1. Clone the repository:
``
   git clone https://github.com/codecharlan/library.git
``
2. Navigate to the project directory:
   `` 
   cd library
``
3. Install the required dependencies using Maven:
   ```shell
   mvn clean install
   ```
4. Build the application
   ```shell
   mvn clean package
   ```
   
### Prerequisites

Input and output data are formatted in JSON.

### Running the Application

To begin using the Library Application, follow these steps:


1. Start the application with:
    ```shell
    mvn spring-boot:run
    ```
   The application will start on the default port (8080)
   
### API Endpoints
The API exposes the following endpoints:

| Endpoint                                     | HTTP Method | Description                              |
|----------------------------------------------|-------------|------------------------------------------|
| `/books`                                     | GET         | Retrieve a list of all books             |
| `/books`                                     | POST        | Add a new book to the library            |
| `/books/{id}`                                | GET         | Retrieve details of a specific book      |
| `/books/{id}`                                | PUT         | Update details of a specific book        |
| `/books/{id}`                                | DELETE      | Remove a book from the library           |
| `/users`                                     | GET         | Retrieve a list of all users             |
| `/users`                                     | POST        | Add a new user                           |
| `/users/{id}`                                | GET         | Retrieve details of a specific user      |
| `/users/{id}`                                | PUT         | Update details of a specific user        |
| `/users/{id}`                                | DELETE      | Remove a user from the system            |
| `/loans`                                     | POST        | Record a new loan                        |
| `/loans`                                     | GET         | Retrieve a list of all loans             |
| `/loans/{id}`                                | GET         | Retrieve details of a specific loan      |
| `/loans/{id}/return?returnDate={returnDate}` | PUT         | Mark a loan as returned                  |



For comprehensive API usage details, refer to our 

Swagger: [Swagger Documentation](http://localhost:8080/swagger-ui/index.html).

Postman: [Postman Documentation](https://documenter.getpostman.com/view/31876952/2sA3rwNaUj).

### Testing the Application
The Library application includes a set of unit tests to ensure the functionality of the application. To run the tests, use the following command:
 ```shell
    mvn test
  ```

### Technology Used:
* Java 17
* SpringBoot
* Maven
* Spring Security
* Spring Data JPA
* Docker
* Lombok
* CI/CD
* Junit & Mockito
* Git
* Swagger
* Postman

### Assumptions
* Email address is unique for every user in the application.
* Isbn is unique for every book created.
* Return date should not be before loan date.

### License
The Library application is licensed under the [MIT License](LICENSE.md).

Feel free to reach out with any questions, feedback, or suggestions.