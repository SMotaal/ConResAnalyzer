/*
 * @(#)JiveParametersPanel.java   12/07/14
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
public abstract class JiveParametersPanel extends ModuleParametersPanel implements PropertyChangeListener {



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

protected Collection<ParameterField> fields       = new LinkedHashSet<ParameterField>();
  private Map<String, NameValueField>  components   = new HashMap<String, NameValueField>();
  protected GroupOptions               groupOptions = new GroupOptions();

  /**
   * 	@param name
   * 	@param title
   */
  public JiveParametersPanel(String name, String title) {    super();
    initializePanel(name, title);
  }

  /**
   * 	@param name
   * 	@param title
   * 	@param isDoubleBuffered
   */
  public JiveParametersPanel(String name, String title, boolean isDoubleBuffered) {
    super(isDoubleBuffered);
    initializePanel(name, title);
  }

  /**
   * 	@param name
   * 	@param title
   * 	@param layout
   */
  public JiveParametersPanel(String name, String title, LayoutManager layout) {
    super(layout);
    initializePanel(name, title);
  }

  /**
   * 	@param name
   * 	@param title
   * 	@param layout
   * 	@param isDoubleBuffered
   */
  public JiveParametersPanel(String name, String title, LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
    initializePanel(name, title);
  }

  /**
   *  @param field
   */
  protected void addField(ParameterField field) {
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
	FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0); // groupOptions.paddingWidth, groupOptions.paddingHeight);
      

    this.setLayout(layout);
    this.setAlignmentX(LEFT_ALIGNMENT);
    
    this.setMaximumSize(new Dimension(GroupOptions.LONG_TEXT_OPTIONS.getMaximumWidth()+80, Integer.MAX_VALUE));
    
    // Dimension minimumLayout1 =  layout.minimumLayoutSize(this);

    if (box) add(Box.createVerticalStrut(groupOptions.marginHeight));

    int fieldIndex = 0;

    for (ParameterField field : fields) {
    	field.setAlignmentX(LEFT_ALIGNMENT);
      if (box && fieldIndex++ > 1) add(Box.createVerticalStrut(groupOptions.paddingHeight));
      add(field);
    }

    if (box) add(Box.createVerticalStrut(groupOptions.marginHeight));
    
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
//	if (ParameterField.getGroupOptions("default-long", false)==null) ParameterField.setGroupOptions("default-long", GroupOptions.LONG_TEXT_OPTIONS);
//	if (ParameterField.getGroupOptions("default-short", false)==null) ParameterField.setGroupOptions("default-short", GroupOptions.SHORT_TEXT_OPTIONS);

	  
    setName(name);
    setTitle(title);
    createFields();
    createFieldUI();
  }
  
  public static Factory TinyFactory() {
	  String groupID = "default-tiny";
	  GroupOptions groupOptions = GroupOptions.TINY_TEXT_OPTIONS;
		if (ParameterField.getGroupOptions(groupID, false)==null) ParameterField.setGroupOptions(groupID, groupOptions);
		return Factory.Group(groupID);
  }
  
  public static Factory ShortFactory() {
	  String groupID = "default-short";
	  GroupOptions groupOptions = GroupOptions.SHORT_TEXT_OPTIONS;
	  if (ParameterField.getGroupOptions(groupID, false)==null) ParameterField.setGroupOptions(groupID, groupOptions);
	  return Factory.Group(groupID);
  }
  
  public static Factory LongFactory() {
	  String groupID = "default-long";
	  GroupOptions groupOptions = GroupOptions.LONG_TEXT_OPTIONS;
	  if (ParameterField.getGroupOptions(groupID, false)==null) ParameterField.setGroupOptions(groupID, groupOptions);
	  return Factory.Group(groupID);
  }
  
  public static Factory DefaultFactory() {
	  String groupID = "default";
	  GroupOptions groupOptions = new GroupOptions();
	  if (ParameterField.getGroupOptions(groupID, false)==null) ParameterField.setGroupOptions(groupID, groupOptions);
	  return Factory.Group(groupID);
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
