/*
 * @(#)TargetDimensions.java   11/11/23
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.targets.model;

import com.grasppe.lure.components.AbstractModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.Arrays;

/**
 * @author daflair
 */
public class TargetDimensions extends AbstractModel {

    float	dX = 4.5F;
    float[]	mX = new float[] {
        2.3F, 8.1F, 14F, 19.8F, 25.7F, 31.5F, 37.4F, 43.2F, 49.1F, 54.9F, 60.7F, 66.5F, 72.3F, 78.1F
    };
    float	dY = 4.5F;
    float[]	mY = new float[] {
        2.3F, 6.8F, 11.3F, 15.8F, 20.3F, 24.8F, 29.3F, 33.8F, 38.3F, 42.8F, 47.3F, 51.8F, 56.3F,
        60.8F
    };
    float	dW = mX[1] - mX[0];
    float	dH = mY[1] - mY[0];
    int		mN = mX.length;
    float[]	tX = new float[] { 0.0F, mX[mN - 1] + mX[1] - mX[0] - dX };
    float[]	tY = new float[] { 0.0F, mY[mN - 1] + mY[1] - mY[0] - dY };

    /**
     */
    public TargetDimensions() {

        // TODO Auto-generated constructor stub
    }

    /**
     * @return the dH
     */
    public float getDH() {
        return dH;
    }

    /**
     * @return the dW
     */
    public float getDW() {
        return dW;
    }

    /*
     * Target Coordinates
     * Fiducials ULC: (0, 0) URC: (58.5, 0) LRC: (58.5, 45.0)   LLC: (0, 45.0)
     */

    /**
     * @return the dX
     */
    public float getDX() {
        return dX;
    }

    /**
     * @return the dY
     */
    public float getDY() {
        return dY;
    }

    /**
     * @return the mN
     */
    public int getMN() {
        return mN;
    }

    /**
     * @return the mX
     */
    public float[] getMX() {
        return mX;
    }

    /**
     * @return the mY
     */
    public float[] getMY() {
        return mY;
    }

//  /**
//   * @param controller
//   */
//  public TargetDimensions(AbstractController controller) {
//      super(controller);
//
//      // TODO Auto-generated constructor stub
//  }

    /**
     * @return
     */
    public float[] getPatchCorners() {

        float[]	tx                = new float[] { 0.0F, mX[mX.length - 1] + dX / 2.0F };
        float[]	ty                = new float[] { 0.0F, mY[mY.length - 1] + dY / 2.0F };
        float[]	cornerCoordinates = new float[] {
            tx[0], ty[0], tx[1], ty[0], tx[1], ty[1], tx[0], ty[1]
        };

        return cornerCoordinates;

    }

    /**
     * @return the tX
     */
    public float[] getTX() {
        return tX;
    }

    /**
     * @return the tY
     */
    public float[] getTY() {
        return tY;
    }

    /**
     * @param dH the dH to set
     */
    public void setDH(float dH) {
        this.dH = dH;
    }

    /**
     * @param dW the dW to set
     */
    public void setDW(float dW) {
        this.dW = dW;
    }

    /**
     * @param dX the dX to set
     */
    public void setDX(float dX) {
        this.dX = dX;
    }

    /**
     * @param dY the dY to set
     */
    public void setDY(float dY) {
        this.dY = dY;
    }

    /**
     * @param mX
     * @param mY
     * @param dX
     * @param dY
     * @param tX
     * @param tY
     */
    public void setDimensions(float[] mX, float[] mY, float dX, float dY, float[] tX, float[] tY) {

        int	nX = mX.length;
        int	nY = mY.length;

        // TODO: Check sizes of arrays
        // boolean  mSizeEqual = mX.length == mY.length;
        boolean	mSizeValid = (nX > 0) && (nY > 0);
        boolean	tSizeEqual = tX.length == tY.length;
        boolean	tSizeValid = (tX.length <= 2);

        // TODO: Sort mX/mY in ascending order
        Arrays.sort(mX);
        Arrays.sort(mY);

        // TODO: Calculate tX1/tY1 if tX is empty
        if (tX.length == 0) tX = new float[] { mX[nX - 1] + mX[1] - mX[0] - dX };
        if (tY.length == 0) tY = new float[] { mY[nY - 1] + mY[1] - mY[0] - dY };

        // TODO: Add tX0/tY0 = 0.0F to single value tX1/tY1.
        if (tX.length == 1) tX = new float[] { 0.0F, tX[0] };
        if (tY.length == 1) tY = new float[] { 0.0F, tY[0] };

        // TODO: Check coordinates

        boolean	mMinValid = (mX[0] >= tX[0]) && (mY[0] >= tY[0]);
        boolean	mMaxValid = (mX[nX - 1] <= tX[1]) && (mY[nY - 1] <= tY[1]);

        // TODO: Check patch dimensions
        boolean	mPatchValid = true;		// until false

        for (int i = 0; i < nX; i++) {
            float	mX0 = (i == 0) ? 0
                                   : mX[i - 1];
            float	mX1 = mX[i];
            float	mY0 = (i == 0) ? 0
                                   : mY[i - 1];
            float	mY1 = mY[i];
            float	dmX = mX1 - mX0;
            float	dmY = mY1 - mY0;

            mPatchValid = mPatchValid && (dmX >= dX) && (dmY >= dY);
        }

        this.dX = dX;
        this.dY = dY;
        this.tX = tX;
        this.tY = tY;
        this.mX = mX;
        this.mY = mY;

    }

    /**
     * @param mN the mN to set
     */
    public void setMN(int mN) {
        this.mN = mN;
    }

    /**
     * @param mX the mX to set
     */
    public void setMX(float[] mX) {
        this.mX = mX;
    }

    /**
     * @param mY the mY to set
     */
    public void setMY(float[] mY) {
        this.mY = mY;
    }

    /**
     * @param tX the tX to set
     */
    public void setTX(float[] tX) {
        this.tX = tX;
    }

    /**
     * @param tY the tY to set
     */
    public void setTY(float[] tY) {
        this.tY = tY;
    }
}
