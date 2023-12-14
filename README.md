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
1. Java:  `11.0.16`
2. SpringBoot:  `2.3.5`
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
2. Curl for a sample working API: `curl --location 'localhost:8080/doctors/signup' \
--header 'UserType: doctor' \
--header 'Content-Type: application/json' \
--data-raw '{
    "dob": "1979-06-03",
    "name": "Dr. Bobert Tea",
    "hospital": "Downside Medical",
    "speciality": "Neurosurgeon",
    "address": "246 Tree St, Countryside",
    "phoneNumber": "444-507-8901",
    "appendedEmail": "doctor:dr880@example.com",
    "password": "pass"
}'`

#### Java JDK not installed or misconfigured
If a Java JDK is not installed or properly configured on your system, you may encounter the following error:
```
[INFO] BUILD FAILURE
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:3.1.5:run (default-cli) on project PatientTrackerSystem: Process terminated with exit code: 1
```
Make sure that you have a JDK installed and that the JAVA_HOME environment variable is properly set.


## 4. High-Level Component Descriptions

#### Commons
- dto: Contains the request/response contract agreed with the frontend that the APIs must adhere to.
- jwt: Contains the various classes responsible for the session management using JWT tokenization. JwtRequestFilter filters out the request without the tokens. WebSecurityConfig contains the access control for the various APIs.
- Constants: In order to avoid the magic strings, this file contains the set of constants used across the repository.
- EmailsService: Responsible for generation of emails during the cases of scheduling/rescheduling of appointments and also for the OTP generations.
- Utils: Contains the static utility methods.

#### Controllers
- DoctorController: Handles operations related to doctors, including retrieval, filtering, and signup functionalities.
- PatientController: Manages patient-related operations, such as patient retrieval, registration, and deletion.
- AdminController: Manages the approval process for the doctors.
- CaseController: Contains the CRUD APIs for the cases. A patient can open a new case and the doctor is responsible for closing the case. Read cases shall be invoked by both the doctor and the patient.
- AppointmentController: Contains the CRUD APIs for the appointments. A patient schedules an appointment and both can fetch the appointments.
- PasswordController: Both patient and doctor can reset their password using the password reset API and can generate the OTPs using the corresponding API.

#### Models
- Appointment: Represents appointments associated with a case, patient, and doctor.
- Case: Represents a medical case, including patient and doctor information, symptoms, and prescribed medicines.
- Doctor: Represents a doctor with details like name, specialty, and approval status.
- DoctorsLogin: Manages login information specific to doctors.
- Patient: Represents a patient with details like name, address, and contact information.
- PatientsLogin: Manages login information specific to patients.
- UserLogin: Abstract class representing login information for users which shall be extended by both the DoctorsLogin and the PatientsLogin DTO.

#### Repositories
- AppointmentRepository: Manages appointments in the database, allowing retrieval based on doctor, patient, date, and time.
- CaseRepository: Handles medical cases, providing methods for retrieval based on patient, doctor, and case ID.
- DoctorRepository: Manages doctor entities, allowing searches based on speciality, name, and address.
- DoctorSignupRepository: Manages doctor signup data, extending JpaRepository for basic CRUD operations.
- PatientRepository: Manages patient entities, allowing retrieval based on patient ID and email.
- PatientSignupRepository: Manages patient signup data, extending JpaRepository for basic CRUD operations.

#### Main Application
- PatientTrackerSystemApplication: Main class for running the Patient Tracker System Spring Boot application.
