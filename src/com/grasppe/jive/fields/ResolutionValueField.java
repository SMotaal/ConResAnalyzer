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

import java.beans.PropertyChangeListener;

import java.text.NumberFormat;

import javax.swing.SwingUtilities;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * @author daflair
 *
 */
public class ResolutionValueField extends NumericValueField {
	
	private static double[] numericRange = { 0.625, 0.746, 0.891, 1.064, 1.269, 1.515, 1.812, 2.155, 2.577, 3.086, 3.676, 4.386, 5.208, 6.25 };

	/**
	 * 
	 */
	public ResolutionValueField() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param value
	 * @param minimum
	 * @param maximum
	 * @param range
	 */
	public ResolutionValueField(Double value, double minimum, double maximum,
			double[] range) {
		super(value, minimum, maximum, range);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param minimum
	 * @param maximum
	 */
	public ResolutionValueField(double minimum, double maximum) {
		super(null, minimum, maximum, numericRange);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param value
	 */
	public ResolutionValueField(Double value) {
		super(value);
		// TODO Auto-generated constructor stub
	}			// implements FocusListener { //
  /*
   *  (non-Javadoc)
   *   @see javax.swing.JFormattedTextField#setValue(java.lang.Object)
   */
}
