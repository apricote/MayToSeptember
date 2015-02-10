package Model;

import java.util.*;

/**
 * This Object resembles the Value history of one Stock.
 * It contains an ordered Map of the StockData Objects belonging to this Stock, with the Date as the Key.
 */
public class StockHistory {
    private SortedMap<Date, StockData> history;

    /**
     * Creates a new History Object without any Values. The Values can be added later though.
     */
    public StockHistory() {
        this(new TreeMap<Date, StockData>());
    }

    /**
     * Creates a new StockHistory on the given StockData Objects.
     * @param history The StockData Objects that belong into this History
     */
    public StockHistory(Map<Date, StockData> history) {
        this.history = new TreeMap<Date, StockData>(history);
    }

    /**
     * Returns the Map of StockData Objects that belong into this History
     * @return history
     */
    public SortedMap<Date, StockData> getHistory() {
        return history;
    }

    /**
     * Exchanges the current History with the new given one.
     * @param newData The new Data for the History
     */
    public void setHistory(Map<Date, StockData> newData) {
        this.history = new TreeMap<Date, StockData>(newData);
    }

    /**
     * Adds the new StockData Object to the Map.
     * If the Date of <code>newData</code> is already in the History,
     * it will overwrite the current <code>StockData</code> with the new one. The current object will be lost.
     * @param newData the new history to be included
     */
    public void addData(Map<Date, StockData> newData) {
        history.putAll(newData);
    }

    /**
     * Adds a single StockData Object to the history. If there is already an Object with this Date it will be overwritten.
     * @param newData The new StockData Object that is supposed to be added
     */
    public void addData(StockData newData) {
        history.put(newData.getDate(), newData);
    }

    /**
     * Returns the StockData Object at the given date. If there is no entry for this date an empty <code>Optional<StockData></code>
     * will be returned.
     * @param date an StockData Object will be searched for this date
     * @return The StockData Object belonging to the <code>date</code> or an empty <code>Optional</code> if there is none
     */
    public Optional<StockData> getStockData(Date date) {
        if(history.containsKey(date)) {
            return Optional.of(history.get(date));
        } else {
            return Optional.empty();
        }

    }

    /**
     * Deletes all the StockData Objects from this History that are between, and including, the two given Dates.
     * @param beginDate The beginning Date of the deletion
     * @param endDate The ending Date of the deletion
     */
    public void deleteStockData(Date beginDate, Date endDate) {
        DateRange range = new DateRange(beginDate, endDate);

        Iterator<Map.Entry<Date, StockData>> entries = getHistory().entrySet().iterator();
        while (entries.hasNext()) {
            Date entry = entries.next().getKey();
            if(range.isInRange(entry)) {
                entries.remove();
            }
        }
    }

    /**
     * Deletes all the StockData Objects from the History that are between, and including, the two given Dates.
     * It doesnt take into account the year, so that entries from every year are deleted.
     * Because of that the Date::getYear of the parameters isnt important.
     * @param beginDate The beginning Date of the deletion
     * @param endDate The ending Date of the deletion
     */
    public void deleteStockDataEveryYear(Date beginDate, Date endDate) {
        DateRange range = new DateRange(beginDate, endDate);

        Iterator<Map.Entry<Date, StockData>> entries = getHistory().entrySet().iterator();
        while (entries.hasNext()) {
            Date entry = entries.next().getKey();
            if(range.isInYearlyRange(entry)) {
                entries.remove();
            }
        }
    }
}
