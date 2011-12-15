/*
 * @(#)PatchDimensions.java   11/12/15
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.targets.model;

import com.grasppe.lure.components.AbstractModel;

/**
 * @author daflair
 *
 */
public class PatchDimensions extends AbstractModel {

    float	xCenter, yCenter;
    float	xSpan, ySpan;
    float	xRepeat, yRepeat;
    float[]	xCorners, yCorners;

    /**
     * @param xCenter
     * @param yCenter
     * @param xSpan
     * @param ySpan
     * @param xRepeat
     * @param yRepeat
     * @param xCorners
     * @param yCorners
     */
    public PatchDimensions(float xCenter, float yCenter, float xSpan, float ySpan, float xRepeat,
                           float yRepeat, float[] xCorners, float[] yCorners) {
        super();
        this.xCenter  = xCenter;
        this.yCenter  = yCenter;
        this.xSpan    = xSpan;
        this.ySpan    = ySpan;
        this.xRepeat  = xRepeat;
        this.yRepeat  = yRepeat;
        this.xCorners = xCorners;
        this.yCorners = yCorners;
    }

    /**
     * @return the xCenter
     */
    public float getXCenter() {
        return xCenter;
    }

    /**
     * @return the xCorners
     */
    public float[] getXCorners() {
        return xCorners;
    }

    /**
     * @return the xRepeat
     */
    public float getXRepeat() {
        return xRepeat;
    }

    /**
     * @return the xSpan
     */
    public float getXSpan() {
        return xSpan;
    }

    /**
     * @return the yCenter
     */
    public float getYCenter() {
        return yCenter;
    }

    /**
     * @return the yCorners
     */
    public float[] getYCorners() {
        return yCorners;
    }

    /**
     * @return the yRepeat
     */
    public float getYRepeat() {
        return yRepeat;
    }

    /**
     * @return the ySpan
     */
    public float getYSpan() {
        return ySpan;
    }

//  /**
//   *
//   */
//  public PatchDimensions() {}

//  /**
//   * @param controller
//   */
//  public PatchDimensions(AbstractController controller) {
//    super(controller);
//  }

}
