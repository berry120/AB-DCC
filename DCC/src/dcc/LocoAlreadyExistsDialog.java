/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Michael
 */
public class LocoAlreadyExistsDialog extends Stage {
    
    private Button okButton;
    
    public LocoAlreadyExistsDialog(DCCAddress address) {
        setTitle("Loco already added");
        setOnShown(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                DCCUtils.centre(LocoAlreadyExistsDialog.this);
            }
        });
        VBox mainPane = new VBox(20);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setPadding(new Insets(10));
        
        Label label = new Label("You've already added the loco with address " + address.getAddress() + ".");
        label.getStyleClass().add("biglabel");

        okButton = new Button("OK");
        okButton.getStyleClass().add("newButton");
        okButton.setDefaultButton(true);
        okButton.setOnAction((ActionEvent event) -> {
            hide();
        });
        mainPane.getChildren().addAll(label, okButton);
        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add(DCCUtils.STYLESHEET);
        setScene(scene);
        
    }
    
}
