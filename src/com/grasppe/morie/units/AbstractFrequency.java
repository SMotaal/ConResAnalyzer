/*
 * @(#)AbstractSpatialFrequency.java   11/10/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.morie.units;

import java.io.InvalidClassException;

/**
 * @author daflair
 *
 */
public abstract class AbstractFrequency extends AbstractRate {

    /** Field description */
    public static double	defaultValue = 0;

    /** Field description */
    public FrequencyTypes	frequencyType;

    /**
     * Constructs ...
     *
     */
    protected AbstractFrequency() {
        super();
    }

    /**
     * @param inputValue
     * @throws InvalidClassException
     */
    protected AbstractFrequency(AbstractRate inputValue) throws InvalidClassException {
        super(inputValue);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param value
     */
    protected AbstractFrequency(double value) {
        super(value);

        // TODO Auto-generated constructor stub
    }
}
