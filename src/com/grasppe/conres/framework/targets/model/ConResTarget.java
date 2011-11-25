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
package com.grasppe.conres.framework.targets.model;

import com.grasppe.conres.framework.units.ContrastValue;
import com.grasppe.conres.framework.units.ResolutionValue;
import com.grasppe.conres.framework.units.ToneValue;
import com.grasppe.morie.units.AbstractValue;

/**
 * @author daflair
 *
 */
public class ConResTarget extends GridTarget {

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
    
}
