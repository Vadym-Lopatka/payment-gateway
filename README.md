# Payment Gateway

Hello guys :)

The assignment's requirements were fully implemented.

### Service API
Service run on port 8080

#### Endpoints
POST /api/transactions - create transaction

GET /api/transactions/{transactionId} - get transaction by id(invoice)

#### Dependencies
Java14, SpringBoot(web, test), Lombok and Hibernate-validator impl

### Implementation details

#### DB or Storage
I decided to use just ConcurrentMap.

But as was asked, I implemented it in a loosely-coupled way, so it can be easily extended with different persistence storage.

#### Entities in controllers
There are a lot of controversial approaches on this topic, but, in my opinion,
for simple cases(like test task:)) it is absolutely ok to use entities on controller level.

On the other side, I am absolutely sure that for complex(real) projects we should clearly divide
these levels by using a RequestDto instead an Entity itself.

#### Test
I believe that the main and important aspects were well tested using unit and integration testing approaches.

Nevertheless, for prod-like code, the test coverage should be even higher.
