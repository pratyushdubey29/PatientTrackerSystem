package edu.pav.PatientTrackerSystem.commons;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {

    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.YYYY_MM_DD_STRING);
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.HH_mm_STRING);

    public enum DateTimeFormatStatus {
        ILL_FORMATTED,
        PAST_DATETIME,
        CORRECT
    }

    public static String getCurrentFormattedDate(){
        return LocalDate.now().format(dateFormatter);
    }

    public static String getCurrentFormattedTime(){
        return LocalTime.now().format(timeFormatter);
    }

    public static boolean isFutureDatetime(String dateA, String timeA, String dateB, String timeB) {
        // Checks if Date-TimeA is in future of Date-TimeB
        return dateA.compareTo(dateB) > 0 || (dateA.equals(dateB) && timeA.compareTo(timeB) > 0);
    }

    public static boolean isValidDate(String dateString) {
        try {
            LocalDate.parse(dateString, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidTime(String timeString) {
        try {
            LocalTime.parse(timeString, timeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

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
}
