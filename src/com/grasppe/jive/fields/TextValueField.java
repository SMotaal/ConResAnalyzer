/*
 * @(#)NumericValueField.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.fields;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class TextValueField extends JTextField implements ValueField, NamedField { //PropertyChangeListener,

  /**
   */
  public TextValueField() {
    this("");

    // TODO Auto-generated constructor stub
  }

  /**
   *    @param text
   */
  public TextValueField(String text) {
    super(text);

    final TextValueField textField = this;

    getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        updateLabel(e);
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        updateLabel(e);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        updateLabel(e);
      }

      private void updateLabel(DocumentEvent e) {
        java.awt.EventQueue.invokeLater(new Runnable() {

          @Override
          public void run() {
            // textField.setValue(textField.getText());
        	  //textField.firePropertyChange("text", textField.getValue(), textField.getText());
        	  textField.updateText();
          }
        });
      }
    });

//    addPropertyChangeListener("text", this);

    addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusGained(java.awt.event.FocusEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            selectAll();
          }

        });
      }

    });

  }
  
  private void updateText() {
	  //setValue(super.getText());
	  firePropertyChange("value", "", super.getText());
  }

//  /**
//   *      @param evt
//   */
//  public void propertyChange(PropertyChangeEvent evt) {
//
//    System.out.println(evt);
//
//    if (!(evt.getSource() instanceof JTextField)) return;
//
//    JTextField source = (JTextField)evt.getSource();
//
//    try {
//
//      String value    = source.getText();
//      String newValue = (String)evt.getNewValue();
//      String oldValue = (String)evt.getOldValue();
//
//      if (newValue != oldValue) this.setValue(value);
//
//    } catch (NullPointerException exception) {
//      System.out.println(exception);
//    }
//
//  }

  /**
   *    @return
   */
  public String getValue() {
    return super.getText();
  }

  /**
   *    @param value
   */
  @Override
  public void setValue(Object value) {
    setValue((String)value);
  }

  /**
   *      @param value
   */
  public void setValue(String value) {
    String newValue = value;
    
//    if (super.getText().equals(newValue))
//    	return;
//    
//    try {
//
//    	//firePropertyChange("value", super.getText(), newValue);
//    
//    } catch (Exception exception) {
//    	System.out.println(newValue);
//    	System.out.println(exception);
//    }    

    super.setText(newValue);

    this.selectAll();
  }
}
