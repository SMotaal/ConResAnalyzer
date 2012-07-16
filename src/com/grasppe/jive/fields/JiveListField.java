/*
 * @(#)JiveListField.java   12/07/14
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

import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * @author daflair
 *
 */
public class JiveListField extends JComboBox implements ValueField, NamedField, NameValueField {

  protected Object oldValue = null;

  /**
   *
   */
  public JiveListField() {
    initializeField();
  }

  /**
   * @param aModel
   */
  public JiveListField(ComboBoxModel aModel) {
    super(aModel);
    initializeField();
  }

  /**
   * @param items
   */
  public JiveListField(Object[] items) {
    super(items);
    initializeField();
  }

  /**
   * @param items
   */
  public JiveListField(Vector<?> items) {
    super(items);
    initializeField();
  }

  /**
   */
  protected void initializeField() {
    super.setEditable(true);
    super.putClientProperty("JComboBox.isSquare", true);
    super.putClientProperty("JComponent.sizeVariant", "regular");
    
    super.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        try {
        	
        	 ((ValueField) e.getSource()).upateValue();
//      	theField.updateValue();

//          Object newValue = getValue();
//          String name     = getName();
//
//          if ((name == null) || name.isEmpty()) name = "value";
//          if ((oldValue != null) && oldValue.equals(newValue)) return;
//
//          firePropertyChange(name, oldValue, newValue);
//
//          oldValue = newValue;
        } catch (Exception exception) {
          GrasppeKit.debugError("JiveListField>ItemStateChanged", exception, 1);
        }
      }
    });
  }
  
  public void upateValue() { //Object newValue) {
      Object newValue = getValue();
      String name     = getName();

      if ((name == null) || name.isEmpty()) name = "value";
      if ((oldValue != null) && oldValue.equals(newValue)) return;

      firePropertyChange(name, oldValue, newValue);

      oldValue = newValue;	  
  }

  /**
   *    @return
   */
  @Override
  public Object getValue() {
    return super.getSelectedIndex(); // Item();
  }

  /**
   *    @param value
   */
  @Override
  public void setValue(Object value) {
	  
	  try {
		  super.setSelectedIndex((Integer)value);
	  } catch (Exception exception) {
		  super.setSelectedItem(value);
	  }
//	  boolean valueSet = false;
//	    try {
//	    	if (!valueSet) {
//      super.setSelectedItem(value);
//      valueSet = true;
//      return; 
//	    	}
//    } finally {}
//    
//    try {
//    	if (!valueSet) {
//        	// Get number of items
//        	int num = super.getItemCount();
//        	
//        	String trimValue = ((String)value).replaceAll("\\W", "").toLowerCase();
//
//        	// Get items
//        	for (int i=0; i<num; i++) {
//        	    Object item = super.getItemAt(i);
//        	    String trimItem = ((String)item).replaceAll("\\W", "").toLowerCase();
//        	    if (trimItem.matches(trimValue)) {
//        	    	super.setSelectedIndex(i);
//        	    	valueSet = true;
//        	    	return;
//        	    }
//        	}
//        	
//        	valueSet = true;
//    	}
//    } finally {}
//
//    try {
//    	if (!valueSet) {
//    super.setSelectedIndex((Integer)value);
//    valueSet = true;
//    return;
//    	}
//    } finally {}    
  }
}
