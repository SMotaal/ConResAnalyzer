package com.grasppe.forms.fields;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.text.NumberFormatter;

public class PercentFormatter extends NumberFormatter {
	
	private boolean convertNumber = true;

	/**
	 * 
	 */
	protected PercentFormatter() {
		super(NumberFormat.getNumberInstance()); // getPercentInstance());
	}

	/**
	 * @param format
	 */
	protected PercentFormatter(NumberFormat format) {
		super(format);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.InternationalFormatter#stringToValue(java.lang.String)
	 */
	@Override
	public Object stringToValue(String text) throws ParseException {
		
        Number number = (Number)super.stringToValue(text);
        if (number != null) {
            double d = number.doubleValue() / getMultipler();
            number = new Double(d);
        }
        return number;
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.InternationalFormatter#valueToString(java.lang.Object)
	 */
	@Override
	public String valueToString(Object value) throws ParseException {
        Number number = (Number)value;
        if (number != null) {
            double d = number.doubleValue() * getMultipler();
            number = new Double(d);
        }
        return super.valueToString(number);
		
		// return super.valueToString(value);
	}
	
	private double getMultipler() {
		double multipler;
		
		if (convertNumber == true) multipler = 100.0;
		else multipler = 1.0;
		
		return multipler;		
	}
	

}
