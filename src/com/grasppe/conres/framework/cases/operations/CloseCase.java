/*
 * @(#)CloseCase.java   11/12/02
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.cases.operations;

import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.lure.framework.GrasppeKit;

import ij.IJ;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.rmi.UnexpectedException;

/**
 * Defines Case Manager's Close Case actions and command, using the EAC pattern.
 *
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CloseCase extends CaseManagerCommand {

    protected static final String	name         = "CloseCase";
    protected static final int		mnemonicKey  = KeyEvent.VK_C;
    protected boolean				isCaseClosed = true;
    protected boolean	backgroundMode = false;

    /**
     * Constructs a realization of AbstractCommand.
     *
     * @param listener
     */
    public CloseCase(ActionListener listener) {
        super(listener, name);
        super.mnemonicKey = mnemonicKey;
        getModel().attachObserver(this);
        update();
    }

    /**
     * Performs the command operations when called by execute().
     *
     * @return
     */
    @Override
    public boolean perfomCommand() {
        boolean	canProceed = !isCaseClosed;		// && !getModel().isBusy();      // canExecute();

        GrasppeKit.debugText("Close Case Attempt", "will be checking isCaseClosed()", 3);

        if (!canProceed) {
            notifyObservers();

            return true;	// Action responded to in alternative scenario
        }

        if (!altPressed())
            canProceed = IJ.showMessageWithCancel(name, "Do you want to close the current case?");
        if (!canProceed) return true;		// Action responded to in alternative scenario
        GrasppeKit.debugText("Close Case Proceeds", "User confirmed close.", 3);

        try {
            getModel().backgroundCurrentCase();
            if (!backgroundMode) getModel().discardBackgroundCase();

        } catch (UnexpectedException e) {
            e.printStackTrace();
        }

        GrasppeKit.debugText("Closed Case Success",
                             "Moved current case to background and cleared current case.", 3);
        getModel().notifyObservers();
        notifyObservers();

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
        boolean	canProceed = true;
        backgroundMode =  true;

        try {
            canProceed = forceExecute(); //(keyEvent);
            getModel().notifyObservers();
        } catch (Exception e) {

            GrasppeKit.debugText("Close Case Attempt",
                                 "Failed to close case or no case was open!" + "\n\n"
                                 + e.toString(), 2);
            canProceed = false;
        }
        backgroundMode = false;

        return canProceed;
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        canExecute(!updateCaseClosed());	//
        super.update();
    }

    /**
     * 	@return
     */
    private boolean updateCaseClosed() {
        CaseManagerModel	model = getModel();
        boolean				value = !(model.hasCurrentCase());

        GrasppeKit.debugText("isCaseClose", "" + value, 3);

        isCaseClosed = value;

        return value;

    }
}
