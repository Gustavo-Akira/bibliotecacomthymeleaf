# ADR-0004: Incremental Architectural Refactoring Strategy

* Status: Approved
* Date: 2026-08-22

## Context

The application currently follows a modified MVC architecture inherited from its original implementation.

Controllers are responsible for HTTP handling, application flow, and parts of the business logic, while JPA entities are used directly throughout the application and persistence concerns are closely coupled to the application flow.

The modernization process has already established a safety net through characterization tests and CI and has completed the Java and Spring Boot platform migration defined in ADR-0003.

With the platform modernization completed, the next phase defined by ADR-0001 is architectural refactoring.

The existing architecture makes responsibilities difficult to isolate and increases the cost and risk of changing business behavior. Business rules, HTTP concerns, persistence operations, and entity manipulation are frequently implemented within the same application flow.

The architectural refactoring must therefore improve separation of responsibilities without requiring a Big Bang rewrite.

The existing application will continue to be treated as a production-like legacy system. Existing behavior should be preserved unless a behavior is explicitly identified as a bug and intentionally corrected.

## Decision

The application will be refactored incrementally toward clearer architectural boundaries.

The refactoring will separate responsibilities into distinct layers and responsibilities, while allowing the existing MVC implementation to coexist temporarily with the new structure.

The target architecture will establish clear boundaries between:

- Presentation / HTTP handling
- Application / use-case orchestration
- Domain / business rules
- Infrastructure / persistence and external concerns

Controllers should progressively become responsible primarily for HTTP concerns and delegation to application services or use cases.

Business rules should progressively be removed from controllers and placed in appropriate application or domain components.

Persistence-specific concerns should remain isolated from business logic as the architecture evolves.

The refactoring will not require an immediate rewrite of the existing application.

New or modified functionality should preferably follow the new architectural boundaries, while existing functionality will be migrated incrementally when there is sufficient justification and test coverage.

## Refactoring Strategy

Architectural changes will be introduced through small, independently reviewable changes.

Each refactoring step should:

1. Preserve existing behavior.
2. Keep the application runnable.
3. Maintain or improve automated test coverage.
4. Avoid mixing architectural refactoring with unrelated functional changes.
5. Leave the codebase in a stable state.

The existing characterization tests will be used as a safety net during the migration.

When a flow is migrated to a new architectural boundary, its existing behavior should remain covered by tests before the legacy implementation is removed.

## Target Boundaries

### Presentation

Responsible for:

- HTTP requests and responses.
- Request parameters and validation related to the HTTP boundary.
- Authentication and authorization integration.
- Selecting the appropriate view or response.

Controllers should not contain business rules or direct persistence orchestration when the corresponding flow has been migrated.

### Application

Responsible for:

- Use-case orchestration.
- Coordinating domain operations.
- Managing application-level workflows.
- Defining the interaction between domain logic and infrastructure ports.

Application services should not depend on HTTP-specific concepts.

### Domain

Responsible for:

- Business rules.
- Business invariants.
- Domain concepts and behavior.
- Decisions that should remain independent of the delivery mechanism and persistence technology.

The domain should not depend directly on Spring MVC, controllers, or infrastructure implementations.

### Infrastructure

Responsible for:

- Database access.
- Spring Data JPA repositories and adapters.
- External services.
- Framework-specific implementations.
- Technical concerns required to support application and domain behavior.

Infrastructure dependencies should not leak into domain rules.

## Migration Approach

The migration will be performed flow by flow rather than by package-wide rewriting.

A typical migration should follow:

```text
Existing controller flow
        ↓
Characterization tests
        ↓
Identify responsibilities
        ↓
Extract application/use-case boundary
        ↓
Extract domain rules where appropriate
        ↓
Isolate persistence concerns
        ↓
Update tests
        ↓
Remove obsolete controller logic
```

The first refactoring targets should be selected based on:

- Business importance.
- Complexity.
- Coupling.
- Frequency of change.
- Existing test coverage.
- Risk of regression.
- Value provided by establishing a clearer architectural boundary.

## Alternatives Considered

### Big Bang Architectural Rewrite

Replace the existing MVC implementation with a completely new architecture in a single migration.

This would provide greater freedom to redesign the system but would require migrating all functionality before the new architecture could be validated.

It would also make it difficult to distinguish architectural changes from behavioral changes.

This alternative was rejected because it conflicts with the incremental modernization strategy established in ADR-0001.

### Continue With the Existing MVC Architecture

Keep the current architecture and only perform platform and dependency upgrades.

This would minimize short-term changes but would leave the existing coupling and responsibility boundaries unresolved.

This alternative was rejected because the purpose of the next modernization phase is specifically to reduce architectural coupling and improve maintainability.

### Immediate Full Hexagonal Architecture Migration

Rewrite the entire application around ports and adapters immediately.

This would establish strong architectural boundaries but would introduce significant structural change before the existing flows have been fully understood.

It was rejected as the initial strategy because it would increase migration risk and could turn the architectural refactoring into another Big Bang migration.

Hexagonal architecture concepts may be adopted incrementally where they provide clear value.

## Consequences

### Positive

- Reduces coupling between HTTP, business logic, and persistence.
- Makes business rules easier to test independently.
- Makes future changes safer and more localized.
- Provides clearer responsibilities for application components.
- Allows architectural improvements without rewriting the entire application.
- Creates a path toward more maintainable and testable code.

### Negative

- The application will temporarily contain multiple architectural styles.
- Some flows may require additional mapping or orchestration code.
- Refactoring will require maintaining both old and new implementations during transitions.
- The overall modernization will take longer than a complete rewrite in the short term.

### Risks

- Poorly understood business rules may be moved to the wrong layer.
- Abstractions may be introduced before their value is clear.
- Temporary architectural duplication may increase complexity.
- Refactoring may accidentally change existing behavior.
- Excessive abstraction could make a small application unnecessarily complex.

## Success Criteria

The architectural refactoring will be considered successful if:

- Controllers progressively contain less business logic.
- Business rules can be tested without requiring HTTP infrastructure.
- Persistence concerns are isolated from business rules.
- New functionality can be implemented without increasing legacy coupling.
- Existing behavior remains protected by automated tests.
- Architectural changes can be introduced incrementally.
- The application remains runnable throughout the refactoring.
- The resulting architecture provides clearer and more maintainable boundaries.

## Comments

This ADR establishes the architectural refactoring strategy rather than prescribing a complete final architecture.

Specific architectural decisions, such as the adoption of ports and adapters, domain-driven design patterns, module boundaries, repository abstractions, or package organization, should be documented in subsequent ADRs when they become necessary.

The architectural refactoring should preserve the incremental and production-oriented modernization strategy established in ADR-0001.
