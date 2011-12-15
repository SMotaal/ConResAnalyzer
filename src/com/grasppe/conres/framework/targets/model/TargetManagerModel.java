/*
 * @(#)TargetManagerModel.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets.model;

import ij.ImagePlus;

import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.framework.targets.model.grid.ConResTarget;
import com.grasppe.conres.framework.targets.model.grid.GridBlock;
import com.grasppe.lure.components.AbstractModel;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class TargetManagerModel extends AbstractModel {

    /**
	 * @return the activeImagePlus
	 */
	public ImagePlus getActiveImagePlus() {
		return activeImagePlus;
	}

	/**
	 * @param activeImagePlus the activeImagePlus to set
	 */
	public void setActiveImagePlus(ImagePlus activeImagePlus) {
		this.activeImagePlus = activeImagePlus;
	}

	ConResTarget	activeTarget     = null;
    ConResTarget	backgroundTarget = null;
    
    ImagePlus activeImagePlus = null;

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
//        setActiveBlock(null);
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
        if (getActiveTarget() == null) return null;

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

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractModel#getController()
     */

    /**
     *  @return
     */
    @Override
    public TargetManager getController() {

        // TODO Auto-generated method stub
        return (TargetManager)super.getController();
    }

    /**
     * @param activeBlock the activeBlock to set
     */
    public void setActiveBlock(GridBlock activeBlock) {
        try {
        	if (activeBlock==null) {
        		if (getActiveTarget()!=null) getActiveTarget().setActiveBlock(null);
        	}
        	getActiveTarget().setActiveBlock(activeBlock);        		
//        	if (activeBlock!=null && getActiveTarget()!=null) {
//        		if (activeBlock != getActiveTarget())
//        	}
//            if (activeBlock!=null) getController().loadImage();
        } catch (Exception exception) {
        	GrasppeKit.debugError("Updating Active Block", exception, 2);
        }

        notifyObservers();
    }

    /**
     * @param activeTarget the activeTarget to set
     */
    public void setActiveTarget(ConResTarget activeTarget) {
        boolean	updated = false;

        try {
            if (activeTarget == null) {
                if (this.activeTarget != null) {
                    this.backgroundTarget = this.activeTarget;
                    this.activeTarget     = null;
                } else {
                    if (this.backgroundTarget != null) this.activeTarget = this.backgroundTarget;
                }
                
                updated = true;
            } else {

                if (activeTarget.getActiveBlock() == null && activeTarget.getTargetBlocks() != null && activeTarget.getTargetBlocks().length > 0)
                        activeTarget.setActiveBlock(activeTarget.getTargetBlocks()[0]);

                if (this.activeTarget != activeTarget) {
                	updated = true;
                	this.activeTarget = activeTarget;
                }
            }
        } catch (Exception exception) {
        	GrasppeKit.debugError("Updating Active Target", exception, 2);
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
