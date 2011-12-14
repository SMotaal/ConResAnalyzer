/*
 * @(#)CaseManagerView.java   11/12/02
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.cases.view;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Class description
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CaseManagerView extends AbstractView implements Observer {

    static String	separator            = ": ";
    static String	frameString          = "Case Manager";
    static String	currentCaseString    = "";
    static String	newCaseString        = "newCase";
    static String	backgroundCaseString = "backgroundCase";
    static String	lockedString         = "locked";
    JFrame			frame;		// = new JFrame(frameString);
    JLabel			currentCaseLabel, newCaseLabel,	backgroundCaseLabel, lockedLabel;

    /**
     * Constructs a new ConResAnalyzerView with a predefined controller.
     * @param controller
     */
    public CaseManagerView(CaseManager controller) {
        super(controller);
    }

    /**
     */
    @Override
    protected void updateDebugLabels() {
        CaseManagerModel	model = getModel();

        if (model == null) return;

        updateDebugLabel("currentCase", model.getCurrentCase());
        updateDebugLabel("newCase", model.getNewCase());
        updateDebugLabel("backgroundCase", model.getBackgroundCase());

        super.updateDebugLabels();
    }

    /**
     *  @return
     */
    public CaseManagerModel getModel() {
        return (CaseManagerModel)super.getControllerModel();
    }

	@Override
	protected void finalizeUpdates() {
		// TODO Auto-generated method stub
		
	}
}
