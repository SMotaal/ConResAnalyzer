/*
 * @(#)NewCase.java   11/11/26
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.cases.operations;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 *         Defines Case Manager's New Case actions and command, using the EAC pattern.
 *        
 *         @version        $Revision: 1.0, 11/11/08
 *         @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class NewCase extends CaseManagerCommand {

    protected static final String	name        = "NewCase";
    protected static final int		mnemonicKey = KeyEvent.VK_N;

    /**
     *
     */
    private CaseManager	caseManager;

    /**
     * Constructs a realization of AbstractCommand.
     *
     * @param listener
     * @param caseManager TODO
     */
    public NewCase(CaseManager caseManager, ActionListener listener) {
        super(listener, name);
        this.caseManager  = caseManager;
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
        boolean	canProceed = canExecute();

        // if (!canProceed) return true;     // Action responded to in alternative scenario
        // TODO: Show imageFolderChooser, if can create new case with images, confirm and close case before creating new case
        SelectCaseImagesFolder	selectCaseImagesFolder = new SelectCaseImagesFolder();

        GrasppeKit.debugText("Open Case Attempt", "Call SelectCaseFolder", 3);

        // TODO: Show imageFolderChooser
//      canProceed = false;
//      while (!canProceed) 
        if (!selectCaseImagesFolder.quickSelect()) return false;

        canProceed = true;

        // TODO: Validate imageFolder structure (if not Show imageFolderChooser)

        // TODO: Confirm and close current case before attempting to switching cases
        canProceed = ((CloseCase)this.caseManager.getCommandHandler().getCommand(
            "CloseCase")).quickClose(getKeyEvent());
        if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario

        // TODO: Create new case in metadata entry state
        GrasppeKit.debugText("New Case Creation",
                             "New case will be created and passed for metadata entry", 3);

        try {
            getModel().getNewCase();		// getModel().Case//new getModel()canProceed..CaseModel();
            getModel().notifyObservers();
            canProceed = true;
        } catch (Exception e) {
            GrasppeKit.debugText("New Case Failure",
                                 "Failed to create new case" + "\n\n" + e.toString(), 2);
            e.printStackTrace();
            canProceed = false;
        }

        if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario

        try {
//            getModel().currentCase    = getModel().newCase;		// Make current the new case
//            getModel().newCase        = null;					// Clear new case
//            getModel().backgroundCase = null;					// clear background case
        	getModel().promoteNewCase();
            canProceed                = true;
            getModel().notifyObservers();
        } catch (Exception e) {
            GrasppeKit.debugText("New Case Failure",
                                 "Failed to reorganize cases in the case manager model." + "\n\n"
                                 + e.toString(), 2);
            e.printStackTrace();
            canProceed = false;
        }

        if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario
        GrasppeKit.debugText("New Case Success",
                             "Created new case and reorganize cases in the case manager model.", 3);

        return true;	// Action responded to in intended scenario
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        super.update();

        // TODO: Enable if open case, else disable
        canExecute(true);		// getModel().hasCurrentCase());
    }

//  /**
//   * @return the mnemonicKey
//   */
//  @Override
//  public int getMnemonicKey() {
//      return mnemonicKey;
//  }
}
