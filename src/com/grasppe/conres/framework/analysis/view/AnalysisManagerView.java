/*
 * @(#)AnalysisManagerView.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis.view;

import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.analysis.model.AnalysisManagerModel;
import com.grasppe.lure.components.AbstractView;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AnalysisManagerView extends AbstractView {

    /**
     * @param controller
     */
    public AnalysisManagerView(AnalysisManager controller) {
        super(controller);
    }

    /**
     */
    @Override
    protected void updateDebugLabels() {
        AnalysisManagerModel	model = getModel();

        if (model == null) return;

        try {
            updateDebugLabel("activeTarget", model.getActiveTarget());
        } catch (Exception exception) {
            updateDebugLabel("activeTarget", "");
        }

        try {
            updateDebugLabel("activeBlock", model.getActiveBlock());
        } catch (Exception exception) {
            updateDebugLabel("activeBlock", "");
        }

        super.updateDebugLabels();
    }

    /**
     *  @return
     */
    @Override
    public AnalysisManagerModel getModel() {
        return (AnalysisManagerModel)getControllerModel();
    }

	@Override
	protected void finalizeUpdates() {
		// TODO Auto-generated method stub
		
	}
}
