# Library Management System (JDBC + DAO)

A console-based Java backend application that simulates a simple library management system.  
The project focuses on practicing **core Java**, **JDBC**, **SQL**, and the **DAO pattern**, with a clear separation of concerns and layered architecture.

---

## Project Overview

This application allows users to interact with a library system through a console menu.  
Users can navigate between different sections to manage books, authors, readers, and system services.

The project was built as part of Java backend practice with an emphasis on:
- clean architecture
- database interaction
- object-oriented design
- maintainable and extensible code structure

---

## Application Navigation

The application uses a console-based menu system:

Main menu:
1. Book navigation menu  
2. Reader navigation menu  
3. Author navigation menu  
4. Service menu  
5. Exit  

### Book navigation menu
- Show the list of books  
- Search for a book by title  
- Search for a book by author  
- Add a new book  
- Update a book  
- Delete a book  
- Return to the main menu  

Some advanced service operations are planned for future versions and currently display informative messages about upcoming functionality.

---

## Architecture Overview

The project follows a layered architecture with clear responsibility separation.

### Domain Layer (Entities)
Located in `ClassesDOJO`:
- `Book`
- `Author`
- `Genre`
- `Reader`
- `Loan`

These classes represent core business entities and contain only domain-related data.

---

### DAO Layer

#### DAO Interfaces
Located in `InterfaceDAO`:
- `BookDao`
- `AuthorDao`
- `GenreDao`
- `ReaderDao`
- `LoanDao`

Defines contracts for data access operations.

#### JDBC Implementations
Located in `JdbcAndDAO`:
- `JdbcBookDao`
- `JdbcAuthorDao`
- `JdbcGenreDao`
- `JdbcReaderDao`
- `JdbcLoanDao`

Each implementation uses **JDBC** to interact with a PostgreSQL database.

---

### Row Mappers
Located in `RowMappers`:
- `BookRowMapper`
- `AuthorRowMapper`
- `GenreRowMapper`
- `ReaderRowMapper`
- `LoanRowMapper`

Responsible for mapping `ResultSet` rows to domain objects.

---

### System and Application Layer
Located in `System`:

Key components:
- `Application` – application entry point
- `LibraryController` – controls navigation and application flow
- `Input` / `Output` interfaces – abstraction over user input and output
- `SystemInput` / `SystemOutput` – console-based implementations
- `InputCheck` and `CheckChoice` – user input validation
- `Connector` – database connection handling

This layer manages user interaction and coordinates calls between the UI and DAO layers.

---

### Exception Handling
Located in `Exception`:
- `DaoException`

Used to handle and wrap database-related errors in a controlled way.

---

## Technologies Used

- Java
- JDBC
- PostgreSQL
- SQL
- DAO Pattern
- IntelliJ IDEA
- Git & GitHub

---

## Database

The application uses a relational database (PostgreSQL).  
SQL queries are executed via JDBC, and results are mapped to domain objects using RowMapper classes.

---

## Current Status and Future Improvements

Some service-level features are intentionally left unimplemented and marked for future versions.  
Planned improvements include:
- transaction management
- extended service logic
- improved error handling
- version 2.0 with enhanced business rules

---

## Purpose of the Project

This project was created to:
- practice Java backend development
- understand JDBC and SQL integration
- apply the DAO pattern in a real project
- design clean and maintainable architecture
- prepare for junior backend developer roles
