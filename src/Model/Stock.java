package Model;

/**
 * This class resembles a single Stock and its market history.
 * The optimizedHistory is just a second slot for another, not real, history that should be optimized,
 * though there is no control that it is really optimized and can even be worse than the normal history.
 */
public class Stock {
    private String name;
    private String shortName;
    private StockHistory history;
    private StockHistory optimizedHistory;

    /**
     * Creates a new Stock object with the given name and shortName. Neither the history nor the optimized history will be set.
     * Instead a new and empty StockHistory Object will be used as <code>history</code> and <code>optimizedHistory</code>
     * @param name The name of the Stock
     * @param shortName The short name of the Stock
     */
    public Stock(String name, String shortName) {
        this(name, shortName, new StockHistory(), new StockHistory());
    }


    /**
     * Creates a new Stock object with the given name, shortName and History. The optimized history wont be set
     * and instead an empty StockHistory Object will be used.
     * @param name The name of the Stock
     * @param shortName The short name of the Stock
     * @param history The history of the stock
     */
    public Stock(String name, String shortName, StockHistory history) {
        this(name, shortName, history, new StockHistory());
    }

    /**
     * Creates a new Stock object with the given name, shortName, histoy and optimizedHistory.
     * @param name The name of the Stock
     * @param shortName The short name of the Stock
     * @param history The history of the stock
     * @param optimizedHistory The optimized history of the stock
     */
    public Stock(String name, String shortName, StockHistory history, StockHistory optimizedHistory) {
        this.name = name;
        this.shortName = shortName;
        this.history = history;
        this.optimizedHistory = optimizedHistory;
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
     * Returns the optimized history of the Stock
     * @return optimizedHistory
     */
    public StockHistory getOptimizedHistory() {
        return optimizedHistory;
    }

    /**
     * Sets the optimized history of the Stock
     * @param optimizedHistory The new optimized history
     */
    public void setOptimizedHistory(StockHistory optimizedHistory) {
        this.optimizedHistory = optimizedHistory;
    }
}
