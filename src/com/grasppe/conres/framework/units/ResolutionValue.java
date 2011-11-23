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
package com.grasppe.conres.framework.units;



//~--- JDK imports ------------------------------------------------------------

import java.io.InvalidClassException;

import com.grasppe.morie.units.spatial.SpatialFrequency;
import com.grasppe.morie.units.spatial.addressibility.LinePairsPerMillimetre;

/**
 * @author daflair
 *
 */
public class ResolutionValue extends LinePairsPerMillimetre {

//  /** Field description */
//  public static final Millimetres   denominatorValue = new Millimetres(1);
//
//  /** Field description */
//  public static final ConcreteValue numeratorValue = new ConcreteValue("line-pairs",
//                                                           "line-pair", "addressibility", 2);
//
//    /** Field description */
//    public static final String    symbol = "lp/mm";

    /**
     * Constructs ...
     *
     */
    public ResolutionValue() {
        super();
    }

    /**
     * @param value
     */
    public ResolutionValue(double value) {
        super(value);
    }

    /**
     * @param inputRate
     * @throws InvalidClassException
     */
    protected ResolutionValue(SpatialFrequency inputRate) throws InvalidClassException {
        super(inputRate);
    }
}
