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

import com.grasppe.morie.units.AbstractValue;
import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.ValueTypes;
import com.grasppe.morie.units.spatial.SpatialFrequency;
import com.grasppe.morie.units.spatial.length.Micrometres;

//~--- JDK imports ------------------------------------------------------------

import java.io.InvalidClassException;

/**
 * @author daflair
 *
 */
public class MicronsPerSpot extends SpatialFrequency {

    /** Field description */
    public static final Micrometres	numeratorValue = new Micrometres(1);

    /** Field description */
    public static final Spots	denominatorValue = new Spots(1);

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition(ValueTypes.FREQUENCY,
                                                         "image addressibility", "µm/sp");

    /**
     * Constructs ...
     *
     */
    public MicronsPerSpot() {
        super();
    }

    /**
     * @param value
     */
    public MicronsPerSpot(double value) {
        super(value);
    }

    /**
     * @param inputRate
     * @throws InvalidClassException
     */
    public MicronsPerSpot(SpatialFrequency inputRate) throws InvalidClassException {
        super(inputRate);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    protected UnitDefinition getDefinition() {
        return unitDefinition;
    }

    /**
     * @return the denominatorValue
     */
    protected AbstractValue getDenominatorValue() {
        return denominatorValue;
    }

    /**
     * @return the numeratorValue
     */
    protected AbstractValue getNumeratorValue() {
        return numeratorValue;
    }
}
