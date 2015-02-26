package Model;

import Controller.Logger;
import Controller.LoggingLevel;
import Controller.Settings;

import java.math.BigDecimal;
import java.sql.*;

public class DatabaseConnection {
    private Connection connection = null;

    /**
     * Creates a new <code>DatabaseConnection</code> from the Settings
     */
    public DatabaseConnection() {
        try {
            Class.forName(Settings.JDBC_DRIVER_STRING);
        } catch (ClassNotFoundException e) {
            Logger.log("JDBC Driver not found", LoggingLevel.ERROR);
            e.printStackTrace();
        }
        Logger.log("JDBC Driver loaded!", LoggingLevel.DEBUG);

        try {
            connection = DriverManager.getConnection(Settings.JDBC_CONNECTION_STRING, Settings.MYSQL_USERNAME, Settings.MYSQL_PASSWORD);
        } catch (SQLException e) {
            Logger.log("SQLException: " + e.getMessage(), LoggingLevel.ERROR);
            Logger.log("SQLState: " + e.getSQLState(), LoggingLevel.DEBUG);
            Logger.log("VendorError: " + e.getErrorCode(), LoggingLevel.DEBUG);
            e.printStackTrace();
        }
        Logger.log("Connection to Database established", LoggingLevel.DEBUG);
    }

    /**
     * This Method creates a Stock Object from the Database.
     *
     * @return the saved Stock object from database
     */
    public Stock getStockFromDatabase() {
        Stock resultStock = null;
        try {
            Statement stockStatement = connection.createStatement();
            ResultSet stockRS = stockStatement.executeQuery("SELECT Name, ShortName FROM stock WHERE id = 1");
            stockRS.next(); // Only the first Row returned will be used as the Stock name + short name
            String stockName = stockRS.getString("Name");
            String stockShortName = stockRS.getString("ShortName");
            resultStock = new Stock(stockName, stockShortName);

            Statement stockHistoryStatement = connection.createStatement();
            ResultSet stockHistoryRS = stockHistoryStatement.executeQuery("SELECT Date, Price FROM stockhistory");

            StockHistory history = new StockHistory();
            while(stockHistoryRS.next()) {
                BigDecimal price = stockHistoryRS.getBigDecimal("Price");

                String dateString = stockHistoryRS.getString("Date");
                Date date = new Date(dateString);

                StockData newData = new StockData(price, date);
                history.addData(newData);
            }

            resultStock.setHistory(history);
        } catch (SQLException e) {
            Logger.log("SQLException: " + e.getMessage(), LoggingLevel.ERROR);
            Logger.log("SQLState: " + e.getSQLState(), LoggingLevel.DEBUG);
            Logger.log("VendorError: " + e.getErrorCode(), LoggingLevel.DEBUG);
            e.printStackTrace();
        }
        Logger.log("Stock returned from Database", LoggingLevel.DEBUG);
        return resultStock;
    }

    /**
     * Saves the Stock to Database.
     * Overwrites the old saved stock data.
     * @param saveStock The stock to be saved
     */
    public void saveStockToDatabase(Stock saveStock) {
        try {
            Statement stockStatement = connection.createStatement();
            Statement stockHistoryDeleteStatement = connection.createStatement();

            String stockUpdateSQL = "UPDATE Stock " +
                                        "SET name = " + saveStock.getName() + ", shortname = " + saveStock.getShortName() + " " +
                                        "WHERE id=1;"; // Just one row is used from that table

            String stockHistoryDeleteSQL = "DELETE FROM StockHistory";

            stockStatement.execute(stockUpdateSQL);
            stockHistoryDeleteStatement.execute(stockHistoryDeleteSQL); // Clear up history

            for(StockData entry : saveStock.getHistory().getHistory().values()) {
                Statement stockHistoryInsertStatement = connection.createStatement();
                String stockHistoryInsertSQL = "INSERT INTO StockHistory" +
                                                "VALUES (" + entry.getValue().toString() + ", " + entry.getDate().toString() + ");"; // TODO
                stockHistoryInsertStatement.execute(stockHistoryInsertSQL);
            }
            Logger.log("Stock succesfully saved into Database", LoggingLevel.DEBUG);
        } catch (SQLException e) {
            Logger.log("SQLException: " + e.getMessage(), LoggingLevel.ERROR);
            Logger.log("SQLState: " + e.getSQLState(), LoggingLevel.DEBUG);
            Logger.log("VendorError: " + e.getErrorCode(), LoggingLevel.DEBUG);
            e.printStackTrace();
        }
    }
}
