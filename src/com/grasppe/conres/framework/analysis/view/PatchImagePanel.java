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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/08
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PatchImagePanel extends PatchBoundView implements ComponentListener {

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
        
        this.addComponentListener(this);
        
        setBackground(Color.GRAY);
        update();
    }

    /**
     *  @param g
     */
    public void paint(Graphics g) {
        super.paint(g);

        try {
        	g.setColor(getBackground());
        	g.drawRect(0, 0, getWidth(), getHeight());
        	g.fillRect(0, 0, getWidth(), getHeight());
        } catch (Exception exception) {
        	return;
        }
        try {
        	AnalysisStepperModel model = getModel();
            g.drawImage(getModel().getPatchImage(), 0, 0, patchWidth, patchHeight, this);
        } catch (Exception exception) {
//        	getModel().getController().updatePatchPreviews();
//	    	  int delay = 1000; //milliseconds
//	    	  ActionListener taskPerformer = new ActionListener() {
//	    	      public void actionPerformed(ActionEvent evt) {
//	    	          repaint();
//	    	      }
//	    	  };
//        	  new Timer(delay, taskPerformer).start();
//            return;
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

	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void componentShown(ComponentEvent arg0) {
		update();
	}
}
