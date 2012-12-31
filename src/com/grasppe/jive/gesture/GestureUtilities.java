/*
 * @(#)GestureUtilities.java   12/12/25
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

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.MouseWheelEvent;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/12/25
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class GestureUtilities {

  /**
   */
  public enum GestureDirection { NONE, UP, DOWN, LEFT, RIGHT, IN, OUT, CLOCKWISE, ANTICLOCKWISE }

  /**
   */
  public enum GestureMode { NONE, SCROLL, SWIPE, ROTATE, MAGINIFY }

  /**
   *
   *    @version        $Revision: 1.0, 12/12/25
   *    @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
   */
  public interface RotateListener {

    /**
     *  @param direction
     *  @param e
     */
    void rotate(GestureDirection direction, RotationEvent e);
  }


  /**
   *
   *    @version        $Revision: 1.0, 12/12/25
   *    @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
   */
  public interface ScrollListener {

    /**
     *  @param direction
     *  @param e
     */
    void scroll(GestureDirection direction, MouseWheelEvent e);

    /**
     *  @param e
     */
    void scrollDown(MouseWheelEvent e);

    /**
     *  @param e
     */
    void scrollLeft(MouseWheelEvent e);

    /**
     *  @param e
     */
    void scrollRight(MouseWheelEvent e);

    /**
     *  @param e
     */
    void scrollUp(MouseWheelEvent e);
  }


  /**
   *
   *    @version        $Revision: 1.0, 12/12/25
   *    @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
   */
  public interface SwipeListener {

    /**
     *  @param direction
     *  @param e
     */
    void swipe(GestureDirection direction, GestureEvent e);

    /**
     *  @param e
     */
    void swipeDown(GestureEvent e);

    /**
     *  @param e
     */
    void swipeLeft(GestureEvent e);

    /**
     *  @param e
     */
    void swipeRight(GestureEvent e);

    /**
     *  @param e
     */
    void swipeUp(GestureEvent e);
  }


  /**
   *
   *    @version        $Revision: 1.0, 12/12/25
   *    @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
   */
  public interface ZoomListener {

    /**
     *  @param direction
     *  @param e
     */
    void zoom(GestureDirection direction, MagnificationEvent e);

    /**
     *  @param e
     */
    void zoomIn(MagnificationEvent e);

    /**
     *  @param e
     */
    void zoomOut(MagnificationEvent e);
  }
}
