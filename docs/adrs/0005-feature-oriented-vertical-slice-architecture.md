# ADR-0005: Feature-Oriented Vertical Slice Architecture

* Status: Approved
* Date: 2026-08-22

## Context

ADR-0004 establishes the incremental architectural refactoring strategy for the application.

The existing application follows a modified MVC architecture in which controllers, business logic, persistence operations, and entity manipulation are frequently coupled.

The application is now ready to begin the architectural refactoring phase after completing the platform modernization defined by ADR-0003.

A full horizontal migration of the application would require restructuring all controllers, services, repositories, and models before the benefits and trade-offs of the new architecture could be validated.

Because the application is a legacy system with incomplete historical documentation, the target architecture should be introduced incrementally and validated through real business flows.

The application also needs to support temporary coexistence between legacy and modernized components during the migration.

## Decision

The application will adopt a **feature-oriented vertical slice architecture** as the target structure for the incremental architectural refactoring.

Each business feature or bounded context will own the components required to implement its functionality.

A migrated feature may contain the following architectural boundaries:

```text
feature/
├── presentation/
├── application/
├── domain/
└── infrastructure/
```

The exact presence of each layer will depend on the responsibilities and complexity of the feature. Layers and abstractions should not be introduced when they provide no meaningful architectural value.

The architecture will prioritize clear dependency boundaries over a uniform number of classes or abstractions.

### Vertical Slice Migration

Architectural migration will be performed one feature at a time.

The first migrated feature will serve as the reference implementation for subsequent migrations.

The initial target will be **Genre**, because its functionality is relatively isolated and already has characterization tests covering its main existing flows.

The migration will allow legacy and modernized features to coexist.

The legacy application will not be reorganized solely for structural consistency before its features are migrated.

## Architectural Boundaries

### Presentation

Responsible for interaction with external delivery mechanisms, primarily HTTP.

Responsibilities include:

- Controllers.
- Request and response handling.
- HTTP-specific DTOs.
- HTTP validation.
- Authentication and authorization integration.
- View or response selection.

Presentation components may depend on the Application layer but should not contain business rules.

### Application

Responsible for executing application use cases and coordinating the flow between domain and infrastructure.

Responsibilities include:

- Use cases.
- Application services where required.
- Application-level orchestration.
- Transaction boundaries where appropriate.
- Coordination of domain operations.

Application components should not depend on HTTP-specific concerns.

### Domain

Responsible for business concepts and business rules.

Responsibilities may include:

- Entities.
- Value objects.
- Business invariants.
- Domain services when required.
- Domain-level ports when an abstraction is required.

The Domain layer should remain independent of Spring MVC, HTTP, and infrastructure implementations.

Domain models should not be forced to contain abstractions that do not represent meaningful domain concepts.

### Infrastructure

Responsible for technical implementations required by the application.

Responsibilities include:

- Spring Data JPA.
- Database access.
- Repository implementations.
- Persistence mappings where required.
- External service integrations.
- Framework-specific implementations.

Infrastructure may depend on Application and Domain abstractions to provide their implementations.

Infrastructure details should not leak into the Domain layer.

## Dependency Direction

The preferred dependency direction is:

```text
Presentation
      ↓
Application
      ↓
Domain
      ↑
Infrastructure
```

Infrastructure provides implementations for abstractions required by the Application or Domain.

The following dependencies should be avoided:

```text
Domain → Spring MVC
Domain → Spring Data
Domain → HTTP
Domain → PostgreSQL

Application → Controller
Application → HTTP Request/Response
```

The architecture should establish dependency boundaries based on responsibility rather than package naming alone.

## Persistence

Persistence will not be completely separated from domain models by default.

The migration will evaluate the existing JPA entity model feature by feature.

A persistence-specific model, mapper, or adapter should only be introduced when the separation provides a meaningful benefit, such as:

- Preventing persistence concerns from leaking into business rules.
- Protecting domain invariants.
- Reducing persistence coupling.
- Supporting a domain model that differs materially from the database representation.

The architecture therefore does not mandate duplication of domain entities and JPA entities for every feature.

## Use Cases

Use cases will represent meaningful application actions rather than CRUD operations created solely for structural consistency.

For example, a feature may expose:

```text
CreateGenre
UpdateGenre
DeleteGenre
```

when these represent meaningful application operations.

However, if a feature has trivial behavior and introducing separate classes would only add indirection, the application layer may use a simpler structure.

The goal is to make application responsibilities explicit, not to maximize the number of abstractions.

## Repository Boundaries

Persistence abstractions will be introduced when they establish a useful architectural boundary.

When a repository port is required, the preferred direction is:

```text
Application / Domain
        ↓
Repository Port
        ↑
Repository Adapter
        ↓
Spring Data JPA
```

Spring Data repositories should not become the primary abstraction exposed directly to domain logic when doing so would couple the domain to the persistence framework.

The need for repository ports will be evaluated feature by feature.

## Package Organization

The preferred package organization is by business feature rather than by global technical layer.

The target structure is conceptually:

```text
br.com.biblioteca
│
├── genero
│   ├── presentation
│   ├── application
│   ├── domain
│   └── infrastructure
│
├── livro
│   ├── presentation
│   ├── application
│   ├── domain
│   └── infrastructure
│
├── editora
│   ├── presentation
│   ├── application
│   ├── domain
│   └── infrastructure
│
└── usuario
    ├── presentation
    ├── application
    ├── domain
    └── infrastructure
```

This structure represents the target organization. Legacy features may continue using the existing package structure until they are migrated.

## Migration Strategy

Each feature migration should be performed as an independently reviewable vertical slice.

The expected process is:

```text
Existing feature
       ↓
Characterization tests
       ↓
Identify responsibilities
       ↓
Define feature boundary
       ↓
Introduce target architecture
       ↓
Move/extract application logic
       ↓
Isolate domain rules
       ↓
Isolate infrastructure concerns
       ↓
Update tests
       ↓
Validate behavior
       ↓
Remove obsolete legacy implementation
```

The migration should not require the entire application to adopt the target architecture before the first feature can be completed.

The first implementation will be **Genre**.

Genre will be used to validate:

- Feature-oriented package organization.
- Presentation/Application/Domain/Infrastructure boundaries.
- Use-case organization.
- Repository boundaries.
- Test strategy.
- Coexistence with the remaining legacy application.

## Testing

Characterization tests will remain the primary safety net for preserving existing behavior during migration.

The migrated feature should additionally introduce tests appropriate to its new boundaries.

Where practical:

- Presentation behavior should be validated through controller/HTTP tests.
- Application behavior should be testable without HTTP infrastructure.
- Domain rules should be testable without Spring infrastructure.
- Infrastructure behavior should be tested against the persistence mechanism where necessary.

The migration should not remove existing characterization coverage until equivalent behavior has been preserved by the new implementation.

## Alternatives Considered

### Horizontal Layer Migration

Reorganize the entire application into global technical layers:

```text
presentation/
application/
domain/
infrastructure/
```

and migrate all existing features into those layers.

This approach provides immediate structural consistency but requires broad changes across the application before the architecture can be validated.

It was rejected because it increases migration scope and makes it more difficult to isolate regressions.

### Big Bang Architectural Rewrite

Replace the existing MVC implementation with the target architecture in a single migration.

This was rejected because it conflicts with the incremental modernization strategy established by ADR-0001 and ADR-0004.

### Feature-Oriented Vertical Slices

Migrate one business feature at a time while keeping the target architectural boundaries inside each feature.

This was selected because it:

- Limits the scope of each change.
- Allows legacy and modernized code to coexist.
- Provides an executable validation of the architecture.
- Makes rollback easier.
- Allows architectural decisions to be refined based on real implementation experience.
- Reduces the risk of introducing unnecessary abstractions across the entire application.

### Immediate Full Hexagonal Architecture

Apply ports and adapters comprehensively to every feature before migration begins.

This was rejected as a mandatory structure because not every feature necessarily requires the same degree of abstraction.

Hexagonal principles may be applied where they establish useful boundaries.

## Consequences

### Positive

- Architectural migration can happen incrementally.
- Each feature has clearer ownership of its application, domain, and infrastructure concerns.
- Legacy and modernized code can coexist.
- Architectural decisions can be validated through real implementation.
- The scope of individual pull requests remains manageable.
- Business rules become easier to test independently.
- The application becomes progressively less coupled to Spring and persistence concerns.

### Negative

- The repository will temporarily contain multiple architectural styles.
- Some features may have different levels of architectural maturity during the migration.
- Temporary duplication may exist between legacy and modernized components.
- Feature boundaries may require refinement as the domain becomes better understood.
- Additional mapping and abstraction may be required for some features.

### Risks

- Features may be split according to technical concerns instead of meaningful business boundaries.
- Vertical slices may introduce inconsistent patterns if the target boundaries are not followed consistently.
- Excessive abstraction may still occur within individual features.
- Legacy dependencies may leak into new features during migration.
- The first feature may reveal architectural decisions that need to be revised.

## Success Criteria

The target architecture will be considered validated when the Genre feature demonstrates that:

- The feature can be isolated from the legacy controller structure.
- Presentation concerns are separated from application logic.
- Application responsibilities are explicit.
- Domain logic does not depend on HTTP or Spring infrastructure.
- Persistence concerns are isolated behind appropriate boundaries.
- Existing Genre behavior remains protected by automated tests.
- Application-level behavior can be tested without requiring HTTP infrastructure where appropriate.
- The resulting structure can be applied to subsequent features without introducing unnecessary complexity.

## Comments

This ADR defines the target architectural direction and migration structure.

The Genre feature will be the first implementation of this architecture and will be used to validate the decisions made here.

Architectural decisions discovered during the Genre migration that materially change the target architecture should be documented through an update to this ADR or a subsequent ADR.

The architecture should evolve based on demonstrated needs of the application rather than theoretical completeness.
