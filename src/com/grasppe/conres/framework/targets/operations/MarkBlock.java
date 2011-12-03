/*
 * @(#)MarkBlock.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets.operations;

import com.grasppe.conres.framework.targets.CornerSelector;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 *         Defines Case Manager's New Case actions and command, using the EAC pattern.
 *         @version        $Revision: 1.0, 11/11/08
 *         @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public class MarkBlock extends TargetManagerCommand {

    protected static final String	name        = "MarkBlock";
    protected static final int		mnemonicKey = KeyEvent.VK_M;

    /**
     */
    private TargetManager	controller;

    /**
     * Constructs a realization of AbstractCommand.
     * @param listener
     * @param controller TODO
     */
    public MarkBlock(TargetManager controller, ActionListener listener) {
        super(listener, name);
        this.controller   = controller;
        super.mnemonicKey = mnemonicKey;
        update();
    }

    /**
     *  @return
     */
    protected boolean canMarkBlocks() {
        try {
            ConResBlock	block = getModel().getActiveBlock();

            return block != null;
        } catch (Exception exception) {
            return false;
        }

    }

    /**
     * Performs the command operations when called by execute().
     * @return
     */
    @Override
    public boolean perfomCommand() {
        boolean			canProceed      = canExecute();

        CornerSelector	conrnerSelector = controller.getCornerSelector();		// new CornerSelector(controller);

        canProceed = new SelectCornersOperation(conrnerSelector).execute(true);

        return true;
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        canExecute(canMarkBlocks());	// getModel().hasCurrentCase());
        super.update();
    }

//  /**
//   * @return the mnemonicKey
//   */
//  @Override
//  public int getMnemonicKey() {
//      return mnemonicKey;
//  }
}
