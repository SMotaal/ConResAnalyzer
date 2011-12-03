/*
 * @(#)inches.java   11/10/24
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



package com.grasppe.morie.units.spatial.length;

import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.ValueTypes;

/**
 * Class description
 * @version        Enter version here..., 11/10/24
 * @author         Enter your name here...
 */
public class Millimetres extends Length {

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition("millimetre", "mm",
                                                      ValueTypes.LENGTH, Math.pow(10, -3));

    /**
     */
    public Millimetres() {
        super();
    }

    /**
     * @param value
     */
    public Millimetres(double value) {
        super(value);
    }

    /**
     * @param inputValue
     */
    public Millimetres(Length inputValue) {
        super(inputValue);
    }

    /**
     * @return
     */
    @Override
    protected UnitDefinition getDefinition() {
        return Millimetres.unitDefinition;
    }
}
