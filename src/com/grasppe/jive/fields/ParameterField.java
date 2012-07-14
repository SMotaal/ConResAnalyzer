/*
 * @(#)ParameterField.java   12/07/13
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.jive.fields;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.text.FlowView.FlowStrategy;

import com.sun.tools.javac.comp.Flow;

/**
 * @author daflair
 *
 */
public class ParameterField extends JPanel implements PropertyChangeListener {

/**
	 * @return the fieldSuffix
	 */
	public Object getValue() {
		return ((ValueField)fieldComponent).getValue();
	}

	/**
	 * @param fieldSuffix the fieldSuffix to set
	 */
	public void setValue(Object newValue) {
		((ValueField)fieldComponent).setValue(newValue);
	}

//Size Variables
  protected static HashMap<String, GroupOptions> gourpOptionsMap = new HashMap<String, GroupOptions>();
  protected String                               fieldLabel;
  protected String                               fieldSuffix;
  protected JComponent                           fieldComponent;
  protected JLabel                               labelComponent;
  protected JLabel                               suffixComponent;
  protected ParameterField                       fieldPanel;
  protected GroupOptions                         groupOptions;
  protected String                               groupID = "default";

  // protected static GroupOptions textGroupOptions = new GroupOptions(200, 0);

  /**
   *    @param name
   *   @param fieldComponent
   *   @param fieldLabel
   *   @param fieldSuffix
   */
  public ParameterField(String name, JComponent fieldComponent, String fieldLabel, String fieldSuffix) {
    super();

    this.fieldPanel = this;

    this.setName(name);

    this.fieldComponent  = fieldComponent;
    fieldComponent.addPropertyChangeListener("value", this);    
    
    this.fieldLabel      = fieldLabel;
    this.fieldSuffix     = fieldSuffix;

    this.labelComponent  = new JLabel(fieldLabel);
    this.suffixComponent = new JLabel(fieldSuffix);

    this.groupOptions    = getGroupOptions(groupID);
    
    this.addComponentListener(new ComponentAdapter() {

		/* (non-Javadoc)
		 * @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
		 */
		@Override
		public void componentResized(ComponentEvent e) {
			// TODO Auto-generated method stub
			super.componentResized(e);
//			
//			Component component = e.getComponent();
//			
//			double minX = component.getMinimumSize().getWidth();
//			double minY = component.getMinimumSize().getHeight();
//			double newX = component.getWidth();
//			double newY = component.getHeight();
//			
//			component.setSize((int)Math.max(minX, newX), (int)Math.max(minY, newY));
		}
	});

    this.updateLayout();

  }

  /**
   *    @param evt
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
	  try {
	  if (evt.getSource().equals(fieldComponent)) // && evt.getPropertyName().equals("value"))
		  	firePropertyChange(this.getName(), evt.getOldValue(), evt.getNewValue());

	    } catch (Exception exception) {
	    	System.out.println(evt);
	    	System.out.println(exception);
	    }
	  
  }

  /**
   */
  public void updateLayout() {

    fieldPanel.removeAll();
    
    // Field Panel
    BoxLayout layout = new BoxLayout(fieldPanel, BoxLayout.LINE_AXIS);
    
    fieldPanel.add(Box.createHorizontalStrut(getOptions().marginWidth));
    fieldPanel.add(labelComponent);			// , BorderLayout.LINE_START);
    fieldPanel.add(Box.createHorizontalStrut(getOptions().paddingWidth));
    fieldPanel.add(fieldComponent);			// , BorderLayout.CENTER);
    fieldPanel.add(Box.createHorizontalStrut(getOptions().paddingWidth));
    fieldPanel.add(suffixComponent);		// , BorderLayout.LINE_END);
    fieldPanel.add(Box.createHorizontalStrut(getOptions().marginWidth));
    
    labelComponent.setAlignmentX(LEFT_ALIGNMENT);
    fieldComponent.setAlignmentX(LEFT_ALIGNMENT);
    suffixComponent.setAlignmentX(LEFT_ALIGNMENT);    
    
    // Label Component
    labelComponent.setLabelFor(fieldComponent);    
    labelComponent.setPreferredSize(new Dimension(getOptions().labelWidth, labelComponent.getPreferredSize().height));
    labelComponent.setMinimumSize(new Dimension(getOptions().labelWidth, labelComponent.getPreferredSize().height));//new Dimension(labelComponent.getPreferredSize()));
    labelComponent.setMaximumSize(new Dimension(getOptions().labelWidth, labelComponent.getPreferredSize().height));//new Dimension(labelComponent.getPreferredSize()));
    labelComponent.setSize(labelComponent.getPreferredSize());
    labelComponent.setHorizontalAlignment(JLabel.TRAILING);
    
    // Field Component
    fieldComponent.setPreferredSize(new Dimension(getOptions().fieldWidth, fieldComponent.getPreferredSize().height));
    fieldComponent.setMinimumSize(new Dimension(getOptions().fieldWidth/2, fieldComponent.getPreferredSize().height));
    fieldComponent.setMaximumSize(new Dimension(getOptions().fieldWidth, fieldComponent.getPreferredSize().height));
    fieldComponent.setSize(fieldComponent.getPreferredSize());
    
    // Suffix Component
    suffixComponent.setPreferredSize(new Dimension(getOptions().suffixWidth, suffixComponent.getPreferredSize().height));
    suffixComponent.setMinimumSize(new Dimension(suffixComponent.getPreferredSize()));
    suffixComponent.setMaximumSize(new Dimension(suffixComponent.getPreferredSize()));    
    suffixComponent.setSize(suffixComponent.getPreferredSize());
    suffixComponent.setHorizontalAlignment(JLabel.LEADING);
    
    fieldPanel.setLayout(layout);
    fieldPanel.setMaximumSize(new Dimension(getOptions().getMaximumWidth() , (int) fieldPanel.getMinimumSize().getHeight()));
    fieldPanel.setMinimumSize(new Dimension(getOptions().getMinimumWidth() , (int) fieldPanel.getMinimumSize().getHeight()));
    
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
  public static GroupOptions getGroupOptions(String id) {
    if (!ParameterField.gourpOptionsMap.containsKey(id)) ParameterField.setGroupOptions(id, new GroupOptions());

    return ParameterField.gourpOptionsMap.get(id);
  }

  /**
   * 	@return
   */
  public GroupOptions getOptions() {
    return getGroupOptions(groupID);
  }

  /**
   * @param groupID the groupID to set
   */
  public void setGroupID(String groupID) {
    this.groupID = groupID;
    this.groupOptions = ParameterField.getGroupOptions(groupID);
    try {
    	this.updateLayout();
    } catch (Exception exception){}
  }

  /**
   *    @param id
   *    @param options
   */
  public static void setGroupOptions(String id, GroupOptions options) {
    ParameterField.gourpOptionsMap.put(id, options);
  }
  
  /* (non-Javadoc)
	 * @see javax.swing.JComponent#setFont(java.awt.Font)
	 */
	@Override
	public void setFont(Font font) {
		// TODO Auto-generated method stub
		for (Component component : this.getComponents()){
			try {
				component.setFont(font);
			} finally {
				
			}
		}
		super.setFont(font);
	}
  
}
