/*
 * @(#)Tags.java   11/11/15
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer;

import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.conres.analyzer.operations.Close;
import com.grasppe.conres.analyzer.operations.Quit;
import com.grasppe.conres.analyzer.view.ConResAnalyzerView;
import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.matlab.MatLabManager;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.IAuxiliaryCaseManager;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Class description
 * @version        $Revision: 0.1, 12/12/03
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ConResAnalyzer extends AbstractController implements ActionListener {

  /**
	 * @return the matlabManager
	 */
	public MatLabManager getMatLabManager() {
		return matLabManager;
	}

	/**
	 * @param matlabManager the matlabManager to set
	 */
	public void setMatLabManager(MatLabManager matlabManager) {
		this.matLabManager = matlabManager;
	}

/** Field description */
  public static final String     BUILD = "0.2a-06";
  protected MatLabManager 		 matLabManager;
  protected CaseManager          caseManager;
  protected TargetManager        targetManager;
  protected AnalysisManager      analysisManager;
  protected AbstractController[] managers;		// = new AbstractController[]{caseManager, targetManager,analysisManager};
  protected ConResAnalyzerView   analyzerView;
  protected PreferencesManager   preferencesManager;

  // protected LinkedHashMap<String, AbstractCommand>  commands;

  /**
   * Constructs and attaches a new controller and a new model.
   */
  public ConResAnalyzer() {
    this(new ConResAnalyzerModel());
    analyzerView = new ConResAnalyzerView(this);
    attachView(analyzerView);
    updateCommands();
    setCaseManager(new CaseManager(this));
    setTargetManager(new TargetManager(this));
    setAnalysisManager(new AnalysisManager(this));
    setPreferencesManager(new PreferencesManager(this));
    setMatLabManager(new MatLabManager(this));
    managers = new AbstractController[] { caseManager, targetManager, analysisManager, preferencesManager, matLabManager };
    analyzerView.createView();

  }
  
  public ConResAnalyzer(com.mathworks.jmi.types.MLArrayRef matlabController) {
	  this();
	  
	  this.getMatLabManager().setMatlabController(matlabController);
  }

  /**
   * Constructs a new controller and attaches it to the unattached model.
   * @param model
   */
  private ConResAnalyzer(ConResAnalyzerModel model) {
    super(model);
  }

  /**
   */
  public void backgroundCurrentCase() {
    try {
      for (AbstractController manager : managers) {
        if ((manager != null) && (manager instanceof IAuxiliaryCaseManager)) {
          try {
            ((IAuxiliaryCaseManager)manager).backgroundCurrentCase();
          } catch (Exception exception) {
            GrasppeKit.debugError("Backgrounding Case", exception, 8);
          }
        }
      }
    } catch (Exception exception) {
      GrasppeKit.debugError("Backgrounding Case", exception, 8);
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
    return canQuitManagers();
  }

  /**
   *    @return
   */
  public boolean canQuitManagers() {
    boolean canQuit = true;

    for (AbstractController manager : managers) {
      manager.attemptQuit();

//    try {
//        manager.wait();
//    } catch (InterruptedException exception) {
//        return false;
//    }
      if (!manager.canQuit()) canQuit = false;
    }

    return canQuit;
  }

  /**
   * Create and populate all commands from scratch.
   */
  @Override
  public void createCommands() {
    putCommand(new Close(this));
    putCommand(new Quit(this));
  }

  /*
   *  (non-Javadoc)
   *   @see java.lang.Object#finalize()
   */

  /**
   *    @throws Throwable
   */
  @Override
  public void detatch() throws Throwable {
    GrasppeKit.debugText("Detatching", getClass().getSimpleName(), 1);
    
	try{
		getMatLabManager().fireAction("CloseAnalyzer");
	} catch (Exception exception) {
	}

    notifyDetatchObservers();

    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        try {
          for (AbstractController manager : managers) {
            manager.detatch();
          }

        } catch (Throwable exception) {
          GrasppeKit.debugError("Detatching Manager", exception, 1);
        }

      }

    });

    super.detatch();

    // if(commands!=null && commands.size()>0) {
    // for (String key : commands.keySet()) {
    // commands.get(key).finalize();
    // }
    // }
    //
    // commands.clear();
    //
    // if (getModel()!=null) {
    // AbstractView[] views = getModel().views.toArray(new AbstractView[0]);
    //
    // for(int i = 0; i < views.length; i++) {
    // views[i].finalize();
    // }
    //
    // getModel().detachObservers();
    // }

  }

  /**
   */
  public void discardBackgroundCase() {
    try {
      for (AbstractController manager : managers) {
        if ((manager != null) && (manager instanceof IAuxiliaryCaseManager)) {
          try {
            ((IAuxiliaryCaseManager)manager).discardBackgroundCase();
          } catch (Exception exception) {
            GrasppeKit.debugError("Discarding Case", exception, 8);
          }
        }
      }
    } catch (Exception exception) {
      GrasppeKit.debugError("Discarding Case", exception, 8);
    }
  }

  /**
   */
  public void forceCommandUpdates() {
    Iterator<AbstractCommand> commandIterator = getCommands().values().iterator();

    while (commandIterator.hasNext())
      commandIterator.next().update();
  }

  /**
   */
  public void restoreBackgroundCase() {
    try {
      for (AbstractController manager : managers) {
        if ((manager != null) && (manager instanceof IAuxiliaryCaseManager)) {
          try {
            ((IAuxiliaryCaseManager)manager).restoreBackgroundCase();
          } catch (Exception exception) {
            GrasppeKit.debugError("Restoring Case", exception, 8);
          }
        }
      }
    } catch (Exception exception) {
      GrasppeKit.debugError("Restoring Case", exception, 8);
    }
  }

  /**
   * @return the analysisManager
   */
  public AnalysisManager getAnalysisManager() {
    return analysisManager;
  }

  /**
   * @return the caseManager
   */
  public CaseManager getCaseManager() {
    return caseManager;
  }

  /**
   * @return
   */
  @Override
  public LinkedHashMap<String, AbstractCommand> getCommands() {
    LinkedHashMap<String, AbstractCommand> allCommands = new LinkedHashMap<String, AbstractCommand>();

    for (AbstractController manager : managers)
      if (manager != null) allCommands.putAll(appendCommands(manager));

    return allCommands;
  }

  /**
   *  @return
   */
  public JFrame getMainFrame() {
    return getView().getFrame();
  }

  /**
   * @return the managers
   */
  public AbstractController[] getManagers() {
    return managers;
  }

  /**
   * @return
   */
  @Override
  public ConResAnalyzerModel getModel() {
    return (ConResAnalyzerModel)super.getModel();
  }

  /**
   * @return the preferencesManager
   */
  public PreferencesManager getPreferencesManager() {
    return preferencesManager;
  }

  /**
   * @return the targetManager
   */
  public TargetManager getTargetManager() {
    return targetManager;
  }

  /*
   *  (non-Javadoc)
   * @see com.grasppe.lure.components.AbstractController#getView()
   */

  /**
   *  @return
   */
  @Override
  public ConResAnalyzerView getView() {
    return analyzerView;
  }

  /**
   * @param analysisManager the analysisManager to set
   */
  public void setAnalysisManager(AnalysisManager analysisManager) {
    this.analysisManager = analysisManager;
  }

  /**
   * @param caseManager the caseManager to set
   */
  public void setCaseManager(CaseManager caseManager) {
    this.caseManager = caseManager;
  }

  /**
   * @param newModel
   * @throws IllegalAccessException
   */
  public void setModel(ConResAnalyzerModel newModel) throws IllegalAccessException {
    super.setModel(newModel);
  }

  /**
   * @param preferencesManager the preferencesManager to set
   */
  public void setPreferencesManager(PreferencesManager preferencesManager) {
    this.preferencesManager = preferencesManager;
  }

  /**
   * @param targetManager the targetManager to set
   */
  public void setTargetManager(TargetManager targetManager) {
    this.targetManager = targetManager;
  }


}
