/*
 * @(#)CornerSelectorListeners.java   11/11/26
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.imagej;

import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * Class description
 *
 * @version $Revision: 1.0, 11/11/11
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CornerSelectorListeners {

    static int	debugginLevel = 5;

//    /** Field description */
//    public static MouseListener	IJMouseListener = new MouseListener() {
//
//        public void mouseClicked(MouseEvent e) {
//            debugEvent("IJMouseListener", e);
//            //CornerSelectorView.updateROI(e);
//            e.consume();
//        }
//
//        public void mouseEntered(MouseEvent e) {
//            debugEvent("IJMouseListener", e);
//            e.consume();
//        }
//
//        public void mouseExited(MouseEvent e) {
//            debugEvent("IJMouseListener", e);
//            //CornerSelectorView.redrawFrame();
//            e.consume();
//        }
//
//        public void mousePressed(MouseEvent e) {
//            debugEvent("IJMouseListener", e);
//            e.consume();
//        }
//
//        public void mouseReleased(MouseEvent e) {
//            debugEvent("IJMouseListener", e);
//            e.consume();
//        }
//    };
//
//    /** Field description */
//    public static MouseWheelListener	IJWheelListener = new MouseWheelListener() {
//
//        public void mouseWheelMoved(MouseWheelEvent e) {
//            debugEvent("IJWheelListener", e);
//            e.consume();
//        }
//    };
//
//    /** Field description */
//    public static MouseMotionListener	IJMotionListener = new MouseMotionListener() {
//
//        public void mouseMoved(MouseEvent e) {
//            CornerSelectorView.moveFrame(e.getXOnScreen(), e.getYOnScreen());
//            debugEvent("IJMotionListener", e);
//            e.consume();
//        }
//
//        public void mouseDragged(MouseEvent e) {
//            debugEvent("IJMotionListener", e);
//            e.consume();
//        }
//    };

    /** Field description */
    public static WindowListener	WindowEventListener = new WindowListener() {

        public void windowActivated(WindowEvent e) {
            debugEvent("Window", e);
        }

        public void windowClosed(WindowEvent e) {
        	notify();
            Frame[]	frames        = Frame.getFrames();
            int		visibleFrames = 0;

            for (Frame frame : frames)
                if (frame.isVisible()) visibleFrames++;

            debugEvent("Window", e);

            JFrame	zoomFrame = CornerSelectorView.CornerSelectorCommons.getZoomWindow();

            if ((visibleFrames == 1) && zoomFrame.isVisible()) zoomFrame.setVisible(false);		// CornerSelectorView.delayedExit();
            if (visibleFrames == 0) System.exit(0);		// CornerSelectorView.delayedExit();
        }

        public void windowClosing(WindowEvent e) {
            debugEvent("Window", e);
        }

        public void windowDeactivated(WindowEvent e) {
            debugEvent("Window", e);
        }

        public void windowDeiconified(WindowEvent e) {
            debugEvent("Window", e);
        }

        public void windowIconified(WindowEvent e) {
            debugEvent("Window", e);
        }

        public void windowOpened(WindowEvent e) {
            debugEvent("Window", e);
        }
    };

    /**
     * Outputs debug information and event details
     *
     * @param grouping
     * @param e
     */
    static void debugEvent(String grouping, MouseEvent e) {
        debugEvent(GrasppeKit.getCaller().methodName, grouping, e, debugginLevel);
    }

    /**
     * Outputs debug information and event details
     *
     * @param grouping
     * @param e
     */
    static void debugEvent(String grouping, WindowEvent e) {
        debugEvent(GrasppeKit.getCaller().methodName, grouping, e, debugginLevel);
    }

    /**
     * Outputs debug information and event details
     *
     * @param label
     * @param grouping
     * @param e
     * @param level
     */
    static void debugEvent(String label, String grouping, MouseEvent e, int level) {
        String	cursorString = "";

        try {
            Point	cursorLocation =
                CornerSelectorView.CornerSelectorCommons.getImageWindow().getCanvas()
                    .getCursorLoc();

            cursorString = "\t" + cursorLocation.toString();
        } catch (Exception exception) {}

        GrasppeKit.debugText((grouping + " Event").trim(), "Mouse " + label + cursorString, level);
    }

    /**
     * Outputs debug information and event details
     *
     * @param label
     * @param grouping
     * @param e
     * @param level
     */
    static void debugEvent(String label, String grouping, WindowEvent e, int level) {
        String	testString = "";

        GrasppeKit.debugText((grouping + " Event").trim(), "Mouse " + label + testString, level);
    }
}
