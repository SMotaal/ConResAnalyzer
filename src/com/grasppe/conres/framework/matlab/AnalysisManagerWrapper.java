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

import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.analysis.model.AnalysisManagerModel;
import com.grasppe.lure.components.ObservableComponent;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
 * @author daflair
 *
 */
public class AnalysisManagerWrapper extends ObservableComponent implements Observer {

  /** Field description */
  public AnalysisManager analysisManager;

  /**
   *
   *    @param manager
   */
  public AnalysisManagerWrapper(AnalysisManager manager) {
    analysisManager = manager;
    getAnalysisManagerModel().attachObserver(this);
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
   *  @return
   */
  public AnalysisManager getAnalysisManager() {
    return analysisManager;

  }

  /**
   *  @return
   */
  public AnalysisManagerModel getAnalysisManagerModel() {
    return getAnalysisManager().getModel();
  }
}
