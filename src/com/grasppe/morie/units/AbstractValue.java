/*
 * @(#)abstractValue.java   11/10/24
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



package com.grasppe.morie.units;

import java.io.InvalidClassException;

import java.text.DecimalFormat;

/**
 * Class description
 * @version        Enter version here..., 11/10/24
 * @author         Enter your name here...
 */
public abstract class AbstractValue {

    /** Field description */
    public static double	defaultValue = 0;

    /** Field description */
    public static double	significance = Math.pow(10, 6);

    /** Field description */
    protected UnitDefinition	unitDefinition;

    /** Field description */
    public double	value;

    /**
     */
    protected AbstractValue() {
        this(defaultValue);
    }

    /**
     * @param inputValue
     */
    protected AbstractValue(AbstractValue inputValue) {
        this();
        this.setStandardValue(inputValue.getStandardValue());
    }

    /**
     * @param value
     */
    protected AbstractValue(double value) {
        this.value = value;
    }

    /**
     * @return
     */
    @Override
    public AbstractValue clone() {
        AbstractValue	newValue = null;

        try {
            newValue = this.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return newValue;
    }

    /**
     * @return
     * @throws InvalidClassException
     */
    protected AbstractValue standardValue() throws InvalidClassException {
        return null;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        String	returnString;
        String	standardSymbol;
        String	standardValue;

//      try {
//          standardSymbol = " " + standardValue().getDefinition().symbol;
//      } catch (Exception e) {
//          standardSymbol = "";
//      }
//
//      try {
//          standardValue = " =  SI " + getStandardValue() + standardSymbol;
//      } catch (Exception e) {
//          standardValue = " (no SI defined)";
//      }
        String			thisSuffix   = getSuffix(2);
        String			thisSingular = getSuffix(1);
        DecimalFormat	df           = new DecimalFormat("###.########");

        returnString = thisSuffix + ": " + df.format(getValue()) + " " + getSymbol();

//      returnString += standardValue;
        return returnString;
    }

    /**
     * @return the abstractType
     */
    public String getAbstractType() {
        return getDefinition().abstractType;
    }

    /**
     * @return
     */
    protected UnitDefinition getDefinition() {
        return this.unitDefinition;
    }

    /**
     * @return
     */
    public double getFactor() {
        return getDefinition().factor;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return getDefinition().prefix;
    }

    /**
     * @return
     */
    public double getStandardValue() {
        return getValue() * getDefinition().factor;
    }

    /**
     * @return the suffix
     */
    public String getSuffix() {
        return getSuffix(getValue());
    }

    /**
     * @param value
     * @return the suffix
     */
    public String getSuffix(double value) {
        try {
            return (value == 1) ? getDefinition().singular
                                : getDefinition().suffix;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return
     */
    public String getSymbol() {
        return this.getDefinition().symbol;
    }

    /**
     * @return
     */
    public ValueTypes getType() {
        return this.getDefinition().valueType;
    }

    /**
     * @return
     */
    public double getValue() {
        return Math.round(this.value * significance) / significance;
    }

    /**
     * @param value
     * @return
     */
    protected static boolean isNotRate(AbstractValue value) {
        return !(value instanceof AbstractRate);
    }

    /**
     * @param value1
     * @param value2
     * @return
     */
    protected static boolean isSameType(AbstractValue value1, AbstractValue value2) {
        try {
            if (value1.getDefinition().valueType == value2.getDefinition().valueType) {
                if (value1.getDefinition().valueType == ValueTypes.ABSTRACT)
                    return value1.getDefinition().abstractType
                           == value2.getDefinition().abstractType;
                else return true;
            }
        } catch (Exception e) {}

        return false;
    }

    /**
     * @param inputValue
     */
    public void setStandardValue(double inputValue) {
        this.setValue(inputValue / getDefinition().factor);
    }

    /**
     * @param inputValue
     */
    public void setValue(AbstractValue inputValue) {
        setValue(inputValue.getStandardValue());
    }

    /**
     * @param value
     */
    public void setValue(double value) {

        // if ((!hasMinimum || (value >= minimum)) && (!hasMaximum || (value <= maximum)))
        this.value = value;
    }
}
