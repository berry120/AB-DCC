/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Michael
 */
public class LocoConsoleOutline extends StackPane implements ConsoleItem {
    
    private Rectangle outline = new Rectangle();
    private Button newLocoButton = new Button();
    
    public LocoConsoleOutline() {
        setAlignment(Pos.CENTER);
        outline.setFill(Color.TRANSPARENT);
        outline.setStroke(Color.web("#CCCCE0"));
        outline.setStrokeWidth(5);
        outline.setWidth(300);
        outline.heightProperty().bind(heightProperty());
        outline.setManaged(false);
        outline.getStrokeDashArray().addAll(25d, 10d);
        outline.setArcHeight(50);
        outline.setArcWidth(50);
        outline.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                newLocoButton.fire();
            }
        });
        
        newLocoButton = new Button("", new ImageView(new Image("file:img/add.png")));
        newLocoButton.getStyleClass().add("addbutton");
        
        getChildren().add(outline);
        getChildren().add(newLocoButton);
        setMaxHeight(500);
        setMinWidth(300);
    }
    
    public void setOnAdd(EventHandler<ActionEvent> eh) {
        newLocoButton.setOnAction(eh);
    }
    
}
