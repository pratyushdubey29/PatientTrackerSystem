CREATE DATABASE IF NOT EXISTS PATIENT_TRACKER;
USE PATIENT_TRACKER;

-- Create the Doctor table
CREATE TABLE Doctor (
    doctor_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    specialty VARCHAR(100)
);

-- Create the Patient table
CREATE TABLE Patient (
    patient_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    date_of_birth DATE,
    gender VARCHAR(10),
    contact_number VARCHAR(15)
);

-- Create the Doctor login table
CREATE TABLE DoctorLogin (
    doctor_id INT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255) -- Use appropriate hashing and encryption for password
);

-- Create the Patient login table
CREATE TABLE PatientLogin (
    patient_id INT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255) -- Use appropriate hashing and encryption for password
);
