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
package com.grasppe.morie.units.spatial.addressibility;



//~--- JDK imports ------------------------------------------------------------

import java.io.InvalidClassException;

import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.ValueTypes;
import com.grasppe.morie.units.spatial.SpatialFrequency;
import com.grasppe.morie.units.spatial.length.Millimetres;

/**
 * @author daflair
 *
 */
public class MillimetresPerSpot extends SpatialFrequency {

    /** Field description */
    public static final Millimetres	numeratorValue = new Millimetres(1);

    /** Field description */
    public static final Spots	denominatorValue = new Spots(1);

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition(ValueTypes.FREQUENCY,
                                                         "image addressibility", "mm/sp");

    /**
     * Constructs ...
     *
     */
    public MillimetresPerSpot() {
        super();
    }

    /**
     * @param value
     */
    public MillimetresPerSpot(double value) {
        super(value);
    }

    /**
     * @param inputRate
     * @throws InvalidClassException
     */
    public MillimetresPerSpot(SpatialFrequency inputRate) throws InvalidClassException {
        super(inputRate);
    }

    /**
     * Method description
     *
     *
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
	protected Spots getDenominatorValue() {
        return denominatorValue;
    }

    /**
     * @return the numeratorValue
     */
    @Override
	protected Millimetres getNumeratorValue() {
        return numeratorValue;
    }
}
