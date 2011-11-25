/*
 * @(#)ValueFactory.java   11/10/27
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



package com.grasppe.conres.framework.units;

import com.grasppe.conres.framework.targets.model.GridAxis;
import com.grasppe.morie.units.AbstractValue;

/**
 * Class description
 *
 *
 * @version        Enter version here..., 11/10/27
 * @author         Enter your name here...
 */
public class ValueFactory {

    /**
     * @param values
     * @return
     */
    public static ContrastValue[] CreateContrastValues(double values[]) {
        ContrastValue[]	newValues = new ContrastValue[values.length];

        for (int i = 0; i < values.length; i++)
            newValues[i] = new ContrastValue(values[i]);

        return newValues;
    }

    /**
     * @param values
     * @return
     */
    public static ResolutionValue[] CreateResolutionValues(double values[]) {
        ResolutionValue[]	newValues = new ResolutionValue[values.length];

        for (int i = 0; i < values.length; i++)
            newValues[i] = new ResolutionValue(values[i]);

        return newValues;
    }

    /**
     * @param values
     * @return
     */
    public static ToneValue[] CreateToneValues(double values[]) {
        ToneValue[]	newValues = new ToneValue[values.length];

        for (int i = 0; i < values.length; i++)
            newValues[i] = new ToneValue(values[i]);

        return newValues;
    }

    /**
     * @param values
     * @param value
     * @return
     */
    public static AbstractValue[] CreateValues(double values[], AbstractValue value) {
        AbstractValue[]	newValues = new AbstractValue[values.length];

        for (int i = 0; i < values.length; i++) {
            newValues[i] = value.clone();
            newValues[i].setValue(values[i]);
        }

        return newValues;
    }
}
