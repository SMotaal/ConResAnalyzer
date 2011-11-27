/*
 * @(#)GridPatch.java   11/10/26
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.targets.model.grid;

import com.grasppe.morie.units.AbstractValue;

/**
 * @author daflair
 */
public class GridPatch { //extends ObservableObject {

    /** Field description */
    protected int			patchRow, patchColumn;
    protected AbstractValue	xValue,	yValue;
    
    public GridPatch() {
    	super();
    }
    
    public GridPatch(int row, int column, AbstractValue xValue, AbstractValue yValue) {
    	this.patchRow = row;
    	this.patchColumn = column;
//    	this.xValue = xValue;
//    	this.yValue = yValue;
    	setXValue(xValue);
    	setYValue(yValue);
    }

    /**
     * @return the xValue
     */
    public AbstractValue getXValue() {
        return xValue;
    }

    /**
     * @return the yValue
     */
    public AbstractValue getYValue() {
        return yValue;
    }

    /**
     * @param xValue the xValue to set
     */
    public void setXValue(AbstractValue xValue) {
        this.xValue = xValue;
    }

    /**
     * @param yValue the yValue to set
     */
    public void setYValue(AbstractValue yValue) {
        this.yValue = yValue;
    }

    /**
     * @return the patchColumn
     */
    public int getPatchColumn() {
        return patchColumn;
    }

    /**
     * @return the patchRow
     */
    public int getPatchRow() {
        return patchRow;
    }

    /**
     * @param patchColumn the patchColumn to set
     */
    public void setPatchColumn(int patchColumn) {
        this.patchColumn = patchColumn;
    }

    /**
     * @param patchRow the patchRow to set
     */
    public void setPatchRow(int patchRow) {
        this.patchRow = patchRow;
    }
}
