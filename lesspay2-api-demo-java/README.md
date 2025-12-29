# Lesspay2 API Demo

This project demonstrates how to integrate with the Lesspay2 API, covering Payin and Payout functionalities. It includes utility classes for signature generation and JUnit test examples for various API endpoints.

## Project Structure

```
lesspay2-api-demo/
├── pom.xml
├── README.md
├── src/main/resources/
│   └── application.yml           # Configuration file
├── src/main/java/com/dpe/lesspay2/demo/
│   ├── ApiDemoApplication.java   # Spring Boot Application
│   ├── config/
│   │   └── ApiConfig.java        # API Configuration
│   ├── util/
│   │   └── SignUtil.java         # Signature Utility
│   ├── dto/                      # Data Transfer Objects
│   │   ├── CreatePayinOrderDTO.java
│   │   ├── CreatePayoutOrderDTO.java
│   │   ├── PayinQueryDTO.java
│   │   ├── PayoutQueryDTO.java
│   │   └── PayoutBankDTO.java
│   └── client/
│       └── LesspayApiClient.java # Client Wrapper
└── src/test/java/com/dpe/lesspay2/demo/example/
    ├── PayinExample.java         # Payin Create Order Example
    ├── PayinQueryExample.java    # Payin Query Order Example
    ├── PayoutExample.java        # Payout Batch Create Example
    ├── PayoutQueryExample.java   # Payout Query Order Example
    └── PayoutBankExample.java    # Payout Supported Banks Example
```

## Prerequisites

- Java 8 or higher
- Maven 3.6 or higher

## Configuration

Before running the examples, you must configure your `AppId`, `AppSecret`, and `BaseUrl`.

You can modify them  in the static constants within each Example class (e.g., `PayinExample.java`).


## Running Examples

The examples are implemented as JUnit tests. You can run them directly from your IDE or using Maven.

### 1. Payin Examples

- **Create Payin**:
  - Class: `com.dpe.lesspay2.demo.example.PayinExample`
  - Method: `testPayinDemo()`

- **Fetch Payin**:
  - Class: `com.dpe.lesspay2.demo.example.PayinQueryExample`
  - Methods: `testPayinQueryByRequestId()`, `testPayinQueryByPayOrderId()`

### 2. Payout Examples

- **Batch Create Payout**:
  - Class: `com.dpe.lesspay2.demo.example.PayoutExample`
  - Method: `testPayoutDemo()`

- **Fetch Payout**:
  - Class: `com.dpe.lesspay2.demo.example.PayoutQueryExample`
  - Methods: `testPayoutQueryByRequestId()`, `testPayoutQueryByPayOrderId()`

- **Payout Supported Banks**:
  - Class: `com.dpe.lesspay2.demo.example.PayoutBankExample`
  - Method: `testPayoutBankDemo()`

## Signature Algorithm (SHA256)

Security verification is enforcing by checking the signature of the HTTP request.

**Steps:**

1.  **Collect Parameters**: specific request parameters (excluding empty values).
2.  **Sort**: Sort keys in ASCII dictionary order.
3.  **Concatenate**: Join keys and values in `key=value` format, separated by `&`.
4.  **Append Key**: Append `&key=YOUR_SECRET` to the end of the string.
5.  **Hash**: Calculate the SHA256 hash of the string and convert to uppercase.

**Example:**

Parameters:
```json
{
    "target_amount": "100",
    "target_currency": "USD",
    "transaction_type": "PAY_IN"
}
```

Secret Key: `YOUR_SECRET_KEY`

String to Sign:
```
target_amount=100&target_currency=USD&transaction_type=PAY_IN&key=YOUR_SECRET_KEY
```

Signature: `SHA256(...).toUpperCase()`

For implementation details, refer to `src/main/java/com/dpe/lesspay2/demo/util/SignUtil.java`.

## JSON Serialization

Snake_case naming strategy is used for JSON serialization. All DTOs are annotated with `@JSONField(name = "...")` to ensure correct field mapping.
