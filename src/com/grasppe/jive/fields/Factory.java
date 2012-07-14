/*
 * @(#)Factory.java   12/07/14
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.fields;

/**
 * Class description
 * 	@version        $Revision: 1.0, 12/07/14
 * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
 */
public class Factory {

  /**
   * 	@param name
   * 	@param label
   * 	@param value
   * 	@param options
   * 	@param suffix
   * 	@return
   */
  public static ParameterField createListField(String name, String label, Object value, Object[] options, String suffix) {
    return new ParameterField(name, ComponentFactory.createListField(name, value, options), label, suffix);
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

  /**
   *      @param name
   *      @param label
   *      @param value
   * 	@param minimum
   * 	@param maximum
   *      @param suffix
   *      @return
   */
  public static ParameterField createNumericField(String name, String label, double value, double minimum, double maximum, String suffix) {

    return new ParameterField(name, ComponentFactory.createNumericFieldComponent(name, value, minimum, maximum), label, suffix);

  }

  /**
   *      @param name
   *      @param label
   *      @param value
   *      @param suffix
   *      @return
   */
  public static ParameterField createTextField(String name, String label, String value, String suffix) {

    return new ParameterField(name, ComponentFactory.createTextFieldComponent(name, value), label, suffix);

  }
}
