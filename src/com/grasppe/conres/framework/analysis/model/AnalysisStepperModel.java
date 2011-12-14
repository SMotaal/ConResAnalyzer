/*
 * @(#)AnalysisManagerModel.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis.model;

import com.grasppe.conres.framework.analysis.AnalysisStepper;
import com.grasppe.conres.framework.analysis.stepping.BlockState;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.framework.targets.model.grid.ConResPatch;
import com.grasppe.lure.components.AbstractModel;

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
	 * @return the patchPreviewSize
	 */
	public int getPatchPreviewSize() {
		patchPreviewSize = getController().getTargetManager().patchPreviewSize;
		return patchPreviewSize;
	}

	/** Field description */
    private BlockState	blockState = null;		// new BlockState(10, 10, 0, 0, BlockState.fudgeMap0());

    /** Field description */
    private List<int[]>		history     = new ArrayList<int[]>();		// @SuppressWarnings("rawtypes")
    private ConResBlock		activeBlock = null;
    private ConResPatch		activePatch = null;
    private BufferedImage
		image                           = null,
		patchImage                      = null;

	private boolean	scratchEnabled = false;
	
	private int patchPreviewSize = 550;

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
//        this.patchPreviewSize =getController().getTargetManager().patchPreviewSize;
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
     * @return the history
     */
    public List<int[]> getHistory() {
        return history;
    }

    /**
     * @return the patchImage
     */
    public BufferedImage getPatchImage() {
        return patchImage;
    }

    /**
     * @param activeBlock the activeBlock to set
     */
    public void setActiveBlock(ConResBlock activeBlock) {
        try {
            this.activeBlock = activeBlock;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @param activePatch the activePatch to set
     */
    public void setActivePatch(ConResPatch activePatch) {
        try {
            this.activePatch = activePatch;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @param blockState the blockState to set
     */
    public void setBlockState(BlockState blockState) {
        try {
            this.blockState = blockState;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @param history the history to set
     */
    public void setHistory(List<int[]> history) {
        try {
            this.history = history;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image) {
        try {
            this.image = image;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
    	if (image==null)
    		this.image=null;
    	else
    		setImage(getController().toBufferedImage(image));
    }

    /**
     * @param patchImage the patchImage to set
     */
    public void setPatchImage(BufferedImage patchImage) {
        try {
            this.patchImage = patchImage;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @param patchImage the patchImage to set
     */
    public void setPatchImage(Image patchImage) {
        try {
        	if (patchImage==null)
        		this.patchImage = null;
        	else
        		setPatchImage(getController().toBufferedImage(patchImage));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

	public boolean isScratchEnabled() {
		return scratchEnabled;
	}

	public void setScratchEnabled(boolean scratchEnabled) {
		this.scratchEnabled = scratchEnabled;
	}
}
