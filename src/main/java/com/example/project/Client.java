package com.example.project;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

// TODO LIST:
// TODO: Add TCP client code to send\receive data from server
// TODO: consider design changes
// TODO: add buttons to connect\disconnect from server
// todo: 1+ modules in one class
// TODO: add error handling + custom exception
// TODO: default values for input fields \ default DB values
// TODO: something else ?

// optional TODO LIST:
// todo: fxml or css ?
// todo: add more features, buttons, etc
// todo: menu bar with diff options
// todo: host, port, etc as input fields or in menu settings + default values
// todo: somethign else?
public class Client extends Application {

    private TextArea responseArea; // For displaying server responses
    private TextField dateField, timeField, roomField, classField; // Class details input

    @Override
    public void start(Stage primaryStage) {
        // Action selection
        ComboBox<String> actionComboBox = new ComboBox<>();
        actionComboBox.getItems().addAll("Add Class", "Remove Class", "Display Schedule");

        // Initialize class details input fields
        dateField = new TextField();
        dateField.setPromptText("Date (dd/MM/yyyy)");

        timeField = new TextField();
        timeField.setPromptText("Time (HH:mm)");

        roomField = new TextField();
        roomField.setPromptText("Room Number");

        classField = new TextField();
        classField.setPromptText("Class Name");

        // Listener to handle action selection changes
        actionComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateFieldVisibility(newValue);
            }
        });

        // Send Request Button
        Button sendButton = new Button("Send Request");
        sendButton.setOnAction(e -> {
            String response = simulateServerResponse(actionComboBox.getValue(), classField.getText());
            responseArea.setText(response);
        });

        // Response Display
        responseArea = new TextArea();
        responseArea.setEditable(false);

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(actionComboBox, 0, 0);
        grid.add(dateField, 0, 1);
        grid.add(timeField, 0, 2);
        grid.add(roomField, 0, 3);
        grid.add(classField, 0, 4);
        grid.add(sendButton, 0, 5);
        grid.add(responseArea, 0, 6);

        // Scene and Stage
        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setTitle("Class Scheduler Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateFieldVisibility(String action) {
        boolean isScheduleAction = "Display Schedule".equals(action);

        // Date, Time, and Room fields are not needed for Display Schedule action
        dateField.setVisible(!isScheduleAction);
        dateField.setDisable(isScheduleAction);

        timeField.setVisible(!isScheduleAction);
        timeField.setDisable(isScheduleAction);

        roomField.setVisible(!isScheduleAction);
        roomField.setDisable(isScheduleAction);

        // Class Name field is always visible but enabled for all actions
        classField.setDisable(false);
    }

    private String simulateServerResponse(String action, String className) {
        if ("Add Class".equals(action)) {
            return "Class " + className + " added successfully.";
        } else if ("Remove Class".equals(action)) {
            return "Class " + className + " removed successfully.";
        } else if ("Display Schedule".equals(action)) {
            return "Schedule for class " + className + ":\n[Server response simulated]";
        } else {
            return "Invalid action.";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
