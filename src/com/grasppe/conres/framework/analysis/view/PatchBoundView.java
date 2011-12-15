/*
 * @(#)PatchBoundView.java   11/12/09
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.analysis.view;

import com.grasppe.conres.framework.analysis.model.AnalysisStepperModel;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * Class description
 * 	@version        $Revision: 1.0, 11/12/09
 * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
 */
public class PatchBoundView extends JPanel implements Observer {

    AnalysisStepperModel	model = null;

//  public PatchBoundView() {
//      super();
//      updateSize();
//  }
//
//  public PatchBoundView(LayoutManager arg0) {
//      super(arg0);
//      
//  }
//
//  public PatchBoundView(boolean arg0) {
//      super(arg0);
//  }

    /**
     */
    public PatchBoundView() {
        super();
        updateSize();
    }

    /**
     * Create the panel with a model.
     *  @param model
     */
    public PatchBoundView(AnalysisStepperModel model) {
        this();
        attachModel(model);
    }

    /**
     * 	@param arg0
     * 	@param arg1
     */
    public PatchBoundView(LayoutManager arg0, boolean arg1) {
        super(arg0, arg1);
    }

    /**
     *  @param model
     */
    public void attachModel(AnalysisStepperModel model) {
        this.model = model;
        update();
    }

    /**
     * 	@throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            getModel().detachObserver(this);
        } catch (Exception exception) {
            super.finalize();
        }

        super.finalize();
    }

    /**
     */
    public void update() {
        updateSize();
        updateView();
    }

    /**
     */
    public void updateSize() {}

    /**
     */
    public void updateView() {
    	repaint(100);
    }

    /**
     *  @return
     */
    protected AnalysisStepperModel getModel() {
        if (model != null) return model;

        return model;
    }
}
