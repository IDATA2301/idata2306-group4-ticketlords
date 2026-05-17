## Setup

1. Copy `.env.example` to `.env`
2. Fill in your actual values in `.env`
3. Run the application

Tests with postman:

Windows (cmd/powershell) (trur eg)
Run the program with: gradlew bootRun --args="--spring.profiles.active=test"

Unix (bash) (trur eg)
Run the program with: ./gradlew bootRun --args="--spring.profiles.active=test"

The springboot program connects to an alternative database, which is not used in production.
After the program has compiled, and opened it's port (8081) we can connect to it with postman to run the postman tests.

