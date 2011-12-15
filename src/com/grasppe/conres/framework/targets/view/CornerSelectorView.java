/*
 * @(#)CornerSelectorView.java   11/11/08
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets.view;

import com.grasppe.conres.analyzer.view.ConResAnalyzerView;
import com.grasppe.conres.framework.imagej.ImageJPanel;
import com.grasppe.conres.framework.imagej.MagnifierCanvas;
import com.grasppe.conres.framework.targets.CornerSelector;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
import com.grasppe.conres.framework.targets.model.TargetDimensions;
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
import ij.gui.Overlay;
import ij.gui.PointRoi;

import ij.plugin.PlugIn;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.media.jai.WarpPolynomial;

import javax.naming.directory.InvalidAttributesException;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */

/**
 * @author daflair
 */
public class CornerSelectorView extends AbstractView
        implements PlugIn, GrasppeEventHandler, MouseListener, MouseWheelListener,
                   MouseMotionListener {

    protected static String	name = CornerSelectorView.class.getSimpleName();

    /** ImageWindow with scaled down image */
    public JPanel	viewContainer;

    /** Field description */
    public ImageJPanel	imagePanel;

    /** ZoomWindow with enlarged image at center relative ImageWindow position */
    public MagnifierCanvas	zoomCanvas;

    /** Field description */
    public JFrame	zoomWindow;

    /** ZoomWindow handling functions */
    public boolean	showZoom = false;

    /** Field description */
    public boolean	isMouseOverImage = false;

    /** Field description */
    public boolean	zoomLock = false;

    /** Field description */
    public boolean	zoomPatch = false;
    int				dbg       = 0;

    /**
     * @param controller
     */
    public CornerSelectorView(CornerSelector controller) {
        super(controller);
    }

    /**
     *  @param a
     *  @param b
     *  @return
     */
    private Point add(Point a, Point b) {
        return new Point(round(a.getX() + b.getX()), round(a.getY() + b.getY()));
    }

    /**
     *  @param point
     */
    private void addBlockVertex(Point point) {
        addBlockVertex(round(point.getX()), round(point.getY()));
    }

    /**
     *  @param pointX
     *  @param pointY
     */
    private void addBlockVertex(int pointX, int pointY) {
        int	dbg = 2;

        try {
            if (blockPointCount() == 0) {
                BlockROI	blockROI = new BlockROI(pointX, pointY);

                getModel().setBlockROI(blockROI);
            } else if (blockPointCount() < 3)
                       getModel().setBlockROI(getModel().getBlockROI().addPoint(pointX, pointY));
            else return;

            GrasppeKit.debugText("CornerSelector - Add Point",
                                 pointString(getModel().getBlockROI()), dbg);

            setOverlayROI(getModel().getBlockROI());

            notifyObservers();
        } catch (Exception exception) {
            GrasppeKit.debugError("Adding Block Vertex",
                                  new Exception("Could not add block vertex at " + pointX + ", "
                                      + pointY + ". ", exception), 3);
            GrasppeKit.debugError("Adding Block Vertex", exception, 3);
        }
    }

    /**
     *  @param a
     *  @param b
     *  @return
     */
    private Point addPoint(Point a, Point b) {
        a.setLocation(a.getX() + b.getX(), a.getY() + b.getY());

        return a;
    }

    /**
     */
    public void attachMouseListeners() {
        getImageCanvas().addMouseListener(this);
        getImageCanvas().addMouseMotionListener(this);
        getImageCanvas().addMouseWheelListener(this);
    }

    /**
     *  @return
     */
    private int blockPointCount() {
        BlockROI	blockROI = getModel().getBlockROI();

        if (blockROI == null) return 0;
        else return blockROI.getNCoordinates();
    }

    /**
     */
    public void calculateAffineGrid() {		// PointRoi pointROI, int nR, int nC) {

        int	dbg = 0;

        try {
            sortROIs();		// cornerSelectorCommons.overlayROI);
            warpPatchGrid();
        } catch (InvalidAttributesException exception) {
            GrasppeKit.debugText("CornerSelector - Calculate Grid", exception.getMessage(), dbg);
        }

        setOverlayROI(getModel().getPatchSetROI());
        getModel().setMagnifyPatchIndex(0);

        if (getModel().isValidSelection()) getTargetManager().savePatchCenterROIs(getController());

    }

    /**
     */
    public void calculateFourthVertex() {		// PointRoi pointROI) {
        PointRoi	pointROI = getModel().getBlockROI();

        if (pointROI.getNCoordinates() != 3) return;

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

        getModel().setBlockROI(newROI);

        setOverlayROI(getModel().getBlockROI());

    }

    /**
     * @return
     */
    public boolean canMagnifyPatch() {
        try {
            return shouldZoomPatch();

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
     */
    public void clearBlockPoints() {
        CornerSelectorModel	model = getModel();

        model.setBlockROI(null);
        model.setPatchSetROI(null);
        setOverlayROI(null);
        notifyObservers();
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
    @Override
    protected void finalizeUpdates() {

        // TODO Auto-generated method stub

    }

    /**
     */
    public void magnifyLastPatch() {
        try {
            if (!canMagnifyPatch()) {
                getModel().setMagnifyPatchIndex(-1);

                return;
            }

            int	pI = getModel().getMagnifyPatchIndex();

            pI--;

            pI %= (getModel().getOverlayROI().getNCoordinates());

            getModel().setMagnifyPatchIndex(pI);

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
                getModel().setMagnifyPatchIndex(-1);

                return;
            }

            int	pI = getModel().getMagnifyPatchIndex();
            int	nI = getModel().getOverlayROI().getNCoordinates();

            pI++;

            pI %= nI;

            getModel().setMagnifyPatchIndex(pI);

        } catch (Exception exception) {
            return;
        }

        updateMagnifyPatch();

    }

    /**
     * @param e
     */
    public void mouseClicked(MouseEvent e) {

        if (e.isConsumed()) return;
        if (getImageWindow() == null) return;
        if (!getImageWindow().isVisible()) return;

        GrasppeKit.debugText("CornerSelector - Click Source",
                             e.getSource().getClass().getSimpleName(), dbg);
        CornerSelectorListeners.debugEvent(this, "CornerSelectorView", "Click", e, dbg);

        int	clickCount = e.getClickCount();

        if (!(e.getSource() == getImageCanvas())) return;		// (e.getSource() == getZoomCanvas())

        // TODO: Add points and calculate grid when possible
        try {
            switch (clickCount) {

            case 2 :	// TODO: Clear points
                clearBlockPoints();
                break;

            case 1 :	// TODO: Add points 1, 2, or 3 and when possible calculate 4 and then the grid

//              PatchSetROI patchSetROI =  getModel().getPatchSetROI();
                if ((getModel().getPatchSetROI() != null)
                        && (getModel().getPatchSetROI().getNCoordinates() > 0))
                    break;
                if (blockPointCount() < 3) addBlockVertex(getImageCoordinates());
                if (blockPointCount() == 3) calculateFourthVertex();
                if (blockPointCount() == 4) calculateAffineGrid();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            GrasppeKit.debugText("CornerSelector - Add Point", exception.getMessage(), dbg);
        }

        e.consume();
        update();

    }

    /**
     *  @param e
     */
    public void mouseDragged(MouseEvent e) {
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseEntered(MouseEvent e) {
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseExited(MouseEvent e) {
        redrawFrame();
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseMoved(MouseEvent e) {
        moveFrame(e.getXOnScreen(), e.getYOnScreen());
        e.consume();
    }

    /**
     *  @param e
     */
    public void mousePressed(MouseEvent e) {

//      CornerSelectorListeners.debugEvent(this, "IJMouseListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseReleased(MouseEvent e) {

//      CornerSelectorListeners.debugEvent(this, "IJMouseListener", e);
        e.consume();
    }

    /**
     *  @param e
     */
    public void mouseWheelMoved(MouseWheelEvent e) {

//      CornerSelectorListeners.debugEvent(this, "IJWheelListener", e);
        e.consume();
    }

    /**
     * @param x
     * @param y
     */
    public void moveFrame(int x, int y) {

//
//      if (zoomLock) return;
//
//      if (canMagnifyPatch()) {
//          if (getZoomWindow() == null) return;
//
//          JFrame        zoomWindow  = getZoomWindow();
//          ImageWindow   viewContainer = getImageWindow();
//          PointRoi  pointROI    = getModel().getOverlayROI();
//
//          isMouseOverImage = viewContainer.getCanvas().getBounds().contains(x - viewContainer.getX(),
//                  y - viewContainer.getY());
//
//          int   mX = 100;       // viewContainer.getCanvas().
//          int   mY = 100;
//
//          // zoomWindow.setLocation(xCenter, yCoordinates);
//
//          zoomWindow.setLocation(x - zoomWindow.getWidth() / 2, y - zoomWindow.getHeight() / 2);
//
//          zoomLock = true;
//
//          redrawFrame();
//
//          return;
//      } else {
//
//          updateMagnifier();
//
//          if (getZoomWindow() == null) return;
//
//          JFrame        zoomWindow  = getZoomWindow();
//          ImageWindow   viewContainer = getImageWindow();
//
//          isMouseOverImage = viewContainer.getCanvas().getBounds().contains(x - viewContainer.getX(),
//                  y - viewContainer.getY());
//
//          // Sets visible and returns updated zoomWindow.isVisible()
//
//          zoomWindow.setLocation(x - zoomWindow.getWidth() / 2, y - zoomWindow.getHeight() / 2);
//
//          if (!isShowZoom()) return;
//
//          redrawFrame();
//      }
    }

    /**
     * 	@param keyCode
     * 	@param keyModifiers
     * 	@return
     */
    private boolean nudgeROI(KeyEvent keyEvent) { //int keyCode, int keyModifiers) {
    	int dbg = 2;
    	int keyCode = keyEvent.getKeyCode();
    	int offset = 3;
    	if ((keyCode != KeyEvent.VK_UP) && (keyCode != KeyEvent.VK_DOWN)
    			&& (keyCode != KeyEvent.VK_LEFT) && (keyCode != KeyEvent.VK_RIGHT))
    		return false;
    	if (keyEvent.isAltDown()) { //return false;
            getModel().getPatchSetROI().nudgeCorner(keyCode);
    	} else if (keyEvent.isShiftDown()) {
    		Rectangle bounds = getModel().getPatchSetROI().getBounds();
	    		switch(keyCode) {
				case KeyEvent.VK_UP:
					getModel().getPatchSetROI().setLocation(bounds.x, bounds.y-offset);
					break;
				case KeyEvent.VK_DOWN:
					getModel().getPatchSetROI().setLocation(bounds.x, bounds.y+offset);
					break;
				case KeyEvent.VK_LEFT:
					getModel().getPatchSetROI().setLocation(bounds.x-offset, bounds.y);
					break;
				case KeyEvent.VK_RIGHT:
					getModel().getPatchSetROI().setLocation(bounds.x+offset, bounds.y);
					break;
			}
    	}
		else return false;
        setOverlayROI(getModel().getPatchSetROI());
        notifyObservers();
        if (getModel().isValidSelection()) getTargetManager().savePatchCenterROIs(getController());
        return true;
    }

    /**
     * @param pointROI
     * @return
     */
    public String pointString(PointRoi pointROI) {

        if (pointROI == null) return "";

        try {
            Polygon	polygon       = pointROI.getPolygon();

            int[]	xPoints       = polygon.xpoints;	// pointROI.getXCoordinates();
            int[]	yPoints       = polygon.ypoints;	// pointROI.getYCoordinates();

            int		nPoints       = polygon.npoints;

            String	strPoints     = "";
            String	strPointCount = "Points: " + nPoints;

            int		nShow         = 5;

            for (int i = 0; i < nPoints; i++)
                if ((i == nShow) && (nPoints > nShow + 1)) {
                    strPoints += "... ";
                    i         = nPoints - 2;
                } else if (i == nPoints - 1)
                           strPoints += "(" + xPoints[i] + ", " + yPoints[i] + ")";
                else strPoints += "(" + xPoints[i] + ", " + yPoints[i] + "), ";

            return (strPointCount + "\t" + strPoints);
        } catch (Exception exception) {
            return "";
        }
    }

    /**
     * Opens an ImagePlus image using Opener and creates and displays it in an
     * ImageWindow;
     *  @param imageFile
     */
    public void prepareImageWindow(ImageFile imageFile) {

        try {
            ImagePlus	imagePlus = getModel().getImagePlus();


            imagePanel = new ImageJPanel(imagePlus);
            imagePanel.setFitImage(true);
            imagePanel.setBounds(0, 0, 120, 120);
            imagePanel.setBackground(Color.DARK_GRAY);

            viewContainer = new JPanel(new BorderLayout());
            viewContainer.setBackground(Color.DARK_GRAY);
            viewContainer.add(imagePanel, BorderLayout.CENTER);
            viewContainer.addComponentListener(imagePanel);

            getParentView().setContainer(viewContainer);

            imagePanel.updateSize();

            KeyListener	keyListener = new KeyAdapter() {

                public void keyPressed(KeyEvent ke) {
                    if (ke.isConsumed()) return;
                    if (nudgeROI(ke))ke.consume(); //.getKeyCode(), ke.getModifiers())) 
                }
            };

            viewContainer.addKeyListener(keyListener);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     */
    public void prepareMagnifier() {

//      prepareMagnifierFrame();
//
//      JFrame                frame          = getZoomWindow();
//      ImageWindow           viewContainer    = getImageWindow();
//      MouseMotionListener   motionListener = this;      // CornerSelectorListeners.IJMotionListener;
//
//      frame.addMouseMotionListener(motionListener);
//      frame.addMouseListener(this);
//      viewContainer.getCanvas().addMouseMotionListener(motionListener);
//
//      /* Static Updates */
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

        MagnifierCanvas	zoomCanvas = new MagnifierCanvas(getImagePlus());

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

        GrasppeKit.debugText("Zoom Frame", "Content Pane " + contentPane.getBounds().toString(),
                             dbg);
        GrasppeKit.debugText("Zoom Frame", "Frame " + getZoomWindow().getBounds().toString(), dbg);
        GrasppeKit.debugText("Zoom Frame", "Zoom Canvas " + zoomCanvas.getBounds().toString(), dbg);

        double	windowZoom = getImageCanvas().getMagnification();

        zoomCanvas.setMagnification(windowZoom * 2.0);

        getZoomWindow().setVisible(isShowZoom());

        getZoomWindow().pack();
        getZoomWindow().setSize(300, 300);

        zoomCanvas = zoomCanvas;

    }

    /**
     */
    public void redrawFrame() {

//      if (getZoomWindow() == null) return;
//
//      if (!getZoomWindow().isVisible()) return;     // {Testing.zoomWindow.setVisible(false); return;}
//
//      ImageWindow   viewContainer  = getImageWindow();
//      ImageCanvas   imageCanvas  = viewContainer.getCanvas();
//      JFrame        zoomWindow   = getZoomWindow();
//
//      Point     zoomWindowLocation,
//                zoomLocation = null;
//
//      if (zoomWindow.isVisible()) {
//          zoomWindowLocation = zoomWindow.getLocationOnScreen();        // Location
//          zoomLocation       = zoomCanvas.getLocationOnScreen();        // Location on
//      }
//
//      Point zoomPosition = zoomCanvas.getLocation();    // Position within
//
//      // component
//      Dimension zoomSize            = zoomCanvas.getSize();
//      Double        zoomScale           = zoomCanvas.getMagnification();
//
//      Point     imageWindowLocation = viewContainer.getLocationOnScreen();
//      Point     imageLocation       = imageCanvas.getLocationOnScreen();
//      Point     imagePosition       = imageCanvas.getLocation();
//      Dimension imageSize           = imageCanvas.getSize();
//      Double        imageScale          = imageCanvas.getMagnification();
//
//      Point     pointerCenter       = null,
//                sourceCenter        = null;
//
//      try {
//          if (canMagnifyPatch()) {
//              int           pI        = getModel().getMagnifyPatchIndex();
//
//              PointRoi  pointROI  = getModel().getOverlayROI();
//
//              int           pX        = pointROI.getXCoordinates()[pI];
//              int           pY        = pointROI.getYCoordinates()[pI];
//
//              Rectangle roiBounds = pointROI.getBounds();
//
//              double        rX        = roiBounds.getX();
//              double        rY        = roiBounds.getY();
//
//              int           fX        = round(rX + pX);
//              int           fY        = round(rY + pY);
//
//              pointerCenter = new Point(fX, fY);
//
//              sourceCenter  = pointerCenter;
//
//              String    debugString = "Patch #" + pI + "-"
//                                   + GrasppeKit.lastSplit(pointerCenter.toString());
//
//              GrasppeKit.debugText("Magnifier", debugString, dbg);
//
//          } else {
//
//              /* Pointer Center: pointer location in image canvas in screen units */
//              if (zoomWindow.isVisible()) {
//                  pointerCenter = zoomCanvas.getMousePosition();
//                  pointerCenter = new Point(zoomLocation.x - imageLocation.x + pointerCenter.x,
//                                            zoomLocation.y - imageLocation.y + pointerCenter.y);
//              } else
//                  pointerCenter = imageCanvas.getMousePosition();
//
//              sourceCenter = new Point(round(pointerCenter.x / imageScale),
//                                       round(pointerCenter.y / imageScale));
//
//          }
//      } catch (Exception exception) {
//          zoomWindow.setVisible(false);
//          pointerCenter = null;
//      }
//
//      if (pointerCenter == null) return;
//
//      // if (!zoomWindow.isVisible()) zoomWindow.setVisible(true);
//
//      /*
//       * Source Center: pointer location in source coordinate space in source
//       * units
//       */
//
//      /* Source Size: source rectangle size in source units */
//      Dimension sourceSize = new Dimension(round(zoomSize.width / zoomScale),
//                                 round(zoomSize.height / zoomScale));
//
//      /* Source Corner: source rectangle top-left in source units */
//      Point sourceCorner = new Point((sourceCenter.x - sourceSize.width / 2),
//                                     (sourceCenter.y - sourceSize.height / 2));
//
//      /* Source Rectangle: source rectangle in source units */
//      Rectangle sourceRectangle = new Rectangle(sourceCorner, sourceSize);
//
//      String        debugString     = "";
//
//      debugString += "\t" + GrasppeKit.lastSplit(pointerCenter.toString());
//      debugString += "\t" + GrasppeKit.lastSplit(zoomSize.toString());
//      debugString += "\t" + GrasppeKit.lastSplit(sourceRectangle.toString());
//
//      GrasppeKit.debugText("Magnifier", debugString, dbg);
//
//      zoomCanvas.setSourceRect(sourceRectangle);
//
//      zoomCanvas.repaint(0, 0, zoomSize.width, zoomSize.height);

    }

    /**
     *  @param value
     *  @return
     */
    public int round(double value) {
        return (int)Math.round(value);
    }

    /**
     *  @param value
     *  @return
     */
    public int round(float value) {
        return (int)Math.round(value);
    }

    /**
     *  @param value
     *  @return
     */
    public int round(int value) {
        return value;
    }

    /**
     * @param arg
     */

    public void run(String arg) {

        if (getController().getBlockImage() == null) return;

//      ImageWindow   viewContainer = getImageWindow();

        if (viewContainer != null) {		// && viewContainer.isVisible()) {
            viewContainer.setVisible(false);

//          viewContainer.dispose();
        } else {
            GrasppeEventDispatcher	eventDispatcher = GrasppeEventDispatcher.getInstance();

            eventDispatcher.attachEventHandler(this);
        }

        getTargetManager().loadImage();

        ImageFile	blockImageFile = getController().getBlockImage();

        prepareImageWindow(blockImageFile);

        // viewContainer = getImageWindow();

        attachMouseListeners();
        setVisible(true);

//      prepareMagnifier();

    }

    /**
     *         @return
     */
    public boolean shouldZoomPatch() {

        // try {
        if (getModel().getMagnifyPatchIndex() > -1) {
            zoomPatch = true;
        }

        boolean	nullROI          = getModel().getOverlayROI() == null;
        boolean	insufficientROIs = getModel().getOverlayROI().getNCoordinates() < 64;

        if (nullROI | insufficientROIs) zoomPatch = false;

        // } catch (Exception exception) {
        // zoomPatch = false;
        // }

        return isZoomPatch();
    }

    /**
     *  @throws InvalidAttributesException
     */
    private void sortROIs( /* PointRoi pointROI */) throws InvalidAttributesException {

//      cornerSelectorCommons.sortedROI = null;

        PointRoi	pointROI = getModel().getBlockROI();

        if (pointROI == null) return;

        // TODO: get the convex boundary of the four pointROI
        Polygon	roiPolygon = pointROI.getConvexHull();

//      PointRoi  newROI     = new PointRoi(roiPolygon);

        int[]	roiXs    = roiPolygon.xpoints,
				roiYs    = roiPolygon.ypoints;

        int[]	sortedIs = getController().sortRectangleROIIndex(roiXs, roiYs);		// new int[4];
        int[]	sortedXs = new int[4];
        int[]	sortedYs = new int[4];

        for (int p = 0; p < 4; p++) {
            int	i = sortedIs[p];

            sortedXs[p] = roiXs[i];
            sortedYs[p] = roiYs[i];
        }

        PointRoi	sortedROI = new PointRoi(sortedXs, sortedYs, 4);

        getModel().setBlockROI(new BlockROI(sortedROI.getPolygon()));

    }

    /**
     *  @param a
     *  @param b
     *  @return
     */
    private Point subtract(Point a, Point b) {
        return new Point(round(a.getX() - b.getX()), round(a.getY() - b.getY()));
    }

    /**
     *  @param a
     *  @param b
     *  @return
     */
    private Point subtractPoint(Point a, Point b) {
        a.setLocation(a.getX() - b.getX(), a.getY() - b.getY());

        return a;
    }

    /**
     *  @throws Throwable
     */
    private void terminate() throws Throwable {
        JFrame	zoomWindow = getZoomWindow();

//      ImageWindow   viewContainer = getImageWindow();

        if ((zoomWindow != null) && zoomWindow.isDisplayable()) zoomWindow.dispose();

//      if ((viewContainer != null) && viewContainer.isDisplayable()) viewContainer.dispose();
        super.notifyObservers();
        super.finalize();
    }

    /**
     */
    @Override
    protected void updateDebugLabels() {
        CornerSelectorModel	model = getModel();

        if (model == null) return;

        updateDebugLabel("blockROI", pointString(model.getBlockROI()));
        updateDebugLabel("patchSetROI", pointString(model.getPatchSetROI()));
        updateDebugLabel("overlayROI", pointString(model.getOverlayROI()));

        updateDebugLabel("imageFile", model.getTargetImageFile());
        updateDebugLabel("imageDimensions", model.getImageDimensions());

        updateDebugLabel("targetDefinitionFile", model.getTargetDefinitionFile());
        updateDebugLabel("targetDimensions", model.getTargetDimensions());

        if (model.getMagnifyPatchIndex() > -1)
            updateDebugLabel("magnifyPatchIndex", model.getMagnifyPatchIndex());
        else updateDebugLabel("magnifyPatchIndex", "");

        super.updateDebugLabels();
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
            double	minScale     = getImageCanvas().getMagnification();

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
        boolean	mouseOver  = isMouseOverImage;

        setShowZoom(keyPressed && mouseOver);

        // if (!canMagnifyPatch()) Testing.setShowZoom(keyPressed && mouseOver);

    }

    /**
     */
    public void updateMagnifyPatch() {
        try {
            if (!canMagnifyPatch()) {
                getModel().setMagnifyPatchIndex(-1);

                return;
            }

            int			pI        = getModel().getMagnifyPatchIndex();

            PointRoi	pointROI  = getModel().getOverlayROI();

            int			pX        = pointROI.getXCoordinates()[pI];
            int			pY        = pointROI.getYCoordinates()[pI];

            Rectangle	roiBounds = pointROI.getBounds();

            double		rX        = roiBounds.getX();
            double		rY        = roiBounds.getY();

            int			fX        = round(rX + pX);
            int			fY        = round(rY + pY);

            int			gX        = getImageCanvas().screenX(fX);
            int			gY        = getImageCanvas().screenX(fY);

            GrasppeKit.debugText("ROI Bounds:\tr: (" + roiBounds.getX() + ", " + roiBounds.getY()
                                 + ")\tp: (" + pX + ", " + pY + ")\tf: (" + fX + ", " + fY
                                 + ")\tg: (" + gX + ", " + gY + ")", dbg);

            shouldZoomPatch();

            setShowZoom(true);

            zoomLock = false;

            moveFrame(gX, gY);

        } catch (Exception exception) {
            return;
        }
    }

    /**
     */
    @SuppressWarnings("restriction")
    private void warpPatchGrid() {

        int	dbg = 0;

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
        
        TargetDimensions	targetDimensions;

        try {
        	targetDimensions = getTargetManager().getModel().getActiveTarget().getDimensions();
        	getModel().setTargetDimensions(targetDimensions);
        } catch (Exception exception) {
        	GrasppeKit.debugError("Reading Target Dimensions", exception,2);
        	return;
        }
        WarpPolynomial		warp;

        // Determine the points needed
        int	warpDegree = 1;		// degree - The desired degree of the warp polynomials.
        int	warpPoints = (warpDegree + 1) * (warpDegree + 2) / 2;		// 3; // pointsNeeded = (degree + 1) * (degree + 2) / 2;

//      float spanX      = targetDimensions.getXSpan();
        float[]	centerXs = targetDimensions.getXCenters();

//      float repeatX    = targetDimensions.getXRepeat();
        float[]	boundsX = targetDimensions.getXBounds();

//      float sizeX      = boundsX[1] - boundsX[0];

//      float spanY      = targetDimensions.getYSpan();
        float[]	centerYs = targetDimensions.getYCenters();

//      float repeatY    = targetDimensions.getYRepeat();
        float[]	boundsY = targetDimensions.getYBounds();

//      float sizeY      = boundsY[1] - boundsY[0];

        int	stepsX  = targetDimensions.getXCount();		// 14;
        int	stepsY  = targetDimensions.getYCount();		// 14;
        int	stepsXY = stepsX * stepsY;

        // Warp Destination Arguments
        int		destOffset   = 0;		// destOffset - The initial entry of destCoords to be used.
        float[]	targetCoords = new float[] {	// destCoords - An array of floats containing the destination coordinates with X and Y alternating.
            boundsX[0], boundsY[0], boundsX[1], boundsY[0], boundsX[1], boundsY[1], boundsX[0],
            boundsY[1]
        };

        // Warp Source Arguments
        BlockROI	sortedROI    = getModel().getBlockROI();
        int[]		sortedXs     = sortedROI.getPolygon().xpoints;
        int[]		sortedYs     = sortedROI.getPolygon().ypoints;

        int			sourceOffset = 0;		// sourceOffset - the initial entry of sourceCoords to be used.
        int[]		s            = { 0, 1, 2, 3 };
        float[]		imageCoords  = new float[] {	// sourceCoords - An array of floats containing the source coordinates with X and Y alternating.
            sortedXs[s[0]], sortedYs[s[0]], sortedXs[s[1]], sortedYs[s[1]], sortedXs[s[2]],
            sortedYs[s[2]], sortedXs[s[3]], sortedYs[s[3]]
        };

        float	preScale  = 1.0F;		// 2.0F
        float	postScale = 1.0F / preScale;

        warp = WarpPolynomial.createWarp(imageCoords, sourceOffset, targetCoords, destOffset,
                                         2 * warpPoints, preScale, preScale, postScale, postScale,		// 1.0F / sizeX, 1.0F / sizeY, sizeX, sizeY,
                                         warpDegree);

        int[]	cXs = new int[stepsXY];
        int[]	cYs = new int[stepsXY];

        for (int c = 0; c < stepsX; c++) {
            float	xc = centerXs[c];

            for (int r = 0; r < stepsY; r++) {

                // TODO: Map the centers
                float	yr = centerYs[r];

                int		cI = (c * stepsY) + r;

                Point2D	PC = new Point2D.Float(xc, yr);
                Point2D	IC = warp.mapDestPoint(PC);

                cXs[cI] = round(IC.getX());
                cYs[cI] = round(IC.getY());

                if ((cI < 3) || (cI > stepsXY - 3))
                    GrasppeKit.debugText("Warp Points",
                                         "C" + cI + ": " + GrasppeKit.lastSplit(PC.toString())
                                         + " ==> " + GrasppeKit.lastSplit(IC.toString()), dbg);

            }
        }

        getModel().setPatchSetROI(new PatchSetROI(cXs, cYs, stepsXY));
    }

    /**
     * @return the blockROI
     */
    protected PointRoi getBlockCornerROI() {
        return getModel().getBlockROI();	// blockROI;
    }

    /**
     *  @return
     */
    public CornerSelector getController() {
        return (CornerSelector)controller;
    }

    /**
     *  @return
     */
    public ImageCanvas getImageCanvas() {
        if (getImagePanel() == null) return null;

        return getImagePanel().getImageCanvas();
    }

    /**
     *  @return
     */
    private Point getImageCoordinates() {
        if ((getImageWindow() == null) ||!getImageWindow().isVisible()) return null;

        Point	mousePosition = null;

        try {

//          if ((getZoomWindow() != null) && getZoomWindow().isVisible())
//              mousePosition = add(getZoomCanvas().getMousePosition(),
//                                  subtract(getZoomCanvas().getLocationOnScreen(),
//                                           getImageCanvas().getLocationOnScreen()));
//          else mousePosition = getImageCanvas().getMousePosition();
            mousePosition = getImageCanvas().getMousePosition();

            mousePosition.setLocation(getImageCanvas().offScreenX(round(mousePosition.getX())),
                                      getImageCanvas().offScreenY(round(mousePosition.getY())));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return mousePosition;
    }

    /**
     * @return the imagePanel
     */
    public ImageJPanel getImagePanel() {
        return imagePanel;
    }

    /**
     *  @return
     */
    private ImagePlus getImagePlus() {
        if (getImageCanvas() == null) return null;

        return getImageCanvas().getImage();
    }

    /**
     * @return the image window
     */
    public JPanel getImageWindow() {
        if (viewContainer == null) run(null);		// prepareImageWindow(getController().getBlockImage());

        return viewContainer;
    }

    /**
     * Returns a correctly-cast model.
     * @return
     */
    public CornerSelectorModel getModel() {
        return (CornerSelectorModel)super.getControllerModel();
    }

    /**
     * 	@return
     */
    public ConResAnalyzerView getParentView() {
        if (getController() == null) return null;
        if (getController().getAnalyzer() == null) return null;

        return getController().getAnalyzer().getView();
    }

    /**
     * @return the patchSetROI
     */
    protected PatchSetROI getPatchSetROI() {
        return getModel().getPatchSetROI();
    }

    /**
     *  @return
     */
    public TargetManager getTargetManager() {
        return getController().getTargetManager();
    }

    /**
     *  @return
     */
    private MagnifierCanvas getZoomCanvas() {
        if (getZoomWindow() == null) return null;

        return zoomCanvas;
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
     * @return the zoomPatch
     */
    public boolean isZoomPatch() {
        return zoomPatch;
    }

    /**
     * @param imagePanel the imagePanel to set
     */
    public void setImagePanel(ImageJPanel imagePanel) {
        this.imagePanel = imagePanel;
    }

    /**
     *  @param pointROI
     */
    public void setOverlayROI(PointRoi pointROI) {
        getModel().setOverlayROI(pointROI);
        if (getImagePlus() == null) return;

        // TODO: Update overlay with defined points
        try {

            if (getModel().getOverlayROI() != null) {

                Polygon	roiPolygon     = getModel().getOverlayROI().getPolygon();

                Polygon	overlayPolygon = new Polygon();

                for (int i = 0; i < roiPolygon.npoints; i++)
                    overlayPolygon.addPoint(getImageCanvas().screenX(roiPolygon.xpoints[i]),
                                            getImageCanvas().screenY(roiPolygon.ypoints[i]));

                // roiPolygon.
                PointRoi	overlayROI = new PointRoi(overlayPolygon);

                overlayROI.setStrokeWidth(2);
                overlayROI.setFillColor(Color.DARK_GRAY);
                overlayROI.setStrokeColor(Color.GREEN);
                overlayROI.setColor(Color.WHITE);

                Overlay	overlay = new Overlay(overlayROI);		// getModel().getOverlayROI());

//              overlay.setLabelFont(new Font("SansSerif", Font.BOLD, 32)); //overlay.getLabelFont().deriveFont(14.0F));
                overlay.drawNames(true);
                overlay.drawLabels(true);
                getImagePlus().setHideOverlay(false);		// getImageWindow().repaint();

                getImageCanvas().setOverlay(overlay);		// getModel().getOverlayROI(), Color.RED, 2, Color.WHITE); //overlay);

            } else {
                getImageCanvas().setOverlay(null);

            }

            getImageCanvas().setImageUpdated();
            getImageCanvas().repaint();

            GrasppeKit.debugText("CornerSelector - Draw Overlay",
                                 pointString(getModel().getOverlayROI()), dbg);

        } catch (Exception exception) {
            exception.printStackTrace();
            GrasppeKit.debugText("CornerSelector - Draw Overlay", exception.getMessage(), dbg);
        }
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
     * 	@param visible
     */
    public void setVisible(boolean visible) {
        if (viewContainer != null) viewContainer.setVisible(visible);
    }

    /**
     * @param zoomPatch the zoomPatch to set
     */
    public void setZoomPatch(boolean zoomPatch) {
        zoomPatch = zoomPatch;
    }

    /**
     * @param zoomWindow
     */
    public void setZoomWindow(JFrame zoomWindow) {
        this.zoomWindow = zoomWindow;
    }
}
