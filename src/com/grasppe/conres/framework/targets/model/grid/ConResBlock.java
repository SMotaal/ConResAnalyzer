/*
 * @(#)ConResBlock.java   11/10/27
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.targets.model.grid;

import com.grasppe.conres.framework.targets.model.axis.ContrastAxis;
import com.grasppe.conres.framework.targets.model.axis.ResolutionAxis;
import com.grasppe.conres.framework.units.ContrastValue;
import com.grasppe.conres.framework.units.ResolutionValue;
import com.grasppe.conres.framework.units.ToneValue;

/**
 * @author daflair
 *
 */
public class ConResBlock extends GridBlock {

    protected static String
		xLabel = "Contrast, Log step increments",
		yLabel = "Resolution, Line Pairs per Millimeter, Log steps";

    protected static String
		xUnit = "%",
		yUnit = "l2/mm";

    protected ToneValue			zValue;
//    protected ContrastAxis		xAxis;
//    protected ResolutionAxis	yAxis;

    /**
     * Constructs ...
     *
     * @param toneValue
     * @param xValues
     * @param yValues
     */
    public ConResBlock(double toneValue, double[] xValues, double[] yValues) {
        super(xValues, yValues);
        this.zValue = new ToneValue(toneValue);
    }

    /**
     * @param toneValue
     * @param xValues
     * @param yValues
     */
    public ConResBlock(ToneValue toneValue, ContrastValue[] xValues, ResolutionValue[] yValues) {
        super(xValues, yValues);
        this.zValue = toneValue;
    }

    /**
     * Method description
     * @param toneValue
     * @param xValues
     * @param yValues
     * @return
     */
    public static ConResBlock buildBlock(double toneValue, double[] xValues, double[] yValues) {
        int					xSteps       = xValues.length;
        int					ySteps       = yValues.length;
        ToneValue			newToneValue = new ToneValue(toneValue);
        ContrastValue[]		newXValues   = new ContrastValue[xSteps];
        ResolutionValue[]	newYValues   = new ResolutionValue[ySteps];

        for (int xi = 0; xi < xSteps; xi++)
            newXValues[xi] = new ContrastValue(xValues[xi]);
        for (int yi = 0; yi < ySteps; yi++)
            newYValues[yi] = new ResolutionValue(yValues[yi]);

        return new ConResBlock(newToneValue, newXValues, newYValues);
    }

    /**
     * Method description
     *
     * @param xValues
     * @param yValues
     */
    @Override
    public void generateAxes(double[] xValues, double[] yValues) {
        setXAxis(new ContrastAxis(xValues));
        setYAxis(new ResolutionAxis(yValues));
    }

    /**
     * @param row
     * @param column
     * @return
     */
    public ConResPatch getPatch(int row, int column) {
        return (ConResPatch)super.getPatch(row, column);
    }
//
//    /**
//     * @return the xAxis
//     */
//    public ContrastAxis getXAxis() {
//        return xAxis;
//    }

    /**
     * @return the yLabel
     */
    public static String getXLabel() {
        return xLabel;
    }

    /**
     * @return the yUnit
     */
    public static String getXUnit() {
        return xUnit;
    }

    /**
     * @param column
     * @return
     */
    public ContrastValue getXValue(int column) {
        return (ContrastValue)super.getXValue(column);
    }
//
//    /**
//     * @return the yAxis
//     */
//    public ResolutionAxis getYAxis() {
//        return yAxis;
//    }

    /**
     * @return the yLabel
     */
    public static String getYLabel() {
        return yLabel;
    }

    /**
     * @return the yUnit
     */
    public static String getYUnit() {
        return yUnit;
    }

    /**
     * @param row
     * @return
     */
    public ResolutionValue getYValue(int row) {
        return (ResolutionValue)super.getYValue(row);
    }
}
