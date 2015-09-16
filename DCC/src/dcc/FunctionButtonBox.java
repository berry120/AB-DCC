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
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 *
 * @author Michael
 */
public class FunctionButtonBox extends VBox {

    private static final int MAX_FUNCTIONS = 28;
    private final ObjectProperty<Loco> locoProperty;
    private int numButtons;

    public FunctionButtonBox(Loco loco) {
        super(5);
        locoProperty = new SimpleObjectProperty<>(loco);
        setAlignment(Pos.TOP_CENTER);
        heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newHeight) -> {
            int num = newHeight.intValue() / 55;
            if (num > MAX_FUNCTIONS) {
                num = MAX_FUNCTIONS;
            }
            if (num != numButtons) {
                numButtons = num;
                layoutButtons();
            }
        });
    }

    private void layoutButtons() {
        getChildren().clear();
        for (int i = 0; i < numButtons; i++) {
            FunctionButton but = new FunctionButton(i);
            but.setOnAction((ActionEvent event) -> {
                if (but.isSelected()) {
                    locoProperty.get().getFunctionsOn().add(but.getFuncNum());
                } else {
                    locoProperty.get().getFunctionsOn().remove(but.getFuncNum());
                }
            });
            but.getStyleClass().add("funcbut");
            getChildren().add(but);
        }
    }

}
