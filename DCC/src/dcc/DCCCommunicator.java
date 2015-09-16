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
public interface DCCCommunicator {
    
    void setMovement(MovementPacket packet);
    
    void setFunction(FunctionPacket packet);
    
    void reset(HardResetPacket packet);
    
    void programCV(CVProgramPacket packet);
    
    void start();
    
}
