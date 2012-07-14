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
import com.grasppe.jive.fields.ParameterField;
import com.grasppe.jive.fields.TextValueField;

//~--- JDK imports ------------------------------------------------------------

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
public class FunctionParametersPanel extends ModuleParametersPanel implements PropertyChangeListener {

  private String functionValue = "x2";		// functionValue = "x",

  // private MarkupValueField functionComponent;
  private TextValueField functionComponent;
  private ParameterField functionField;			// functionField,

  /**
   * Create the panel.
   */
  public FunctionParametersPanel() {

    this.addPropertyChangeListener(this);
    this.setName("Function-Panel");

    this.setTitle("Function");

    // setLayout(new FormLayout(PatchGeneratorParametersPanel.COLUMN_SPEC, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
    // FormFactory.DEFAULT_ROWSPEC,
    // FormFactory.RELATED_GAP_ROWSPEC,
    // FormFactory.DEFAULT_ROWSPEC,
    // FormFactory.RELATED_GAP_ROWSPEC, }));

    createPanelFields();
  }

  /**
   */
  private void createPanelFields() {

    ParameterField.setGroupOptions("long-text", GroupOptions.LONG_TEXT_OPTIONS);
    ParameterField.setGroupOptions("short-text", GroupOptions.SHORT_TEXT_OPTIONS);

    // functionField = Factory.createMarkupField("Function-Expression", "f(x)", functionValue, ";");
    // functionField.setGroupID("long-text");
    // //functionField.setPreferredSize(preferredSize)Size(new Dimension(functionField.getMinimumSize().width,200));
    // functionComponent = (MarkupValueField)functionField.getFieldComponent();//(TextValueField)createTextField("Function-Expression", functionValue, "f(x)", "", 2);

    functionField = Factory.createTextField("Function-Expression", "g(x)", functionValue, ";");
    functionField.setGroupID("long-text");
    functionComponent = (TextValueField)functionField.getFieldComponent();		// (TextValueField)createTextField("Function-Expression", functionValue, "f(x)", "", 2);

    BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

    this.setLayout(layout);			// GridLayout());

    // functionField.setAlignmentX(LEFT_ALIGNMENT);
    functionField.setAlignmentX(LEFT_ALIGNMENT);

    this.setAlignmentX(LEFT_ALIGNMENT);

    // add(Box.createVerticalStrut(functionField.getOptions().marginHeight));
    // add(functionField);
    add(Box.createVerticalStrut(functionField.getOptions().paddingHeight));
    add(functionField);
    add(Box.createVerticalStrut(functionField.getOptions().marginHeight));

    // functionField.addPropertyChangeListener(this);
    functionField.addPropertyChangeListener(this);

    // addParameterField(functionField, 2);

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
        frame.getContentPane().add(new FunctionParametersPanel());

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

      if (sourceName.matches("Function-Panel")) {

        // if (functionComponent.getValue().equals(functionValue))
        // return;

        // else if (propertyName.matches("Function-Expression")) functionComponent.setValue(functionValue);
        if (propertyName.matches("Function-Expression")) functionComponent.setValue(functionValue);

        else {
          return;
        }

        this.notifyObservers();

      } else {

        // Panel Field Changed... Update Property
        // else if (sourceName.matches("Function-Expression")) setFunctionValue(functionComponent.getValue());
        if (sourceName.matches("Function-Expression")) setFunctionValue2(functionComponent.getValue());
        else {
          return;
        }
      }

      System.out.println(propertyName + ": " + evt.getNewValue().toString());
    } catch (Exception exception) {
      System.out.println(evt);
      System.out.println(exception);
    }

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

  /**
   *    @param newValue
   */
  public void setFunctionValue2(String newValue) {

    // this.functionValue2 = functionValue;

    String oldValue = this.functionValue;

    this.functionValue = newValue;
    this.firePropertyChange("Function-Expression-2", oldValue, newValue);

  }
}
