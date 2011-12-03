/*
 * @(#)TargetManager.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseModel;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.framework.targets.model.grid.ConResTarget;
import com.grasppe.conres.framework.targets.operations.MarkBlock;
import com.grasppe.conres.framework.targets.operations.SelectBlock;
import com.grasppe.conres.framework.targets.view.TargetManagerView;
import com.grasppe.conres.io.TargetDefinitionReader;
import com.grasppe.conres.io.model.IConResTargetDefinition;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.conres.io.model.TargetDefinitionFile;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.IAuxiliaryCaseManager;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;

import java.io.IOException;

import java.util.LinkedHashMap;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class TargetManager extends AbstractController implements IAuxiliaryCaseManager {

    /** Field description */
    public ConResAnalyzer	analyzer;

    /** Field description */
    public TargetManagerModel	backgroundModel = null;

    /** Field description */
    public CornerSelector	cornerSelector = null;

    /**
     * @param listener
     */
    private TargetManager(ActionListener listener) {
        super(listener);
    }

    /**
     *  @param analyzer
     */
    public TargetManager(ConResAnalyzer analyzer) {
        this((ActionListener)analyzer);
        attachView(new TargetManagerView(this));
        setAnalyzer(analyzer);
        analyzer.getCaseManager().getModel().attachObserver(this);
    }

    /**
     *  @throws IllegalAccessException
     */
    public void backgroundCurrentCase() throws IllegalAccessException {
        getModel().backgroundCurrentTarget();
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
     * Create and populate all commands from scratch.
     */
    @Override
    public void createCommands() {
        commands = new LinkedHashMap<String, AbstractCommand>();
        putCommand(new SelectBlock(this, this));
        putCommand(new MarkBlock(this, this));
    }

    /**
     */
    public void discardBackgroundCase() {
        getModel().discardBackgroundTarget();
    }

    /**
     *  @param targetDefinitionFile
     *  @throws IOException
     */
    public void loadTargetDefinitionFile(TargetDefinitionFile targetDefinitionFile)
            throws IOException {

        // TODO: Create reader and read target from Case Manager current case
        // TargetDefinitionFile file = targetDefinitionFile.getTargetDefinitionFile();
        TargetDefinitionReader	reader = new TargetDefinitionReader(targetDefinitionFile);

    }

    /**
     *  @throws IllegalAccessException
     */
    public void restoreBackgroundCase() throws IllegalAccessException {
        getModel().restoreBackgroundTarget();
    }

    /**
     *  @return
     */
    public CaseModel getActiveCase() {
        return getCaseManager().getModel().getCurrentCase();
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
    public ImageFile getBlockImage() {
        return getBlockImage(getModel().getActiveBlock());
    }

    /**
     *  @param block
     *  @return
     */
    public ImageFile getBlockImage(ConResBlock block) {
        return getBlockImage(new Double(block.getZValue().value).intValue());
    }

    /**
     *  @param toneValue
     *  @return
     */
    public ImageFile getBlockImage(int toneValue) {
    	if (getActiveCase()==null) return null;
        return getActiveCase().caseFolder.getImageFile(toneValue);
    }

    /**
     *  @return
     */
    public CaseManager getCaseManager() {
        return getAnalyzer().getCaseManager();
    }

    /**
     *  @return
     */
    public CornerSelector getCornerSelector() {
        if (cornerSelector == null) cornerSelector = new CornerSelector(this);

        return cornerSelector;
    }

    /**
     * Returns a correctly-cast model.
     * @return
     */
    public TargetManagerModel getModel() {
        return (TargetManagerModel)model;
    }

    /**
     * @return
     */
    @Override
    protected TargetManagerModel getNewModel() {
        GrasppeKit.debugText(getClass().getSimpleName(), "Getting new Model", 2);

        return new TargetManagerModel();
    }

    /**
     * @param analyzer the analyzer to set
     */
    public void setAnalyzer(ConResAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     *  @param targetDefinitionFile
     */
    public void setTargetDefinitionFile(TargetDefinitionFile targetDefinitionFile) {
        getModel().setActiveTarget(buildTargetModel(targetDefinitionFile));
    }

}
