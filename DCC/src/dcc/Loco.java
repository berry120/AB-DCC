/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import dcc.DCCUtils.Direction;
import dcc.DCCUtils.SpeedMode;
import dcc.packets.CVProgramPacket;
import java.util.HashSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

/**
 *
 * @author Michael
 */
public class Loco {
    
    private final IntegerProperty speed;
    private final BooleanProperty stopped;
    private final BooleanProperty emergencyStopped;
    private final ObjectProperty<Direction> direction;
    private final ObjectProperty<DCCUtils.SpeedMode> speedMode;
    private final ObjectProperty<DCCAddress> address;
    private final ObservableSet<Integer> functionsOn;
    private final ObservableList<CVProgramPacket> cvQueue;
    
    public Loco(DCCAddress defaultAddress) {
        functionsOn = FXCollections.observableSet(new HashSet<>());
        address = new SimpleObjectProperty<>(defaultAddress);
        speed = new SimpleIntegerProperty(0);
        stopped = new SimpleBooleanProperty(true);
        emergencyStopped = new SimpleBooleanProperty(false);
        direction = new SimpleObjectProperty<>(Direction.FORWARD);
        speedMode = new SimpleObjectProperty<>(SpeedMode.SPEED_28);
        cvQueue = FXCollections.observableArrayList();
    }

    public ObjectProperty<DCCAddress> addressProperty() {
        return address;
    }

    public IntegerProperty speedProperty() {
        return speed;
    }

    public BooleanProperty stoppedProperty() {
        return stopped;
    }

    public BooleanProperty emergencyStoppedProperty() {
        return emergencyStopped;
    }

    public ObjectProperty<Direction> directionProperty() {
        return direction;
    }

    public ObjectProperty<SpeedMode> speedModeProperty() {
        return speedMode;
    }

    public ObservableSet<Integer> getFunctionsOn() {
        return functionsOn;
    }
    
    public ObservableList<CVProgramPacket> getCVQueue() {
        return cvQueue;
    }
    
}