/*
 * @(#)CornerSelector.java   11/11/26
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.targets;

import com.grasppe.conres.framework.imagej.CornerSelectorView;
import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
import com.grasppe.conres.framework.targets.operations.SelectCornersOperation;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.framework.GrasppeKit.ExitCodes;

/**
 * Class description
 *
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CornerSelector extends AbstractController {

    /** Field description */
    public CornerSelectorView	selectorView = null;
    SelectCornersOperation		operation;

    /**
     * Constructs and attaches a new controller and a new model.
     */
    public CornerSelector() {
        this(new CornerSelectorModel());
    }

    /**
     * Constructs a new controller and attaches it to the unattached model.
     * @param model
     */
    public CornerSelector(CornerSelectorModel model) {
        super(model);
    }

    /**
     *  @return
     * @throws Throwable 
     */
    public ExitCodes showSelectorWindow() throws Throwable {
        selectorView = new CornerSelectorView(this);
        attachView(selectorView);
        operation = new SelectCornersOperation(this);
        operation.execute();
        selectorView.terminate();
        return operation.getExitCode();
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
