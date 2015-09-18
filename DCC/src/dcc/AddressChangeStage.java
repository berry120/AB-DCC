/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import dcc.packets.CVProgramPacket;
import dcc.packets.Packet;
import java.util.ArrayList;
import java.util.List;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Michael
 */
public class AddressChangeStage extends Stage {

    private TextField newAddressField;

    private List<CVProgramPacket> packets;
    private DCCAddress newAddress;
    private final Button programButton;
    private final Button cancelButton;
    private final Loco loco;

    public AddressChangeStage(Loco loco) {
        initModality(Modality.APPLICATION_MODAL);
        this.loco = loco;
        packets = new ArrayList<>();
        setTitle("Change address");
        setOnShown((WindowEvent event) -> {
            DCCUtils.centre(AddressChangeStage.this);
        });
        VBox mainPane = new VBox(20);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setPadding(new Insets(10));

        Label desc = new Label("Change address " + loco.addressProperty().get().getAddress());
        desc.getStyleClass().add("addressLabel");

        GridPane fieldPane = new GridPane();
        fieldPane.setAlignment(Pos.CENTER);
        fieldPane.setHgap(10);
        fieldPane.setVgap(10);

        Label addressLabel = new Label("Address");
        addressLabel.getStyleClass().add("addressLabel");
        newAddressField = new TextField();
        newAddressField.setText(Integer.toString(loco.addressProperty().get().getAddress()));
        newAddressField.setDisable(true);
        newAddressField.getStyleClass().add("addressField");
        fieldPane.add(addressLabel, 0, 0);
        fieldPane.add(newAddressField, 1, 0);

        Label newAddressLabel = new Label("New Address");
        newAddressLabel.getStyleClass().add("addressLabel");
        newAddressField = new TextField();
        newAddressField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newAddress) -> {
            validate();
        });
        newAddressField.getStyleClass().add("addressField");
        fieldPane.add(newAddressLabel, 0, 1);
        fieldPane.add(newAddressField, 1, 1);

        programButton = new Button("Program New Address");
        programButton.setDisable(true);
        programButton.getStyleClass().add("newButton");
        programButton.setDefaultButton(true);
        programButton.setOnAction((ActionEvent event) -> {
            DCCAddress address = loco.addressProperty().get();
            int cvVal = Integer.parseInt(newAddressField.getText());
            if (DCCUtils.isValidDCCShortAddress(cvVal)) {
                packets.add(new CVProgramPacket(address, 1, cvVal));
            } else {
                int cv18 = cvVal & 0xFF; //Lower byte
                int cv17 = (cvVal >> 8) & 0xFF; //Upper byte
                packets.add(new CVProgramPacket(address, 17, cv17));
                packets.add(new CVProgramPacket(address, 18, cv18));
            }
            newAddress = DCCAddress.fromNum(cvVal);
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
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent t) -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                ((Stage) scene.getWindow()).hide();
            }
        });
        scene.getStylesheets().add(DCCUtils.STYLESHEET);
        setScene(scene);
    }

    private void validate() {
        boolean addressValid;
        try {
            int address = Integer.parseInt(newAddressField.getText());
            addressValid = DCCUtils.isValidDCCAddress(address);
            if (addressValid) {
                newAddressField.setStyle("-fx-text-fill:black;");
            } else {
                newAddressField.setStyle("-fx-text-fill:red;");
            }
        } catch (NumberFormatException ex) {
            newAddressField.setStyle("-fx-text-fill:red;");
            addressValid = false;
        }

        programButton.setDisable(!(addressValid));
    }

    public List<CVProgramPacket> getPackets() {
        return packets;
    }

    public DCCAddress getNewAddress() {
        return newAddress;
    }

}
