/*
 * @(#)inches.java   11/10/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



package com.grasppe.conres.framework.units;

import com.grasppe.morie.units.ConcreteValue;
import com.grasppe.morie.units.ValueTypes;

/**
 * Class description
 *
 *
 * @version        Enter version here..., 11/10/24
 * @author         Enter your name here...
 */
public class ContrastValue extends ConcreteValue {

    /** Field description */
    public static ValueTypes	valueType = ValueTypes.DIGITALCOUNTS;

    /** Field description */
    public static final double	factor = 1.0;		// meters

    /** Field description */
    public static final String	prefix = "";

    /** Field description */
    public static final String	singular = "contrast";

    /** Field description */
    public static final String	suffix = "contrast";

    /** Field description */
    public static final String	symbol = "%";

    /** Field description */
    public static double
		maximum = 100.00,
		minimum = 0.00;

    /** Field description */
    protected static boolean
		hasMaximum = !Double.isNaN(maximum),
		hasMinimum = !Double.isNaN(minimum);

    /**
     * Constructs ...
     *
     */
    public ContrastValue() {
        this(1);
    }

    /**
     * Constructs ...
     *
     *
     * @param value
     */
    public ContrastValue(double value) {

        // super(value, factor, symbol, valueType, prefix, suffix, singular);
        super(value);
    }
}
