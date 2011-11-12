/*
 * @(#)ConResAnalyzerPlugInA1.java   11/11/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.Grasppe.ConRes.Analyzer.IJ;

import com.Grasppe.Common.StopWatch;
import com.Grasppe.GrasppeKit;
import com.Grasppe.GrasppeKit.AbstractController;
import com.Grasppe.GrasppeKit.AbstractModel;
import com.Grasppe.GrasppeKit.AbstractView;
import com.Grasppe.GrasppeKit.KeyCode;
import com.Grasppe.GrasppeKit.KeyEventID;

import ij.IJ;
import ij.ImagePlus;

import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.Overlay;
import ij.gui.PointRoi;

import ij.io.Opener;

import ij.plugin.PlugIn;

import ij.util.Java2;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 *
 */

/**
 * @author daflair
 *
 */
public class ConResAnalyzerPlugInA1 implements PlugIn {

    protected static String			name      = ConResAnalyzerPlugInA1.class.getSimpleName();
    protected static int			exitDelay = 10 * 1000;
    protected static ConResAnalyzer	analyzer;

    /**
     * Method description
     *
     * @param pointROI
     *
     * @return
     */
    public static PointRoi calculateFourthVertex(PointRoi pointROI) {
        if (pointROI.getNCoordinates() != 3) return pointROI;

        // We have three points in the clicking order!

        // TODO: determine the boundary of the three points
        Polygon	roiPolygon = pointROI.getConvexHull();

        // TODO: determine point opposite hypotenuse, and the longest x offset
        // and y offset for the other two sides.
        double	longestDistance = 0,
				xDistance       = 0,
				yDistance       = 0,
				xOffset         = 0,
				yOffset         = 0;
        int		rPoint          = 0,
				xPoint          = 0,
				yPoint          = 0;

        for (int i = 0; i < 3; i++) {
            Point	pointA = new Point(roiPolygon.xpoints[(i + 1) % 2],
                                     roiPolygon.ypoints[(i + 1) % 2]);
            Point	pointB = new Point(roiPolygon.xpoints[(i + 2) % 2],
                                     roiPolygon.ypoints[(i + 2) % 2]);
            double	deltaX   = pointA.x - pointB.x;
            double	deltaY   = pointA.y - pointB.y;
            double	distance = pointA.distance(pointB);
            
            GrasppeKit.debugText("Vertex Iteration", "\t(i:"+i+")\t" + GrasppeKit.lastSplit(pointA.toString()) + "\t"
            		+ GrasppeKit.lastSplit(pointB.toString()),3);

            if (distance > longestDistance) {
                rPoint          = i;
                longestDistance = distance;
            } else {

                // TODO: offset direction?
//                if (Math.abs(deltaX) > xDistance) xOffset = deltaX;
//                if (Math.abs(deltaY) > yDistance) yOffset = deltaY;
//                xDistance = Math.max(Math.abs(deltaX), xDistance);
//                yDistance = Math.max(Math.abs(deltaY), yDistance);
            }
        }

        // TODO: offset reference point by xOffset and yOffset
        Point	referencePoint = new Point(roiPolygon.xpoints[rPoint], roiPolygon.xpoints[rPoint]);
        Point	lastPoint      = new Point(referencePoint);

        //lastPoint.setLocation(x, y)

        // TODO: added last point to polygon
        roiPolygon.addPoint(lastPoint.x, lastPoint.y);

        // TODO: create the new roi from polygon
        PointRoi	newROI = new PointRoi(roiPolygon);

        return pointROI;
    }

    /**
     * @deprecated  Java exits on last window anyway!
     */
    @Deprecated
    public static void delayedExit() {

        GrasppeKit.debugText("Delayed Exit (" + name + ")",
                             "Exiting if no windows open in the next " + (exitDelay / 1000)
                             + " seconds!", 2);

        int				delay         = exitDelay;		// milliseconds
        ActionListener	taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Frame[]	frames = Frame.getFrames();

                for (Frame frame : frames)
                    if (frame.isVisible()) return;		// visibleFrames++;

                System.exit(0);
            }
        };

        new Timer(delay, taskPerformer).start();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        new ConResAnalyzerPlugInA1().run("");

    }

    /**
     * @param x
     * @param y
     */
    public static void moveFrame(int x, int y) {
        updateMagnifier();

        if (Testing.zoomWindow == null) return;

        JFrame		zoomWindow  = Testing.zoomWindow;
        ImageWindow	imageWindow = Testing.imageWindow;

        Testing.isMouseOverImage = imageWindow.getCanvas().getBounds().contains(x
                - imageWindow.getX(), y - imageWindow.getY());

        // Sets visible and returns updated zoomWindow.isVisible()

        zoomWindow.setLocation(x - zoomWindow.getWidth() / 2, y - zoomWindow.getHeight() / 2);

        if (!Testing.isShowZoom()) return;
        redrawFrame();
    }

    /**
     * Method description
     */
    public static void prepareFrame() {
        Testing.zoomWindow = new JFrame();		// "SpringLayout");
        Testing.zoomWindow.setUndecorated(true);
        Testing.zoomWindow.setSize(300, 300);
        Testing.zoomWindow.setMaximumSize(Testing.zoomWindow.getSize());
        Testing.zoomWindow.setAlwaysOnTop(true);
        Testing.zoomWindow.setFocusableWindowState(false);
        Testing.zoomWindow.setResizable(false);

        Testing.zoomWindow.setBackground(Color.black);

        MagnifierCanvas	zoomCanvas = new MagnifierCanvas(Testing.imageWindow.getImagePlus());

        zoomCanvas.addMouseListener(TestingListeners.IJMouseListener);
        zoomCanvas.addMouseMotionListener(TestingListeners.IJMotionListener);
        zoomCanvas.addMouseWheelListener(TestingListeners.IJWheelListener);
        zoomCanvas.setBackground(Color.black);

        // Testing.imageWindow.getCanvas().zoomIn(0, 0);

        Container	contentPane = Testing.zoomWindow.getContentPane();

        contentPane.setBackground(Color.BLACK);

        /** {@link http://www.java2s.com/Tutorial/Java/0240__Swing/SpringLayout.htm} */

        // SpringLayout layout = new SpringLayout();
        // contentPane.setLayout(layout);

        contentPane.add(zoomCanvas);

        int	padding = 0;

        GrasppeKit.debugText("Zoom Frame", "Content Pane " + contentPane.getBounds().toString(), 3);
        GrasppeKit.debugText("Zoom Frame", "Frame " + Testing.zoomWindow.getBounds().toString(), 3);
        GrasppeKit.debugText("Zoom Frame", "Zoom Canvas " + zoomCanvas.getBounds().toString(), 3);

//      layout.putConstraint(SpringLayout.NORTH, zoomCanvas, padding, SpringLayout.NORTH, contentPane);
//      layout.putConstraint(SpringLayout.EAST, zoomCanvas, padding, SpringLayout.EAST, contentPane);
//      layout.putConstraint(SpringLayout.SOUTH, zoomCanvas, padding, SpringLayout.SOUTH, contentPane);
//        layout.putConstraint(SpringLayout.WEST, zoomCanvas, padding, SpringLayout.WEST, contentPane);

        // zoomCanvas.setSize(contentPane.getWidth() - 2*padding, contentPane.getHeight() - 2*padding);
        // zoomCanvas.setLocation(0, 0);

        double	windowZoom = Testing.imageWindow.getCanvas().getMagnification();

        zoomCanvas.setMagnification(windowZoom * 2.0);

        Testing.zoomWindow.setVisible(Testing.isShowZoom());

        Testing.zoomWindow.pack();
        Testing.zoomWindow.setSize(300, 300);

        Testing.zoomCanvas = zoomCanvas;

    }

    /**
     * Method description
     */
    public static void redrawFrame() {
        if (Testing.zoomWindow == null) return;

        if (!Testing.zoomWindow.isVisible()) return;	// {Testing.zoomWindow.setVisible(false); return;}

        ImageWindow		imageWindow  = Testing.imageWindow;
        ImageCanvas		imageCanvas  = imageWindow.getCanvas();
        JFrame			zoomWindow   = Testing.zoomWindow;
        MagnifierCanvas	zoomCanvas   = Testing.zoomCanvas;

        Point			zoomWindowLocation,
						zoomLocation = null;

        if (zoomWindow.isVisible()) {
            zoomWindowLocation = zoomWindow.getLocationOnScreen();		// Location on screen
            zoomLocation       = zoomCanvas.getLocationOnScreen();		// Location on screen
        }

        Point		zoomPosition        = zoomCanvas.getLocation();		// Position within component
        Dimension	zoomSize            = zoomCanvas.getSize();
        Double		zoomScale           = zoomCanvas.getMagnification();

        Point		imageWindowLocation = imageWindow.getLocationOnScreen();
        Point		imageLocation       = imageCanvas.getLocationOnScreen();
        Point		imagePosition       = imageCanvas.getLocation();
        Dimension	imageSize           = imageCanvas.getSize();
        Double		imageScale          = imageCanvas.getMagnification();

        Point		pointerCenter;

        try {

            /* Pointer Center: pointer location in image canvas in screen units */
            if (zoomWindow.isVisible()) {
                pointerCenter = zoomCanvas.getMousePosition();
                pointerCenter = new Point(zoomLocation.x - imageLocation.x + pointerCenter.x,
                                          zoomLocation.y - imageLocation.y + pointerCenter.y);
            } else
                pointerCenter = imageCanvas.getMousePosition();
        } catch (Exception exception) {
            zoomWindow.setVisible(false);
            pointerCenter = null;
        }

        if (pointerCenter == null) return;

        // if (!zoomWindow.isVisible()) zoomWindow.setVisible(true);

        /* Source Center: pointer location in source coordinate space in source units */
        Point	sourceCenter = new Point((int)(pointerCenter.x / imageScale),
                                       (int)(pointerCenter.y / imageScale));

        /* Source Size: source rectangle size in source units */
        Dimension	sourceSize = new Dimension((int)(zoomSize.width / zoomScale),
                                   (int)(zoomSize.height / zoomScale));

        /* Source Corner: source rectangle top-left in source units */
        Point	sourceCorner = new Point((int)(sourceCenter.x - sourceSize.width / 2),
                                       (int)(sourceCenter.y - sourceSize.height / 2));

        /* Source Rectangle: source rectangle in source units */
        Rectangle	sourceRectangle = new Rectangle(sourceCorner, sourceSize);

        String		debugString     = "";

        debugString += "\t" + GrasppeKit.lastSplit(pointerCenter.toString());
        debugString += "\t" + GrasppeKit.lastSplit(zoomSize.toString());
        debugString += "\t" + GrasppeKit.lastSplit(sourceRectangle.toString());

        GrasppeKit.debugText("Magnifier", debugString, 4);

        Testing.zoomCanvas.setSourceRect(sourceRectangle);

        Testing.zoomCanvas.repaint(0, 0, zoomSize.width, zoomSize.height);

    }

    /**
     * Method description
     *
     * @param arg
     */
    public void run(String arg) {
        IJ.showMessage(name, "Hello world!");

        // Testing.

        String[]	imageNames = { "CirRe27U_50t.png", "CirRe27U_50i.tif" };

        Testing.imageName = imageNames[0];
        Testing.keyboardFocusManager.addKeyEventDispatcher(Testing.keyEventDispatch);

        GrasppeKit.debugLevel = 3;

        // testAnalyzerMenu();
        testImageWindow();
        testImageJMouseListeners();
        testMagnifier();

    }

//  JFrame    Testing.jFrame = new JFrame();
//
//  Testing.jFrame.setSize(500, 500);
//  Testing.jFrame.setVisible(true);
//  JComponent
//  GestureUtilities.registerListener(Testing.jFrame, new GesturePhaseListener() {
//
//      @Override
//      public void gestureEnded(GesturePhaseEvent e) {
//          String    eventLabel     = "Phase Ended";
//          Point cursorLocation = Testing.imageWindow.getCanvas().getCursorLoc();
//
//          GrasppeKit.debugText("Image Window Gesture Event",
//                               "Gesture " + eventLabel + "\t" + e.toString(), 3);
//      }
//      @Override
//      public void gestureBegan(GesturePhaseEvent e) {
//          String    eventLabel     = "Phase Began";
//          Point cursorLocation = Testing.imageWindow.getCanvas().getCursorLoc();
//
//          GrasppeKit.debugText("Image Window Gesture Event",
//                               "Gesture " + eventLabel + "\t" + e.toString(), 3);
//      }
//
//  });
    // Testing.imageWindow.getCanvas().addMouseListener();
    // Testing.imageWindow.add
    // Macro macro = new Macro();
    // Macro_Runner macroRunner = new MacroRunner();
    // analyzerView.show();
    // analyzerView.close();

    /**
     * Method description
     */
    public void testAnalyzerMenu() {
        if (analyzer == null) analyzer = new ConResAnalyzer();

        /* Static Variables */

        /* Static Members */
        WindowListener	windowListener = TestingListeners.WindowEventListener;

        /* Local Variables */
        ConResAnalyzerModel	analyzerModel = analyzer.getModel();
        ConResAnalyzerView	analyzerView  = new ConResAnalyzerView(analyzer);

        /* Test Statements */
        analyzerView.prepareView(windowListener);

        /* Static Updates */

    }

    /**
     * Method description
     */
    public void testImageJMouseListeners() {

        /* Static Variables */

        /* Static Members */
        MouseMotionListener	motionListener = TestingListeners.IJMotionListener;
        MouseWheelListener	wheelListener  = TestingListeners.IJWheelListener;
        MouseListener		mouseListener  = TestingListeners.IJMouseListener;
        ImageWindow			imageWindow    = Testing.imageWindow;

        /* Local Variables */

        /* Test Statements */
        imageWindow.getCanvas().addMouseListener(mouseListener);
        imageWindow.getCanvas().addMouseMotionListener(motionListener);
        imageWindow.getCanvas().addMouseWheelListener(wheelListener);

        /* Static Updates */

    }

    /**
     * Opens an ImagePlus image using Opener and creates and displays it in an ImageWindow;
     */
    public void testImageWindow() {

        /* Static Variables */

        /* Static Members */
        ImageWindow		imageWindow    = Testing.imageWindow;
        WindowListener	windowListener = TestingListeners.WindowEventListener;

        /* Local Variables */
        Opener		opener;
        ImagePlus	imagePlus;

        /* Test Statements */

        Testing.startTimer();

        opener    = new Opener();
        imagePlus = opener.openImage(Testing.getInputPath());		// .openURL(inputURL);
        Testing.checkTimer("Opened ImagePlus " + imagePlus.getTitle());

//      imagePlus.getProcessor().autoThreshold();     // 128);
//      Testing.checkTimer("Auto Threshold " + imagePlus.getTitle());

        Testing.imageWindow = new ImageWindow(imagePlus);		// Initialize static variable here
        imageWindow         = Testing.imageWindow;
        Testing.checkTimer("Created ImageWindow " + imagePlus.getTitle());

        // imageWindow.

        // imageWindow.getCanvas().zoom100Percent();
        // Testing.checkTimer("Zoomed " + imagePlus.getTitle());

        Testing.imageWindow.getCanvas().fitToWindow();

        imageWindow.setVisible(true);
        Testing.checkTimer("ImageWindow Displayed " + imagePlus.getTitle());

        imageWindow.addWindowListener(windowListener);

        /* Static Updates */

    }

    /**
     * Method description
     */
    public void testMagnifier() {
        prepareFrame();

        /* Static Variables */

        /* Static Members */
        JFrame				frame          = Testing.zoomWindow;
        ImageWindow			imageWindow    = Testing.imageWindow;
        MouseMotionListener	motionListener = TestingListeners.IJMotionListener;

        /* Local Variables */

        /* Test Statements */
        frame.addMouseMotionListener(motionListener);
        imageWindow.getCanvas().addMouseMotionListener(motionListener);

        /* Static Updates */
    }

    /**
     * Method description
     */
    public void testProgessBar() {

        /* Static Variables */

        /* Static Members */

        /* Local Variables */

        /* Test Statements */

        /* Static Updates */

    }

    /**
     * Method description
     */
    public void testVertexSelectionTool() {

        // testMagnifier()

        /* when click, mark ImageJ roi */

    }

    /**
     * Method description
     */
    public static void updateMagnification() {

        // boolean keyPressed = MyDispatcher.isPressed({KeyCode.VK_ALT,KeyCode.VK_EQUALS});
        boolean	plusKey = MyDispatcher.isPressed(new KeyCode[] { KeyCode.VK_ALT,
                              KeyCode.VK_EQUALS });
        boolean	minusKey = MyDispatcher.isPressed(new KeyCode[] { KeyCode.VK_ALT,
                               KeyCode.VK_MINUS });

        MagnifierCanvas	zoomCanvas   = Testing.zoomCanvas;
        double			currentScale = zoomCanvas.getMagnification();
        double			maxScale     = 1.0;
        double			minScale     = Testing.imageWindow.getCanvas().getMagnification();

        if (plusKey) zoomCanvas.setMagnification(Math.min(currentScale * 1.25, maxScale));

        if (minusKey) zoomCanvas.setMagnification(Math.max(currentScale * 0.95, minScale));
    }

    /**
     * Method description
     */
    public static void updateMagnifier() {

        if (Testing.zoomWindow == null) return;

        boolean	keyPressed = MyDispatcher.isDown(KeyCode.VK_ALT);
        boolean	mouseOver  = Testing.isMouseOverImage;

        Testing.setShowZoom(keyPressed && mouseOver);

    }

    /**
     * Method description
     *
     * @param e
     */
    public static void updateROI(MouseEvent e) {
        if (e.isConsumed()) return;
        if (Testing.imageWindow == null) return;
        if (!Testing.imageWindow.isVisible()) return;

        // TODO: Add get mouse position relative to canvas
        Point	mousePosition = Testing.imageWindow.getCanvas().getMousePosition();

        if (mousePosition == null) return;

        // TODO: Determine whether to add point (click) or clear points (triple click)
        int	clickCount = e.getClickCount();

        if (clickCount == 3) Testing.pointROI = null;

        if ((clickCount == 1) && (Testing.pointROI == null))
            Testing.pointROI = new PointRoi(mousePosition.x, mousePosition.y,
                Testing.imageWindow.getImagePlus());
        else if ((clickCount == 1) && (Testing.pointROI != null))
                 Testing.pointROI = Testing.pointROI.addPoint(mousePosition.x, mousePosition.y);

        // TODO: When 0 points are defined, clear overlay
        if ((Testing.pointROI == null) || (Testing.pointROI.getNCoordinates() == 0)) {
            Testing.imageWindow.getImagePlus().setOverlay(new Overlay());
            return;
        }
        
        String debugStrings1 = debugPoints(Testing.pointROI);

        // TODO: When 3 points are defined, calculate the fourth
        if (Testing.pointROI.getNCoordinates() == 3)
        	Testing.pointROI = calculateFourthVertex(Testing.pointROI);

        // TODO: When 4 points are defined! We are done.

        
        // TODO: Finally, update overlay with defined points        
        String debugStrings2 = debugPoints(Testing.pointROI);        
        Overlay	overlay = new Overlay(Testing.pointROI);
        overlay.drawNames(true);
        overlay.drawLabels(true);
        Testing.imageWindow.getImagePlus().setOverlay(overlay);
        
        GrasppeKit.debugText("Vertex Selection", GrasppeKit.cat(new String[]{debugStrings1, debugStrings2},"\n"),3);
    }
    
    public static String debugPoints(PointRoi pointROI) {
        int pointCount = 0;
        
        pointCount = pointROI.getNCoordinates();
        String strPointCount = "Points: " + pointCount;
        String strPoints = "";

        for (int i = 0; i < pointCount; i++)
        	strPoints+="P" + i + ": (" + pointROI.getXCoordinates()[i] + ", " + pointROI.getYCoordinates()[i] + ")\t";
        
        return (strPointCount + "\n" + strPoints);
    }

    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/11
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public static class MagnifierCanvas extends ImageCanvas {

        /**
         * Constructs ...
         *
         * @param imp
         */
        public MagnifierCanvas(ImagePlus imp) {
            super(imp);
        }

        /**
         * Method description
         *
         * @param g
         */
        public void paint(Graphics g) {
            try {
                Java2.setBilinearInterpolation(g, true);
                Java2.setAntialiased(g, true);

                Image	img = imp.getImage();

                if (img != null) g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                g.drawImage(img, 0, 0, (int)(srcRect.width * magnification),
                            (int)(srcRect.height * magnification), srcRect.x, srcRect.y,
                            srcRect.x + srcRect.width, srcRect.y + srcRect.height, null);

            } catch (OutOfMemoryError e) {
                IJ.outOfMemory("Paint");
            }
        }

        /**
         * Method description
         *
         * @param x
         * @param y
         */
        public void setRect(int x, int y) {

            double	newMag = 1;

            int		sx     = super.screenX(x);
            int		sy     = super.screenY(y);

            /* super.adjustSourceRect(1.0, sx, sy); */

            // IJ.log("adjustSourceRect1: "+newMag+" "+dstWidth+"  "+dstHeight);
            int	w = (int)Math.round(dstWidth / newMag);

            if (w * newMag < dstWidth) w++;

            int	h = (int)Math.round(dstHeight / newMag);

            if (h * newMag < dstHeight) h++;
            x = offScreenX(x);
            y = offScreenY(y);

            Rectangle	r = new Rectangle(x - w / 2, y - h / 2, w, h);

            if (r.x < 0) r.x = 0;
            if (r.y < 0) r.y = 0;
            if (r.x + w > imageWidth) r.x = imageWidth - w;
            if (r.y + h > imageHeight) r.y = imageHeight - h;
            srcRect = r;

            repaint();

        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/09
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    private static class MyDispatcher implements KeyEventDispatcher {

        /** Field description */
        public static HashSet<KeyCode>	pressedKeys = new HashSet<KeyCode>();

        /** Field description */
        public static boolean	newCombination = true;

        /** Field description */
        public static boolean	consumedCombination = false;

        /**
         * Method description
         *
         * @param e
         *
         * @return
         */
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {

            // TODO: consume key combinations;
//          if (pressedKeys.isEmpty()) consumedCombination = false;
            processKey(e);

//          if (!consumedCombination && pressedKeys.isEmpty()) {
            if (!pressedKeys.isEmpty()) {
                updateMagnifier();
                updateMagnification();
                redrawFrame();
                GrasppeKit.debugText("Key Event",
                                     GrasppeKit.keyEventString(e) + " (PressedKeys "
                                     + pressedKeyString() + ")", 3);
            }

            return false;
        }

        /**
         * Method description
         *
         * @return
         */
        public static String pressedKeyString() {
            if (pressedKeys.isEmpty()) return "";

            String[]	pressedKeyArray = new String[pressedKeys.size()];

            int			i               = 0;

            for (KeyCode keyCode : pressedKeys) {
                pressedKeyArray[i] = keyCode.toString();
                i                  += 1;
            }

            return GrasppeKit.cat((String[])pressedKeyArray, "+");
        }

        /**
         * Method description
         *
         * @param e
         */
        public static void processKey(KeyEvent e) {
            KeyEventID	eventID = KeyEventID.get(e.getID());

            try {
                switch (eventID) {

                case PRESSED :
                    pressedKeys.add(KeyCode.get(e.getKeyCode()));
                    break;

                case RELEASED :
                    pressedKeys.remove(KeyCode.get(e.getKeyCode()));
                    break;
                }
            } catch (Exception exception) {}

        }

        /**
         * Method description
         *
         * @param keyCode
         *
         * @return
         */
        public static boolean isDown(KeyCode keyCode) {
            return pressedKeys.contains(keyCode);
        }

        /**
         * Method description
         *
         * @param keyCode
         *
         * @return
         */
        public static boolean isPressed(KeyCode keyCode) {
            if (pressedKeys.isEmpty()) return false;

            if (pressedKeys.contains(keyCode) && (pressedKeys.size() == 1)) {
                GrasppeKit.debugText("Key Combination Pressed", pressedKeyString(), 3);

                // pressedKeys.clear();
//              consumedCombination = true;
                return true;
            }

            return false;
        }

        /**
         * Method description
         *
         * @param keyCodes
         *
         * @return
         */
        public static boolean isPressed(KeyCode[] keyCodes) {
            if (pressedKeys.isEmpty()) return false;

            if (Arrays.asList(keyCodes).containsAll(pressedKeys)) {
                GrasppeKit.debugText("Key Combination Pressed", pressedKeyString(), 3);

                // consumedCombination = true;
                return true;
            }

            return false;
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class TargetManager extends AbstractController {

        /**
         * Constructs and attaches a new controller and a new model.
         */
        public TargetManager() {
            this(new TargetManagerModel());
        }

        /**
         * Constructs a new controller and attaches it to the unattached model.
         *
         * @param model
         */
        public TargetManager(TargetManagerModel model) {
            GrasppeKit.getInstance().super(model);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class TargetManagerModel extends AbstractModel {

        /**
         * Constructs a new model object with no predefined controller.
         */
        public TargetManagerModel() {
            GrasppeKit.getInstance().super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public TargetManagerModel(TargetManager controller) {
            GrasppeKit.getInstance().super(controller);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class TargetManagerView extends AbstractView {

        /**
         * Constructs ...
         *
         * @param controller
         */
        public TargetManagerView(AbstractController controller) {
            GrasppeKit.getInstance().super(controller);

            // TODO Auto-generated constructor stub
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/11
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public static class Testing {

        /** Field description */
        public static KeyboardFocusManager	keyboardFocusManager =
            KeyboardFocusManager.getCurrentKeyboardFocusManager();

        /** Field description */
        public static MyDispatcher	keyEventDispatch = new MyDispatcher();

        /** Field description */
        public static boolean	isMouseOverImage = false;
        static ImageWindow		imageWindow;
        static JFrame			zoomWindow;
        static MagnifierCanvas	zoomCanvas;
        static PointRoi			pointROI;
        static StopWatch		timer      = new StopWatch();
        static String			rootFolder = "/Users/daflair/Documents/MATLAB/ConResAlpha/data/samples";
        static String			caseFolder = "Approval_Scans_ConRes26_FS";
        static boolean			showZoom   = false;
        static String			imageName  = "CirRe27U_50i.tif";	// "CirRe27U_50t.png";
        static String			inputPath  = getInputPath();

        /**
         * Constructs ...
         */
        private Testing() {}

        /**
         * Outputs elapsed time with generic description without stopping or reseting the timer.
         * @param description
         */
        static void checkTimer(String description) {
            GrasppeKit.debugText("Elapsed Time",
                                 description + " in " + timer.getElapsedTime() + " ms.", 3);
        }

        /**
         * Outputs elapsed time with generic description and resets timer.
         */
        static void markTimer() {
            markTimer("Task finished in");
        }

        /**
         * Outputs elapsed time with specified description and resets timer.
         * @param description
         */
        static void markTimer(String description) {
            checkTimer(description);

//          GrasppeKit.debugText("Elapsed Time",
//                               description + " in " + timer.getElapsedTime() + " ms.", 3);
            startTimer();
        }

        /**
         * Starts timer.
         */
        static void startTimer() {
            timer.start();
        }

        /**
         * @return the caseFolder
         */
        public static String getCaseFolder() {
            return caseFolder;
        }

        /**
         * @return the imageName
         */
        public static String getImageName() {
            return imageName;
        }

        /**
         * Method description
         *
         * @return
         */
        public static String getInputPath() {
            return (getRootFolder() + "/" + getCaseFolder() + "/"
                    + getImageName()).replaceAll("//", "/");
        }

        /**
         * Method description
         *
         * @return
         */
        public static KeyEventDispatcher getKeyEventDispatch() {
            return keyEventDispatch;
        }

        /**
         * @return the rootFolder
         */
        public static String getRootFolder() {
            return rootFolder;
        }

        /**
         * @return the showZoom
         */
        public static boolean isShowZoom() {
            return showZoom;
        }

        /**
         * @param caseFolder the caseFolder to set
         */
        public static void setCaseFolder(String caseFolder) {
            Testing.caseFolder = caseFolder;
        }

        /**
         * @param imageName the imageName to set
         */
        public static void setImageName(String imageName) {
            Testing.imageName = imageName;
        }

        /**
         * @param rootFolder the rootFolder to set
         */
        public static void setRootFolder(String rootFolder) {
            Testing.rootFolder = rootFolder;
        }

        /**
         * @param showZoom the showZoom to set
         */
        public static void setShowZoom(boolean showZoom) {
            Testing.zoomWindow.setVisible(showZoom);
            Testing.showZoom = showZoom;
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/11
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public static class TestingListeners {

        static int	debugginLevel = 5;

        /** Field description */
        public static MouseListener	IJMouseListener = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                debugEvent("IJMouseListener", e);
                updateROI(e);
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
                redrawFrame();
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
                moveFrame(e.getXOnScreen(), e.getYOnScreen());
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
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Activated (" + name + ")", e.toString());
            }
            @Override
            public void windowClosed(WindowEvent e) {
                Frame[]	frames        = Frame.getFrames();

                int		visibleFrames = 0;

                for (Frame frame : frames)
                    if (frame.isVisible()) visibleFrames++;

                debugEvent("Window", e);	// GrasppeKit.debugText("Window Closed (" + name + ")", e.toString());
                if ((visibleFrames == 1) && Testing.zoomWindow.isVisible()) delayedExit();
                if (visibleFrames == 0) delayedExit();
            }
            @Override
            public void windowClosing(WindowEvent e) {
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Closing (" + name + ")", e.toString());
            }
            @Override
            public void windowDeactivated(WindowEvent e) {
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Deactivated (" + name + ")", e.toString());
            }
            @Override
            public void windowDeiconified(WindowEvent e) {
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Deiconified (" + name + ")", e.toString());
            }
            @Override
            public void windowIconified(WindowEvent e) {
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Iconified (" + name + ")", e.toString());
            }
            @Override
            public void windowOpened(WindowEvent e) {
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Opened (" + name + ")", e.toString());
            }
        };

        /**
         * Outputs debug information and event details
         * @param grouping
         * @param e
         */
        static void debugEvent(String grouping, MouseEvent e) {
            debugEvent(GrasppeKit.getCaller().methodName, grouping, e, debugginLevel);
        }

        /**
         * Outputs debug information and event details
         * @param grouping
         * @param e
         */
        static void debugEvent(String grouping, WindowEvent e) {
            debugEvent(GrasppeKit.getCaller().methodName, grouping, e, debugginLevel);
        }

        /**
         * Outputs debug information and event details
         * @param label
         * @param grouping
         * @param e
         * @param level
         */
        static void debugEvent(String label, String grouping, MouseEvent e, int level) {
            String	cursorString = "";

            try {
                Point	cursorLocation = Testing.imageWindow.getCanvas().getCursorLoc();

                cursorString = "\t" + cursorLocation.toString();
            } catch (Exception exception) {}

            GrasppeKit.debugText((grouping + " Event").trim(), "Mouse " + label + cursorString,
                                 level);
        }

        /**
         * Outputs debug information and event details
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
}
