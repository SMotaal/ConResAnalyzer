/*
 * @(#)PatchImagePanel.java   11/12/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.analysis.view;

import com.grasppe.conres.framework.analysis.model.AnalysisStepperModel;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/08
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PatchImagePanel extends PatchBoundView {

    int	patchWidth  = 550,
		patchHeight = 550;

    /**
     * Create the panel with a model.
     *  @param model
     */
    public PatchImagePanel(AnalysisStepperModel model) {
        super(model);
        patchWidth = getModel().getPatchPreviewSize();
        patchHeight = getModel().getPatchPreviewSize();
    }

    /**
     *  @param g
     */
    public void paint(Graphics g) {
        super.paint(g);

        try {
            g.drawImage(getModel().getPatchImage(), 0, 0, patchWidth, patchHeight, this);
        } catch (Exception exception) {
            return;
        }
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.framework.analysis.view.PatchBoundView#updateSize()
     */

    /**
     */
    @Override
    public void updateSize() {
        super.updateSize();

        try {
//            BufferedImage	patchImage  = getModel().getPatchImage();
//            int				patchWidth  = patchImage.getWidth(),
//							patchHeight = patchImage.getHeight();
//
//            if (!((this.patchWidth != patchWidth) || (this.patchHeight != patchHeight))) return;
//
//            this.patchWidth  = patchWidth;
//            this.patchHeight = patchHeight;
        } catch (NullPointerException exception) {
            GrasppeKit.debugError("Updating Patch Preview Size", exception, 5);
        }

        setPreferredSize(new Dimension(this.patchWidth, this.patchHeight));
        setMinimumSize(new Dimension(this.patchWidth, this.patchHeight));
        setSize(new Dimension(this.patchWidth, this.patchHeight));
    }
}
