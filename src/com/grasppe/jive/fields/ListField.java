/*
 * @(#)ListField.java   12/07/14
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
public class ListField extends JComboBox implements ValueField, NamedField, NameValueField {

  protected Object oldValue = null;

  /**
   *
   */
  public ListField() {
    initializeField();
  }

  /**
   * @param aModel
   */
  public ListField(ComboBoxModel aModel) {
    super(aModel);
    initializeField();
  }

  /**
   * @param items
   */
  public ListField(Object[] items) {
    super(items);
    initializeField();
  }

  /**
   * @param items
   */
  public ListField(Vector<?> items) {
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

          Object newValue = getValue();
          String name     = getName();

          if ((name == null) || name.isEmpty()) name = "value";
          if ((oldValue != null) && oldValue.equals(newValue)) return;

          firePropertyChange(name, oldValue, newValue);

          oldValue = newValue;
        } catch (Exception exception) {
          GrasppeKit.debugError("ListField>ItemStateChanged", exception, 1);
        }
      }
    });
  }

  /**
   *    @return
   */
  @Override
  public Object getValue() {
    return super.getSelectedItem();
  }

  /**
   *    @param value
   */
  @Override
  public void setValue(Object value) {
    try {
      super.setSelectedItem(value);
    } finally {
      super.setSelectedIndex((Integer)value);
    }

  }
}
