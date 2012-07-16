/*
 * @(#)JiveFieldFactory.java   12/07/14
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive;

import javax.swing.JComponent;

import com.grasppe.jive.components.JiveField;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/14
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class JiveFieldFactory {

  protected static String  groupID  = "default";
  protected static JiveFieldFactory instance = null;

  /**
   */
  private JiveFieldFactory() {			// String groupID) {

    // this.groupID = groupID;
  }

  /**
   *  @return
   */
  public static JiveFieldFactory Default() {
    return Group("default");
  }

  /**
   *  @param id
   *  @return
   */
  public static JiveFieldFactory Group(String id) {

    // if (instance == null) instance = new JiveFieldFactory();
    JiveFieldFactory jiveFieldFactory = new JiveFieldFactory();

    jiveFieldFactory.groupID = id;

    return jiveFieldFactory;			// instance;
  }

  /**
   *  @param name
   *  @param label
   *  @param value
   *  @param options
   *  @param suffix
   *  @return
   */
  public JiveField createListField(String name, String label, Object value, Object[] options, String suffix) {
    JiveField newField = new JiveField(name, JiveComponentFactory.createListField(name, value, options), label, suffix);

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
  public JiveField createMarkupField(String name, String label, String value, String suffix) {

    JiveField newField = new JiveField(name, JiveComponentFactory.createMarkupFieldComponent(name, value), label, suffix);

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
  public JiveField createNumericField(String name, String label, double value, double minimum, double maximum, String suffix) {

    JiveField newField = new JiveField(name,
                                                 JiveComponentFactory.createNumericFieldComponent(name, value, minimum, maximum),
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
  public JiveField createCustomField(String name, String label, JComponent fieldComponent, String suffix) {

    JiveField newField = new JiveField(name, fieldComponent, label, suffix);

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
  public JiveField createTextField(String name, String label, String value, String suffix) {

    JiveField newField = new JiveField(name, JiveComponentFactory.createTextFieldComponent(name, value), label, suffix);

    newField.setGroupID(groupID);

    return newField;

  }
}
