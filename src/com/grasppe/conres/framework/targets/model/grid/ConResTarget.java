/*
 * @(#)ConResTarget.java   11/10/27
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

import java.lang.reflect.Array;

import com.grasppe.conres.framework.targets.model.TargetDimensions;
import com.grasppe.conres.framework.targets.model.axis.ContrastAxis;
import com.grasppe.conres.framework.targets.model.axis.ResolutionAxis;
import com.grasppe.conres.framework.targets.model.axis.ToneAxis;
import com.grasppe.conres.framework.units.ContrastValue;
import com.grasppe.conres.framework.units.ResolutionValue;
import com.grasppe.conres.framework.units.ToneValue;
import com.grasppe.morie.units.AbstractValue;

/**
 * @author daflair
 *
 */
public class ConResTarget extends GridTarget {

    /**
	 * @return the targetBlocks
	 */
	public ConResBlock[] getTargetBlocks() {
		return targetBlocks;
	}

	/**
	 * @return the zAxis
	 */
	public static ToneAxis getzAxis() {
		return zAxis;
	}

	/**
	 * @return the yAxis
	 */
	public static ResolutionAxis getyAxis() {
		return yAxis;
	}

	/**
	 * @return the xAxis
	 */
	public static ContrastAxis getxAxis() {
		return xAxis;
	}

	/**
	 * @return the dimensions
	 */
	public TargetDimensions getDimensions() {
		return dimensions;
	}
	/** Field description */
    protected ConResBlock[]	targetBlocks;
    protected static ToneAxis	zAxis;
    protected static ResolutionAxis	yAxis;
    protected static ContrastAxis	xAxis;
    protected TargetDimensions dimensions;

    /**
     *
     * @param blockSteps
     * @param rowSteps
     * @param columnSteps
     */
    protected ConResTarget(double[] blockSteps, double[] rowSteps, double[] columnSteps) {
        super();
        
        int nBlocks = blockSteps.length;
        
        // Setup block axis
        zAxis = new ToneAxis(blockSteps);
        
        targetBlocks = new ConResBlock[nBlocks];
        
        for (int i = 0; i < nBlocks; i++) { //(AbstractValue value : zAxis.getValues())
        	targetBlocks[i] = new ConResBlock(blockSteps[i], columnSteps, rowSteps);
        }
        
        // Setup row axis
        yAxis = new ResolutionAxis(rowSteps);

        // Setup column axis
        xAxis = new ContrastAxis(columnSteps);
        
    }

	public ConResTarget(int[] blockToneValues, float[] yValues,
			float[] xValues) {
		this(asDouble(blockToneValues), asDouble(yValues), asDouble(xValues));
	}

	public static double[] asDouble(int[] array){
		double[] newArray = new double[array.length];
		for (int i=0; i<array.length; i++)//float value : blockToneValues)
			newArray[i] = (double)array[i];		
		return newArray;
	}
	public static double[] asDouble(float[] array){
		double[] newArray = new double[array.length];
		for (int i=0; i<array.length; i++)//float value : blockToneValues)
			newArray[i] = (double)array[i];		
		return newArray;
	}
    
}
