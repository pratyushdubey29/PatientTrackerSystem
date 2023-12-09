package edu.pav.PatientTrackerSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for the Patient Tracker System application.
 * This class contains the main method to run the Spring Boot application.
 */
@SpringBootApplication
public class PatientTrackerSystemApplication {

	/**
     * The main method to start the Patient Tracker System application.
     *
     * @param args The command line arguments.
     */
	public static void main(String[] args) {
		SpringApplication.run(PatientTrackerSystemApplication.class, args);
	}
}
