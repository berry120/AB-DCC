/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Michael
 */
public class VelocitySlider extends StackPane {
    
    private static final Color GO_COLOR = Color.MEDIUMSEAGREEN;
    private static final Color STOP_COLOR = Color.INDIANRED;
    
    private Slider slider;
    private Rectangle background;
    private Rectangle progress;
    
    private IntegerProperty speedProperty = new SimpleIntegerProperty();
    private BooleanProperty stoppedProperty = new SimpleBooleanProperty(false);

    public VelocitySlider() {
        slider = new Slider();
        slider.setOrientation(Orientation.VERTICAL);
        slider.getStyleClass().add("velslider");
        slider.setMin(0);
        slider.setMax(126);
        speedProperty.bind(slider.valueProperty());

        background = new Rectangle(10, getHeight(), Color.WHITE);
        background.heightProperty().bind(heightProperty());
        progress = new Rectangle(10, 1, GO_COLOR);
        StackPane.setAlignment(progress, Pos.BOTTOM_CENTER);
        progress.heightProperty().bind(slider.valueProperty().divide(slider.maxProperty()).multiply(heightProperty()));
        progress.getStyleClass().add("velprogress");

        getChildren().addAll(background, progress, slider);
        
        stoppedProperty.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    progress.setFill(STOP_COLOR);
                }
                else {
                    progress.setFill(GO_COLOR);
                }
            }
        });
        
        setMaxHeight(2000);
        setMinHeight(50);
    }
    
    public BooleanProperty stoppedProperty() {
        return stoppedProperty;
    }
    
    public IntegerProperty speedProperty() {
        return speedProperty;
    }

}
