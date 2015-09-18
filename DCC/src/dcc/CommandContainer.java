/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mjrb5
 */
public class CommandContainer {

    private String functionCommand;
    private String movementCommand;
    private String resetCommand;
    private String programCommand;

    public CommandContainer() {
    }

    public void setFunctionCommand(String functionCommand) {
        this.functionCommand = functionCommand;
    }

    public void setMovementCommand(String movementCommand) {
        this.movementCommand = movementCommand;
    }

    public void setResetCommand(String resetCommand) {
        if (resetCommand != null) {
            setFunctionCommand(null);
            setMovementCommand(null);
        }
        this.resetCommand = resetCommand;
    }
    
    public void setProgramCommand(String programCommand) {
        if (programCommand != null) {
            setFunctionCommand(null);
            setMovementCommand(null);
        }
        this.programCommand = programCommand;
    }

    public List<String> getCommands() {
        List<String> ret = new ArrayList<>();
        if (movementCommand != null) {
            ret.add(movementCommand);
        }
        if (functionCommand != null) {
            ret.add(functionCommand);
        }
        if (programCommand != null) {
            ret.add(programCommand);
        }
        if (resetCommand != null) {
            ret.add(resetCommand);
        }
        return ret;
    }

}
