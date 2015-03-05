package View;

import Controller.Logger;
import Controller.LoggingLevel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MayToSeptember - Stock Optimizer");

        initRootLayout();

        initStockView();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Window.fxml"));

            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            Logger.log("Root Layout could not be loaded", LoggingLevel.ERROR);
            e.printStackTrace();
        }
    }

    private void initStockView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Main.fxml"));

            AnchorPane stockView = (AnchorPane) loader.load();

            rootLayout.setCenter(stockView);

        } catch (IOException e) {
            Logger.log("Stock View could not be loaded", LoggingLevel.ERROR);
            e.printStackTrace();
        }

    }
}
