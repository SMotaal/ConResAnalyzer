/*
 * @(#)JiveComponentFactory.java   12/07/14
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.grasppe.jive.fields.ListField;
import com.grasppe.jive.fields.MarkupValueField;
import com.grasppe.jive.fields.NumericValueField;
import com.grasppe.jive.fields.TextValueField;

/**
 * Class description
 * 	@version        $Revision: 1.0, 12/07/14
 * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
 */
public class JiveComponentFactory {
	
	protected static float defaultAlignmentX = JComponent.LEFT_ALIGNMENT;

  /**
   * 	@param name
   * 	@param value
   * 	@param options
   * 	@return
   */
  public static JComponent createListField(String name, Object value, Object[] options) {

    ListField newField = new ListField(options);

    newField.setName(name);
    newField.setValue(value);
    newField.setAlignmentX(defaultAlignmentX);

    return newField;
  }

  /**
   *    @param name
   *    @param value
   *    @return
   */
  public static JComponent createMarkupFieldComponent(String name, String value) {

    MarkupValueField newField = new MarkupValueField();

    newField.setName(name);
    newField.setValue(value);
    newField.setAlignmentX(defaultAlignmentX);

    return newField;
  }

  /**
   *      @param name
   *      @param value
   *      @param minimum
   *      @param maximum
   *      @return
   */
  public static JComponent createNumericFieldComponent(String name, double value, double minimum, double maximum) {
    NumericValueField newField = new NumericValueField(minimum, maximum);

    newField.setName(name);
    newField.setValue(value);
    newField.setAlignmentX(defaultAlignmentX);
    newField.setHorizontalAlignment(SwingConstants.TRAILING);

    return newField;
  }
  
  /**
   *      @param name
   *      @param value
   *      @param minimum
   *      @param maximum
   *      @return
   */
  public static JComponent createCustomFieldComponent(String name, NumericValueField newField) {
	  // NumericValueField newField = new NumericValueField(minimum, maximum);

    newField.setName(name);
    // newField.setValue(value);
    newField.setAlignmentX(defaultAlignmentX);
    newField.setHorizontalAlignment(SwingConstants.TRAILING);

    return newField;
  }  

  /**
   *      @param name
   *      @param value
   *      @return
   */
  public static JComponent createTextFieldComponent(String name, String value) {

    TextValueField newField = new TextValueField();

    newField.setName(name);
    newField.setValue(value);
    newField.setAlignmentX(defaultAlignmentX);
    newField.setHorizontalAlignment(SwingConstants.LEADING);

    return newField;
  }
}
