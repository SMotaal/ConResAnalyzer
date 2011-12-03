/*
 * @(#)CaseManager.java   11/11/24
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.cases;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.conres.framework.cases.model.CaseModel;
import com.grasppe.conres.framework.cases.operations.CloseCase;
import com.grasppe.conres.framework.cases.operations.OpenCase;
import com.grasppe.conres.framework.cases.view.CaseManagerView;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.io.model.CaseFolder;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractModel;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;

import java.io.IOException;

import java.util.LinkedHashMap;

/**
 *     Class description
 *     @version        $Revision: 0.1, 11/11/08
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CaseManager extends AbstractController implements ActionListener {

    /** Field description */
    public ConResAnalyzer	analyzer;

    /**
     * @param listener
     */
    private CaseManager(ActionListener listener) {
        super(listener);
    }

    /**
     *  @param analyzer
     */
    public CaseManager(ConResAnalyzer analyzer) {
        this((ActionListener)analyzer);
        attachView(new CaseManagerView(this));
        setAnalyzer(analyzer);
    }

    /**
     * @param model
     * @param listener
     */
    public CaseManager(AbstractModel model, ActionListener listener) {
        super(model, listener);

//      model.attachController(this);
    }

    /**
     */
    public void backgroundCurrentCase() {
        getAnalyzer().backgroundCurrentCase();
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
     */
    public void discardBackgroundCase() {
        getAnalyzer().discardBackgroundCase();

    }

    /**
     *  @param newCase
     *  @throws IOException
     */
    public void loadCase(CaseModel newCase) throws IOException {

        CaseFolder	caseFolder = new CaseFolder(newCase.path);

        try {
            getTargetManager().loadTargetDefinitionFile(caseFolder.getTargetDefinitionFile());

            ImageFile[]	imageFiles = caseFolder.getImageFiles();
        } catch (IOException exception) {
            newCase = null;
            throw exception;
        }

        try {
            newCase.caseFolder           = caseFolder;

            newCase.title                = caseFolder.getName();

            newCase.targetDefinitionFile = caseFolder.getTargetDefinitionFile();

            newCase.imageFiles           = caseFolder.getImageFiles();

            getTargetManager().setTargetDefinitionFile(newCase.targetDefinitionFile);

            newCase.filesLoaded = true;

            return;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  @return
     */
    public AbstractController parentController() {
        return null;
    }

    /**
     */
    public void restoreBackgroundCase() {
        getAnalyzer().restoreBackgroundCase();
    }

    /**
     * @return the analyzer
     */
    public ConResAnalyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * Return the controller's correctly-cast model
     * @return
     */
    @Override
    public CaseManagerModel getModel() {
        return (CaseManagerModel)super.getModel();
    }

    /**
     *  @return
     */
    @Override
    protected AbstractModel getNewModel() {
        GrasppeKit.debugText(getClass().getSimpleName(), "Getting new Model", 2);

        return new CaseManagerModel(this);
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.AbstractController#getNewModel()
     */

    /**
     * @return
     */

//  @Override
//  protected CaseManagerModel getNewModel() {
//      return new CaseManagerModel();
//  }

    /**
     *  @return
     */
    public TargetManager getTargetManager() {
        return getAnalyzer().getTargetManager();
    }

    /**
     * @param analyzer the analyzer to set
     */
    public void setAnalyzer(ConResAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * Sets controller's model to a CaseManagerModel and not any AbstractModel.
     * @param newModel
     * @throws IllegalAccessException
     */
    public void setModel(CaseManagerModel newModel) throws IllegalAccessException {
        super.setModel(newModel);
    }
}
