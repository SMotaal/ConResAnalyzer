/*
 * @(#)ConcreteValue.java   11/10/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.morie.units;

/**
 * @author daflair
 *
 */
public class ConcreteValue extends AbstractValue {

    /** Field description */
    public static double	defaultValue = 0;

    /** Field description */
    public static ValueTypes	valueType;

    /**
     *
     */
    public ConcreteValue() {
        super();
    }

    /**
     * @param inputValue
     */
    protected ConcreteValue(AbstractValue inputValue) {
        super(inputValue);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param value
     */
    protected ConcreteValue(double value) {
        super(value);

        // TODO Auto-generated constructor stub
    }
}
