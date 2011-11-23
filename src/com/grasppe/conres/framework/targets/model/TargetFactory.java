/*
 * @(#)TargetFactory.java   11/10/27
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
public class TargetFactory {

    /**
     * Method description
     *
     *
     * @param blockSteps
     * @param rowSteps
     * @param columnSteps
     *
     * @return
     */
    public static ConResTarget createConResTarget(double[] blockSteps, double[] rowSteps,
            double[] columnSteps) {
        ConResTarget	newTarget = new ConResTarget(blockSteps, rowSteps, columnSteps);

        return newTarget;
    }
}
