/*
 * @(#)inches.java   11/10/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



package com.grasppe.morie.units.spatial.resolution;

import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.spatial.Cycles;

/**
 * Class description
 *
 *
 * @version        Enter version here..., 11/10/24
 * @author         Enter your name here...
 */
public class Dots extends Cycles {

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition("dot", "dot",
                                                         "discrete image unit", 1);

    /**
     *
     */
    public Dots() {
        super();
    }

    /**
     * @param inputValue
     */
    public Dots(Cycles inputValue) {
        super(inputValue);
    }

    /**
     * @param value
     */
    public Dots(double value) {
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
        return Dots.unitDefinition;
    }
}
