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
  int						dbg = 4;

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
  public void assumeFail() {
    if (!(row < getMaxRow())) return;

    BlockState	before = finalState.clone();
    BlockState	after  = fillValues(before.clone(), getMaxRow(), row, column, null, ASSUMED_FAIL);

    // if (row>getMinRow()) after.setValue(CLEAR, row-1, column);

    setFinalState(after);
  }

  /**
   */
  public void assumeMarginal() {
    if (!(row < getMaxRow())) return;

    BlockState	before        = finalState.clone();

    int					firstMarginal = findNextIs(finalState, getMinRow(), column, MARGINAL);

    if (firstMarginal < 0) firstMarginal = row;

    int	nextNotMarginal = findNextNot(finalState, firstMarginal, column, MARGINAL);

    int	firstPass       = findNextIs(finalState, getMinRow(), column, PASS);
    int	firstReject     = findNextIs(finalState, getMinRow(), column, FAIL);
    int	lastPass        = findNextNot(finalState, getMinRow(), column, PASS) - 1;
    int	lastMarginal    = findNextNot(finalState, firstMarginal, column, MARGINAL) - 1;

    if ((firstMarginal > lastPass) && (firstMarginal < firstReject)) return;

    if (checkColumn()) return;

    BlockState	after = fillValues(before.clone(), getMaxRow(), row + 1, column, MARGINAL, CLEAR);

    setFinalState(after);
  }

  /**
   */
  public void assumePass() {
    if (!(row > getMinRow())) return;

    BlockState	before = finalState.clone();
    BlockState	after  = fillValues(before.clone(), getMinRow(), row - 1, column, null, ASSUMED_PASS);

    setFinalState(after);
  }

  /**
   *    @return
   */
  public boolean checkBlock() {
    int	dbg = 2;

    for (int c = finalState.getFirstColumn(); c < finalState.getColumns(); c++)
      if (!checkColumn(c)) {
        GrasppeKit.debugText("Checking Block", "Column: " + c + " is not valid", dbg);

        return false;
      } else
        GrasppeKit.debugText("Checking Block", "Column: " + c + " is valid", dbg);

    return true;
  }

  /**
   *  @return
   */
  public boolean checkColumn() {
    return checkColumn(column);
  }

  /**
   *    @param column
   *  @return
   */
  public boolean checkColumn(int column) {
	  int dbg = 2;
    int	firstRow      = getMinRow(),
				lastRow       = getMaxRow();

    int	startMarginal = findNextIs(finalState, firstRow, column, MARGINAL);
    int	endMarginal   = (startMarginal == -1) ? -1
                                              : findNextNot(finalState, startMarginal, column, MARGINAL);
    
    if (endMarginal!=-1) endMarginal -=1;

    
    boolean foundMarginal = startMarginal>-1;
    boolean extraMarginal = foundMarginal && endMarginal!=-1 && endMarginal<lastRow && (findNextIs(finalState, endMarginal+1, column, MARGINAL)>=1);

    int	startPass     = findNextIs(finalState, firstRow, column, PASS);
    int	endPass       = (startPass == -1) ? -1
                                          : findNextNot(finalState, startPass, column, PASS);
//    int	extraPass     = (endPass == -1) ? -1
//                                        : findNextIs(finalState, endPass, column, PASS);
    if (endPass!=-1) endPass -=1;
    
    boolean foundPass = (startPass==firstRow);
    boolean extraPass = foundPass && endPass!=-1 && endPass < lastRow && findNextIs(finalState, endPass+1, column, PASS)!=-1;

    int	startFail     = findNextIs(finalState, firstRow, column, FAIL);
    int	endFail       = (startFail <= getMinRow()) ? -1
                                                   : findNextNot(finalState, startFail, column, FAIL);
    
  	
//    if ((endPass == -1) && (startPass != -1)) endPass = startPass;
    if (startFail == -1) startFail = lastRow + 1;
    if (startFail<=lastRow && endFail == -1) endFail = lastRow;
    
    boolean foundFail = (endFail==lastRow);
    
    // found additional segment of pass or marginal
    if (extraPass || extraMarginal) {
    	GrasppeKit.debugText("Checking Column", "found additional segment of pass or marginal", dbg);
    	return false;
    }
    
    // found no pass and no fail, need at least one
    if (!foundPass && !foundFail) {	//    if ((endPass < firstRow) && (startFail > lastRow)) {
    	GrasppeKit.debugText("Checking Column", "found no pass and no fail, need at least one", dbg);
    	return false;
    }
    
    // found invalid fail boundary
    if (!foundFail &&  endPass!=lastRow && endMarginal!=lastRow) {
    	GrasppeKit.debugText("Checking Column", "found invalid fail boundary", dbg);
    	return false;    	
    }
    
    // found invalid pass boundary
	if (!foundPass &&  startFail!=firstRow && startMarginal!=firstRow) {
		GrasppeKit.debugText("Checking Column", "found invalid pass boundary", dbg);
		return false;    	
	}
	
	// found invalid marginal boundary
	if (foundMarginal && (startMarginal!=firstRow && startMarginal!=endPass+1) && (endMarginal!=lastRow && endMarginal==startFail-1)) {
		GrasppeKit.debugText("Checking Column", "found invalid marginal boundary", dbg);
		return false;    	
	}
//    
//    // not found fail boundary at or past the last row
//    if ((endFail<lastRow) && (endPass!=lastRow || endMarginal!=lastRow)) {
//    	GrasppeKit.debugText("Checking Column", "not found fail boundary at or past the last row", dbg);
//    	return false;
//    }

//    if (					//
//            ((endPass == -1) && (startFail == -1))
//            ||		// no pass and no fail, need one of the two //(endPass < firstRow) && (startFail > lastRow)
//              ((startMarginal != -1) && ((endPass > startMarginal) || (extraPass > -1))) ||			// found pass after marginal
//                ((startMarginal != -1) && (startFail < endMarginal)) ||													// found marginal after fail
//                  ((endFail != -1) && (endFail != lastRow))) return false;

    return true;

//  if (firstPass<0 && firstFail>0)   // firstMarginal<0 &&
//    return false;
//  
//  if (lastPass == firstFail-1 && firstFail>0) return true;
//  
//  if (lastPass < firstFail && firstFail>0 && lastPass>0)
//    if (findNextNot(finalState, lastPass, column, MARGINAL)==firstFail)
//        return true;
//
//  if (firstMarginal>0) return checkAbove(firstMarginal) && checkBelow(firstMarginal);
//  else return checkBelow(Math.max(0,firstPass));        

  }

  /**
   * @return  true when executed successfully
   */
  @Override
  public boolean execute() {

    PatchDesignation	intended = PatchDesignation.designation(finalValue);

    try {
      if (intended == PASS) {
        assumePass();
        finalState.setValue(ASSUMED_PASS, row, column);			// Judged Pass
      } else if (intended == FAIL) {
        assumeFail();
        finalState.setValue(ASSUMED_FAIL, row, column);			// Judged Fail
      } else if (intended == MARGINAL) {
        if (finalState.getPatchValue(row, column) != MARGINAL) assumeMarginal();
        finalState.setValue(MARGINAL, row, column);
      } else if (intended == CLEAR) {
        fillValues(finalState, getMinRow(), getMaxRow(), column, CLEAR);
      } else
        return false;
    } catch (Exception exception) {
      GrasppeKit.debugError("Setting Values", exception, 2);
    }

    try {
      if (checkColumn()) setFinalState(new StepOver(finalState).executedState());
      else setFinalState(new StepNext(finalState).executedState());
    } catch (Exception exception) {
      GrasppeKit.debugError("Stepping Next", exception, 2);
    }

    return true;
  }
}
