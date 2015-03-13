package View;

import Controller.Logger;
import Controller.LoggingLevel;
import Controller.StockOptimizer;
import Model.*;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class WindowController {

    @FXML
    private LineChart<Number, Number> assetWorthGraph;

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

    private Main mainApp;

    @FXML
    private void initialize() {
        // Setup DatePickers for MayToSeptember selection
        sellingTimeStartPicker.setValue(LocalDate.of(2015, 5, 1));
        sellingTimeEndPicker.setValue(LocalDate.of(2015, 5, 31));
        buyingTimeStartPicker.setValue(LocalDate.of(2015, 9, 1));
        buyingTimeEndPicker.setValue(LocalDate.of(2015, 9, 30));
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void loadStockButtonClicked() {
        DatabaseConnection db = new DatabaseConnection();
        Stock stock = db.getStockFromDatabase();
        mainApp.setStock(stock);
        reloadStockLabels();
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
            return;
            //TODO Errorhandling
        }

        if(sellingTimeStartPicker.getValue() == null) {
            Logger.log("Null", LoggingLevel.ERROR);
        } else {
            Logger.log(sellingTimeStartPicker.getValue().toString(), LoggingLevel.ERROR);
        }

        if( sellingTimeStartPicker.getValue() == null ||
                sellingTimeEndPicker.getValue() == null ||
                buyingTimeStartPicker.getValue() == null ||
                buyingTimeEndPicker.getValue() == null) {
            return;
            //TODO Errorhandling
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
            return;
            //TODO Errorhandling
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
    }
}