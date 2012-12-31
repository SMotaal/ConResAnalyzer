/*
 * @(#)OldAbstractActionWrapper.java   12/12/05
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.matlab.trash;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;

/**
 * Class description
 * 	@version        $Revision: 1.0, 12/12/05
 * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
 */
public class OldAbstractActionWrapper extends AbstractAction {

  /* (non-Javadoc)
	 * @see javax.swing.AbstractAction#addActionListener(java.beans.PropertyChangeListener)
	 */
	public synchronized void addActionListener(
			PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		super.addPropertyChangeListener(listener);
	}

	/* (non-Javadoc)
	 * @see javax.swing.AbstractAction#fireAction(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	protected void fireAction(String propertyName, Object oldValue,
			Object newValue) {
		// TODO Auto-generated method stub
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	/* (non-Javadoc)
	 * @see javax.swing.AbstractAction#getActionListeners()
	 */
	public synchronized PropertyChangeListener[] getActionListeners() {
		// TODO Auto-generated method stub
		return super.getPropertyChangeListeners();
	}

	/* (non-Javadoc)
	 * @see javax.swing.AbstractAction#removeActionListener(java.beans.PropertyChangeListener)
	 */
	public synchronized void removeActionListener(
			PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		super.removePropertyChangeListener(listener);
	}

/**
   *
   */
  public OldAbstractActionWrapper() {

    // TODO Auto-generated constructor stub
  }

@Override
public void actionPerformed(ActionEvent arg0) {
	// TODO Auto-generated method stub
	System.out.println(arg0);
}
}
