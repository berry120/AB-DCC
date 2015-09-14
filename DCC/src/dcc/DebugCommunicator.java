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

/**
 *
 * @author Michael
 */
public class DebugCommunicator implements DCCCommunicator {

    @Override
    public void setMovement(MovementPacket packet) {
        System.out.println(packet.toString());
    }

    @Override
    public void reset(HardResetPacket packet) {
        System.out.println(packet.toString());
    }

    @Override
    public void setFunction(FunctionPacket packet) {
        System.out.println(packet.toString());
    }

    @Override
    public void programCV(CVProgramPacket packet) {
        System.out.println(packet.toString());
    }
    
}
