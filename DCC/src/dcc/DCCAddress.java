/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import dcc.DCCUtils.AddressType;
import java.util.Objects;

/**
 *
 * @author Michael
 */
public class DCCAddress {

    private int address;
    private AddressType type;

    public DCCAddress(int address, AddressType type) {
        if (address < 0) {
            throw new IllegalArgumentException("Address must be >=0");
        }
        if (address < 1) {
            throw new IllegalArgumentException("Address must be >=0");
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
    
    public boolean isBroadcast() {
        return address==0;
    }

    /**
     * Return the address type as an integer, where 1 indicates a long address
     * type and 0 represents a short address type. (This is useful for some
     * hardware implementations.)
     *
     * @return
     */
    public int getAddressTypeAsInt() {
        if (type == AddressType.LONG) {
            return 1;
        }
        return 0;
    }

    public String toString() {
        String ret = Integer.toString(address);
        if (type == AddressType.LONG) {
            ret += "(L)";
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.address;
        hash = 89 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DCCAddress other = (DCCAddress) obj;
        if (this.address != other.address) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

}
