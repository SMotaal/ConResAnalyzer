/*
 * @(#)TargetManagerWrapper.java   12/12/04
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.matlab;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.conres.framework.cases.model.CaseModel;
import com.grasppe.lure.components.ObservableComponent;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
 * @author daflair
 *
 */
public class CaseManagerWrapper extends ObservableComponent implements Observer {

  /** Field description */
  public CaseManager caseManager;

  /**
   *
   *    @param manager
   */
  public CaseManagerWrapper(CaseManager manager) {
    caseManager = manager;
    getCaseManagerModel().attachObserver(this);
  }

  /*
   *  (non-Javadoc)
   * @see com.grasppe.lure.framework.GrasppeKit.Observer#detatch(com.grasppe.lure.framework.GrasppeKit.Observable)
   */

  /**
   *    @param oberservableObject
   */
  @Override
  public void detatch(Observable oberservableObject) {

    // TODO Auto-generated method stub

  }

  /*
   *  (non-Javadoc)
   * @see com.grasppe.lure.framework.GrasppeKit.Observer#update()
   */

  /**
   */
  @Override
  public void update() {
    notifyObservers();
  }

  /**
   *    @return
   */
  public CaseModel getActiveCase() {
    return getCaseManagerModel().getCurrentCase();
  }

  /**
   *  @return
   */
  public CaseManager getCaseManager() {
    return caseManager;
  }

  /**
   *  @return
   */
  public CaseManagerModel getCaseManagerModel() {
    return getCaseManager().getModel();
  }
}
