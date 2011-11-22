/*
 * @(#)GridAxis.java   11/10/27
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.targets.model;

import com.grasppe.conres.framework.units.ValueFactory;
import com.grasppe.morie.units.AbstractValue;
import com.grasppe.morie.units.ConcreteValue;
import com.grasppe.morie.units.UnitDefinition;

/**
 * @author daflair
 *
 */
public class GridAxis {

    /** Field description */
    protected double[]	steps;

    /** Field description */
    protected AbstractValue[]	values;

    /** Field description */
    protected String	label;

    /** Field description */
    protected String	symbol;

    /** Field description */
    protected UnitDefinition	unitDefinition;

    /**
     * Constructs ...
     *
     *
     * @param steps
     */
    protected GridAxis(double[] steps) {
        super();
        setSteps(steps);
        generateValues();
    }

    /**
     * Method description
     *
     *
     *
     */
    protected void generateValues() {
        values = ValueFactory.CreateValues(steps, this.values[0]);
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the steps
     */
    public double[] getSteps() {
        return steps;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return the values
     */
    public ConcreteValue[] getValues() {
        return (ConcreteValue[])values;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @param steps the steps to set
     */
    public void setSteps(double[] steps) {
        this.steps = steps;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
