/*
 * @(#)CornerSelector.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets;

import com.grasppe.conres.framework.imagej.CornerSelectorView;
import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
import com.grasppe.conres.framework.targets.model.TargetDimensions;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.lure.components.AbstractController;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CornerSelector extends AbstractController {

    /** Field description */
    public CornerSelectorView	selectorView = null;

    /** Field description */
    public TargetManager	targetManager = null;

    /**
     * Constructs and attaches a new controller and a new model.
     *  @param targetManager
     */
    public CornerSelector(TargetManager targetManager) {
        this(targetManager, new CornerSelectorModel());
        selectorView = new CornerSelectorView(this);
        getManagerModel().attachObserver(targetManager);
        attachView(selectorView);
    }

    /**
     * Constructs a new controller and attaches it to the unattached model.
     *  @param targetManager
     * @param model
     */
    private CornerSelector(TargetManager targetManager, CornerSelectorModel model) {
        super(model);
        this.targetManager = targetManager;
        
        TargetDimensions targetDimensions = targetManager.getModel().getActiveTarget().getDimensions();
        model.setTargetDimensions(targetDimensions);
//        model.getTargetDimensions(targetManager.)
        getManagerModel().attachObserver(this);
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractController#update()
     */

    /**
     */
    @Override
    public void update() {
        getModel().setTargetImageFile(getBlockImage());
        super.update();
    }

    /**
     *  @param model
     */
    protected static void validateSelection(CornerSelectorModel model) {
        try {
            boolean	validTDF       = model.getTargetImageFile().validate();
            boolean	validImage     = model.getTargetImageFile().validate();
            boolean	validCornerROI = model.getBlockROI().validate();
            boolean	validCenterROI = model.getPatchSetROI().validate();
            boolean	validSelection = validTDF && validImage && validCornerROI && validCenterROI;

            model.setValidSelection(validSelection);
        } catch (Exception exception) {
            model.setValidSelection(false);
        }

    }

    /**
     *  @return
     */
    public ImageFile getBlockImage() {
        return targetManager.getBlockImage();
    }

    /**
     *  @return
     */
    public TargetManagerModel getManagerModel() {
        return targetManager.getModel();
    }

    /**
     * Returns a correctly-cast model.
     * @return
     */
    public CornerSelectorModel getModel() {
        return (CornerSelectorModel)model;
    }

    /**
     * @return the selectorView
     */
    public CornerSelectorView getSelectorView() {
        return selectorView;
    }

    /**
     *  @return
     */
    public boolean isSelectionValid() {
        validateSelection(getModel());

        return getModel().isValidSelection();
    }

    /**
     * @param selectorView the selectorView to set
     */
    public void setSelectorView(CornerSelectorView selectorView) {
        this.selectorView = selectorView;
    }
}
