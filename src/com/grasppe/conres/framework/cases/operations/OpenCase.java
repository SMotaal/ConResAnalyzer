/*
 * @(#)OpenCase.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.cases.operations;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.conres.framework.cases.model.CaseModel;
import com.grasppe.conres.preferences.ConResAnalyzerPreferencesAdapter;
import com.grasppe.lure.components.AbstractCommand.Types;
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
 *         @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public class OpenCase extends CaseManagerCommand {

    protected static final String	name               = "OpenCase";
    protected static final int		mnemonicKey        = KeyEvent.VK_O;
    protected static final String description = "Open an existing case or create a new case from a folder with scanned images and a target definition file.";
//    String							defaultChooserPath = CaseManagerModel.defaultChooserPath;
    protected static final String type = Types.FILE;
    /**
	 * @return the commandMenu
	 */
	public String getMenuKey() {
		return type;
	}

    /**
     */
    private CaseManager	controller;
    int					dbg = 0;

    /**
     * Constructs a realization of AbstractCommand.
     * @param listener
     * @param controller TODO
     */
    public OpenCase(CaseManager controller, ActionListener listener) {
        super(listener, name);
        this.controller   = controller;
        super.mnemonicKey = mnemonicKey;
        super.description = description;
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
                                 + " and reorganize cases in the case manager model.", dbg);

            getModel().notifyObservers();

        } else {
            GrasppeKit.debugText(
                "Open Case Unsuccessful",
                "New case was not created... Reverting cases in the case manager model.", dbg);

            if (newCase != null) finalizeOpenCase(newCase);

            getModel().notifyObservers();

            if (userCancelled) throw new CancellationException("User cancelled the open case request.");
            else canProceed = false;
        }
        
        String caseFolderPath = new File(newCase.path).getParent(); //.getAbsolutePath();
        
        ConResAnalyzerPreferencesAdapter.putDefaultCasePath(caseFolderPath);

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

        GrasppeKit.debugText("Open Case Attempt", "Call SelectCaseFolder", dbg);
        if (!getModel().canGetNewCase())
            new UnexpectedException(
                "Unexpectedly... could not make room for a new case right now!").printStackTrace();

        while (!canProceed) {

            SelectCaseFolder	selectCaseFolder = new SelectCaseFolder();

            canProceed = selectCaseFolder.quickSelect();

            if (canProceed)
                GrasppeKit.debugText("Open Case Selected",
                                     "SelectCaseFolder returned "
                                     + selectCaseFolder.getSelectedFile().getAbsolutePath(), dbg);
            else
                GrasppeKit.debugText("Open Case Cancled", "SelectCaseFolder was not completed",
                                     dbg);
            if (!canProceed) break;		// Action responded to in alternative scenario

            // TODO: Verify case folder!
            // TODO: Confirm and close current case before attempting to switching cases
            canProceed = confirmCaseClose();

            if (!canProceed) break;		// Alternative scenario succeeded

            try {
                canProceed = openCase(selectCaseFolder.getSelectedFile().getAbsoluteFile());
            } catch (CancellationException exception) {
                break;
            }
        }

        return canExecute(true);
    }
    
    public boolean confirmCaseClose() {
        try {
            return ((CloseCase)this.controller.getCommandHandler().getCommand(
                "CloseCase")).quickClose(getKeyEvent());
        } catch (Exception exception) {
            return true;
        }
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
