/*
 * @(#)StepUp.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

/**
 * @author daflair
 */
public class StepUp extends SteppingStrategy {

    /**
     * @param blockState
     */
    public StepUp(BlockState blockState) {
        super(blockState);

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
        if (!this.validMove(this.moveBy(-1, 0))) return false;

        return true;
    }
}
