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

import java.util.ArrayList;
import java.util.List;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AnalysisStepperModel extends AbstractModel {

    /** Field description */
    private BlockState	blockState = null;		// new BlockState(10, 10, 0, 0, BlockState.fudgeMap0());

    /** Field description */
    private List<int[]>	history     = new ArrayList<int[]>();		// @SuppressWarnings("rawtypes")
    private ConResBlock	activeBlock = null;
    private ConResPatch	activePatch = null;

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
     * @return the blockState
     */
    public BlockState getBlockState() {
        return blockState;
    }

    /**
     * @return the history
     */
    public List<int[]> getHistory() {
        return history;
    }

    /**
     * @param activeBlock the activeBlock to set
     */
    public void setActiveBlock(ConResBlock activeBlock) {
        this.activeBlock = activeBlock;
        notifyObservers();
    }

    /**
     * @param activePatch the activePatch to set
     */
    public void setActivePatch(ConResPatch activePatch) {
        this.activePatch = activePatch;
    }

    /**
     * @param blockState the blockState to set
     */
    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;

        try {
            notifyObservers();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @param history the history to set
     */
    public void setHistory(List<int[]> history) {
        this.history = history;
        notifyObservers();
    }
}
