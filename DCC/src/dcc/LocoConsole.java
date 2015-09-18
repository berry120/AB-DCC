/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import dcc.DCCUtils.Direction;
import dcc.packets.CVProgramPacket;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Michael
 */
public class LocoConsole extends VBox implements ConsoleItem {

    private ObjectProperty<Loco> locoProperty;

    private HBox headerBox;
    private HBox directionBox;
    private HBox mainBox;

    private Button addressButton;
    private Button settingsButton;
    private Button removeButton;
    private ImageView locoImg;
    private ToggleButton leftArrow;
    private ToggleButton rightArrow;
    private VelocitySlider speedSlider;
    private ToggleButton eStop;

    public LocoConsole(Loco loco) {
        super(10);
        setBackground(new Background(new BackgroundFill(Color.web("#CCCCE0"), new CornerRadii(20, false), Insets.EMPTY)));
        setAlignment(Pos.TOP_CENTER);

        headerBox = new HBox(130);
        VBox.setMargin(headerBox, new Insets(5, 5, 0, 5));
        headerBox.setAlignment(Pos.TOP_CENTER);
        addressButton = new Button(loco.addressProperty().asString().get());
        addressButton.setOnAction((ActionEvent event) -> {
            AddressChangeStage acs = new AddressChangeStage(loco);
            acs.showAndWait();
            List<CVProgramPacket> packets = acs.getPackets();
            for(CVProgramPacket packet : packets) {
                loco.getCVQueue().add(packet);
            }
            DCCAddress newAddress = acs.getNewAddress();
            if(newAddress!=null) {
                loco.addressProperty().set(newAddress);
            }
        });
        addressButton.getStyleClass().add("addressButton");
        settingsButton = new Button("", new ImageView(new Image("file:img/settings.png")));
        settingsButton.setOnAction((ActionEvent event) -> {
            CVProgramStage prog = new CVProgramStage(loco);
            prog.showAndWait();
            CVProgramPacket packet = prog.getPacket();
            if (packet != null) {
                loco.getCVQueue().add(packet);
            }
        });
        settingsButton.getStyleClass().add("settingsButton");
        removeButton = new Button("", new ImageView(new Image("file:img/remove.png", 16, 16, true, true)));
        removeButton.getStyleClass().add("removeButton");
        HBox leftBox = new HBox(5);
        leftBox.setAlignment(Pos.TOP_CENTER);
        leftBox.getChildren().addAll(addressButton, settingsButton);
        headerBox.getChildren().addAll(leftBox, removeButton);
        getChildren().add(headerBox);

        locoImg = new ImageView(new Image("file:img/train.png", 256, 1000, true, true));
        StackPane imgPane = new StackPane();
        VBox.setMargin(imgPane, new Insets(0, 5, 0, 5));
        imgPane.setBackground(new Background(new BackgroundFill(Color.web("#FFEBCC"), new CornerRadii(20, false), Insets.EMPTY)));
        imgPane.getChildren().add(locoImg);
        getChildren().add(imgPane);

        directionBox = new HBox(10);
        directionBox.setAlignment(Pos.CENTER);
        leftArrow = new ToggleButton("", new ImageView(new Image("file:img/arrowleft.png")));
        rightArrow = new ToggleButton("", new ImageView(new Image("file:img/arrowright.png")));
        ToggleGroup tg = new ToggleGroup();
        leftArrow.setToggleGroup(tg);
        rightArrow.setToggleGroup(tg);
        preventToggleDeselect(leftArrow, tg);
        preventToggleDeselect(rightArrow, tg);
        rightArrow.setSelected(true);
        directionBox.getChildren().addAll(leftArrow, rightArrow);
        getChildren().add(directionBox);

        mainBox = new HBox(70);
        VBox.setVgrow(mainBox, Priority.ALWAYS);
        mainBox.setAlignment(Pos.CENTER);
        speedSlider = new VelocitySlider();
        FunctionButtonBox buttonBox = new FunctionButtonBox(loco);
        mainBox.getChildren().addAll(speedSlider, buttonBox);
        mainBox.setMinHeight(50);
        mainBox.setMaxHeight(2000);
        getChildren().add(mainBox);

        eStop = new ToggleButton("STOP");
        eStop.getStyleClass().add("estop");
        VBox.setMargin(eStop, new Insets(0, 5, 0, 0));
        speedSlider.stoppedProperty().bind(eStop.selectedProperty());
        getChildren().add(eStop);

        setMinHeight(480);

        locoProperty = new SimpleObjectProperty<>();
        locoProperty.addListener((ObservableValue<? extends Loco> observable, Loco oldLoco, Loco newLoco) -> {
            newLoco.speedProperty().bind(speedSlider.speedProperty());
            newLoco.stoppedProperty().bind(speedSlider.stoppedProperty());
            rightArrow.setOnAction((ActionEvent event) -> {
                newLoco.directionProperty().set(Direction.FORWARD);
            });
            leftArrow.setOnAction((ActionEvent event) -> {
                newLoco.directionProperty().set(Direction.REVERSE);
            });
            addressButton.textProperty().bind(newLoco.addressProperty().asString());
        });
        locoProperty.set(loco);
    }

    public void setOnClose(EventHandler<ActionEvent> eh) {
        removeButton.setOnAction(eh);
    }

    public ObjectProperty<Loco> locoProperty() {
        return locoProperty;
    }

    private void preventToggleDeselect(ToggleButton but, ToggleGroup tg) {
        but.addEventFilter(MouseEvent.MOUSE_RELEASED, (MouseEvent mouseEvent) -> {
            if (but.equals(tg.getSelectedToggle())) {
                mouseEvent.consume();
            }
        });
    }

}
