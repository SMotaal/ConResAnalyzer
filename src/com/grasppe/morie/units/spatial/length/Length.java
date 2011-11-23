/*
 * @(#)lengthValue.java   11/10/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.morie.units.spatial.length;

import com.grasppe.morie.units.AbstractValue;
import com.grasppe.morie.units.ValueTypes;

/**
 * Class description
 *
 *
 * @version        Enter version here..., 11/10/24
 * @author         Enter your name here...
 */
public abstract class Length extends AbstractValue {

    /** Field description */
    public static ValueTypes	valueType = ValueTypes.LENGTH;

    // ** Field description */
    // public static UnitDefinition  unitDefinition = new UnitDefinition("", "", ValueTypes.LENGTH);

    /**
     *
     */
    protected Length() {
        super();
    }

    /**
     * @param value
     */
    protected Length(double value) {
        super(value);
    }

    /**
     * Constructs ...
     *
     *
     * @param inputValue
     */
    protected Length(Length inputValue) {
        super(inputValue);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
	public final Metres standardValue() {
        return new Metres();
    }

    /**
     * Method description
     *
     *
     * @param inputValue
     *
     * @return
     */
    public final Metres standardValue(double inputValue) {
        return new Metres(inputValue);
    }

    /**
     * Method description
     *
     *
     * @param inputValue
     *
     * @return
     */
    public final Metres standardValue(Length inputValue) {
        return new Metres(inputValue.getStandardValue());
    }
}
