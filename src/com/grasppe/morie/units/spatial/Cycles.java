/*
 * @(#)inches.java   11/10/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



package com.grasppe.morie.units.spatial;

import com.grasppe.morie.units.AbstractValue;
import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.ValueTypes;

/**
 * Class description
 *
 *
 * @version        Enter version here..., 11/10/24
 * @author         Enter your name here...
 */
public class Cycles extends AbstractValue {

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition("cycle", "cycle",
                                                         ValueTypes.UNITS);

    /**
     *
     */
    public Cycles() {
        super();
    }

    /**
     * @param inputValue
     */
    public Cycles(Cycles inputValue) {
        super(inputValue);
    }

    /**
     * @param value
     */
    public Cycles(double value) {
        super(value);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
	protected UnitDefinition getDefinition() {
        return Cycles.unitDefinition;
    }
}
