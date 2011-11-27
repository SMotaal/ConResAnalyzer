/*
 * @(#)CornerSelectorView.java   11/11/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.imagej;

import com.grasppe.conres.framework.targets.CornerSelector;
import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
import com.grasppe.conres.framework.targets.model.roi.BlockROI;
import com.grasppe.conres.framework.targets.model.roi.PatchSetROI;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeEventDispatcher;
import com.grasppe.lure.framework.GrasppeEventHandler;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.KeyCode;
import com.grasppe.lure.framework.StopWatch;

import ij.IJ;
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
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;

import javax.media.jai.WarpPolynomial;

import javax.swing.JFrame;

/**
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 *
 */

/**
 * @author daflair
 *
 */
public class CornerSelectorView extends AbstractView
        implements PlugIn, GrasppeEventHandler, MouseListener, MouseWheelListener,
                   MouseMotionListener {

    protected static String	name       = CornerSelectorView.class.getSimpleName();
    protected static int	exitDelay  = 3 * 1000;
    private static Object	threadLock = new Object();

    /** Field description */
    public BlockROI	blockROI;

    /** Field description */
    public PatchSetROI	patchSetROI;

    /** Field description */
    public double	patchRadius;

    /**
     * @param controller
     */
    public CornerSelectorView(CornerSelector controller) {
        super(controller);
        this.model = controller.getModel();
    }

    /**
     * Method description
     */
    public void attachMouseListeners() {
        ImageWindow	imageWindow = CornerSelectorCommons.getImageWindow();

        imageWindow.getCanvas().addMouseListener(this);
        imageWindow.getCanvas().addMouseMotionListener(this);
        imageWindow.getCanvas().addMouseWheelListener(this);

    }

    /**
     * @param pointROI
     * @param nR
     * @param nC
     *  @return
     */
    public static PatchSetROI calculateAffineGrid(PointRoi pointROI, int nR, int nC) {

        // TODO: get the convex boundary of the four pointROI
        Polygon		roiPolygon = pointROI.getConvexHull();

        PointRoi	newROI     = new PointRoi(roiPolygon);

        Rectangle	roiBounds  = newROI.getBounds();
        int			tlX        = roiBounds.x,
					tlY        = roiBounds.y;
        int			nROI       = newROI.getNCoordinates();
        int[]		roiXs      = newROI.getXCoordinates(),
					roiYs      = newROI.getYCoordinates(),
					roidX      = new int[nROI],
					roidY      = new int[nROI],
					roidXY     = new int[nROI];
        int			maxdXY     = 0,
					maxdXYi    = 0,
					mindXY     = 0,
					mindXYi    = 0,
					meandX     = 0,
					meandY     = 0;

        if (roiXs.length < 4) return null;

        // TODO: sort the points - find distance between each point and top left
        for (int p = 0; p < 4; p++) {
            roidX[p]  = roiXs[p];
            roidY[p]  = roiYs[p];
            roidXY[p] = (int)Math.sqrt(Math.pow(roidX[p], 2) + Math.pow(roidY[p], 2));

            if (roidXY[p] > maxdXY) {
                maxdXYi = p;
                maxdXY  = roidXY[p];
            }

            if (mindXY == 0) mindXY = maxdXY;

            if (roidXY[p] < mindXY) {
                mindXYi = p;
                mindXY  = roidXY[p];
            }

            meandX = (meandX * p + roidX[p]) / (p + 1);
            meandY = (meandY * p + roidY[p]) / (p + 1);
        }

        int[]	sortedXs = new int[4];
        int[]	sortedYs = new int[4];
        int		sI       = 0;

        // TODO: Sort the points, clockwise, top-left, top-right, bottom-right, bottom-left
        for (int p = 0; p < 4; p++) {
            if (p == mindXYi) {
                sortedXs[0] = roiXs[p];
                sortedYs[0] = roiYs[p];
            } else if (p == maxdXYi) {
                sortedXs[2] = roiXs[p];
                sortedYs[2] = roiYs[p];
            } else {
                if (sI == 0) {
                    sortedXs[1] = roiXs[p];
                    sortedYs[1] = roiYs[p];
                    sI          = 1;
                } else {

                    // finding the top-right (1), then bottom-left (3).
                    if (sortedXs[1] < roiXs[p]) {
                        sortedXs[3] = roiXs[1];
                        sortedYs[3] = roiYs[1];
                        sortedXs[1] = roiXs[p];
                        sortedYs[1] = roiYs[p];
                    } else if (sortedXs[1] > roiXs[p]) {
                        sortedXs[3] = roiXs[p];
                        sortedYs[3] = roiYs[p];
                    }
                }
            }
        }

        PointRoi	sortedROI = new PointRoi(sortedXs, sortedYs, 4);

        // TODO: WarpPolynomial

        /*
         *  public static WarpPolynomial createWarp
         * Returns an instance of WarpPolynomial or its subclasses that approximately maps the given scaled destination image coordinates into the given scaled source image coordinates. The mapping is given by:
         *
         * x' = postScaleX*(xpoly(x*preScaleX, y*preScaleY));
         * x' = postScaleY*(ypoly(x*preScaleX, y*preScaleY));
         *
         * Typically, it is useful to set preScaleX to 1.0F/destImage.getWidth() and postScaleX to srcImage.getWidth() so that the input and output of the polynomials lie between 0 and 1.
         *
         * The degree of the polynomial is supplied as an argument.
         *
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

        int				degree       = 1;		// degree - The desired degree of the warp polynomials.
        int				pointsNeeded = (degree + 1) * (degree + 2) / 2;
        int				numPoints    = pointsNeeded;

        int				width        = 10;
        int				height       = 10;

        float[]			coeffs       = {
            1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F
        };
        float[]			xcoeffs;	// An array of coefficients that maps a destination point to the source's X coordinate.
        float[]	ycoeffs;	// An array of coefficients that maps a destination point to the source's Y coordinate.
        float[]	sourceCoords;		// sourceCoords - An array of floats containing the source coordinates with X and Y alternating.
        int		sourceOffset = 0;		// sourceOffset - the initial entry of sourceCoords to be used.
        float[]	destCoords;		// destCoords - An array of floats containing the destination coordinates with X and Y alternating.
        int	destOffset = 0;		// destOffset - The initial entry of destCoords to be used.
        int	numCoords  = 2 * numPoints;		// (2*numPoints) numCoords - The number of coordinates from sourceCoords and destCoords to be used.
        float	preScaleX = 1.0F / width;		// (1.0F/width) preScaleX - The scale factor to apply to input (dest) X positions.
        float	preScaleY = 1.0F / height;		// (1.0F/height) preScaleY - The scale factor to apply to input (dest) Y positions.
        float	postScaleX = width;		// ((float)width) postScaleX - The scale factor to apply to X polynomial output.
        float	postScaleY = height;	// ((float)height) postScaleY - The scale factor to apply to the Y polynomial output.

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
        float	dw = x0[1] - x0[0];
        float[]	tx = new float[] { 0.0F, x0[x0.length - 1] + dx / 2.0F };

        float	dy = 4.5F;
        float[]	y0 = new float[] {
            2.3F, 6.8F, 11.3F, 15.8F, 20.3F, 24.8F, 29.3F, 33.8F, 38.3F, 42.8F, 47.3F, 51.8F, 56.3F,
            60.8F
        };
        float	dh           = y0[1] - y0[0];
        float[]	ty           = new float[] { 0.0F, y0[y0.length - 1] + dy / 2.0F };

        float[]	targetCoords = new float[] {
            tx[0], ty[0], tx[1], ty[0], tx[1], ty[1], tx[0], ty[1]
        };
        float[]	imageCoords  = new float[] {
            sortedXs[0], sortedYs[0], sortedXs[1], sortedYs[1], sortedXs[2], sortedYs[2],
            sortedXs[3], sortedYs[3]
        };

        sourceCoords = imageCoords;
        destCoords   = targetCoords;

        warp         = WarpPolynomial.createWarp(sourceCoords, sourceOffset, destCoords, destOffset,
                                         2 * numPoints, 1.0F / width, 1.0F / height, width, height,
                                         degree);

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
//              Rectangle2D   PR = new Rectangle2D.Float(x1, y1, x2 - x1, y2 - y1);
//              Rectangle2D   IR = warp.mapDestRect(PR.getBounds());
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

        PointRoi	patchCenterROI = new PointRoi(cXs, cYs, nP);
        PointRoi	vertexROI      = new PointRoi(cX4s, cY4s, cX4s.length);
        PointRoi	combinedROI    = new PointRoi(cX5s, cY5s, cX5s.length);

        CornerSelectorCommons.pointROI          = patchCenterROI;		// combinedROI; //centerROI;
        CornerSelectorCommons.magnifyPatchIndex = 0;

        Overlay	overlay = new Overlay(CornerSelectorCommons.pointROI);

        overlay.drawNames(true);
        overlay.drawLabels(true);
        CornerSelectorCommons.getImageWindow().getImagePlus().setOverlay(overlay);

        return new PatchSetROI(cXs, cYs, nP, nP);

    }

    /**
     * @param pointROI
     *
     * @return
     */
    public static BlockROI calculateFourthVertex(PointRoi pointROI) {
        if (pointROI.getNCoordinates() != 3) return (BlockROI)pointROI;

        // We have three points in the clicking order!

        // TODO: determine the convex boundary of the three points
        Polygon	roiPolygon = pointROI.getConvexHull();

        // TODO: determine point opposite hypotenuse, and the longest x offset
        // and y offset for the other two sides.
        double	longestDistance = 0,
				distance        = 0,
				deltaX          = 0,
				deltaY          = 0;

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

            deltaX   = point1.x - point2.x;
            deltaY   = point1.y - point2.y;
            distance = point1.distance(point2);

            if (distance > longestDistance) {
                pointA          = points[iA];
                pointB          = points[iB];
                pointC          = points[iC];
                longestDistance = distance;
            }

            GrasppeKit.debugText("Vertex Iteration",
                                 "\t(i:" + i + ")\t" + GrasppeKit.lastSplit(pointC.toString())
                                 + "\t" + GrasppeKit.lastSplit(pointA.toString()) + "\t"
                                 + GrasppeKit.lastSplit(pointB.toString()), 5);
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
            return CornerSelectorCommons.shouldZoomPatch();

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
     *
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
     *
     * @return
     */
    public boolean dispatchedKeyEvent(KeyEvent e) {

        updateMagnification();

        return false;
    }

    /**
     * Method description
     */
    public void magnifyLastPatch() {
        try {
            if (!canMagnifyPatch()) {
                CornerSelectorCommons.magnifyPatchIndex = -1;

                return;
            }

            int	pI = CornerSelectorCommons.magnifyPatchIndex;

            pI--;

            pI                                      %= (CornerSelectorCommons.pointROI.getNCoordinates());

            CornerSelectorCommons.magnifyPatchIndex = pI;

        } catch (Exception exception) {
            return;
        }

        updateMagnifyPatch();

    }

    /**
     * Method description
     */
    public void magnifyNextPatch() {
        try {
            if (!canMagnifyPatch()) {
                CornerSelectorCommons.magnifyPatchIndex = -1;

                return;
            }

            int	pI = CornerSelectorCommons.magnifyPatchIndex;
            int	nI = CornerSelectorCommons.pointROI.getNCoordinates();

            pI++;

            pI %= nI;

            //

            CornerSelectorCommons.magnifyPatchIndex = pI;

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
        CornerSelectorListeners.debugEvent("IJMouseListener", e);
        updateROI(e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseDragged(MouseEvent e) {
        CornerSelectorListeners.debugEvent("IJMotionListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseEntered(MouseEvent e) {
        CornerSelectorListeners.debugEvent("IJMouseListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseExited(MouseEvent e) {
        CornerSelectorListeners.debugEvent("IJMouseListener", e);
        redrawFrame();
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseMoved(MouseEvent e) {
        moveFrame(e.getXOnScreen(), e.getYOnScreen());
        CornerSelectorListeners.debugEvent("IJMotionListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mousePressed(MouseEvent e) {
        CornerSelectorListeners.debugEvent("IJMouseListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseReleased(MouseEvent e) {
        CornerSelectorListeners.debugEvent("IJMouseListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        CornerSelectorListeners.debugEvent("IJWheelListener", e);
        e.consume();
    }

    /**
     * @param x
     * @param y
     */
    public void moveFrame(int x, int y) {

        if (CornerSelectorCommons.zoomLock) return;

        if (canMagnifyPatch()) {
            if (CornerSelectorCommons.getZoomWindow() == null) return;

            JFrame		zoomWindow  = CornerSelectorCommons.getZoomWindow();
            ImageWindow	imageWindow = CornerSelectorCommons.getImageWindow();
            PointRoi	pointROI    = CornerSelectorCommons.pointROI;

            CornerSelectorCommons.isMouseOverImage = imageWindow.getCanvas().getBounds().contains(x
                    - imageWindow.getX(), y - imageWindow.getY());

            int	mX = 100;		// imageWindow.getCanvas().
            int	mY = 100;

            // zoomWindow.setLocation(mX, mY);

            zoomWindow.setLocation(x - zoomWindow.getWidth() / 2, y - zoomWindow.getHeight() / 2);

            CornerSelectorCommons.zoomLock = true;

            redrawFrame();

            return;
        } else {

            updateMagnifier();

            if (CornerSelectorCommons.getZoomWindow() == null) return;

            JFrame		zoomWindow  = CornerSelectorCommons.getZoomWindow();
            ImageWindow	imageWindow = CornerSelectorCommons.getImageWindow();

            CornerSelectorCommons.isMouseOverImage = imageWindow.getCanvas().getBounds().contains(x
                    - imageWindow.getX(), y - imageWindow.getY());

            // Sets visible and returns updated zoomWindow.isVisible()

            zoomWindow.setLocation(x - zoomWindow.getWidth() / 2, y - zoomWindow.getHeight() / 2);

            if (!CornerSelectorCommons.isShowZoom()) return;

            redrawFrame();
        }
    }

    /**
     * Opens an ImagePlus image using Opener and creates and displays it in an
     * ImageWindow;
     */
    public void prepareImageWindow() {

        /* Static Members */
        ImageWindow		imageWindow    = CornerSelectorCommons.getImageWindow();
        WindowAdapter	windowListener = new WindowAdapter() {

            public void windowClosed(WindowEvent e) {

//              notify();
//              Frame[] frames        = Frame.getFrames();
//              int     visibleFrames = 0;
//
//              for (Frame frame : frames)
//                  if (frame.isVisible()) visibleFrames++;
//
//              debugEvent("Window", e);
//
//              JFrame  zoomFrame = CornerSelectorView.CornerSelectorCommons.getZoomWindow();
//
//              if ((visibleFrames == 1) && zoomFrame.isVisible()) zoomFrame.setVisible(false);     // CornerSelectorView.delayedExit();
//              if (visibleFrames == 0) System.exit(0);     // CornerSelectorView.delayedExit();
                synchronized (threadLock) {		// frame.setVisible(false);
                    threadLock.notify();
                }
            }

//          public void windowClosing(WindowEvent e) {}
//
//          public void windowActivated(WindowEvent e) {}
//
//          public void windowDeactivated(WindowEvent e) {}
//
//          public void windowDeiconified(WindowEvent e) {}
//
//          public void windowIconified(WindowEvent e) {}
//
//          public void windowOpened(WindowEvent e) {}

        };		// CornerSelectorListeners.WindowEventListener;

        /* Local Variables */
        Opener		opener;
        ImagePlus	imagePlus;

        opener = new Opener();

        String	imagePath = CornerSelectorCommons.getInputPath();

        imagePlus = opener.openImage(imagePath);

        CornerSelectorCommons.setImageWindow(new ImageWindow(imagePlus));		// Initialize static

        imageWindow = CornerSelectorCommons.getImageWindow();

        CornerSelectorCommons.getImageWindow().getCanvas().fitToWindow();

        // imageWindow.setVisible(true);

        imageWindow.addWindowListener(windowListener);

    }

    /**
     * Method description
     */
    public void prepareMagnifier() {
        prepareMagnifierFrame();

        JFrame				frame          = CornerSelectorCommons.getZoomWindow();
        ImageWindow			imageWindow    = CornerSelectorCommons.getImageWindow();
        MouseMotionListener	motionListener = this;		// CornerSelectorListeners.IJMotionListener;

        frame.addMouseMotionListener(motionListener);
        frame.addMouseListener(this);
        imageWindow.getCanvas().addMouseMotionListener(motionListener);

        /* Static Updates */
    }

    /**
     * Method description
     */
    public void prepareMagnifierFrame() {
        CornerSelectorCommons.setZoomWindow(new JFrame());		// "SpringLayout");
        CornerSelectorCommons.getZoomWindow().setUndecorated(true);
        CornerSelectorCommons.getZoomWindow().setSize(300, 300);
        CornerSelectorCommons.getZoomWindow().setMaximumSize(
            CornerSelectorCommons.getZoomWindow().getSize());

        // Testing.getZoomWindow().setAlwaysOnTop(true);
        CornerSelectorCommons.getZoomWindow().setFocusableWindowState(false);
        CornerSelectorCommons.getZoomWindow().setResizable(false);

        CornerSelectorCommons.getZoomWindow().setBackground(Color.black);

        MagnifierCanvas	zoomCanvas =
            new MagnifierCanvas(CornerSelectorCommons.getImageWindow().getImagePlus());

        zoomCanvas.addMouseListener(this);			// CornerSelectorListeners.IJMouseListener);
        zoomCanvas.addMouseMotionListener(this);	// CornerSelectorListeners.IJMotionListener);
        zoomCanvas.addMouseWheelListener(this);		// CornerSelectorListeners.IJWheelListener);
        zoomCanvas.setBackground(Color.black);

        // Testing.imageWindow.getCanvas().zoomIn(0, 0);

        Container	contentPane = CornerSelectorCommons.getZoomWindow().getContentPane();

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
        GrasppeKit.debugText("Zoom Frame",
                             "Frame "
                             + CornerSelectorCommons.getZoomWindow().getBounds().toString(), 3);
        GrasppeKit.debugText("Zoom Frame", "Zoom Canvas " + zoomCanvas.getBounds().toString(), 3);

        double	windowZoom = CornerSelectorCommons.getImageWindow().getCanvas().getMagnification();

        zoomCanvas.setMagnification(windowZoom * 2.0);

        CornerSelectorCommons.getZoomWindow().setVisible(CornerSelectorCommons.isShowZoom());

        CornerSelectorCommons.getZoomWindow().pack();
        CornerSelectorCommons.getZoomWindow().setSize(300, 300);

        CornerSelectorCommons.zoomCanvas = zoomCanvas;

    }

    /**
     */
    public void redrawFrame() {
        if (CornerSelectorCommons.getZoomWindow() == null) return;

        if (!CornerSelectorCommons.getZoomWindow().isVisible()) return;		// {Testing.zoomWindow.setVisible(false); return;}

        ImageWindow		imageWindow  = CornerSelectorCommons.getImageWindow();
        ImageCanvas		imageCanvas  = imageWindow.getCanvas();
        JFrame			zoomWindow   = CornerSelectorCommons.getZoomWindow();
        MagnifierCanvas	zoomCanvas   = CornerSelectorCommons.zoomCanvas;

        Point			zoomWindowLocation,
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
                int			pI        = CornerSelectorCommons.magnifyPatchIndex;

                PointRoi	pointROI  = CornerSelectorCommons.pointROI;

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

        CornerSelectorCommons.zoomCanvas.setSourceRect(sourceRectangle);

        CornerSelectorCommons.zoomCanvas.repaint(0, 0, zoomSize.width, zoomSize.height);

    }

    /**
     * Method description
     *
     * @param arg
     */

    public void run(String arg) {
        IJ.showMessage(name, "Hello world!");

        final CornerSelectorView	thisView   = this;

        String[]					imageNames = { "CirRe27U_50t.png", "CirRe27U_50i.tif" };

        CornerSelectorCommons.imageName = imageNames[0];

        GrasppeKit.timestampLevel       = 5;
        GrasppeKit.debugLevel           = 3;

        GrasppeEventDispatcher	eventDispatcher = GrasppeEventDispatcher.getInstance();

        eventDispatcher.attachEventHandler(thisView);

        prepareImageWindow();

        final ImageWindow	imageWindow = CornerSelectorCommons.getImageWindow();

        attachMouseListeners();
        imageWindow.setVisible(true);

//      prepareMagnifier();

        IJ.showMessage(name, "Goodbye world!");

    }

    /**
     *  @throws Throwable
     */
    public void terminate() throws Throwable {
        JFrame		zoomWindow  = CornerSelectorCommons.getZoomWindow();
        ImageWindow	imageWindow = CornerSelectorCommons.getImageWindow();

        if ((zoomWindow != null) && zoomWindow.isDisplayable()) zoomWindow.dispose();
        if ((imageWindow != null) && imageWindow.isDisplayable()) imageWindow.dispose();
        super.notifyObservers();
        super.finalize();
    }

//  /**
//   * Method description
//   */
//  public void testROIFinder() {
//      Point[]   points = new Point[] { new Point(0, 0), new Point(1, 0), new Point(1, 1),
//                                     new Point(0, 1) };
//
//      Point origin  = new Point(0, 0);
//      int       stretch = 1;
//
//      for (int p = 0; p < 4; p++) {
//
//          // TODO: calculate other three points with origin and stretch
//          int           pA       = (p + 1) % 4,
//                    pB       = (p + 2) % 4,
//                    pC       = (p + 3) % 4;
//          Point[]       points2  = new Point[] { points[pA], points[pB], points[pC] };
//          int[]     xs       = new int[] { points2[0].x, points2[1].x, points2[2].x };
//          int[]     ys       = new int[] { points2[0].y, points2[1].y, points2[2].y };
//          PointRoi  pointROI = new PointRoi(xs, ys, 3);
//
//          GrasppeKit.debugText("Points Selection", debugPoints(pointROI), 3);
//
//          PointRoi  newROI = calculateFourthVertex(pointROI);
//
//          GrasppeKit.debugText("Points Determined", debugPoints(newROI), 3);
//      }
//  }

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

    public void update() {

        // TODO Auto-generated method stub

    }

    /**
     * Method description
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

            MagnifierCanvas	zoomCanvas   = CornerSelectorCommons.zoomCanvas;
            double			currentScale = zoomCanvas.getMagnification();
            double			maxScale     = 1.0;
            double			minScale     = CornerSelectorCommons.getImageWindow().getCanvas().getMagnification();

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
     * Method description
     */
    public static void updateMagnifier() {

        if (CornerSelectorCommons.getZoomWindow() == null) return;

        boolean	keyPressed = GrasppeEventDispatcher.isDown(KeyCode.VK_ALT);
        boolean	mouseOver  = CornerSelectorCommons.isMouseOverImage;

        CornerSelectorCommons.setShowZoom(keyPressed && mouseOver);

        // if (!canMagnifyPatch()) Testing.setShowZoom(keyPressed && mouseOver);

    }

    /**
     * Method description
     */
    public void updateMagnifyPatch() {
        try {
            if (!canMagnifyPatch()) {
                CornerSelectorCommons.magnifyPatchIndex = -1;

                return;
            }

            int			pI        = CornerSelectorCommons.magnifyPatchIndex;

            PointRoi	pointROI  = CornerSelectorCommons.pointROI;

            int			pX        = pointROI.getXCoordinates()[pI];
            int			pY        = pointROI.getYCoordinates()[pI];

            Rectangle	roiBounds = pointROI.getBounds();

            double		rX        = roiBounds.getX();
            double		rY        = roiBounds.getY();

            int			fX        = (int)rX + pX;
            int			fY        = (int)rY + pY;

            int			gX        = CornerSelectorCommons.imageWindow.getCanvas().screenX(fX);
            int			gY        = CornerSelectorCommons.imageWindow.getCanvas().screenX(fY);

            GrasppeKit.debugText("ROI Bounds:\tr: (" + roiBounds.getX() + ", " + roiBounds.getY()
                                 + ")\tp: (" + pX + ", " + pY + ")\tf: (" + fX + ", " + fY
                                 + ")\tg: (" + gX + ", " + gY + ")", 2);

            CornerSelectorCommons.shouldZoomPatch();

            CornerSelectorCommons.setShowZoom(true);

            CornerSelectorCommons.zoomLock = false;

            moveFrame(gX, gY);

        } catch (Exception exception) {
            return;
        }
    }

    /**
     * Method description
     *
     * @param e
     */
    public void updateROI(MouseEvent e) {
        if (e.isConsumed()) return;
        if (CornerSelectorCommons.getImageWindow() == null) return;
        if (!CornerSelectorCommons.getImageWindow().isVisible()) return;

        // TODO: Determine whether to add point (click) or clear points (triple
        // click)
        int	clickCount = e.getClickCount();

        if (clickCount == 3) CornerSelectorCommons.pointROI = null;

        canMagnifyPatch();

        int	newX = 0;
        int	newY = 0;

        // TODO: Add get mouse position relative to canvas
        Point	mousePosition;

        try {

//          if (CornerSelectorCommons.getZoomWindow() == null
//                  | !CornerSelectorCommons.getZoomWindow().isVisible()) {
            mousePosition = CornerSelectorCommons.getImageWindow().getCanvas().getMousePosition();
            newX          = mousePosition.x;
            newY          = mousePosition.y;

            if (CornerSelectorCommons.pointROI != null) {
                newX = CornerSelectorCommons.getImageWindow().getCanvas().offScreenX(newX);
                newY = CornerSelectorCommons.getImageWindow().getCanvas().offScreenY(newY);
            }

//          } else {
//              Point mousePosition2 = CornerSelectorCommons.zoomCanvas.getMousePosition();
//              Point tlImage        =
//                  CornerSelectorCommons.getImageWindow().getCanvas().getLocationOnScreen();
//              Point tlZoom = CornerSelectorCommons.zoomCanvas.getLocationOnScreen();
//
//              newX = tlZoom.x + mousePosition2.x - tlImage.x;
//              newY = tlZoom.y + mousePosition2.y - tlImage.y;
//
//              if (CornerSelectorCommons.pointROI != null) {
//                  newX = CornerSelectorCommons.getImageWindow().getCanvas().offScreenX(newX);
//                  newY = CornerSelectorCommons.getImageWindow().getCanvas().offScreenY(newY);
//              }
//          }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        mousePosition = new Point(newX, newY);

        int	pointX = newX;		// Testing.imageWindow.getCanvas().offScreenX(mousePosition.x);
        int	pointY = newY;		// Testing.imageWindow.getCanvas().offScreenY(mousePosition.y);

        if (mousePosition == null) return;

        if ((clickCount == 1) && (CornerSelectorCommons.pointROI == null))
            CornerSelectorCommons.pointROI = new PointRoi(pointX, pointY,		// mousePosition.x, mousePosition.y,
                CornerSelectorCommons.getImageWindow().getImagePlus());
        else if ((clickCount == 1)
                 && ((CornerSelectorCommons.pointROI != null)
                     && (CornerSelectorCommons.pointROI.getNCoordinates() < 3)))
                 CornerSelectorCommons.pointROI = CornerSelectorCommons.pointROI.addPoint(pointX,
                 pointY);

        // TODO: When 0 points are defined, clear overlay
        if ((CornerSelectorCommons.pointROI == null)
                || (CornerSelectorCommons.pointROI.getNCoordinates() == 0)) {
            CornerSelectorCommons.getImageWindow().getImagePlus().setOverlay(new Overlay());

            return;
        }

//      String    debugStrings1 = debugPoints(CornerSelectorCommons.pointROI);

        // TODO: When 3 points are defined, calculate the fourth
        if (CornerSelectorCommons.pointROI.getNCoordinates() == 3) {
            BlockROI	newROI = calculateFourthVertex(CornerSelectorCommons.pointROI);

            CornerSelectorCommons.pointROI = newROI;
            setBlockROI(newROI);
            notifyObservers();
        }

        // TODO: When 4 points are defined! We are done.

        // TODO: Finally, update overlay with defined points
//      String    debugStrings2 = debugPoints(CornerSelectorCommons.pointROI);
        Overlay	overlay = new Overlay(CornerSelectorCommons.pointROI);

        overlay.drawNames(true);
        overlay.drawLabels(true);
        CornerSelectorCommons.getImageWindow().getImagePlus().setOverlay(overlay);

        // Testing.zoomCanvas.setOverlay(overlay);
        // Testing.zoomCanvas.getImage().updateAndRepaintWindow();
        // Testing.imageWindow.getImagePlus().updateAndRepaintWindow();

        if (CornerSelectorCommons.pointROI.getNCoordinates() == 4) {

//          GrasppeKit.debugText("Vertex Selection",
//                               "\n\t" + debugStrings1 + "\n\t" + "")"debugStrings2, 3);
            setPatchSetROI(calculateAffineGrid(CornerSelectorCommons.pointROI, 10, 10));
            notifyObservers();
        }

//      setPatchSetROI(calculateAffineGrid(CornerSelectorCommons.pointROI, 10, 10));
    }

    /**
     * @return the blockROI
     */
    protected PointRoi getBlockCornerROI() {
        return blockROI;
    }

    /**
     * Returns a correctly-cast model.
     * @return
     */
    public CornerSelectorModel getModel() {
        return (CornerSelectorModel)model;
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
     * @param blockROI the blockROI to set
     */
    protected void setBlockROI(BlockROI blockROI) {
        getModel().setBlockROI(blockROI);
        this.blockROI = getModel().getBlockROI();
    }

    /**
     * @param patchRadius the patchRadius to set
     */
    protected void setPatchRadius(double patchRadius) {
        this.patchRadius = patchRadius;
    }

    /**
     * 	@param patchSetROI
     */
    protected void setPatchSetROI(PatchSetROI patchSetROI) {
        getModel().setPatchSetROI(patchSetROI);
        this.patchSetROI = getModel().getPatchSetROI();
    }

    /**
     * Class description
     *
     * @version $Revision: 1.0, 11/11/11
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public static class CornerSelectorCommons {

        /** Field description */
        public static KeyboardFocusManager	keyboardFocusManager =
            KeyboardFocusManager.getCurrentKeyboardFocusManager();

        /** Field description */
        public static GrasppeEventDispatcher	keyEventDispatch =
            GrasppeEventDispatcher.getInstance();

        /** Field description */
        public static boolean	isMouseOverImage = false;

        /** Field description */
        public static ImageWindow	imageWindow;

        /** Field description */
        public static JFrame	zoomWindow;

        /** Field description */
        public static MagnifierCanvas	zoomCanvas;

        /** Field description */
        public static PointRoi	pointROI;

        /** Field description */
        public static StopWatch	timer = new StopWatch();

        /** Field description */
        public static String	rootFolder = "/Users/daflair/Documents/data/conres";

        /** Field description */
        public static String	caseFolder = "Approval_Scans_ConRes26_FS";

        /** Field description */
        public static boolean	showZoom = false;

        /** Field description */
        public static boolean	zoomPatch = false;

        /** Field description */
        public static String	imageName = "CirRe27U_50i.tif";		// "CirRe27U_50t.png";

        /** Field description */
        public static String	inputPath = getInputPath();

        /** Field description */
        public static boolean	zoomLock = false;

        /** Field description */
        public static int	magnifyPatchIndex = -1;

        /**
         */
        private CornerSelectorCommons() {}

        /**
         * @return
         */
        public static boolean shouldZoomPatch() {

//          try {         
            if (magnifyPatchIndex > -1) {
                zoomPatch = true;
            }

            boolean	nullROI          = pointROI == null;
            boolean	insufficientROIs = pointROI.getNCoordinates() < 64;

            if (nullROI | insufficientROIs) zoomPatch = false;

//          } catch (Exception exception) {
//            zoomPatch = false;
//          }

            return isZoomPatch();
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
         * @return the image window
         */
        public static ImageWindow getImageWindow() {
            return imageWindow;
        }

        /**
         * @return combined input path
         */
        public static String getInputPath() {
            return (getRootFolder() + "/" + getCaseFolder() + "/"
                    + getImageName()).replaceAll("//", "/");
        }

        /**
         * @return global key event dispatcher
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
         * @return the zoom frame
         */
        public static JFrame getZoomWindow() {
            return zoomWindow;
        }

        /**
         * @return the showZoom
         */
        public static boolean isShowZoom() {

//          if (magnifyPatchIndex>0) {
//              setShowZoom(true);
//              return true;
//          }
            return showZoom;
        }

        /**
         * @return the zoomPatch
         */
        public static boolean isZoomPatch() {
            return zoomPatch;
        }

        /**
         * @param caseFolder    the caseFolder to set
         */
        public static void setCaseFolder(String caseFolder) {
            CornerSelectorCommons.caseFolder = caseFolder;
        }

        /**
         * @param imageName the imageName to set
         */
        public static void setImageName(String imageName) {
            CornerSelectorCommons.imageName = imageName;
        }

        /**
         * @param imageWindow
         */
        public static void setImageWindow(ImageWindow imageWindow) {
            CornerSelectorCommons.imageWindow = imageWindow;
        }

        /**
         * @param rootFolder the rootFolder to set
         */
        public static void setRootFolder(String rootFolder) {
            CornerSelectorCommons.rootFolder = rootFolder;
        }

        /**
         * @param showZoom  the showZoom to set
         */
        public static void setShowZoom(boolean showZoom) {
            CornerSelectorCommons.getZoomWindow().setVisible(true);

//          if (shouldZoomPatch()) {
//            Testing.getZoomWindow().setVisible(true);
//            showZoom = true;
//          }
//          else Testing.getZoomWindow().setVisible(showZoom);
            CornerSelectorCommons.showZoom = showZoom;
        }

        /**
         * @param zoomPatch the zoomPatch to set
         */
        public static void setZoomPatch(boolean zoomPatch) {
            CornerSelectorCommons.zoomPatch = zoomPatch;
        }

        /**
         * @param zoomWindow
         */
        public static void setZoomWindow(JFrame zoomWindow) {
            CornerSelectorCommons.zoomWindow = zoomWindow;
        }
    }
}
