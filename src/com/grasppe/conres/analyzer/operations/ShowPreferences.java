/*
 * @(#)Quit.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer.operations;

import ij.IJ;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import com.grasppe.conres.analyzer.PreferencesManager;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractCommand.Types;

/**
 * Defines Case Manager's Close Case actions and command, using the EAC pattern.
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ShowPreferences extends AbstractCommand {

    protected static final String	name        = "ShowPreferences";
    protected static final int		mnemonicKey = KeyEvent.VK_P;
    protected static final String type = Types.EDIT;
    protected PreferencesManager manager = null;
    
    /**
	 * @return the commandMenu
	 */
	public String getMenuKey() {
		return type;
	}

    /**
     * Constructs a realization of AbstractCommand.
     * @param listener
     */
    public ShowPreferences(PreferencesManager manager) {
        super(manager, name);
        super.mnemonicKey = mnemonicKey;
        this.manager = manager; 
        executable        = true;
        update();
    }

    /**
     * Performs the command operations when called by execute().
     * @return
     */
    @Override
    public boolean perfomCommand() {
    	
    	manager.getView().show();
        return true;
        
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        super.update();
        // TODO: Enable if open case, else disable
        canExecute(true);
    }
}
