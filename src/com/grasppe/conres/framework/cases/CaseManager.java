/*
 * @(#)CaseManager.java   11/11/24
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.cases;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.conres.framework.cases.model.CaseModel;
import com.grasppe.conres.framework.cases.operations.CloseCase;
import com.grasppe.conres.framework.cases.operations.NewCase;
import com.grasppe.conres.framework.cases.operations.OpenCase;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.io.model.CaseFolder;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.conres.io.model.TargetDefinitionFile;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractModel;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import java.util.LinkedHashMap;

/**
 *     Class description
 *
 *     @version        $Revision: 0.1, 11/11/08
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CaseManager extends AbstractController implements ActionListener {

    /** Field description */
    public ConResAnalyzer	analyzer;

    /**
     * Constructs a new CaseManager
     */
    public CaseManager() {
        super();
    }

    /**
     * @param model
     */
    public CaseManager(AbstractModel model) {
        super(model);
    }

    /**
     * @param listener
     */
    public CaseManager(ActionListener listener) {
        super(listener);
    }

    /**
     * 	@param analyzer
     */
    public CaseManager(ConResAnalyzer analyzer) {
        this((ActionListener)analyzer);
        setAnalyzer(analyzer);
    }

    /**
     * @param model
     * @param listener
     */
    public CaseManager(AbstractModel model, ActionListener listener) {
        super(model, listener);
    }

    /**
     * Create and populate all commands from scratch.
     */
    @Override
    public void createCommands() {
        commands = new LinkedHashMap<String, AbstractCommand>();
        putCommand(new OpenCase(this, this));
        putCommand(new CloseCase(this));
    }

    /**
     * 	@return
     */
    public AbstractController parentController() {
        return null;
    }

    /**
     * @return the analyzer
     */
    public ConResAnalyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * Return the controller's correctly-cast model
     *
     * @return
     */
    @Override
    public CaseManagerModel getModel() {
        return (CaseManagerModel)super.getModel();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.AbstractController#getNewModel()
     */

    /**
     * Method description
     *
     * @return
     */
    @Override
    protected CaseManagerModel getNewModel() {
        return new CaseManagerModel();
    }

    /**
     * @param analyzer the analyzer to set
     */
    public void setAnalyzer(ConResAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * Sets controller's model to a CaseManagerModel and not any AbstractModel.
     *
     * @param newModel
     *
     * @throws IllegalAccessException
     */
    public void setModel(CaseManagerModel newModel) throws IllegalAccessException {
        super.setModel(newModel);
    }
    
    public void loadCase(CaseModel newCase) throws FileNotFoundException {
    	try {

	        newCase.caseFolder = new CaseFolder(newCase.path);
	        
    		CaseFolder caseFolder = newCase.caseFolder;
    		
	        newCase.title      = caseFolder.getName();
    		
    		newCase.targetDefinitionFile = caseFolder.getTargetDefinitionFile();
			getTargetManager().loadTargetDefinitionFile(newCase.targetDefinitionFile);
			
			ImageFile[] imageFiles = caseFolder.getImageFiles();
			
	    	newCase.filesLoaded = true;
	        
			return;
    	} catch (FileNotFoundException e) {
    		throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public TargetManager getTargetManager(){
    	return getAnalyzer().getTargetManager();
    }
    
    
}
