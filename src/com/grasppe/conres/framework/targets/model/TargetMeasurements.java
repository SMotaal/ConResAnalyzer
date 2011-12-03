/*
 * @(#)TargetDimensions.java   11/11/23
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.targets.model;

/**
 * @author daflair
 */
public class TargetMeasurements extends TargetDimensions {

    protected float[]	xValues;
    protected float[]	yValues;
    protected float[]	zValues;

    /**
     */
    public TargetMeasurements() {
        super();
    }

    /**
     * @return the xValues
     */
    public float[] getXValues() {
        return xValues;
    }

    /**
     * @return the yValues
     */
    public float[] getYValues() {
        return yValues;
    }

    /**
     * @return the zValues
     */
    public float[] getZValues() {
        return zValues;
    }

    /**
     *  @param xValues
     *  @param yValues
     */
    public void setValues(float[] xValues, float[] yValues) {
        setXValues(xValues);
        setYValues(yValues);
    }

    /**
     *  @param xValues
     *  @param yValues
     *  @param zValues
     */
    public void setValues(float[] xValues, float[] yValues, float[] zValues) {
        setXValues(xValues);
        setYValues(yValues);
        setZValues(zValues);
    }

    /**
     * @param xValues the xValues to set
     */
    public void setXValues(float[] xValues) {
        this.xValues = xValues;
    }

    /**
     * @param yValues the yValues to set
     */
    public void setYValues(float[] yValues) {
        this.yValues = yValues;
    }

    /**
     * @param zValues the zValues to set
     */
    public void setZValues(float[] zValues) {
        this.zValues = zValues;
    }
}
