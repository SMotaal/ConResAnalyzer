/*
 * @(#)TargetManager.java   11/11/26
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.targets;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.conres.framework.cases.operations.CloseCase;
import com.grasppe.conres.framework.cases.operations.OpenCase;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.framework.targets.model.grid.ConResTarget;
import com.grasppe.conres.framework.targets.operations.MarkBlock;
import com.grasppe.conres.framework.targets.operations.SelectBlock;
import com.grasppe.conres.io.TargetDefinitionReader;
import com.grasppe.conres.io.model.CaseFolder;
import com.grasppe.conres.io.model.IConResTargetDefinition;
import com.grasppe.conres.io.model.TargetDefinitionFile;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractModel;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

/**
 * Class description
 *
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class TargetManager extends AbstractController {

    /** Field description */
    public ConResAnalyzer	analyzer;

    /**
     * Constructs and attaches a new controller and a new model.
     */
    public TargetManager() {
        this(new TargetManagerModel());
    }

    /**
     * @param listener
     */
    public TargetManager(ActionListener listener) {
        super(listener);
    }

    /**
     * 	@param analyzer
     */
    public TargetManager(ConResAnalyzer analyzer) {
        this((ActionListener)analyzer);
        setAnalyzer(analyzer);
    }

    /**
     * Constructs a new controller and attaches it to the unattached model.
     * @param model
     */
    public TargetManager(TargetManagerModel model) {
        super(model);
    }

    /**
     * @param model
     * @param listener
     */
    public TargetManager(AbstractModel model, ActionListener listener) {
        super(model, listener);
    }

    /**
     *  @param targetDefinition
     *  @return
     */
    public static ConResTarget buildTargetModel(IConResTargetDefinition targetDefinition) {
        return new ConResTarget(targetDefinition.getBlockToneValues(),
                                targetDefinition.getMeasurements().getYValues(),
                                targetDefinition.getMeasurements().getXValues());
    }

    /**
     * 	@param targetDefinitionFile
     * 	@throws Exception
     */
    public void loadTargetDefinitionFile(TargetDefinitionFile targetDefinitionFile) throws Exception {

        // TODO: Create reader and read target from Case Manager current case
    	//TargetDefinitionFile file = targetDefinitionFile.getTargetDefinitionFile();
        TargetDefinitionReader	reader = new TargetDefinitionReader(targetDefinitionFile);

        getModel().setActiveTarget(buildTargetModel(targetDefinitionFile));
    }

    /**
     * @return the analyzer
     */
    public ConResAnalyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * Returns a correctly-cast model.
     * @return
     */
    public TargetManagerModel getModel() {
        return (TargetManagerModel)model;
    }
    
    /**
     * Method description
     *
     * @return
     */
    @Override
    protected TargetManagerModel getNewModel() {
        return new TargetManagerModel();
    }    

    /**
     * @param analyzer the analyzer to set
     */
    public void setAnalyzer(ConResAnalyzer analyzer) {
        this.analyzer = analyzer;
    }
    
    public CaseManager getCaseManager(){
    	return getAnalyzer().getCaseManager();
    }
    
    /**
     * Create and populate all commands from scratch.
     */
    @Override
    public void createCommands() {
        commands = new LinkedHashMap<String, AbstractCommand>();
        putCommand(new SelectBlock(this, this));
        putCommand(new MarkBlock(this, this));
    }    
}
