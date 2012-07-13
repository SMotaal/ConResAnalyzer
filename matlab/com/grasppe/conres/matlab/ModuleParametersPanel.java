/*
 * @(#)ModuleParametersPanel.java   12/07/09
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.matlab;

import com.grasppe.forms.fields.NumericValueField;
import com.grasppe.forms.fields.ResolutionValueField;
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

import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/09
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ModuleParametersPanel extends JPanel implements Observable {

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
  
  protected void initializePanel() {
	  this.addComponentListener(new ComponentAdapter() {

		/* (non-Javadoc)
		 * @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
		 */
		@Override
		public void componentResized(ComponentEvent e) {
			JComponent component = (JComponent) e.getComponent();
			
			Dimension preferredSize = component.getPreferredSize();
			Dimension maximumSize = component.getMaximumSize(); //getPreferredSize();
			Dimension newSize = new Dimension(maximumSize.width, preferredSize.height);
			
			component.setMaximumSize(newSize);
//			setAlignmentX(CENTER_ALIGNMENT);
//			setOpaque(true);
//			  setBackground(Color.blue);
			}
	});
	  
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
