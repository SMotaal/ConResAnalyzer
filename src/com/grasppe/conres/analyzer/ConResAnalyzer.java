/*
 * @(#)ConResAnalyzer.java   11/11/15
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer;

import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.conres.analyzer.view.ConResAnalyzerView;
import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.cases.CaseManager;
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

/**
 * Class description
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ConResAnalyzer extends AbstractController implements ActionListener {

	public static final String	BUILD = "0.1b-13";
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
        analyzerView.createView();
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

    /**
     * Create and populate all commands from scratch.
     */
    @Override
    public void createCommands() {

        // putCommand(new Quit(this));
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
        LinkedHashMap<String, AbstractCommand>	allCommands = new LinkedHashMap<String,
                                                                 AbstractCommand>();

        for (AbstractController manager : managers)
            if (manager != null) allCommands.putAll(appendCommands(manager));

        return allCommands;
    }

    /**
     * 	@return
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
     * 	@return
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
     * @param targetManager the targetManager to set
     */
    public void setTargetManager(TargetManager targetManager) {
        this.targetManager = targetManager;
    }
}
