/*
 * @(#)ContrastAxis.java   11/10/27
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.targets.model;

import com.grasppe.conres.framework.units.ToneValue;

/**
 * @author daflair
 *
 */
public final class ToneAxis extends GridAxis {

    protected ToneValue[]	values;
    protected ToneValue
		minimumValue           = new ToneValue(0),
		maximumValue           = new ToneValue(100);
    protected String	label  = "Reference Tone Value",
						symbol = "%";

    /**
     * @param steps
     */
    protected ToneAxis(double[] steps) {
        super(steps);
    }

    /**
     * @param value
     * @return
     */
    @Override
    public ToneValue createValue(double value) {
        return new ToneValue(value);
    }

    /**
     * @param length
     * @return
     */
    @Override
    public ToneValue[] createValueArray(int length) {
        return new ToneValue[length];
    }

    /**
     * @return the maximumValue
     */
    @Override
    public ToneValue getMaximumValue() {
        return maximumValue;
    }

    /**
     * @return the minimumValue
     */
    @Override
    public ToneValue getMinimumValue() {
        return minimumValue;
    }
}
