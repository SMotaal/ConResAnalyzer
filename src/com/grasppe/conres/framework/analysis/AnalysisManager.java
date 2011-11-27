/*
 * @(#)AnalysisManager.java   11/11/26
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.analysis;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.framework.analysis.model.AnalysisManagerModel;
import com.grasppe.conres.framework.analysis.operations.AnalyzeBlock;
import com.grasppe.conres.framework.cases.CaseManager;
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
public class AnalysisManager extends AbstractController {

    /** Field description */
    public ConResAnalyzer	analyzer;

    /**
     * Constructs and attaches a new controller and a new model.
     */
    public AnalysisManager() {
        this(new AnalysisManagerModel());
    }

    /**
     * @param listener
     */
    public AnalysisManager(ActionListener listener) {
        super(listener);
    }

    /**
     * Constructs a new controller and attaches it to the unattached model.
     * @param model
     */
    public AnalysisManager(AnalysisManagerModel model) {
        super(model);
    }

    /**
     *  @param analyzer
     */
    public AnalysisManager(ConResAnalyzer analyzer) {
        this((ActionListener)analyzer);
        setAnalyzer(analyzer);
    }

    /**
     * @param model
     * @param listener
     */
    public AnalysisManager(AbstractModel model, ActionListener listener) {
        super(model, listener);
    }

//  /**
//   *  @param analysisDefinition
//   *  @return
//   */
//  public static ConResTarget buildTargetModel(IConResTargetDefinition targetDefinition) {
//      return new ConResTarget(targetDefinition.getBlockToneValues(),
//                              targetDefinition.getMeasurements().getYValues(),
//                              targetDefinition.getMeasurements().getXValues());
//  }

    /**
     * Create and populate all commands from scratch.
     */
    @Override
    public void createCommands() {
        commands = new LinkedHashMap<String, AbstractCommand>();
        putCommand(new AnalyzeBlock(this, this));
    }

//
//  /**
//   *  @param targetDefinitionFile
//   *  @throws Exception
//   */
//  public void loadTargetDefinitionFile(TargetDefinitionFile targetDefinitionFile)
//          throws Exception {
//
//      // TODO: Create reader and read target from Case Manager current case
//      // TargetDefinitionFile file = targetDefinitionFile.getTargetDefinitionFile();
//      TargetDefinitionReader    reader = new TargetDefinitionReader(targetDefinitionFile);
//
//      getModel().setActiveTarget(buildTargetModel(targetDefinitionFile));
//  }

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
     * Method description
     *
     * @return
     */
    @Override
    protected AnalysisManagerModel getNewModel() {
        return new AnalysisManagerModel();
    }

    /**
     * @param analyzer the analyzer to set
     */
    public void setAnalyzer(ConResAnalyzer analyzer) {
        this.analyzer = analyzer;
    }
}
