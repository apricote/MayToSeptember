package com.toelle.maytoseptember.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toelle.maytoseptember.model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabaseConnection {

    public DatabaseConnection() {
    }

    /**
     * This Method creates a Stock Object from the JSON.
     * It looks up the file <code>./db.json</code> and reads from that.
     *
     * @return the saved Stock object from json
     */
    public Stock getStockFromDatabase() {
        Path dbPath = Paths.get("db.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn;
        String stockName = "";
        String stockShortName = "";
        StockHistory sh = new StockHistory();

        try {
            jn = mapper.readTree(dbPath.toFile());
            stockName = jn.get("Name").asText();
            stockShortName = jn.get("Shortname").asText();

            JsonNode jnStockData = jn.get("StockData");

            for(int i = 0; i < jnStockData.size(); i++) {
                JsonNode jnStockDataElement = jnStockData.get(i);
                Date stockDataDate = new Date(jnStockDataElement.get("Date").asText());
                BigDecimal stockDataValue = new BigDecimal(jnStockDataElement.get("Value").asText());

                StockData stockData = new StockData(stockDataValue, stockDataDate);
                sh.addData(stockData);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log("Die Datei db.json wurde nicht gefunden.", LoggingLevel.ERROR);
            Logger.log("Es konnte kein Stockobjekt generiert werden." , LoggingLevel.WARNING);
        }


        Logger.log("Finished reading in Data from JSON", LoggingLevel.DEBUG);
        //TODO reimplement
        return new Stock(stockName, stockShortName, sh);
    }
}
