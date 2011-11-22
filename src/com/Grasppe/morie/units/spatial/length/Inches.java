/*
 * @(#)inches.java   11/10/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



package com.grasppe.morie.units.spatial.length;

import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.ValueTypes;

/**
 * Class description
 *
 *
 * @version        Enter version here..., 11/10/24
 * @author         Enter your name here...
 */
public class Inches extends Length {

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition("inch", "inches", "in",
                                                         ValueTypes.LENGTH, 0.0254);

    /**
     *
     */
    public Inches() {
        super();
    }

    /**
     * @param value
     */
    public Inches(double value) {
        super(value);
    }

    /**
     * @param inputValue
     */
    public Inches(Length inputValue) {
        super(inputValue);
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
