/*
 * @(#)StepNext.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



package com.grasppe.conres.framework.analysis.stepping;

/**
 * @author daflair
 */
public class StepOver extends SteppingStrategy {

    /** Field description */
    protected int	step = 1;
    protected int	finalValue;

    /**
     * @param blockState
     */
    public StepOver(BlockState blockState) {
        super(blockState);
    }

    /**
     * @return  true when executed successfully
     */
    @Override
    public boolean execute() {

        return moveOver();
    }
}
