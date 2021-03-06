package com.toelle.maytoseptember.view;

import com.toelle.maytoseptember.controller.DatabaseConnection;
import com.toelle.maytoseptember.controller.Logger;
import com.toelle.maytoseptember.controller.LoggingLevel;
import com.toelle.maytoseptember.controller.StockOptimizer;
import com.toelle.maytoseptember.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Optional;

public class WindowController {

    @FXML
    private LineChart<java.util.Date, Number> assetWorthGraph;

    @FXML
    private DatePicker sellingTimeStartPicker;

    @FXML
    private DatePicker sellingTimeEndPicker;

    @FXML
    private DatePicker buyingTimeStartPicker;

    @FXML
    private DatePicker buyingTimeEndPicker;

    @FXML
    private Button loadStockButton;

    @FXML
    private Button optimizeAllYearButton;

    @FXML
    private Button optimizeButton;

    @FXML
    private Label stockNameLabel;

    @FXML
    private Label stockShortNameLabel;

    @FXML
    private Label daterangeLabel;

    @FXML
    private Label entriesCountLabel;

    @FXML
    private Label sellingTimeDaterangeLabel;

    @FXML
    private Label buyingTimeDaterangeLabel;

    @FXML
    private Label sellingDateLabel;

    @FXML
    private Label buyingDateLabel;

    @FXML
    private Label performanceIndexLabel;

    @FXML
    private DateAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private Main mainApp;

    @FXML
    private void initialize() {
        // Setup DatePickers for MayToSeptember selection
        sellingTimeStartPicker.setValue(LocalDate.of(2015, 5, 1));
        sellingTimeEndPicker.setValue(LocalDate.of(2015, 5, 31));
        buyingTimeStartPicker.setValue(LocalDate.of(2015, 9, 1));
        buyingTimeEndPicker.setValue(LocalDate.of(2015, 9, 30));

        assetWorthGraph.setCreateSymbols(false);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void loadStockButtonClicked() {
        DatabaseConnection db = new DatabaseConnection();
        Stock stock;

        try {
            stock = db.getStockFromDatabase();
        } catch (IOException e) {
            Logger.log("Stock could not be loaded.", LoggingLevel.WARNING);
            showStockLoadingErrorAlert();
            return;
        }

        mainApp.setStock(stock);
        reloadStockLabels();
        redrawGraph();
    }

    private void reloadStockLabels() {
        // stockNameLabel
        // stockShortNameLabel
        // daterangeLabel
        // entriesCountLabel
        if(mainApp.getStock() != null) {
            Stock s = mainApp.getStock();
            stockNameLabel.setText(s.getName());
            stockShortNameLabel.setText(s.getShortName());
            entriesCountLabel.setText(s.getHistory().getHistory().size() + "");

            String daterangeString;
            daterangeString = s.getHistory().getHistory().firstKey().toString();
            daterangeString += " to ";
            daterangeString += s.getHistory().getHistory().lastKey().toString();

            daterangeLabel.setText(daterangeString);
        } else {
            Logger.log("No Stock Object found in MainApp", LoggingLevel.WARNING);
            Logger.log("Couldn't reload Stock labels", LoggingLevel.WARNING);
        }
    }

    private void reloadOStockLabels() {
        // sellingTimeDaterangeLabel
        // buyingTimeDaterangeLabel
        // sellingDateLabel
        // buyingDateLabel
        // performanceIndexLabel
        if(mainApp.getOStock() != null && mainApp.getOStock().isOptimized()) {
            OptimizedStock oS = mainApp.getOStock();
            String sellingTimeDaterangeString = oS.getSellingTime().getBeginDate().toString();
            sellingTimeDaterangeString += " to ";
            sellingTimeDaterangeString += oS.getSellingTime().getEndDate().toString();
            sellingTimeDaterangeLabel.setText(sellingTimeDaterangeString);

            String buyingTimeDaterangeString = oS.getBuyingTime().getBeginDate().toString();
            buyingTimeDaterangeString += " to ";
            buyingTimeDaterangeString += oS.getBuyingTime().getBeginDate().toString();
            buyingTimeDaterangeLabel.setText(buyingTimeDaterangeString);

            sellingDateLabel.setText(oS.getOptimalDates()[0].toString());
            buyingDateLabel.setText(oS.getOptimalDates()[1].toString());

            performanceIndexLabel.setText(oS.getPerformanceIndex() + "");
        }
    }

    @FXML
    private void optimizeButtonClicked() {
        if (mainApp.getStock() == null) {
            showNoStockAlert();
            return;
        }

        if( sellingTimeStartPicker.getValue() == null ||
                sellingTimeEndPicker.getValue() == null ||
                buyingTimeStartPicker.getValue() == null ||
                buyingTimeEndPicker.getValue() == null) {
            showWrongDatesAlert();
            return;
        }

        Date sellingTimeStart = new Date(sellingTimeStartPicker.getValue().toString());
        Date sellingTimeEnd = new Date(sellingTimeEndPicker.getValue().toString());
        Date buyingTimeStart = new Date(buyingTimeStartPicker.getValue().toString());
        Date buyingTimeEnd = new Date(buyingTimeEndPicker.getValue().toString());

        DateRange sellingRange = new DateRange(sellingTimeStart, sellingTimeEnd);
        DateRange buyingRange = new DateRange(buyingTimeStart, buyingTimeEnd);

        OptimizedStock oStock = new OptimizedStock(mainApp.getStock(), sellingRange, buyingRange);
        optimize(oStock);

    }

    @FXML
    private void optimizeAllYearButtonClicked() {
        if (mainApp.getStock() == null) {
            showNoStockAlert();
            return;
        }

        DateRange sellingRange = new DateRange(new Date(1, 1, 2000), new Date(31, 12, 2000));
        DateRange buyingRange = new DateRange(new Date(1, 1, 2000), new Date(31, 12, 2000));
        OptimizedStock oStock = new OptimizedStock(mainApp.getStock(), sellingRange, buyingRange);

        optimize(oStock);


    }

    private void optimize(OptimizedStock oStock) {
        oStock = StockOptimizer.optimize(oStock);

        mainApp.setOStock(oStock);
        reloadOStockLabels();
        redrawGraph();
    }

    private void redrawGraph() {
        if(mainApp.getStock() == null) {
            return;
            //TODO Errorhandling
        }
        ObservableList<XYChart.Series<java.util.Date, Number>> series = FXCollections.observableArrayList();

        ObservableList<XYChart.Data<java.util.Date, Number>> stockSeriesData, oStockSeriesData;

        stockSeriesData = generateStockSeriesData();
        oStockSeriesData = generateOStockSeriesData();

        if(stockSeriesData != null) {
            series.add(new XYChart.Series<>("Normal " + mainApp.getStock().getShortName(), stockSeriesData));
        }

        if(oStockSeriesData != null) {
            series.add(new XYChart.Series<>("Optimized " + mainApp.getStock().getShortName(), oStockSeriesData));
        }

        assetWorthGraph.setData(series);
    }

    private ObservableList<XYChart.Data<java.util.Date, Number>> generateStockSeriesData() {
        ObservableList<XYChart.Data<java.util.Date, Number>> dataList = FXCollections.observableArrayList();

        if(mainApp.getStock() != null) {
            for(StockData entry : mainApp.getStock().getHistory().getHistory().values()) {
                java.util.Date juDate = new GregorianCalendar(entry.getDate().getYear(), entry.getDate().getMonth() - 1, entry.getDate().getDay()).getTime();
                dataList.add(new XYChart.Data<>(juDate, entry.getValue()));
            }
        }

        if(dataList.size() > 0) {
            return dataList;
        } else {
            return null;
        }
    }

    private ObservableList<XYChart.Data<java.util.Date, Number>> generateOStockSeriesData() {

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

        ObservableList<XYChart.Data<java.util.Date, Number>> dataList = FXCollections.observableArrayList();

        if(mainApp.getStock() != null && mainApp.getOStock() != null) {
            BigDecimal valueOfStockOnFirstDay = mainApp.getStock().getHistory().getStockData(mainApp.getStock().getHistory().getHistory().firstKey()).get().getValue();
            Assets assets = new Assets(valueOfStockOnFirstDay);

            DateRange breakTime = new DateRange(mainApp.getOStock().getOptimalDates()[0], mainApp.getOStock().getOptimalDates()[1]); // SellingDate -> BuyingDate

            for (Map.Entry<Date, StockData> data : mainApp.getStock().getHistory().getHistory().entrySet()) {
                BigDecimal valueOfAssets;
                if(breakTime.isInYearlyRange(data.getKey())) {
                    assets.sellAllStocks(data.getValue().getValue());

                    valueOfAssets = assets.amountOfMoney;

                } else {
                    assets.sellAllStocks(data.getValue().getValue());
                    valueOfAssets = assets.amountOfMoney;

                    assets.buyAllStocks(data.getValue().getValue());
                }

                java.util.Date juDate = new GregorianCalendar(data.getKey().getYear(), data.getKey().getMonth() - 1, data.getKey().getDay()).getTime();
                dataList.add(new XYChart.Data<>(juDate, valueOfAssets));

            }
        }

        if(dataList.size() > 0) {
            return dataList;
        } else {
            return null;
        }
    }

    private void showNoStockAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Missing Stock");
        alert.setHeaderText("No Stock is loaded yet");
        alert.setContentText("Do you want to try to load a stock?");

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == buttonYes) {
            loadStockButtonClicked();
        }
    }

    private void showWrongDatesAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong Dates");
        alert.setHeaderText("Please use valid dates");
        alert.setContentText("Every field must be filled and the end of each range must be after the beginning.");

        alert.showAndWait();
    }

    private void showStockLoadingErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("File missing");
        alert.setHeaderText("Database file db.json not found!");
        alert.setContentText("Please ensure that the correct file in the correct format is in the " +
                "same folder as this app.");

        alert.showAndWait();
    }
}

