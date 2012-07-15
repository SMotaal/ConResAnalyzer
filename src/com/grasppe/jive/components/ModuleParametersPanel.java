/*
 * @(#)ModuleParametersPanel.java   12/07/09
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.components;

import com.grasppe.jive.fields.NumericValueField;
import com.grasppe.jive.fields.ParameterField;
import com.grasppe.jive.fields.ResolutionValueField;
import com.grasppe.jive.fields.TextValueField;
import com.grasppe.lure.components.Observers;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/09
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ModuleParametersPanel extends JPanel implements Observable {
	
	protected static ModuleParametersPanel activePanel;

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

/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		if (this.title!=null && this.title.equals(title)) return;
		firePropertyChange("Title", this.title, title);
		this.title = title;
	}

protected Observers observers = new Observers(this);
  protected String title;

  /**
   */
  public ModuleParametersPanel() {
    super();
    this.initializePanel();
  }

  /**
   *    @param isDoubleBuffered
   */
  public ModuleParametersPanel(boolean isDoubleBuffered) {
    super(isDoubleBuffered);
    this.initializePanel();
  }

  /**
   *    @param layout
   */
  public ModuleParametersPanel(LayoutManager layout) {
    super(layout);
    this.initializePanel();
  }

  /**
   *    @param layout
   *    @param isDoubleBuffered
   */
  public ModuleParametersPanel(LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
    this.initializePanel();
  }
  
  protected void flowResize() {
	  
	  JComponent container = (JComponent)this;
	  
		int height = 0; int width = 0;
		
		for (Component component : container.getComponents()) {
			height = Math.max(height, component.getY() + component.getHeight());
			width = Math.max(width, component.getX() + component.getWidth());
		}
		
		int minLayoutHeight = 10; //((Double)this.getLayout().minimumLayoutSize(this).getHeight()).intValue();
		int minLayoutWidth = 150; //((Double)this.getLayout().minimumLayoutSize(this).getWidth()).intValue();
		int minHeight = ((Double)this.getMinimumSize().getHeight()).intValue();
		int minWidth = ((Double)this.getMinimumSize().getWidth()).intValue() + 100;
		
		if (width<minLayoutWidth || height < minLayoutHeight) return; //Math.max(width,getMinimumSize().getWidth()) < 100) return;
//		if (Math.max(height,getMinimumSize().getHeight()) < this.getLayout().minimumLayoutSize(this).getHeight()) return;
		

		Dimension preferredSize = new Dimension(width, height); //container.getPreferredSize();
//		Dimension minimumSize = new Dimension(width, height);
//		Dimension maximumSize = container.getMaximumSize(); //getPreferredSize();
//		Dimension newSize = new Dimension(maximumSize.width, preferredSize.height);
		
		container.setPreferredSize(preferredSize);
		container.setMinimumSize(preferredSize); //minimumSize)Size(preferredSize);
		container.setMaximumSize(preferredSize);
		
		container.setOpaque(false);
		container.setBackground(Color.white);
		// container.setMaximumSize(newSize);
//		setAlignmentX(CENTER_ALIGNMENT);
//		setOpaque(true);
//		  setBackground(Color.blue);
		
		container.revalidate();
		
		if (container.getParent()!=null)
			container.getParent().validate();
		
	  
  }
  
  protected void initializePanel() {
	  this.addComponentListener(new ComponentAdapter() {

		/* (non-Javadoc)
		 * @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
		 */
		@Override
		public void componentResized(ComponentEvent e) {
			ModuleParametersPanel container = (ModuleParametersPanel) e.getComponent();
			
			container.flowResize();
//			int height = 0; int width = 0;
//			
//			for (Component component : container.getComponents()) {
//				height = Math.max(height, component.getY() + component.getHeight());
//				width = Math.max(width, component.getX() + component.getWidth());
//			}
//
//			Dimension preferredSize = new Dimension(width, height); //container.getPreferredSize();
//			Dimension maximumSize = container.getMaximumSize(); //getPreferredSize();
//			Dimension newSize = new Dimension(maximumSize.width, preferredSize.height);
//			
//			container.setPreferredSize(preferredSize);
			
			}
	});
	  
//	  this.addFocusListener(new FocusAdapter() {
//
//		/* (non-Javadoc)
//		 * @see java.awt.event.FocusAdapter#focusGained(java.awt.event.FocusEvent)
//		 */
//		@Override
//		public void focusGained(FocusEvent e) {
//			// TODO Auto-generated method stub
//			activePanel = (ModuleParametersPanel) e.getSource();
//			super.focusGained(e);
//		}
//		  
//	});
	  flowResize();
  }
  
  public void makeActive() {
	  activePanel = this;
  }
  
  public static ModuleParametersPanel getActivePanel() {
	  return activePanel;
  }

  /**
   *   Attaches an observer through the observers object which will include the observer in future update() notify calls.
   *   @param observer
   */
  public void attachObserver(Observer observer) {
    observers.attachObserver(observer);
  }

  /**
   *      @param name
   *      @param value
   *      @param label
   *      @param minimum
   *      @param maximum
   *      @param suffix
   *      @param row
   *      @return
   */
  protected JComponent createNumericField(String name, double value, String label, double minimum, double maximum, String suffix, int row) {
    JLabel lblName = new JLabel(label + ":");

    add(lblName, "2, " + row + ", right, default");

    JLabel lblSuffix = new JLabel(suffix);

    add(lblSuffix, "6, " + row + ", left, default");

    NumericValueField txtField = new NumericValueField(minimum, maximum);

    txtField.setHorizontalAlignment(SwingConstants.TRAILING);
    txtField.setName(name);
    txtField.setValue(value);
    txtField.addPropertyChangeListener("value", (PropertyChangeListener)this);
    add(txtField, "4, " + row);

    return txtField;
  }
  
  protected JComponent createListField(String name, Object value, Object[] options, String label, String suffix, int row) {
	    JLabel lblName = new JLabel(label + ":");

	    add(lblName, "2, " + row + ", right, default");

	    JLabel lblSuffix = new JLabel(suffix);

	    add(lblSuffix, "6, " + row + ", left, default");


	    JComboBox lstField = new JComboBox(options);
	    
	    lstField.putClientProperty("JComboBox.isSquare", true);
	    lstField.putClientProperty("JComponent.sizeVariant", "regular");
	    
	    lstField.setName(name);
	    try {
	    	lstField.setSelectedItem(value);
	    } finally {
	    	lstField.setSelectedIndex((Integer)value);
	    }
	    
	    lstField.addPropertyChangeListener("value", (PropertyChangeListener)this);
	    add(lstField, "4, " + row);
	    
	    lstField.setEditable(true);
	    
	    return lstField;
  }
  
  protected void addParameterField(ParameterField field, int row) {
	  add(field, "2, " + row + ", right, default");
  }
  
  protected JComponent createTextField(String name, String value, String label, String suffix, int row) {
	    JLabel lblName = new JLabel(label + ":");

	    add(lblName, "2, " + row + ", right, default");

	    JLabel lblSuffix = new JLabel(suffix);

	    add(lblSuffix, "6, " + row + ", left, default");


	    TextValueField txtField = new TextValueField(value);
	    
	    
	    txtField.setName(name);
	    
	    txtField.addPropertyChangeListener("value", (PropertyChangeListener)this);
	    add(txtField, "4, " + row);
	    
//	    txtField.setMinimumSize(new Dimension(180, txtField.getMinimumSize().height));
	    
	    // txtField.setEditable(true);
	    
	    return txtField;
}
  

  /**
   *    @param name
   *    @param value
   *    @param label
   *    @param minimum
   *    @param maximum
   *    @param suffix
   *    @param row
   *    @return
   */
  protected JComponent createResolutionField(String name, double value, String label, double minimum, double maximum, String suffix, int row) {
    JLabel lblName = new JLabel(label + ":");

    add(lblName, "2, " + row + ", right, default");

    JLabel lblSuffix = new JLabel(suffix);

    add(lblSuffix, "6, " + row + ", left, default");

    ResolutionValueField txtField = new ResolutionValueField(minimum, maximum);

    txtField.setHorizontalAlignment(SwingConstants.TRAILING);
    txtField.setName(name);
    txtField.setValue(value);
    txtField.addPropertyChangeListener("value", (PropertyChangeListener)this);
    add(txtField, "4, " + row);

    return txtField;
  }

  /**
   *   Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
   *   @param observer
   */
  public void detachObserver(Observer observer) {
    observers.detachObserver(observer);
  }

  /**
   *   Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
   */
  public void detachObservers() {
    observers.detachObservers();
  }

  /**
   *    @param observer
   */
  public void notifyObserver(Observer observer) {
    observer.update();
  }

  /**
   *   Notifies all observer through the observers object which calls update().
   */
  public void notifyObservers() {
    observers.notifyObservers();
  }

  /**
   *    @return
   */
  public String observerString() {
    return observers.toString();
  }
}
