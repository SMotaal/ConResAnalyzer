/*
 * @(#)AbstractActionWrapper.java   12/12/05
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.components;

import java.awt.event.ActionEvent;

import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/12/05
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AbstractActionWrapper extends AbstractAction {

  /**
   *
   */
  public AbstractActionWrapper() {

    // TODO Auto-generated constructor stub
  }

  /**
   *    @param arg0
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {

    // TODO Auto-generated method stub
    System.out.println(arg0);
  }

//  /*
//   *  (non-Javadoc)
//   *   @see javax.swing.AbstractAction#addActionListener(java.beans.PropertyChangeListener)
//   */
//
//  /**
//   *    @param listener
//   */
//  public synchronized void addActionListener(PropertyChangeListener listener) {
//
//    // TODO Auto-generated method stub
//    super.addPropertyChangeListener(listener);
//  }

  /**
   * 	@param actionName
   */
  public void fireAction(String actionName) {

    // TODO Auto-generated method stub
    super.firePropertyChange(actionName, null, null);
  }

  /**
   * 	@param actionName
   * 	@param data
   */
  public void fireAction(String actionName, Object data) {

    // TODO Auto-generated method stub
    super.firePropertyChange(actionName, data, null);
  }

  /*
   *  (non-Javadoc)
   * @see javax.swing.AbstractAction#fireAction(java.lang.String, java.lang.Object, java.lang.Object)
   */

  /**
   *    @param actionName
   *    @param data
   *    @param metadata
   */
  public void fireAction(String actionName, Object data, Object metadata) {

    // TODO Auto-generated method stub
    super.firePropertyChange(actionName, data, metadata);
  }

//  /*
//   *  (non-Javadoc)
//   * @see javax.swing.AbstractAction#removeActionListener(java.beans.PropertyChangeListener)
//   */
//
//  /**
//   *    @param listener
//   */
//  public synchronized void removeActionListener(PropertyChangeListener listener) {
//
//    // TODO Auto-generated method stub
//    super.removePropertyChangeListener(listener);
//  }
//
//  /*
//   *  (non-Javadoc)
//   * @see javax.swing.AbstractAction#getActionListeners()
//   */
//
//  /**
//   *    @return
//   */
//  public synchronized PropertyChangeListener[] getActionListeners() {
//
//    // TODO Auto-generated method stub
//    return super.getPropertyChangeListeners();
//  }
}
