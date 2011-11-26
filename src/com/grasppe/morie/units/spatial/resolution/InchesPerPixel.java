/*
 * @(#)CyclesPerMetre.java   11/10/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.morie.units.spatial.resolution;



//~--- JDK imports ------------------------------------------------------------

import java.io.InvalidClassException;

import com.grasppe.morie.units.AbstractValue;
import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.ValueTypes;
import com.grasppe.morie.units.spatial.SpatialFrequency;
import com.grasppe.morie.units.spatial.length.Inches;

/**
 * @author daflair
 *
 */
public class InchesPerPixel extends SpatialFrequency {

    public static final Pixel	denominatorValue = new Pixel(1);

    public static final Inches	numeratorValue = new Inches(1);

    public static UnitDefinition	unitDefinition = new UnitDefinition(ValueTypes.FREQUENCY,
                                                         "image resolution", "in/px");

    /**
     */
    public InchesPerPixel() {
        super();
    }

    /**
     * @param value
     */
    public InchesPerPixel(double value) {
        super(value);
    }

    /**
     * @param inputRate
     * @throws InvalidClassException
     */
    public InchesPerPixel(SpatialFrequency inputRate) throws InvalidClassException {
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
