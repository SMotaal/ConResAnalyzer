/*
 * @(#)GestureAdapter.java   12/12/25
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

import com.grasppe.jive.gesture.GestureUtilities.GestureDirection;
import com.grasppe.jive.gesture.GestureUtilities.GestureMode;
import com.grasppe.jive.gesture.GestureUtilities.RotateListener;
import com.grasppe.jive.gesture.GestureUtilities.ScrollListener;
import com.grasppe.jive.gesture.GestureUtilities.SwipeListener;
import com.grasppe.jive.gesture.GestureUtilities.ZoomListener;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *   Class description
 *      @version        $Revision: 1.0, 12/12/24
 *      @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
class GestureAdapter extends com.apple.eawt.event.GestureAdapter
        implements MouseWheelListener, ScrollListener, SwipeListener, ZoomListener, RotateListener {

  private GestureAdapterSettings settings = new GestureAdapterSettings();
  private GestureAdapterState    state    = new GestureAdapterState();

  /**
   *    @param e
   *    @return
   */
  private MouseWheelEvent createScrollEvent(MouseWheelEvent e) {
    return new MouseWheelEvent((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(),
                               e.getClickCount(), false, e.getScrollType(), e.getScrollAmount(),
                               state.getCombinedScrollRotations());
  }

  /**
   *    @param e
   *    @return
   */
  private GestureEvent createSwipeEvent(GestureEvent e) {
    return e;
  }

  /**
   *    @param direction
   *    @param e
   */
  private void fireRotateEvent(GestureDirection direction, RotationEvent e) {
    if (!validMode(GestureMode.ROTATE) ||!state.resetMode(GestureMode.ROTATE)) return;

    state.updateTime();
    rotate(direction, e);
  }

  /**
   *  @param direction
   *  @param e
   */
  private void fireScrollEvent(GestureDirection direction, MouseWheelEvent e) {
    if (!validDirection(direction)) return;

    // if (!validMode(GestureMode.SCROLL) || !state.resetMode(GestureMode.SCROLL)) return;
    // state.updateTime();
    scroll(direction, createScrollEvent(e));
  }

  /**
   *  @param direction
   *  @param e
   */
  private void fireSwipeEvent(GestureDirection direction, GestureEvent e) {
    if (!validDirection(direction)) return;

    // if (!validMode(GestureMode.SWIPE) || !state.resetMode(GestureMode.SWIPE)) return;
    // state.updateTime();
    swipe(direction, createSwipeEvent(e));
  }

  /**
   *    @param direction
   *    @param e
   */
  private void fireWheelEvent(GestureDirection direction, MouseWheelEvent e) {
    state.addPhaseEvent(Math.abs(e.getWheelRotation()), direction);

    boolean validScrollTime = state.getPhaseSeconds() > settings.getSwipeSecondsThreshold();

    state.updateTime();
    
    // if (direction!=state.getCurrentDirection()) state.

    if ((state.isCoasting() && state.isScrolling()) || (state.isPhasing() && validScrollTime)) {
      if (!state.isScrolling()) state.resetMode(GestureMode.SCROLL);

      fireScrollEvent(direction, e);
    }

    if (state.isPhasing() &&!validScrollTime) {
      state.resetMode(GestureMode.NONE);
    }
  }

  /**
   *    @param direction
   *    @param e
   */
  private void fireZoomEvent(GestureDirection direction, MagnificationEvent e) {
    if (!validMode(GestureMode.MAGINIFY) ||!state.resetMode(GestureMode.MAGINIFY)) return;
    state.updateTime();
    zoom(direction, e);
  }

  /*
   *  (non-Javadoc)
   * @see com.apple.eawt.event.GestureAdapter#gestureBegan(com.apple.eawt.event.GesturePhaseEvent)
   */

  /**
   *  @param arg0
   */
  @Override
  public void gestureBegan(com.apple.eawt.event.GesturePhaseEvent arg0) {
    state.startPhasing();

    // System.out.println("Phase Begins: " + arg0);
  }

  /*
   *  (non-Javadoc)
   * @see com.apple.eawt.event.GestureAdapter#gestureEnded(com.apple.eawt.event.GesturePhaseEvent)
   */

  /**
   *  @param e
   */
  @Override
  public void gestureEnded(com.apple.eawt.event.GesturePhaseEvent e) {

    boolean validSwipeTime   = state.getPhaseSeconds() <= settings.getSwipeSecondsThreshold();
    boolean validSwipeRatio  = state.getPhaseRatio() > settings.getSwipeRatioThreshold(state.getCurrentDirection());
    boolean validSwipeEvents = state.getPhaseEvents() <= settings.getSwipeEventThreshold();

    // GrasppeKit.debugText("Scrolling: " + state.isScrolling() + "\t Swiping: " + state.isSwiping() + "\t Timing: " + state.getPhaseSeconds() + "\t Events:" + state.getPhaseEvents()  + "\t Ratio: " + state.getPhaseRatio(), 0);
    if (!state.isScrolling() && !state.isSwiping()
        && (validSwipeTime && validSwipeEvents && validSwipeRatio)) {
      state.resetMode(GestureMode.SWIPE);
      fireSwipeEvent(state.getPhaseDirection(), e);
    }

    state.endPhasing();
  }

  /*
   *  (non-Javadoc)
   * @see com.apple.eawt.event.GestureAdapter#magnify(com.apple.eawt.event.MagnificationEvent)
   */

  /**
   *  @param e
   */
  @Override
  public final void magnify(MagnificationEvent e) {
    GestureDirection direction = (e.getMagnification() > 0) ? GestureDirection.IN
                                                            : GestureDirection.OUT;

    fireZoomEvent(direction, e);

    // System.out.println("Magnify: " + e);
  }

  /**
   *  @param e
   */
  @Override
  public final void mouseWheelMoved(MouseWheelEvent e) {
	  
	int vMask = 0; //InputEvent.ALT_DOWN_MASK;
	int hMask = InputEvent.SHIFT_DOWN_MASK;
	//int offMask = InputEvent.CTRL_DOWN_MASK;
	
	// if (event.getModifiersEx() & (onmask | offmask) == onmask) {
	  

    boolean          isVertical       = ((e.getModifiersEx() & (vMask | hMask)) == vMask);	//e.getModifiersEx() == 0;
    boolean          isHoriontal      = ((e.getModifiersEx() & (hMask | vMask)) == hMask);//e.getModifiersEx() == InputEvent.SHIFT_DOWN_MASK;

    GestureDirection gestureDirection = GestureDirection.NONE;

    if (isHoriontal) gestureDirection = (e.getWheelRotation() > 0) ? GestureDirection.LEFT
                                                                   : GestureDirection.RIGHT;
    else if (isVertical) gestureDirection = (e.getWheelRotation() > 0) ? GestureDirection.UP
                                                                       : GestureDirection.DOWN;

    if (gestureDirection == GestureDirection.NONE) return;

    fireWheelEvent(gestureDirection, e);

  }

  /*
   *  (non-Javadoc)
   * @see com.apple.eawt.event.GestureAdapter#rotate(com.apple.eawt.event.RotationEvent)
   */

  /**
   *    @param e
   */
  @Override
  public final void rotate(RotationEvent e) {

    GestureDirection direction = (e.getRotation() < 0) ? GestureDirection.CLOCKWISE
                                                       : GestureDirection.ANTICLOCKWISE;

    fireRotateEvent(direction, e);
  }

  /**
   *    @param direction
   *    @param e
   */
  @Override
  public void rotate(GestureDirection direction, RotationEvent e) {
    System.out.println("\tRotate " + direction + ": " + e);
  }

  /**
   *    @param direction
   *    @param e
   */
  public void scroll(GestureDirection direction, MouseWheelEvent e) {
    if (direction == GestureDirection.UP) scrollUp(e);
    if (direction == GestureDirection.DOWN) scrollDown(e);
    if (direction == GestureDirection.LEFT) scrollLeft(e);
    if (direction == GestureDirection.RIGHT) scrollRight(e);
  }

  /**
   *  @param e
   */
  public void scrollDown(MouseWheelEvent e) {
    System.out.println("\tScroll Down: " + e);
  }

  /**
   *  @param e
   */
  public void scrollLeft(MouseWheelEvent e) {
    System.out.println("\tScroll Left: " + e);
  }

  /**
   *  @param e
   */
  public void scrollRight(MouseWheelEvent e) {
    System.out.println("\tScroll Right: " + e);
  }

  /**
   *  @param e
   */
  public void scrollUp(MouseWheelEvent e) {
    System.out.println("\tScroll Up: " + e);
  }

  /**
   *    @param direction
   *    @param e
   */
  @Override
  public void swipe(GestureDirection direction, GestureEvent e) {
    if (direction == GestureDirection.UP) swipeUp(e);
    if (direction == GestureDirection.DOWN) swipeDown(e);
    if (direction == GestureDirection.LEFT) swipeLeft(e);
    if (direction == GestureDirection.RIGHT) swipeRight(e);

  }

  /*
   *  (non-Javadoc)
   * @see com.apple.eawt.event.GestureAdapter#swipedDown(com.apple.eawt.event.SwipeEvent)
   */

  /**
   *    @param e
   */
  public void swipeDown(GestureEvent e) {
    System.out.println("\tSwipe Down: " + e);
  }

  /*
   *  (non-Javadoc)
   * @see com.apple.eawt.event.GestureAdapter#swipedLeft(com.apple.eawt.event.SwipeEvent)
   */

  /**
   *    @param e
   */
  public void swipeLeft(GestureEvent e) {
    System.out.println("\tSwipe Left: " + e);
  }

  /*
   *  (non-Javadoc)
   * @see com.apple.eawt.event.GestureAdapter#swipedRight(com.apple.eawt.event.SwipeEvent)
   */

  /**
   *    @param e
   */
  public void swipeRight(GestureEvent e) {

    System.out.println("\tSwipe Right: " + e);

  }

  /*
   *  (non-Javadoc)
   * @see com.apple.eawt.event.GestureAdapter#swipedUp(com.apple.eawt.event.SwipeEvent)
   */

  /**
   *    @param e
   */
  public void swipeUp(GestureEvent e) {
    System.out.println("\tSwipe Up: " + e);
  }

  /**
   *    @param direction
   *    @return
   */
  private boolean validDirection(GestureDirection direction) {
    return (direction == GestureDirection.UP) || (direction == GestureDirection.DOWN) || (direction == GestureDirection.LEFT)
           || (direction == GestureDirection.RIGHT);
  }

  /**
   *    @param mode
   *    @return
   */
  public boolean validMode(GestureMode mode) {
    return ((state.getCurrentMode() == GestureMode.NONE) || (state.getCurrentMode() == mode)
            || (state.getLastSeconds() > settings.getModeSecondsThreshold()));
  }

  /**
   *    @param direction
   *    @param e
   */
  @Override
  public void zoom(GestureDirection direction, MagnificationEvent e) {
    if (direction == GestureDirection.IN) zoomIn(e);
    if (direction == GestureDirection.OUT) zoomOut(e);
  }

  /**
   *    @param e
   */
  @Override
  public void zoomIn(MagnificationEvent e) {
    System.out.println("\tZoom In: " + e);
  }

  /**
   *    @param e
   */
  @Override
  public void zoomOut(MagnificationEvent e) {
    System.out.println("\tZoom Out: " + e);
  }
}
