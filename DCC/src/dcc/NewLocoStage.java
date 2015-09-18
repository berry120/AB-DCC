/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Michael
 */
public class NewLocoStage extends Stage {

    private Button addButton;
    private Button cancelButton;
    private ObjectProperty<DCCAddress> addressProperty;

    public NewLocoStage() {
        setTitle("New Loco");
        setOnShown((WindowEvent event) -> {
            DCCUtils.centre(NewLocoStage.this);
        });
        addressProperty = new SimpleObjectProperty<>();
        VBox mainPane = new VBox(20);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setPadding(new Insets(10));
        HBox inputPane = new HBox(10);
        inputPane.setAlignment(Pos.CENTER);
        Label lab = new Label("Address");
        lab.getStyleClass().add("addressLabel");
        TextField tf = new TextField();
        tf.getStyleClass().add("addressField");
        inputPane.getChildren().addAll(lab, tf);

        tf.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newAddress) -> {
            try {
                int address = Integer.parseInt(newAddress);
                boolean valid = DCCUtils.isValidDCCAddress(address);
                addButton.setDisable(!valid);
                if(valid) {
                    tf.setStyle("-fx-text-fill:black;");
                }
                else {
                    tf.setStyle("-fx-text-fill:red;");
                }
            } catch (NumberFormatException ex) {
                tf.setStyle("-fx-text-fill:red;");
                addButton.setDisable(true);
            }
        });

        addButton = new Button("Add loco");
        addButton.getStyleClass().add("newButton");
        addButton.setDisable(true);
        addButton.setDefaultButton(true);
        addButton.setOnAction((ActionEvent event) -> {
            int address = 0;
            try {
                address = Integer.parseInt(tf.getText());
            } catch (NumberFormatException ex) {
            }
            if (DCCUtils.isValidDCCAddress(address)) {
                addressProperty.set(DCCAddress.fromNum(address));
            }
            hide();
        });
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("newButton");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                addressProperty.set(null);
                hide();
            }
        });
        buttons.getChildren().addAll(addButton, cancelButton);
        mainPane.getChildren().addAll(inputPane, buttons);
        Scene scene = new Scene(mainPane);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ESCAPE) {
                    ((Stage) scene.getWindow()).hide();;
                }
            }
        });
        scene.getStylesheets().add(DCCUtils.STYLESHEET);
        setScene(scene);
    }

    public DCCAddress getAddress() {
        return addressProperty.get();
    }

}
