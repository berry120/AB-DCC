/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc.packets;

import dcc.DCCAddress;
import dcc.Loco;
import java.util.Set;

/**
 *
 * @author Michael
 */
public class FunctionPacket implements Packet {

    private DCCAddress address;
    private Set<Integer> functionsOn;

    private FunctionPacket(DCCAddress address, Set<Integer> functionsOn) {
        this.address = address;
        this.functionsOn = functionsOn;
    }

    public static FunctionPacket fromLoco(Loco loco) {
        return new FunctionPacket(loco.addressProperty().get(), loco.getFunctionsOn());
    }

    public DCCAddress getAddress() {
        return address;
    }

    public Set<Integer> getFunctionsOn() {
        return functionsOn;
    }
    
    public int getBitMask() {
        int ret = 0;
        for(int func : functionsOn) {
            ret |= (1<<func);
        }
        return ret;
    }

    public String toString() {
        String ret = address + "," + "Functions:";
        for (int funcNum : functionsOn) {
            ret += funcNum;
            ret += ",";
        }
        return ret;
    }

}
