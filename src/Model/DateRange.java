package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a date range
 * Immutable
 */
public class DateRange {

    private Date beginDate;
    private Date endDate;

    /**
     * Initiates a new DateRange Object.
     * The two given Dates will be the first and the last day of the Range. Both are inclusive
     * @param beginDate the first day of the range
     * @param endDate the last day of the range
     */
    public DateRange(Date beginDate, Date endDate) {
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    /**
     * Returns the first date of the range
     * @return beginDate
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * Returns the last date of the range
     * @return endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Creates a List of all the Dates that are between the start and the end.
     * Both ends of the range are included in the list.
     * @return All dates in the Range
     */
    public List<Date> getContainedDates() {
        ArrayList<Date> containedDates = new ArrayList<>();

        Date presentDate = getBeginDate();
        while(isInRange(presentDate)) {
            containedDates.add(presentDate);
            presentDate = presentDate.next();
        }

        return containedDates;
    }

    /**
     * Checks if the given Date is in the Range.
     * Day, Month and Year of the dates are important.
     * @param date the to-be-checked date
     * @return true if it is in the range, <br> false if not
     */
    public boolean isInRange(Date date) {
        if(date.compareTo(beginDate) >= 0 && date.compareTo(endDate) <= 0) {
            return true;
        } else {
            return false;
        }
    }
}
