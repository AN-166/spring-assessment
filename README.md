# Customer Batch Processing System

This is a Spring Boot application that reads a `.txt` batch file during startup, processes each line, and inserts the data into a `customer` table. The application also provides secured RESTful APIs to search and update customer data.

## Features

- Spring Batch for file processing
- Inserts data into MySQL `customer` table
- JWT-based authentication (default login: `admin` / `123456`)
- REST APIs:
  - Search customer by `accountNumber`, `description`, or `customerId`
  - Update customer `description`
- Optimistic locking using a `version` field to handle concurrent updates

## Tech Stack

- Java 17+
- Spring Boot
- Spring Batch
- Spring Security (JWT)
- MySQL
- JPA (Hibernate)

## Documentation
- Full system and design documentation is available in [`project-document.docx`](./project-document.docx)
