/*
 * @(#)ConResAnalyzer.java   11/11/15
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.analyzer;


import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.conres.analyzer.operations.Quit;
import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;

/**
 * Class description
 *
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ConResAnalyzer extends AbstractController implements ActionListener {

    /**
	 * @return the analysisManager
	 */
	public AnalysisManager getAnalysisManager() {
		return analysisManager;
	}

	/**
	 * @param analysisManager the analysisManager to set
	 */
	public void setAnalysisManager(AnalysisManager analysisManager) {
		this.analysisManager = analysisManager;
	}

	/**
	 * @return the caseManager
	 */
	public CaseManager getCaseManager() {
		return caseManager;
	}

	/**
	 * @param caseManager the caseManager to set
	 */
	public void setCaseManager(CaseManager caseManager) {
		this.caseManager = caseManager;
	}

	/**
	 * @return the targetManager
	 */
	public TargetManager getTargetManager() {
		return targetManager;
	}

	/**
	 * @param targetManager the targetManager to set
	 */
	public void setTargetManager(TargetManager targetManager) {
		this.targetManager = targetManager;
	}


	protected CaseManager	caseManager;
    protected TargetManager targetManager;
    protected AnalysisManager analysisManager;
    protected AbstractController[] managers; // = new AbstractController[]{caseManager, targetManager,analysisManager};

    // protected LinkedHashMap<String, AbstractCommand>  commands;

    /**
     * Constructs and attaches a new controller and a new model.
     */
    public ConResAnalyzer() {
        this(new ConResAnalyzerModel());
        updateCommands();
        setCaseManager( new CaseManager(this));
        setTargetManager (new TargetManager(this));
        setAnalysisManager(new AnalysisManager(this));
        managers = new AbstractController[]{caseManager, targetManager,analysisManager};
    }

    /**
     * Constructs a new controller and attaches it to the unattached model.
     *
     * @param model
     */
    public ConResAnalyzer(ConResAnalyzerModel model) {
        super(model);
    }

    /**
     * Create and populate all commands from scratch.
     */
    @Override
	public void createCommands() {
        putCommand(new Quit(this));
    }
    
    public void forceCommandUpdates() {
    	Iterator<AbstractCommand> commandIterator = getCommands().values().iterator();
    	
    	while(commandIterator.hasNext())
    		commandIterator.next().update();
    }

    /**
     * Method description
     *
     * @return
     */
    @Override
	public LinkedHashMap<String, AbstractCommand> getCommands() {
    	LinkedHashMap<String,AbstractCommand> allCommands = new LinkedHashMap<String,AbstractCommand>();
    	for(AbstractController manager : managers)
    		if (manager!=null) allCommands.putAll(appendCommands(manager));
        return allCommands;
    }

    /**
     * Method description
     *
     * @return
     */
    @Override
    public ConResAnalyzerModel getModel() {

        // TODO Auto-generated method stub
        return (ConResAnalyzerModel)super.getModel();
    }

    /**
     * Method description
     *
     * @param newModel
     *
     * @throws IllegalAccessException
     */
    public void setModel(ConResAnalyzerModel newModel) throws IllegalAccessException {

        // TODO Auto-generated method stub
        super.setModel(newModel);
    }
}
