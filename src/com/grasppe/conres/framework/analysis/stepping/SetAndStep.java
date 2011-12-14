/*
 * @(#)SetAndStep.java   11/08/25
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
public class SetAndStep extends SteppingStrategy {

    /** Field description */
    protected int	value;

    /**
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
     * @return
     */
    @Override
    public boolean execute() {

        // TODO Auto-generated method stub
    	BlockState newState = this.getFinalState();
        int	column = this.getFinalState().getColumn();
        int	row    = this.getFinalState().getRow();
        int	value  = this.value;
               
        switch(PatchDesignation.designation(value)) {
        case PASS :
            newState.setValue(PatchDesignation.PASS, row, column);
        	assumePass(newState, row, column);
        	// move down
        	break;
        case MARGINAL :
        	newState.setValue(PatchDesignation.MARGINAL, row, column);
        	// if next is judged 
        case FAIL :
        	newState.setValue(PatchDesignation.FAIL, row, column);
        	assumeFail(newState, row, column);
        	// move up
        	break;        	
        }
        
        // TODO: Move to next position
        
        
        setFinalState(newState);

        return true;
    }
    
    public static void assumePass (BlockState blockState, int row, int column) {
    	int maxRow = blockState.rows-1;
    	int minRow = 0;
    	fillValues(blockState, minRow, row+1, column, PatchDesignation.PASS, PatchDesignation.ASSUMED_PASS);

    }
    
    public static void assumeFail (BlockState blockState, int row, int column) {
    	int maxRow = blockState.rows-1;
    	int minRow = 0;
    	
    	fillValues(blockState, row+1, maxRow, column, PatchDesignation.FAIL, PatchDesignation.ASSUMED_FAIL);
    }
    
    public static void fillValues (BlockState blockState, int startRow, int endRow, int column, PatchDesignation newValue) {
    	fillValues(blockState,startRow, endRow, column, null, newValue);
    }
    
    public static void fillValues (BlockState blockState, int startRow, int endRow, int column, PatchDesignation testValue, PatchDesignation newValue) {
    	if (startRow>endRow)
    		for (int r = endRow; r>=startRow; r--) {
    			if (testValue==null || blockState.getDesignation(r, column)!=testValue)
    				blockState.setValue(newValue, r, column);
    		}
		else if (startRow<endRow)
    		for (int r = endRow; r<=startRow; r++) {
    			if (testValue==null || blockState.getDesignation(r, column)!=testValue)
    				blockState.setValue(newValue, r, column);    			
    		}
    }    
    
    public static void assumeMarginal (BlockState blockState, int row, int column) {
    	int maxRow = blockState.rows-1;
    	int minRow = 0;
    	int lastPass = findLast(blockState, row, column, PatchDesignation.PASS);
    	int nextFail = findNext(blockState, row, column, PatchDesignation.FAIL);
    	int lastMarginal = findLast(blockState, row, column, PatchDesignation.MARGINAL);
    	int nextMarginal = findNext(blockState, row, column, PatchDesignation.MARGINAL);
    	
//    	 Nothing to assume!
//    	
//    	if (lastPass < row && lastMarginal<minRow) {
//    		// TODO: nothing to assume and move up
//    	} else if (lastPass < row && lastMarginal>lastPass) {
//    		// TODO: assume marginal
//    	}
    }
    
    public static int findLast(BlockState blockState, int row, int column, PatchDesignation designation) {
    	while ((row>-1) &&  (blockState.getDesignation(row-1, column)!=designation))
    		row--;
    	return row;    	
    }
    
    public static int findNext(BlockState blockState, int row, int column, PatchDesignation designation) {
    	while ((row<blockState.rows) &&  (blockState.getDesignation(row+1, column)!=designation))
    		row++;
    	return row;
    }
}
