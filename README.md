# TestCard Shopping Cart System

## Project Overview

This Kotlin-based project models a **shopping cart system** that handles multiple products, categories, and various discount rules. The solution is designed to follow **Clean Architecture** and **SOLID principles**, ensuring separation of concerns between different layers (data, domain, and presentation) and making the code easier to test and maintain.

This project does **not require a UI**. The focus is on implementing the **business logic** and writing **unit tests** to cover the core functionalities and edge cases.

## Business Requirements

The main business requirements for the shopping cart system include:

1. **Multiple Products in a Basket**:
    - A basket can contain multiple products.
    - Products belong to a specific category (e.g., fruits, drinks, snacks).

2. **Discounts and Promotions**:
    - **General basket discount**: a percentage discount applied to the total basket (e.g., 10% off the total).
    - **Product specific discount**: a discount applied to a specific product (e.g., 20% off apples).
    - **Buy 2 get 1 free promotion**: for example, buy 2 apples, get the 3rd free.
    - **Combination deal**: a deal that applies to a specific combination of products (e.g., buy 1 sandwich, 1 apple, and 1 drink for a fixed price).

3. **Discount Rules**:
    - Only **one discount** can be applied to a product at a time.
    - Discounts should apply **as many times as they are valid** (e.g., a combination deal can apply multiple times for a larger basket).
    - The system should prioritize discounts based on a strategy defined by the business.

## Architecture Overview

The project is structured following **Clean Architecture**:

- **Data Layer**: contains repositories and data sources that handle interactions with external data. In this project, we simulate data without connecting to a real API.

- **Domain Layer**: contains business logic, including the use cases and models. The core logic for applying discounts and handling products and baskets is located here. All business rules and edge cases are handled at this level.

- **Presentation Layer (future scope)**: if a UI is later added, the domain logic is cleanly separated and can easily be connected to a presentation layer (using tools like Jetpack Compose).

## Use Cases

The following use cases drive the business logic of the shopping cart system:

1. **Add Products to Basket**:
    - add products to a shopping basket, associating them with categories and prices.

2. **Apply General Discount**:
    - apply a percentage discount to the total basket.

3. **Apply Product-Specific Discount**:
    - apply a discount to a specific product category or type.

4. **Apply Buy 2 Get 1 Free Promotion**:
    - for every 2 items purchased of a certain product, the customer gets a 3rd free.

5. **Apply Combination Deal**:
    - a combination of products (e.g., sandwich, apple, and drink) can be purchased for a special price.

6. **Calculate Final Price**:
    - calculate the final price after applying all valid discounts and promotions.

## Unit Tests

The business logic is tested using **MockK** for mocking dependencies, and each use case has corresponding unit tests to ensure correctness. The tests are focused on:

- Verifying that discounts are applied correctly according to the rules.
- Ensuring that no product receives more than one discount at a time.
- Checking edge cases, such as:
    - **Empty basket** (no products should result in no discounts).
    - **Invalid promotions** (ensuring that promotions don’t apply when conditions aren’t met).
    - **Multiple applications of a discount** (e.g., buy 6 apples, get 2 free).

Each test case is carefully crafted to ensure all business rules are respected and that the system behaves as expected under various conditions.

## Test Cases

The key test cases cover the following scenarios:

1. **Adding products** to the basket and verifying that they are stored correctly.
2. **Applying a general basket discount** and ensuring it reduces the total price.
3. **Applying a product-specific discount** and verifying that only the target product receives the discount.
4. **Verifying buy 2 get 1 free** logic by adding multiple quantities of a product.
5. **Handling combination deals**, ensuring that when the correct products are in the basket, the special price is applied.
6. **Ensuring discounts don't overlap** on the same product by testing that a product cannot receive both a general discount and a product-specific discount.

## Technologies Used

- **Kotlin**: the core programming language for building the system.
- **MockK**: for mocking dependencies and writing unit tests.
- **Hilt**: for dependency injection (to keep the system modular and testable).
- **JUnit**: for writing and running the unit tests.

## How to Run the Project

To run the project and tests:

1. Clone the repository to your local machine.
2. Sync the project to install the required dependencies.
3. Run the unit tests via the IDE or using Gradle:
   ```bash
   ./gradlew test
