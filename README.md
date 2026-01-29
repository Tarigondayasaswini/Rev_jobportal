# Rev_jobportal
A Java console-based job portal built with JDBC, DAO–Service architecture, and MySQL, enabling job seekers and employers to manage profiles, jobs, applications, and notifications with clean design and unit-tested business logic.

# Project Architecture

The Job Portal Console Application follows a layered architecture to ensure separation of concerns, maintainability, and testability.

---

## Model Layer (`org.jobportal.model`)

Contains core domain entities:

- User  
- JobSeekerProfile  
- Resume  
- Company  
- Job  
- JobApplication  
- Notification  
- PasswordRecovery  

Additional responsibilities:

- Represents database tables designed using Third Normal Form (3NF)
- Acts as data carriers between DAO, Service, and Controller layers
- Contains no business logic (POJOs only)

---

## DAO Layer (`org.jobportal.dao`)

- Handles all database operations using JDBC
- Uses `PreparedStatement` to prevent SQL Injection
- Performs CRUD operations for all entities

Handles the following responsibilities:

- Authentication and password recovery queries
- Job posting and job search queries
- Job application persistence
- Resume and profile storage
- Notification storage and updates

Uses a centralized `DBConnection` utility.

---

## Service Layer (`org.jobportal.service`)

- Contains all business logic and workflow orchestration
- Enforces business rules such as:
  - Only active users can login
  - Job must exist and be open before applying
  - Duplicate applications are prevented
  - Resume must exist before applying
  - Profile completion status is maintained
- Coordinates multiple DAOs when required

Handles notifications for:

- New job postings
- Application shortlist or rejection
- Job matches

Additional characteristics:

- Uses meaningful response enums (`ApplyStatus`)
- Fully unit-tested using JUnit 5 and Mockito

---

## Controller Layer (`org.jobportal.controller`)

- Implements a console-based user interface (CLI)
- Displays menus and dashboards
- Accepts and validates user input
- Navigates Job Seeker and Employer flows
- Delegates all business logic to the Service layer
- Duplicate input logic removed using helper methods

---

## Config Layer (`org.jobportal.config`)

Provides centralized database configuration.

`DBConnection` manages:

- Database connection creation
- Connection reuse
- Safe resource cleanup

---

## Utility Layer (`org.jobportal.util`)

Contains shared utilities used across the application:

- Password hashing utility
- Status enums for application flow

Ensures code reuse and consistency.

---

# Database Design (3NF)

The MySQL database (`job_portal_db`) is fully normalized to Third Normal Form (3NF) to eliminate redundancy and ensure data integrity.

---

## Key Relationships

- User → JobSeekerProfile
- JobSeekerProfile → Resume
- User (Employer) → Company
- Company → Job
- Job → JobApplication
- User → Notification
- User → PasswordRecovery

Foreign keys and constraints ensure referential integrity.

---

# Application Workflow

## Authentication

- User registration and login
- Secure password hashing
- Active/inactive account validation
- Password recovery using token-based mechanism

---

## Job Seeker Flow

- Create and update profile
- Create and manage resume
- Search and filter jobs
- Apply using selected or latest resume
- View and withdraw job applications
- Receive and view notifications
- Change password

---

## Employer Flow

- Create and manage company profile
- Post, update, close, reopen, and delete jobs
- View company jobs
- View applicants by job
- Filter applicants
- Bulk shortlist or reject applicants
- View job statistics
- Receive notifications
- Change password

---

## Job Application Lifecycle

1. Employer posts a job
2. Job seeker applies
3. Business validations performed
4. Application saved
5. Notifications sent
6. Employer reviews applicants
7. Shortlist or reject action
8. Applicant notified

---

# Security

- Passwords are never stored in plain text
- Passwords are hashed before storage
- Login validates hashed credentials
- Account activation status enforced

---

# Input Validation

- Menu input validation
- Email format validation
- Numeric and range checks
- Duplicate and state validations
- Applied at both Controller and Service layers

---

# Testing

Service layer unit testing using:

- JUnit 5
- Mockito

Additional testing details:

- DAO dependencies mocked during tests
- Reflection-based injection used for legacy-safe testing
- Database is not required during unit testing

---

# Technical Stack

- Programming Language: Java 21 (LTS)
- Database: MySQL 8+
- Database Access: JDBC
- Build Tool: Maven
- Logging Framework: SLF4J with Logback
- Testing Framework: JUnit 5
- Mocking Framework: Mockito
- Architecture Pattern: DAO–Service–Controller
- Design Principles: Clean Code, SOLID, DRY
- Database Design: SQL Normalization (3NF)

---

# Design Decisions and Clean Code Practices

- Dead code removed only after cross-verification
- Domain getters retained even if not immediately used
- Business rules enforced only in Service layer
- Controllers kept thin and readable
- Duplicate logic refactored into reusable methods
- IDE warnings handled through design validation, not blind suppression

---

# Conclusion

This project demonstrates real-world backend development using core Java and JDBC, focusing on:

- Clean layered architecture
- Proper database normalization
- Strong business-rule enforcement
- Maintainable and testable codebase
- Professional coding standards suitable for production systems
