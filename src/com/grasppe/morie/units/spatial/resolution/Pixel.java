/*
 * @(#)inches.java   11/10/24
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



package com.grasppe.morie.units.spatial.resolution;

import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.spatial.Cycles;

/**
 * Class description
 * @version        Enter version here..., 11/10/24
 * @author         Enter your name here...
 */
public class Pixel extends Cycles {

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition("pixel", "px",
                                                      "discrete image unit", 1);

    /**
     */
    public Pixel() {
        super();
    }

    /**
     * @param inputValue
     */
    public Pixel(Cycles inputValue) {
        super(inputValue);
    }

    /**
     * @param value
     */
    public Pixel(double value) {
        super(value);
    }

    /**
     * @return
     */
    @Override
    protected UnitDefinition getDefinition() {
        return Pixel.unitDefinition;
    }
}
