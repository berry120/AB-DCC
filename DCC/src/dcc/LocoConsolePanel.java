/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Michael
 */
public class LocoConsolePanel extends HBox {

    private ObservableList<ConsoleItem> consoles;
    private LocoConsoleOutline outline;
    private LocoController controller;

    public LocoConsolePanel(LocoController controller) {
        super(5);
        setAlignment(Pos.CENTER);
        this.controller = controller;
        consoles = FXCollections.observableArrayList();
        outline = new LocoConsoleOutline();
        consoles.add(outline);
        getChildren().add(outline);
        outline.setOnAdd((ActionEvent event) -> {
            NewLocoStage nls = new NewLocoStage();
            nls.setOnHiding((WindowEvent we) -> {
                DCCAddress address = nls.getAddress();
                if(address!=null) {
                    if(controller.hasLoco(address)) {
                        new LocoAlreadyExistsDialog(address).showAndWait();
                    }
                    else {
                        outline.setOpacity(0);
                        Timeline animOutline = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(outline.opacityProperty(), 0)),
                                new KeyFrame(Duration.seconds(0.1), new KeyValue(outline.opacityProperty(), 1))
                        );
                        Loco loco = new Loco(address);
                        controller.addLoco(loco);
                        LocoConsole lc = new LocoConsole(loco);
                        lc.setOnClose((ActionEvent ae) -> {
                            consoles.remove(lc);
                            controller.removeLoco(loco);
                            getChildren().remove(lc);
                        });
                        lc.setOpacity(0);
                        Timeline animlc = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(lc.opacityProperty(), 0)),
                                new KeyFrame(Duration.seconds(0.1), new KeyValue(lc.opacityProperty(), 1))
                        );
                        consoles.add(consoles.size() - 1, lc);
                        getChildren().add(getChildren().size() - 1, lc);
                        animOutline.play();
                        animlc.play();
                    }
                }
            });
            nls.showAndWait();
        });
    }

}
