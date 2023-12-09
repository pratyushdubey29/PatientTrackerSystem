# Patient Tracker System

## 1. Project Description

The Patient Tracker System is a comprehensive solution for medical institutions to efficiently manage patient information, doctor details, appointments, and medical cases. It offers a user-friendly interface for doctors and staff to streamline their workflow and enhance patient care.

## 2. System and Library Requirements

Before running the Patient Tracker System, ensure that your system meets the following requirements:

- Java Development Kit (JDK) version 8 or higher
- Apache Maven for building and managing the project
- Spring Boot framework
- MySQL relational database

### Recommended (Our) Versions
TODO: Update Java version
1. Java:  `21.0.1`
2. SpringBoot:  `3.1.5`
3. Maven:  `3.9.5`
4. Mysql:  `8.1.0`


## 3. How to Run the Project Locally

Follow these steps to run the Patient Tracker System on your local machine:

#### Clone the Repository

```
git clone https://github.com/pratyushdubey29/PatientTrackerSystem

cd PatientTrackerSystem 
```
#### To run the application:
1. Make sure that all the dependencies are installed with appropriate versions as mentioned above.
2. script.sql in mysql environment/cli
3. To clear the cache: `mvn clean`
4. To install the dependencies: `mvn clean install`
5. To run the application: `mvn spring-boot:run`
6. The application will be accessible on the port 8080.

#### Test an API.
1. We will be using Postman to make HTTP requests to our server in order to test out our APIs.
2. Curl for a sample working API: `curl --location 'localhost:8080/doctors'` 

#### Java JDK not installed or misconfigured
If a Java JDK is not installed or properly configured on your system, you may encounter the following error:
```
[INFO] BUILD FAILURE
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:3.1.5:run (default-cli) on project PatientTrackerSystem: Process terminated with exit code: 1
```
Make sure that you have a JDK installed and that the JAVA_HOME environment variable is properly set.


## 4. High-Level Component Descriptions

#### Controllers
- DoctorController: Handles operations related to doctors, including retrieval, filtering, and signup functionalities.
- PatientController: Manages patient-related operations, such as patient retrieval, registration, and deletion.

#### Models
- Appointment: Represents appointments associated with a case, patient, and doctor.
- Case: Represents a medical case, including patient and doctor information, symptoms, and prescribed medicines.
- Doctor: Represents a doctor with details like name, specialty, and approval status.
- DoctorsLogin: Manages login information specific to doctors.
- Patient: Represents a patient with details like name, address, and contact information.
- PatientsLogin: Manages login information specific to patients.
- UserLogin: Abstract class representing login information for users.
- UserLoginKey: Represents the composite key for user login information.

#### Repositories
- AppointmentRepository: Manages appointments in the database, allowing retrieval based on doctor, patient, date, and time.
- CaseRepository: Handles medical cases, providing methods for retrieval based on patient, doctor, and case ID.
- DoctorRepository: Manages doctor entities, allowing searches based on speciality, name, and address.
- DoctorSignupRepository: Manages doctor signup data, extending JpaRepository for basic CRUD operations.
- PatientRepository: Manages patient entities, allowing retrieval based on patient ID and email.
- PatientSignupRepository: Manages patient signup data, extending JpaRepository for basic CRUD operations.

#### Main Application
- PatientTrackerSystemApplication: Main class for running the Patient Tracker System Spring Boot application.
