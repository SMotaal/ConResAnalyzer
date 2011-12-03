/*
 * @(#)CaseManagerModel.java   11/11/27
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.cases.model;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.lure.components.AbstractModel;

//~--- JDK imports ------------------------------------------------------------

import java.rmi.UnexpectedException;

/**
 * Class description
 *
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CaseManagerModel extends AbstractModel {

    /** Field description */
    public static String	defaultChooserPath =
        "/Users/daflair/Documents/data/conres/Approval_Scans_ConRes26_FS";

    /** Holds a case that is about to close until currentCase is not null */
    protected CaseModel	backgroundCase = null;

    /** Field description */
    protected CaseModel	currentCase = null;

    /** Field description */
    protected CaseModel	newCase = null;
//    protected boolean	locked  = false;

    /**
     * Constructs a new model with a predefined controller.
     *
     * @param controller
     */
    public CaseManagerModel(CaseManager controller) {
        super(controller);
    }

    /**
     * @throws UnexpectedException
     */
    public void backgroundCurrentCase() throws UnexpectedException {

//      if (newCase==null)
//          currentCase = null;
        try {
            if ((backgroundCase == null) && (currentCase != null)) backgroundCase = currentCase;
        } catch (Exception exception) {}

//      else
//          throw new UnexpectedException(
//              "Case manager could not background the current case case since it does not exist.");

        currentCase = null;
        notifyObservers();
        getController().backgroundCurrentCase();

    }

    /**
     *  @return
     */
    public boolean canGetNewCase() {
        return ((backgroundCase == null) || (currentCase == null));
    }

    /**
     *  @throws UnexpectedException
     */
    public void discardBackgroundCase() throws UnexpectedException {

//      if (newCase == null)  // return;
//          throw new UnexpectedException(
//              "Case manager could not discard a new case since it does not exist.");
        // if (backgroundCase != null) currentCase = backgroundCase;

//      newCase        = null;
        backgroundCase = null;
        getController().discardBackgroundCase();
        notifyObservers();
    }

//    /**
//     *  @return
//     */
//    protected boolean lock() {
//        return locked = true;
//    }

    /**
     *  @throws UnexpectedException
     */
    public void promoteNewCase() throws UnexpectedException {

        // TODO: Wrong call sequence, must first getNewCase()
        if (newCase == null)
            throw new UnexpectedException(
                "Case manager could not promote a new case since it does not exist.");

        // TODO: How should we recover from this?
        if ((backgroundCase != null) && (currentCase != null))
            throw new UnexpectedException(
                "Case manager is mulfunctioning and cannot promote a newcase when has both a background case and a current case.");
        currentCase    = newCase;
        newCase        = null;
        backgroundCase = null;		// if (backgroundCase!=null)
        getController().discardBackgroundCase();
        notifyObservers();
    }

    /**
     *  @throws UnexpectedException
     */
    public void restoreBackgroundCase() throws UnexpectedException {
        if (backgroundCase != null) {

            // TODO: How should we recover from this?
            if ((backgroundCase != null) && (currentCase != null))
                throw new UnexpectedException(
                    "Case manager is mulfunctioning and cannot rollback the current case since it has both a background case and a current case.");

            currentCase    = backgroundCase;
            newCase        = null;
            backgroundCase = null;
        }

        getController().restoreBackgroundCase();
        notifyObservers();
    }

//    /**
//     *  @return
//     */
//    protected boolean unlock() {
//        return locked = false;
//    }

    /**
     * @return the backgroundCase
     */
    public CaseModel getBackgroundCase() {
        return backgroundCase;
    }

    /**
     *  @return
     */
    private CaseManager getController() {
        return (CaseManager)controller;
    }

    /**
     * @return the currentCase
     */
    public CaseModel getCurrentCase() {
        return currentCase;
    }

    /**
     * @return
     * @throws UnexpectedException
     */
    public CaseModel getNewCaseModel() throws UnexpectedException {
        if (!canGetNewCase())		// (backgroundCase != null) && (currentCase != null))
            throw new UnexpectedException(
                "Case manager could not create a new case since it already has both a background case and a current case.");

        newCase = new CaseModel();
        notifyObservers();

        return newCase;
    }
    
    public CaseModel getNewCase() {
    	return newCase;
    }

    /**
     * Method description
     *
     * @return
     */
    public boolean hasCurrentCase() {
    	

        // if (currentCase != null) GrasppeKit.debugText("Current Case", currentCase.toString());
        return (currentCase != null);

//      if (backgroundCase != null) GrasppeKit.debugText("Current Case", backgroundCase.toString());
//      else GrasppeKit.debugText("Current Case", "null!");
//
//      return ((currentCase != null) | ((currentCase != null) && (backgroundCase != null)));
    }

    /**
     *  @return
     */
    public boolean isBusy() {
        return false;

//      return ((newCase != null) && (backgroundCase != null));
    }

//    /**
//     * @return the locked
//     */
//    public boolean isLocked() {
//        return locked;
//    }
}
