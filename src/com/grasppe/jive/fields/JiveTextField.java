/*
 * @(#)JiveNumericField.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.fields;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.grasppe.lure.framework.GrasppeKit;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class JiveTextField extends JTextField implements ValueField, NamedField, NameValueField {		// PropertyChangeListener,

  protected Object oldValue = null;

  /**
   */
  public JiveTextField() {
    this("");

    // TODO Auto-generated constructor stub
  }

  /**
   *    @param text
   */
  public JiveTextField(String text) {
    super(text);

    final JiveTextField textField = this;

//    getDocument().addDocumentListener(new DocumentListener() {
//
//      @Override
//      public void removeUpdate(DocumentEvent e) {
//        updateLabel(e);
//      }
//
//      @Override
//      public void insertUpdate(DocumentEvent e) {
//        updateLabel(e);
//      }
//
//      @Override
//      public void changedUpdate(DocumentEvent e) {
//        updateLabel(e);
//      }
//
//      private void updateLabel(DocumentEvent e) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//          @Override
//          public void run() {
////            textField.upateValue();
//          }
//        });
//      }
//    });

    // addPropertyChangeListener("text", this);

    addFocusListener(new java.awt.event.FocusAdapter() {

      /* (non-Javadoc)
		 * @see java.awt.event.FocusAdapter#focusLost(java.awt.event.FocusEvent)
		 */
		@Override
		public void focusLost(FocusEvent arg0) {
	        SwingUtilities.invokeLater(new Runnable() {

	            @Override
	            public void run() {

			upateValue();			
	            }

	        });
	      }

	public void focusGained(java.awt.event.FocusEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            selectAll();
          }

        });
      }
      
      

    });
    
    addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			upateValue();			
//			GrasppeKit.debugText(e.toString(),2);
		}
	});

  }

  /**
   */
  public void upateValue() {		// Object newValue) {
    Object newValue = getValue();
    String name     = getName();

    if ((name == null) || name.isEmpty()) name = "value";
    if ((oldValue != null) && oldValue.equals(newValue)) return;

    firePropertyChange(name, oldValue, newValue);

    oldValue = newValue;
  }

  /**
   */
  private void updateText() {

    // setValue(super.getText());
    firePropertyChange("value", "", super.getText());
  }

  // /**
  // *      @param evt
  // */
  // public void propertyChange(PropertyChangeEvent evt) {
  //
  // System.out.println(evt);
  //
  // if (!(evt.getSource() instanceof JTextField)) return;
  //
  // JTextField source = (JTextField)evt.getSource();
  //
  // try {
  //
  // String value    = source.getText();
  // String newValue = (String)evt.getNewValue();
  // String oldValue = (String)evt.getOldValue();
  //
  // if (newValue != oldValue) this.setValue(value);
  //
  // } catch (NullPointerException exception) {
  // System.out.println(exception);
  // }
  //
  // }

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

    super.setText(newValue);

    this.selectAll();
  }
}
