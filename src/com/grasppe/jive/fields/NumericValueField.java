/*
 * @(#)NumericValueField.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.fields;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class NumericValueField extends JFormattedTextField implements PropertyChangeListener {

  protected double[] numericRange = {};			// 
  protected Double minimumValue = null;
  protected Double maximumValue = null;

  /**
   */
  public NumericValueField() {

    // Initialize
    this(new DefaultFormatterFactory(
        new NumberFormatter(NumberFormat.getNumberInstance()),
        new NumberFormatter(NumberFormat.getNumberInstance()),		// new NumberFormatter(NumberFormat.getPercentInstance()),
                                     new NumberFormatter(NumberFormat.getNumberInstance())));

  }

  /**
   *   @param factory
   */
  public NumericValueField(AbstractFormatterFactory factory) {
    super(factory);

    setColumns(4);

    addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusGained(java.awt.event.FocusEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            selectAll();
          }

        });
      }

    });

    addPropertyChangeListener("value", this);

  }

  /**
   *    @param value
   */
  public NumericValueField(Double value) {
    this();
    if (value != null) setValue(value);
  }

  /**
   *    @param minimum
   *    @param maximum
   */
  public NumericValueField(double minimum, double maximum) {
    this();
    minimumValue = minimum;
    maximumValue = maximum;
  }

  /**
   *    @param value
   *    @param minimum
   *    @param maximum
   *    @param range
   */
  public NumericValueField(Double value, double minimum, double maximum, double[] range) {
    this();
    minimumValue = minimum;
    maximumValue = maximum;
    numericRange = range;
    if (value != null) setValue(value);
  }

  /**
   *      @param evt
   */
  public void propertyChange(PropertyChangeEvent evt) {

    JFormattedTextField source = (JFormattedTextField)evt.getSource();

    try {

      Double value    = ((Number)source.getValue()).doubleValue();
      Double newValue = ((Number)evt.getNewValue()).doubleValue();
      Double oldValue = ((Number)evt.getOldValue()).doubleValue();

      // System.out.println("Value: " + value + " [" + oldValue + " => " + newValue + "]");

      if (newValue != null) {
        double closestValue = newValue;

        closestValue = getClosestValue(newValue);

        if (newValue != closestValue) {
          source.setValue(closestValue);

          return;
        }
      }

      if ((newValue == null) || (newValue >= minimumValue)
          || (newValue <= maximumValue))		// if ((newValue >= minimumValue) || (newValue <= maximumValue))
        if ((oldValue != null) && ((newValue < minimumValue) || (newValue > maximumValue))) {
        source.setValue(oldValue);
      }

    } catch (NullPointerException exception) {

      // TODO: handle exception
    }
  }

  /**
   *    @param actualValue
   *    @return
   */
  private double getClosestValue(Double actualValue) {
    double closestValue = actualValue;

    if ((actualValue != null) && (numericRange.length > 0)) {
      double difference = Math.abs(numericRange[0] - actualValue);

      for (double thisValue : numericRange) {
        double newDifference = Math.abs(thisValue - actualValue);

        if (newDifference <= difference) {
          closestValue = thisValue;
          difference   = newDifference;
        }
      }
    }

    return closestValue;
  }

  /**
   *   @return the maximumValue
   */
  public Double getMaximumValue() {
    return maximumValue;
  }

  /**
   *     @return the minimumValue
   */
  public Double getMinimumValue() {
    return minimumValue;
  }

  /**
   *   @param maximumValue the maximumValue to set
   */
  public void setMaximumValue(Double maximumValue) {
    this.maximumValue = maximumValue;
  }

  /**
   *   @param minimumValue the minimumValue to set
   */
  public void setMinimumValue(Double minimumValue) {
    this.minimumValue = minimumValue;
  }

  /**
   *      @param value
   */
  @Override
  public void setValue(Object value) {
    Double newValue = (Double)value;

    if (newValue != null) newValue = getClosestValue(newValue);
    
    super.setValue(newValue);

    this.selectAll();    
  }
}
