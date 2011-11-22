/*
 * @(#)ContrastAxis.java   11/10/27
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.targets.model;

import com.grasppe.conres.framework.units.ResolutionValue;
import com.grasppe.conres.framework.units.ValueFactory;

/**
 * @author daflair
 *
 */
public final class ResolutionAxis extends GridAxis {

    /** Field description */
    protected ResolutionValue[]	values;

    /** Field description */
    protected String	label, symbol;

    /**
     * Constructs ...
     *
     *
     * @param steps
     * @param label
     * @param symbol
     */
    protected ResolutionAxis(double[] steps, String label, String symbol) {

        // super(steps, label, symbol);
        super(steps);
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.conresalpha.targets.GridAxis#createValues(double[])
     */

    /**
     * Method description
     *
     *
     */
    @Override
    protected void generateValues() {

        // TODO Auto-generated method stub
        values = ValueFactory.CreateResolutionValues(steps);
    }
}
