/*
 * @(#)ConResTarget.java   11/10/27
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.targets.model.grid;

import com.grasppe.conres.framework.targets.model.TargetDimensions;
import com.grasppe.conres.framework.targets.model.axis.ContrastAxis;
import com.grasppe.conres.framework.targets.model.axis.ResolutionAxis;
import com.grasppe.conres.framework.targets.model.axis.ToneAxis;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * @author daflair
 */
public class ConResTarget extends GridTarget {

    protected static ToneAxis		zAxis;
    protected static ResolutionAxis	yAxis;
    protected static ContrastAxis	xAxis;

    /** Field description */
    protected ConResBlock[]		targetBlocks;
    protected TargetDimensions	dimensions;

    /**
     * @param blockSteps
     * @param rowSteps
     * @param columnSteps
     */
    protected ConResTarget(double[] blockSteps, double[] rowSteps, double[] columnSteps) {
        super();

        int	nBlocks = blockSteps.length;

        // Setup block axis
        zAxis        = new ToneAxis(blockSteps);

        targetBlocks = new ConResBlock[nBlocks];

        for (int i = 0; i < nBlocks; i++) {		// (AbstractValue value : zAxis.getValues())
            targetBlocks[i] = new ConResBlock(blockSteps[i], columnSteps, rowSteps);
        }

        // Setup row axis
        yAxis = new ResolutionAxis(rowSteps);

        // Setup column axis
        xAxis = new ContrastAxis(columnSteps);

    }

    /**
     *  @param blockToneValues
     *  @param yValues
     *  @param xValues
     */
    public ConResTarget(int[] blockToneValues, float[] yValues, float[] xValues) {
        this(asDouble(blockToneValues), asDouble(yValues), asDouble(xValues));
    }

    /**
     *  @param array
     *  @return
     */
    public static double[] asDouble(float[] array) {
        double[]	newArray = new double[array.length];

        for (int i = 0; i < array.length; i++)		// float value : blockToneValues)
            newArray[i] = (double)array[i];

        return newArray;
    }

    /**
     *  @param array
     *  @return
     */
    public static double[] asDouble(int[] array) {
        double[]	newArray = new double[array.length];

        for (int i = 0; i < array.length; i++)		// float value : blockToneValues)
            newArray[i] = (double)array[i];

        return newArray;
    }

    /**
     * @return the xAxis
     */
    public static ContrastAxis getxAxis() {
        return xAxis;
    }

    /**
     * @return the yAxis
     */
    public static ResolutionAxis getyAxis() {
        return yAxis;
    }

    /**
     * @return the zAxis
     */
    public static ToneAxis getzAxis() {
        return zAxis;
    }

    /**
     *  @return
     */
    public String toString() {
        try {
            return (GrasppeKit.cat(new String[] { getClass().getSimpleName(),
                    " (" + zAxis.getSteps().length + "x" + xAxis.getSteps().length + "x"
                    + yAxis.getSteps().length + ")" }));
        } catch (Exception exception) {
            return "";
        }
    }

    /**
     * @return the dimensions
     */
    public TargetDimensions getDimensions() {
        return dimensions;
    }

    /**
     * @return the targetBlocks
     */
    public ConResBlock[] getTargetBlocks() {
        return targetBlocks;
    }
}
