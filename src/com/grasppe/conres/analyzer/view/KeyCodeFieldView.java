/*
 * @(#)StringFieldView.java   11/12/19
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.analyzer.view;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.grasppe.conres.analyzer.PreferencesManager;
import com.grasppe.conres.preferences.Preferences.Tags;
import com.grasppe.conres.preferences.PreferencesAdapter;
import com.grasppe.lure.components.AbstractPreferencesFactory;
import com.grasppe.lure.framework.GrasppeKit.KeyCode;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JTextField;

/**
 *   Class description
 *      @version        $Revision: 1.0, 11/12/19
 *      @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class KeyCodeFieldView extends PreferencesFieldView {

  JTextField	textField = null;

  /**
   *      @param preference
   *      @param manager
   */
  protected KeyCodeFieldView(Tags preference, PreferencesManager manager) {
    super(preference, manager);
  }

  /*
   *  (non-Javadoc)
   *   @see com.grasppe.conres.analyzer.view.PreferencesFieldView#createView()
   */

  /**
   */
  @Override
  protected void createView() {
    super.createView();
    createTextField();
  }

  
  protected void createTextField() {
	    if (textField != null) return;

//	    update();

	    textField = new JTextField(getValueString()); //(String)getCurrentValue());
	    
	    textField.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				updateView();
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

	    add(textField);	  
  }
  
  /*
   *  (non-Javadoc)
   * @see com.grasppe.conres.analyzer.view.PreferencesFieldView#updateView()
   */

  /**
   */
  @Override
  protected void updateView() {
    if (textField != null) {
      if (getCurrentValue() != null) textField.setText("" + getValueString());
      else textField.setText("");
    }

    super.updateView();
  }

  public String getValueString() {
	  Integer currentInteger = new Integer((String)getCurrentValue());
	  return "" + (char)(currentInteger).intValue();
  }
  
  public String getFieldString() {
	  return "" + (int)textField.getText().charAt(0);
  }
  /*
   *  (non-Javadoc)
   *   @see com.grasppe.conres.analyzer.view.PreferencesFieldView#getNewValue()
   */

  /**
   * 	@return
   */
  @Override
  Object getNewValue() {
	  if (textField!=null) {
		  boolean updated = false;
		  String fieldValue = getFieldString();//textField.getText();
		  String currentValue = (String)getCurrentValue(); 
		  if (currentValue!=null) 
			  updated =  !fieldValue.equals(currentValue);
			  
			  if (updated)
				  setNewValue(fieldValue);
	  }
    return super.getNewValue();
  }

  /*
   *  (non-Javadoc)
   * @see com.grasppe.conres.analyzer.view.PreferencesFieldView#getValue()
   */

  /**
   *    @return
   */
  @Override
  protected String getStoredValue() {
    Object	value = super.getStoredValue();

    if (value == null) value = "null";
    else if (value.getClass().isArray())
           value = AbstractPreferencesFactory.encodeArrayValues(PreferencesAdapter.getInstance().asObjectArray(value));
    else value = value.toString();

    return (String)value;
  }
}
