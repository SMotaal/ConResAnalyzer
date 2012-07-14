package com.grasppe.jive.fields;

public class Factory {

	/**
	   *    @param name
	   *    @param label
	   *    @param value
	   *    @param suffix
	   *    @return
	   */
	  public static ParameterField createNumericField(String name, String label, double value, double minimum, double maximum, String suffix) {
	
	    return new ParameterField(name, ComponentFactory.createNumericFieldComponent(name, value, minimum, maximum), label, suffix);
	
	  }

	/**
	   *    @param name
	   *    @param label
	   *    @param value
	   *    @param suffix
	   *    @return
	   */
	  public static ParameterField createTextField(String name, String label, String value, String suffix) {
	
	    return new ParameterField(name, ComponentFactory.createTextFieldComponent(name, value), label, suffix);
	
	  }

		/**
	   *    @param name
	   *    @param label
	   *    @param value
	   *    @param suffix
	   *    @return
	   */
	  public static ParameterField createMarkupField(String name, String label, String value, String suffix) {
	
	    return new ParameterField(name, ComponentFactory.createMarkupFieldComponent(name, value), label, suffix);
	
	  }
}
