package com.grasppe.conres.framework.targets.model;

import java.io.File;

import com.grasppe.conres.framework.targets.CornerSelector;
import com.grasppe.conres.io.model.IConResTargetDefinition;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.lure.components.AbstractModel;

/**
 * Class description
 *
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CornerSelectorModel extends AbstractModel {
	
	public boolean isVisibleView() {
		return visibleView;
	}

	public void setVisibleView(boolean visibleView) {
		this.visibleView = visibleView;
	}

	public boolean isValidSelection() {
		return validSelection;
	}

	public void setValidSelection(boolean validSelection) {
		this.validSelection = validSelection;
	}

	public boolean isFinalizedView() {
		return finalizedView;
	}

	public void setFinalizedView(boolean finalizedView) {
		this.finalizedView = finalizedView;
	}

	public ImageFile getTargetImageFile() {
		return targetImageFile;
	}

	public void setTargetImageFile(ImageFile targetImageFile) {
		this.targetImageFile = targetImageFile;
	}

	public IConResTargetDefinition getTargetDefinitionFile() {
		return targetDefinitionFile;
	}

	public void setTargetDefinitionFile(IConResTargetDefinition targetDefinitionFile) {
		this.targetDefinitionFile = targetDefinitionFile;
	}

	public TargetDimensions getTargetDimensions() {
		return targetDimensions;
	}

	public void setTargetDimensions(TargetDimensions targetDimensions) {
		this.targetDimensions = targetDimensions;
	}

	public TargetDimensions getImageDimensions() {
		return imageDimensions;
	}

	public void setImageDimensions(TargetDimensions imageDimensions) {
		this.imageDimensions = imageDimensions;
	}

	public BlockROI getBlockROI() {
		return blockROI;
	}

	public void setBlockROI(BlockROI blockROI) {
		this.blockROI = blockROI;
	}

	public PatchSetROI getPatchSetROI() {
		return patchSetROI;
	}

	public void setPatchSetROI(PatchSetROI patchSetROI) {
		this.patchSetROI = patchSetROI;
	}

	/* Sample target image */
	protected ImageFile targetImageFile;
	
	/* Target Definition File */
	protected IConResTargetDefinition targetDefinitionFile;
	
	/* Physical dimensions per target definition file*/
	protected TargetDimensions targetDimensions;
	
	/* Pixel dimensions per Sample target image */
	protected TargetDimensions imageDimensions;
	
	protected BlockROI blockROI;
	protected PatchSetROI patchSetROI;
	
	protected boolean visibleView = false;
	protected boolean validSelection = false;	// set by controller.validateSelection()
	protected boolean finalizedView = false;	// after window close

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
}