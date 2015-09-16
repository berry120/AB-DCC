/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc.packets;

import dcc.DCCAddress;

/**
 *
 * @author Michael
 */
public class HardResetPacket {
    
    private DCCAddress address;
    
    public HardResetPacket(DCCAddress address) {
        this.address = address;
    }
    
    public String toString() {
        return address+",reset";
    }

    public DCCAddress getAddress() {
        return address;
    }
    
}
