/*
 * @(#)TargetManagerView.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets.view;

import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractView;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class TargetManagerView extends AbstractView {

    /**
     * @param controller
     */
    public TargetManagerView(AbstractController controller) {
        super(controller);
    }

    /**
     */
    @Override
    protected void updateDebugLabels() {
        TargetManagerModel	model = getModel();

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

//      updateDebugLabel("newCase", model.getNewCase());
//      updateDebugLabel("backgroundCase", model.getBackgroundCase());

        super.updateDebugLabels();
    }

    /**
     *  @return
     */
    @Override
    protected TargetManagerModel getModel() {
        return (TargetManagerModel)getControllerModel();
    }
}
