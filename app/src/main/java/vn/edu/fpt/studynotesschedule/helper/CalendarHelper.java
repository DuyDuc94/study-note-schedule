package vn.edu.fpt.studynotesschedule.helper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarHelper {
    public static LocalDate selectedDate, firstOfMonth;
    private final static int GRID_SIZE = 42;    // 6 * 7

    public static String dateFormatter(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }

    public static String dayFormatter(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E");
        return date.format(formatter);
    }

    public static String dayNumberFormatter(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("e");
        return date.format(formatter);
    }

    public static String monthFormatter(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("L");
        return date.format(formatter);
    }

    public static String timeFormatter(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    // generate a list of days in a month
    public static ArrayList<LocalDate> fillCalendar(LocalDate date) {
        ArrayList<LocalDate> daysOfMonth = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int monthLength = yearMonth.lengthOfMonth();

        firstOfMonth = CalendarHelper.selectedDate.withDayOfMonth(1).minusDays(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i=1; i<=GRID_SIZE; i++) {
            if(i <= dayOfWeek || i > monthLength + dayOfWeek)
                daysOfMonth.add(null);
            else
                daysOfMonth.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i - dayOfWeek));
        }

        return daysOfMonth;
    }

    public static String getMonths(LocalDate date) {
        String month = "";
        for (int i = 0; i < 12; i++) {
            String monthString = CalendarHelper.monthFormatter(date);
            switch (monthString) {
                case "1":
                    month = "January";
                    break;
                case "2":
                    month = "February";
                    break;
                case "3":
                    month = "March";
                    break;
                case "4":
                    month = "April";
                    break;
                case "5":
                    month = "May";
                    break;
                case "6":
                    month = "June";
                    break;
                case "7":
                    month = "July";
                    break;
                case "8":
                    month = "August";
                    break;
                case "9":
                    month = "September";
                    break;
                case "10":
                    month = "October";
                    break;
                case "11":
                    month = "November";
                    break;
                case "12":
                    month = "December";
                    break;
                default:
                    break;
            }
        }
        String monthYear = month + " " + date.getYear();
        return monthYear;
    }

    public static String getDays(LocalDate date) {
        String day = "";
        String dayString;
        for (int i = 0; i < 7; i++) {
            // minusDays to emulator -> shift by 1 day
            dayString = dayNumberFormatter(date.minusDays(1));

            switch (dayString) {
                case "1":
                    day = "Monday";
                    break;
                case "2":
                    day = "Tuesday";
                    break;
                case "3":
                    day = "Wednesday";
                    break;
                case "4":
                    day = "Thursday";
                    break;
                case "5":
                    day = "Friday";
                    break;
                case "6":
                    day = "Saturday";
                    break;
                case "7":
                    day = "Sunday";
                    break;
                default:
                    break;
            }
        }
        return day;
    }
}
