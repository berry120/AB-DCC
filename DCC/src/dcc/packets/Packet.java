/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc.packets;

import dcc.DCCAddress;

/**
 *
 * @author mjrb5
 */
public interface Packet {
    
    DCCAddress getAddress();
    
}
