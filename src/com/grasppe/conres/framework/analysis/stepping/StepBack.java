/*
 * @(#)StepBack.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

import java.util.List;

/**
 * @author daflair
 */
public class StepBack extends SteppingStrategy {

    /** Field description */
    private int[]	pastStep;

    /**
     * @param blockState
     * @param history
     */
    public StepBack(BlockState blockState, List history) {
        super(blockState);
        this.pastStep = (int[])history.get(history.size() - 1);

        // TODO Auto-generated constructor stub
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.SteppingStrategy#execute()
     */

    /**
     * @return
     */
    @Override
    public boolean execute() {

        // TODO Auto-generated method stub
        // if(!this.validMove(this.moveBy(-1, 0))) return false;
        // return true;
        return this.moveTo(this.pastStep[0], this.pastStep[1]);
    }
}
