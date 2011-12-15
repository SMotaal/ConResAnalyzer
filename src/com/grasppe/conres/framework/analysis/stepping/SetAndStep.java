/*
 * @(#)SetAndStep.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



package com.grasppe.conres.framework.analysis.stepping;

import com.grasppe.conres.framework.analysis.stepping.BlockState.PatchDesignation;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * @author daflair
 */
public class SetAndStep extends SteppingStrategy {

    protected int	finalValue;

    /**
     * @param blockState
     * @param value
     */
    public SetAndStep(BlockState blockState, int value) {
        super(blockState);
        finalValue = value;
    }

    /**
     */
    public void assumeClear() {
        if (!(row < getMaxRow())) return;

        BlockState	before = finalState.clone();
        BlockState	after  = fillValues(before.clone(), getMaxRow(), row + 1, column, MARGINAL,
                                      CLEAR);

        setFinalState(after);
    }

    /**
     */
    public void assumeFail() {
        if (!(row < getMaxRow())) return;

        BlockState	before = finalState.clone();
        BlockState	after  = fillValues(before.clone(), getMaxRow(), row + 1, column, null,
                                      ASSUMED_FAIL);

        setFinalState(after);
    }

    /**
     */
    public void assumePass() {
        if (!(row > getMinRow())) return;

        BlockState	before = finalState.clone();
        BlockState	after  = fillValues(before.clone(), getMinRow(), row - 1, column, null,
                                      ASSUMED_PASS);

        setFinalState(after);
    }

    /**
     * 	@return
     */
    public boolean checkColumn() {
        int	firstMarginal = findNextIs(finalState, getMinRow(), column, MARGINAL);
        int firstPass = findNextIs(finalState, getMinRow(), column, MARGINAL);

        if (firstMarginal>0) return checkAbove(firstMarginal) && checkBelow(firstMarginal);
        else return checkBelow(Math.max(0,firstPass));        
        
    }

    /**
     * @return  true when executed successfully
     */
    @Override
    public boolean execute() {

        PatchDesignation	intended = PatchDesignation.designation(finalValue);

        try {
            if (intended == PASS) {
                finalState.setValue(PASS, row, column);		// Judged Pass
                assumePass();
            } else if (intended == FAIL) {
                finalState.setValue(FAIL, row, column);		// Judged Fail
                assumeFail();
            } else if (intended == MARGINAL) {
                if (finalState.getPatchValue(row, column) != MARGINAL) assumeClear();
                finalState.setValue(MARGINAL, row, column);
            } else
                return false;
        } catch (Exception exception) {
            GrasppeKit.debugError("Setting Values", exception, 2);
        }

        try {
            if (checkColumn())
            	setFinalState(new StepOver(finalState).executedState());
            else
            	setFinalState(new StepNext(finalState).executedState());
        } catch (Exception exception) {
            GrasppeKit.debugError("Stepping Next", exception, 2);
        }

        return true;
    }
}
