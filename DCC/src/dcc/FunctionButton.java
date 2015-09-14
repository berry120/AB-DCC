/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import javafx.scene.control.ToggleButton;

/**
 *
 * @author Michael
 */
public class FunctionButton extends ToggleButton {
    
    private int funcNum;
    
    public FunctionButton(int funcNum) {
        super("F" + funcNum);
        this.funcNum = funcNum;
    }

    public int getFuncNum() {
        return funcNum;
    }
    
}
