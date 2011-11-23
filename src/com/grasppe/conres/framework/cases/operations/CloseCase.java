package com.grasppe.conres.framework.cases.operations;

import ij.IJ;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import com.grasppe.lure.framework.GrasppeKit;

/**
 * Defines Case Manager's Close Case actions and command, using the EAC pattern.
 *
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CloseCase extends CaseManagerCommand {

    protected static final String	name        = "CloseCase";
    protected static final int		mnemonicKey = KeyEvent.VK_C;

    /**
     * Constructs a realization of AbstractCommand.
     *
     * @param listener
     */
    public CloseCase(ActionListener listener) {
        super(listener, name);
        super.mnemonicKey = mnemonicKey;
        update();
    }

    /**
     * Performs the command operations when called by execute().
     *
     * @return
     */
    @Override
	public boolean perfomCommand() {
        boolean	canProceed = !isCaseClosed();		// canExecute();

        GrasppeKit.debugText("Close Case Attempt", "will be checking isCaseClosed()", 4);
        if (!canProceed) return true;		// Action responded to in alternative scenario
        if (!altPressed())
            canProceed = IJ.showMessageWithCancel(name,
                "Do you want to close the current case?");
        if (!canProceed) return true;		// Action responded to in alternative scenario
        GrasppeKit.debugText("Close Case Proceeds", "User confirmed close.", 3);
        getModel().backgroundCase = getModel().currentCase;
        getModel().currentCase    = null;
        GrasppeKit.debugText("Closed Case Success",
                             "Moved current case to background and cleared current case.",
                             4);
        getModel().notifyObservers();

        // update();
        return true;	// Action responded to in intended scenario
    }

    /**
     * Method description
     *
     * @param keyEvent
     *
     * @return
     */
    public boolean quickClose(KeyEvent keyEvent) {
        boolean	canProceed;

        try {
            canProceed = execute(true, keyEvent);		// Don't care if a case was closed
            canProceed = isCaseClosed();				// Only care that no case is open!

            // getModel().notifyObservers();
        } catch (Exception e) {

            // forget about current case!
            GrasppeKit.debugText("Close Case Attempt",
                                 "Failed to close case or no case was open!" + "\n\n"
                                 + e.toString(), 2);
            canProceed = false;
        }

        return canProceed;
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        super.update();

        // TODO: Enable if open case, else disable
        canExecute(!isCaseClosed());	//

        // notifyObservers();
    }

    /**
     * Method description
     *
     * @return
     */
    public boolean isCaseClosed() {
        boolean	value = !(getModel().hasCurrentCase());

        GrasppeKit.debugText("isCaseClose", "" + value, 3);

        return value;
    }
}