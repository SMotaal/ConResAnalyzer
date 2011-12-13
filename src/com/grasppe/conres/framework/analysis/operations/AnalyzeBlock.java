/*
 * @(#)AnalyzeBlock.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis.operations;

import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.analysis.AnalysisStepper;
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
public class AnalyzeBlock extends AnalysisCommand {

    protected static final String	name        = "AnalyzeBlock";
    protected static final int		mnemonicKey = KeyEvent.VK_A;
    protected static final String description = "Visually evaluate the patches of the current target block.";
    protected static final String type = "block";
    /**
	 * @return the commandMenu
	 */
	public String getMenuKey() {
		return type;
	}

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
        super.description = description;
        update();
    }

    /**
     * Performs the command operations when called by execute().
     * @return
     */
    @Override
    public boolean perfomCommand() {
        boolean			canProceed      = canExecute();

        controller.getAnalysisStepper().showView();

        return true;
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        canExecute(canAnalyzeBlock());		// canMarkBlocks());       // getModel().hasCurrentCase());
        super.update();
    }
    
    /**
     *  @return
     */
    protected boolean canAnalyzeBlock() {
        try {
            ConResBlock	block = getModel().getActiveBlock();

            return block != null;
        } catch (Exception exception) {
            return false;
        }

    }

}
