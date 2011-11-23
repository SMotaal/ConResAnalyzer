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

import com.grasppe.conres.framework.units.ToneValue;
import com.grasppe.conres.framework.units.ValueFactory;

/**
 * @author daflair
 *
 */
public final class ToneAxis extends GridAxis {

    /** Field description */
    protected ToneValue[]	values;

    /**
     * Constructs ...
     *
     *
     * @param steps
     * @param label
     * @param symbol
     */
    protected ToneAxis(double[] steps, String label, String symbol) {
        super(steps);		// , label, symbol);
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
        values = ValueFactory.CreateToneValues(steps);
    }
}
