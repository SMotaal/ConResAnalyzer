/*
 * @(#)AnalysisManagerModel.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis.model;

import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.framework.targets.model.grid.ConResTarget;
import com.grasppe.lure.components.AbstractModel;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AnalysisManagerModel extends AbstractModel {

    ConResTarget	activeTarget = null;
    ConResBlock		activeBlock  = null;

    /**
     * Constructs a new model object with no predefined controller.
     */
    public AnalysisManagerModel() {
        super();
    }

    /**
     * Constructs a new model with a predefined controller.
     * @param controller
     */
    public AnalysisManagerModel(AnalysisManager controller) {
        super(controller);
    }

    /**
     * @return the activeBlock
     */
    public ConResBlock getActiveBlock() {
        return activeBlock;
    }

    /**
     * @return the activeTarget
     */
    public ConResTarget getActiveTarget() {
        return activeTarget;
    }

    /**
     * @param activeBlock the activeBlock to set
     */
    public void setActiveBlock(ConResBlock activeBlock) {
        this.activeBlock = activeBlock;
        notifyObservers();
    }

    /**
     * @param activeTarget the activeTarget to set
     */
    public void setActiveTarget(ConResTarget activeTarget) {
        this.activeTarget = activeTarget;
        notifyObservers();
    }
}
