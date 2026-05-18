This is the backend repository for our ticket booking project. It is a Spring Boot application hosted through DigitalOcean at this link: https://ticketlords-backend-app-ripdj.ondigitalocean.app 

To test the project locally, you can clone the repository and run the Spring Boot application as described in the ## Setup section below.


## Setup

This project uses PostgreSQL databases. You can easily check them directly with DBeaver, or other alternatives as long as you have the necessary credentials. DBeaver download link: https://dbeaver.io/download/
Pick the download which fits your system.

There are two application.properties in this file, one for testing and one for running normally.

When running the setup locally, we recommend using the following command in the terminal, for normal use:
Windows (cmd/powershell):
.\gradlew bootRun

Unix (bash):
./gradlew bootRun

As for running the project for testing, we recommend using this command instead:
Windows (cmd/powershell):
.\gradlew bootRun --args="--spring.profiles.active=test"

Unix (bash/zsh):
 ./gradlew bootRun --args="--spring.profiles.active=test"


Note however, you need to inject some environment variables, or the program won't run. If you dont have access to those variables, you will have to create your own. The variables are described in .env.example, however you need your own actual values for them.

You can set these environment variables by creating a new .env file in the root of this project, and setting the values in that file. You can use whatever text editor you'd like.

It should look something like this:
<img width="1627" height="514" alt="Image" src="https://github.com/user-attachments/assets/9891c6e9-75ab-47c9-b766-4bb3f02e5e93" />

You can now start the project locally, using either of the commands mentioned above.

## Tests with postman:
If you ran the program with either of these commands:
Windows (cmd/powershell)
.\gradlew bootRun --args="--spring.profiles.active=test"

Unix (bash/zsh)
./gradlew bootRun --args="--spring.profiles.active=test"

The Spring Boot program connects to the test database, which is not used in production.
After the program has compiled, and opened its port (8081) we can connect to it with postman to run the postman tests.

In postman:
Navigate to environments: and set BASE_URL=http://localhost:8081 

Then you can run the test collection, which can be downloaded from this link: 



