CREATE DATABASE IF NOT EXISTS PATIENT_TRACKER_DB;
USE PATIENT_TRACKER_DB;

-- Create the Doctor table
CREATE TABLE doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    dob VARCHAR(50),
    hospital VARCHAR(50),
    speciality VARCHAR(100),
    address VARCHAR(100),
    phone_number VARCHAR(13),
    email VARCHAR(50) UNIQUE,
    is_approved BOOL
);

CREATE TABLE patients(
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    dob VARCHAR(50),
    sex CHAR,
    height INT,
    weight INT,
    address VARCHAR(100),
    phone_number VARCHAR(13),
    email VARCHAR(50) UNIQUE
);

CREATE TABLE cases (
    case_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    doctor_id INT,
    open_date VARCHAR(50),
    close_date VARCHAR(50),
    symptoms VARCHAR(200),
    medicines VARCHAR(200),
    cost DECIMAL(7,2)
);


CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    case_id INT,
    patient_id INT,
    doctor_id INT,
    date VARCHAR(50),
    time VARCHAR(50)
);

CREATE TABLE doctors_login (
    user_name VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50),
    user_id UNIQUE
);

CREATE TABLE patients_login (
    user_name VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50),
    user_id UNIQUE
);

-- Insert 5 random rows into the DOCTORS table
INSERT INTO doctors (doctor_id, name, dob, hospital, speciality, address, phone_number, email, is_approved)
VALUES
    (1, 'Dr. John Smith', '1980-05-15', 'St. Mary Hospital', 'Cardiologist', '123 Main St, Cityville', '555-123-4567', 'dr.john.smith@example.com', False),
    (2, 'Dr. Jane Doe', '1975-08-20', 'City General Hospital', 'Pediatrician', '456 Elm St, Townville', '555-234-5678', 'dr.jane.doe@example.com', True),
    (3, 'Dr. Michael Brown', '1990-03-10', 'County Medical Center', 'Dermatologist', '789 Oak St, Villageton', '555-345-6789', 'dr.michael.brown@example.com', True),
    (4, 'Dr. Sarah Johnson', '1988-12-05', 'City Health Clinic', 'Gynecologist', '101 Pine St, Hamletville', '555-456-7890', 'dr.sarah.johnson@example.com', True),
    (5, 'Dr. Robert Lee', '1972-06-30', 'Hillside Medical Group', 'Orthopedic Surgeon', '246 Birch St, Countryside', '555-567-8901', 'dr.robert.lee@example.com', False);

INSERT INTO cases (case_id, patient_id, doctor_id, open_date, close_date, symptoms, medicines, cost)
VALUES
    (1, 1, 2, '2023-01-02', '2023-03-02', 'Cold', 'Levocetirizine', '500.20'),
    (2, 2, 4, '2023-02-10', '2023-04-12', 'Cramps', 'Ibuprofen', '610.20'),
    (3, 3, 2, '2023-06-29', '2023-09-29', 'Headache','Paracetamol', '850.20'),
    (4, 3, 3, '2023-09-02', '', 'Acne', 'Isotretinoin, Trifarotene', '999.99');

INSERT INTO appointments (appointment_id, case_id, patient_id, doctor_id, date, time)
VALUES
    (1, 1, 1, 2, '2023-01-10', '09:00'),
    (2, 2, 2, 4, '2023-02-15', '10:30'),
    (3, 1, 1, 2, '2023-02-20', '10:00'),
    (4, 3, 3, 2, '2023-07-05', '15:00'),
    (5, 4, 3, 3, '2023-09-05', '14:00'),
    (6, 4, 3, 3, '2023-11-21', '16:00');


INSERT INTO patients (name, dob, sex, height, weight, address, phone_number, email)
VALUES
('Aarav Patel', '1990-05-15', 'M', 175, 70, '58 Not Main St, Cityville', '555-123-6967', 'aarav.patel@example.com'),
('Maria Gonzalez', '1985-08-20', 'F', 162, 55, '46 Mel St, Townville', '955-234-5678', 'maria.gonzalez@example.com'),
('Satoshi Tanaka', '1995-03-10', 'M', 180, 80, '71 Ok St, Villageton', '885-945-6789', 'satoshi.tanaka@example.com'),
('Emily Brown', '1992-12-05', 'F', 168, 60, '1221 Pin St, Hamletville', '585-456-7890', 'emily.brown@example.com'),
('Luca Romano', '1988-06-30', 'M', 170, 65, '986 Goal St, Countryside', '515-567-8901', 'luca.romano@example.com');