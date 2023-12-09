package edu.pav.PatientTrackerSystem.commons;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class containing various helper methods for common tasks.
 */
public class Utils {

    /**
     * Date formatter for the yyyy-MM-dd pattern.
     */
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.yyyy_MM_dd_STRING);
    
    /**
     * Time formatter for the HH:mm pattern.
     */
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.HH_mm_STRING);

    /**
     * Enum representing the status of date-time format validation.
     */
    public enum DateTimeFormatStatus {
        ILL_FORMATTED,
        PAST_DATETIME,
        CORRECT
    }

    /**
     * Retrieves the current formatted date in the yyyy-MM-dd pattern.
     *
     * @return The current formatted date.
     */
    public static String getCurrentFormattedDate(){
        return LocalDate.now().format(dateFormatter);
    }

    /**
     * Retrieves the current formatted time in the HH:mm pattern.
     *
     * @return The current formatted time.
     */
    public static String getCurrentFormattedTime(){
        return LocalTime.now().format(timeFormatter);
    }


    /**
     * Checks if a given date-time is in the future compared to another date-time.
     *
     * @param dateA The date of the first date-time.
     * @param timeA The time of the first date-time.
     * @param dateB The date of the second date-time.
     * @param timeB The time of the second date-time.
     * @return True if the first date-time is in the future of the second date-time, false otherwise.
     */
    public static boolean isFutureDatetime(String dateA, String timeA, String dateB, String timeB) {
        // Checks if Date-TimeA is in future of Date-TimeB
        return dateA.compareTo(dateB) > 0 || (dateA.equals(dateB) && timeA.compareTo(timeB) > 0);
    }

    /**
     * Checks if a given date string is valid according to the yyyy-MM-dd pattern.
     *
     * @param dateString The date string to be validated.
     * @return True if the date string is valid, false otherwise.
     */
    public static boolean isValidDate(String dateString) {
        try {
            LocalDate.parse(dateString, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if a given time string is valid according to the HH:mm pattern.
     *
     * @param timeString The time string to be validated.
     * @return True if the time string is valid, false otherwise.
     */
    public static boolean isValidTime(String timeString) {
        try {
            LocalTime.parse(timeString, timeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates the format of a requested date-time and checks if it is in the past.
     *
     * @param requestedDate The requested date.
     * @param requestedTime The requested time.
     * @return The status of date-time format validation.
     */
    public static DateTimeFormatStatus dateTimeFormatAndPastCheck(String requestedDate, String requestedTime){

        String formattedCurrDate = getCurrentFormattedDate();
        String formattedCurrTime = getCurrentFormattedTime();

        // If requested date is not formatted well
        if (!isValidDate(requestedDate) || !isValidTime(requestedTime)) {
            return DateTimeFormatStatus.ILL_FORMATTED;
        }
        // If requested date is in the past
        if (isFutureDatetime(formattedCurrDate, formattedCurrTime, requestedDate, requestedTime)) {
            return DateTimeFormatStatus.PAST_DATETIME;
        }

        return DateTimeFormatStatus.CORRECT;
    }

    /**
     * Encrypts a password using the MD5 hashing algorithm.
     *
     * @param password The plain-text password to be encrypted.
     * @return The encrypted password in hexadecimal format.
     */
    public static String encryptPassword(String password) {
        try
        {
            /* MessageDigest instance for MD5. */
            MessageDigest m = MessageDigest.getInstance("MD5");

            /* Add plain-text password bytes to digest using MD5 update() method. */
            m.update(password.getBytes());

            /* Convert the hash value into bytes */
            byte[] bytes = m.digest();

            /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            /* Complete hashed password in hexadecimal format */
            return s.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return Constants.EMPTY_STRING;
    }
}
