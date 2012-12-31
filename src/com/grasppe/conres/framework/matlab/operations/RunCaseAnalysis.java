/*
 * @(#)AnalyzeBlock.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.matlab.operations;

import com.grasppe.conres.framework.matlab.MatLabManager;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 *         Defines Case Manager's New Case actions and command, using the EAC pattern.
 *         @version        $Revision: 1.0, 11/11/08
 *         @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class RunCaseAnalysis extends MatLabCommand {

  protected static final String name        = "RunCaseAnalysis";
  protected static final int    mnemonicKey = KeyEvent.VK_E;
  protected static final String description = "Automaticly evaluate the blocks of the current case.";
  protected static final String type        = Types.FILE;

  /**
   */
  private MatLabManager controller;

  /**
   * Constructs a realization of AbstractCommand.
   * @param listener
   * @param controller
   */
  public RunCaseAnalysis(MatLabManager controller, ActionListener listener) {
    super(listener, name);
    this.controller   = controller;
    super.mnemonicKey = mnemonicKey;
    super.description = description;
    update();
  }

  /**
   *  @return
   */
  protected boolean canRunCaseAnalysis() {
    try {
      ConResBlock block               = controller.getTargetWrapper().getActiveBlock();

      boolean     hasMatLabController = hasMatLabController();

      return hasMatLabController() && (block != null);
    } catch (Exception exception) {
      GrasppeKit.debugError("Checking MatLab Command", exception, 5);

      return false;
    }

  }

  /**
   * Performs the command operations when called by execute().
   * @return
   */
  @Override
  public boolean perfomCommand() {
    boolean canProceed = canExecute();

    // controller.getAnalysisStepper().showView();
    try {

      // Matlab.mtFeval("disp", (new Object[]{getMatLabController()}), 1);
      getManager().fireAction(name);		// doClick(); //runBlockAnalysisHook.notifyCallback();
    } catch (Exception exception) {

      // TODO Auto-generated catch block
      exception.printStackTrace();
    }

    return true;
  }

  /**
   * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
   */
  @Override
  public void update() {
    canExecute(canRunCaseAnalysis());		// canMarkBlocks());       // getModel().hasCurrentCase());
    super.update();
  }

  /**
   * @return the commandMenu
   */
  public String getMenuKey() {
    return type;
  }
}
