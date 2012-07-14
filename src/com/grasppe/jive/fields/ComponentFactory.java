package com.grasppe.jive.fields;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

public class ComponentFactory {

	/**
	   * 	@param name
	   * 	@param value
	   * 	@param minimum
	   * 	@param maximum
	   * 	@return
	   */
	  public static JComponent createNumericFieldComponent(String name, double value, double minimum, double maximum) {
	    NumericValueField newField = new NumericValueField(minimum, maximum);
	
	    newField.setHorizontalAlignment(SwingConstants.TRAILING);
	    newField.setName(name);
	    newField.setValue(value);
	
	    // newField.addPropertyChangeListener("value", (PropertyChangeListener)this);
	    return newField;
	  }

	/**
	   * 	@param name
	   *    @param value
	   *    @return
	   */
	  public static JComponent createTextFieldComponent(String name, String value) {
	
	    TextValueField newField = new TextValueField();
	
	    newField.setHorizontalAlignment(SwingConstants.LEADING);
	
	    newField.setName(name);
	    newField.setValue(value);
	
	    // newField.addPropertyChangeListener("value", (PropertyChangeListener)this);
	
	    return newField;
	  }
	  
		/**
	   * 	@param name
	   *    @param value
	   *    @return
	   */
	  public static JComponent createMarkupFieldComponent(String name, String value) {
	
	    MarkupValueField newField = new MarkupValueField();
	
	    //newField.setHorizontalAlignment(SwingConstants.LEADING);
	
	    newField.setName(name);
	    newField.setValue(value);
	
	    // newField.addPropertyChangeListener("value", (PropertyChangeListener)this);
	
	    return newField;
	  }
	  

}
