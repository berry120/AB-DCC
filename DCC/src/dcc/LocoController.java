/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import dcc.packets.CVProgramPacket;
import dcc.packets.FunctionPacket;
import dcc.packets.HardResetPacket;
import dcc.packets.MovementPacket;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;

/**
 *
 * @author Michael
 */
public class LocoController {

    private DCCCommunicator comm;
    private ObservableList<Loco> locos;

    public LocoController() {
        comm = new DebugCommunicator();
        locos = FXCollections.observableArrayList();
    }

    public void addLoco(Loco loco) {
        loco.speedProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            comm.setMovement(MovementPacket.fromCurrentLocoState(loco));
        });
        loco.directionProperty().addListener((ObservableValue<? extends DCCUtils.Direction> observable, DCCUtils.Direction oldValue, DCCUtils.Direction newValue) -> {
            comm.setMovement(MovementPacket.fromCurrentLocoState(loco));
        });
        loco.speedModeProperty().addListener((ObservableValue<? extends DCCUtils.SpeedMode> observable, DCCUtils.SpeedMode oldValue, DCCUtils.SpeedMode newValue) -> {
            comm.setMovement(MovementPacket.fromCurrentLocoState(loco));
        });
        loco.stoppedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            comm.setMovement(MovementPacket.fromCurrentLocoState(loco));
        });
        loco.emergencyStoppedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            comm.setMovement(MovementPacket.fromCurrentLocoState(loco));
        });
        loco.addressProperty().addListener((ObservableValue<? extends DCCAddress> observable, DCCAddress oldAddress, DCCAddress newValue) -> {
            comm.reset(new HardResetPacket(oldAddress));
        });
        loco.getFunctionsOn().addListener(new SetChangeListener<Integer>() {

            @Override
            public void onChanged(SetChangeListener.Change<? extends Integer> change) {
                comm.setFunction(FunctionPacket.fromLoco(loco));
            }
        });
        loco.getCVQueue().addListener(new ListChangeListener<CVProgramPacket>() {

            @Override
            public void onChanged(ListChangeListener.Change<? extends CVProgramPacket> c) {
                while (c.next()) {
                    for (CVProgramPacket packet : c.getAddedSubList()) {
                        System.out.println(packet);
                        loco.getCVQueue().remove(packet);
                    }
                }
            }
        });
        locos.add(loco);
    }

    public boolean hasLoco(DCCAddress address) {
        for (Loco loco : locos) {
            if (loco.addressProperty().get().getAddress() == address.getAddress()) {
                return true;
            }
        }
        return false;
    }

    public void removeLoco(Loco loco) {
        comm.reset(new HardResetPacket(loco.addressProperty().get()));
        locos.remove(loco);
    }

}
