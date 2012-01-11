/*
 * @(#)PreferencesFieldView.java   11/12/19
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.analyzer.view;

import com.grasppe.conres.analyzer.PreferencesManager;
import com.grasppe.conres.preferences.Preferences.Tags;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *   Class description
 *      @version        $Revision: 1.0, 11/12/19
 *      @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PreferencesFieldView extends JPanel {

///**
//   * @param maximumLabelWidth the maximumLabelWidth to set
//   */
//  protected static void setMaximumLabelWidth(int maximumLabelWidth) {
//  this.maximumLabelWidth = maximumLabelWidth;
//  }

  Tags								preference   = null;
  PreferencesManager	manager      = null;
  JLabel							label        = null;
  private Object							currentValue = null;
private Object newValue     = null;
private Object storedValue = null;

//static int maximumLabelWidth = 0;

  /**
   * @param preference
   *  @param manager
   */
  protected PreferencesFieldView(Tags preference, PreferencesManager manager) {
    super();
    this.preference = preference;
    this.manager    = manager;
    SwingUtilities.invokeLater(new Runnable() {
		
		@Override
		public void run() {
			createView();			
		}
	});
    update();
  }

  /**
   *    @param preference
   *    @param manager
   *    @return
   */
  public static PreferencesFieldView buildFieldView(Tags preference, PreferencesManager manager) {
    return new StringFieldView(preference, manager);
  }

  /**
   */
  protected void createView() {

//  if label
    if (label != null) return;
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    setMinimumSize(new Dimension(200, 50));
    setAlignmentY(CENTER_ALIGNMENT);

    label = new JLabel(getPreference().key() + ":");

    add(label);

//  if (label.getWidth()+15 > maximumLabelWidth)
//    maximumLabelWidth =
////  label.setPreferredSize(label.getWidth()+)
  }

  /**
   */
  protected void update() {
	  if (getCurrentValue()==null)
		  setCurrentValue(getStoredValue());
	  updateView();
  }
  
  protected void updateView() {
	  repaint();
  }
  
  
  public void storeValue() {
	  Object newValue = getNewValue();
	  if (newValue!=null)
		  getManager().putValue(getPreference(), getNewValue());
	  setCurrentValue(getStoredValue());
	  if (getCurrentValue()==null) setCurrentValue(preference.defaultValue());
	  setNewValue(null);
//	  if (getCurrentValue()==getNewValue())
//		  return;
  }

  /**
   * @return the label
   */
  protected JLabel getLabel() {
    return label;
  }

  /**
   * @return the manager
   */
  protected PreferencesManager getManager() {
    return manager;
  }

  /**
   * @return the preference
   */
  protected Tags getPreference() {
    return preference;
  }

  /**
   * @return the value
   */
  protected Object getStoredValue() {
    return getManager().getValue(getPreference()); //getCurrentValue();
  }

Object getNewValue() {
	return newValue;
}

void setNewValue(Object newValue) {
	this.newValue = newValue;
}

Object getCurrentValue() {
	return currentValue;
}

void setCurrentValue(Object currentValue) {
	this.currentValue = currentValue;
}

//protected Object
}
