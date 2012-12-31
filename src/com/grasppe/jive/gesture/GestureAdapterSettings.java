/*
 * @(#)GestureAdapterSettings.java   12/12/25
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.gesture;

import com.grasppe.jive.gesture.GestureUtilities.GestureDirection;

/**
 * Class description
 * 	@version        $Revision: 1.0, 12/12/25
 * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
 */
public class GestureAdapterSettings {

  /**
	 * @param modeSecondsThreshold the modeSecondsThreshold to set
	 */
	public void setModeSecondsThreshold(double modeSecondsThreshold) {
		this.modeSecondsThreshold = modeSecondsThreshold;
	}

	/**
	 * @param swipeSecondsThreshold the swipeSecondsThreshold to set
	 */
	public void setSwipeSecondsThreshold(double swipeSecondsThreshold) {
		this.swipeSecondsThreshold = swipeSecondsThreshold;
	}

	/**
	 * @param swipeRatioThreshold the swipeRatioThreshold to set
	 */
	public void setSwipeRatioThreshold(double swipeRatioThreshold) {
		this.swipeRatioThreshold = swipeRatioThreshold;
	}

	/**
	 * @param swipeRatioUpThreshold the swipeRatioUpThreshold to set
	 */
	public void setSwipeRatioUpThreshold(double swipeRatioUpThreshold) {
		this.swipeRatioUpThreshold = swipeRatioUpThreshold;
	}

	/**
	 * @param swipeRatioDownThreshold the swipeRatioDownThreshold to set
	 */
	public void setSwipeRatioDownThreshold(double swipeRatioDownThreshold) {
		this.swipeRatioDownThreshold = swipeRatioDownThreshold;
	}

	/**
	 * @param swipeRatioLeftThreshold the swipeRatioLeftThreshold to set
	 */
	public void setSwipeRatioLeftThreshold(double swipeRatioLeftThreshold) {
		this.swipeRatioLeftThreshold = swipeRatioLeftThreshold;
	}

	/**
	 * @param swipeRatioRightThreshold the swipeRatioRightThreshold to set
	 */
	public void setSwipeRatioRightThreshold(double swipeRatioRightThreshold) {
		this.swipeRatioRightThreshold = swipeRatioRightThreshold;
	}

	/**
	 * @param swipeEventThreshold the swipeEventThreshold to set
	 */
	public void setSwipeEventThreshold(int swipeEventThreshold) {
		this.swipeEventThreshold = swipeEventThreshold;
	}

private double modeSecondsThreshold     = 1.0;
  private double swipeSecondsThreshold    = 0.15;
  private double swipeRatioThreshold      = 2.15;
  private double swipeRatioUpThreshold    = swipeRatioThreshold;
  private double swipeRatioDownThreshold  = swipeRatioThreshold;
  private double swipeRatioLeftThreshold  = swipeRatioThreshold - 0.25;
  private double swipeRatioRightThreshold = swipeRatioThreshold - 0.25;
  private int    swipeEventThreshold      = 15;

  /**
   * @return the modeSecondsThreshold
   */
  public double getModeSecondsThreshold() {
    return modeSecondsThreshold;
  }

  /**
   * @return the swipeEventThreshold
   */
  public int getSwipeEventThreshold() {
    return swipeEventThreshold;
  }

  /**
   * @return the swipeRatioThreshold
   */
  public double getSwipeRatioThreshold() {
    return swipeRatioThreshold;
  }

  /**
   * 	@param direction
   * 	@return
   */
  public double getSwipeRatioThreshold(GestureDirection direction) {
    if (direction == GestureDirection.UP) return swipeRatioUpThreshold;
    if (direction == GestureDirection.DOWN) return swipeRatioDownThreshold;
    if (direction == GestureDirection.LEFT) return swipeRatioLeftThreshold;
    if (direction == GestureDirection.RIGHT) return swipeRatioRightThreshold;

    return swipeRatioThreshold;
  }

  /**
   * @return the swipeSecondsThreshold
   */
  public double getSwipeSecondsThreshold() {
    return swipeSecondsThreshold;
  }
}
