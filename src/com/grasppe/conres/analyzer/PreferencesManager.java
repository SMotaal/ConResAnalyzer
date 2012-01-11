/*
 * @(#)PreferencesManager.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer;

import com.grasppe.conres.analyzer.model.PreferencesManagerModel;
import com.grasppe.conres.analyzer.operations.ShowPreferences;
import com.grasppe.conres.analyzer.view.PreferencesManagerView;
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.preferences.Preferences;
import com.grasppe.conres.preferences.Preferences.Tags;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;

import java.util.LinkedHashMap;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PreferencesManager extends AbstractController {

  int	dbg = 0;

  /** Field description */
  public ConResAnalyzer	analyzer;

  /** Field description */
  public Preferences	preferences;

  /**
   * @param listener
   */
  private PreferencesManager(ActionListener listener) {
    super(listener);
    attachModel(new PreferencesManagerModel());
  }

  /**
   *  @param analyzer
   */
  public PreferencesManager(ConResAnalyzer analyzer) {
    this((ActionListener)analyzer);
    attachView(new PreferencesManagerView(this));
    setAnalyzer(analyzer);

//  getTargetManagerModel().attachObserver(this);
  }

  /**
   *  @param object1
   *  @param object2
   *  @return
   */
  private boolean areEqual(Object object1, Object object2) {
    if ((object1 == null) || (object2 == null)) return false;

    if (!object1.getClass().equals(object2.getClass()) &&!object2.getClass().equals(object1.getClass())) return false;

    try {
      return object1.equals(object2);
    } catch (Exception exception) {
      return false;
    }
  }

  /**
   * Create and populate all commands from scratch.
   */
  @Override
  public void createCommands() {
    commands = new LinkedHashMap<String, AbstractCommand>();
    putCommand(new ShowPreferences(this));
  }

  /*
   *  (non-Javadoc)
   * @see com.grasppe.lure.components.AbstractController#update()
   */

  /**
   */
  @Override
  public void update() {
    super.update();
    updateTargetManager();
  }

  /**
   */
  public void updateTargetManager() {

//  boolean   updateActiveTarget, updateActiveBlock;
//  
//  ConResTarget activeTarget = getTargetManagerModel().getActiveTarget();
//  
//  if (activeTarget==null) {
//    getModel().setActiveTarget(null);
//    updateActiveTarget = true;
//  } else {
//    if ((updateActiveTarget = areEqual(getModel().getActiveTarget(),
//            activeTarget)) == false)
//        getModel().setActiveTarget(activeTarget);           
//  }
//  
//  ConResBlock activeBlock = getTargetManagerModel().getActiveBlock();
//
//  if (activeBlock==null) {
//    getModel().setActiveBlock(null);
//    updateActiveBlock = true;
//  } else {
//    if ((updateActiveBlock = areEqual(getModel().getActiveBlock(),
//            activeBlock)) == false)
//        getModel().setActiveBlock(activeBlock);
//  }
//
//  GrasppeKit.debugText(getClass().getSimpleName() + " Update",
//                       GrasppeKit.cat((updateActiveTarget) ? "updateActiveTarget"
//          : "", (updateActiveBlock) ? "updateActiveBlock"
//                                    : ""), dbg);
//  
//  if (updateActiveTarget || updateActiveBlock) {
//    if (getAnalysisStepper()!=null)
//        getAnalysisStepper().updateActiveBlock();
//    getModel().notifyObservers();
//  }
//  return;

  }

  /**
   * @return the analyzer
   */
  public ConResAnalyzer getAnalyzer() {
    return analyzer;
  }

  /**
   *  @return
   */
  public CaseManager getCaseManager() {
    return getAnalyzer().getCaseManager();
  }

  /**
   * Returns a correctly-cast model.
   * @return
   */
  public PreferencesManagerModel getModel() {
    return (PreferencesManagerModel)model;
  }

  /**
   *     @return the preferences
   */
  protected Preferences getPreferences() {
    return preferences;
  }

  /**
   *  @return
   */
  public TargetManager getTargetManager() {
    return getAnalyzer().getTargetManager();
  }

  /**
   *  @return
   */
  public TargetManagerModel getTargetManagerModel() {
    return getTargetManager().getModel();
  }

  /**
   * 	@param preference
   * 	@return
   */
  public Object getValue(Tags preference) {
	  Object value =  Preferences.get(preference);
	  return value; //Preferences.get(preference);
  }
  
  /**
   * 	@param preference
   * 	@return
   */
  public Object putValue(Tags preference, Object value) {
	  Preferences.put(preference, value);
	  return getValue(preference);
  }

  /*
   *  (non-Javadoc)
   *   @see com.grasppe.lure.components.AbstractController#getView()
   */

  /**
   * 	@return
   */
  @Override
  public PreferencesManagerView getView() {
	  PreferencesManagerModel model =  getModel();
	  PreferencesManagerView view = model.getView();
    return (PreferencesManagerView)getModel().getView();
  }

  /**
   * @param analyzer the analyzer to set
   */
  public void setAnalyzer(ConResAnalyzer analyzer) {
    this.analyzer = analyzer;
  }
}
