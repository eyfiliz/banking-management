# Banking Management

This project contains RESTful APIs which accepting messages from POS devices.

Endpoints:
- Make Payment
- Get Balance

Transaction has one of two message types: Payment and Adjustment

## **Restrictions**

Payment cannot be performed if :

- Account does not exist
- Transaction Id not null
- Payment amount is zero or negative
- Payment amount is greater than balance

Adjustment cannot be performed if :

- Account does not exist
- Transaction Id null
- Transaction Id does not match with the one in db
- Adjustment amount is zero
- Adjustment amount is greater than balance if it is positive
- Adjustment amount's absolute value is greater than old payment amount including sum of old adjustment amounts if it is negative

## **Libraries & Plugins used**

- Spring Boot
- Hibernate Validator
- Lombok
- Model Mapper
- Spring Boot Test

## **Database**

This service uses H2 database. When service starts, a few sample account data in data.sql file will be inserted into db.
