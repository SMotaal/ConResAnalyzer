/*
 * @(#)TargetManagerModel.java   11/11/26
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.targets.model;

import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.lure.components.AbstractModel;

/**
 * Class description
 *
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class TargetManagerModel extends AbstractModel {

    ConResTarget	activeTarget;

    /**
     * Constructs a new model object with no predefined controller.
     */
    public TargetManagerModel() {
        super();
    }

    /**
     * Constructs a new model with a predefined controller.
     *
     * @param controller
     */
    public TargetManagerModel(TargetManager controller) {
        super(controller);
    }

    /**
     * @return the activeTarget
     */
    public ConResTarget getActiveTarget() {
        return activeTarget;
    }

    /**
     * @param activeTarget the activeTarget to set
     */
    public void setActiveTarget(ConResTarget activeTarget) {
        this.activeTarget = activeTarget;
    }
}
