package View;

import Controller.Logger;
import Controller.LoggingLevel;
import Model.Stock;
import Model.OptimizedStock;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.awt.*;
import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    private Stock stock;
    private OptimizedStock oStock;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MayToSeptember - Stock Optimizer");

        initRootLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Window.fxml"));

            rootLayout = (AnchorPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Load the controller
            WindowController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            Logger.log("Root Layout could not be loaded", LoggingLevel.ERROR);
            e.printStackTrace();
        }
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public OptimizedStock getOStock() {
        return oStock;
    }

    public void setOStock(OptimizedStock oStock) {
        this.oStock = oStock;
    }
}
