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

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JTextField;

/**
 *   Class description
 *      @version        $Revision: 1.0, 11/12/19
 *      @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class StringFieldView extends PreferencesFieldView {

  JTextField	textField = null;

  /**
   *      @param preference
   *      @param manager
   */
  protected StringFieldView(Tags preference, PreferencesManager manager) {
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
    if (textField != null) return;

//    update();

    textField = new JTextField((String)getCurrentValue());
    
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

//  updateView();
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
      if (getCurrentValue() != null) textField.setText((String)getCurrentValue());
      else textField.setText("");
    }

    super.updateView();
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
		  String fieldText = textField.getText();
		  String currentText = (String)getCurrentValue(); 
		  if (currentText!=null) 
			  updated =  !fieldText.equals(currentText);
			  
			  if (updated)
				  setNewValue(fieldText);
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
