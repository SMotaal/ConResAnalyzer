/*
 * @(#)MarkBlock.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets.operations;

import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.grid.ConResTarget;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 *         Defines Case Manager's New Case actions and command, using the EAC pattern.
 *         @version        $Revision: 1.0, 11/11/08
 *         @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public class SelectBlock extends TargetManagerCommand {

    protected static final String	name        = "SelectBlock";
    protected static final int		mnemonicKey = KeyEvent.VK_B;

    /**
     */
    private TargetManager	controller;

    /**
     * Constructs a realization of AbstractCommand.
     * @param listener
     * @param controller TODO
     */
    public SelectBlock(TargetManager controller, ActionListener listener) {
        super(listener, name);
        this.controller   = controller;
        super.mnemonicKey = mnemonicKey;
        controller.getModel().attachObserver(this);
        update();
    }

    /**
     *  @return
     */
    protected boolean canSelectBlocks() {
        if (getModel() == null) return false;

        ConResTarget	target = getModel().getActiveTarget();

        if (target == null) return false;

        return target.getTargetBlocks().length > 0;
    }

    /**
     * Performs the command operations when called by execute().
     * @return
     */
    @Override
    public boolean perfomCommand() {
        boolean	canProceed = canExecute();

        canProceed = new SelectBlockFunction(controller).execute(true);

        return canProceed;
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        boolean	newState = canSelectBlocks();

        canExecute(newState);		// getModel().hasCurrentCase());
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
