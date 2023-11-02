### Versions
1. Java: 21.0.1
2. SpringBoot: 3.1.5
3. Maven: 3.9.5
4. Mysql: 8.1.0

### To run the application:
1. Make sure that all the dependencies are installed with appropriate versions as mentioned above.
2. script.sql in mysql environment/cli
3. To clear the cache: `mvn clean`
4. To install the dependencies: `mvn clean install`
5. To run the application: `mvn spring-boot:run`
6. The application will start running on the port 8080.

### Test an API.
1. We will be using Postman to make HTTP requests to our server in order to test out our APIs.
2. Curl for a sample working API: `curl --location 'localhost:8080/doctors'` 

#### Java JDK not installed or misconfigured
If a Java JDK is not installed or properly configured on your system, you may encounter the following error:
```
[INFO] BUILD FAILURE
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:3.1.5:run (default-cli) on project PatientTrackerSystem: Process terminated with exit code: 1
```
Make sure that you have a JDK installed and that the JAVA_HOME environment variable is properly set.
