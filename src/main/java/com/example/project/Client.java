package com.example.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Client extends Application {

    private Label outputLabel; // Label to display text that changes

    @Override
    public void start(Stage primaryStage) {
        // Text Field for user input
        TextField userInputField = new TextField();

        // Buttons with their respective actions
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> SendButtonCall());

        Button startButton = new Button("Start");
        startButton.setOnAction(e -> StartButtonCall());

        Button stopButton = new Button("Stop");
        stopButton.setOnAction(e -> StopButtonCall());

        // Label for program testing
        Label testingLabel = new Label("program testing");

        // Label for output
        outputLabel = new Label("Output will appear here...");

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(testingLabel, userInputField, sendButton, startButton, stopButton, outputLabel);
        layout.setAlignment(Pos.CENTER);

        // Scene
        Scene scene = new Scene(layout, 300, 300);

        // Stage
        primaryStage.setTitle("Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void SendButtonCall() {
        System.out.println("Send Button Pressed");
        // Implement the desired functionality here
    }

    private void StartButtonCall() {
        System.out.println("Start Button Pressed");
        // Implement the desired functionality here
    }

    private void StopButtonCall() {
        System.out.println("Stop Button Pressed");
        // Implement the desired functionality here
    }

    public void ChangeOutput(String newText) {
        outputLabel.setText(newText);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
