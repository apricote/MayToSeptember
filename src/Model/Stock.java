package Model;

/**
 * This class resembles a single Stock and its market history.
 *
 * The optimalDays are the optimal selling and buying dates for this stock,
 * though there is no control that it is really optimized and can even be worse than the normal history.
 */
public class Stock {
    private String name;
    private String shortName;
    private StockHistory history;
    private Date[] optimalDays; // [0] => sell, [1] => buy

    /**
     * Creates a new Stock object with the given name and shortName. Neither the history nor the optimized history will be set.
     * Instead a new and empty StockHistory Object will be used as <code>history</code> and and a <code>null</code>-Object
     * as the <code>optimalDays</code>
     * @param name The name of the Stock
     * @param shortName The short name of the Stock
     */
    public Stock(String name, String shortName) {
        this(name, shortName, new StockHistory(), null);
    }


    /**
     * Creates a new Stock object with the given name, shortName and History. The optimized history wont be set
     * and instead an empty StockHistory Object will be used.
     * @param name The name of the Stock
     * @param shortName The short name of the Stock
     * @param history The history of the stock
     */
    public Stock(String name, String shortName, StockHistory history) {
        this(name, shortName, history, null);
    }

    /**
     * Creates a new Stock object with the given name, shortName, histoy and optimalDays.
     * @param name The name of the Stock
     * @param shortName The short name of the Stock
     * @param history The history of the stock
     * @param optimalDays The optimal buying and selling date of the Stock
     */
    public Stock(String name, String shortName, StockHistory history, Date[] optimalDays) {
        this.name = name;
        this.shortName = shortName;
        this.history = history;
        this.optimalDays = optimalDays;
    }

    /**
     * Returns the name of the Stock
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Stock
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the short name of the Stock
     * @return shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets the short name of the stock
     * @param shortName The new short name
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * Returns the history of the Stock
     * @return history
     */
    public StockHistory getHistory() {
        return history;
    }

    /**
     * Sets the history of the stock
     * @param history The new history
     */
    public void setHistory(StockHistory history) {
        this.history = history;
    }

    /**
     * Returns the optimal buying and selling dates that were calculated for the in
     * the last Optimizer run for this Stock.
     *
     * It is not guaranteed that this is the absolute maximum for the Stock, but rather
     * the maximum for the given Ranges of the Optimizer for that run.
     * @return optimalDays
     */
    public Date[] getOptimalDays() {
        return optimalDays;
    }

    /**
     * Sets the optimal selling and buying date for this Stock.
     *
     * This does not need to be the absolute maximum, possible for the stock,
     * but rather the maximum that is achievable in the given buying and selling date ranges,
     * that were given to the Optimizer.
     * @param optimalDays The new optimized history
     */
    public void setOptimalDays(Date[] optimalDays) {
        this.optimalDays = optimalDays;
    }
}
