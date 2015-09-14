/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import dcc.DCCUtils.AddressType;

/**
 *
 * @author Michael
 */
public class DCCAddress {

    private int address;
    private AddressType type;

    public DCCAddress(int address, AddressType type) {
        if(address<1) {
            throw new IllegalArgumentException("Address must be >0");
        }
        if(address<1) {
            throw new IllegalArgumentException("Address must be >0");
        }
        this.address = address;
        this.type = type;
    }
    
    public static DCCAddress fromNum(int addressNum) {
        AddressType type;
        if (DCCUtils.isValidDCCShortAddress(addressNum)) {
            type = AddressType.SHORT;
        } else {
            type = AddressType.LONG;
        }
        return new DCCAddress(addressNum, type);
    }

    public int getAddress() {
        return address;
    }

    public AddressType getType() {
        return type;
    }

    public String toString() {
        String ret = Integer.toString(address);
//        if (type == AddressType.LONG && address < 128) {
//            ret += "(L)";
//        }
        return ret;
    }

}
