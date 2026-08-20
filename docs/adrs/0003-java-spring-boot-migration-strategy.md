# ADR-0003: Java and Spring Boot Migration Strategy

* Status: Proposed
* Date: 2026-08-19

## Context

As defined in ADR-0001, the application will be modernized incrementally while remaining runnable and validated throughout the migration.

The application currently runs on Java 11 and Spring Boot 2.2.6.RELEASE. Both the Java runtime and the Spring Boot version are outdated and must be migrated to supported versions as part of the modernization process.

The compatibility between Java and Spring Boot versions makes it undesirable to treat the Java and Spring Boot migrations as completely independent upgrades.

The current Spring Boot version imposes limitations on the Java versions that can be used safely and supported by the application. At the same time, modern Spring Boot versions require a newer Java baseline.

The final modernization target is a supported Java LTS release together with a current supported Spring Boot major version.

The migration must therefore define an incremental sequence that allows each platform change to be independently validated while avoiding a single large Java and Spring Boot upgrade.

The characterization tests established according to ADR-0002 will be used as the primary behavioral safety net throughout the migration.

## Decision

Java and Spring Boot will be migrated as an incremental platform migration rather than as two completely independent migrations.

The migration will use compatible intermediate states to progressively move the application from its current legacy platform to the final supported platform.

The migration will initially prioritize reaching a supported Spring Boot baseline while preserving Java 11 where practical, followed by the Java LTS migration and subsequent Spring Boot major-version migrations.

The planned migration path is:

```text
Java 11 + Spring Boot 2.2.6
        |
        v
Java 11 + Spring Boot 2.7.x
        |
        v
Java 17 + Spring Boot 2.7.x
        |
        v
Java 17 + Spring Boot 3.x
        |
        v
Java 21 + Spring Boot 3.x
        |
        v
Java 21 + Spring Boot 4.1.x
```

The exact patch and minor versions will be selected at implementation time based on the supported versions and compatibility requirements identified during each migration step.

Java 21 is currently considered the preferred final Java baseline because it is an LTS release and provides a stable modern runtime for the final platform.

The final Spring Boot version will be selected from the current supported stable major versions available when the migration reaches the final platform step.

Each migration step must:

* preserve existing application behavior whenever possible;
* keep the application buildable and runnable;
* pass the existing automated test suite;
* pass the CI pipeline;
* minimize unrelated refactoring;
* isolate compatibility changes from functional changes.

If a migration step reveals a constraint that makes the planned sequence unsafe or impractical, the sequence may be adjusted and the change must be documented.

## Migration Strategy

### Phase 1 — Current Baseline

The current platform will be treated as the baseline:

```text
Java 11
Spring Boot 2.2.6.RELEASE
```

The existing characterization tests and CI pipeline must pass before the platform migration begins.

### Phase 2 — Spring Boot 2.7 Baseline

Spring Boot will be upgraded from 2.2.6.RELEASE to a compatible 2.7.x release while keeping Java 11.

This step establishes a more recent Spring Boot baseline without introducing a Java runtime change at the same time.

Compatibility problems caused by the Spring Boot upgrade will be resolved before proceeding.

### Phase 3 — Java 17

Java will be upgraded from Java 11 to Java 17 while keeping the application on the established Spring Boot 2.7.x baseline.

The purpose of this step is to establish Java 17 as the minimum modern runtime baseline before migrating to the Spring Boot 3 generation.

Java compatibility issues will be resolved without introducing unrelated architectural changes.

### Phase 4 — Spring Boot 3

Spring Boot will be migrated from the 2.7.x generation to a supported 3.x release while keeping Java 17.

This phase will address the breaking changes introduced by the Spring Boot 3 generation, including framework and dependency changes required by the migration.

The migration will preserve existing application behavior whenever possible.

### Phase 5 — Java 21

After the application is stable on Spring Boot 3.x and Java 17, Java will be upgraded to Java 21.

This step establishes the preferred final Java LTS baseline independently from the subsequent Spring Boot major-version upgrade.

### Phase 6 — Current Spring Boot

The application will be migrated from Spring Boot 3.x to the current supported Spring Boot major version selected for the final platform.

The final migration will address compatibility requirements between the selected Spring Boot version, Java 21, and the application's dependencies.

The application must remain functionally equivalent unless an intentional behavior change is explicitly documented and tested.

## Validation

Every migration step will use the same validation sequence:

```text
Build
  |
  v
Application startup
  |
  v
Characterization tests
  |
  v
CI
  |
  v
Next migration step
```

A migration step must not proceed while known failures introduced by that step remain unexplained.

Failures that represent existing application behavior rather than migration regressions should be distinguished from failures introduced by the platform upgrade.

Known application bugs may remain documented in `findings.md` and do not automatically block the migration unless they prevent the application from being validated or make the affected migration unsafe.

## Alternatives Considered

### Direct Java and Spring Boot Upgrade

Upgrade Java and Spring Boot directly from the current versions to the final target platform.

This approach would minimize the number of migration steps but would combine several independent sources of incompatibility.

A failure could originate from the Java runtime, Spring Boot, Spring Security, Hibernate, dependencies, or application code, making diagnosis significantly more difficult.

This alternative was rejected because it conflicts with the incremental modernization strategy defined in ADR-0001.

### Java Upgrade Before Spring Boot

Upgrade Java from 11 directly to a modern LTS version while keeping Spring Boot 2.2.6.RELEASE.

Although this would provide a clean separation between the Java and Spring Boot changes, the compatibility constraints of the current Spring Boot version make this an undesirable migration path.

This alternative was rejected because it would introduce an unsupported or poorly supported intermediate platform merely to keep the migrations artificially independent.

### Upgrade to Spring Boot 2.7 and Stop

Upgrade Spring Boot to the 2.7 generation and keep the application on that platform.

This would reduce immediate migration risk and provide a newer framework baseline.

It was rejected because Spring Boot 2.7 is itself an old generation and does not satisfy the project's objective of reaching a current supported platform.

### Incremental Platform Migration

Migrate through compatible Java and Spring Boot versions while validating each intermediate state independently.

This approach requires more migration steps but reduces the scope of individual changes and makes failures easier to diagnose.

This alternative was selected because it best aligns with the incremental modernization strategy defined in ADR-0001.

## Consequences

### Positive

* Reduces the scope of individual platform migrations.
* Makes compatibility failures easier to attribute.
* Provides stable intermediate states during the modernization.
* Allows the existing characterization tests to validate behavior after each migration step.
* Reduces the risk of combining Java, Spring Boot, and dependency changes into a single migration.
* Establishes a modern LTS Java baseline before reaching the final Spring Boot platform.
* Keeps the migration consistent with the production-oriented approach defined in ADR-0001.

### Negative

* The migration will require multiple intermediate steps.
* The application will temporarily run on intermediate platform versions.
* Some compatibility work may need to be repeated across migration phases.
* The project will require CI validation after each platform change.
* The final migration will take longer than a direct upgrade.

### Risks

* Compatibility constraints may require changes to the planned sequence.
* Dependencies may prevent a clean upgrade to an intermediate version.
* Previously untested application paths may expose migration-specific failures.
* Spring Boot major-version upgrades may require changes across several framework integrations.
* Some existing application behavior may depend on deprecated or removed framework behavior.
* The target Spring Boot version may change before the migration reaches the final phase.

## Success Criteria

The platform migration will be considered successful if:

* The application reaches Java 21 version.
* The application reaches Spring boot 4.1.x.
* Each intermediate migration step is independently validated.
* The application remains buildable and runnable throughout the migration.
* The characterization test suite passes after each migration step, except for explicitly documented pre-existing failures.
* The CI pipeline passes after each migration step.
* Migration regressions can be distinguished from pre-existing application behavior.
* No unnecessary architectural refactoring is introduced solely as part of the platform migration.
* Existing functionality is preserved unless an intentional behavior change is explicitly documented and tested.

## Comments

This ADR defines the migration strategy and target platform direction. It does not prescribe the exact patch versions to be used at each step.

The planned sequence may be adjusted if compatibility testing reveals a safer migration path.

Significant changes to the migration strategy should be documented through an ADR update or a new ADR.
