/*
 * @(#)MarkBlock.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets.operations;

import com.grasppe.conres.framework.targets.CornerSelector;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.lure.components.AbstractCommand.Types;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 *         Defines Case Manager's New Case actions and command, using the EAC pattern.
 *         @version        $Revision: 1.0, 11/11/08
 *         @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class MarkBlock extends TargetManagerCommand {

    protected static final String	name        = "MarkBlock";
    protected static final int		mnemonicKey = KeyEvent.VK_M;
    protected static final String description = "Visually identify the corner points of a target image.";
    protected static final String type = "block";
    /**
	 * @return the commandMenu
	 */
	public String getMenuKey() {
		return type;
	}

    /**
     */
    private TargetManager	controller;

    /**
     * Constructs a realization of AbstractCommand.
     * @param listener
     * @param controller
     */
    public MarkBlock(TargetManager controller, ActionListener listener) {
        super(listener, name);
        this.controller   = controller;
        super.mnemonicKey = mnemonicKey;
        super.description = description;
        update();
    }

    /**
     *  @return
     */
    protected boolean canMarkBlock() {
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

        canProceed = new SelectCornersFunction(conrnerSelector).execute(true);

        return true;
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        canExecute(canMarkBlock());	// getModel().hasCurrentCase());
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
