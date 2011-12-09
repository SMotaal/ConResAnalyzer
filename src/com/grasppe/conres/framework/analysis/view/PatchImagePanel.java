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
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/08
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PatchImagePanel extends JPanel implements Observer {

    /* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			getModel().detachObserver(this);
		} catch (Exception exception) {
			super.finalize();
		}
		super.finalize();
	}

	AnalysisStepperModel	model       = null;
    int						patchWidth  = 100,
							patchHeight = 100;

    /**
     * Create the panel.
     */
    public PatchImagePanel() {
        //setBackground(Color.GREEN);
        updateSize();
//        setLayout(new BorderLayout(0, 0));
    }

    /**
     * Create the panel with a model.
     *  @param model
     */
    public PatchImagePanel(AnalysisStepperModel model) {
        this();
        attachModel(model);
    }

    /**
     *  @param model
     */
    public void attachModel(AnalysisStepperModel model) {
//        model.attachObserver(this);
        this.model = model;
        update();
    }

    /**
     *  @param g
     */
    public void paint(Graphics g) {
    	super.paint(g);
        try {
            g.drawImage(getModel().getPatchImage(),0,0, patchWidth, patchHeight, this);
        } catch (Exception exception) {
        	return;
        }
    }

    /**
     */
    public void update() {
        updateSize();
        this.repaint(1000);
    }

    /**
     */
    public void updateSize() {
        try {
            BufferedImage	patchImage  = getModel().getPatchImage();
            int				patchWidth  = patchImage.getWidth(),
							patchHeight = patchImage.getHeight();

            if (!((this.patchWidth != patchWidth) || (this.patchHeight != patchHeight))) return;

            this.patchWidth  = patchWidth;
            this.patchHeight = patchHeight;

        } catch (NullPointerException exception) {
        	GrasppeKit.debugText("Update Patch Image Size", exception.getMessage(), 2);
            // There is no patchImage
        }

        setPreferredSize(new Dimension(this.patchWidth, this.patchHeight));
        setMinimumSize(new Dimension(this.patchWidth, this.patchHeight));
        setSize(new Dimension(this.patchWidth, this.patchHeight));
    }

    /**
     *  @return
     */
    protected AnalysisStepperModel getModel() {
    	if (model!=null)
    		return model;
        return model;
    }
    
    
}
