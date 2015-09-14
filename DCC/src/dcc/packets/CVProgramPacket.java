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
public class CVProgramPacket {

    private DCCAddress address;
    private int cvAddress;
    private int cvVal;

    public CVProgramPacket(DCCAddress address, int cvAddress, int cvVal) {
        this.address = address;
        this.cvAddress = cvAddress;
        this.cvVal = cvVal;
    }

    public DCCAddress getAddress() {
        return address;
    }

    public int getCvAddress() {
        return cvAddress;
    }

    public int getCvNewVal() {
        return cvVal;
    }

    public String toString() {
        return address.getAddress() + ",CV address " + cvAddress + " value " + cvVal;
    }

}
