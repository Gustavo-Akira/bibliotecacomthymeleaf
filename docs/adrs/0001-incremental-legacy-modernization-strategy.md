# ADR-0001: Incremental Legacy Modernization Strategy

* Status: Approved
* Date: 2026-08-15

## Context

This project was originally developed approximately six years ago, early in my career, and was abandoned before reaching its original requirements.

The application is currently based on Java 11, Spring Boot 2.2.6, Thymeleaf, Hibernate, and PostgreSQL. Its architecture follows a modified MVC approach, where controllers are responsible for HTTP handling, application flow, and business rules, while models contain entity state and persistence relationships.

The codebase has no automated tests and was originally developed without pull requests, code reviews, or an automated development workflow. Configuration is also local and is not externalized through environment variables or other configuration mechanisms.

The application is now being resumed with the intention of completing its original requirements while progressively modernizing its codebase and architecture.

The application will be treated as if it were already running in production. Therefore, changes must account for the risks associated with modifying an existing system whose behavior is not fully protected by automated tests.

The modernization requires a strategy that allows the system to evolve incrementally while keeping the application runnable and providing sufficient validation before subsequent changes are introduced.

Existing behavior will generally be preserved during refactoring. However, behaviors identified as bugs may be corrected during the modernization process when the intended behavior can be established and the change is explicitly identified, tested, and validated.

## Decision

The application will be modernized incrementally, following a production-oriented development workflow.

Each significant change should be introduced as a small, independently reviewable change and should leave the application in a runnable state.

The modernization will prioritize establishing a sufficient safety net before making changes that could significantly affect the application's behavior or infrastructure.

The process will incorporate practices expected in a production environment, including automated tests, pull requests, code review, continuous integration, and incremental migration.

Refactoring should preserve existing behavior unless a behavior is explicitly identified as a bug. Bugs discovered during the modernization may be corrected as part of the migration, provided that the intended behavior is understood and the correction is explicitly tested and validated.

The modernization will be performed in phases rather than through a Big Bang rewrite. Specific technical decisions and implementation strategies for each phase will be documented in subsequent ADRs.

## Migration Strategy

The modernization will be performed through sequential phases. Each phase must reach a sufficiently stable and validated state before the next phase is started.

### Phase 1 — Establish a Safety Net

The first phase will focus on understanding and protecting the existing system behavior.

Automated tests will be introduced for the most important existing flows, prioritizing critical business behavior and areas likely to be affected by upcoming migrations.

These tests will initially focus on characterizing the existing behavior rather than redesigning it.

The goal is to establish a sufficient safety net to detect unintended regressions during subsequent migrations.

### Phase 2 — Java Migration

The Java runtime will be migrated incrementally from its current version to a newer supported version.

The migration will focus on compatibility and maintaining existing application behavior while addressing issues introduced by the new Java version and its ecosystem.

### Phase 3 — Spring Boot Migration

After the Java migration has been stabilized, Spring Boot and its related dependencies will be migrated incrementally.

The migration will address breaking changes, deprecated APIs, and compatibility issues while maintaining existing application behavior whenever possible.

### Phase 4 — Architectural Refactoring

Once the underlying platform has been modernized, the application architecture will be progressively refactored.

The main goal is to reduce coupling and establish clearer boundaries between application, domain, and infrastructure responsibilities.

Specific architectural decisions will be documented in separate ADRs.

### Phase 5 — Database Evolution

Database-related changes will be performed after the application architecture and persistence responsibilities have been sufficiently understood and improved.

Database changes will be introduced incrementally, avoiding a single large migration whenever possible.

The migration strategy will prioritize compatibility between the existing application and the evolving database structure.

## Alternatives Considered

### Big Bang Rewrite

Rebuild the application using a modern technology stack and architecture, replacing the existing implementation once the new version is considered complete.

This approach would provide greater freedom to redesign the system, but would require a large amount of work before the new implementation could replace the existing one. It would also make it difficult to identify whether differences in behavior are caused by intentional changes, bugs, or implementation differences.

This alternative was rejected because it introduces significant delivery and regression risk and does not align with the goal of treating the application as an existing production system.

### Incremental Modernization

Modernize the existing application through small, controlled changes, keeping the application runnable throughout the migration.

Each phase can be validated independently, allowing technical and behavioral problems to be identified closer to the change that caused them.

This approach was selected because it provides a safer path for evolving the existing system while preserving the ability to deliver and validate changes continuously.

### Strangler Fig Pattern

Gradually replace parts of the existing application with new implementations while keeping the legacy system running alongside the new architecture until the old components can be removed.

This approach provides strong isolation between legacy and modern implementations and can be appropriate for larger systems where a complete migration would be difficult.

It was considered but not selected as the primary strategy because the current application is small enough that maintaining two parallel architectures would add unnecessary complexity.

Individual techniques inspired by this pattern may still be used when appropriate during specific migrations.

## Consequences

### Positive

* Reduces the risk associated with modernizing an existing application.
* Allows problems to be identified closer to the change that introduced them.
* Establishes automated validation where none currently exists.
* Allows the application to remain runnable throughout the modernization.
* Provides opportunities to correct known bugs without requiring a separate rewrite.
* Makes architectural and technical decisions explicit and traceable.
* Allows the modernization process itself to simulate production engineering practices.

### Negative

* The modernization will take longer than a complete rewrite in the short term.
* Some temporary compatibility code may be required during migrations.
* The codebase may temporarily contain both legacy and modern approaches.
* Additional effort will be required to create and maintain automated tests.
* Multiple migration phases may require changes to dependencies and tooling before the final architecture is reached.

### Risks

* Existing behavior may be poorly understood due to the absence of automated tests.
* Characterization tests may unintentionally preserve existing bugs.
* Dependency upgrades may introduce unexpected behavior changes.
* A migration phase may reveal constraints that require changes to the planned sequence.
* The incremental approach may increase complexity temporarily before reducing it.

## Success Criteria

The modernization strategy will be considered successful if:

* The application remains runnable throughout the modernization process.
* Each migration phase can be completed and validated independently.
* Automated tests provide sufficient confidence to detect unintended behavioral regressions.
* Changes that alter existing behavior are explicitly identified and validated.
* Bugs discovered during the migration are corrected through explicit, tested changes.
* The application can be incrementally migrated without requiring a Big Bang rewrite.
* Each completed phase leaves the codebase in a stable state suitable for continuing the next phase.
* Technical and architectural decisions remain traceable through ADRs.

## Comments

This strategy is intentionally designed for this project as a controlled modernization exercise, simulating the constraints and risks of maintaining a production legacy application.

The migration phases may be adjusted when new technical constraints or dependencies are discovered. Significant changes to the migration strategy should be documented through an ADR update or a new ADR.
