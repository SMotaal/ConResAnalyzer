/*
 * @(#)ConResPatch.java   11/10/27
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.targets.model.grid;

import com.grasppe.conres.framework.units.ContrastValue;
import com.grasppe.conres.framework.units.ResolutionValue;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * @author daflair
 */
public class ConResPatch extends GridPatch {

    /**
     * @param row
     * @param column
     * @param xValue
     * @param yValue
     */
    public ConResPatch(int row, int column, ContrastValue xValue, ResolutionValue yValue) {
        super(row, column, xValue, yValue);
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */

    private String[] columnLabels = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
    /**
     * 	@return
     */
    @Override
    public String toString() {
    	
    	
    	String positionString = "Position:\t" + columnLabels[getPatchColumn()] + getPatchRow();
    	String valueString = "Contrast:\t" + Math.round(getXValue().getValue())+"%\n" +
    			"Resolution:\t" + Math.round(getYValue().value*100)/100.0 + " " + getYValue().getSuffix(); //GrasppeKit.cat(getXValue().toString(), getYValue().toString());

        return positionString + "\n" + valueString;
    }

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
