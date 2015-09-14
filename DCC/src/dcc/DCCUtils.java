/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author Michael
 */
public class DCCUtils {
    
    public enum SpeedMode {

        SPEED_128, SPEED_28, SPEED_14
    };

    public enum Direction {

        FORWARD, REVERSE

    }

    public enum AddressType {

        SHORT, LONG
    }

    public static final String STYLESHEET = "file:img/style.css";
    private static Stage primaryStage = null;

    private DCCUtils() {
    }

    public static boolean isValidDCCAddress(int address) {
        return address > 0 && address < 10240;
    }

    public static boolean isValidDCCShortAddress(int address) {
        return address > 0 && address < 128;
    }

    public static boolean isValidCVAddress(int address) {
        return address > 0 && address < 1025; 
   }

    public static boolean isValidCVValue(int val) {
        return val > 0 && val < 256;
    }

    
    public static void centre(Stage dialog) {
        double x = primaryStage.getX() + primaryStage.getWidth() / 2 - dialog.getWidth() / 2;
        double y = primaryStage.getY() + primaryStage.getHeight() / 2 - dialog.getHeight() / 2;
        dialog.setX(x);
        dialog.setY(y);
    }
    
    public static void setPrimaryStage(Stage primaryStage) {
        if(DCCUtils.primaryStage==null) {
            DCCUtils.primaryStage = primaryStage;
        }
        else {
            throw new IllegalArgumentException("Primary stage already set");
        }
    }

}
