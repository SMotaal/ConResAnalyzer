/*
 * @(#)GestureAdapterState.java   12/12/25
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.gesture;

import com.grasppe.jive.gesture.GestureUtilities.GestureDirection;
import com.grasppe.jive.gesture.GestureUtilities.GestureMode;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/12/25
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class GestureAdapterState {

  private boolean                phasing          = false;
  private boolean                coasting         = false;
  private boolean                scrolling        = false;
  private boolean                swiping          = false;
  private int                    phaseEvents      = 0;
  private ArrayList<Integer>     phaseRotations   = new ArrayList<Integer>();
  private int                    phaseRotation    = 0;
  private int                    scrollRotation   = 0;
  private long                   phaseTime        = 0;
  private long                   lastTime         = 0;
  private int                    phaseUp          = 0;
  private int                    phaseDown        = 0;
  private int                    phaseLeft        = 0;
  private int                    phaseRight       = 0;
  private GestureMode            currentMode      = GestureMode.NONE;
  private GestureDirection       currentDirection = GestureDirection.NONE;
  private long                   modeTime         = 0;
  private GestureAdapterSettings settings;

  /**
   */
  public GestureAdapterState() {
    super();
  }

  /**
   *     @param settings
   */
  public GestureAdapterState(GestureAdapterSettings settings) {
    super();
    this.settings = settings;
  }

  /**
   *    @param rotations
   *    @param direction
   */
  public void addPhaseEvent(int rotations, GestureDirection direction) {
    phaseEvents   += 1;
    phaseRotations.add(rotations);

    if (direction == GestureDirection.UP) phaseUp += 1;
    if (direction == GestureDirection.DOWN) phaseDown += 1;
    if (direction == GestureDirection.LEFT) phaseLeft += 1;
    if (direction == GestureDirection.RIGHT) phaseRight += 1;

    GestureDirection newDirection = currentDirection;
    if (phaseUp > phaseDown + phaseLeft + phaseRight) newDirection = GestureDirection.UP;
    if (phaseDown > phaseUp + phaseLeft + phaseRight) newDirection = GestureDirection.DOWN;
    if (phaseLeft > phaseDown + phaseUp + phaseRight) newDirection = GestureDirection.LEFT;
    if (phaseRight > phaseDown + phaseLeft + phaseUp) newDirection = GestureDirection.RIGHT;
    
    if (direction==newDirection)
    	phaseRotation += rotations;
    else    	
    	currentDirection = newDirection;
  }
  
  public GestureDirection getPhaseDirection() {
	    if (phaseUp > phaseDown + phaseLeft + phaseRight) return GestureDirection.UP;
	    if (phaseDown > phaseUp + phaseLeft + phaseRight) return GestureDirection.DOWN;
	    if (phaseLeft > phaseDown + phaseUp + phaseRight) return GestureDirection.LEFT;
	    if (phaseRight > phaseDown + phaseLeft + phaseUp) return GestureDirection.RIGHT;
		return currentDirection;
  }

  /**
   */
  public void endPhasing() {
    phasing  = false;
    coasting = true;
  }

  /**
   *    @param mode
   *    @return
   */
  public boolean resetMode(GestureMode mode) {
    boolean isSameMode = currentMode == mode;

    // boolean isValidTime      =   getModeSeconds()>settings.getModeSecondsThreshold();

    if ((mode == GestureMode.NONE) || (mode == GestureMode.MAGINIFY)
        || (mode == GestureMode.ROTATE)) {		// (mode == GestureMode.NONE)
      if (!isSameMode) {
        scrolling = false;
        swiping   = false;

        modeTime  = System.nanoTime();
      }

      currentMode = mode;

      if (mode == GestureMode.NONE) currentDirection = GestureDirection.NONE;

      return true;
    }

    if ((mode == GestureMode.SCROLL) && (currentMode == GestureMode.NONE)) {
      if (!isSameMode) {
        scrolling = true;
        swiping   = false;
        modeTime  = System.nanoTime();
      }

      currentMode = mode;

      return true;
    }

    if ((mode == GestureMode.SWIPE) && (currentMode == GestureMode.NONE)) {
      if (!isSameMode) {
        scrolling = false;
        swiping   = true;
        modeTime  = System.nanoTime();
      }

      currentMode = mode;

      return true;

    }

    return false;
  }

  /**
   */
  public void resetPhase() {
    phasing       = false;
    phaseEvents   = 0;
    phaseRotation 	= 0;
    scrollRotation 	= 0;
    phaseTime     = 0;
    phaseUp       = 0;
    phaseDown     = 0;
    phaseLeft     = 0;
    phaseRight    = 0;
    phaseRotations.clear();
  }

  /**
   */
  public void startPhasing() {
    resetPhase();
    phaseTime = System.nanoTime();
    phasing   = true;
    coasting  = false;
    scrollRotation = 0;
  }

  /**
   */
  public void updateTime() {
    lastTime = System.nanoTime();
  }

///**
// * @param coasting the coasting to set
// */
//public void setCoasting(boolean coasting) {
//  this.coasting = coasting;
//}

///**
// * @param currentDirection the currentDirection to set
// */
//public void setCurrentDirection(GestureDirection currentDirection) {
//  this.currentDirection = currentDirection;
//}

///**
// * @param currentMode the currentMode to set
// */
//public void setCurrentMode(GestureMode currentMode) {
//  this.currentMode = currentMode;
//}

///**
// * @param phaseTime the phaseTime to set
// */
//public void setPhaseTime(long phaseTime) {
//  this.phaseTime = phaseTime;
//}
//
///**
// * @param phasing the phasing to set
// */
//public void setPhasing(boolean phasing) {
//  this.phasing = phasing;
//}

  /**
   *    @return
   */
  public int getCombinedScrollRotations() {
    int currentRotation = phaseRotation - scrollRotation;

    this.scrollRotation = currentRotation;

    return this.scrollRotation;
  }

  /**
   * @return the currentDirection
   */
  public GestureDirection getCurrentDirection() {
    return currentDirection;
  }

  /**
   * @return the currentMode
   */
  public GestureMode getCurrentMode() {
    return currentMode;
  }

  /**
   * @return the lastTime
   */
  public double getLastSeconds() {
    return (lastTime == 0) ? 0
            : (System.nanoTime() - lastTime) / Math.pow(10, 9);
    
  }

  /**
   * @return the modeTime
   */
  public double getModeSeconds() {
    return (modeTime == 0) ? 0
                           : (System.nanoTime() - modeTime) / Math.pow(10, 9);
  }

  /**
   * @return the phaseDown
   */
  public int getPhaseDown() {
    return phaseDown;
  }

  /**
   * @return the phaseEvents
   */
  public int getPhaseEvents() {
    return phaseEvents;
  }

  /**
   * @return the phaseLeft
   */
  public int getPhaseLeft() {
    return phaseLeft;
  }

  /**
   *    @return
   */
  public double getPhaseRatio() {
    return (phaseEvents == 0) ? 0
                              : (phaseRotation / phaseEvents);
  }

  /**
   * @return the phaseRight
   */
  public int getPhaseRight() {
    return phaseRight;
  }

  /**
   * @return the phaseRotation
   */
  public int getPhaseRotation() {
    return phaseRotation;
  }

  /**
   * @return the phaseRotations
   */
  public ArrayList<Integer> getPhaseRotations() {
    return phaseRotations;
  }

  /**
   * @return the phaseTime
   */
  public double getPhaseSeconds() {
    return (phaseTime == 0) ? 0
                            : (System.nanoTime() - phaseTime) / Math.pow(10, 9);
  }

  /**
   * @return the phaseUp
   */
  public int getPhaseUp() {
    return phaseUp;
  }

  /**
   * @return the scrollRotation
   */
  public int getScrollRotation() {
    return scrollRotation;
  }

  /**
   *   @return the settings
   */
  public GestureAdapterSettings getSettings() {
    return settings;
  }

  /**
   * @return the coasting
   */
  public boolean isCoasting() {
    return coasting;
  }

  /**
   * @return the phasing
   */
  public boolean isPhasing() {
    return phasing;
  }

  /**
   * @return the scrolling
   */
  public boolean isScrolling() {
    return scrolling;
  }

  /**
   * @return the swiping
   */
  public boolean isSwiping() {
    return swiping;
  }

  /**
   * @param settings the settings to set
   */
  public void setSettings(GestureAdapterSettings settings) {
    this.settings = settings;
  }

///**
// * @param scrolling the scrolling to set
// */
//public void setScrolling(boolean scrolling) {
//  this.scrolling = scrolling;
//}

///**
// * @param swiping the swiping to set
// */
//public void setSwiping(boolean swiping) {
//  this.swiping = swiping;
//}
}
