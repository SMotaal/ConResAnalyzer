/*
 * @(#)SetAndStep.java   11/08/25
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.analysis.stepping;

/**
 * @author daflair
 *
 */
public class SetAndStep extends SteppingStrategy {

    /** Field description */
    protected int	value;

    /**
     * Constructs ...
     *
     *
     * @param blockState
     * @param value
     */
    public SetAndStep(BlockState blockState, int value) {
        super(blockState);
        this.value = value;

        // TODO Auto-generated constructor stub
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.SteppingStrategy#execute()
     */

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public boolean execute() {

        // TODO Auto-generated method stub
        int	column = this.getFinalState().getColumn();
        int	row    = this.getFinalState().getRow();
        int	value  = this.value;

        this.getFinalState().setValue(value, row, column);

        StepNext	stepNext = new StepNext(this.getFinalState());
        boolean		executed = stepNext.execute();

        this.setFinalState(stepNext.getFinalState());

        return executed;
    }
}
