package com.grasppe.conres.alpha;

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.grasppe.lure.framework.GrasppeKit;

/**
 * Class description
 *
 * @version $Revision: 1.0, 11/11/11
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class TestingListeners {

    static int	debugginLevel = 5;

    /** Field description */
    public static MouseListener	IJMouseListener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
            debugEvent("IJMouseListener", e);
            ConResBootCamp.updateROI(e);
            e.consume();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            debugEvent("IJMouseListener", e);
            e.consume();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            debugEvent("IJMouseListener", e);
            ConResBootCamp.redrawFrame();
            e.consume();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            debugEvent("IJMouseListener", e);
            e.consume();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            debugEvent("IJMouseListener", e);
            e.consume();
        }
    };

    /** Field description */
    public static MouseWheelListener	IJWheelListener = new MouseWheelListener() {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            debugEvent("IJWheelListener", e);
            e.consume();
        }
    };

    // Testing.imageWindow.getCanvas().addMouseMotionListener(

    /** Field description */
    public static MouseMotionListener	IJMotionListener = new MouseMotionListener() {

        @Override
        public void mouseMoved(MouseEvent e) {

            // e.getSource().getClass().equals(ImageCanvas.class);
        	//if (!ConResBootCamp.canMagnifyPatch())
        		ConResBootCamp.moveFrame(e.getXOnScreen(), e.getYOnScreen());
            debugEvent("IJMotionListener", e);
            e.consume();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            debugEvent("IJMotionListener", e);
            e.consume();
        }
    };

    /** Field description */
    public static WindowListener	WindowEventListener = new WindowListener() {

        @Override
        public void windowActivated(WindowEvent e) {
            debugEvent("Window", e);	// GrasppeKit.debugText("Window Activated ("

            // + name + ")", e.toString());
        }

        @Override
        public void windowClosed(WindowEvent e) {
            Frame[]	frames        = Frame.getFrames();

            int		visibleFrames = 0;

            for (Frame frame : frames)
                if (frame.isVisible()) visibleFrames++;

            debugEvent("Window", e);	// GrasppeKit.debugText("Window Closed ("

            // + name + ")", e.toString());
            if ((visibleFrames == 1) && ConResBootCamp.Testing.getZoomWindow().isVisible()) ConResBootCamp.delayedExit();
            if (visibleFrames == 0) ConResBootCamp.delayedExit();
        }

        @Override
        public void windowClosing(WindowEvent e) {
            debugEvent("Window", e);	// GrasppeKit.debugText("Window Closing ("

            // + name + ")", e.toString());
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            debugEvent("Window", e);	// GrasppeKit.debugText("Window Deactivated ("

            // + name + ")", e.toString());
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            debugEvent("Window", e);	// GrasppeKit.debugText("Window Deiconified ("

            // + name + ")", e.toString());
        }

        @Override
        public void windowIconified(WindowEvent e) {
            debugEvent("Window", e);	// GrasppeKit.debugText("Window Iconified ("

            // + name + ")", e.toString());
        }

        @Override
        public void windowOpened(WindowEvent e) {
            debugEvent("Window", e);	// GrasppeKit.debugText("Window Opened ("

            // + name + ")", e.toString());
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
            Point	cursorLocation = ConResBootCamp.Testing.getImageWindow().getCanvas().getCursorLoc();

            cursorString = "\t" + cursorLocation.toString();
        } catch (Exception exception) {}

        GrasppeKit.debugText((grouping + " Event").trim(), "Mouse " + label + cursorString,
                             level);
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

        GrasppeKit.debugText((grouping + " Event").trim(), "Mouse " + label + testString,
                             level);
    }
}