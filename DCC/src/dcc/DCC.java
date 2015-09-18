/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcc;

import java.util.Iterator;
import java.util.Set;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Michael
 */
public class DCC extends Application {

    private LocoController controller;

    @Override
    public void start(Stage primaryStage) {
        DCCUtils.setPrimaryStage(primaryStage);
        controller = new LocoController();
        LocoConsolePanel lcp = new LocoConsolePanel(controller);

        ScrollPane scroll = new ScrollPane(lcp);
        scroll.setPannable(true);
        scroll.setFitToHeight(true);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.getStyleClass().add("mainscroll");
        Scene scene = new Scene(scroll, 1000, 800);

        lcp.setOnScroll((ScrollEvent event) -> {
            Set<Node> sbSet = scroll.lookupAll(".scroll-bar");
            Iterator<Node> iter = sbSet.iterator();
            ScrollBar scrollBar = null;
            while (iter.hasNext() && scrollBar == null) {
                Node sb = iter.next();
                if (sb instanceof ScrollBar && ((ScrollBar) sb).getOrientation() == Orientation.HORIZONTAL) {
                    scrollBar = (ScrollBar) sb;
                }
            }
            if (scrollBar != null) {
                if (event.getDeltaY() < 0) {
                    scrollBar.increment();
                } else {
                    scrollBar.decrement();
                }
            }
        });
        scene.getStylesheets().add(DCCUtils.STYLESHEET);
        primaryStage.setScene(scene);
        primaryStage.setTitle("AB-DCC");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
