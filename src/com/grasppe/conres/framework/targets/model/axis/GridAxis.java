/*
 * @(#)GridAxis.java   11/10/27
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.targets.model.axis;

import com.grasppe.morie.units.AbstractValue;
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
    protected AbstractValue		minimumValue;
    protected AbstractValue		maximumValue;

    /** Field description */
    protected String	label;

    /** Field description */
    protected String	symbol;

    /** Field description */
    protected UnitDefinition	unitDefinition;

    /**
     * @param steps
     */
    protected GridAxis(double[] steps) {
        super();
        setSteps(steps);
        generateValues();
    }

    /**
     * @param value
     * @return
     */
    public AbstractValue createValue(double value) {
        return null;
    }

    /**
     * @param length
     * @return
     */
    public AbstractValue[] createValueArray(int length) {
        return new AbstractValue[length];
    }

    /**
     */
    public void generateValues() {
        try {
            int	nSteps = steps.length;

            setMinimumValue(steps[0]);
            setMaximumValue(steps[nSteps - 1]);

            values = createValueArray(nSteps);

            for (int i = 0; i < nSteps; i++)
                values[i] = createValue(steps[i]);

        } catch (Exception exception) {		// AbstractValues cannot be instantiated
            exception.printStackTrace();
        }
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the maximumValue
     */
    public AbstractValue getMaximumValue() {
        return maximumValue;
    }

    /**
     * @return the minimumValue
     */
    public AbstractValue getMinimumValue() {
        return minimumValue;
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
     * @return the unitDefinition
     */
    public UnitDefinition getUnitDefinition() {
        return unitDefinition;
    }

    /**
     * @return the values
     */
    public AbstractValue[] getValues() {
        return values;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @param maximumValue the maximumValue to set
     */
    public void setMaximumValue(AbstractValue maximumValue) {
        this.maximumValue = maximumValue;
    }

    /**
     * @param maximumValue the maximumValue to set
     */
    public void setMaximumValue(double maximumValue) {
        this.maximumValue = createValue(maximumValue);
    }

    /**
     * @param minimumValue the minimumValue to set
     */
    public void setMinimumValue(AbstractValue minimumValue) {
        this.minimumValue = minimumValue;
    }

    /**
     * @param minimumValue
     */
    public void setMinimumValue(double minimumValue) {
        this.minimumValue = createValue(minimumValue);
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
