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

import javax.swing.JOptionPane;

import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractCommand.Types;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * Defines Case Manager's Close Case actions and command, using the EAC pattern.
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class Quit extends ConResAnalyzerCommand {

    protected static final String	name        = "Quit";
    protected static final int		mnemonicKey = KeyEvent.VK_Q;
    protected static final String type = Types.FILE;
    
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
    public Quit(ActionListener listener) {
        super(listener, name);
        super.mnemonicKey = mnemonicKey;
        executable        = true;
        update();
    }

    /**
     * Performs the command operations when called by execute().
     * @return
     */
    @Override
    public boolean perfomCommand() {


//        if (altPressed() || IJ.showMessageWithCancel(name, "Do you really want to quit?"))
    	((AbstractController)actionListener).attemptQuit();
//    	try {
//			commandHandler.wait();
//		} catch (InterruptedException exception) {
//			// TODO Auto-generated catch block
//			exception.printStackTrace();
//		}
    	if(((AbstractController)actionListener).canQuit()) {
    		try {
				((AbstractController)actionListener).detatch();
			} catch (Throwable exception) {
				// TODO Auto-generated catch block
				GrasppeKit.debugError("Detatching Failed", exception, 1);
			}
            System.exit(0);
    	}

        return false;	// Action responded to in intended scenario
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
