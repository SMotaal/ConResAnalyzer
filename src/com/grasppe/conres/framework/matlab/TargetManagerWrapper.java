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

import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.framework.targets.model.grid.ConResTarget;
import com.grasppe.lure.components.ObservableComponent;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
 * @author daflair
 *
 */
public class TargetManagerWrapper extends ObservableComponent implements Observer {

  /** Field description */
  public TargetManager targetManager;

  /**
   *
   *    @param manager
   */
  public TargetManagerWrapper(TargetManager manager) {
    targetManager = manager;
    getTargetManagerModel().attachObserver(this);
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
  public ConResBlock getActiveBlock() {
    return getTargetManagerModel().getActiveBlock();
  }

  /**
   *    @return
   */
  public ConResTarget getActiveTarget() {
    return getTargetManagerModel().getActiveTarget();
  }

  /**
   *  @return
   */
  public TargetManager getTargetManager() {
    return targetManager;
  }

  /**
   *  @return
   */
  public TargetManagerModel getTargetManagerModel() {
    return getTargetManager().getModel();
  }
}
