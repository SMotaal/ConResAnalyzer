/*
 * @(#)UnitDefinition.java   11/10/27
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
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
public class UnitDefinition {

    /** Field description */
    protected static double	defaultFactor = 1;

    /** Field description */
    protected static double	defaultValue = 0;

    /** Field description */
    public String	prefix;

    /** Field description */
    public String	singular;

    /** Field description */
    public String	suffix;

    /** Field description */
    public String	symbol;

    /** Field description */
    public String	abstractType;

    /** Field description */
    public ValueTypes	valueType;

    /** Field description */
    public double	factor;

    /** Field description */
    public double	maximum, minimum;

    /** Field description */
    protected boolean	hasMaximum = !Double.isNaN(maximum),
						hasMinimum = !Double.isNaN(minimum);

    /**
     * Constructs ...
     *
     *
     *
     * @param abstractType
     */
    public UnitDefinition(String abstractType) {
        this("", "", "", "", ValueTypes.ABSTRACT, abstractType, defaultFactor);
    }

    /**
     * Constructs ...
     *
     *
     * @param valueType
     */
    public UnitDefinition(ValueTypes valueType) {
        this("", "", "", "", valueType, "", defaultFactor);
    }

    /**
     * Constructs ...
     *
     *
     * @param singular
     * @param symbol
     * @param valueType
     */
    public UnitDefinition(String singular, String symbol, ValueTypes valueType) {
        this("", singular, singular + "s", symbol, valueType, "", defaultFactor);
    }

    /**
     * Constructs ...
     *
     *
     * @param abstractType
     * @param symbol
     * @param valueType
     */
    public UnitDefinition(ValueTypes valueType, String abstractType, String symbol) {
        this("", "", "", symbol, ValueTypes.ABSTRACT, abstractType, defaultFactor);
    }

    /**
     * Constructs ...
     *
     *
     * @param singular
     * @param symbol
     * @param abstractType
     * @param factor
     */
    public UnitDefinition(String singular, String symbol, String abstractType, double factor) {
        this("", singular, singular + "s", symbol, ValueTypes.ABSTRACT, abstractType, factor);
    }

    /**
     * Constructs ...
     *
     *
     * @param singular
     * @param suffix
     * @param symbol
     * @param abstractType
     */
    public UnitDefinition(String singular, String suffix, String symbol, String abstractType) {
        this("", singular, suffix, symbol, ValueTypes.ABSTRACT, abstractType, defaultFactor);
    }

    /**
     * Constructs ...
     *
     *
     * @param singular
     * @param suffix
     * @param symbol
     * @param valueType
     */
    public UnitDefinition(String singular, String suffix, String symbol, ValueTypes valueType) {
        this("", singular, suffix, symbol, valueType, "", defaultFactor);
    }

    /**
     * Constructs ...
     *
     *
     * @param singular
     * @param symbol
     * @param valueType
     * @param factor
     */
    public UnitDefinition(String singular, String symbol, ValueTypes valueType, double factor) {
        this("", singular, singular + "s", symbol, valueType, "", factor);
    }

    /**
     * Constructs ...
     *
     *
     * @param singular
     * @param suffix
     * @param symbol
     * @param abstractType
     * @param factor
     */
    public UnitDefinition(String singular, String suffix, String symbol, String abstractType,
                          double factor) {
        this("", singular, suffix, symbol, ValueTypes.ABSTRACT, abstractType, factor);
    }

    /**
     * Constructs ...
     *
     *
     * @param prefix
     * @param singular
     * @param suffix
     * @param symbol
     * @param valueType
     */
    public UnitDefinition(String prefix, String singular, String suffix, String symbol,
                          ValueTypes valueType) {
        this(prefix, singular, suffix, symbol, valueType, "", defaultFactor);
    }

    /**
     * Constructs ...
     *
     *
     * @param singular
     * @param suffix
     * @param symbol
     * @param valueType
     * @param factor
     */
    public UnitDefinition(String singular, String suffix, String symbol, ValueTypes valueType,
                          double factor) {
        this("", singular, suffix, symbol, valueType, "", factor);
    }

    /**
     * Constructs ...
     *
     *
     * @param prefix
     * @param singular
     * @param suffix
     * @param symbol
     * @param abstractType
     * @param factor
     */
    public UnitDefinition(String prefix, String singular, String suffix, String symbol,
                          String abstractType, double factor) {
        this(prefix, singular, suffix, symbol, ValueTypes.ABSTRACT, abstractType, factor);
    }

    /**
     * Constructs ...
     *
     *
     * @param prefix
     * @param singular
     * @param suffix
     * @param symbol
     * @param valueType
     * @param factor
     */
    public UnitDefinition(String prefix, String singular, String suffix, String symbol,
                          ValueTypes valueType, double factor) {
        this(prefix, singular, suffix, symbol, valueType, "", factor);
    }

    /**
     * @param prefix
     * @param singular
     * @param suffix
     * @param symbol
     * @param valueType
     * @param abstractType
     * @param factor
     */
    public UnitDefinition(String prefix, String singular, String suffix, String symbol,
                          ValueTypes valueType, String abstractType, double factor) {
        super();
        this.prefix       = prefix;
        this.singular     = singular;
        this.suffix       = suffix;
        this.symbol       = symbol;
        this.valueType    = valueType;
        this.abstractType = abstractType;
        this.factor       = factor;
    }
}
