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

        // TODO Auto-generated constructor stub
    }

    /**
     *  @return
     */
    @Override
    protected AnalysisManagerModel getModel() {
        return (AnalysisManagerModel)super.getControllerModel();
    }
}
