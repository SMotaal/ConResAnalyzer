/*
 * @(#)JiveField.java   12/07/13
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.jive.components;

import com.grasppe.jive.fields.JiveGroupMetrics;
import com.grasppe.jive.fields.NameValueField;
import com.grasppe.jive.fields.NamedField;
import com.grasppe.jive.fields.ValueField;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * @author daflair
 *
 */
public class JiveField extends JPanel implements PropertyChangeListener, NamedField, FocusListener {

//Size Variables
  protected static HashMap<String, JiveGroupMetrics> gourpMetricsMap = new HashMap<String, JiveGroupMetrics>();
  protected String                                   fieldLabel;
  protected String                                   fieldSuffix;
  protected JComponent                               fieldComponent;
  protected JLabel                                   labelComponent;
  protected JLabel                                   suffixComponent;
  protected JiveField                                fieldPanel;
  protected JiveGroupMetrics                         groupMetrics;
  protected String                                   groupID      = "default";
  protected boolean                                  debugBorders = false;

  // protected static JiveGroupMetrics textGroupOptions = new JiveGroupMetrics(200, 0);

  /**
   *    @param name
   *   @param fieldComponent
   *   @param fieldLabel
   *   @param fieldSuffix
   */
  public JiveField(String name, JComponent fieldComponent, String fieldLabel, String fieldSuffix) {
    super();

    this.fieldPanel = this;

    this.setName(name);

    this.fieldComponent = fieldComponent;
    fieldComponent.addPropertyChangeListener(this);

    this.fieldLabel      = fieldLabel;
    this.fieldSuffix     = fieldSuffix;

    this.labelComponent  = new JLabel(fieldLabel);
    this.suffixComponent = new JLabel(fieldSuffix);

    this.groupMetrics    = getGroupMetrics(groupID);

    this.addComponentListener(new ComponentAdapter() {

      /*
       *  (non-Javadoc)
       * @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
       */
      @Override
      public void componentResized(ComponentEvent e) {

        // TODO Auto-generated method stub
        super.componentResized(e);

//          
//      Component component = e.getComponent();
//      
//      double minX = component.getMinimumSize().getWidth();
//      double minY = component.getMinimumSize().getHeight();
//      double newX = component.getWidth();
//      double newY = component.getHeight();
//      
//      component.setSize((int)Math.max(minX, newX), (int)Math.max(minY, newY));
      }
    });

    this.updateLayout();

  }

  /**
   *    @param e
   */
  @Override
  public void focusGained(FocusEvent e) {

    // TODO Auto-generated method stub
//  GrasppeKit.debugText("JiveField", "Focus gained: " + e.toString(), 4); //getAncestorOfClass

//  if (e.getSource() instanceof JComponent) {
//      JComponent component = (JComponent)e.getSource();
//      
//      JiveAbstractPanel panel = (JiveAbstractPanel) SwingUtilities.getAncestorOfClass(JiveAbstractPanel.class, component);
//      
//      GrasppeKit.debugText("JiveField>Panel", "Focus gained: " + panel.getTitle(), 2);
//      
//      panel.makeActive();
//      
//  } else {
//      GrasppeKit.debugText("JiveField>Unknown", "Focus gained: " + e.getSource().toString(), 0);
//      Toolkit.getDefaultToolkit().beep();
//  }
  }

//public void updateActivePanel() {
//  
//  JiveAbstractPanel panel = (JiveAbstractPanel) SwingUtilities.getAncestorOfClass(JiveAbstractPanel.class, component);
//
//}

  /**
   *    @param e
   */
  @Override
  public void focusLost(FocusEvent e) {

    // TODO Auto-generated method stub
//  GrasppeKit.debugText("JiveField", "Focus lost: " + e.toString(), 2);//getAncestorOfClass

  }

  /**
   *    @param evt
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    try {
      if ((evt.getSource().equals(fieldComponent)) && (evt.getPropertyName().equals(fieldComponent.getName())))
        firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());

    } catch (Exception exception) {
      System.out.println(evt);
      System.out.println(exception);
    }

  }

  /**
   */
  public void updateLayout() {

    fieldPanel.removeAll();

    // fieldPanel.add(Box.createHorizontalStrut(getOptions().marginWidth));
    fieldPanel.add(labelComponent);			// , BorderLayout.LINE_START);
    fieldPanel.add(Box.createHorizontalStrut(getMetrics().paddingWidth));
    fieldPanel.add(fieldComponent);			// , BorderLayout.CENTER);
    fieldPanel.add(Box.createHorizontalStrut(getMetrics().paddingWidth));
    fieldPanel.add(suffixComponent);		// , BorderLayout.LINE_END);

    // fieldPanel.add(Box.createHorizontalStrut(getOptions().marginWidth));

    labelComponent.setAlignmentX(LEFT_ALIGNMENT);
    fieldComponent.setAlignmentX(LEFT_ALIGNMENT);
    suffixComponent.setAlignmentX(LEFT_ALIGNMENT);

    // Label Component
    labelComponent.setLabelFor(fieldComponent);
    labelComponent.setPreferredSize(new Dimension(getMetrics().labelWidth, labelComponent.getPreferredSize().height));
    labelComponent.setMinimumSize(
        new Dimension(
            getMetrics().labelWidth,
            labelComponent.getPreferredSize().height));			// new Dimension(labelComponent.getPreferredSize()));
    labelComponent.setMaximumSize(
        new Dimension(
            getMetrics().labelWidth,
            labelComponent.getPreferredSize().height));			// new Dimension(labelComponent.getPreferredSize()));
    labelComponent.setSize(labelComponent.getPreferredSize());
    labelComponent.setHorizontalAlignment(JLabel.TRAILING);
    labelComponent.setOpaque(false);

    // Field Component
    fieldComponent.setPreferredSize(new Dimension(getMetrics().fieldWidth, fieldComponent.getPreferredSize().height));
    fieldComponent.setMinimumSize(new Dimension(getMetrics().fieldWidth / 2, fieldComponent.getPreferredSize().height));
    fieldComponent.setMaximumSize(new Dimension(getMetrics().fieldWidth, fieldComponent.getPreferredSize().height));
    fieldComponent.setSize(fieldComponent.getPreferredSize());
    fieldComponent.setOpaque(false);

    fieldComponent.addFocusListener(this);

    // Suffix Component
    suffixComponent.setPreferredSize(new Dimension(getMetrics().suffixWidth, suffixComponent.getPreferredSize().height));
    suffixComponent.setMinimumSize(new Dimension(suffixComponent.getPreferredSize()));
    suffixComponent.setMaximumSize(new Dimension(suffixComponent.getPreferredSize()));
    suffixComponent.setSize(suffixComponent.getPreferredSize());
    suffixComponent.setHorizontalAlignment(JLabel.LEADING);
    suffixComponent.setOpaque(false);

    // Field Panel
    BoxLayout layout = new BoxLayout(fieldPanel, BoxLayout.LINE_AXIS);

    fieldPanel.setLayout(layout);

//  fieldPanel.validate();
//  fieldPanel.setPreferredSize(getPreferredSize());
//  fieldPanel.validate();
//  fieldPanel.setMaximumSize(new Dimension(getOptions().getMaximumWidth(), (int)fieldPanel.getMinimumSize().getHeight()));
//  fieldPanel.setMinimumSize(new Dimension(getOptions().getMinimumWidth(), (int)fieldPanel.getMinimumSize().getHeight()));
    fieldPanel.setOpaque(false);
    if (debugBorders) fieldPanel.setBorder(new LineBorder(Color.blue));

  }

  /**
   * @return the fieldComponent
   */
  public JComponent getFieldComponent() {
    return fieldComponent;
  }

  /**
   *     @return the groupID
   */
  public String getGroupID() {
    return groupID;
  }

  /**
   *    @param id
   *    @return
   */
  public static JiveGroupMetrics getGroupMetrics(String id) {
    if (!JiveField.gourpMetricsMap.containsKey(id)) JiveField.setGroupMetrics(id, new JiveGroupMetrics());

    return JiveField.gourpMetricsMap.get(id);
  }

  /**
   *    @param id
   *    @param autoCreate
   *    @return
   */
  public static JiveGroupMetrics getGroupMetrics(String id, boolean autoCreate) {
    if (autoCreate) return getGroupMetrics(id);
    else if (!JiveField.gourpMetricsMap.containsKey(id)) return null;
    else return JiveField.gourpMetricsMap.get(id);
  }

  /**
   *    @return
   */
  public JiveGroupMetrics getMetrics() {
    return getGroupMetrics(groupID);
  }

/*(non-Javadoc)
    * @see java.awt.Component#getName()
     */

  /**
   *    @return
   */
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   *     @return the fieldSuffix
   */
  public Object getValue() {
    return ((ValueField)fieldComponent).getValue();
  }

  /*
   *  (non-Javadoc)
   *   @see javax.swing.JComponent#setFont(java.awt.Font)
   */

  /**
   *    @param font
   */
  @Override
  public void setFont(Font font) {

    // TODO Auto-generated method stub
    for (Component component : this.getComponents()) {
      try {
        component.setFont(font);
      } finally {}
    }

    super.setFont(font);
  }

  /**
   * @param groupID the groupID to set
   */
  public void setGroupID(String groupID) {
    this.groupID      = groupID;
    this.groupMetrics = JiveField.getGroupMetrics(groupID);

    try {
      this.updateLayout();
    } catch (Exception exception) {}
  }

  /**
   *    @param id
   *    @param metrics
   */
  public static void setGroupMetrics(String id, JiveGroupMetrics metrics) {
    JiveField.gourpMetricsMap.put(id, metrics);
  }

  /*
   *  (non-Javadoc)
   * @see java.awt.Component#setName(java.lang.String)
   */

  /**
   *    @param name
   */
  @Override
  public void setName(String name) {
    try {
//      assert name != null;
      getFieldComponent().setName(name);
//    } catch (AssertionError error) {
//      GrasppeKit.debugError("JiveField>Component>setName", error, 5);
    } catch (NullPointerException exception) {
        GrasppeKit.debugError("JiveField>Component>setName", exception, 5);
    } catch (Exception exception) {
      GrasppeKit.debugError("JiveField>Component>setName", exception, 0);
    }

    super.setName(name);
  }

  /**
   *    @param newValue
   */
  public void setValue(Object newValue) {
    try {
      NameValueField field = (NameValueField)fieldComponent;

      if (field.getValue().equals(newValue)) return;
      field.setValue(newValue);
    } catch (Exception exception) {
      GrasppeKit.debugError("JiveField>Component>SetValue", exception, 0);
    }

  }

//public void updateActiveComponent(JComponent)

}
