CREATE DATABASE IF NOT EXISTS PATIENT_TRACKER_DB;
USE PATIENT_TRACKER_DB;

-- Create the Doctor table
CREATE TABLE DOCTORS (
    doctor_id INT PRIMARY KEY,
    name VARCHAR(50),
    dob VARCHAR(50),
    hospital VARCHAR(50),
    speciality VARCHAR(100),
    address VARCHAR(100),
    phone_number VARCHAR(13),
    email VARCHAR(50),
    is_approved BOOL
);

-- Insert 5 random rows into the DOCTORS table
INSERT INTO DOCTORS (doctor_id, name, dob, hospital, speciality, address, phone_number, email, is_approved)
VALUES
    (1, 'Dr. John Smith', '1980-05-15', 'St. Mary Hospital', 'Cardiologist', '123 Main St, Cityville', '555-123-4567', 'dr.john.smith@example.com', True),
    (2, 'Dr. Jane Doe', '1975-08-20', 'City General Hospital', 'Pediatrician', '456 Elm St, Townville', '555-234-5678', 'dr.jane.doe@example.com', False),
    (3, 'Dr. Michael Brown', '1990-03-10', 'County Medical Center', 'Dermatologist', '789 Oak St, Villageton', '555-345-6789', 'dr.michael.brown@example.com', True),
    (4, 'Dr. Sarah Johnson', '1988-12-05', 'City Health Clinic', 'Gynecologist', '101 Pine St, Hamletville', '555-456-7890', 'dr.sarah.johnson@example.com', False),
    (5, 'Dr. Robert Lee', '1972-06-30', 'Hillside Medical Group', 'Orthopedic Surgeon', '246 Birch St, Countryside', '555-567-8901', 'dr.robert.lee@example.com', True);
