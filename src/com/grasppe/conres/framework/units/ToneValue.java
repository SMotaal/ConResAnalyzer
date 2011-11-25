/*
 * @(#)inches.java   11/10/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



package com.grasppe.conres.framework.units;

import com.grasppe.morie.units.ValueTypes;
import com.grasppe.morie.units.AbstractValue;
import com.grasppe.morie.units.UnitDefinition;

/**
 * Class description
 *
 *
 * @version        Enter version here..., 11/10/24
 * @author         Enter your name here...
 */
public class ToneValue extends AbstractValue {

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition("tone step", "%", 
    											ValueTypes.DIGITALCOUNTS);

    /**
     */
    public ToneValue() {
        super();
    }

    /**
     * @param inputValue
     */
    public ToneValue(ToneValue inputValue) {
        super(inputValue);
    }

    /**
     * @param value
     */
    public ToneValue(double value) {
        super(value);
    }

    /**
     * @return
     */
    @Override
	protected UnitDefinition getDefinition() {
        return ToneValue.unitDefinition;
    }
}
