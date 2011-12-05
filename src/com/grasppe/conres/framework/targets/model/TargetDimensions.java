/*
 * @(#)TargetDimensions.java   11/11/23
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.targets.model;

import com.grasppe.lure.components.AbstractModel;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.util.Arrays;

/**
 * @author daflair
 */
public class TargetDimensions extends AbstractModel {

    float	xSpan    = 4.5F;
    float[]	xCenters = new float[] {
        2.3F, 8.1F, 14F, 19.8F, 25.7F, 31.5F, 37.4F, 43.2F, 49.1F, 54.9F, 60.7F, 66.5F, 72.3F, 78.1F
    };
    float	ySpan    = 4.5F;
    float[]	yCenters = new float[] {
        2.3F, 6.8F, 11.3F, 15.8F, 20.3F, 24.8F, 29.3F, 33.8F, 38.3F, 42.8F, 47.3F, 51.8F, 56.3F,
        60.8F
    };
    float	xRepeat = xCenters[1] - xCenters[0];
    float	yRepeat = yCenters[1] - yCenters[0];
    int		xCount  = xCenters.length;
    int		yCount  = yCenters.length;
    float[]	xBounds = new float[] { 0.0F, xCenters[xCount - 1] + xRepeat - xSpan };		// + xCenters[1] - xCenters[0] - xSpan };
    float[]	yBounds = new float[] { 0.0F, yCenters[yCount - 1] + yRepeat - ySpan };		// + yCenters[1] - yCenters[0] - ySpan };

    /**
     */
    public TargetDimensions() {}
    
    public String toString() {
    	String countString = "" + xCount + "x" + yCount;
    	Float xSize = new Float(xBounds[1]-xBounds[0]);
    	Float ySize = new Float(yBounds[1]-yBounds[0]);
    	String sizeString = "" + xSize.toString() + "x" + ySize.toString(); 
    	
    	return getClass().getSimpleName() + " " + sizeString + " [" + countString + " patches]";
    }

    /**
     * @return the bounds of the patch set in clockwise sequence
     */
    public float[] getPatchCorners() {

        float[]	tx = getXBounds();		// new float[] { 0.0F, xCenters[xCenters.length - 1] + xSpan / 2.0F };
        float[]	ty = getYBounds();		// new float[] { 0.0F, yCenters[yCenters.length - 1] + ySpan / 2.0F };
        float[]	cornerCoordinates = new float[] {
            tx[0], ty[0], tx[1], ty[0], tx[1], ty[1], tx[0], ty[1]
        };

        return cornerCoordinates;
    }

    /**
     * @return the xBounds
     */
    public float[] getXBounds() {
        xBounds = new float[] { 0.0F, getXCenters()[getXCount() - 1] + getXRepeat() - getXSpan()/2 };

        return xBounds;
    }

    /**
     * @return the xCenters
     */
    public float[] getXCenters() {
        return xCenters;
    }

    /**
     * @return the xCount
     */
    public int getXCount() {
        xCount = getXCenters().length;

        return xCount;
    }

    /**
     * @return the xRepeat
     */
    public float getXRepeat() {
        xRepeat = getXCenters()[1] - getXCenters()[0];

        return xRepeat;
    }

    /**
     * @return the xSpan
     */
    public float getXSpan() {
        return xSpan;
    }

    /**
     * @return the yBounds
     */
    public float[] getYBounds() {
        yBounds = new float[] { 0.0F, getYCenters()[getYCount() - 1] + getYRepeat() - getYSpan()/2 };

        return yBounds;
    }

    /**
     * @return the yCenters
     */
    public float[] getYCenters() {
        return yCenters;
    }

    /**
     * @return the yCount
     */
    public int getYCount() {
        yCount = getYCenters().length;

        return yCount;
    }

    /**
     * @return the yRepeat
     */
    public float getYRepeat() {
        yRepeat = getYCenters()[1] - getYCenters()[0];

        return yRepeat;
    }

    /**
     * @return the ySpan
     */
    public float getYSpan() {
        return ySpan;
    }

    /**
     *  @param xCenters
     *  @param yCenters
     *  @param xSpan
     *  @param ySpan
     */
    public void setDimensions(float[] xCenters, float[] yCenters, float xSpan, float ySpan) {
        setDimensions(xCenters, yCenters, xSpan, ySpan, new float[] {}, new float[] {});
    }

    /**
     *  @param xCenters
     *  @param yCenters
     *  @param xSpan
     *  @param ySpan
     *  @param xBounds
     *  @param yBounds
     */
    public void setDimensions(float[] xCenters, float[] yCenters, float xSpan, float ySpan,
                              float[] xBounds, float[] yBounds) {

        int	nX = xCenters.length;
        int	nY = yCenters.length;

        // TODO: Check sizes of arrays
        // boolean  mSizeEqual = xCenters.length == yCenters.length;
        boolean	mSizeValid = (nX > 0) && (nY > 0);
        boolean	tSizeEqual = xBounds.length == yBounds.length;
        boolean	tSizeValid = (xBounds.length <= 2);

        // TODO: Sort xCenters/yCenters in ascending order
        Arrays.sort(xCenters);
        Arrays.sort(yCenters);

        // TODO: Calculate tX1/tY1 if xBounds is empty
        if (xBounds.length == 0)
            xBounds = new float[] { xCenters[nX - 1] + xCenters[1] - xCenters[0] - xSpan };
        if (yBounds.length == 0)
            yBounds = new float[] { yCenters[nY - 1] + yCenters[1] - yCenters[0] - ySpan };

        // TODO: Add tX0/tY0 = 0.0F to single value tX1/tY1.
        if (xBounds.length == 1) xBounds = new float[] { 0.0F, xBounds[0] };
        if (yBounds.length == 1) yBounds = new float[] { 0.0F, yBounds[0] };

        // TODO: Check coordinates

        boolean	mMinValid = (xCenters[0] >= xBounds[0]) && (yCenters[0] >= yBounds[0]);
        boolean	mMaxValid = (xCenters[nX - 1] <= xBounds[1]) && (yCenters[nY - 1] <= yBounds[1]);

        // TODO: Check patch dimensions
        boolean	mPatchValid = true;		// until false

        for (int i = 0; i < nX; i++) {
            float	mX0 = (i == 0) ? 0
                                   : xCenters[i - 1];
            float	mX1 = xCenters[i];
            float	mY0 = (i == 0) ? 0
                                   : yCenters[i - 1];
            float	mY1 = yCenters[i];
            float	dmX = mX1 - mX0;
            float	dmY = mY1 - mY0;

            mPatchValid = mPatchValid && (dmX >= xSpan) && (dmY >= ySpan);
        }

        this.xSpan    = xSpan;
        this.ySpan    = ySpan;
        this.xBounds  = xBounds;
        this.yBounds  = yBounds;
        this.xCenters = xCenters;
        this.yCenters = yCenters;

    }

    /**
     *  @param dX
     */
    public void setXSpan(float dX) {
        this.xSpan = dX;
    }

    /**
     *  @param dY
     */
    public void setYSpan(float dY) {
        this.ySpan = dY;
    }
}
