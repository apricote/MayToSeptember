package Model;

/**
 * A StockData Object represents the Stock data for a single Day.
 * It knows the value of the Stock at that day and the day it references too.
 * This object is immutable.
 */
public class StockData {
    private int value;
    private Date date;

    /**
     * Creates a new StockDate object based on the information given in the parameters
     * @param value The value that the Stock had at the given date
     * @param date The date of the given value
     */
    public StockData(int value, Date date, Stock stock) {
        this.value = value;
        this.date = date;
    }

    /**
     * Returns the value of this StockData object
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the date of this StockData object
     * @return date
     */
    public Date getDate() {
        return date;
    }
}
