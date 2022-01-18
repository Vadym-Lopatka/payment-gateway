# Payment Gateway

## Assignment

Implement a backend for a simple payment gateway that accepts credit card payments.
It needs to perform some basic input validation and apply PCI mandated restrictions
on data handling. This is a rough sample of what a real payment gateway actually does.
It may take an afternoon or two to complete, take it at your own pace and use any
resources you like. We understand that people are busy, so it’s up to the you to
choose which part of the task deserves more focus if you wish to be selective. 

### Structure

* Java
* Maven
* Spring Boot

### Delivery

* create a Github repository with your project
* build the project up with a few of incremental commits with meaningful messages
* include a `README.md` with the project description and build/test/usage instructions
* showcase your usage of developer conventions, project structure, comments, etc.

### Expectations

Most of the features can be easily put together using popular frameworks like Spring,
Jackson, Hibernate, etc. The task is rather simple, but what we're looking for is a feel
of quality and readability of code, project structure, tests, etc. Treat the assignment
as a regular work project that you could come back to a year after, and make it easy on
your future self to understand the code, make changes and avoid regression after
introducing new features.

If you find the spec to be a little vague, or have any other questions, do not hesitate to ask.

## Requirements

The gateway accepts credit card payments for processing and maintains a transaction history.

### REST API

A RESTful web API shall be provided. Use the `application/json` media type and the
appropriate HTTP status codes for all responses.

The following two endpoints should be offered:

| Use case | Input | Action | Response |
| --- | --- | --- | --- |
| 1. Submit a payment | Payment request JSON (see below) | Accept a request, validate input, save, write to audit | Approval indicator and/or errors |
| 2. Retrieve a prior transaction | Invoice number from the original request | Find the transaction in the database | Transaction JSON, sanitized per PCI restrictions |

### Request Format

Data structure, used for the original request, audit output and for transaction retrieval.
```
{
  invoice: 1234567,
  amount: 1299,
  currency: 'EUR',
  cardholder: {
    name: 'First Last',
    email: 'email@domain.com'
  },
  card: {
    pan: '4200000000000001',
    expiry: '0624',
    cvv: '789'
  }
}
```

### Validation

Before the transaction is approved, the following conditions must be met:

* all fields should be present;
* amount should be a positive integer;
* email should have a valid format;
* PAN should be 16 digits long and pass a Luhn check;
* expiry date should be valid and not in the past.

### Persistence

Use any storage solution that gets the job done for this assignment. A simple map,
an embedded database, or another in memory option is fine, as long as it allows
you to look up past transactions for Use Case #2.

Leave some room for making it easier to change persistence implementation in the future.

Apply PCI restrictions when saving data (see below).

### Audit

The gateway needs to stream the transaction to an external source for audit.
To make things simple, let's write it to a local file `audit.log` in JSON format, one record per line.
Make the path configurable.

### PCI Restrictions

PCI regulations require us to encrypt or mask certain fields while in storage or transit.

Use a simple Base64 encoder as mock encryption for this assignment.

| Field | Database: Encrypt | Audit and REST response: Sanitize | Example Input | Sanitized Output |
| --- | --- | --- | --- | --- |
| cardholder name| must be encrypted | mask all characters | First Last | ********** |
| PAN | must be encrypted | mask all characters, except last 4 digits| 4200000000000001 | ************0001 |
| expiry date | must be encrypted | mask all characters | 0624 | **** |
| CVV | do not persist | do not include | 789 | ~~excluded~~ |

### Response Format

Successful transaction:
```
{
  approved: true
}
```

Declined transaction example:
```
{
  approved: false,
  errors: {
    currency: 'Currency is required.',
    email: 'Invalid cardholder email format.',
    expiry: 'Payment card is expired.'
  }
}
```

Transaction response and audit:
```
{
  invoice: 1234567,
  amount: 1299,
  currency: 'EUR',
  cardholder: {
    name: '**********',
    email: 'email@domain.com'
  },
  card: {
    pan: '************0001',
    expiry: '****'
  }
}
```
