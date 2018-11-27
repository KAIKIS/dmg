package com.github.kaikis.dmg.app;

import com.github.kaikis.dmg.resourceloader.ResourceLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            VBox vBox = (VBox) loader.load(ResourceLoader.getResourceAsStream("mainUI.fxml"));

            // Show the scene containing the root layout.
            Scene scene = new Scene(vBox);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
