# Legacy Application Baseline Findings

This document records behaviors, limitations, and incomplete
functionality observed while executing and manually exploring the
application before modernization.

The findings represent the current state of the application and are
not, by themselves, considered requirements or proposed solutions.

These findings may be updated as additional behavior is discovered
during characterization testing and modernization.

## Application Baseline

- The application successfully starts using the existing legacy stack.
- PostgreSQL can be provided locally through Docker.
- The application successfully connects to the database.
- Authentication is functional.
- Administrative access requires authorities using the `ROLE_*`
  convention expected by Spring Security.
- Administrative roles currently need to exist in the database before
  they can be assigned successfully.

## User Management

- Administrative users are currently created through direct SQL
  database manipulation.
- Roles are currently created through direct SQL database manipulation.
- The administrative user creation flow currently creates users with
  the `SECRETARY` role.
- The user management interface does not clearly communicate the role
  associated with each user.
- The external user registration flow is not currently functional.
- The USER profile edit action is not currently functional.
- The application can authenticate users successfully.

## Book Management

- Books can be created through the administrative dashboard.
- Books can be edited through the administrative dashboard.
- Books can be deleted through the administrative dashboard.
- A book can currently be created without a genre.
- Book genre cannot currently be edited through the available flow.
- The success page displayed after deleting a book contains a message
  indicating an insertion rather than a deletion.
- There is currently no action available to buy a book.
- There is currently no action available to rent a book.

## Publisher Management

- Publishers can be created through the administrative dashboard.
- Publishers can be edited through the administrative dashboard.
- Publishers can be deleted through the administrative dashboard.
- Publisher addresses cannot currently be edited through the available
  flow.

## Genre Management

- Genres can be created through the administrative dashboard.
- Genres can be deleted through the administrative dashboard.
- The genre deletion flow does not currently provide a clear success
  message.
- Genre editing is not currently functional in the book management
  flow.

## Dashboard

- The administrative dashboard is not fully implemented.
- The purchases section is not currently functional in the
  administrative area.
- The rentals section is not currently functional in the
  administrative area.
- Some administrative functionality is available even though the
  dashboard as a whole is incomplete.

## Error Handling

- Unhandled application errors are presented through the Spring Boot
  Whitelabel Error Page.
- Some errors leave the user on a technical error page without a
  user-friendly recovery or navigation path.

## User Interface

- Success pages generally display a textual success message without
  providing clear navigation to the next action.
- The individual book/publisher view can display a different navigation
  bar from the rest of the application.
- Some navigation items in the individual view do not have clear or
  functional actions.
- JavaScript-related navigation items are present without clear
  associated functionality.
- The books and publishers navigation may display a login option even
  when an authenticated USER is accessing the application.

## Incomplete Functionality

The following functionality was found to be unavailable or
incomplete during the baseline exploration:

- External user registration.
- User profile editing.
- Book purchasing.
- Book rental.
- Administrative purchase management.
- Administrative rental management.
- Book genre editing.
- Publisher address editing.
- Complete administrative dashboard functionality.

## Operational Observations

- Roles currently require direct database insertion.
- An administrative user currently requires direct database insertion
  to establish the initial administrative access.
- The current application relies on database state that is not
  established through an application-managed initialization flow.

## Notes

These findings describe observed behavior and should not be interpreted
as a definitive statement that every observed behavior is a bug.

In particular, behaviors such as allowing a book without a genre or
creating users with a specific role require further investigation
before being classified as incorrect behavior.

Characterization tests will progressively convert relevant observed
behavior into executable documentation.

Known bugs and incomplete functionality may be corrected during the
modernization process according to the strategy defined in
ADR-0001 and ADR-0002.