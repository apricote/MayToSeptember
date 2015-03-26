package com.toelle.maytoseptember.model;

import java.util.*;

/**
 * This class resembles a Date.
 * Any object of this class is immutable and can therefore not be changed.
 * This implementation of a date does not include checks of validity or different timezones. It is really basic and should
 * not be used outside of this Project. It got created because the <code>java.util.Date</code> implementation was too much for this use case.
 */
public class Date implements Comparable<Date> {
    private int day;
    private int month;
    private int year;

    /**
     * Creates a new Date based on the day, month and year it should resemble.
     * @param day Day
     * @param month Month
     * @param year Year
     */
    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Creates a new Date based on the String in one of the following formats:
     * <ul>
     *     <li>YYYY-MM-DD</li>
     *     <li>YYYY-MM-D</li>
     *     <li>YYYY-M-DD</li>
     *     <li>YYYY-M-D</li>
     * </ul>
     * @param date String in one of the accepted formats
     */
    public Date(String date) {

        this.year = Integer.parseInt(date.substring(0, 4));
        this.month = Integer.parseInt(date.substring(5, date.lastIndexOf("-")));
        this.day = Integer.parseInt(date.substring(date.lastIndexOf("-") + 1, date.length()));
    }

    /**
     * Returns the Day of this Date
     * @return day
     */
    public int getDay() {
        return day;
    }

    /**
     * Returns the Month of this Date
     * @return month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the Year of this Date
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * This method compares the date to another date.<br>
     *     Returns:
     * <ul>
     *     <li>-1, if this date is earlier than the other date</li>
     *     <li>0, if both dates are equal</li>
     *     <li>1 if this date is later than the other date</li>
     * </ul>
     * @param otherDate The date this date should be compared to
     * @return -1 .. 1
     */
    public int compareTo(Date otherDate) {
        if(getYear() > otherDate.getYear()) {
            return 1;
        } else if(getYear() < otherDate.getYear()) {
            return -1;
        } else if(getMonth() > otherDate.getMonth()) {
            return 1;
        } else if(getMonth() < otherDate.getMonth()) {
            return -1;
        } else if(getDay() > otherDate.getDay()) {
            return 1;
        } else if(getDay() < otherDate.getDay()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Returns the represented date in the format 'YYYY-MM-DD'
     * @return YYYY-MM-DD
     */
    @Override
    public String toString() {
        String date = this.getYear() + "-" + this.getMonth() + "-" + this.getDay();
        return date;
    }

    /**
     * This method creates the "Tommorow-Date" based on the view of the Object.
     * It considers the month transition and the year transition and even leap-years.
     * Only if the current value of Date is a valid Date the correct functionalty of this function is guaranteed.
     * @return the following date
     */
    public Date next() {
        int nextYear, nextMonth, nextDay;

        // Year
        if(getDay() == 31 && getMonth() == 12) {
            nextYear = getYear() + 1;
        } else {
            nextYear = getYear();
        }

        // Month + Day
        if( (daysOfMonth(getMonth(), getYear()) == getDay())) {
            nextMonth = getMonth() + 1;
            nextDay = 1;
        } else {
            nextMonth = getMonth();
            nextDay = getDay() + 1;
        }

        return new Date(nextDay, nextMonth, nextYear);
    }

    /**
     * Calculates the days in the month in the year given.
     * @param month days in <code>month</code>
     * @param year to check if <code>month</code> is a leap year
     * @return days in the month
     */
    private static int daysOfMonth(int month, int year) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else if (month == 2) {
            if (new GregorianCalendar().isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        } else {
            return -1;
        }
    }
}
