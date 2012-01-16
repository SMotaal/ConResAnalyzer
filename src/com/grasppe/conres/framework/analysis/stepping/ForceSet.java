/*
 * @(#)ForceSet.java   12/01/15
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.analysis.stepping;

import com.grasppe.conres.framework.analysis.stepping.BlockState.PatchDesignation;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * @author daflair
 *
 */
public class ForceSet extends SetAndStep {

  /**
   * @param blockState
   * @param value
   */
  public ForceSet(BlockState blockState, int value) {
    super(blockState, value);
  }

  /*
   *  (non-Javadoc)
   * @see com.grasppe.conres.framework.analysis.stepping.SetAndStep#execute()
   */

  /**
   *    @return
   */
  @Override
  public boolean execute() {
    PatchDesignation intended = PatchDesignation.designation(finalValue);

    try {
      finalState.setValue(intended, row, column);
    } catch (Exception exception) {
      GrasppeKit.debugError("Setting Values", exception, 2);
    }

    try {

//    if (checkColumn()) setFinalState(new StepOver(finalState).executedState());
//    else setFinalState(new StepNext(finalState).executedState());
    } catch (Exception exception) {
      GrasppeKit.debugError("Stepping Next", exception, 2);
    }

    return true;
  }
}
