/*
 * @(#)ToneValueField.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
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

import com.grasppe.jive.formatters.PercentFormatter;

/**
 * @author daflair
 *
 */
public class ToneValueField extends JFormattedTextField implements PropertyChangeListener {			// implements FocusListener { //

  /**
	 * @return the minimumValue
	 */
	public Double getMinimumValue() {
		return minimumValue;
	}

	/**
	 * @param minimumValue the minimumValue to set
	 */
	public void setMinimumValue(Double minimumValue) {
		this.minimumValue = minimumValue;
	}

	/**
	 * @return the maximumValue
	 */
	public Double getMaximumValue() {
		return maximumValue;
	}

	/**
	 * @param maximumValue the maximumValue to set
	 */
	public void setMaximumValue(Double maximumValue) {
		this.maximumValue = maximumValue;
	}

private Double minimumValue = null;
  private Double maximumValue = null;

  /**
   *
   */
  public ToneValueField() {
    super(new DefaultFormatterFactory(
        new NumberFormatter(NumberFormat.getPercentInstance()),
        new NumberFormatter(NumberFormat.getPercentInstance()),			// new NumberFormatter(NumberFormat.getPercentInstance()),
                                      new PercentFormatter()));

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
  
  public ToneValueField(double minimum, double maximum) {
	  this();
	  minimumValue = minimum;
	  maximumValue = maximum;
  }

//@Override
//public void focusGained(FocusEvent arg0) {
//    selectAll();
//}
//
//@Override
//public void focusLost(FocusEvent arg0) {
//    // TODO Auto-generated method stub
//}

  /**
   *    @param evt
   */
  public void propertyChange(PropertyChangeEvent evt) {

    JFormattedTextField source = (JFormattedTextField)evt.getSource();

    try {

      Double value    = (Double)source.getValue();
      Double newValue = (Double)evt.getNewValue();
      Double oldValue = (Double)evt.getOldValue();

      // System.out.println("Value: " + value + "[" + oldValue + " => " + newValue + "]");

      if ((newValue == null) || (newValue >= minimumValue) || (newValue <= maximumValue))
        if ((oldValue != null) && (newValue < minimumValue || newValue > maximumValue)) {
        source.setValue(oldValue);
      }
    } catch (Exception exception) {
		// TODO: handle exception
	}
  }

//if (source == txtContrast) {
//  double value;
//
//  try {
//    value = ((Number)((JFormattedTextField)source).getValue()).doubleValue();
//  } catch (Exception exception) {
//    value = contrastValue;
//  }
//
//  System.out.println("Contrast (" + contrastValue + "): " + value);
//
//  if ((value >= 0.0) && (value <= 1.0)) {
//    contrastValue = value;
//  } else {
//
//    // System.out.println("Contrast (" + contrastValue + "):" + value);
//    ((JFormattedTextField)source).setValue(new Double(contrastValue));
//  }
//} else if (source == txtReferenceToneValue) {
//
////  double value = ((Number)((JFormattedTextField)source).getValue()).doubleValue();
//  //
////  if ((value >= 0) && (value <= 100)) referenceToneValue = value;
////  else ((JFormattedTextField)source).setValue(new Double(referenceToneValue));
//}

}
