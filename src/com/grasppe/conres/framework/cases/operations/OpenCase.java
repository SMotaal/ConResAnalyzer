/*
 * @(#)OpenCase.java   11/11/26
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.cases.operations;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.conres.framework.cases.model.CaseModel;
import com.grasppe.conres.io.model.CaseFolder;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import ij.IJ;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.rmi.UnexpectedException;

/**
 *         Defines Case Manager's New Case actions and command, using the EAC pattern.
 *        
 *         @version        $Revision: 1.0, 11/11/08
 *         @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class OpenCase extends CaseManagerCommand {

    protected static final String	name        = "OpenCase";
    protected static final int		mnemonicKey = KeyEvent.VK_O;
	String	defaultChooserPath = CaseManagerModel.defaultChooserPath;


    /**
     */
    private CaseManager	controller;

    /**
     * Constructs a realization of AbstractCommand.
     *
     * @param listener
     * @param controller TODO
     */
    public OpenCase(CaseManager controller, ActionListener listener) {
        super(listener, name);
        this.controller  = controller;
        super.mnemonicKey = mnemonicKey;
        update();
    }

    /**
     * Performs the command operations when called by execute().
     * @return
     */
    @Override
    public boolean perfomCommand() {
        boolean				canProceed       = canExecute(); // && !getModel().isBusy();

        
        CaseModel newCase = null;
		try {
			newCase = getModel().getNewCase();
		} catch (UnexpectedException e) {
//			IJ.showMessage(e.getMessage());
			e.printStackTrace();
		}
		GrasppeKit.debugText("Open Case Attempt", "Call SelectCaseFolder", 3);
        
        while (canProceed && !newCase.filesLoaded) {

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
	        canProceed = ((CloseCase)this.controller.getCommandHandler().getCommand(
	            "CloseCase")).quickClose(getKeyEvent());
	        if (!canProceed) break;		// Alternative scenario succeeded
	
	        newCase.path       = selectCaseFolder.getSelectedFile().getAbsolutePath();
	        
	        try {
	        	getCaseManager().loadCase(newCase);
	        } catch (FileNotFoundException e) {
                canProceed = IJ.showMessageWithCancel(name,
	                    "The selected case folder is missing some files.\n\n" +
	                    e.getMessage() +"\n\n" +
	                    		"Do you want to select another folder?");
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
        }
        
        if (canProceed && newCase!=null && newCase.filesLoaded){
        	try {
				getModel().promoteNewCase();
			} catch (UnexpectedException e) {
//				IJ.showMessage(e.getMessage());
				e.printStackTrace();
			}
            GrasppeKit.debugText("Open Case Success",
                    "Created new CaseModel for " + getModel().getCurrentCase().title
                    + " and reorganize cases in the case manager model.", 3);
        } else {
            GrasppeKit.debugText("Open Case Unsuccessful",
                    "New case was not created... Reverting cases in the case manager model.", 3);
            try {
            	getModel().discardNewCase();       	
			} catch (UnexpectedException e) {
//				IJ.showMessage(e.getMessage());
				e.printStackTrace();
			}
            
            try {
            	getModel().rollBackCurrentCase();
			} catch (UnexpectedException e) {
//				IJ.showMessage(e.getMessage());
				e.printStackTrace();
			}
        }

        getModel().notifyObservers();
        return canExecute(true);
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        super.update();
        
//        if (getModel().isBusy()) return;
        
        canExecute(true);		// getModel().hasCurrentCase());
    }

    /**
     * @return the controller
     */
    public CaseManager getCaseManager() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setCaseManager(CaseManager caseManager) {
        this.controller = caseManager;
    }

//  /**
//   * Method description
//   */
//  public void execute() {
//      super.execute();
//      if (!canExecute()) return;
//
//      // TODO: Show caseChooser, then confirm and close case before opening chosen case
//      // TODO: Show caseFolderChooser, if can create open case, confirm and close case before opening the new case
//  }
}
