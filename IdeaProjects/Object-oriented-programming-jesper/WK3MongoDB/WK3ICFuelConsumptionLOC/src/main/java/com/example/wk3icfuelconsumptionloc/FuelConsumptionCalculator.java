package com.example.wk3icfuelconsumptionloc;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class FuelConsumptionCalculator extends Application {
    private ResourceBundle bundle;
    private Label distanceLabel, fuelLabel, resultLabel;
    private TextField distanceField, fuelField;
    private Button calculateButton;

    private String LastClickedLanguage = "en";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Jesper Selenius");

        // Text fields for distance and fuel used
        distanceField = new TextField();
        fuelField = new TextField();

        // Initialize the button and label UI components before calling loadLanguage
        distanceLabel = new Label();
        fuelLabel = new Label();
        calculateButton = new Button();
        resultLabel = new Label();

        // Language buttons
        Button enButton = new Button("EN");
        Button frButton = new Button("FR");
        Button jpButton = new Button("JP");
        Button irButton = new Button("IR");

        // Set widths for uniformity
        distanceField.setPrefWidth(200);
        fuelField.setPrefWidth(200);
        calculateButton.setPrefWidth(200);
        resultLabel.setPrefWidth(200);
        enButton.setPrefWidth(50);
        frButton.setPrefWidth(50);
        jpButton.setPrefWidth(50);
        irButton.setPrefWidth(50);

        // Layout for input fields
        VBox inputFields = new VBox(10, distanceLabel, distanceField, fuelLabel, fuelField, calculateButton, resultLabel);
        inputFields.setAlignment(Pos.CENTER_LEFT);

        VBox langButtons = new VBox(10, enButton, frButton, jpButton, irButton);
        langButtons.setAlignment(Pos.CENTER_LEFT);

        HBox mainLayout = new HBox(20, inputFields, langButtons);
        mainLayout.setPadding(new Insets(20));

        loadLanguage(Locale.ENGLISH);

        calculateButton.setOnAction(event -> {
            try {
                double distance = Double.parseDouble(distanceField.getText());
                double fuelUsed = Double.parseDouble(fuelField.getText());

                double fuelConsumption = (fuelUsed / distance) * 100;

                resultLabel.setText(bundle.getString("result").replace("{0}", String.format("%.2f", fuelConsumption)));

                saveDataToDatabase(distance, fuelUsed, fuelConsumption, LastClickedLanguage); // Assuming English for simplicity

            } catch (NumberFormatException e) {
                // Handle invalid input
                resultLabel.setText(bundle.getString("error"));
            } catch (SQLException e) {
                // Handle SQL exception
                resultLabel.setText("Error saving data to database.");
            }
        });

        // Set up language button actions
        enButton.setOnAction(e -> loadLanguage(new Locale("en"))); // English
        frButton.setOnAction(e -> loadLanguage(new Locale("fr"))); // French
        jpButton.setOnAction(e -> loadLanguage(new Locale("jp"))); // Japanese
        irButton.setOnAction(e -> loadLanguage(new Locale("ir"))); // Persian

        Scene scene = new Scene(mainLayout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void loadLanguage(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
        LastClickedLanguage = locale.getLanguage();
        distanceLabel.setText(bundle.getString("distance"));
        fuelLabel.setText(bundle.getString("fuelUsed"));
        calculateButton.setText(bundle.getString("calculate"));
        resultLabel.setText("");
    }

    // Save the calculated data to the database
    private void saveDataToDatabase(double distance, double fuelUsed, double consumption, String language) throws SQLException {
        String query = "INSERT INTO CalculatedConsumption (distance, fuel_used, consumption, language) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, distance);
            stmt.setDouble(2, fuelUsed);
            stmt.setDouble(3, consumption);
            stmt.setString(4, language);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully.");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}