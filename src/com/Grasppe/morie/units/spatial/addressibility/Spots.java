/*
 * @(#)inches.java   11/10/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



package com.grasppe.morie.units.spatial.addressibility;

import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.spatial.Cycles;

/**
 * Class description
 *
 *
 * @version        Enter version here..., 11/10/24
 * @author         Enter your name here...
 */
public class Spots extends Cycles {

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition("spot", "spot",
                                                         "discrete image subunit", 1);

    /**
     *
     */
    public Spots() {
        super();
    }

    /**
     * @param inputValue
     */
    public Spots(Cycles inputValue) {
        super(inputValue);
    }

    /**
     * @param value
     */
    public Spots(double value) {
        super(value);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    protected UnitDefinition getDefinition() {
        return this.unitDefinition;
    }
}
