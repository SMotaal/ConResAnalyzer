package com.grasppe.conres.analyzer.operations;

import ij.IJ;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import com.grasppe.conres.analyzer.ConResAnalyzer;

/**
 * Defines Case Manager's Close Case actions and command, using the EAC pattern.
 *
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class Quit extends ConResAnalyzerCommand {

    protected static final String	name        = "Quit";
    protected static final int		mnemonicKey = KeyEvent.VK_Q;

    /**
     * Constructs a realization of AbstractCommand.
     *
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
     *
     * @return
     */
    @Override
	public boolean perfomCommand() {
        if (!controlKeyPressed()) return true;

        if (altPressed() || IJ.showMessageWithCancel(name, "Do you really want to quit?"))
        	System.exit(0);

//        if (altPressed() || IJ.showMessageWithCancel(name, "Do you really want to quit?"))
//            System.exit(0);

        return true;	// Action responded to in intended scenario
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