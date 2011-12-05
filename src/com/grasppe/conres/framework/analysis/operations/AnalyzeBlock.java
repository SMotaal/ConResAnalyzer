/*
 * @(#)AnalyzeBlock.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis.operations;

import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.analysis.AnalysisStepper;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 *         Defines Case Manager's New Case actions and command, using the EAC pattern.
 *         @version        $Revision: 1.0, 11/11/08
 *         @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AnalyzeBlock extends AnalysisCommand {

    protected static final String	name        = "AnalyzeBlock";
    protected static final int		mnemonicKey = KeyEvent.VK_M;

    /**
     */
    private AnalysisManager	controller;

    /**
     * Constructs a realization of AbstractCommand.
     * @param listener
     * @param controller TODO
     */
    public AnalyzeBlock(AnalysisManager controller, ActionListener listener) {
        super(listener, name);
        this.controller   = controller;
        super.mnemonicKey = mnemonicKey;
        update();
    }

    /**
     * Performs the command operations when called by execute().
     * @return
     */
    @Override
    public boolean perfomCommand() {
        boolean			canProceed      = canExecute();

//        AnalysisStepper	analysisStepper = new AnalysisStepper(controller);

        controller.getAnalysisStepper().testRun();

//      CornerSelector conrnerSelector = new CornerSelector(controller);
//      canProceed = new SelectCornersFunction(conrnerSelector).execute(true);

        return true;
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        canExecute(true);		// canMarkBlocks());       // getModel().hasCurrentCase());
        super.update();
    }

//  protected boolean canMarkBlocks() {
//    ConResBlock block = getModel().getActiveBlock();
////      if (block==null) return false;
//    return block!=null;
//  }    

//  /**
//   * @return the mnemonicKey
//   */
//  @Override
//  public int getMnemonicKey() {
//    return mnemonicKey;
//  }
}
