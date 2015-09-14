/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import dcc.packets.CVProgramPacket;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Michael
 */
public class CVProgramStage extends Stage {
    
    private TextField cvNumField;
    private TextField cvValueField;
    
    private CVProgramPacket packet;
    private Button programButton;
    private Button cancelButton;
    private Loco loco;

    public CVProgramStage(Loco loco) {
        this.loco = loco;
        setTitle("Program Raw CV");
        setOnShown(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                DCCUtils.centre(CVProgramStage.this);
            }
        });
        VBox mainPane = new VBox(20);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setPadding(new Insets(10));
        
        GridPane fieldPane = new GridPane();
        fieldPane.setAlignment(Pos.CENTER);
        fieldPane.setHgap(10);
        fieldPane.setVgap(10);
        
        Label addressLabel = new Label("Address");
        addressLabel.getStyleClass().add("addressLabel");
        cvNumField = new TextField();
        cvNumField.setText(Integer.toString(loco.addressProperty().get().getAddress()));
        cvNumField.setDisable(true);
        cvNumField.getStyleClass().add("addressField");
        fieldPane.add(addressLabel, 0, 0);
        fieldPane.add(cvNumField, 1, 0);
        
        Label cvNumLabel = new Label("CV Number");
        cvNumLabel.getStyleClass().add("addressLabel");
        cvNumField = new TextField();
        cvNumField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newAddress) -> {
            validate();
        });
        cvNumField.getStyleClass().add("addressField");
        fieldPane.add(cvNumLabel, 0, 1);
        fieldPane.add(cvNumField, 1, 1);
        
        Label cvValueLabel = new Label("CV Value");
        cvValueLabel.getStyleClass().add("addressLabel");
        cvValueField = new TextField();
        cvValueField.getStyleClass().add("addressField");
        cvValueField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            validate();
        });
        fieldPane.add(cvValueLabel, 0, 2);
        fieldPane.add(cvValueField, 1, 2);

        programButton = new Button("Program CV");
        programButton.setDisable(true);
        programButton.getStyleClass().add("newButton");
        programButton.setDefaultButton(true);
        programButton.setOnAction((ActionEvent event) -> {
            DCCAddress address = loco.addressProperty().get();
            int cvNum = Integer.parseInt(cvNumField.getText());
            int cvVal = Integer.parseInt(cvValueField.getText());
            packet = new CVProgramPacket(address, cvNum, cvVal);
            hide();
        });
        
        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("newButton");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                hide();
            }
        });
        bottomButtons.getChildren().addAll(programButton, cancelButton);
        mainPane.getChildren().addAll(fieldPane, bottomButtons);
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
    
    private void validate() {
        boolean addressValid;
        try {
            int address = Integer.parseInt(cvNumField.getText());
            addressValid = DCCUtils.isValidCVAddress(address);
            if (addressValid) {
                cvNumField.setStyle("-fx-text-fill:black;");
            } else {
                cvNumField.setStyle("-fx-text-fill:red;");
            }
        } catch (NumberFormatException ex) {
            cvNumField.setStyle("-fx-text-fill:red;");
            addressValid = false;
        }
        
        boolean valValid;
        try {
            int cvValue = Integer.parseInt(cvValueField.getText());
            valValid = DCCUtils.isValidCVValue(cvValue);
            if (valValid) {
                cvValueField.setStyle("-fx-text-fill:black;");
            } else {
                cvValueField.setStyle("-fx-text-fill:red;");
            }
        } catch (NumberFormatException ex) {
            cvValueField.setStyle("-fx-text-fill:red;");
            valValid = false;
        }
        
        programButton.setDisable(!(addressValid && valValid));
    }

    public CVProgramPacket getPacket() {
        return packet;
    }
    
}
