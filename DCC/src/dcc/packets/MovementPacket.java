/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc.packets;

import dcc.DCCAddress;
import dcc.DCCUtils;
import dcc.Loco;

/**
 *
 * @author Michael
 */
public class MovementPacket implements Packet {

    private DCCUtils.Direction direction;
    private DCCUtils.SpeedMode speedMode;
    private int speed;
    private DCCAddress address;
    private boolean emergencyStop;
    private boolean stop;

    public MovementPacket(DCCUtils.Direction direction, DCCUtils.SpeedMode speedMode, int speed, DCCAddress address, boolean emergencyStop, boolean stop) {
        this.direction = direction;
        this.speedMode = speedMode;
        this.speed = speed;
        this.address = address;
        this.emergencyStop = emergencyStop;
        this.stop = stop;
    }

    public static MovementPacket fromCurrentLocoState(Loco loco) {
        return new MovementPacket(loco.directionProperty().get(), loco.speedModeProperty().get(), loco.speedProperty().get(), loco.addressProperty().get(), loco.emergencyStoppedProperty().get(), loco.stoppedProperty().get());
    }

    public DCCUtils.Direction getDirection() {
        return direction;
    }

    public DCCUtils.SpeedMode getSpeedMode() {
        return speedMode;
    }

    public int getSpeed() {
        return speed;
    }

    public DCCAddress getAddress() {
        return address;
    }

    public boolean isEmergencyStop() {
        return emergencyStop;
    }

    public boolean isStop() {
        return stop;
    }
    
    public String toString() {
        String ret = address + ",";
        if (emergencyStop) {
            ret += "estop";
        } else if (stop) {
            ret += "stop";
        } else {
            ret += direction + "," + speed + "," + speedMode;
        }
        return ret;
    }

}
