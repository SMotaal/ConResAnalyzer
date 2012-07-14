/*
 * @(#)Factory.java   12/07/14
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.fields;

import javax.swing.JComponent;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/14
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class Factory {

  protected static String  groupID  = "default";
  protected static Factory instance = null;

  /**
   */
  private Factory() {			// String groupID) {

    // this.groupID = groupID;
  }

  /**
   *  @return
   */
  public static Factory Default() {
    return Group("default");
  }

  /**
   *  @param id
   *  @return
   */
  public static Factory Group(String id) {

    // if (instance == null) instance = new Factory();
    Factory factory = new Factory();

    factory.groupID = id;

    return factory;			// instance;
  }

  /**
   *  @param name
   *  @param label
   *  @param value
   *  @param options
   *  @param suffix
   *  @return
   */
  public ParameterField createListField(String name, String label, Object value, Object[] options, String suffix) {
    ParameterField newField = new ParameterField(name, ComponentFactory.createListField(name, value, options), label, suffix);

    newField.setGroupID(groupID);

    return newField;
  }

  /**
   *    @param name
   *    @param label
   *    @param value
   *    @param suffix
   *    @return
   */
  public ParameterField createMarkupField(String name, String label, String value, String suffix) {

    ParameterField newField = new ParameterField(name, ComponentFactory.createMarkupFieldComponent(name, value), label, suffix);

    newField.setGroupID(groupID);

    return newField;

  }

  /**
   *      @param name
   *      @param label
   *      @param value
   *  @param minimum
   *  @param maximum
   *      @param suffix
   *      @return
   */
  public ParameterField createNumericField(String name, String label, double value, double minimum, double maximum, String suffix) {

    ParameterField newField = new ParameterField(name,
                                                 ComponentFactory.createNumericFieldComponent(name, value, minimum, maximum),
                                                 label, suffix);

    newField.setGroupID(groupID);

    return newField;

  }
  
  /**
   *      @param name
   *      @param label
   *      @param value
   *  @param minimum
   *  @param maximum
   *      @param suffix
   *      @return
   */
  public ParameterField createCustomField(String name, String label, JComponent fieldComponent, String suffix) {

    ParameterField newField = new ParameterField(name, fieldComponent, label, suffix);

    newField.setGroupID(groupID);

    return newField;

  }  

  /**
   *      @param name
   *      @param label
   *      @param value
   *      @param suffix
   *      @return
   */
  public ParameterField createTextField(String name, String label, String value, String suffix) {

    ParameterField newField = new ParameterField(name, ComponentFactory.createTextFieldComponent(name, value), label, suffix);

    newField.setGroupID(groupID);

    return newField;

  }
}
