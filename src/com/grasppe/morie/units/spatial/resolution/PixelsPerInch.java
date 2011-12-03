/*
 * @(#)CyclesPerMetre.java   11/10/24
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.morie.units.spatial.resolution;

import com.grasppe.morie.units.AbstractValue;
import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.ValueTypes;
import com.grasppe.morie.units.spatial.SpatialFrequency;
import com.grasppe.morie.units.spatial.length.Inches;

//~--- JDK imports ------------------------------------------------------------

import java.io.InvalidClassException;

/**
 * @author daflair
 */
public class PixelsPerInch extends SpatialFrequency {

    /** Field description */
    public static final Pixel	numeratorValue = new Pixel(1);

    /** Field description */
    public static final Inches	denominatorValue = new Inches(1);

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition(ValueTypes.FREQUENCY,
                                                      "image resolution", "ppi");

    /**
     */
    public PixelsPerInch() {
        super();
    }

    /**
     * @param value
     */
    public PixelsPerInch(double value) {
        super(value);
    }

    /**
     * @param inputRate
     * @throws InvalidClassException
     */
    public PixelsPerInch(SpatialFrequency inputRate) throws InvalidClassException {
        super(inputRate);
    }

    /**
     * @return
     */
    @Override
    protected UnitDefinition getDefinition() {
        return unitDefinition;
    }

    /**
     * @return the denominatorValue
     */
    @Override
    protected AbstractValue getDenominatorValue() {
        return denominatorValue;
    }

    /**
     * @return the numeratorValue
     */
    @Override
    protected AbstractValue getNumeratorValue() {
        return numeratorValue;
    }
}
