/*
 * @(#)StepNext.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

import com.grasppe.conres.framework.analysis.stepping.BlockState.PatchDesignation;

/**
 * @author daflair
 */
public class StepNext extends SteppingStrategy {

    /** Field description */
    protected int	step = 1;
    protected int	finalValue;

    /**
     * @param blockState
     */
    public StepNext(BlockState blockState) {
        super(blockState);
    }

    /**
     * @return  true when executed successfully
     */
    @Override
    public boolean execute() {

        PatchDesignation	current = PatchDesignation.designation(startValue);

        if (current == PASS) return moveDownOrOver();
        else if (current == FAIL) return moveUpOrOver();
        else if (current == MARGINAL)
                 return (checkAbove(row) && moveDownOrOver()) || moveUpOrOver();
        else return false;
    }

    /**
     *  @return
     */
    protected boolean moveDownOrOver() {
        boolean	executed = moveDown() || moveOver();

        return executed;
    }

    /**
     *  @return
     */
    protected boolean moveUpOrOver() {
        boolean	executed = moveUp() || moveOver();

        return executed;
    }
}
