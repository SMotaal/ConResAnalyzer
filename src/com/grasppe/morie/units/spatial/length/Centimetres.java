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
public class Centimetres extends Length {

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition("centimetre", "cm",
                                                      ValueTypes.LENGTH, Math.pow(10, -2));

    /**
     */
    public Centimetres() {
        super();
    }

    /**
     * @param value
     */
    public Centimetres(double value) {
        super(value);
    }

    /**
     * @param inputValue
     */
    public Centimetres(Length inputValue) {
        super(inputValue);
    }

    /**
     * @return
     */
    @Override
    protected UnitDefinition getDefinition() {
        return Centimetres.unitDefinition;
    }
}
