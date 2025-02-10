# Challenge
** This repository is only for evaluating my skillset **

This microservice has two endpoints:
POST ("/execute-operation")
GET ("/fetch-operations")
Both connected to a PostgreSQL database which records and retrieves the operations that a user could execute.
The first endpoint only adds the two given numbers (more details in the swagger documentation) and multiplies that addition by an external factor. This factor will be cached for 30 minutes and used if the external api isn't responding after 3 attempts to connect. 
The second endpoint will fetch the operations that have been executed (including previous executions of this endpoint) with a system of pagination (the user can enter the page and its size).
Both endpoints can't be executed too often.
