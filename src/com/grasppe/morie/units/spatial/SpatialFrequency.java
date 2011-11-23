/*
 * @(#)SpatialFrequency.java   11/10/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.morie.units.spatial;



//~--- JDK imports ------------------------------------------------------------

import java.io.InvalidClassException;

import com.grasppe.morie.units.AbstractRate;
import com.grasppe.morie.units.FrequencyTypes;

/**
 * @author daflair
 *
 */
public abstract class SpatialFrequency extends AbstractRate {

    /** Field description */
    public static final FrequencyTypes	frequencyType = FrequencyTypes.SPATIAL;

    /**
     *
     */
    protected SpatialFrequency() {
        super();
    }

    /**
     * @param value
     */
    protected SpatialFrequency(double value) {
        super(value);
    }

    /**
     * @param inputValue
     * @throws InvalidClassException
     */
    protected SpatialFrequency(SpatialFrequency inputValue) throws InvalidClassException {
        super(inputValue);
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.morie.units.AbstractRate#clone()
     */

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public SpatialFrequency clone() {
        return (SpatialFrequency)super.clone();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.morie.units.AbstractRate#convertFromRate(com.grasppe.morie.units.AbstractRate)
     */

//  /**
//   * Method description
//   *
//   *
//   * @param inputRate
//   *
//   * @throws InvalidClassException
//   */
//  public void convertFromRate(SpatialFrequency inputRate) throws InvalidClassException {
//      super.convertFromRate(inputRate);
//  }

    /**
     * Method description
     *
     *
     * @return
     * @throws InvalidClassException
     */
    @Override
	public final CyclesPerMetre standardValue() throws InvalidClassException {
        return new CyclesPerMetre(this);
    }
}
