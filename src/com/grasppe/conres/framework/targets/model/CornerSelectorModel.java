/*
 * @(#)CornerSelectorModel.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets.model;

import com.grasppe.conres.framework.targets.CornerSelector;
import com.grasppe.conres.framework.targets.model.roi.BlockROI;
import com.grasppe.conres.framework.targets.model.roi.PatchSetROI;
import com.grasppe.conres.io.model.IConResTargetDefinition;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.lure.components.AbstractModel;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CornerSelectorModel extends AbstractModel {

    /* Sample target image */
    protected ImageFile	targetImageFile;

    /* Target Definition File */
    protected IConResTargetDefinition	targetDefinitionFile;

    /* Physical dimensions per target definition file */
    protected TargetDimensions	targetDimensions;

    /* Pixel dimensions per Sample target image */
    protected TargetDimensions	imageDimensions;
    protected BlockROI			blockROI;
    protected PatchSetROI		patchSetROI;
    protected boolean			visibleView    = false;
    protected boolean			validSelection = false;		// set by controller.validateSelection()
    protected boolean			finalizedView  = false;		// after window close

    /**
     * Constructs a new model object with no predefined controller.
     */
    public CornerSelectorModel() {
        super();
    }

    /**
     * Constructs a new model with a predefined controller.
     * @param controller
     */
    public CornerSelectorModel(CornerSelector controller) {
        super(controller);
    }

    /**
     *  @return
     */
    public BlockROI getBlockROI() {
        return blockROI;
    }

    /**
     *  @return
     */
    public TargetDimensions getImageDimensions() {
        return imageDimensions;
    }

    /**
     *  @return
     */
    public PatchSetROI getPatchSetROI() {
        return patchSetROI;
    }

    /**
     *  @return
     */
    public IConResTargetDefinition getTargetDefinitionFile() {
        return targetDefinitionFile;
    }

    /**
     *  @return
     */
    public TargetDimensions getTargetDimensions() {
        return targetDimensions;
    }

    /**
     *  @return
     */
    public ImageFile getTargetImageFile() {
        return targetImageFile;
    }

    /**
     *  @return
     */
    public boolean isFinalizedView() {
        return finalizedView;
    }

    /**
     *  @return
     */
    public boolean isValidSelection() {
        return validSelection;
    }

    /**
     *  @return
     */
    public boolean isVisibleView() {
        return visibleView;
    }

    /**
     *  @param blockROI
     */
    public void setBlockROI(BlockROI blockROI) {
        this.blockROI = blockROI;
    }

    /**
     *  @param finalizedView
     */
    public void setFinalizedView(boolean finalizedView) {
        this.finalizedView = finalizedView;
    }

    /**
     *  @param imageDimensions
     */
    public void setImageDimensions(TargetDimensions imageDimensions) {
        this.imageDimensions = imageDimensions;
    }

    /**
     *  @param patchSetROI
     */
    public void setPatchSetROI(PatchSetROI patchSetROI) {
        this.patchSetROI = patchSetROI;
    }

    /**
     *  @param targetDefinitionFile
     */
    public void setTargetDefinitionFile(IConResTargetDefinition targetDefinitionFile) {
        this.targetDefinitionFile = targetDefinitionFile;
    }

    /**
     *  @param targetDimensions
     */
    public void setTargetDimensions(TargetDimensions targetDimensions) {
        this.targetDimensions = targetDimensions;
    }

    /**
     *  @param targetImageFile
     */
    public void setTargetImageFile(ImageFile targetImageFile) {
        this.targetImageFile = targetImageFile;
    }

    /**
     *  @param validSelection
     */
    public void setValidSelection(boolean validSelection) {
        this.validSelection = validSelection;
    }

    /**
     *  @param visibleView
     */
    public void setVisibleView(boolean visibleView) {
        this.visibleView = visibleView;
    }
}
