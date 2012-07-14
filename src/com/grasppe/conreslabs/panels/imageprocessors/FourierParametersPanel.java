/*
 * @(#)PatchParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conreslabs.panels.imageprocessors;

import com.grasppe.jive.components.ModuleParametersPanel;
import com.grasppe.jive.fields.Factory;
import com.grasppe.jive.fields.GroupOptions;
import com.grasppe.jive.fields.NameValueField;
import com.grasppe.jive.fields.ParameterField;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class FourierParametersPanel extends ModuleParametersPanel implements PropertyChangeListener {

//  private NameValueField              modeComponent, methodComponent;
  private Collection<ParameterField>  fields     = new LinkedHashSet<ParameterField>();
  private Map<String, NameValueField> components = new HashMap<String, NameValueField>();
  private ParameterField              modeField, methodField;
  private String[]                    modeOptions   = { "Automatic", "Forward", "Reverse" };
  private String[]                    methodOptions = { "Logarithmic Scaling", "Raw Power Spectrum" };
  private GroupOptions                groupOptions  = new GroupOptions();

  /**
   * Create the panel.
   */
  public FourierParametersPanel() {

    this.setName("Fourier-Panel");

    this.setTitle("Fourier");

    createPanelFields();
  }

  /**
   *  @param field
   */
  private void addField(ParameterField field) {
    fields.add(field);
    field.addPropertyChangeListener(field.getName(), this);
    this.addPropertyChangeListener(field.getName(), this);
    components.put(field.getName(), (NameValueField)field.getFieldComponent());
  }

  /**
   */
  private void createPanelFields() {

    ParameterField.setGroupOptions("long-text", GroupOptions.LONG_TEXT_OPTIONS);
    ParameterField.setGroupOptions("short-text", GroupOptions.SHORT_TEXT_OPTIONS);

    methodField = Factory.createListField("Fourier-Method", "Method", 0, methodOptions, "");
    methodField.setGroupID("long-text");
//    methodComponent = (NameValueField)methodField.getFieldComponent();

    addField(methodField);

    modeField = Factory.createListField("Fourier-Mode", "Mode", 0, modeOptions, "");
    modeField.setGroupID("long-text");
//    modeComponent = (NameValueField)modeField.getFieldComponent();

    addField(modeField);

    BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

    this.setLayout(layout);
    this.setAlignmentX(LEFT_ALIGNMENT);

    add(Box.createVerticalStrut(groupOptions.marginHeight));

    int fieldIndex = 0;

    for (ParameterField field : fields) {
      if (fieldIndex++ > 1) add(Box.createVerticalStrut(groupOptions.paddingHeight));
      add(field);
    }

    add(Box.createVerticalStrut(groupOptions.marginHeight));

  }

  /**
   *    @param args
   */
  public static void main(String[] args) {

    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        JFrame frame = new JFrame("FunctionParametersPanel Demo");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add contents to the window.
        frame.getContentPane().add(new FourierParametersPanel());

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }

    });
  }

  /**
   *    @param evt
   */
  public void propertyChange(PropertyChangeEvent evt) {

    try {

      String sourceName   = ((JComponent)evt.getSource()).getName();
      String propertyName = evt.getPropertyName();

      if (sourceName.matches(getName())) {		// Panel Property Changed... Update Field

        if (components.containsKey(propertyName)) {
          components.get(propertyName).setValue(evt.getNewValue());
          this.notifyObservers();
        }
      } else {																// Panel Field Changed... Update Property
        if (components.containsKey(sourceName)) {
        	setValue(sourceName, evt.getNewValue());
        }
      }

      System.out.println(propertyName + ": " + evt.getNewValue().toString());
      System.out.println(getValues());
    } catch (Exception exception) {
      System.out.println(evt);
      System.out.println(exception);
    }

  }
  
  public HashMap<String, Object> getValues() {
	  HashMap<String, Object> values = new HashMap<String, Object>();
	  int i = -1;
	  for (String name : components.keySet()) {
		  values.put(name, components.get(name).getValue());
	  }	 
	  return values;
  }

  /**
   * 	@param name
   * 	@return
   */
  public Object getValue(String name) {
    if (components.containsKey(name)) return components.get(name).getValue();
    else return null;
  }

  /**
   * 	@param name
   * 	@param newValue
   */
  public void setValue(String name, Object newValue) {
    if (!components.containsKey(name)) return;

    NameValueField component = components.get(name);
    Object         oldValue  = component.getValue();

    if (oldValue.equals(newValue)) return;
    component.setValue(newValue);
    this.firePropertyChange(name, oldValue, newValue);
    this.notifyObservers();
    
    //GrasppeKit.de
    System.out.println(getValues());
  }

  // /**
  // *   @return the functionValue
  // */
  // public String getFunctionValue() {
  // return functionValue;
  // }

  // /**
  // *    @param newValue
  // */
  // public void setFunctionValue(String newValue) {
  // //this.functionValue = functionValue;
  //
  // String oldValue = this.functionValue;
  //
  // this.functionValue = newValue;
  // this.firePropertyChange("Function-Expression", oldValue, newValue);
  //
  // }

  // /**
  // *    @param newValue
  // */
  // public void setFunctionValue2(String newValue) {
  //
  // // this.functionValue2 = functionValue;
  //
  // String oldValue = this.functionValue;
  //
  // this.functionValue = newValue;
  // this.firePropertyChange("Function-Expression-2", oldValue, newValue);
  //
  // }
}
