/*
 * @(#)GestureComponent.java   12/12/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.gesture;

import com.apple.eawt.event.GestureEvent;
import com.apple.eawt.event.MagnificationEvent;
import com.apple.eawt.event.RotationEvent;

import com.grasppe.jive.components.AbstractActionWrapper;
import com.grasppe.jive.gesture.GestureUtilities.GestureDirection;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/12/24
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class GestureComponent extends JComponent {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  /** Field description */
  private AbstractActionWrapper actionWrapper = new AbstractActionWrapper();
  private GestureAdapter        gestureAdapter;
  private MouseAdapter          mouseAdapter;

  /**
   *
   */
  public GestureComponent() {
    applyComponentStyle();
    attachGestureEvents();
  }

  /**
   */
  private void applyComponentStyle() {
    setOpaque(false);
    setBorder(new LineBorder(Color.GRAY, 10, true));
  }

  /**
   */
  private void attachGestureEvents() {

    mouseAdapter = new MouseAdapter() {

      boolean wasDoubleClick = false;

      /*
       *  (non-Javadoc)
       * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseClicked(MouseEvent e) {

        redispatchMouseEvent(e);		// getParent().dispatchEvent(createParentEvent(getParent(), e));

        // http://stackoverflow.com/questions/548180/java-ignore-single-click-on-double-click

        final MouseEvent mouseEvent = e;

        // System.out.println( "Click at (" + e.getX() + ":" + e.getY() + ")" );
        if (e.getClickCount() == 2) {

          // System.out.println( "  and it's a double click!");
          wasDoubleClick = true;
        } else {
          Integer timerinterval = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
          Timer   timer         = new Timer(timerinterval.intValue(), new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
              if (wasDoubleClick) {
                wasDoubleClick = false;			// reset flag
                fireAction("Double Click", mouseEvent);
              } else {

                // System.out.println( "  and it's a simple click!");
                fireAction("Click", mouseEvent);
              }
            }

          });

          timer.setRepeats(false);
          timer.start();
        }

        // if (e.getClickCount() ==1 && !e.isConsumed()) fireAction("Click", e);
        // if (e.getClickCount() ==2 && !e.isConsumed()) fireAction("Double Click", e);
      }

      /*
       *  (non-Javadoc)
       * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseDragged(MouseEvent e) {
        fireAction("Drag", e);
        redispatchMouseEvent(e);		// getParent().dispatchEvent(createParentEvent(getParent(), e));
      }

      /*
       *  (non-Javadoc)
       * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseEntered(MouseEvent e) {
        fireAction("Mouse Enter", e);
        redispatchMouseEvent(e);		// getParent().dispatchEvent(createParentEvent(getParent(), e));
      }

      /*
       *  (non-Javadoc)
       * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseExited(MouseEvent e) {
        fireAction("Mouse Exit", e);
        redispatchMouseEvent(e);		// getParent().dispatchEvent(createParentEvent(getParent(), e));
      }

      /*
       *  (non-Javadoc)
       * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseMoved(MouseEvent e) {
        fireAction("Hover", e);
        redispatchMouseEvent(e);		// getParent().dispatchEvent(createParentEvent(getParent(), e));
      }

      /*
       *  (non-Javadoc)
       * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
       */
      @Override
      public void mousePressed(MouseEvent e) {
        fireAction("Button Down", e);
        redispatchMouseEvent(e);		// getParent().dispatchEvent(createParentEvent(getParent(), e));
      }

      /*
       *  (non-Javadoc)
       * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseReleased(MouseEvent e) {
        fireAction("Button Up", e);
        redispatchMouseEvent(e);		// getParent().dispatchEvent(createParentEvent(getParent(), e));
      }

    };

    gestureAdapter = new GestureAdapter() {

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#scroll(com.grasppe.jive.components.GestureUtilities.GestureDirection, java.awt.event.MouseWheelEvent)
       */
      @Override
      public void scroll(GestureDirection direction, MouseWheelEvent e) {

        // fireAction("Scroll", e, direction);
        super.scroll(direction, e);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#swipe(com.grasppe.jive.components.GestureUtilities.GestureDirection, com.apple.eawt.event.GestureEvent)
       */
      @Override
      public void swipe(GestureDirection direction, GestureEvent e) {

        // fireAction("Swipe", e, direction);
        super.swipe(direction, e);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#zoom(com.grasppe.jive.components.GestureUtilities.GestureDirection, com.apple.eawt.event.MagnificationEvent)
       */
      @Override
      public void zoom(GestureDirection direction, MagnificationEvent e) {

        // fireAction("Zoom", e, direction);
        super.zoom(direction, e);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#rotate(com.grasppe.jive.components.GestureUtilities.GestureDirection, com.apple.eawt.event.RotationEvent)
       */
      @Override
      public void rotate(GestureDirection direction, RotationEvent e) {
        fireAction("Rotate", e, direction);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#scrollDown(java.awt.event.MouseWheelEvent)
       */
      @Override
      public void scrollDown(MouseWheelEvent e) {
        fireAction("Scroll Down", e, GestureDirection.DOWN);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#scrollLeft(java.awt.event.MouseWheelEvent)
       */
      @Override
      public void scrollLeft(MouseWheelEvent e) {
        fireAction("Scroll Left", e, GestureDirection.LEFT);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#scrollRight(java.awt.event.MouseWheelEvent)
       */
      @Override
      public void scrollRight(MouseWheelEvent e) {
        fireAction("Scroll Right", e, GestureDirection.RIGHT);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#scrollUp(java.awt.event.MouseWheelEvent)
       */
      @Override
      public void scrollUp(MouseWheelEvent e) {
        fireAction("Scroll Up", e, GestureDirection.UP);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#swipeDown(com.apple.eawt.event.GestureEvent)
       */
      @Override
      public void swipeDown(GestureEvent e) {
        fireAction("Swipe Down", e, GestureDirection.DOWN);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#swipeLeft(com.apple.eawt.event.GestureEvent)
       */
      @Override
      public void swipeLeft(GestureEvent e) {
        fireAction("Swipe Left", e, GestureDirection.LEFT);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#swipeRight(com.apple.eawt.event.GestureEvent)
       */
      @Override
      public void swipeRight(GestureEvent e) {
        fireAction("Swipe Right", e, GestureDirection.RIGHT);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#swipeUp(com.apple.eawt.event.GestureEvent)
       */
      @Override
      public void swipeUp(GestureEvent e) {
        fireAction("Swipe Up", e, GestureDirection.UP);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#zoomIn(com.apple.eawt.event.MagnificationEvent)
       */
      @Override
      public void zoomIn(MagnificationEvent e) {
        fireAction("Zoom In", e, GestureDirection.IN);
      }

      /*
       *  (non-Javadoc)
       * @see com.grasppe.jive.components.GestureAdapter#zoomOut(com.apple.eawt.event.MagnificationEvent)
       */
      @Override
      public void zoomOut(MagnificationEvent e) {
        fireAction("Zoom Out", e, GestureDirection.OUT);
      }

    };

    com.apple.eawt.event.GestureUtilities.addGestureListenerTo(this, gestureAdapter);

    this.addMouseWheelListener(gestureAdapter);

    this.addMouseMotionListener(mouseAdapter);
    this.addMouseListener(mouseAdapter);
  }

  /**
   *    @param parent
   *    @param e
   *    @return
   */
  private MouseEvent createParentEvent(Component parent, MouseEvent e) {

    return SwingUtilities.convertMouseEvent(
        (Component)e.getSource(), e,
        parent);		// new MouseEvent(parent, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), false, e.getButton());

//  e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(),
//                     e.getClickCount(), false, e.getScrollType(), e.getScrollAmount(),
//                     e.getWheelRotation());
  }

  /**
   *     @param actionName
   *     @see com.grasppe.jive.components.AbstractActionWrapper#fireAction(java.lang.String)
   */
  public void fireAction(String actionName) {

    // System.out.println(actionName);
    GrasppeKit.debugText(actionName, 0);
    actionWrapper.fireAction(actionName);
  }

  /**
   * @param actionName
   * @param data
   * @see com.grasppe.jive.components.AbstractActionWrapper#fireAction(java.lang.String, java.lang.Object)
   */
  public void fireAction(String actionName, Object data) {

    GrasppeKit.debugText(actionName + ": " + data.toString(), 0);
    actionWrapper.fireAction(actionName, data);
  }

  /**
   * @param actionName
   * @param data
   * @param metadata
   * @see com.grasppe.jive.components.AbstractActionWrapper#fireAction(java.lang.String, java.lang.Object, java.lang.Object)
   */
  public void fireAction(String actionName, Object data, Object metadata) {

    GrasppeKit.debugText(actionName + ": " + data.toString(), 0);
    GrasppeKit.debugText("\t" + metadata, 0);
    actionWrapper.fireAction(actionName, data, metadata);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

    // TODO Auto-generated method stub
    JFrame     mainFrame  = new JFrame();

    JComponent jComponent = new GestureComponent();

    mainFrame.getContentPane().add(jComponent, BorderLayout.CENTER);

    jComponent.setMinimumSize(new Dimension(500, 500));
    mainFrame.setMinimumSize(new Dimension(600, 600));

    mainFrame.pack();

    mainFrame.setVisible(true);

  }

  /**
   *    @param g
   */
  public void paint(Graphics g) {

    // http://www.dreamincode.net/forums/topic/57078-make-a-transparent-swing-ui-components/

    Graphics2D g2 = (Graphics2D)g.create();

    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
    super.paint(g2);
    g2.dispose();
  }

  /**
   *    @param e
   */
  private void redispatchMouseEvent(MouseEvent e) {

    return;

    // // http://www.java-gaming.org/index.php/topic,16460.
    //
    // Point     glassPanePoint = e.getPoint();
    // Container container      = (Container)SwingUtilities.getRoot(this);           // frame.getContentPane();
    // Point     containerPoint = SwingUtilities.convertPoint(this, glassPanePoint, container);
    //
    // if (containerPoint.y >= 0) {
    // Component component = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);
    //
    // // TODO : Remove this, test only
    // // if( component instanceof JPanel )
    // // {
    // // frame.requestFocus();
    // // }
    // // END remove
    //
    // if ((component != null)) {
    // Point componentPoint = SwingUtilities.convertPoint(this, glassPanePoint, component);
    //
    // try {
    // component.dispatchEvent(new MouseEvent(component, e.getID(), e.getWhen(), e.getModifiers(), componentPoint.x,
    // componentPoint.y, e.getClickCount(), e.isPopupTrigger()));
    // } catch (Exception exception) {}
    //
    // }
    // }
  }

  /**
   *   @return the actionWrapper
   */
  public AbstractActionWrapper getActionWrapper() {
    return actionWrapper;
  }
}
