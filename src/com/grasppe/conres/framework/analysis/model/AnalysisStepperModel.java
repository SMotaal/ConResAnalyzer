/*
 * @(#)AnalysisManagerModel.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis.model;

import com.grasppe.conres.framework.analysis.AnalysisStepper;
import com.grasppe.conres.framework.analysis.stepping.BlockState;
import com.grasppe.conres.framework.targets.model.PatchDimensions;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.framework.targets.model.grid.ConResPatch;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.lure.components.AbstractModel;

import ij.ImagePlus;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Image;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AnalysisStepperModel extends AbstractModel {

    /**
	 * @return the modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	/**
	 * @return the cursorColor
	 */
	public int[] getCursorColor() {
		return cursorColor;
	}

	/**
	 * @param cursorColor the cursorColor to set
	 */
	public void setCursorColor(int[] cursorColor) {
		this.cursorColor = cursorColor;
	}

	/**
	 * @return the voidPatchColor
	 */
	public int[] getVoidPatchColor() {
		return voidPatchColor;
	}

	/**
	 * @param voidPatchColor the voidPatchColor to set
	 */
	public void setVoidPatchColor(int[] voidPatchColor) {
		this.voidPatchColor = voidPatchColor;
	}

	/**
	 * @return the assumeFailPatchColor
	 */
	public int[] getAssumeFailPatchColor() {
		return assumeFailPatchColor;
	}

	/**
	 * @param assumeFailPatchColor the assumeFailPatchColor to set
	 */
	public void setAssumeFailPatchColor(int[] assumeFailPatchColor) {
		this.assumeFailPatchColor = assumeFailPatchColor;
	}

	/**
	 * @return the failPatchColor
	 */
	public int[] getFailPatchColor() {
		return failPatchColor;
	}

	/**
	 * @param failPatchColor the failPatchColor to set
	 */
	public void setFailPatchColor(int[] failPatchColor) {
		this.failPatchColor = failPatchColor;
	}

	/**
	 * @return the marginalPatchColor
	 */
	public int[] getMarginalPatchColor() {
		return marginalPatchColor;
	}

	/**
	 * @param marginalPatchColor the marginalPatchColor to set
	 */
	public void setMarginalPatchColor(int[] marginalPatchColor) {
		this.marginalPatchColor = marginalPatchColor;
	}

	/**
	 * @return the passPatchColor
	 */
	public int[] getPassPatchColor() {
		return passPatchColor;
	}

	/**
	 * @param passPatchColor the passPatchColor to set
	 */
	public void setPassPatchColor(int[] passPatchColor) {
		this.passPatchColor = passPatchColor;
	}

	/**
	 * @return the assumePassPatchColor
	 */
	public int[] getAssumePassPatchColor() {
		return assumePassPatchColor;
	}

	/**
	 * @param assumePassPatchColor the assumePassPatchColor to set
	 */
	public void setAssumePassPatchColor(int[] assumePassPatchColor) {
		this.assumePassPatchColor = assumePassPatchColor;
	}

	/**
	 * @return the clearPatchColor
	 */
	public int[] getClearPatchColor() {
		return clearPatchColor;
	}

	/**
	 * @param clearPatchColor the clearPatchColor to set
	 */
	public void setClearPatchColor(int[] clearPatchColor) {
		this.clearPatchColor = clearPatchColor;
	}

	/** Field description */
    private BlockState	blockState = null;		// new BlockState(10, 10, 0, 0, BlockState.fudgeMap0());

    /** Field description */
    private List<int[]>		history          = new ArrayList<int[]>();		// @SuppressWarnings("rawtypes")
    private ConResBlock		activeBlock      = null;
    private ConResPatch		activePatch      = null;
    private BufferedImage
		image                                = null,
		patchImage                           = null;
    private boolean			scratchEnabled   = false;
    private int				patchPreviewSize = 550;
    private PatchDimensions	patchDimensions  = null;
    private double			imageDPI,
							displayDPI       = 128.0;
    private double			resolutionRatio,
							scaleRatio       = 2.5,
							windowRatio      = 6.0;
    private ImageFile		blockImage       = null;
    private ImagePlus		blockImagePlus   = null;
    
    private int[] voidPatchColor = null;
    private int[] assumeFailPatchColor = null;
    private int[] failPatchColor = null;
    private int[] marginalPatchColor = null;
    private int[] passPatchColor = null;
    private int[] assumePassPatchColor = null;
    private int[] clearPatchColor = null;
    private int[] cursorColor = null;
    
    private boolean modified = false;

    /**
     * Constructs a new model object with no predefined controller.
     */
    public AnalysisStepperModel() {
        super();
    }

    /**
     * Constructs a new model with a predefined controller.
     * @param controller
     */
    public AnalysisStepperModel(AnalysisStepper controller) {
        super(controller);

//      this.patchPreviewSize =getController().getTargetManager().patchPreviewSize;
    }

    /**
     * @return the activeBlock
     */
    public ConResBlock getActiveBlock() {
        return activeBlock;
    }

    /**
     * @return the activePatch
     */
    public ConResPatch getActivePatch() {
        return activePatch;
    }

    /**
     * @return the blockImage
     */
    public ImageFile getBlockImage() {
        return blockImage;
    }

    /**
     * @return the blockImagePlus
     */
    public ImagePlus getBlockImagePlus() {
        return blockImagePlus;
    }

    /**
     * @return the image
     */
    public BufferedImage getBlockMapImage() {
        return image;
    }

    /**
     * @return the blockState
     */
    public BlockState getBlockState() {
        return blockState;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractModel#getController()
     */

    /**
     *  @return
     */
    @Override
    public AnalysisStepper getController() {
        return (AnalysisStepper)super.getController();
    }

    /**
     * @return the displayDPI
     */
    public double getDisplayDPI() {
        return displayDPI;
    }

    /**
     * @return the history
     */
    public List<int[]> getHistory() {
        return history;
    }

    /**
     * @return the imageDPI
     */
    public double getImageDPI() {
        return imageDPI;
    }

    /**
     * @return the patchDimensions
     */
    public PatchDimensions getPatchDimensions() {
        return patchDimensions;
    }

    /**
     * @return the patchImage
     */
    public BufferedImage getPatchImage() {
        return patchImage;
    }

    /**
     * @return the patchPreviewSize
     */
    public int getPatchPreviewSize() {

//      patchPreviewSize = getController().getTargetManager().patchPreviewSize;

        return patchPreviewSize;
    }

    /**
     * @return the resolutionRatio
     */
    public double getResolutionRatio() {
        return resolutionRatio;
    }

    /**
     * @return the scaleRatio
     */
    public double getScaleRatio() {
        return scaleRatio;
    }

    /**
     * @return the windowRatio
     */
    public double getWindowRatio() {
        return windowRatio;
    }

    /**
     *  @return
     */
    public boolean isScratchEnabled() {
        return scratchEnabled;
    }

    /**
     * @param activeBlock the activeBlock to set
     */
    public void setActiveBlock(ConResBlock activeBlock) {
        try {
            this.activeBlock = activeBlock;
        } catch (Exception exception) {
        	this.activeBlock = null;
        }
    }

    /**
     * @param activePatch the activePatch to set
     */
    public void setActivePatch(ConResPatch activePatch) {
        try {
            this.activePatch = activePatch;
        } catch (Exception exception) {
        	this.activePatch = null;
        }
    }

    /**
     * @param blockImage the blockImage to set
     */
    public void setBlockImage(ImageFile blockImage) {
        this.blockImage = blockImage;
    }

    /**
     * @param blockImagePlus the blockImagePlus to set
     */
    public void setBlockImagePlus(ImagePlus blockImagePlus) {
        this.blockImagePlus = blockImagePlus;
    }

    /**
     * @param blockState the blockState to set
     */
    public void setBlockState(BlockState blockState) {
        try {
            this.blockState = blockState;
        } catch (Exception exception) {
        	this.blockState = null;
        }
    }

    /**
     * @param displayDPI the displayDPI to set
     */
    public void setDisplayDPI(double displayDPI) {
        this.displayDPI = displayDPI;
    }

    /**
     * @param history the history to set
     */
    public void setHistory(List<int[]> history) {
        try {
            this.history = history;
        } catch (Exception exception) {
        	this.history = null;
        }
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image) {
        try {
            this.image = image;
        } catch (Exception exception) {
            this.image = null;
        }
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        if (image == null) this.image = null;
        else setImage(getController().toBufferedImage(image));
    }

    /**
     * @param imageDPI the imageDPI to set
     */
    public void setImageDPI(double imageDPI) {
        this.imageDPI = imageDPI;
    }

    /**
     * @param patchDimensions the patchDimensions to set
     */
    public void setPatchDimensions(PatchDimensions patchDimensions) {
        this.patchDimensions = patchDimensions;
    }

    /**
     * @param patchImage the patchImage to set
     */
    public void setPatchImage(BufferedImage patchImage) {
        try {
            this.patchImage = patchImage;
        } catch (Exception exception) {
            this.patchImage = null;
        }
    }

    /**
     * @param patchImage the patchImage to set
     */
    public void setPatchImage(Image patchImage) {
        try {
            if (patchImage == null) this.patchImage = null;
            else setPatchImage(getController().toBufferedImage(patchImage));
        } catch (Exception exception) {
            this.patchImage = null;
        }
    }

    /**
     * @param patchPreviewSize the patchPreviewSize to set
     */
    public void setPatchPreviewSize(int patchPreviewSize) {
        this.patchPreviewSize = patchPreviewSize;
    }

    /**
     * 	@param dpiRatio
     */
    public void setResolutionRatio(double dpiRatio) {
        this.resolutionRatio = dpiRatio;
    }

    /**
     * @param scaleRatio the scaleRatio to set
     */
    public void setScaleRatio(double scaleRatio) {
        this.scaleRatio = scaleRatio;
    }

    /**
     *  @param scratchEnabled
     */
    public void setScratchEnabled(boolean scratchEnabled) {
        this.scratchEnabled = scratchEnabled;
    }

    /**
     * @param windowRatio the windowRatio to set
     */
    public void setWindowRatio(double windowRatio) {
        this.windowRatio = windowRatio;
    }
}
