/*
 * @(#)CyclesPerMetre.java   11/10/24
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.morie.units.spatial;

import com.grasppe.morie.units.AbstractValue;
import com.grasppe.morie.units.UnitDefinition;
import com.grasppe.morie.units.ValueTypes;
import com.grasppe.morie.units.spatial.length.Metres;

//~--- JDK imports ------------------------------------------------------------

import java.io.InvalidClassException;

/**
 * @author daflair
 */
public class CyclesPerMetre extends SpatialFrequency {

    /** Field description */
    public static final Cycles	numeratorValue = new Cycles(1);

    /** Field description */
    public static final Metres	denominatorValue = new Metres(1);

    /** Field description */
    public static UnitDefinition	unitDefinition = new UnitDefinition(ValueTypes.FREQUENCY,
                                                      "spatial frequency", "cycles/m");

    /**
     */
    public CyclesPerMetre() {
        super();
    }

    /**
     * @param value
     */
    public CyclesPerMetre(double value) {
        super(value);
    }

    /**
     * @param inputRate
     * @throws InvalidClassException
     */
    public CyclesPerMetre(SpatialFrequency inputRate) throws InvalidClassException {
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

//  //  /**
//  //   * @param numeratorValue the numeratorValue to set
//  //   */
//  //  protected void setNumeratorValue(AbstractValue numeratorValue) {
//  //      this.numeratorValue = numeratorValue;
//  //  }
//  
//      /**
//       * @return the factor
//       */
//      public double getFactor() {
//          return factor;
//      }
//    /**
//     * @param denominatorValue the denominatorValue to set
//     */
//    protected void setDenominatorValue(AbstractValue denominatorValue) {
//        this.denominatorValue = denominatorValue;
//    }    
}
