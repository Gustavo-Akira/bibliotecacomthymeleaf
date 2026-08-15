# Project Library thymeleaf

I'm picking it back up to refactor and evolve it incrementally until the original requirements are fulfilled, treating the application as if it were already running in production.


## Requirements

- Manage Users
- Management Dashboard
- User Dashboard
- Buy Books
- Books Management
- Inventory Management
- Publisher Management
- Delivery Management
- Book Gender Management
- User Address Management

## Current Technical State

The application currently follows a modified MVC architecture:

- Controllers: HTTP handling, application flow, and business rules
- Models: domain data, entity relationships, and mutable state
- Repositories: persistence and infrastructure concerns
- Security: authentication and authorization concerns

Current codebase language:
- Mixed Portuguese and English terminology

### Development Process

The project was originally developed without a formal development
workflow.

- No pull requests
- No code review
- No automated tests
- Manual validation
- No CI/CD

### Runtime & Framework

- Java: 11
- Spring Boot: 2.2.6
- Thymeleaf
- Materialize: 1.0.0
- jQuery: 3.4.1
- Build: Maven
- Database: PostgreSQL
- Hibernate Dialect: PostgreSQL82Dialect
- Configuration: Local properties only; no environment variables or externalized secrets
- Tests: None


## Refactoring Initiative

This project is being progressively modernized from its original
technology stack.

The modernization will be performed incrementally, avoiding a
Big Bang rewrite and preserving the system's behavior whenever
possible.

The target versions and architectural changes will be defined
through Architecture Decision Records (ADRs).


## Known Technical Debt

- No automated tests
- Mixed Portuguese and English terminology
- Legacy Java and Spring Boot versions
- Legacy Hibernate dialect configuration (`PostgreSQL82Dialect`)
- Local configuration without environment-based configuration
- Business rules are directly implemented in controllers
- No explicit application/service layer
- Domain model is tightly coupled to persistence concerns
- Controllers have high responsibility and coupling