/*
 * @(#)ContrastAxis.java   11/10/27
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.targets.model.axis;

import com.grasppe.conres.framework.units.ContrastValue;

/**
 * @author daflair
 *
 */
public final class ContrastAxis extends GridAxis {

    protected ContrastValue[]	values;
    protected ContrastValue
		minimumValue           = new ContrastValue(0),
		maximumValue           = new ContrastValue(100);
    protected String	label  = "Contrast, Log step increments",
						symbol = "%";

    /**
     * @param steps
     */
    public ContrastAxis(double[] steps) {
        super(steps);
    }

    /**
     * @param value
     * @return
     */
    @Override
    public ContrastValue createValue(double value) {
        return new ContrastValue(value);
    }

    /**
     * @param length
     * @return
     */
    @Override
    public ContrastValue[] createValueArray(int length) {
        return new ContrastValue[length];
    }

//    /**
//     * @return the maximumValue
//     */
//    @Override
//    public ContrastValue getMaximumValue() {
//        return maximumValue;
//    }
//
//    /**
//     * @return the minimumValue
//     */
//    @Override
//    public ContrastValue getMinimumValue() {
//        return minimumValue;
//    }
}
