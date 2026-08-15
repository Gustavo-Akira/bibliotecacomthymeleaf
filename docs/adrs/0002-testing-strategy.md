# ADR-0002: Legacy Application Testing Strategy

* Status: Approved
* Date: 2026-08-15

## Context

The application currently has no automated tests. Its behavior has
historically been validated through manual testing during development.

The application contains business rules directly within controllers,
persistence concerns within the model, and infrastructure concerns
within repositories. This makes the current behavior difficult to
validate independently and increases the risk of unintended
regressions during the modernization process.

As defined in ADR-0001, the application will be treated as if it were
already running in production and will be modernized incrementally.
Automated tests are therefore required before making changes that may
significantly affect the application's behavior or infrastructure.

Because the application has no existing test suite, introducing tests
also requires understanding the behavior currently implemented by the
system. Some existing behaviors may represent intentional business
rules, while others may be accidental behavior or bugs.

There is currently no automated specification of the application's behavior.

The testing strategy must therefore provide confidence for
modernization without turning the initial testing phase into a
complete rewrite of the application.

## Decision

The application will adopt a hybrid testing strategy combining
characterization tests with unit and integration tests.

Characterization tests will be introduced first for critical existing
flows to establish the current observable behavior of the application
before significant refactoring or migration takes place.

As responsibilities are progressively extracted from controllers,
models, and repositories, unit tests will be introduced for the
resulting components and their business rules.

Integration tests will be introduced where behavior depends on
interactions between application components, persistence, or external
infrastructure.

The test suite will evolve alongside the modernization rather than
being completed before the migration begins.

Tests will initially describe existing behavior where that behavior is
not known to be incorrect. When a behavior is identified as a bug, the
corresponding test may be changed to represent the intended behavior
before or together with the bug fix.

The testing strategy will prioritize confidence in changes over
achieving a predefined code coverage target.

## Testing Strategy

The test suite will be introduced incrementally alongside the
modernization process.

### Phase 1 — Characterization Tests

Characterization tests will be created around critical application
flows using the application's existing interfaces and behavior.

The purpose of these tests is to establish an executable description
of how the system currently behaves before significant changes are
introduced.

At this stage, the tests should avoid requiring structural changes to
the application solely for the purpose of making it easier to test.

### Phase 2 — Unit Tests

As responsibilities are extracted from controllers, models, and other
highly coupled components, unit tests will be introduced for the
resulting units.

Unit tests will focus primarily on business rules and behavior that
can be validated independently from infrastructure.

This phase will evolve together with the architectural refactoring
rather than attempting to unit test the existing architecture in its
entirety.

### Phase 3 — Integration Tests

Integration tests will be introduced for behavior that depends on
interactions between application components or infrastructure.

This includes persistence, database interactions, and other
integration boundaries where unit tests alone cannot provide
sufficient confidence.

### Phase 4 — Test Refinement

As the application architecture and behavior become better understood,
existing tests will be progressively improved.

Characterization tests that initially captured implementation-driven
behavior may be replaced or complemented by tests that express the
intended business behavior.

Tests that describe known bugs will be updated as part of the
corresponding bug correction.

## Alternatives Considered

### Characterization Tests Only

Introduce tests only around existing application flows and use them as
the primary safety net throughout the modernization.

This approach would require fewer structural changes to the application
at the beginning and would provide protection against regressions in
observable behavior.

It was not selected because it would provide limited protection for
business rules and individual components as the architecture evolves.

### Unit Tests First

Refactor the existing application enough to make its components
independently testable and introduce unit tests before establishing
characterization tests around the existing behavior.

This approach would encourage a more testable architecture from the
beginning.

It was not selected because it requires modifying the existing
architecture before establishing confidence in its current behavior.
This could cause existing behavior to be changed or lost without a
reliable baseline for comparison.

### Hybrid Testing Strategy

Introduce characterization tests for critical existing flows first,
then progressively introduce unit and integration tests as
responsibilities and boundaries are extracted during the modernization.

This approach provides an initial safety net for existing behavior
while allowing the test strategy to evolve together with the
architecture.

This alternative was selected because it balances protection of the
legacy system with the need to improve testability during the
refactoring process.

## Consequences

### Positive

* Provides an initial safety net around existing application behavior.
* Allows the modernization to begin without requiring the entire
  application to become independently testable first.
* Enables business rules to gain more focused tests as responsibilities
  are extracted from highly coupled components.
* Allows integration boundaries to be validated independently from
  individual business rules.
* Helps distinguish existing behavior from intended behavior as the
  system is progressively understood.
* Improves testability as part of the architectural modernization rather
  than treating it as a separate effort.

### Negative

* The test suite will temporarily contain different testing approaches
  as the architecture evolves.
* Some characterization tests may become obsolete after architectural
  changes and will need to be replaced or removed.
* Introducing tests around legacy flows may require working with
  tightly coupled components and infrastructure.
* Some behaviors may be difficult to test without first making limited
  changes to the existing code.
* Maintaining the test suite will add development effort throughout the
  modernization.
* Characterization tests may initially describe behavior without
    expressing the desired design or business model.

### Risks

* Characterization tests may encode accidental behavior or existing
  bugs.
* Tests coupled too closely to implementation details may become
  unnecessarily fragile during refactoring.
* Integration tests may become slow or dependent on local
  infrastructure if their boundaries are not carefully defined.
* The absence of tests in parts of the application will continue to
  represent risk until those areas are progressively covered.

## Success Criteria

The testing strategy will be considered successful if:

* Critical existing application flows are protected by automated tests
  before the corresponding areas undergo significant modernization.
* Tests provide sufficient confidence to identify unintended behavioral
  regressions during migration and refactoring.
* Business rules introduced or extracted during the modernization are
  covered by appropriate automated tests.
* Integration boundaries that cannot be reliably validated through unit
  tests are covered by integration tests.
* Tests distinguish between existing behavior and intentionally changed
  behavior.
* Known bugs corrected during the modernization are represented by tests
  that validate their intended behavior.
* The test suite can evolve alongside the architecture without
  unnecessarily preserving legacy implementation details.
* The testing strategy does not require completing the entire test suite
  before subsequent modernization phases can begin.


## Comments

The testing strategy is expected to evolve as the application becomes
better understood and its architecture changes.

Testing decisions that introduce significant changes to this strategy
should be documented through an ADR update or a new ADR.