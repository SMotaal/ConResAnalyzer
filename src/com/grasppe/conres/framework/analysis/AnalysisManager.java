/*
 * @(#)AnalysisManager.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.framework.analysis.model.AnalysisManagerModel;
import com.grasppe.conres.framework.analysis.operations.AnalyzeBlock;
import com.grasppe.conres.framework.analysis.operations.ExportAnalysis;
import com.grasppe.conres.framework.analysis.view.AnalysisManagerView;
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.framework.targets.model.grid.ConResTarget;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.framework.GrasppeKit;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;

import java.util.LinkedHashMap;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AnalysisManager extends AbstractController {

    /* (non-Javadoc)
	 * @see com.grasppe.lure.components.AbstractController#canQuit()
	 */
	@Override
	public boolean canQuit() {
		return getAnalysisStepper().canQuit();
	}

	/**
	 * @return the analysisStepper
	 */
	public AnalysisStepper getAnalysisStepper() {
		if (analysisStepper==null) {
			analysisStepper = new AnalysisStepper(this);
			analysisStepper.getModel().attachObserver(getCommand("ExportAnalysis"));
		}
		return analysisStepper;
	}

	int	dbg = 0;

    /** Field description */
    public ConResAnalyzer	analyzer;
    protected AnalysisStepper analysisStepper;

    /**
     * @param listener
     */
    private AnalysisManager(ActionListener listener) {
        super(listener);
    }

    /**
     *  @param analyzer
     */
    public AnalysisManager(ConResAnalyzer analyzer) {
        this((ActionListener)analyzer);
        attachView(new AnalysisManagerView(this));
        setAnalyzer(analyzer);
        getTargetManagerModel().attachObserver(this);
    }

    /**
     * 	@param object1
     * 	@param object2
     * 	@return
     */
    private boolean areEqual(Object object1, Object object2) {
        if ((object1 == null) || (object2 == null)) return false;

        if (!object1.getClass().equals(object2.getClass())
                &&!object2.getClass().equals(object1.getClass()))
            return false;

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
        putCommand(new AnalyzeBlock(this, this));
        putCommand(new ExportAnalysis(this, this));
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
        boolean	updateActiveTarget,	updateActiveBlock;
        
        ConResTarget activeTarget = getTargetManagerModel().getActiveTarget();
        
        if (activeTarget==null) {
        	getModel().setActiveTarget(null);
        	updateActiveTarget = true;
        } else {
//        	if ((updateActiveTarget = areEqual(getModel().getActiveTarget(), activeTarget)) == false)
        	if (updateActiveTarget=getTargetManagerModel().hasChanged("activeTarget"))
        		getModel().setActiveTarget(activeTarget);        	
        }
        
        ConResBlock activeBlock = getTargetManagerModel().getActiveBlock();

        if (activeBlock==null) {
        	getModel().setActiveBlock(null);
        	updateActiveBlock = true;
        } else {
//	        if ((updateActiveBlock = areEqual(getModel().getActiveBlock(), activeBlock)) == false)
        	if (updateActiveBlock=getTargetManagerModel().hasChanged("activeBlock"))
	            getModel().setActiveBlock(activeBlock);
        }

        GrasppeKit.debugText(getClass().getSimpleName() + " Update",
                             GrasppeKit.cat((updateActiveTarget) ? "updateActiveTarget"
                : "", (updateActiveBlock) ? "updateActiveBlock"
                                          : ""), dbg);
        
        if (updateActiveTarget || updateActiveBlock) {
        	if (getAnalysisStepper()!=null)
        		getAnalysisStepper().updateActiveBlock();
        	getModel().notifyObservers();
        }
        return;

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
    public AnalysisManagerModel getModel() {
        return (AnalysisManagerModel)model;
    }

    /**
     * @return
     */
    @Override
    protected AnalysisManagerModel getNewModel() {
        GrasppeKit.debugText(getClass().getSimpleName(), "Getting new Model", dbg);

        return new AnalysisManagerModel();

    }
    
    /**
     * 	@return
     */
    public TargetManager getTargetManager() {
        return getAnalyzer().getTargetManager();
    }

    /**
     * 	@return
     */
    public TargetManagerModel getTargetManagerModel() {
        return getTargetManager().getModel();
    }

    /**
     * @param analyzer the analyzer to set
     */
    public void setAnalyzer(ConResAnalyzer analyzer) {
        this.analyzer = analyzer;
    }
}
