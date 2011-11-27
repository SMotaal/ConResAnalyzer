package com.grasppe.conres.analyzer.operations;

import java.awt.event.ActionListener;


/**
 * Last Patch EAC pattern.
 *
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class NextPatch extends ConResAnalyzerCommand {
//
    protected static final String	name        = "Next Patch";
//    protected static final int		mnemonicKey = KeyEvent.VK_PERIOD;
//
//    /**
//     * Constructs a realization of AbstractCommand.
//     *
//     * @param listener
//     */
    public NextPatch(ActionListener listener) {
        super(listener, name);
//        super.mnemonicKey = mnemonicKey;
//        executable        = true;
//        update();
    }
//
//    /**
//     * Performs the command operations when called by execute().
//     *
//     * @return
//     */
//    @Override
//	public boolean perfomCommand() {
//
//        // TODO: Replace test code
//        ConResBootCamp.magnifyNextPatch();
//
//        return true;	// Action responded to in intended scenario
//    }
//
//    /**
//     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
//     */
//    @Override
//    public void update() {
//        super.update();
//
//        // TODO: Enable if open case, else disable
//        canExecute(ConResBootCamp.canMagnifyPatch());
//    }
}