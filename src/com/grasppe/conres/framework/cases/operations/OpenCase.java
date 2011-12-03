/*
 * @(#)OpenCase.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.cases.operations;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.conres.framework.cases.model.CaseModel;
import com.grasppe.lure.framework.GrasppeKit;

import ij.IJ;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.IOException;

import java.rmi.UnexpectedException;

import java.util.concurrent.CancellationException;

/**
 *         Defines Case Manager's New Case actions and command, using the EAC pattern.
 *         @version        $Revision: 1.0, 11/11/08
 *         @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class OpenCase extends CaseManagerCommand {

    protected static final String	name               = "OpenCase";
    protected static final int		mnemonicKey        = KeyEvent.VK_O;
    String							defaultChooserPath = CaseManagerModel.defaultChooserPath;

    /**
     */
    private CaseManager	controller;

    /**
     * Constructs a realization of AbstractCommand.
     * @param listener
     * @param controller TODO
     */
    public OpenCase(CaseManager controller, ActionListener listener) {
        super(listener, name);
        this.controller   = controller;
        super.mnemonicKey = mnemonicKey;
        update();
    }

    /**
     *  @param e
     *  @return
     */
    private boolean confirmSelectOtherCase(IOException e) {
        return IJ.showMessageWithCancel(name,
                                        "The selected case folder is missing some files.\n\n"
                                        + e.getMessage() + "\n\n"
                                        + "Do you want to select another folder?");

//      e.printStackTrace();
    }

    /**
     *  @param newCase
     */
    private void finalizeOpenCase(CaseModel newCase) {
        if (newCase.filesLoaded) {
            try {
                getModel().discardBackgroundCase();
            } catch (UnexpectedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                getModel().restoreBackgroundCase();
            } catch (UnexpectedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  @param caseFolder
     *  @return
     *  @throws CancellationException
     */
    public boolean openCase(File caseFolder) throws CancellationException {

        boolean		canProceed    = true;

        CaseModel	newCase       = null;

        boolean		userCancelled = false;

        try {
            newCase = getModel().getNewCaseModel();
        } catch (UnexpectedException e) {
            e.printStackTrace();
        }

        newCase.path = caseFolder.getAbsolutePath();

        try {
            getCaseManager().loadCase(newCase);
        } catch (IOException e) {
            canProceed    = confirmSelectOtherCase(e);
            userCancelled = !canProceed;
        }

        if (canProceed && (newCase != null) && newCase.filesLoaded) {
            try {
                getModel().promoteNewCase();
            } catch (UnexpectedException e) {
                e.printStackTrace();
            }

            GrasppeKit.debugText("Open Case Success",
                                 "Created new CaseModel for " + getModel().getCurrentCase().title
                                 + " and reorganize cases in the case manager model.", 3);

            getModel().notifyObservers();

        } else {
            GrasppeKit.debugText(
                "Open Case Unsuccessful",
                "New case was not created... Reverting cases in the case manager model.", 3);

            if (newCase != null) finalizeOpenCase(newCase);

            getModel().notifyObservers();

            if (userCancelled) throw new CancellationException("User cancelled the request");
            else canProceed = false;
        }

        return canProceed;

    }

    /**
     * Performs the command operations when called by execute().
     * @return
     */
    @Override
    public boolean perfomCommand() {
        if (!canExecute()) return false;

        boolean	canProceed = false;

        GrasppeKit.debugText("Open Case Attempt", "Call SelectCaseFolder", 3);
        if (!getModel().canGetNewCase())
            new UnexpectedException(
                "Unexpectedly... could not make room for a new case right now!").printStackTrace();

        while (!canProceed) {

            SelectCaseFolder	selectCaseFolder = new SelectCaseFolder();

            canProceed = selectCaseFolder.quickSelect();

            if (canProceed)
                GrasppeKit.debugText("Open Case Selected",
                                     "SelectCaseFolder returned "
                                     + selectCaseFolder.getSelectedFile().getAbsolutePath(), 3);
            else GrasppeKit.debugText("Open Case Cancled", "SelectCaseFolder was not completed", 3);
            if (!canProceed) break;		// Action responded to in alternative scenario

            // TODO: Verify case folder!
            // TODO: Confirm and close current case before attempting to switching cases
            try {
                canProceed = ((CloseCase)this.controller.getCommandHandler().getCommand(
                    "CloseCase")).quickClose(getKeyEvent());
            } catch (Exception exception) {
                canProceed = true;
                exception.printStackTrace();
            }

            if (!canProceed) break;		// Alternative scenario succeeded

            try {
                canProceed = openCase(selectCaseFolder.getSelectedFile().getAbsoluteFile());
            } catch (CancellationException exception) {
                break;
            }
        }

        return canExecute(true);
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        canExecute(true);		// getModel().hasCurrentCase());
        super.update();
    }

    /**
     * @return the controller
     */
    public CaseManager getCaseManager() {
        return controller;
    }

    /**
     *  @param caseManager
     */
    public void setCaseManager(CaseManager caseManager) {
        this.controller = caseManager;
    }
}
