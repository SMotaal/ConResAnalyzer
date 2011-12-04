/*
 * @(#)TargetManagerModel.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets.model;

import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.framework.targets.model.grid.ConResTarget;
import com.grasppe.conres.framework.targets.model.grid.GridBlock;
import com.grasppe.lure.components.AbstractModel;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class TargetManagerModel extends AbstractModel {

    ConResTarget	activeTarget     = null;
    ConResTarget	backgroundTarget = null;

    /**
     * Constructs a new model object with no predefined controller.
     */
    public TargetManagerModel() {
        super();
    }

    /**
     * Constructs a new model with a predefined controller.
     * @param controller
     */
    public TargetManagerModel(TargetManager controller) {
        super(controller);
    }

    /**
     */
    public void backgroundCurrentTarget() {
        if (getActiveTarget() == null) return;
        setBackgroundTarget(getActiveTarget());
        setActiveTarget(null);
    }

    /**
     */
    public void discardBackgroundTarget() {
        if (getBackgroundTarget() == null) return;
        setBackgroundTarget(null);
    }

    /**
     */
    public void restoreBackgroundTarget() {
        if (getBackgroundTarget() == null) return;
        setActiveTarget(getBackgroundTarget());
        setBackgroundTarget(null);
    }

    /**
     * @return the activeBlock
     */
    public ConResBlock getActiveBlock() {
        return (ConResBlock)getActiveTarget().getActiveBlock();
    }

    /**
     * @return the activeTarget
     */
    public ConResTarget getActiveTarget() {
        return activeTarget;
    }

    /**
     * @return the backgroundTarget
     */
    public ConResTarget getBackgroundTarget() {
        return backgroundTarget;
    }

    /**
     * @param activeBlock the activeBlock to set
     */
    public void setActiveBlock(GridBlock activeBlock) {
        try {

            getActiveTarget().setActiveBlock(activeBlock);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        notifyObservers();
    }

    /**
     * @param activeTarget the activeTarget to set
     */
    public void setActiveTarget(ConResTarget activeTarget) {
        boolean	updated = false;

        try {
            if (activeTarget == null) if (this.activeTarget != null) {
                this.backgroundTarget = this.activeTarget;
                this.activeTarget     = null;
            } else {
                if (this.backgroundTarget != null) this.activeTarget = this.backgroundTarget;
            }
            if ((this.activeTarget != null) && (this.activeTarget != activeTarget)) updated = true;
            this.activeTarget = activeTarget;

            // Set active block to 0 if null
            if ((this.activeTarget.getActiveBlock() == null)
                    && (this.activeTarget.getTargetBlocks().length > 0))
                this.activeTarget.setActiveBlock(this.activeTarget.getTargetBlocks()[0]);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (updated) notifyObservers();

    }

    /**
     * @param backgroundTarget the backgroundTarget to set
     */
    public void setBackgroundTarget(ConResTarget backgroundTarget) {
        this.backgroundTarget = backgroundTarget;
    }
}
