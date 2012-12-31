/*
 * @(#)AnalysisManager.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.matlab;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.matlab.model.MatLabManagerModel;
import com.grasppe.conres.framework.matlab.operations.RunBlockAnalysis;
import com.grasppe.conres.framework.matlab.operations.RunCaseAnalysis;
import com.grasppe.jive.components.AbstractActionWrapper;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.DebuggableComponent;
import com.grasppe.lure.framework.GrasppeKit;

import com.mathworks.jmi.types.MLArrayRef;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;

import java.util.LinkedHashMap;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class MatLabManager extends AbstractController {

  int dbg = 0;

  /** Field description */
  public ConResAnalyzer analyzer;

  /** Field description */
  public CaseManagerWrapper caseWrapper;

  /** Field description */
  public TargetManagerWrapper targetWrapper;

  /** Field description */
  public AnalysisManagerWrapper analysisWrapper;

  /** Field description */
  public MLArrayRef matlabController;

///** Field description */
//public CallbackHook runBlockAnalysisHook = new CallbackHook();
//
///** Field description */
//public CallbackHook closeAnalyzerHook = new CallbackHook();

  /** Field description */
  private AbstractActionWrapper actionWrapper = new AbstractActionWrapper();

  /**
   * @param listener
   */
  private MatLabManager(ActionListener listener) {
    super(listener);

  }

  /**
   *  @param analyzer
   */
  public MatLabManager(ConResAnalyzer analyzer) {
    this((ActionListener)analyzer);

    // attachView(new AnalysisManagerView(this));
    setAnalyzer(analyzer);

    // Attach Wrappers
    caseWrapper     = new CaseManagerWrapper(getAnalyzer().getCaseManager());
    targetWrapper   = new TargetManagerWrapper(getAnalyzer().getTargetManager());
    analysisWrapper = new AnalysisManagerWrapper(getAnalyzer().getAnalysisManager());

    // Attach Wrapper-Observers
    getCaseWrapper().attachObserver(this);
    getTargetWrapper().attachObserver(this);
    getAnalysisWrapper().attachObserver(this);

    // getTargetManagerModel().attachObserver(this);
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

  /*
   *  (non-Javadoc)
   * @see com.grasppe.lure.components.AbstractController#canQuit()
   */

  /**
   *    @return
   */
  @Override
  public boolean canQuit() {
    return true;

    // return getAnalysisStepper().canQuit();
  }

  /**
   * Create and populate all commands from scratch.
   */
  @Override
  public void createCommands() {
    commands = new LinkedHashMap<String, AbstractCommand>();

    putCommand(new RunBlockAnalysis(this, this));
    putCommand(new RunCaseAnalysis(this, this));
    
  }

  /**
   * 	@param oberservableObject
   */
  @Override
  public void detatch(com.grasppe.lure.framework.GrasppeKit.Observable oberservableObject) {
    try {
      fireAction("CloseAnalyzer");
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /**
   *     @param actionName
   *     @see com.grasppe.jive.components.AbstractActionWrapper#fireAction(java.lang.String)
   */
  public void fireAction(String actionName) {
    actionWrapper.fireAction(actionName);
  }

  /**
   * @param actionName
   * @param data
   * @see com.grasppe.jive.components.AbstractActionWrapper#fireAction(java.lang.String, java.lang.Object)
   */
  public void fireAction(String actionName, Object data) {
    actionWrapper.fireAction(actionName, data);
  }

  /**
   * @param actionName
   * @param data
   * @param metadata
   * @see com.grasppe.jive.components.AbstractActionWrapper#fireAction(java.lang.String, java.lang.Object, java.lang.Object)
   */
  public void fireAction(String actionName, Object data, Object metadata) {
    actionWrapper.fireAction(actionName, data, metadata);
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

  }

  /**
   *   @return the actionWrapper
   */
  public AbstractActionWrapper getActionWrapper() {
    return actionWrapper;
  }

  /**
   *    @return
   */
  public AnalysisManagerWrapper getAnalysisWrapper() {
    return analysisWrapper;
  }

  /**
   * @return the analyzer
   */
  public ConResAnalyzer getAnalyzer() {
    return analyzer;
  }

  /**
   *    @return
   */
  public CaseManagerWrapper getCaseWrapper() {
    return caseWrapper;
  }

  /**
   *   @return the matlabController
   */
  public com.mathworks.jmi.types.MLArrayRef getMatLabController() {
    return matlabController;
  }

  /**
   *  @return
   */
  public AnalysisManager getMatLabManager() {
    return getAnalyzer().getAnalysisManager();
  }

  /**
   * Returns a correctly-cast model.
   * @return
   */
  public MatLabManagerModel getModel() {
    return (MatLabManagerModel)model;
  }

  /**
   * @return
   */
  @Override
  protected MatLabManagerModel getNewModel() {
    GrasppeKit.debugText(getClass().getSimpleName(), "Getting new Model", dbg);

    return new MatLabManagerModel();

  }

  /**
   *    @return
   */
  public TargetManagerWrapper getTargetWrapper() {
    return targetWrapper;
  }

  /**
   *    @return
   */
  public boolean hasMatLabController() {
    MLArrayRef matlabController = getMatLabController();

//  System.out.println(matlabController);
//  System.out.println(matlabController.isObject());
//  System.out.println(matlabController.isUnknown());
//  System.out.println(matlabController.getN());
//  System.out.println(matlabController.getM());
//  System.out.println(matlabController.getNDimensions());
//  System.out.println(matlabController.getData());

    // (matlabController.isObject() | matlabController.isUnknown()) &

    return matlabController.getN() == 1 & matlabController.getM() == 1 & matlabController.getNDimensions() == 2;

  }

///**
// *  @return
// */
//public TargetManager getTargetManager() {
//  return getAnalyzer().getTargetManager();
//}
//
///**
// *  @return
// */
//public TargetManagerModel getTargetManagerModel() {
//  return getTargetManager().getModel();
//}

  /**
   * @param analyzer the analyzer to set
   */
  public void setAnalyzer(ConResAnalyzer analyzer) {
    this.analyzer = analyzer;
  }

  /**
   * @param matlabController the matlabController to set
   */
  public void setMatlabController(com.mathworks.jmi.types.MLArrayRef matlabController) {
    this.matlabController = matlabController;

    if (hasMatLabController()) DebuggableComponent.setDebuggingEnabled(false);

//  System.out.println(matlabController.fieldNames());
  }

//private java.util.Vector data = new java.util.Vector();
//
//public interface MatLabCommandListener extends java.util.EventListener {
//    void matLabCommandEvent(MatLabCommandEvent event);
//}  
//
//public synchronized void addMatLabCommandListener(MatLabCommandListener lis) {
//    data.addElement(lis);
//}
//public synchronized void removeMatLabCommandListener(MatLabCommandListener lis) {
//    data.removeElement(lis);
//}
//
//public class MatLabCommandEvent extends java.util.EventObject {
//    private static final long serialVersionUID = 1L;
//    //public float oldValue,newValue;        
//    MatLabCommandEvent(Object obj) { super(obj); }
//    //, float oldValue, float newValue) {
//    //    super(obj);
//        //this.oldValue = oldValue;
//        //this.newValue = newValue;
//    //}
//    
//    // http://undocumentedmatlab.com/blog/matlab-callbacks-for-java-events/
//}
//
//public void notifyRunBlockAnalysis() {
//    java.util.Vector dataCopy;
//    synchronized(this) {
//        dataCopy = (java.util.Vector)data.clone();
//    }
//    
//    for (int i=0; i<dataCopy.size(); i++) {
//      MatLabCommandEvent event = new MatLabCommandEvent(this);
//        ((MatLabCommandListener)dataCopy.elementAt(i)).matLabCommandEvent(event);
//    }
//}

}
