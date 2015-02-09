package Controller;

import Model.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

public class StockOptimizer {

    private Stock stock;
    private DateRange sellingTime;
    private DateRange buyingTime;

    /**
     * Create a new StockOptimizer based on the Stock.
     * The sellingTime and buyingTime still need to be set before calling <code>optimize()</code>
     * @param stock the stock that should be optimized
     */
    public StockOptimizer(Stock stock) {
        this.stock = stock;
    }

    /**
     * Create a new StockOptimizer.
     * The sellingTime, buyingTime and Stock need to be set before calling <code>optimize()</code>
     */
    public StockOptimizer() {
        super();
    }

    /**
     * Returns the stock
     * @return stock
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * Sets the Stock
     * @param stock new Stock
     */
    public void setStock(Stock stock) {
        this.stock = stock;
    }

    /**
     * Returns the selling Time
     * @return sellingTime
     */
    public DateRange getSellingTime() {
        return sellingTime;
    }

    /**
     * Sets the selling Time
     * @param sellingTime new sellingTime
     */
    public void setSellingTime(DateRange sellingTime) {
        this.sellingTime = sellingTime;
    }

    /**
     * Returns the buying Time
     * @return buyingTime
     */
    public DateRange getBuyingTime() {
        return buyingTime;
    }

    /**
     * Sets the buying Time
     * @param buyingTime new buyingTime
     */
    public void setBuyingTime(DateRange buyingTime) {
        this.buyingTime = buyingTime;
    }

    /**
     * Optimizes the Stock. It doesnt return the stock but just changes <code>Stock.optimizedHistory</code>
     * of the <code>stock</code> Object. It does this by generating a list of all possible date combinations.
     * Then iterating over the list to calculate each <code>performanceIndex</code> and to get the maximum value.
     *
     * With the given dates the optimized History is generated and saved in the Stock Object
     */
    public void optimize() {
        StockHistory optimizedHistory = stock.getHistory();

        Vector<Date[]> dateCombinations = getPossibleDateCombinations();

        Date[] currentOptimum = dateCombinations.firstElement();
        float performanceIndexOptimum = -1000; //Basically unset

        for(Date[] dateCombination : dateCombinations) {
            float performanceIndex = calculatePerformanceIndex(dateCombination);

            if (performanceIndex > performanceIndexOptimum) {
                performanceIndexOptimum = performanceIndex;
                currentOptimum = dateCombination;
            }
        }

        Logger.log("Final optimum: " + performanceIndexOptimum, LoggingLevel.INFO);
        Logger.log("For Days: Sell: " + currentOptimum[0] + " Buy: " + currentOptimum[1], LoggingLevel.INFO);

        optimizedHistory.deleteStockDataEveryYear(currentOptimum[0], currentOptimum[1]);

        stock.setOptimizedHistory(optimizedHistory);
    }

    /**
     * Generates a Vector of all the possible Datecombinations.
     * Every dateCombination has one day from the sellingTime and one from the buyingTime.
     *
     * Before this method gets called, sellingTime and buyingTime need to be set. Otherwise it will rise a NullPointerException.
     *
     * @return A Vector of all the combinations
     *          Every combination is an Array of length 2.
     *          The first index is the selling Date, and the second is the buying date.
     */
    private Vector<Date[]> getPossibleDateCombinations() {
        Vector<Date[]> dateCombinations = new Vector<>();

        for(Date presentSellingDate : getSellingTime().getContainedDates()) {
            for(Date presentBuyingDate : getBuyingTime().getContainedDates()) {
                Date[] combination = {presentSellingDate, presentBuyingDate};
                dateCombinations.add(combination);
            }
        }
        return dateCombinations;
    }

    /**
     * Calculates a Performance Index for the given dateCombination.
     *
     * The formula for the pI is <code>(endingMoney - startingMoney) / startingMoney</code>
     * @param dateCombination The dateCombination to calculate the pI for, based on the already set Stock
     * @return the performance Index of the given dateCombination
     */
    private float calculatePerformanceIndex(Date[] dateCombination) {
        /**
         * Saves the money and the stocks
         */
        class Assets {
            BigDecimal amountOfMoney;
            BigDecimal amountOfStocks;

            /**
             * Create a new Asset
             * @param starterMoney The money that is in the bank account in the beginning
             */
            Assets(BigDecimal starterMoney) {
                amountOfMoney = starterMoney;
                amountOfStocks = new BigDecimal(0);
            }

            /**
             * If there are any stocks left in the Asset, all of it will be sold and the equivalent of Money will be added.
             * @param stockValue The value of the Stock for that it should be sold
             */
            void sellAllStocks(BigDecimal stockValue) {
                if (amountOfStocks.compareTo(BigDecimal.ZERO) != 0) {

                    BigDecimal valueOfStock = stockValue.multiply(amountOfStocks);
                    amountOfMoney = amountOfMoney.add(valueOfStock);
                    amountOfStocks = BigDecimal.ZERO;
                }
            }

            /**
             * If there is any money left in the Asset, all of it is used to buy Stocks.
             * @param stockValue The value of the stock for that it should be bought
             */
            void buyAllStocks(BigDecimal stockValue) {
                if (amountOfMoney.compareTo(BigDecimal.ZERO) != 0) {

                    BigDecimal stocksFromMoney = amountOfMoney.divide(stockValue, MathContext.DECIMAL128);
                    amountOfStocks = amountOfStocks.add(stocksFromMoney);
                    amountOfMoney = BigDecimal.ZERO;
                }
            }
        }

        float performanceIndex = 0f;


        BigDecimal starterMoney = new BigDecimal(1000);
        Assets assets = new Assets(starterMoney);

        DateRange breakTime = new DateRange(dateCombination[0], dateCombination[1]); // SellingDate -> BuyingDate

        Date firstDate = stock.getHistory().getHistory().firstKey();
        StockData firstDay = stock.getHistory().getStockData(firstDate).get(); // We can ignore the Optional because we assured
                                                                                //that the key is in the History by using firstKey()

        for (Map.Entry<Date, StockData> data : stock.getHistory().getHistory().entrySet()) {
            if(breakTime.isInYearlyRange(data.getKey())) {
                assets.sellAllStocks(data.getValue().getValue());
            } else {
                assets.buyAllStocks(data.getValue().getValue());
            }
        }

        //In the end sell all Stocks:
        Date lastDate = stock.getHistory().getHistory().lastKey();
        BigDecimal valueOnLastDate = stock.getHistory().getHistory().get(lastDate).getValue();
        assets.sellAllStocks(valueOnLastDate);


        //Actually calculate performanceIndex
        // endingMoney / starterMoney
        BigDecimal endingMoney = assets.amountOfMoney;
        performanceIndex = endingMoney.divide(starterMoney, MathContext.DECIMAL32).floatValue(); //Just using DECIMAL 32 here because its going to a float anyway

        Logger.log("Calculated performanceIndex: " + performanceIndex, LoggingLevel.DEBUG);
        Logger.log("For Days: Sell: " + dateCombination[0].toString() + " Buy: " + dateCombination[1].toString(), LoggingLevel.DEBUG);
        return performanceIndex;
    }


}
