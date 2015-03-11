package View;

import Controller.Logger;
import Controller.LoggingLevel;
import Model.DatabaseConnection;
import Model.Stock;
import Model.OptimizedStock;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

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
}