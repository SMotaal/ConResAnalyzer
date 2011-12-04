/*
 * @(#)CornerSelectorView.java   11/11/08
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.imagej;

import com.grasppe.conres.framework.targets.CornerSelector;
import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
import com.grasppe.conres.framework.targets.model.roi.BlockROI;
import com.grasppe.conres.framework.targets.model.roi.PatchSetROI;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeEventDispatcher;
import com.grasppe.lure.framework.GrasppeEventHandler;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.KeyCode;

import ij.ImagePlus;

import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.Overlay;
import ij.gui.PointRoi;

import ij.io.Opener;

import ij.plugin.PlugIn;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.media.jai.WarpPolynomial;

import javax.naming.directory.InvalidAttributesException;

import javax.swing.JFrame;

/**
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */

/**
 * @author daflair
 */
public class CornerSelectorView extends AbstractView
        implements PlugIn, GrasppeEventHandler, MouseListener, MouseWheelListener,
                   MouseMotionListener {

    protected static String			name                  = CornerSelectorView.class.getSimpleName();
    protected CornerSelectorCommons	cornerSelectorCommons = new CornerSelectorCommons();

    /** Field description */
    public BlockROI	blockROI;

    /** Field description */
    public PatchSetROI	patchSetROI;

    /** Field description */
    public double	patchRadius;

    /** Field description */
    public ImageWindow	imageWindow;

    /** Field description */
    public MagnifierCanvas	zoomCanvas;

    /** Field description */
    public JFrame	zoomWindow;

    /** Field description */
    public boolean	showZoom = false;

    /**
     * @param controller
     */
    public CornerSelectorView(CornerSelector controller) {
        super(controller);
    }

    /**
     */
    public void attachMouseListeners() {
        ImageWindow	imageWindow = getImageWindow();

        imageWindow.getCanvas().addMouseListener(this);
        imageWindow.getCanvas().addMouseMotionListener(this);
        imageWindow.getCanvas().addMouseWheelListener(this);

    }

    /**
     */
    public void calculateAffineGrid() {		// PointRoi pointROI, int nR, int nC) {

        try {
            sortROIs(cornerSelectorCommons.overlayROI);
        } catch (InvalidAttributesException exception) {
            GrasppeKit.debugText("CornerSelectorView", exception.getMessage(), 2);
        }

        if (cornerSelectorCommons.sortedROI == null) return;
        warpPatchGrid();

        cornerSelectorCommons.overlayROI = cornerSelectorCommons.patchCenterROI;	// combinedROI; //centerROI;
        cornerSelectorCommons.magnifyPatchIndex = 0;

        Overlay	overlay = new Overlay(cornerSelectorCommons.overlayROI);

        overlay.drawNames(true);
        overlay.drawLabels(true);
        getImageWindow().getImagePlus().setOverlay(overlay);

//      return new PatchSetROI(cXs, cYs, nP, nP);

    }

    /**
     * @param pointROI
     * @return
     */
    public BlockROI calculateFourthVertex(PointRoi pointROI) {
        if (pointROI.getNCoordinates() != 3) return (BlockROI)pointROI;

        // We have three points in the clicking order!

        // TODO: determine the convex boundary of the three points
        Polygon	roiPolygon = pointROI.getConvexHull();

        // TODO: determine point opposite hypotenuse, and the longest x offset
        // and y offset for the other two sides.
        double	longestDistance = 0,
				distance        = 0;

        Point	pointA          = new Point(roiPolygon.xpoints[0], roiPolygon.ypoints[0]);
        Point	pointB          = new Point(roiPolygon.xpoints[1], roiPolygon.ypoints[1]);
        Point	pointC          = new Point(roiPolygon.xpoints[2], roiPolygon.ypoints[2]);
        Point[]	points          = new Point[] { pointA, pointB, pointC };

        for (int i = 0; i < 3; i++) {
            int		iA     = (i + 1) % 3,
					iB     = (i + 2) % 3,
					iC     = i;

            Point	point1 = points[iA];
            Point	point2 = points[iB];

            distance = point1.distance(point2);

            if (distance > longestDistance) {
                pointA          = points[iA];
                pointB          = points[iB];
                pointC          = points[iC];
                longestDistance = distance;
            }

//          GrasppeKit.debugText("Vertex Iteration",
//                               "\t(i:" + i + ")\t" + GrasppeKit.lastSplit(pointC.toString())
//                               + "\t" + GrasppeKit.lastSplit(pointA.toString()) + "\t"
//                               + GrasppeKit.lastSplit(pointB.toString()), 5);
        }

        // TODO: Sort out this mess, make sure it works.

        int			x1     = pointC.x,
					x2     = pointA.x,
					x3     = pointB.x;
        int			x4     = x3 + x2 - x1;
        int			y1     = pointC.y,
					y2     = pointA.y,
					y3     = pointB.y;
        int			y4     = y3 + y2 - y1;

        BlockROI	newROI = new BlockROI(new int[] { x1, x2, x3, x4 }, new int[] { y1, y2, y3, y4 },
                                       4);

        return newROI;
    }

    /**
     * @return
     */
    public boolean canMagnifyPatch() {
        try {
            return cornerSelectorCommons.shouldZoomPatch();

//          int   pI = Testing.magnifyPatchIndex;
//          int   nP = Testing.pointROI.getNCoordinates();
//
//          // TODO: tie this to actual target patches
//          if (nP < 64) return false;
//
//          if (pI < 0) return false;
//
//          return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * @param pointROI
     * @return
     */
    public String debugPoints(PointRoi pointROI) {
        int	pointCount = 0;

        pointCount = pointROI.getNCoordinates();

        String	strPointCount = "Points: " + pointCount;
        String	strPoints     = "";
        int[]	xPoints       = pointROI.getXCoordinates();
        int[]	yPoints       = pointROI.getYCoordinates();

        for (int i = 0; i < pointCount; i++)
            strPoints += "P" + i + ": (" + xPoints[i] + ", " + yPoints[i] + ")\t";

        return (strPointCount + "\t" + strPoints);
    }

    /**
     * @param e
     * @return
     */
    public boolean dispatchedKeyEvent(KeyEvent e) {

        updateMagnification();

        return false;
    }

    /**
     */
    public void magnifyLastPatch() {
        try {
            if (!canMagnifyPatch()) {
                cornerSelectorCommons.magnifyPatchIndex = -1;

                return;
            }

            int	pI = cornerSelectorCommons.magnifyPatchIndex;

            pI--;

            pI                                      %= (cornerSelectorCommons.overlayROI.getNCoordinates());

            cornerSelectorCommons.magnifyPatchIndex = pI;

        } catch (Exception exception) {
            return;
        }

        updateMagnifyPatch();

    }

    /**
     */
    public void magnifyNextPatch() {
        try {
            if (!canMagnifyPatch()) {
                cornerSelectorCommons.magnifyPatchIndex = -1;

                return;
            }

            int	pI = cornerSelectorCommons.magnifyPatchIndex;
            int	nI = cornerSelectorCommons.overlayROI.getNCoordinates();

            pI++;

            pI %= nI;

            //

            cornerSelectorCommons.magnifyPatchIndex = pI;

        } catch (Exception exception) {
            return;
        }

        updateMagnifyPatch();

    }

//  /**
//   * @param args
//   */
//  public static void main(String[] args) {
//
//      new CornerSelectorView(null).run("");
//
//  }

    /**
     *  @param e
     */
    public void mouseClicked(MouseEvent e) {
        CornerSelectorListeners.debugEvent(this, "IJMouseListener", e);
        updateROI(e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseDragged(MouseEvent e) {
        CornerSelectorListeners.debugEvent(this, "IJMotionListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseEntered(MouseEvent e) {
        CornerSelectorListeners.debugEvent(this, "IJMouseListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseExited(MouseEvent e) {
        CornerSelectorListeners.debugEvent(this, "IJMouseListener", e);
        redrawFrame();
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseMoved(MouseEvent e) {
        moveFrame(e.getXOnScreen(), e.getYOnScreen());
        CornerSelectorListeners.debugEvent(this, "IJMotionListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mousePressed(MouseEvent e) {
        CornerSelectorListeners.debugEvent(this, "IJMouseListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseReleased(MouseEvent e) {
        CornerSelectorListeners.debugEvent(this, "IJMouseListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        CornerSelectorListeners.debugEvent(this, "IJWheelListener", e);
        e.consume();
    }

    /**
     * @param x
     * @param y
     */
    public void moveFrame(int x, int y) {

        if (cornerSelectorCommons.zoomLock) return;

        if (canMagnifyPatch()) {
            if (getZoomWindow() == null) return;

            JFrame		zoomWindow  = getZoomWindow();
            ImageWindow	imageWindow = getImageWindow();
            PointRoi	pointROI    = cornerSelectorCommons.overlayROI;

            cornerSelectorCommons.isMouseOverImage = imageWindow.getCanvas().getBounds().contains(x
                    - imageWindow.getX(), y - imageWindow.getY());

            int	mX = 100;		// imageWindow.getCanvas().
            int	mY = 100;

            // zoomWindow.setLocation(xCenter, yCoordinates);

            zoomWindow.setLocation(x - zoomWindow.getWidth() / 2, y - zoomWindow.getHeight() / 2);

            cornerSelectorCommons.zoomLock = true;

            redrawFrame();

            return;
        } else {

            updateMagnifier();

            if (getZoomWindow() == null) return;

            JFrame		zoomWindow  = getZoomWindow();
            ImageWindow	imageWindow = getImageWindow();

            cornerSelectorCommons.isMouseOverImage = imageWindow.getCanvas().getBounds().contains(x
                    - imageWindow.getX(), y - imageWindow.getY());

            // Sets visible and returns updated zoomWindow.isVisible()

            zoomWindow.setLocation(x - zoomWindow.getWidth() / 2, y - zoomWindow.getHeight() / 2);

            if (!isShowZoom()) return;

            redrawFrame();
        }
    }

    /**
     * Opens an ImagePlus image using Opener and creates and displays it in an
     * ImageWindow;
     *  @param imageFile
     */
    public void prepareImageWindow(ImageFile imageFile) {

        try {
            Opener		opener    = new Opener();
            ImagePlus	imagePlus = opener.openImage(imageFile.getAbsolutePath());

            setImageWindow(new ImageWindow(imagePlus));
            getImageWindow().getCanvas().fitToWindow();
            getImageWindow().setExtendedState(Frame.MAXIMIZED_BOTH);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     */
    public void prepareMagnifier() {
        prepareMagnifierFrame();

        JFrame				frame          = getZoomWindow();
        ImageWindow			imageWindow    = getImageWindow();
        MouseMotionListener	motionListener = this;		// CornerSelectorListeners.IJMotionListener;

        frame.addMouseMotionListener(motionListener);
        frame.addMouseListener(this);
        imageWindow.getCanvas().addMouseMotionListener(motionListener);

        /* Static Updates */
    }

    /**
     */
    public void prepareMagnifierFrame() {
        setZoomWindow(new JFrame());	// "SpringLayout");
        getZoomWindow().setUndecorated(true);
        getZoomWindow().setSize(300, 300);
        getZoomWindow().setMaximumSize(getZoomWindow().getSize());

        // Testing.getZoomWindow().setAlwaysOnTop(true);
        getZoomWindow().setFocusableWindowState(false);
        getZoomWindow().setResizable(false);

        getZoomWindow().setBackground(Color.black);

        MagnifierCanvas	zoomCanvas = new MagnifierCanvas(getImageWindow().getImagePlus());

        zoomCanvas.addMouseListener(this);			// CornerSelectorListeners.IJMouseListener);
        zoomCanvas.addMouseMotionListener(this);	// CornerSelectorListeners.IJMotionListener);
        zoomCanvas.addMouseWheelListener(this);		// CornerSelectorListeners.IJWheelListener);
        zoomCanvas.setBackground(Color.black);

        // Testing.imageWindow.getCanvas().zoomIn(0, 0);

        Container	contentPane = getZoomWindow().getContentPane();

        contentPane.setBackground(Color.BLACK);

        /**
         * {@link http
         * ://www.java2s.com/Tutorial/Java/0240__Swing/SpringLayout.htm}
         */

        // SpringLayout layout = new SpringLayout();
        // contentPane.setLayout(layout);

        contentPane.add(zoomCanvas);

        int	padding = 0;

        GrasppeKit.debugText("Zoom Frame", "Content Pane " + contentPane.getBounds().toString(), 3);
        GrasppeKit.debugText("Zoom Frame", "Frame " + getZoomWindow().getBounds().toString(), 3);
        GrasppeKit.debugText("Zoom Frame", "Zoom Canvas " + zoomCanvas.getBounds().toString(), 3);

        double	windowZoom = getImageWindow().getCanvas().getMagnification();

        zoomCanvas.setMagnification(windowZoom * 2.0);

        getZoomWindow().setVisible(isShowZoom());

        getZoomWindow().pack();
        getZoomWindow().setSize(300, 300);

        zoomCanvas = zoomCanvas;

    }

    /**
     */
    public void redrawFrame() {
        if (getZoomWindow() == null) return;

        if (!getZoomWindow().isVisible()) return;		// {Testing.zoomWindow.setVisible(false); return;}

        ImageWindow	imageWindow  = getImageWindow();
        ImageCanvas	imageCanvas  = imageWindow.getCanvas();
        JFrame		zoomWindow   = getZoomWindow();

        Point		zoomWindowLocation,
					zoomLocation = null;

        if (zoomWindow.isVisible()) {
            zoomWindowLocation = zoomWindow.getLocationOnScreen();		// Location
            zoomLocation       = zoomCanvas.getLocationOnScreen();		// Location on
        }

        Point	zoomPosition = zoomCanvas.getLocation();	// Position within

        // component
        Dimension	zoomSize            = zoomCanvas.getSize();
        Double		zoomScale           = zoomCanvas.getMagnification();

        Point		imageWindowLocation = imageWindow.getLocationOnScreen();
        Point		imageLocation       = imageCanvas.getLocationOnScreen();
        Point		imagePosition       = imageCanvas.getLocation();
        Dimension	imageSize           = imageCanvas.getSize();
        Double		imageScale          = imageCanvas.getMagnification();

        Point		pointerCenter       = null,
					sourceCenter        = null;

        try {
            if (canMagnifyPatch()) {
                int			pI        = cornerSelectorCommons.magnifyPatchIndex;

                PointRoi	pointROI  = cornerSelectorCommons.overlayROI;

                int			pX        = pointROI.getXCoordinates()[pI];
                int			pY        = pointROI.getYCoordinates()[pI];

                Rectangle	roiBounds = pointROI.getBounds();

                double		rX        = roiBounds.getX();
                double		rY        = roiBounds.getY();

                int			fX        = (int)rX + pX;
                int			fY        = (int)rY + pY;

                pointerCenter = new Point(fX, fY);

                sourceCenter  = pointerCenter;

                String	debugString = "Patch #" + pI + "-"
                                     + GrasppeKit.lastSplit(pointerCenter.toString());

                GrasppeKit.debugText("Magnifier", debugString, 3);

            } else {

                /* Pointer Center: pointer location in image canvas in screen units */
                if (zoomWindow.isVisible()) {
                    pointerCenter = zoomCanvas.getMousePosition();
                    pointerCenter = new Point(zoomLocation.x - imageLocation.x + pointerCenter.x,
                                              zoomLocation.y - imageLocation.y + pointerCenter.y);
                } else
                    pointerCenter = imageCanvas.getMousePosition();

                sourceCenter = new Point((int)(pointerCenter.x / imageScale),
                                         (int)(pointerCenter.y / imageScale));

            }
        } catch (Exception exception) {
            zoomWindow.setVisible(false);
            pointerCenter = null;
        }

        if (pointerCenter == null) return;

        // if (!zoomWindow.isVisible()) zoomWindow.setVisible(true);

        /*
         * Source Center: pointer location in source coordinate space in source
         * units
         */

        /* Source Size: source rectangle size in source units */
        Dimension	sourceSize = new Dimension((int)(zoomSize.width / zoomScale),
                                   (int)(zoomSize.height / zoomScale));

        /* Source Corner: source rectangle top-left in source units */
        Point	sourceCorner = new Point((sourceCenter.x - sourceSize.width / 2),
                                       (sourceCenter.y - sourceSize.height / 2));

        /* Source Rectangle: source rectangle in source units */
        Rectangle	sourceRectangle = new Rectangle(sourceCorner, sourceSize);

        String		debugString     = "";

        debugString += "\t" + GrasppeKit.lastSplit(pointerCenter.toString());
        debugString += "\t" + GrasppeKit.lastSplit(zoomSize.toString());
        debugString += "\t" + GrasppeKit.lastSplit(sourceRectangle.toString());

        GrasppeKit.debugText("Magnifier", debugString, 4);

        zoomCanvas.setSourceRect(sourceRectangle);

        zoomCanvas.repaint(0, 0, zoomSize.width, zoomSize.height);

    }

    /**
     * @param arg
     */

    public void run(String arg) {

        if (getController().getBlockImage() == null) return;

        ImageWindow	imageWindow = getImageWindow();

        if ((imageWindow != null) && imageWindow.isVisible()) {
            imageWindow.setVisible(false);
            imageWindow.dispose();
        } else {
            GrasppeEventDispatcher	eventDispatcher = GrasppeEventDispatcher.getInstance();

            eventDispatcher.attachEventHandler(this);
        }

        ImageFile	blockImageFile = getController().getBlockImage();

        prepareImageWindow(blockImageFile);

        imageWindow = getImageWindow();

        attachMouseListeners();
        imageWindow.setVisible(true);

//      prepareMagnifier();

    }

    /**
     *  @param pointROI
     *  @throws InvalidAttributesException
     */
    private void sortROIs(PointRoi pointROI) throws InvalidAttributesException {
        cornerSelectorCommons.sortedROI = null;

        // TODO: get the convex boundary of the four pointROI
        Polygon		roiPolygon = pointROI.getConvexHull();

        PointRoi	newROI     = new PointRoi(roiPolygon);

        int[]		roiXs      = newROI.getXCoordinates(),
					roiYs      = newROI.getYCoordinates();

        if (roiXs.length != 4)
            throw new InvalidAttributesException(
                "Input point count needed for sorting must be 4 and not " + roiXs.length);		// return null;

        // TODO: sort the points - find distance between each point and top left
        int	maxdXY  = 0,
			mindXY  = 0;
        int	maxdXYi = 0,
			mindXYi = 0;

        for (int p = 0; p < 4; p++) {
            int	roidX  = roiXs[p];
            int	roidY  = roiYs[p];
            int	roidXY = (int)Math.sqrt(Math.pow(roidX, 2) + Math.pow(roidY, 2));

            if (roidXY > maxdXY) {
                maxdXYi = p;
                maxdXY  = roidXY;
            }

            if (mindXY == 0) mindXY = maxdXY;

            if (roidXY < mindXY) {
                mindXYi = p;
                mindXY  = roidXY;
            }
        }

        int[]	sortedIs = new int[4];
        int[]	sortedXs = new int[4];
        int[]	sortedYs = new int[4];
        int		sI       = 0;

        // TODO: Sort the points, clockwise, top-left, top-right, bottom-right, bottom-left
        for (int p = 0; p < 4; p++) {
            if (p == mindXYi) sortedIs[0] = p;
            else if (p == maxdXYi) sortedIs[2] = p;
            else {
                if (sI == 0) {
                    sortedIs[1] = p;
                    sI          = p;	// 1;
                } else {
                    if (roiXs[sI] < roiXs[p]) {
                        sortedIs[3] = 1;
                        sortedIs[1] = p;
                    } else if (roiXs[sI] > roiXs[p]) sortedIs[3] = p;
                }
            }
        }

        int	tlX = newROI.getBounds().getLocation().x,
			tlY = newROI.getBounds().getLocation().y;

        for (int p = 0; p < 4; p++) {
            sI           = sortedIs[p];
            sortedXs[sI] = tlX + roiXs[p];
            sortedYs[sI] = tlY + roiYs[p];
        }

        PointRoi	sortedROI = new PointRoi(sortedXs, sortedYs, 4);

        cornerSelectorCommons.sortedROI = sortedROI;	// new PointRoi(sortedXs, sortedYs, 4);

    }

    /**
     *  @throws Throwable
     */
    private void terminate() throws Throwable {
        JFrame		zoomWindow  = getZoomWindow();
        ImageWindow	imageWindow = getImageWindow();

        if ((zoomWindow != null) && zoomWindow.isDisplayable()) zoomWindow.dispose();
        if ((imageWindow != null) && imageWindow.isDisplayable()) imageWindow.dispose();
        super.notifyObservers();
        super.finalize();
    }

    /**
     */

    public void update() {

        // TODO Auto-generated method stub

    }

    /**
     */
    public void updateMagnification() {

        // boolean keyPressed =
        // GrasppeEventDispatcher.isPressed({KeyCode.VK_ALT,KeyCode.VK_EQUALS});
//      boolean plusKey = GrasppeEventDispatcher.isPressed(KeyCode.VK_EQUALS);
//      boolean minusKey = GrasppeEventDispatcher.isPressed(KeyCode.VK_MINUS);
        try {
            boolean	plusKey = GrasppeEventDispatcher.isPressed(new KeyCode[] { KeyCode.VK_ALT,
                    KeyCode.VK_EQUALS });
            boolean	minusKey = GrasppeEventDispatcher.isPressed(new KeyCode[] { KeyCode.VK_ALT,
                    KeyCode.VK_MINUS });

            double	currentScale = zoomCanvas.getMagnification();
            double	maxScale     = 1.0;
            double	minScale     = getImageWindow().getCanvas().getMagnification();

            if (plusKey) {
                zoomCanvas.setMagnification(Math.min(currentScale * 1.25, maxScale));
                redrawFrame();
            }

            if (minusKey) {
                zoomCanvas.setMagnification(Math.max(currentScale * 0.95, minScale));
                redrawFrame();
            }
        } catch (Exception exception) {
            return;
        }
    }

    /**
     */
    public void updateMagnifier() {

        if (getZoomWindow() == null) return;

        boolean	keyPressed = GrasppeEventDispatcher.isDown(KeyCode.VK_ALT);
        boolean	mouseOver  = cornerSelectorCommons.isMouseOverImage;

        setShowZoom(keyPressed && mouseOver);

        // if (!canMagnifyPatch()) Testing.setShowZoom(keyPressed && mouseOver);

    }

    /**
     */
    public void updateMagnifyPatch() {
        try {
            if (!canMagnifyPatch()) {
                cornerSelectorCommons.magnifyPatchIndex = -1;

                return;
            }

            int			pI        = cornerSelectorCommons.magnifyPatchIndex;

            PointRoi	pointROI  = cornerSelectorCommons.overlayROI;

            int			pX        = pointROI.getXCoordinates()[pI];
            int			pY        = pointROI.getYCoordinates()[pI];

            Rectangle	roiBounds = pointROI.getBounds();

            double		rX        = roiBounds.getX();
            double		rY        = roiBounds.getY();

            int			fX        = (int)rX + pX;
            int			fY        = (int)rY + pY;

            int			gX        = getImageWindow().getCanvas().screenX(fX);
            int			gY        = getImageWindow().getCanvas().screenX(fY);

            GrasppeKit.debugText("ROI Bounds:\tr: (" + roiBounds.getX() + ", " + roiBounds.getY()
                                 + ")\tp: (" + pX + ", " + pY + ")\tf: (" + fX + ", " + fY
                                 + ")\tg: (" + gX + ", " + gY + ")", 2);

            cornerSelectorCommons.shouldZoomPatch();

            setShowZoom(true);

            cornerSelectorCommons.zoomLock = false;

            moveFrame(gX, gY);

        } catch (Exception exception) {
            return;
        }
    }

    /**
     * @param e
     */
    public void updateROI(MouseEvent e) {
        if (e.isConsumed()) return;
        if (getImageWindow() == null) return;
        if (!getImageWindow().isVisible()) return;

        // TODO: Determine whether to add point (click) or clear points (triple
        // click)
        int	clickCount = e.getClickCount();

        if (clickCount == 3) cornerSelectorCommons.overlayROI = null;

        canMagnifyPatch();

        int	newX = 0;
        int	newY = 0;

        // TODO: Add get mouse position relative to canvas
        Point	mousePosition;

        try {

//          if (getZoomWindow() == null
//                  | !getZoomWindow().isVisible()) {
            mousePosition = getImageWindow().getCanvas().getMousePosition();
            newX          = mousePosition.x;
            newY          = mousePosition.y;

            if (cornerSelectorCommons.overlayROI != null) {
                newX = getImageWindow().getCanvas().offScreenX(newX);
                newY = getImageWindow().getCanvas().offScreenY(newY);
            }

//          } else {
//              Point mousePosition2 = zoomCanvas.getMousePosition();
//              Point tlImage        =
//                  getImageWindow().getCanvas().getLocationOnScreen();
//              Point tlZoom = zoomCanvas.getLocationOnScreen();
//
//              newX = tlZoom.x + mousePosition2.x - tlImage.x;
//              newY = tlZoom.y + mousePosition2.y - tlImage.y;
//
//              if (cornerSelectorCommons.pointROI != null) {
//                  newX = getImageWindow().getCanvas().offScreenX(newX);
//                  newY = getImageWindow().getCanvas().offScreenY(newY);
//              }
//          }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        mousePosition = new Point(newX, newY);

        int	pointX = newX;		// Testing.imageWindow.getCanvas().offScreenX(mousePosition.x);
        int	pointY = newY;		// Testing.imageWindow.getCanvas().offScreenY(mousePosition.y);

        if (mousePosition == null) return;

        if ((clickCount == 1) && (cornerSelectorCommons.overlayROI == null))
            cornerSelectorCommons.overlayROI = new PointRoi(pointX, pointY,		// mousePosition.x, mousePosition.y,
                getImageWindow().getImagePlus());
        else if ((clickCount == 1)
                 && ((cornerSelectorCommons.overlayROI != null)
                     && (cornerSelectorCommons.overlayROI.getNCoordinates() < 3)))
                 cornerSelectorCommons.overlayROI =
                     cornerSelectorCommons.overlayROI.addPoint(pointX, pointY);

        // TODO: When 0 points are defined, clear overlay
        if ((cornerSelectorCommons.overlayROI == null)
                || (cornerSelectorCommons.overlayROI.getNCoordinates() == 0)) {
            getImageWindow().getImagePlus().setOverlay(new Overlay());

            return;
        }

//      String    debugStrings1 = debugPoints(cornerSelectorCommons.pointROI);

        // TODO: When 3 points are defined, calculate the fourth
        if (cornerSelectorCommons.overlayROI.getNCoordinates() == 3) {
            BlockROI	newROI = calculateFourthVertex(cornerSelectorCommons.overlayROI);

            cornerSelectorCommons.overlayROI = newROI;
            setBlockROI(newROI);
            notifyObservers();
        }

        // TODO: When 4 points are defined! We are done.

        // TODO: Finally, update overlay with defined points
//      String    debugStrings2 = debugPoints(cornerSelectorCommons.pointROI);
        Overlay	overlay = new Overlay(cornerSelectorCommons.overlayROI);

        overlay.drawNames(true);
        overlay.drawLabels(true);
        getImageWindow().getImagePlus().setOverlay(overlay);

        // Testing.zoomCanvas.setOverlay(overlay);
        // Testing.zoomCanvas.getImage().updateAndRepaintWindow();
        // Testing.imageWindow.getImagePlus().updateAndRepaintWindow();

        if (cornerSelectorCommons.overlayROI.getNCoordinates() == 4) {

//          GrasppeKit.debugText("Vertex Selection",
//                               "\n\t" + debugStrings1 + "\n\t" + "")"debugStrings2, 3);
            setPatchSetROI();		// calculateAffineGrid(cornerSelectorCommons.pointROI, 10, 10));
            notifyObservers();
        }

        calculateAffineGrid();

//      setPatchSetROI(calculateAffineGrid(cornerSelectorCommons.pointROI, 10, 10));
    }

    /**
     */
    @SuppressWarnings("restriction")
    private void warpPatchGrid() {

        // TODO: WarpPolynomial

        /*
         *  public static WarpPolynomial createWarp
         * Returns an instance of WarpPolynomial or its subclasses that approximately maps the given scaled destination image coordinates into the given scaled source image coordinates. The mapping is given by:
         * x' = postScaleX*(xpoly(x*preScaleX, y*preScaleY));
         * x' = postScaleY*(ypoly(x*preScaleX, y*preScaleY));
         * Typically, it is useful to set preScaleX to 1.0F/destImage.getWidth() and postScaleX to srcImage.getWidth() so that the input and output of the polynomials lie between 0 and 1.
         * The degree of the polynomial is supplied as an argument.
         * sourceCoords - An array of floats containing the source coordinates with X and Y alternating.
         * sourceOffset - the initial entry of sourceCoords to be used.
         * destCoords - An array of floats containing the destination coordinates with X and Y alternating.
         * destOffset - The initial entry of destCoords to be used.
         * numCoords - The number of coordinates from sourceCoords and destCoords to be used.
         * preScaleX - The scale factor to apply to input (dest) X positions.
         * preScaleY - The scale factor to apply to input (dest) Y positions.
         * postScaleX - The scale factor to apply to X polynomial output.
         * postScaleY - The scale factor to apply to the Y polynomial output.
         * degree - The desired degree of the warp polynomials.
         */

        /*
         * Based on
         * {@link http://java.sun.com/developer/onlineTraining/javaai/jai/src/JAIWarpDemo.java}
         */
        WarpPolynomial	warp;

        // Determine the points needed
        int	warpDegree = 1;		// degree - The desired degree of the warp polynomials.
        int	numPoints  = 3;		// pointsNeeded = (degree + 1) * (degree + 2) / 2;

        int	width      = 10;
        int	height     = 10;

//      float[]   coeffs     = {
//          1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F
//      };

//      float[]   sourceCoords;       // sourceCoords - An array of floats containing the source coordinates with X and Y alternating.
//      float[]   destCoords;     // destCoords - An array of floats containing the destination coordinates with X and Y alternating.
//      float[]   xcoeffs;    // An array of coefficients that maps a destination point to the source's X coordinate.
//      float[]   ycoeffs;    // An array of coefficients that maps a destination point to the source's Y coordinate.
//
//      int   numCoords  = 2 * numPoints;     // (2*numPoints) numCoords - The number of coordinates from sourceCoords and destCoords to be used.
//      float preScaleX = 1.0F / width;       // (1.0F/width) preScaleX - The scale factor to apply to input (dest) X positions.
//      float preScaleY = 1.0F / height;      // (1.0F/height) preScaleY - The scale factor to apply to input (dest) Y positions.
//      float postScaleX = width;     // ((float)width) postScaleX - The scale factor to apply to X polynomial output.
//      float postScaleY = height;    // ((float)height) postScaleY - The scale factor to apply to the Y polynomial output.

        /* let's assume a 14 by 14 target grid and a four point clockwise roi set: P0: (0, 3)   P1: (631, 0)    P2: (633, 526)  P3: (2, 529) */

        /*
         * Target Coordinates
         * Fiducials ULC: (0, 0) URC: (58.5, 0) LRC: (58.5, 45.0)   LLC: (0, 45.0)
         */

        float	dx = 4.5F;
        float[]	x0 = new float[] {
            2.3F, 8.1F, 14F, 19.8F, 25.7F, 31.5F, 37.4F, 43.2F, 49.1F, 54.9F, 60.7F, 66.5F, 72.3F,
            78.1F
        };

//      float dw = x0[1] - x0[0];
        float[]	tx = new float[] { 0.0F, x0[x0.length - 1] + dx / 2.0F };

        float	dy = 4.5F;
        float[]	y0 = new float[] {
            2.3F, 6.8F, 11.3F, 15.8F, 20.3F, 24.8F, 29.3F, 33.8F, 38.3F, 42.8F, 47.3F, 51.8F, 56.3F,
            60.8F
        };

//      float dh = y0[1] - y0[0];
        float[]	ty = new float[] { 0.0F, y0[y0.length - 1] + dy / 2.0F };

        // Warp Destination Arguments
        int		destOffset   = 0;		// destOffset - The initial entry of destCoords to be used.
        float[]	targetCoords = new float[] {	// destCoords - An array of floats containing the destination coordinates with X and Y alternating.
            tx[0], ty[0], tx[1], ty[0], tx[1], ty[1], tx[0], ty[1]
        };

        // Warp Source Arguments
        int[]	sortedXs     = cornerSelectorCommons.sortedROI.getXCoordinates();
        int[]	sortedYs     = cornerSelectorCommons.sortedROI.getYCoordinates();

        int		sourceOffset = 0;		// sourceOffset - the initial entry of sourceCoords to be used.
        float[]	imageCoords  = new float[] {	// sourceCoords - An array of floats containing the source coordinates with X and Y alternating.
            sortedXs[0], sortedYs[0], sortedXs[1], sortedYs[1], sortedXs[2], sortedYs[2],
            sortedXs[3], sortedYs[3]
        };

        warp = WarpPolynomial.createWarp(imageCoords, sourceOffset, targetCoords, destOffset,
                                         2 * numPoints, 1.0F / width, 1.0F / height, width, height,
                                         warpDegree);

        int		tlX  = cornerSelectorCommons.sortedROI.getBounds().getLocation().x;
        int		tlY  = cornerSelectorCommons.sortedROI.getBounds().getLocation().y;

        int		nX   = 14;
        int		nY   = 14;
        int		nP   = nX * nY;

        int[]	cXs  = new int[nP];
        int[]	cYs  = new int[nP];
        int[]	cX5s = new int[nP * 5];
        int[]	cY5s = new int[nP * 5];
        int[]	cX4s = new int[nP * 4];
        int[]	cY4s = new int[nP * 4];

        for (int c = 0; c < nX; c++) {
            float	xc = x0[c];
            float	x1 = xc - dx / 2;
            float	x2 = xc + dx / 2;

            for (int r = 0; r < nY; r++) {

                // TODO: Map the centers
                float	yr = y0[r];
                float	y1 = yr - dy / 2;
                float	y2 = yr + dy / 2;

                Point2D	PC = new Point2D.Float(xc, yr);

                Point2D	IC = warp.mapDestPoint(PC);

                int		cI = (c * nX) + r;

                GrasppeKit.debugText("Warp Points",
                                     "C" + cI + ": " + GrasppeKit.lastSplit(PC.toString())
                                     + " ==> " + GrasppeKit.lastSplit(IC.toString()), 3);

                cXs[cI] = (int)IC.getX() + tlX;
                cYs[cI] = (int)IC.getY() + tlY;

                // Map the vertices (using 4 Point2D instead of Rectangle)
                Point2D[]	PVs = new Point2D.Float[4];

                PVs[0] = new Point2D.Float(x1, y1);
                PVs[1] = new Point2D.Float(x2, y1);
                PVs[2] = new Point2D.Float(x2, y2);
                PVs[3] = new Point2D.Float(x1, y2);

                int	cI5 = (c * nX * 5) + r * 5;
                int	cI4 = (c * nX * 4) + r * 4;

                cX5s[cI5] = cXs[cI];
                cY5s[cI5] = cYs[cI];

                for (int v = 0; v < 4; v++) {
                    Point2D	IV = warp.mapDestPoint(PVs[v]);

                    cX4s[cI4 + v]     = (int)IV.getX() + tlX;
                    cY4s[cI4 + v]     = (int)IV.getY() + tlY;
                    cX5s[cI5 + v + 1] = cX4s[cI4 + v];
                    cY5s[cI5 + v + 1] = cY4s[cI4 + v];
                }

                /*
                 * Point2D[] PVs = new Point2D.Float[4];
                 * PVs[0] = new Point2D.Float(x1, y1);
                 * PVs[1] = new Point2D.Float(x2, y1);
                 * PVs[2] = new Point2D.Float(x2, y2);
                 * PVs[3] = new Point2D.Float(x1, y2);
                 */

//              warp.mapDestRect(arg0;)
            }
        }

        cornerSelectorCommons.patchCenterROI = new PointRoi(cXs, cYs, nP);
        cornerSelectorCommons.vertexROI      = new PointRoi(cX4s, cY4s, cX4s.length);
        cornerSelectorCommons.combinedROI    = new PointRoi(cX5s, cY5s, cX5s.length);
    }

    /**
     * @return the blockROI
     */
    protected PointRoi getBlockCornerROI() {
        return blockROI;
    }

    /**
     *  @return
     */
    public CornerSelector getController() {
        return (CornerSelector)controller;
    }

    /**
     * @return the image window
     */
    public ImageWindow getImageWindow() {
        return imageWindow;
    }

    /**
     * Returns a correctly-cast model.
     * @return
     */
    public CornerSelectorModel getModel() {
        return (CornerSelectorModel)super.getControllerModel();
    }

    /**
     * @return the patchRadius
     */
    protected double getPatchRadius() {
        return patchRadius;
    }

    /**
     * @return the patchSetROI
     */
    protected PatchSetROI getPatchSetROI() {
        return patchSetROI;
    }

    /**
     * @return the zoom frame
     */
    public JFrame getZoomWindow() {
        return zoomWindow;
    }

    /**
     *         @return the showZoom
     */
    public boolean isShowZoom() {

        // if (magnifyPatchIndex>0) {
        // setShowZoom(true);
        // return true;
        // }
        return showZoom;
    }

    /**
     * @param blockROI the blockROI to set
     */
    protected void setBlockROI(BlockROI blockROI) {
        getModel().setBlockROI(blockROI);
        this.blockROI = getModel().getBlockROI();
    }

    /**
     * @param imageWindow
     */
    public void setImageWindow(ImageWindow imageWindow) {
        this.imageWindow = imageWindow;
    }

    /**
     * @param patchRadius the patchRadius to set
     */
    protected void setPatchRadius(double patchRadius) {
        this.patchRadius = patchRadius;
    }

    /**
     */
    protected void setPatchSetROI() {		// PatchSetROI patchSetROI) {
        getModel().setPatchSetROI(patchSetROI);
        this.patchSetROI = getModel().getPatchSetROI();
    }

    /**
     *         @param showZoom  the showZoom to set
     */
    public void setShowZoom(boolean showZoom) {
        getZoomWindow().setVisible(true);

        // if (shouldZoomPatch()) {
        // Testing.getZoomWindow().setVisible(true);
        // showZoom = true;
        // }
        // else Testing.getZoomWindow().setVisible(showZoom);
        this.showZoom = showZoom;
    }

    /**
     * @param zoomWindow
     */
    public void setZoomWindow(JFrame zoomWindow) {
        this.zoomWindow = zoomWindow;
    }

    /**
     * Class description
     * @version $Revision: 1.0, 11/11/11
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CornerSelectorCommons {

        /** Field description */
        public boolean	isMouseOverImage = false;

        /** Field description */
        public PointRoi	overlayROI,	patchCenterROI,	vertexROI, combinedROI,	sortedROI;

        /** Field description */
        public boolean	zoomPatch = false;

        /** Field description */
        public boolean	zoomLock = false;

        /** Field description */
        public int	magnifyPatchIndex = -1;

        /**
         */
        private CornerSelectorCommons() {}

        /**
         * @return
         */
        public boolean shouldZoomPatch() {

//          try {         
            if (magnifyPatchIndex > -1) {
                zoomPatch = true;
            }

            boolean	nullROI          = overlayROI == null;
            boolean	insufficientROIs = overlayROI.getNCoordinates() < 64;

            if (nullROI | insufficientROIs) zoomPatch = false;

//          } catch (Exception exception) {
//            zoomPatch = false;
//          }

            return isZoomPatch();
        }

        /**
         * @return the zoomPatch
         */
        public boolean isZoomPatch() {
            return zoomPatch;
        }

        /**
         * @param zoomPatch the zoomPatch to set
         */
        public void setZoomPatch(boolean zoomPatch) {
            cornerSelectorCommons.zoomPatch = zoomPatch;
        }
    }
}
