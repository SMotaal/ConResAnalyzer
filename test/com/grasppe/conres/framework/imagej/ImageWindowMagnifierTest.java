/*
 * @(#)ImageWindowMagnifierTest.java   11/11/18
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.imagej;

import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.io.Opener;

import java.awt.event.KeyEvent;

import com.grasppe.lure.framework.GrasppeEventDispatcher;
import com.grasppe.lure.framework.GrasppeEventHandler;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * @author daflair
 */
public class ImageWindowMagnifierTest implements GrasppeEventHandler {

    String	imageName = (new String[] { "CirRe27U_50t.png", "CirRe27U_50i.tif" })[0];
    String	imagePath = "";
//    (Testing.getRootFolder() + "/" + Testing.getCaseFolder() + "/" + imageName).replaceAll("//", "/");
    Opener					opener    = new Opener();
    ImagePlus				imagePlus = opener.openImage(imagePath);
    ImageWindow				imageWindow;	// = .getImageWindow();

    // WindowListener    windowListener ;//= CornerSelectorListeners.WindowEventListener;

    ImageWindowMagnifier	imageMagnifier;

    /**
     */
    public ImageWindowMagnifierTest() {

        // Debug System
//        GrasppeKit.timestampLevel = 5;
//        GrasppeKit.debugLevel     = 3;

        // Event Dispatch
        GrasppeEventDispatcher	eventDispatcher = GrasppeEventDispatcher.getInstance();

        eventDispatcher.attachEventHandler(this);

        // Setup & show Image Window
        imageWindow = new ImageWindow(imagePlus);
        imageWindow.getCanvas().fitToWindow();
        imageWindow.setVisible(true);

        // Setup magnifier
        imageMagnifier = new ImageWindowMagnifier(imageWindow);
        
        imageMagnifier.setVisible(true);
    }

    /**
     * @param e
     * @return
     */
    public boolean dispatchedKeyEvent(KeyEvent e) {
        return false;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        ImageWindowMagnifierTest	imageWindowMagnifierTest = new ImageWindowMagnifierTest();

//      // Setup Frame
//      JFrame jFrame = new JFrame("GestureUtilitiesTest");
//      
//        //Use the content pane's default BorderLayout. No need for
//        //setLayout(new BorderLayout());
//      Container framePane = jFrame.getContentPane();
//      
//      jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     */
    public void update() {
    }
}
