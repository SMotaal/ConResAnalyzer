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
public class ReStep extends SteppingStrategy {

  /** Field description */
  private BlockState pastStep;

  /**
   * @param blockState
   * @param history
   */
  public ReStep(BlockState blockState, List history) {
    super(blockState);
    this.pastStep = (BlockState)history.get(history.size() - 1);

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

//  finalState.setValue(pastStep);
    setFinalState(pastStep);

    return this.moveTo(pastStep.getRow(), pastStep.getColumn());
  }
}
