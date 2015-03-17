package com.toelle.maytoseptember.controller;

import com.toelle.maytoseptember.model.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.Vector;

public class StockOptimizer {

    /**
     * Calculates the optimal selling and buying Dates for the constraints given in the
     * <code>oStock</code> Object.
     * @param oStock The constraints for the optimization
     */
    public static OptimizedStock optimize(OptimizedStock oStock) {
        Vector<Date[]> dateCombinations = getPossibleDateCombinations(oStock);

        Date[] currentOptimum = dateCombinations.firstElement();
        float performanceIndexOptimum = -1000; //Basically unset

        for(Date[] dateCombination : dateCombinations) {
            float performanceIndex = calculatePerformanceIndex(dateCombination, oStock.getOptimizedStock());

            if (performanceIndex > performanceIndexOptimum) {
                performanceIndexOptimum = performanceIndex;
                currentOptimum = dateCombination;
            }
        }

        Logger.log("Final optimum: " + performanceIndexOptimum, LoggingLevel.INFO);
        Logger.log("For Days: Sell: " + currentOptimum[0] + " Buy: " + currentOptimum[1], LoggingLevel.INFO);

        oStock.setOptimizedData(currentOptimum, performanceIndexOptimum);
        return oStock;
    }

    /**
     * Generates a Vector of all the possible Datecombinations.
     * Every dateCombination has one day from the sellingTime and one from the buyingTime.

     * @return A Vector of all the combinations
     *          Every combination is an Array of length 2.
     *          The first index is the selling Date, and the second is the buying date.
     */
    private static Vector<Date[]> getPossibleDateCombinations(OptimizedStock stock) {
        Vector<Date[]> dateCombinations = new Vector<>();

        for(Date presentSellingDate : stock.getSellingTime().getContainedDates()) {
            for(Date presentBuyingDate : stock.getBuyingTime().getContainedDates()) {
                Date[] combination = {presentSellingDate, presentBuyingDate};
                dateCombinations.add(combination);
            }
        }
        Logger.log("Date Combinations:" + dateCombinations.size(), LoggingLevel.INFO);
        return dateCombinations;
    }

    /**
     * Calculates a Performance Index for the given dateCombination.
     *
     * The formula for the pI is <code>(endingMoney - startingMoney) / startingMoney</code>
     * @param dateCombination The dateCombination to calculate the pI for, based on the already set Stock
     * @return the performance Index of the given dateCombination
     */
    private static float calculatePerformanceIndex(Date[] dateCombination, Stock stock) {
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
