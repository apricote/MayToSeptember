package Model;

import Controller.Logger;
import Controller.LoggingLevel;

/**
 * This class represents an optimized Stock. It just has a Stock as an attribute but
 * is not on itself.
 *
 * At any moment you can check if it actually contains optimized data or just the settings
 * to optimize it by checking <code>Model.optimized</code>.
 *
 * There is no method to actually generate the optimized Data because this is just a
 * model class and should not contain any business logic. You have to generate this yourself
 * by calling <code>StockOptimzer.optimize()</code>.
 */
public class OptimizedStock {
    private Stock optimizedStock;
    private DateRange sellingTime;
    private DateRange buyingTime;
    private Date[] optimalDates; // 0 => Selling Day, 1 => Buying Day
    private float performanceIndex;
    private boolean optimized;

    /**
     * Constructs a new Object with the given Stock, sellingTime and buyingTime.
     *
     * This Stock wont be optimized by itself. You have to call
     * <code>StockOptimizer.optimize()</code> yourself.
     * @param stock The stock that should be optimized
     * @param sellingTime The time frame the should be used for selling the Stock
     * @param buyingTime The time frame that should be used for buying the Stock
     */
    public OptimizedStock(Stock stock, DateRange sellingTime, DateRange buyingTime) {
        setOptimizedStock(stock);
        setSellingTime(sellingTime);
        setBuyingTime(buyingTime);
        optimized = false;
    }

    /**
     * Returns the Stock
     * @return optimizedStock
     */
    public Stock getOptimizedStock() {
        return optimizedStock;
    }

    /**
     * Sets the stock that should be optimized.
     *
     * If the Stock given as the parameter is not equal to the previous Stock Object,
     * all saved optimizations will be reset because they are not valid anymore for this
     * combination.
     *
     * @param optimizedStock The new Stock that should be optimized.
     */
    public void setOptimizedStock(Stock optimizedStock) {
        if(optimizedStock != this.optimizedStock) {
            resetOptimization();
        }
        this.optimizedStock = optimizedStock;
    }

    /**
     * Returns the <code>sellingTime</code> for the optimization.
     * @return sellingTime
     */
    public DateRange getSellingTime() {
        return sellingTime;
    }

    /**
     * Sets the sellingTime for the Optimization.
     *
     * If the sellingTime given as the parameter is not equal to the previous sellingTime,
     * all saved optimizations will be reset because they are not valid anymore for this
     * combination.
     *
     * @param sellingTime The new sellingTime for the optimization
     */
    public void setSellingTime(DateRange sellingTime) {
        if(sellingTime != this.sellingTime) {
            resetOptimization();
        }
        this.sellingTime = sellingTime;
    }

    /**
     * Returns the <code>buyingTime</code> for the optimization.
     * @return buyingTime
     */
    public DateRange getBuyingTime() {
        return buyingTime;
    }

    /**
     * Sets the buyingTime for the Optimization.
     *
     * If the buyingTime given as the parameter is not equal to the previous buyingTime,
     * all saved optimizations will be reset because they are not valid anymore for this
     * combination.
     *
     * @param buyingTime The new buyingTime for the optimization
     */
    public void setBuyingTime(DateRange buyingTime) {
        if(buyingTime != this.buyingTime) {
            resetOptimization();
        }
        this.buyingTime = buyingTime;
    }

    /**
     * Returns the optimalDates for buying and selling.
     *
     * This is a 2 Element array with the following setup:
     * <ul>
     *     <li>0 - Optimal Date for selling the Stock</li>
     *     <li>1 - Optimal Date for buying the Stock</li>
     * </ul>
     *
     * It is only present and valid if <code>optimized</code> is true. You should
     * always check this before accessing this method.
     *
     * If <code>optimized</code> is false, this method will return <code>null</code>.
     *
     * @return optimalDates
     */
    public Date[] getOptimalDates() {
        if(optimized) {
            return optimalDates;
        } else {
            Logger.log("No optimal Dates found!", LoggingLevel.WARNING);
            return null;
        }
    }

    /**
     * Sets the optimized Data (optimalDates and performanceIndex).
     *
     * This is the only method that can change those values, as they always need
     * to be changed together.
     *
     * Calling this method will set the <code>optimized</code>-Flag to true.
     *
     * @param optimalDates Optimal dates for selling and buying the Stock
     * @param performanceIndex The calculated performanceIndex for this optimization
     */
    public void setOptimizedData(Date[] optimalDates, float performanceIndex) {
        this.optimalDates = optimalDates;
        this.performanceIndex = performanceIndex;
        this.optimized = true;
    }

    /**
     * Returns the performanceIndex for this optimization.
     *
     * The performanceIndex is calculated in <code>StockOptimizer</code> and the
     * formula used for this can be accessed in the Documentation for that Class.
     *
     * The value this method should return is only present and valid if <code>optimized</code>
     * is true. You should always check this before accessing this method.
     * @return performanceIndex
     */
    public float getPerformanceIndex() {
        return performanceIndex;
    }

    /**
     * Resets all the optimization data (optimalDates and performanceIndex) and will
     * set the <code>optimized</code>-Flag to false.
     */
    private void resetOptimization() {
        optimalDates = null;
        performanceIndex = 0f;
        optimized = false;
    }
}
