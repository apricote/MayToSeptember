package Model;

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
     * Creates a new Date based on the String in the YYYY-MM-DD format.
     * @param date String in YYYY-MM-DD format
     */
    public Date(String date) {
        this.year = Integer.parseInt(date.substring(0, 3));
        this.month = Integer.parseInt(date.substring(5, 6));
        this.day = Integer.parseInt(date.substring(8, 9));
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
}
