/*
 * @(#)StepRight.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

/**
 * @author daflair
 */
public class StepRight extends SteppingStrategy {

    /**
     * @param blockState
     */
    public StepRight(BlockState blockState) {
        super(blockState);

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

        if (!this.validMove(this.moveBy(0, 1))) return false;

        return true;
    }
}
