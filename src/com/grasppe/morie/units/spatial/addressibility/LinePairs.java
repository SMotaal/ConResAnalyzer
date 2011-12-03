/*
 * @(#)inches.java   11/10/24
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



package com.grasppe.morie.units.spatial.addressibility;

import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.spatial.Cycles;

/**
 * Class description
 * @version        Enter version here..., 11/10/24
 * @author         Enter your name here...
 */
public class LinePairs extends Cycles {

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition("line-pair", "line-pair",
                                                      "discrete image subunit", 2.0);

    /**
     */
    public LinePairs() {
        super();
    }

    /**
     * @param inputValue
     */
    public LinePairs(Cycles inputValue) {
        super(inputValue);
    }

    /**
     * @param value
     */
    public LinePairs(double value) {
        super(value);
    }

    /**
     * @return
     */
    @Override
    protected UnitDefinition getDefinition() {
        return LinePairs.unitDefinition;
    }
}
