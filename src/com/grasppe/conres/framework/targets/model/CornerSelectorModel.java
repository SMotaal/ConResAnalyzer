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

import ij.ImagePlus;
import ij.gui.PointRoi;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CornerSelectorModel extends AbstractModel {

    /**
	 * @return the imagePlus
	 */
	public ImagePlus getImagePlus() {
		return imagePlus;
	}

	/**
	 * @param imagePlus the imagePlus to set
	 */
	public void setImagePlus(ImagePlus imagePlus) {
		this.imagePlus = imagePlus;
	}

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
    protected PointRoi			overlayROI;
    protected int				magnifyPatchIndex = -1;
    protected boolean			visibleView       = false;
    protected boolean			validSelection    = false;		// set by controller.validateSelection()
    protected boolean			finalizedView     = false;		// after window close
    protected ImagePlus imagePlus = null;

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
     * @return the magnifyPatchIndex
     */
    public int getMagnifyPatchIndex() {
        return magnifyPatchIndex;
    }

    /**
     * @return the overlayROI
     */
    public PointRoi getOverlayROI() {
        return overlayROI;
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
    
    public void setBlockROI(PointRoi blockROI) {
    	if (this.blockROI == blockROI) return;
    	if (blockROI==null) this.blockROI = null;
    	else if (blockROI instanceof BlockROI)
    		this.blockROI = (BlockROI) blockROI;
    	else
    		this.blockROI = new BlockROI(blockROI.getPolygon());
    	
    	notifyObservers();
    }

    /**
     *  @param finalizedView
     */
    public void setFinalizedView(boolean finalizedView) {
    	if (this.finalizedView == finalizedView) return;
        this.finalizedView = finalizedView;
        notifyObservers();
    }

    /**
     *  @param imageDimensions
     */
    public void setImageDimensions(TargetDimensions imageDimensions) {
    	if (this.imageDimensions == imageDimensions) return;
        this.imageDimensions = imageDimensions;
        notifyObservers();
    }

    /**
     * @param magnifyPatchIndex the magnifyPatchIndex to set
     */
    public void setMagnifyPatchIndex(int magnifyPatchIndex) {
    	if (this.magnifyPatchIndex == magnifyPatchIndex) return;
        this.magnifyPatchIndex = magnifyPatchIndex;
        notifyObservers();
    }

    /**
     * @param overlayROI the overlayROI to set
     */
    public void setOverlayROI(PointRoi overlayROI) {
    	if (this.overlayROI == overlayROI) return;
    	if (overlayROI==null) this.overlayROI = null;
    	else if (overlayROI.getClass().equals(PointRoi.class))
    		this.overlayROI = overlayROI;
    	else
    		this.overlayROI = new BlockROI(overlayROI.getPolygon());
    	
    	notifyObservers();
    }
    
    public void setPatchSetROI(PointRoi patchSetROI) {
    	if (this.patchSetROI == patchSetROI) return;
    	if (patchSetROI==null) this.patchSetROI = null;
    	else if (patchSetROI instanceof PatchSetROI)
    		this.patchSetROI = (PatchSetROI) patchSetROI;
    	else    	
    		this.patchSetROI = new PatchSetROI(patchSetROI.getPolygon());
    	setValidSelection(this.patchSetROI!= null);
    	
    	notifyObservers();
    }

    /**
     *  @param targetDefinitionFile
     */
    public void setTargetDefinitionFile(IConResTargetDefinition targetDefinitionFile) {
    	if (this.targetDefinitionFile == targetDefinitionFile) return;
        this.targetDefinitionFile = targetDefinitionFile;
        notifyObservers();
    }

    /**
     *  @param targetDimensions
     */
    public void setTargetDimensions(TargetDimensions targetDimensions) {
    	if (this.targetDimensions == targetDimensions) return;
        this.targetDimensions = targetDimensions;
        notifyObservers();
    }

    /**
     *  @param targetImageFile
     */
    public void setTargetImageFile(ImageFile targetImageFile) {
    	if (this.targetImageFile == targetImageFile) return;
        this.targetImageFile = targetImageFile;
        notifyObservers();
    }

    /**
     *  @param validSelection
     */
    public void setValidSelection(boolean validSelection) {
    	if (this.validSelection == validSelection) return;
        this.validSelection = validSelection;
        notifyObservers();
    }

    /**
     *  @param visibleView
     */
    public void setVisibleView(boolean visibleView) {
    	if (this.visibleView == visibleView) return;
        this.visibleView = visibleView;
        notifyObservers();
    }
}
