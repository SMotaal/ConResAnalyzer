/*
 * @(#)ConResPatch.java   11/10/27
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

import com.grasppe.conres.framework.units.ContrastValue;
import com.grasppe.conres.framework.units.ResolutionValue;
import com.grasppe.morie.units.AbstractValue;

/**
 * @author daflair
 *
 */
public class ConResPatch extends GridPatch {
	
	/**
	 * @param row
	 * @param column
	 * @param xValue
	 * @param yValue
	 */
	public ConResPatch(int row, int column, ContrastValue xValue,
			ResolutionValue yValue) {
		super(row, column, xValue, yValue);
		// TODO Auto-generated constructor stub
	}

	//protected ContrastValue	xValue;
	//protected ResolutionValue yValue;
	
    /**
     * @return the xValue
     */
    public ContrastValue getXValue() {
        return (ContrastValue)xValue;
    }

    /**
     * @return the yValue
     */
    public ResolutionValue getYValue() {
        return (ResolutionValue)yValue;
    }
	
    /**
     * @param xValue the xValue to set
     */
    public void setXValue(ContrastValue xValue) {
        this.xValue = xValue;
    }

    /**
     * @param yValue the yValue to set
     */
    public void setYValue(ResolutionValue yValue) {
        this.yValue = yValue;
    }    
	
}
