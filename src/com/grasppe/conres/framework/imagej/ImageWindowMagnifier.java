/*
 * @(#)ImageWindowMagnifier.java   11/11/17
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.imagej;

import com.grasppe.lure.components.ObservableObject;
import com.grasppe.lure.framework.GrasppeEventDispatcher;
import com.grasppe.lure.framework.GrasppeEventHandler;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.KeyCode;
import com.grasppe.lure.framework.GrasppeKit.Observable;

import ij.IJ;
import ij.ImagePlus;

import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;

import ij.util.Java2;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

/**
 * @author daflair
 */
public class ImageWindowMagnifier extends ImageCanvas
        implements MouseMotionListener, MouseListener, WindowFocusListener, GrasppeEventHandler {		// , MouseListener {

//  /** Field description */
//  public static MouseMotionListener mouseMotionListener = new MouseMotionListener() {
//
//      @Override
//      public void mouseMoved(MouseEvent e) {
//        
//        //if(e.getSource().equals(this))
//            
//
////        ConResBootCamp.moveFrame(e.getXOnScreen(), e.getYOnScreen());
////          debugEvent("IJMotionListener", e);
////          e.consume();
//      }
//
//      @Override
//      public void mouseDragged(MouseEvent e) {}
//  };

    /**
     */
    private static final long	serialVersionUID = 1L;

    /** Window of the magnifier */
    protected JFrame	magnifierFrame = null;

    /** ImageWindow of the source image */
    protected ImageWindow	imageWindow = null;

    /** ImageCanvas of the source image */
    protected ImageCanvas	imageCanvas = null;

    /** ImagePlus of the source image */
    protected ImagePlus	imagePlus = null;

    /** Image pixel coordinates at the centre of the magnifier */
    protected Point	magnifierPosition = null;

    /** Magnifier center on screen in screen pixels */
    protected Point	positionOnScreen = null;

    /** Magnifier center in image pixels */
    protected Point	positionOnImage = null;

    /** Image pixel dimension of the magnifier */
    protected Dimension	magnifierSize = null;

    /** Screen pixel coordinates at the centre of the magnifier frame */
    protected Point	framePosition = null;

    /** Screen pixel dimension of the magnifier */
    protected Dimension	frameSize = null;

    /** Magnification level of magnifier canvas */

    // protected double  magnification = 1.0;

    /** Magnification level of image canvas */
    protected double	imageMagnification = 1.0;

    /** Zoom in exclusively when the defined key code & modifiers are pressed */
    protected boolean	zoomInKeyExclusive = false;

    /** Zoom in when the keys and/or modifiers are pressed */
    protected KeyCode[]	zoomInKeyCode = new KeyCode[] { KeyCode.VK_EQUALS };

    /** Zoom out exclusively when the defined key code & modifiers are pressed */
    protected boolean	zoomOutKeyExclusive = false;

    /** Zoom out when the keys and/or modifiers are pressed */
    protected KeyCode[]	zoomOutKeyCode = new KeyCode[] { KeyCode.VK_MINUS };

    /** Magnifier visible */
    protected boolean	visible  = false;
    protected boolean	prepared = false;

    /** Magnifier window position does not change when the magnifier center position changes */
    protected boolean	staticMode = false;

    /** Magnifier awaiting clicks anywhere (fine-tuning), stationary is true */
    protected boolean	tuningMode = false;

    /** Magnifier remains visible regardless of pressed keys */
    protected boolean	tuningToggled       = true;
    protected boolean	tuningKeyExclusive  = false;
    protected KeyCode[]	tuningKeyCode       = new KeyCode[] { KeyCode.VK_SHIFT };
    protected KeyCode	tuningToggleKeyCode = KeyCode.VK_X;

    /** Magnifier follows the mouse pointer, stationary is false, pointing is true */
    protected boolean	hoveringMode = false;

    /** Magnifier remains visible and follows mouse regardless of pressed keys */
    protected boolean	hoveringToggled       = false;
    protected boolean	hoveringKeyExclusive  = false;
    protected KeyCode[]	hoveringKeyCode       = new KeyCode[] { KeyCode.VK_ALT };
    protected KeyCode	hoveringToggleKeyCode = KeyCode.VK_Z;
    protected boolean	mouseOverCanvas       = false;

//  /**
//   * @param imagePlus
//   */
//  public ImageWindowMagnifier(ImagePlus imagePlus) {
//      super(imagePlus);
//  }

    /**
     * @param imageWindow
     */
    public ImageWindowMagnifier(ImageWindow imageWindow) {
        super(imageWindow.getImagePlus());

        prepareFrame();
        this.setBackground(Color.white);
        bindImageWindow(imageWindow);

    }

    /**
     * @param object1
     * @param object2
     * @return
     */
    public boolean areEqual(Object object1, Object object2) {
        if ((object1 == null) && (object2 == null)) return true;
        if ((object1 == null) || (object2 == null)) return false;
        if (object1.equals(object2)) return true;

//      try {
//          
//      } catch (Exception exception) {
//          
//      }
        return false;
    }

    /**
     * @param imageWindow
     * @return
     */
    public boolean bindImageWindow(ImageWindow imageWindow) {

        // imageWindow.getCanvas().addMouseMotionListener(this);

        this.imageWindow = imageWindow;
        imageWindow.addWindowFocusListener(this);

        this.imageCanvas = imageWindow.getCanvas();
        bindMouseListeners(imageCanvas, this);

        this.imagePlus = imageWindow.getImagePlus();

        return true;
    }
    int dbg = 0;

    /**
     * @param source
     * @param listener
     * @return
     */
    public boolean bindMouseListeners(Component source, Component listener) {
        boolean	isMouseListener       = listener instanceof MouseListener,
				isMouseMotionListener = listener instanceof MouseMotionListener,
				isMouseWheelListener  = listener instanceof MouseWheelListener;
        

        if (isMouseListener) source.addMouseListener((MouseListener)listener);
        if (isMouseMotionListener) source.addMouseMotionListener((MouseMotionListener)listener);
        if (isMouseWheelListener) source.addMouseWheelListener((MouseWheelListener)listener);

        String	strMouseListener       = (isMouseListener) ? "MouseListener"
                : "";
        String	strMouseMotionListener = (isMouseMotionListener) ? "MotionListener"
                : "";
        String	strMouseWheelListener  = (isMouseWheelListener) ? "WheelListener"
                : "";

        GrasppeKit.debugText("Bind Mouse Listeners",
                             "Binding to " + GrasppeKit.simpleName(source) + " <== "
                             + GrasppeKit.simpleName(listener) + " as "
                             + GrasppeKit.catWords(new String[] { strMouseListener,
                strMouseMotionListener, strMouseWheelListener }), dbg);

        // .getClass().getSimpleName()

        return isMouseListener | isMouseMotionListener | isMouseMotionListener;
    }

    /**
     * @param e
     * @return
     */
    public boolean dispatchedKeyEvent(KeyEvent e) {

        // GrasppeKit.debugText("Dispatch Key Event", GrasppeKit.keyEventString(e) + "...", dbg);
        if (GrasppeEventDispatcher.consumedCombination) return false;

        // GrasppeKit.debugText("Dispatch Key Event", GrasppeKit.keyEventString(e) + "... Handling", dbg);
        if (updateVisiblity()) {
            GrasppeEventDispatcher.consumeCombination();
            GrasppeKit.debugText("Dispatch Key Event",
                                 GrasppeKit.keyEventString(e) + "... Success", dbg);

            return true;
        }

        return false;
    }

    /**
     * @param toggled
     */
    protected void enterHoveringMode(boolean toggled) {
        String	debugKey = "";

        if (isHoveringKeyPressed())
            debugKey = "Key Pressed " + GrasppeKit.lastSplit(hoveringKeyCode);
        if (isHoveringTogglePressed())
            debugKey = GrasppeKit.cat(debugKey,
                                      "Toggle Pressed "
                                      + GrasppeKit.lastSplit(hoveringToggleKeyCode));
        GrasppeKit.debugText("Hovering", "Entering Hovering Mode... " + debugKey, dbg);

        if (!toggled && isTuning()) return;
        if (isTuningToggled()) exitTuningMode(toggled);
        staticMode      = false;
        hoveringMode    = true;
        hoveringToggled = toggled ? !isHoveringToggled()
                                  : false;		// toggled;
        GrasppeKit.debugText("Hovering", "Entering Hovering Mode... " + debugKey + " Success", dbg);
    }

    /**
     * @param toggled
     */
    protected void enterTuningMode(boolean toggled) {
        String	debugKey = "";

        if (isTuningKeyPressed()) debugKey = "Key Pressed " + GrasppeKit.lastSplit(tuningKeyCode);
        if (isTuningTogglePressed())
            debugKey = GrasppeKit.cat(debugKey,
                                      "Toggle Pressed "
                                      + GrasppeKit.lastSplit(tuningToggleKeyCode));

        GrasppeKit.debugText("Tuning", "Entering Tuning Mode... " + debugKey, dbg);

        // if (isTuningToggled()) return;
        if (isHoveringToggled()) exitHoveringMode(true);
        staticMode    = true;
        tuningMode    = true;
        tuningToggled = toggled ? !isTuningToggled()
                                : false;	// toggled;
        GrasppeKit.debugText("Tuning", "Entering Tuning Mode... " + debugKey + " Success", dbg);
    }

    /**
     * @param untoggle
     */
    protected void exitHoveringMode(boolean untoggle) {
        String	debugKey = "";

        if (isHoveringKeyPressed())
            debugKey = "Key Pressed " + GrasppeKit.lastSplit(hoveringKeyCode);
        if (isHoveringTogglePressed())
            debugKey = GrasppeKit.cat(debugKey,
                                      "Toggle Pressed "
                                      + GrasppeKit.lastSplit(hoveringToggleKeyCode));
        GrasppeKit.debugText("Hovering", "Exiting Hovering Mode... " + debugKey, dbg);
        if (!untoggle && hoveringToggled) return;
        if (isHoveringKeyPressed()) return;
        staticMode      = false;
        hoveringMode    = false;
        hoveringToggled = false;
        GrasppeKit.debugText("Hovering", "Exiting Hovering Mode... " + debugKey + " Success", dbg);
    }

    /**
     * @param untoggle
     */
    protected void exitTuningMode(boolean untoggle) {
        String	debugKey = "";

        if (isTuningKeyPressed()) debugKey = "Key Pressed " + GrasppeKit.lastSplit(tuningKeyCode);
        if (isTuningTogglePressed())
            debugKey = GrasppeKit.cat(debugKey,
                                      "Toggle Pressed "
                                      + GrasppeKit.lastSplit(tuningToggleKeyCode));
        GrasppeKit.debugText("Tuning", "Exiting Tuning Mode... " + debugKey, dbg);
        if (!untoggle && tuningToggled) return;
        if (isTuningKeyPressed()) return;
        staticMode    = false;
        tuningMode    = false;
        tuningToggled = false;
        GrasppeKit.debugText("Tuning", "Exiting Tuning Mode... " + debugKey + " Success", dbg);
    }

    /**
     */
    public void forceRepaint() {

        // setImageUpdated();

        repaint();		// 0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * @return
     */
    public boolean hideMagnifier() {
        magnifierFrame.setVisible(false);
        this.visible = magnifierFrame.isVisible();

        return true;
    }

    /**
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        updateMouseLocation(e);
    }

    /**
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {
        updateMouseLocation(e);
    }

    /**
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {

        // updateMouseLocation(e);

        if (e.getSource() == magnifierFrame) {
            setMagnifierPosition(e.getX(), e.getY());
            e.consume();

            return;
        }

        if (e.getSource() == this) {
            setMagnifierPosition(e.getX(), e.getY());
            e.consume();

            return;
        }

        if (e.getSource() == imageCanvas) {
            setImageCanvasPosition(e.getX(), e.getY());
        }

    }

    /**
     * @return
     */
    public boolean moveMagnifier() {
        GrasppeKit.debugText("Moving", "Move Magnifier...", dbg);
        if (positionOnImage == null) return false;
        if (magnifierPosition != null)
            if (areEqual(magnifierPosition, positionOnImage)) return false;

        // TODO: move the center point of the canvas to the image coordinates
        // Rectangle sourceRectangle = new Rectangle(positionOnImage, magnifierSize);
        // setSourceRect(sourceRectangle);

        int			srcRectX = positionOnImage.x - magnifierSize.width / 2,
					srcRectY = positionOnImage.y - magnifierSize.height / 2;

        Rectangle	newRect  = new Rectangle(srcRectX, srcRectY, magnifierSize.width,
                                          magnifierSize.height);

        GrasppeKit.debugText("Moving",
                             "Setting Source Rectangle at Magnification "
                             + super.getMagnification() + "\t" + GrasppeKit.lastSplit(newRect), dbg);

        setSourceRect(newRect);

        forceRepaint();

        // TODO: move the center of the magnifier frame on the image window relative to the image coordinates
        if (shouldMoveFrame() /* if not static and imageWindow visible */) {

            GrasppeKit.debugText("Moving", "Move Frame... " + mouseOverCanvas, dbg);

            try {
                Point	imagePosition = imageCanvas.getLocationOnScreen();

                int		iX            = positionOnImage.x,
						iY            = positionOnImage.y;
                int		iW            = magnifierSize.width,
						iH            = magnifierSize.height;

                GrasppeKit.debugText("Moving",
                                     "Magnifier Center:\t" + imageCanvas.screenX(iX) + ", "
                                     + imageCanvas.screenX(iY) + "  \tSize:\t" + iW + ", " + iH
                                     + "  \tMagnification:\t" + magnification, dbg);

//              iX = (int)(iX * magnification);
//              iY = (int)(iY  / magnification);
//              iW = (int)(iW  / magnification);
//              iH = (int)(iH  / magnification);

                int	cX = imagePosition.x,
					cY = imagePosition.y;

                GrasppeKit.debugText("Moving",
                                     "Magnifier Center:\t" + iX + ", " + iY + "  \tSize:\t" + iW
                                     + ", " + iH + "  \tWindow:\t" + cX + ", " + cY, dbg);

                int	nX = cX + iX - iW / 2,
					nY = cY + iY - iH / 2;

                GrasppeKit.debugText("Moving", "Moving frame to " + nX + ", " + nY, dbg);
                magnifierFrame.setLocation(new Point(nX, nY));
                GrasppeKit.debugText("Moving", "Move Frame... Updaing", dbg);

                int	sX = imagePosition.x + imageCanvas.screenX(positionOnImage.x),
					sY = imagePosition.y + imageCanvas.screenY(positionOnImage.y);

                updateMouseLocation(sX, sY);
                magnifierFrame.setVisible(mouseOverCanvas);
                if (!mouseOverCanvas) return false;

                positionOnScreen = new Point(sX, sY);
                GrasppeKit.debugText("Moving", "Move Frame... Success", dbg);
            } catch (Exception exception) {
                magnifierFrame.setVisible(false);
            }
        }

        magnifierPosition = positionOnImage;

        GrasppeKit.debugText("Moving", "Move Magnifier... Success", dbg);

        return true;
    }

    /**
     * @param g
     */

//  @Override
    @Override
    public void paint(Graphics g) {

        // Roi   roi = imp.getRoi();

        GrasppeKit.debugText("Draw Graphics Attempt", GrasppeKit.lastSplit(srcRect), dbg);

        try {
            Java2.setBilinearInterpolation(g, true);
            Java2.setAntialiased(g, true);

            Image	img = imp.getImage();

            if (img != null) g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

//          super.paint(g);
            g.drawImage(img, 0, 0, screenX(srcRect.width), screenY(srcRect.height), srcRect.x,
                        srcRect.y, srcRect.x + srcRect.width, srcRect.y + srcRect.height, null);

            GrasppeKit.debugText("Draw Graphics Success", GrasppeKit.lastSplit(srcRect), dbg);

            return;

//          //if (overlay!=null) drawOverlay(g);
//          //if (showAllROIs) drawAllROIs(g);
//          //if (roi!=null) drawRoi(roi, g);                

        } catch (OutOfMemoryError e) {
            IJ.outOfMemory("Paint");
        }

        GrasppeKit.debugText("Draw Graphics Failure", GrasppeKit.lastSplit(srcRect), dbg);
    }

    /**
     * @return
     */
    public boolean prepareFrame() {

//      if (imageWindow == null) {
//          prepared = false;
//          return false;
//      }

        if (prepared) return true;

        setMagnification(2.0);

        magnifierFrame = (new JFrame());	// "SpringLayout");
        magnifierFrame.setUndecorated(true);
        magnifierFrame.setMaximumSize(magnifierFrame.getSize());

        // magnifierFrame.setAlwaysOnTop(true);
        magnifierFrame.setFocusableWindowState(false);
        magnifierFrame.setResizable(false);

        magnifierFrame.setBackground(Color.black);

        bindMouseListeners(this, this);				// this.addMouseMotionListener(this);

        bindMouseListeners(magnifierFrame, this);		// magnifierFrame.addMouseMotionListener(this);

        magnifierFrame.getContentPane().add(this, BorderLayout.CENTER);

        magnifierFrame.pack();
        setFrameSize(new Dimension(100, 100));		// magnifierFrame.setSize(300, 300);
        setFramePosition(magnifierFrame.getLocation());

        GrasppeEventDispatcher.getInstance().attachEventHandler(this);

        prepared = true;

        return true;

//      MagnifierCanvas   zoomCanvas = new MagnifierCanvas(Testing.getImageWindow().getImagePlus());

//      zoomCanvas.addMouseListener(CornerSelectorListeners.IJMouseListener);
//      zoomCanvas.addMouseMotionListener(CornerSelectorListeners.IJMotionListener);
//      zoomCanvas.addMouseWheelListener(CornerSelectorListeners.IJWheelListener);
//      zoomCanvas.setBackground(Color.black);
//
//      Container contentPane = magnifierFrame.getContentPane();
//
//      contentPane.setBackground(Color.BLACK);
//
//      contentPane.add(zoomCanvas); // TODO: make boarder layout center
//
//      GrasppeKit.debugText("Zoom Frame", "Content Pane " + contentPane.getBounds().toString(), dbg);
//      GrasppeKit.debugText("Zoom Frame",
//                           "Frame " + magnifierFrame.getBounds().toString(), dbg);
//      GrasppeKit.debugText("Zoom Frame", "Zoom Canvas " + zoomCanvas.getBounds().toString(), dbg);
//
//      double    windowZoom = Testing.getImageWindow().getCanvas().getMagnification();
//
//      zoomCanvas.setMagnification(windowZoom * 2.0);

        // Do not set visible // magnifierFrame.setVisible(Testing.isShowZoom());

    }

    /**
     */
    protected void resetMode() {
        exitHoveringMode(false);
        exitTuningMode(false);
    }

    /**
     */
    protected void resetToggle() {
        setHoveringToggled(false);
        setTuningToggled(false);
        resetMode();
    }

    /**
     * @return
     */
    public boolean resizeMagnifer() {
        magnifierFrame.setSize(new Dimension(screenX(magnifierSize.width),
                screenY(magnifierSize.height)));
        frameSize         = magnifierFrame.getSize();

        magnifierPosition = null;

        moveMagnifier();

        return true;
    }

    /**
     * @return
     */
    protected boolean shouldMoveFrame() {
        if (imageWindow == null) return false;
        if (!imageWindow.isVisible()) return false;

        if (staticMode) {
            return false;
        }

        return true;
    }

    /**
     * @return
     */
    public boolean showMagnifier() {

        magnifierFrame.setVisible(true);
        this.visible = magnifierFrame.isVisible();

        return true;
    }

    /**
     */
    public void toFront() {

        // @link{http://stackoverflow.com/questions/309023/howto-bring-a-java-window-to-the-front}
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                magnifierFrame.toFront();
                forceRepaint();

            }

        });
    }

    /**
     */

    public void update() {}

    /**
     * @param e
     */
    public void updateMouseLocation(MouseEvent e) {
        updateMouseLocation(e.getXOnScreen(), e.getYOnScreen());
    }

    /**
     * @param onScreenX
     * @param onScreenY
     */
    public void updateMouseLocation(int onScreenX, int onScreenY) {
        try {
            int	rX = onScreenX - imageCanvas.getLocationOnScreen().x,
				rY = onScreenY - imageCanvas.getLocationOnScreen().y;

            mouseOverCanvas = imageCanvas.contains(rX, rY);
            GrasppeKit.debugText("Mouse Location",
                                 onScreenX + ", " + onScreenY + " within imageCanvas: "
                                 + mouseOverCanvas, dbg);

//          if (isHoveringKeyPressed() && isHovering() &&!mouseOverCanvas) {
//            exitHoveringMode(false);
//            hideMagnifier();
//            return;
//          }

            boolean	hkp = isHoveringKeyPressed();
            boolean	hm  = isHovering();

            if (isHovering()) {
                if (!mouseOverCanvas) setVisible(false);
                if (!isHoveringToggled() &&!isHoveringKeyPressed()) setVisible(false);
                else setVisible(true);

                return;
            }

            if (isTuningToggled()) {
                setVisible(true);

                return;
            }

            setVisible(false);

        } catch (Exception exitTuningMode) {
            mouseOverCanvas = false;
            if (isHovering()) setVisible(false);
            GrasppeKit.debugText("Mouse Location",
                                 onScreenX + ", " + onScreenY
                                 + " within imageCanvas: caused exception", dbg);
        }

    }

    /**
     * @return
     */
    public boolean updateVisiblity() {
        if (isHoveringKeyPressed()) {
            enterHoveringMode(hoveringToggled);
            setVisible(true);

            return true;
        }

        if (isHoveringTogglePressed()) {
            setHoveringToggled(true);
            setVisible(isHoveringToggled());

            return true;
        }

        if (isTuningKeyPressed()) {
            enterTuningMode(tuningToggled);
            setVisible(true);

            return true;
        }

        if (isTuningTogglePressed()) {
            setTuningToggled(true);
            setVisible(isTuningToggled());

            return true;
        }

        setVisible(false);

        return true;
    }

    /**
     * @param e
     */
    public void windowGainedFocus(WindowEvent e) {
        toFront();
    }

    /**
     * @param e
     */
    public void windowLostFocus(WindowEvent e) {}

    /**
     * @return the top-left corner of the frame only if in static mode, otherwise, the center of the frame
     */
    public Point getFramePosition() {
        if (staticMode) return magnifierFrame.getLocation();
        else return framePosition;
    }

    /**
     * @return the size of the magnifier frame
     */
    public Dimension getFrameSize() {
        return magnifierFrame.getSize();
    }

    /**
     * @return the imageCanvas
     */
    public ImageCanvas getImageCanvas() {
        return imageCanvas;
    }

    /**
     * @return the imagePlus
     */
    public ImagePlus getImagePlus() {
        return imagePlus;
    }

    /**
     * @return the imageWindow
     */
    public ImageWindow getImageWindow() {
        return imageWindow;
    }

    /**
     * @return the magnifierFrame
     */
    public JFrame getMagnifierFrame() {
        return magnifierFrame;
    }

    /**
     * @return the magnifierPosition
     */
    public Point getMagnifierPosition() {
        return magnifierPosition;
    }

    /**
     * @return the magnifierSize
     */
    public Dimension getMagnifierSize() {
        return magnifierSize;
    }

    /**
     * @return
     */
    public boolean isHovering() {
        return hoveringMode;
    }

    /**
     * @return
     */
    protected boolean isHoveringKeyPressed() {
        return GrasppeEventDispatcher.isPressed(GrasppeEventDispatcher.asSet(hoveringKeyCode),
                hoveringKeyExclusive);
    }

    /**
     * @return
     */
    protected boolean isHoveringTogglePressed() {
        return GrasppeEventDispatcher.isPressedExclusive(hoveringToggleKeyCode);
    }

    /**
     * @return
     */
    public boolean isHoveringToggled() {
        return hoveringToggled;		// hoveringMode && hoveringToggled;
    }

    /**
     * @return
     */
    public boolean isTuning() {
        return tuningMode;
    }

    /**
     * @return
     */
    protected boolean isTuningKeyPressed() {
        return GrasppeEventDispatcher.isPressed(GrasppeEventDispatcher.asSet(tuningKeyCode),
                tuningKeyExclusive);
    }

    /**
     * @return
     */
    protected boolean isTuningTogglePressed() {
        return GrasppeEventDispatcher.isPressedExclusive(tuningToggleKeyCode);
    }

    /**
     * @return
     */
    public boolean isTuningToggled() {
        return tuningToggled;		// tuningMode && tuningToggled;
    }

    /**
     * @return
     */
    @Override
    public boolean isVisible() {
        return magnifierFrame.isVisible();
    }

    /**
     * @return
     */
    protected boolean isZoomInKeyPressed() {
        return GrasppeEventDispatcher.isPressed(GrasppeEventDispatcher.asSet(zoomInKeyCode),
                zoomInKeyExclusive);
    }

    /**
     * @return
     */
    protected boolean isZoomOutKeyPressed() {
        return GrasppeEventDispatcher.isPressed(GrasppeEventDispatcher.asSet(zoomOutKeyCode),
                zoomOutKeyExclusive);
    }

    /**
     * Set the top-left corner of the magnifier frame only when in static mode.
     * @param framePosition the framePosition to set
     */
    public void setFramePosition(Point framePosition) {
        if (!staticMode) return;

        int	nX = framePosition.x,		// - magnifierSize.width/2,
			nY = framePosition.y;		// - magnifierSize.height/2;

        magnifierFrame.setLocation(new Point(nX, nY));

        int	fX = magnifierFrame.getLocation().x + magnifierSize.width / 2,
			fY = magnifierFrame.getLocation().y + magnifierSize.height / 2;

        this.framePosition = magnifierFrame.getLocation();

//      } else {
//          setMagnifierPosition(x, y)
//      }
    }

    /**
     * @param frameSize the frameSize to set
     */
    public void setFrameSize(Dimension frameSize) {
        setMagnifierSize(offScreenX(frameSize.width), offScreenY(frameSize.height));
    }

    /**
     * @param hoveringMode
     */
    protected void setHoveringToggled(boolean hoveringMode) {
        if (hoveringMode) enterHoveringMode(true);
        else enterHoveringMode(false);
    }

    /**
     * Sets the center point of the magnifier on actual image relative to a given position on the image window in screen pixels.
     * @param x
     * @param y
     * @return
     */
    public boolean setImageCanvasPosition(int x, int y) {
        int	nX = imageCanvas.offScreenX(x),
			nY = imageCanvas.offScreenY(y);

        return setImagePosition(nX, nY);

    }

    /**
     * Sets the center point of the magnifier on actual image directly
     * @param x
     * @param y
     * @return
     */
    public boolean setImagePosition(int x, int y) {
        positionOnImage = new Point(x, y);

        return moveMagnifier();
    }

    /**
     * @param imageWindow the imageWindow to set
     */
    public void setImageWindow(ImageWindow imageWindow) {
        this.imageWindow = imageWindow;
    }

    /**
     * Sets the center point of the magnifier on actual image relative to center point of the magnifier window in screen pixels.
     * @param x
     * @param y
     * @return
     */
    public boolean setMagnifierPosition(int x, int y) {

        int	nX = offScreenX(x),
			nY = offScreenY(y);

        return setImagePosition(nX, nY);
    }

    /**
     * @param width
     * @param height
     */
    public void setMagnifierSize(int width, int height) {
        Dimension	newSize = new Dimension(width, height);

        if (areEqual(magnifierSize, newSize)) return;

        magnifierSize = newSize;

        resizeMagnifer();
    }

    /**
     * @param tuningMode
     */
    protected void setTuningToggled(boolean tuningMode) {
        if (tuningMode) enterTuningMode(true);
        else enterTuningMode(false);
    }

    /**
     * Changes the visibility of the magnifier. If setting visible while visible, bring it to
     * front. Then, If state will not change, do nothing. Will not hide if in a toggled mode.
     * If frame visibility was not changed the state will not be changed. When making visible
     * and not in specific mode, default to tuning mode
     * @param visible
     */
    @Override
    public void setVisible(boolean visible) {

        // If setting visible while visible, bring it to front
        if (visible && magnifierFrame.isVisible()) toFront();

        GrasppeKit.debugText("Visible",
                             "Front: " + (visible && magnifierFrame.isVisible()) + "\tToggleOn: "
                             + (!visible && (hoveringToggled ^ tuningToggled)), dbg);

//      // Do not hide if in a toggled mode
//      if (!visible && (hoveringToggled ^ tuningToggled)) if (showMagnifier()) return;

        if (visible) showMagnifier();
        else hideMagnifier();

        GrasppeKit.debugText("Visible",
                             "Set to " + visible + "\tFrame: " + magnifierFrame.isVisible()
                             + "\tCanvas: " + isVisible() + "\t"
                             + GrasppeKit.lastSplit(getBounds()), dbg);

        // If frame visibility was not changed then don't reflect updated status
        // if (visible != magnifierFrame.isVisible()) return;

        // this.visible = magnifierFrame.isVisible();

        // When making visible and not in specific mode, default to tuning mode
        // if (visible &&!isHovering() &&!isTuning()) enterTuningMode(true);
    }

	@Override
	public void detatch(Observable oberservableObject) {
		// TODO Auto-generated method stub
		
	}
}
