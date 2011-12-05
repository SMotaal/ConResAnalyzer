/*
 * @(#)ConResAnalyzer.java   11/11/15
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer;

import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.conres.analyzer.operations.Quit;
import com.grasppe.conres.analyzer.view.ConResAnalyzerView;
import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.components.IAuxiliaryCaseManager;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Class description
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ConResAnalyzer extends AbstractController implements ActionListener {

    /* (non-Javadoc)
	 * @see com.grasppe.lure.components.AbstractController#getView()
	 */
	@Override
	public ConResAnalyzerView getView() {
		return analyzerView;
	}

	protected CaseManager			caseManager;
    protected TargetManager			targetManager;
    protected AnalysisManager		analysisManager;
    protected AbstractController[]	managers;		// = new AbstractController[]{caseManager, targetManager,analysisManager};
    protected ConResAnalyzerView	analyzerView;

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
        managers = new AbstractController[] { caseManager, targetManager, analysisManager };
        analyzerView.prepareView();
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
                        exception.printStackTrace();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Create and populate all commands from scratch.
     */
    @Override
    public void createCommands() {
        putCommand(new Quit(this));
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
                        exception.printStackTrace();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     */
    public void forceCommandUpdates() {
        Iterator<AbstractCommand>	commandIterator = getCommands().values().iterator();

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
                        exception.printStackTrace();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
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
        LinkedHashMap<String, AbstractCommand>	allCommands = new LinkedHashMap<String,
                                                                 AbstractCommand>();

        for (AbstractController manager : managers)
            if (manager != null) allCommands.putAll(appendCommands(manager));

        return allCommands;
    }

    /**
     * @return
     */
    @Override
    public ConResAnalyzerModel getModel() {

        // TODO Auto-generated method stub
        return (ConResAnalyzerModel)super.getModel();
    }

    /**
     * @return the targetManager
     */
    public TargetManager getTargetManager() {
        return targetManager;
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

        // TODO Auto-generated method stub
        super.setModel(newModel);
    }

    /**
     * @param targetManager the targetManager to set
     */
    public void setTargetManager(TargetManager targetManager) {
        this.targetManager = targetManager;
    }
}
