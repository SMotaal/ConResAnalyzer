/*
 * @(#)TargetBlockModel.java   11/10/26
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */

package com.grasppe.conres.framework.targets.model;

import com.grasppe.morie.units.AbstractValue;

/**
 * @author daflair
 */
public class GridBlock {	// extends ObservableObject {

    /**
	 * @return the xAxis
	 */
	public GridAxis getXAxis() {
		return xAxis;
	}

	/**
	 * @param xAxis the xAxis to set
	 */
	public void setXAxis(GridAxis xAxis) {
		this.xAxis = xAxis;
	}

	/**
	 * @return the yAxis
	 */
	public GridAxis getYAxis() {
		return yAxis;
	}

	/**
	 * @param yAxis the yAxis to set
	 */
	public void setYAxis(GridAxis yAxis) {
		this.yAxis = yAxis;
	}

	protected static String
		xLabel = "",
		yLabel = "";
    protected static String
		xUnit  = "",
		yUnit  = "";
    protected GridAxis			xAxis, yAxis;
    protected int				rows, columns;
    protected AbstractValue[]	xValues;
    protected AbstractValue[]	yValues;

    /**
     * @param xValues
     * @param yValues
     */
    public GridBlock(AbstractValue[] xValues, AbstractValue[] yValues) {
        super();
        rows    = yValues.length;
        columns = xValues.length;
        setValues(xValues, yValues);
    }
    
    public GridBlock(double[] xValues, double[] yValues) {
        super();
        rows    = yValues.length;
        columns = xValues.length;
    	generateAxes(xValues, yValues);
    	GridAxis axis =  getXAxis();
    	this.toString();
    	AbstractValue[] values = getXAxis().getValues();
    	setValues(getXAxis().getValues(),getYAxis().getValues());
    }
    
    public void generateAxes(double[] xValues, double[] yValues) {
    	setXAxis(null);
    	setYAxis(null);
    }

    /**
     * @param row
     * @param column
     * @return
     */
    public GridPatch getPatch(int row, int column) {
        return new GridPatch(row, column, getXValue(column), getYValue(row));
    }

    /**
     * @return the xLabel
     */
    public static String getXLabel() {
        return xLabel;
    }

    /**
     * @return the xUnit
     */
    public static String getXUnit() {
        return xUnit;
    }

    /**
     * @param column
     *
     * @return
     */
    public AbstractValue getXValue(int column) {
        return xValues[column];
    }

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
     *
     * @return
     */
    public AbstractValue getYValue(int row) {
        return yValues[row];
    }

    /**
     * @param xValues
     * @param yValues
     */
    public void setValues(AbstractValue[] xValues, AbstractValue[] yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
    }
}
