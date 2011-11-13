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
import ij.gui.Roi;

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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.Arrays;
import java.util.HashSet;

import javax.media.jai.WarpPolynomial;

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
    protected static int			exitDelay = 3 * 1000;
    protected static ConResAnalyzer	analyzer;

    /**
     * Method description
     *
     * @param pointROI
     * @param nR
     * @param nC
     */
    public static void calculateAffineGrid(PointRoi pointROI, int nR, int nC) {

        // TODO: ensure pointROI has four vertices

        // TODO: get the convex boundary of the four pointROI
        Polygon		roiPolygon = pointROI.getConvexHull();

        PointRoi	newROI     = new PointRoi(roiPolygon);

        Rectangle	roiBounds  = newROI.getBounds();
        int			tlX        = roiBounds.x;
        int			tlY        = roiBounds.y;
        int			roiBW      = roiBounds.width;
        int			roiBH      = roiBounds.height;
        int			nROI       = newROI.getNCoordinates();
        int[]		roiXs      = newROI.getXCoordinates();
        int[]		roiYs      = newROI.getYCoordinates();
        int[]		roidX      = new int[nROI];
        int[]		roidY      = new int[nROI];
        int[]		roidXY     = new int[nROI];
        int			maxdXY     = 0;
        int			maxdXYi    = 0;
        int			mindXY     = 0;
        int			mindXYi    = 0;
        int			meandX     = 0;
        int			meandY     = 0;

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

        GrasppeKit.debugText("ConvexHull",
                             "\n\t" + debugPoints(newROI) + "\n\t" + debugPoints(sortedROI), 3);

        int	xTL = sortedXs[0];
        int	xTR = sortedXs[1];
        int	xBL = sortedXs[3];
        int	xBR = sortedXs[2];
        int	yTL = sortedYs[0];
        int	yTR = sortedYs[1];
        int	yBL = sortedYs[3];
        int	yBR = sortedYs[2];

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
        float	postScaleX = (float)width;		// ((float)width) postScaleX - The scale factor to apply to X polynomial output.
        float	postScaleY = (float)height;		// ((float)height) postScaleY - The scale factor to apply to the Y polynomial output.

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
                                         2 * numPoints, 1.0F / width, 1.0F / height, (float)width,
                                         (float)height, degree);

        // TODO: mapDestPoint

        /*
         *
         */

        int		nX  = 14;
        int		nY  = 14;
        int		nP  = nX * nY;

        int[]	cXs = new int[nP];
        int[]	cYs = new int[nP];
        int[]	cX5s = new int[nP*5];
        int[]	cY5s = new int[nP*5];
        int[]	cX4s = new int[nP*4];
        int[]	cY4s = new int[nP*4];        

        for (int c = 0; c < nX; c++) {
            float	xc = x0[c];
            float	x1 = xc - dx / 2;
            float	x2 = xc + dx / 2;

            for (int r = 0; r < nY; r++) {
            	
            	// TODO: Map the centers
                float		yr = y0[r];
                float		y1 = yr - dy / 2;
                float		y2 = yr + dy / 2;

                Point2D		PC = new Point2D.Float(xc, yr);

                Point2D		IC = warp.mapDestPoint(PC);

                int			cI = (c * nX) + r;

                GrasppeKit.debugText("Warp Points",
                                     "C" + cI + ": " + GrasppeKit.lastSplit(PC.toString())
                                     + " ==> " + GrasppeKit.lastSplit(IC.toString()), 3);

                cXs[cI] = (int)IC.getX() + tlX;
                cYs[cI] = (int)IC.getY() + tlY;
                
                // Map the vertices (using 4 Point2D instead of Rectangle)
//                Rectangle2D	PR = new Rectangle2D.Float(x1, y1, x2 - x1, y2 - y1);
//                Rectangle2D	IR = warp.mapDestRect(PR.getBounds());
                Point2D[]		PVs = new Point2D.Float[4];
                PVs[0]= new Point2D.Float(x1,y1);
                PVs[1]= new Point2D.Float(x2,y1);
                PVs[2]= new Point2D.Float(x2,y2);
                PVs[3]= new Point2D.Float(x1,y2);
                
                int cI5 = (c*nX*5)+r*5;
                int cI4 = (c*nX*4)+r*4;
                cX5s[cI5] = cXs[cI];
                cY5s[cI5] = cYs[cI];
                for (int v = 0; v<4; v++){
                	Point2D IV = warp.mapDestPoint(PVs[v]);
                	cX4s[cI4+v] = (int) IV.getX() + tlX;
                	cY4s[cI4+v] = (int) IV.getY() + tlY;
                	cX5s[cI5+v+1] = cX4s[cI4+v];
                	cY5s[cI5+v+1] = cY4s[cI4+v];
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

        PointRoi	centerROI = new PointRoi(cXs, cYs, nP);
        PointRoi	vertexROI = new PointRoi(cX4s, cY4s, cX4s.length);
        PointRoi	combinedROI = new PointRoi(cX5s, cY5s, cX5s.length);

        Testing.pointROI = vertexROI; //combinedROI; //centerROI;

        Overlay	overlay = new Overlay(Testing.pointROI);

        overlay.drawNames(true);
        overlay.drawLabels(true);
        Testing.imageWindow.getImagePlus().setOverlay(overlay);

//      float[][] tcoeffs = warp.getCoeffs();
//      int length = tcoeffs[0].length;
//      coeffs = new float[2 * length];
//      for (int i = 0; i < length; i++) {
//          coeffs[i] = tcoeffs[0][i];
//          coeffs[i+length] = tcoeffs[1][i];
//      }

        // TODO: If nothing else works, this is the basis for manual iteration
//      for(int r = 0; r<nR; r++) {
//      for(int c = 0; c<nC; c++) {
//      int sX1 = 0;
//      int sX2 = nC-1;
//      int sXF = c/sX2-sX1;
//      int sY1 = 0;
//      int sY2 = nR-1;
//      int sYF = r/sY2-sY1;
//      int dX1 = 
//      
//      }
//      }

//      
//      
//      PointRoi centerROI; // = new PointRoi();
//      
//      // TODO: determine the normalized grid centers
//      Point2D[][] centers = new Point2D[nR][nC];
//      for(int r = 0; r<nR; r++) {
//      //double x1 = 
//      for(int c = 0; c<nR; c++) {
//          double rx = c+0.5/nC;
//          double ry = r+0.5/nR;
//          centers[r][c] = new Point2D.Double(rx, ry);
////            if (centerROI==null)
////                centerROI = new PointRoi(rx,ry);
//      }
//      }
//      

    }

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
            Point	point1 = points[iA];	// new Point(roiPolygon.xpoints[iA],

            // roiPolygon.ypoints[iA]);
            Point	point2 = points[iB];	// new Point(roiPolygon.xpoints[iB],

            // roiPolygon.ypoints[iB]);

            deltaX   = point1.x - point2.x;
            deltaY   = point1.y - point2.y;
            distance = point1.distance(point2);

            if (distance > longestDistance) {
                pointA = points[iA];	// new Point(roiPolygon.xpoints[iA],

                // roiPolygon.ypoints[iA]);
                pointB = points[iB];	// new Point(roiPolygon.xpoints[iB],

                // roiPolygon.ypoints[iB]);
                pointC = points[iC];	// //new Point(roiPolygon.xpoints[iC],

                // roiPolygon.ypoints[iC]);
                longestDistance = distance;
            }

            GrasppeKit.debugText("Vertex Iteration",
                                 "\t(i:" + i + ")\t" + GrasppeKit.lastSplit(pointC.toString())
                                 + "\t" + GrasppeKit.lastSplit(pointA.toString()) + "\t"
                                 + GrasppeKit.lastSplit(pointB.toString()), 5);
        }

        // TODO: Sort out this mess, make sure it works.

        int			x1     = (int)pointC.x,
					x2     = (int)pointA.x,
					x3     = (int)pointB.x;
        int			x4     = x3 + x2 - x1;		// xB+xA-xC
        int			y1     = (int)pointC.y,
					y2     = (int)pointA.y,
					y3     = (int)pointB.y;
        int			y4     = y3 + y2 - y1;

        PointRoi	newROI = new PointRoi(new int[] { x1, x2, x3, x4 }, new int[] { y1, y2, y3, y4 },
                                       4);

        return newROI;
    }

    /**
     * Method description
     *
     * @param pointROI
     *
     * @return
     */
    public static String debugPoints(PointRoi pointROI) {
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
     * @deprecated Java exits on last window anyway!
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

        /**
         * {@link http
         * ://www.java2s.com/Tutorial/Java/0240__Swing/SpringLayout.htm}
         */

        // SpringLayout layout = new SpringLayout();
        // contentPane.setLayout(layout);

        contentPane.add(zoomCanvas);

        int	padding = 0;

        GrasppeKit.debugText("Zoom Frame", "Content Pane " + contentPane.getBounds().toString(), 3);
        GrasppeKit.debugText("Zoom Frame", "Frame " + Testing.zoomWindow.getBounds().toString(), 3);
        GrasppeKit.debugText("Zoom Frame", "Zoom Canvas " + zoomCanvas.getBounds().toString(), 3);

        // layout.putConstraint(SpringLayout.NORTH, zoomCanvas, padding,
        // SpringLayout.NORTH, contentPane);
        // layout.putConstraint(SpringLayout.EAST, zoomCanvas, padding,
        // SpringLayout.EAST, contentPane);
        // layout.putConstraint(SpringLayout.SOUTH, zoomCanvas, padding,
        // SpringLayout.SOUTH, contentPane);
        // layout.putConstraint(SpringLayout.WEST, zoomCanvas, padding,
        // SpringLayout.WEST, contentPane);

        // zoomCanvas.setSize(contentPane.getWidth() - 2*padding,
        // contentPane.getHeight() - 2*padding);
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
            zoomWindowLocation = zoomWindow.getLocationOnScreen();		// Location

            // on screen
            zoomLocation = zoomCanvas.getLocationOnScreen();	// Location on

            // screen
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

        /*
         * Source Center: pointer location in source coordinate space in source
         * units
         */
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

        Testing.imageName = imageNames[1];
        Testing.keyboardFocusManager.addKeyEventDispatcher(Testing.keyEventDispatch);

        GrasppeKit.debugLevel = 3;

        // testROIFinder();

        // testAnalyzerMenu();
        testImageWindow();
        testImageJMouseListeners();
        testMagnifier();

    }

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
     * Opens an ImagePlus image using Opener and creates and displays it in an
     * ImageWindow;
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

        // imagePlus.getProcessor().autoThreshold(); // 128);
        // Testing.checkTimer("Auto Threshold " + imagePlus.getTitle());

        Testing.imageWindow = new ImageWindow(imagePlus);		// Initialize static

        // variable here
        imageWindow = Testing.imageWindow;
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
        frame.addMouseListener(TestingListeners.IJMouseListener);
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
    public void testROIFinder() {
        Point[]	points = new Point[] { new Point(0, 0), new Point(1, 0), new Point(1, 1),
                                       new Point(0, 1) };

        Point	origin  = new Point(0, 0);
        int		stretch = 1;

        for (int p = 0; p < 4; p++) {

            // TODO: calculate other three points with origin and stretch
            int			pA       = (p + 1) % 4,
						pB       = (p + 2) % 4,
						pC       = (p + 3) % 4;
            Point[]		points2  = new Point[] { points[pA], points[pB], points[pC] };
            int[]		xs       = new int[] { points2[0].x, points2[1].x, points2[2].x };
            int[]		ys       = new int[] { points2[0].y, points2[1].y, points2[2].y };
            PointRoi	pointROI = new PointRoi(xs, ys, 3);

            GrasppeKit.debugText("Points Selection", debugPoints(pointROI), 3);

            PointRoi	newROI = calculateFourthVertex(pointROI);

            GrasppeKit.debugText("Points Determined", debugPoints(newROI), 3);
        }
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

        // boolean keyPressed =
        // MyDispatcher.isPressed({KeyCode.VK_ALT,KeyCode.VK_EQUALS});
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
        
        int newX = 0; int newY = 0;

        // TODO: Add get mouse position relative to canvas
        Point	mousePosition;
        if (Testing.zoomWindow == null | !Testing.zoomWindow.isVisible()) {
        	mousePosition = Testing.imageWindow.getCanvas().getMousePosition();
    		newX = mousePosition.x;
    		newY = mousePosition.y;
    		 if (Testing.pointROI != null) {
    			 newX = Testing.imageWindow.getCanvas().offScreenX(newX);
    			 newY = Testing.imageWindow.getCanvas().offScreenY(newY);
    		 }}
        else {
        	Point mousePosition2 = Testing.zoomCanvas.getMousePosition();
        	Point tlImage = Testing.imageWindow.getCanvas().getLocationOnScreen();
        	Point tlZoom = Testing.zoomCanvas.getLocationOnScreen();
        	newX = tlZoom.x+mousePosition2.x-tlImage.x;
        	newY = tlZoom.y+mousePosition2.y-tlImage.y;      	
	   		 if (Testing.pointROI != null) {
				 newX = Testing.imageWindow.getCanvas().offScreenX(newX);
				 newY = Testing.imageWindow.getCanvas().offScreenY(newY);
			 }
        }
    	mousePosition = new Point(newX, newY);        
        int pointX = newX; //Testing.imageWindow.getCanvas().offScreenX(mousePosition.x);
        int pointY = newY; //Testing.imageWindow.getCanvas().offScreenY(mousePosition.y);


        if (mousePosition == null) return;

        // TODO: Determine whether to add point (click) or clear points (triple
        // click)
        int	clickCount = e.getClickCount();

        if (clickCount == 3) Testing.pointROI = null;

        if ((clickCount == 1) && (Testing.pointROI == null))
            Testing.pointROI = new PointRoi(pointX, pointY, //mousePosition.x, mousePosition.y,
                Testing.imageWindow.getImagePlus());
        else if ((clickCount == 1) && (Testing.pointROI != null))
                 Testing.pointROI = Testing.pointROI.addPoint(pointX, pointY);

        // TODO: When 0 points are defined, clear overlay
        if ((Testing.pointROI == null) || (Testing.pointROI.getNCoordinates() == 0)) {
            Testing.imageWindow.getImagePlus().setOverlay(new Overlay());

            return;
        }

        String	debugStrings1 = debugPoints(Testing.pointROI);

        // TODO: When 3 points are defined, calculate the fourth
        if (Testing.pointROI.getNCoordinates() == 3) {
            PointRoi	newROI = calculateFourthVertex(Testing.pointROI);
            Testing.pointROI = newROI;
        }

        // TODO: When 4 points are defined! We are done.

        // TODO: Finally, update overlay with defined points
        String	debugStrings2 = debugPoints(Testing.pointROI);
        Overlay	overlay       = new Overlay(Testing.pointROI);

        overlay.drawNames(true);
        overlay.drawLabels(true);
        Testing.imageWindow.getImagePlus().setOverlay(overlay);
        Testing.imageWindow.getImagePlus().updateAndRepaintWindow();

        if (Testing.pointROI.getNCoordinates() == 4) {
            GrasppeKit.debugText("Vertex Selection",
                                 "\n\t" + debugStrings1 + "\n\t" + debugStrings2, 3);
            calculateAffineGrid(Testing.pointROI, 10, 10);
        }
    }

    /**
     * Class description
     *
     * @version $Revision: 1.0, 11/11/11
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
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
        	Roi roi = imp.getRoi();
            try {
                Java2.setBilinearInterpolation(g, true);
                Java2.setAntialiased(g, true);

                Image	img = imp.getImage();

                if (img != null) g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                g.drawImage(img, 0, 0, (int)(srcRect.width * magnification),
                            (int)(srcRect.height * magnification), srcRect.x, srcRect.y,
                            srcRect.x + srcRect.width, srcRect.y + srcRect.height, null);
                //if (overlay!=null) drawOverlay(g);
                //if (showAllROIs) drawAllROIs(g);
                //if (roi!=null) drawRoi(roi, g);                

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
     * @version $Revision: 1.0, 11/11/09
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
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
            // if (pressedKeys.isEmpty()) consumedCombination = false;
            processKey(e);

            // if (!consumedCombination && pressedKeys.isEmpty()) {
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
                // consumedCombination = true;
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
     * @version $Revision: 0.1, 11/11/08
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
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
     * @version $Revision: 0.1, 11/11/08
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
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
     * @version $Revision: 0.1, 11/11/08
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
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
     * @version $Revision: 1.0, 11/11/11
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
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
         * Outputs elapsed time with generic description without stopping or
         * reseting the timer.
         *
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
         *
         * @param description
         */
        static void markTimer(String description) {
            checkTimer(description);

            // GrasppeKit.debugText("Elapsed Time",
            // description + " in " + timer.getElapsedTime() + " ms.", 3);
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
         * @param caseFolder
         *            the caseFolder to set
         */
        public static void setCaseFolder(String caseFolder) {
            Testing.caseFolder = caseFolder;
        }

        /**
         * @param imageName
         *            the imageName to set
         */
        public static void setImageName(String imageName) {
            Testing.imageName = imageName;
        }

        /**
         * @param rootFolder
         *            the rootFolder to set
         */
        public static void setRootFolder(String rootFolder) {
            Testing.rootFolder = rootFolder;
        }

        /**
         * @param showZoom
         *            the showZoom to set
         */
        public static void setShowZoom(boolean showZoom) {
            Testing.zoomWindow.setVisible(showZoom);
            Testing.showZoom = showZoom;
        }
    }


    /**
     * Class description
     *
     * @version $Revision: 1.0, 11/11/11
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
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
                if ((visibleFrames == 1) && Testing.zoomWindow.isVisible()) delayedExit();
                if (visibleFrames == 0) delayedExit();
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
                Point	cursorLocation = Testing.imageWindow.getCanvas().getCursorLoc();

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
}
