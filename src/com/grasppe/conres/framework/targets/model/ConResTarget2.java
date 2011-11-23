/*
 * @(#)ConResTarget2.java   11/10/27
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

/**
 * @author daflair
 *
 */
public class ConResTarget2 extends GridTarget {

    /** Field description */
    protected ConResBlock[]	testBlockes;

    /** Field description */
    protected ToneAxis	bAxis;

    /** Field description */
    protected ResolutionAxis	rAxis;

    /** Field description */
    protected ContrastAxis	cAxis;

    /**
     *
     * @param blockSteps
     * @param rowSteps
     * @param columnSteps
     */
    protected ConResTarget2(double[] blockSteps, double[] rowSteps, double[] columnSteps) {
        super();

        // Setup block axis
        bAxis = new ToneAxis(blockSteps, "Ref Tone.", "%");
        bAxis.setSteps(blockSteps);
        bAxis.generateValues();

        // Setup row axis
        rAxis = new ResolutionAxis(rowSteps, "Resolution, Line Pairs per Millimeter, Log steps",
                                     "l2/mm");
        rAxis.setSteps(rowSteps);
        rAxis.generateValues();

        // Setup column axis
        cAxis = new ContrastAxis(columnSteps, "Contrast, Log step increments", "%");
        cAxis.setSteps(columnSteps);
        cAxis.generateValues();

        // Create values for all axes steps
        // createAxesValues();
    }
}
