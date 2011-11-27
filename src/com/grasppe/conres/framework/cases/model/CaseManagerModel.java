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
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.rmi.UnexpectedException;

/**
 * Class description
 *
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CaseManagerModel extends AbstractModel {

    /**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}
	
	protected boolean lock() {
		return locked  = true;
	}
	
	protected boolean unlock() {
		return locked  = false;
	}

	/** Field description */
    public static String	defaultChooserPath =
        "/Users/daflair/Documents/data/conres/Approval_Scans_ConRes26_FS";

    /** Holds a case that is about to close until currentCase is not null */
    protected CaseModel	backgroundCase = null;

    /** Field description */
    protected CaseModel	currentCase = null;

    /** Field description */
    protected CaseModel	newCase = null;
    
    protected boolean locked = false;

    /**
     * Constructs a new model object with no predefined controller.
     */
    public CaseManagerModel() {
        super();
    }

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
        if ((backgroundCase == null) && (currentCase != null)) backgroundCase = currentCase;
        else  throw new UnexpectedException(
                "Case manager could not background the current case case since it does not exist.");

        currentCase = null;
        notifyObservers();

    }

    /**
     *  @throws UnexpectedException
     */
    public void discardNewCase() throws UnexpectedException {
        if (newCase == null)	// return;
            throw new UnexpectedException(
                "Case manager could not discard a new case since it does not exist.");
        if (backgroundCase != null) currentCase = backgroundCase;

        newCase        = null;
        backgroundCase = null;
        notifyObservers();
    }

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
        backgroundCase = null;		// if (backgroundCase!=null)
        notifyObservers();
    }

    /**
     * 	@throws UnexpectedException
     */
    public void rollBackCurrentCase() throws UnexpectedException {
        if (backgroundCase == null) return;

        // TODO: How should we recover from this?
        if (backgroundCase != null && currentCase != null)
            throw new UnexpectedException(
                "Case manager is mulfunctioning and cannot rollback the current case since it has both a background case and a current case.");

        currentCase    = backgroundCase;
        backgroundCase = null;
        notifyObservers();
    }

    /**
     * @return the backgroundCase
     */
    public CaseModel getBackgroundCase() {
        return backgroundCase;
    }

    /**
     * @return the currentCase
     */
    public CaseModel getCurrentCase() {
        return currentCase;
    }

    /**
     * Method description
     *
     * @return
     * @throws UnexpectedException
     */
    public CaseModel getNewCase() throws UnexpectedException {
    	if ((backgroundCase != null) && (currentCase != null))
    		throw new UnexpectedException(
    				"Case manager could not create a new case since it already has both a background case and a current case.");

    	newCase = new CaseModel();
        notifyObservers();
        return newCase;
    }

    /**
     * Method description
     *
     * @return
     */
    public boolean hasCurrentCase() {
        if (currentCase != null) GrasppeKit.debugText("Current Case", currentCase.toString());
        if (backgroundCase != null) GrasppeKit.debugText("Current Case", backgroundCase.toString());
        else GrasppeKit.debugText("Current Case", "null!");

        return ((currentCase != null) | (currentCase != null && backgroundCase != null));
    }

    /**
     *  @return
     */
    public boolean isBusy() {
    	return false;
//        return ((newCase != null) && (backgroundCase != null));
    }
}
