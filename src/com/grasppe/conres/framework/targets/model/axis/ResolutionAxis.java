/*
 * @(#)ContrastAxis.java   11/10/27
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.targets.model.axis;

import com.grasppe.conres.framework.units.ResolutionValue;

/**
 * @author daflair
 *
 */
public final class ResolutionAxis extends GridAxis {

    protected ResolutionValue[]	values;
    protected ResolutionValue
		minimumValue           = new ResolutionValue(0),
		maximumValue           = new ResolutionValue(100);
    protected String	label  = "Resolution, Line Pairs per Millimeter, Log steps",
						symbol = "l2/mm";

    /**
     * @param steps
     */
    public ResolutionAxis(double[] steps) {
        super(steps);
    }

    /**
     * @param value
     * @return
     */
    @Override
    public ResolutionValue createValue(double value) {
        return new ResolutionValue(value);
    }

    /**
     * @param length
     * @return
     */
    @Override
    public ResolutionValue[] createValueArray(int length) {
        return new ResolutionValue[length];
    }

    /**
     * @return the maximumValue
     */
    @Override
    public ResolutionValue getMaximumValue() {
        return maximumValue;
    }

    /**
     * @return the minimumValue
     */
    @Override
    public ResolutionValue getMinimumValue() {
        return minimumValue;
    }
}
