/*
 * @(#)JiveModulePanel.java   12/07/14
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.components;

import com.grasppe.jive.JiveFieldFactory;
import com.grasppe.jive.fields.JiveGroupMetrics;
import com.grasppe.jive.fields.NameValueField;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;

/**
 * Class description
 * 	@version        $Revision: 1.0, 12/07/14
 * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
 */
public abstract class JiveModulePanel extends JiveAbstractPanel implements PropertyChangeListener {



/**
	 * @return the permanent
	 */
	public boolean isPermanent() {
		return permanent;
	}

	/**
	 * @param permanent the permanent to set
	 */
	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}

protected Collection<JiveField> fields       = new LinkedHashSet<JiveField>();
  private Map<String, NameValueField>  components   = new HashMap<String, NameValueField>();
  protected JiveGroupMetrics               groupMetrics = new JiveGroupMetrics();

  /**
   * 	@param name
   * 	@param title
   */
  public JiveModulePanel(String name, String title) {    super();
    initializePanel(name, title);
  }

  /**
   * 	@param name
   * 	@param title
   * 	@param isDoubleBuffered
   */
  public JiveModulePanel(String name, String title, boolean isDoubleBuffered) {
    super(isDoubleBuffered);
    initializePanel(name, title);
  }

  /**
   * 	@param name
   * 	@param title
   * 	@param layout
   */
  public JiveModulePanel(String name, String title, LayoutManager layout) {
    super(layout);
    initializePanel(name, title);
  }

  /**
   * 	@param name
   * 	@param title
   * 	@param layout
   * 	@param isDoubleBuffered
   */
  public JiveModulePanel(String name, String title, LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
    initializePanel(name, title);
  }

  /**
   *  @param field
   */
  protected void addField(JiveField field) {
    fields.add(field);
    field.addPropertyChangeListener(field.getName(), this);
    this.addPropertyChangeListener(field.getName(), this);
    components.put(field.getName(), (NameValueField)field.getFieldComponent());
  }

  /**
   */
  private void createFieldUI() {
	// BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);  
	    boolean box = false;
	  
//	GridLayout layout = new GridLayout();
	FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0); // groupMetrics.paddingWidth, groupMetrics.paddingHeight);
      

    this.setLayout(layout);
    this.setAlignmentX(LEFT_ALIGNMENT);
    
    this.setMaximumSize(new Dimension(JiveGroupMetrics.LONG_TEXT_OPTIONS.getMaximumWidth()+80, Integer.MAX_VALUE));
    
    // Dimension minimumLayout1 =  layout.minimumLayoutSize(this);

    if (box) add(Box.createVerticalStrut(groupMetrics.marginHeight));

    int fieldIndex = 0;

    for (JiveField field : fields) {
    	field.setAlignmentX(LEFT_ALIGNMENT);
      if (box && fieldIndex++ > 1) add(Box.createVerticalStrut(groupMetrics.paddingHeight));
      add(field);
    }

    if (box) add(Box.createVerticalStrut(groupMetrics.marginHeight));
    
    // Dimension minimumLayout2 =  layout.minimumLayoutSize(this);
    
    // this.setBorder(new LineBorder(Color.green));
  
//    flowResize();
    
    return;
    
  }

  /**
   */
  protected abstract void createFields();

  /**
   * 	@param name
   * 	@param title
   */
  private void initializePanel(String name, String title) {
//	if (JiveField.getGroupOptions("default-long", false)==null) JiveField.setGroupOptions("default-long", JiveGroupMetrics.LONG_TEXT_OPTIONS);
//	if (JiveField.getGroupOptions("default-short", false)==null) JiveField.setGroupOptions("default-short", JiveGroupMetrics.SHORT_TEXT_OPTIONS);

	  
    setName(name);
    setTitle(title);
    createFields();
    createFieldUI();
  }
  
  public static JiveFieldFactory TinyJiveFieldFactory() {
	  String groupID = "default-tiny";
	  JiveGroupMetrics groupMetrics = JiveGroupMetrics.TINY_TEXT_OPTIONS;
		if (JiveField.getGroupMetrics(groupID, false)==null) JiveField.setGroupMetrics(groupID, groupMetrics);
		return JiveFieldFactory.Group(groupID);
  }
  
  public static JiveFieldFactory ShortJiveFieldFactory() {
	  String groupID = "default-short";
	  JiveGroupMetrics groupMetrics = JiveGroupMetrics.SHORT_TEXT_OPTIONS;
	  if (JiveField.getGroupMetrics(groupID, false)==null) JiveField.setGroupMetrics(groupID, groupMetrics);
	  return JiveFieldFactory.Group(groupID);
  }
  
  public static JiveFieldFactory LongJiveFieldFactory() {
	  String groupID = "default-long";
	  JiveGroupMetrics groupMetrics = JiveGroupMetrics.LONG_TEXT_OPTIONS;
	  if (JiveField.getGroupMetrics(groupID, false)==null) JiveField.setGroupMetrics(groupID, groupMetrics);
	  return JiveFieldFactory.Group(groupID);
  }
  
  public static JiveFieldFactory DefaultJiveFieldFactory() {
	  String groupID = "default";
	  JiveGroupMetrics groupMetrics = new JiveGroupMetrics();
	  if (JiveField.getGroupMetrics(groupID, false)==null) JiveField.setGroupMetrics(groupID, groupMetrics);
	  return JiveFieldFactory.Group(groupID);
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
//         System.out.println(propertyName + ": " + evt.getNewValue().toString());
//        System.out.println(getValues());
      }
      GrasppeKit.debugText("ParametersPanel>Change", getValues().toString(), 6);
    } catch (Exception exception) {
//      System.out.println(evt);
    	GrasppeKit.debugText("ParametersPanel>Change", evt.toString(), 3);    	
//      System.out.println(exception);
      GrasppeKit.debugError("ParametersPanel>Change", exception, 2);
    }

  }

  /**
   *     @param name
   *     @return
   */
  public Object getValue(String name) {
    if (components.containsKey(name)) return components.get(name).getValue();
    else return null;
  }

  /**
   * 	@return
   */
  public HashMap<String, Object> getValues() {
    HashMap<String, Object> values = new HashMap<String, Object>();
    int                     i      = -1;

    for (String name : components.keySet()) {
      values.put(name, components.get(name).getValue());
    }

    return values;
  }

  /**
   *     @param name
   *     @param newValue
   */
  public void setValue(String name, Object newValue) {
    if (!components.containsKey(name)) return;

    NameValueField component = components.get(name);
    Object         oldValue  = component.getValue();

    if (oldValue.equals(newValue)) return;
    component.setValue(newValue);
    this.firePropertyChange(name, oldValue, newValue);
    this.notifyObservers();

    // GrasppeKit.de
//    System.out.println(getValues());
  }
  
  protected boolean permanent = false;
  
  
}
