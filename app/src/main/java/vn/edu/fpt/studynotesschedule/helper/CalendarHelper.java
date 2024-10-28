package vn.edu.fpt.studynotesschedule.helper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarHelper {
    public static LocalDate selectedDate, firstOfMonth;

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

    // chia tháng thành các ngày riêng lẻ
    public static ArrayList<LocalDate> fillCalendar(LocalDate date) {
        ArrayList<LocalDate> daysOfMonth = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int monthLength = yearMonth.lengthOfMonth();

        firstOfMonth = CalendarHelper.selectedDate.withDayOfMonth(1).minusDays(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i=1; i<=42; i++) {
            if(i <= dayOfWeek || i > monthLength + dayOfWeek)
                daysOfMonth.add(null);
            else
                daysOfMonth.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i - dayOfWeek));
        }
        return daysOfMonth;
    }

    // lỗi tiếng việt
    public static String polishMonths(LocalDate date) {
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

}
