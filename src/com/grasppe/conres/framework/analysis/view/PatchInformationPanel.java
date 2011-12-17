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
import com.grasppe.conres.framework.targets.model.grid.ConResPatch;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/08
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PatchInformationPanel extends PatchBoundView {
	
	JLabel positionLabel = null;

//    int	patchWidth  = 100,
//		patchHeight = 100;

    /**
     * Create the panel with a model.
     *  @param model
     */
    public PatchInformationPanel(AnalysisStepperModel model) {
        super(model);
        prepareView();

    }
    
    public void prepareView() {
    	setLayout(new BorderLayout());
    	
    	positionLabel = new JLabel();
    	positionLabel.setVerticalTextPosition(JLabel.TOP);
    	positionLabel.setVerticalAlignment(JLabel.TOP);
    	positionLabel.setHorizontalAlignment(JLabel.LEFT);
    	positionLabel.setHorizontalTextPosition(JLabel.LEFT);
        positionLabel.setPreferredSize(new Dimension(200, 200)); //getHeight()));
//    	positionLabel.setMaximumSize(positionLabel.getPreferredSize());
//    	positionLabel.setMinimumSize(positionLabel.getPreferredSize());
//    	setMaximumSize(new Dimension(200, 200));
    	positionLabel.setFont(positionLabel.getFont().deriveFont(14.0F));
    	
    	positionLabel.setLocation(0, 0);
    	add(positionLabel,BorderLayout.NORTH);
    }
//
//        try {
//            g.drawImage(getModel().getPatchImage(), 0, 0, patchWidth, patchHeight, this);
//        } catch (Exception exception) {
//            return;
//        }
//    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.framework.analysis.view.PatchBoundView#updateSize()
     */
    @Override
    public void updateView() {
    	try {
    		ConResPatch activePatch = getModel().getActivePatch();
    		
    		if (positionLabel!=null) {
    			String newString = activePatch.htmlString();
    			positionLabel.setText(newString);
    		}
    		
    	} catch (Exception exception) {
    		positionLabel.setText("");
    	}
    	super.updateView();
    }
}
