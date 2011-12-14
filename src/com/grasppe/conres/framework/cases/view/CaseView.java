/*
 * @(#)PatchBoundView.java   11/12/09
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.cases.view;

import com.grasppe.conres.framework.analysis.model.AnalysisStepperModel;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Class description
 * 	@version        $Revision: 1.0, 11/12/09
 * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
 */
public class CaseView extends JPanel implements Observer {
	
	CaseManagerModel model = null;
	
	JLabel caseLabel = null;
	
	String defaultText = "<html><b>ConResAnalyzer</b></html>";


    /**
     */
    public CaseView() {
        super(new BorderLayout());
//        DisplayMode	displayMode =
//	            GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]
//	                .getDisplayMode();
//	        int	displayWidth  = displayMode.getWidth();
//	        int	displayHeight = displayMode.getHeight();        
        setPreferredSize(new Dimension(getWidth(),25));
        
        caseLabel = new JLabel(defaultText); //getModel().getCurrentCase().title);
        
        caseLabel.setBackground(Color.WHITE);
        caseLabel.setBorder(new EmptyBorder(0,3,0,3));
        
        this.add(caseLabel);
        
        update();
    }

    /**
     * Create the panel with a model.
     *  @param model
     */
    public CaseView(CaseManagerModel model) {
        this();
        attachModel(model);
    }

    /**
     *  @param model
     */
    public void attachModel(CaseManagerModel model) {
        this.model = model;
        model.attachObserver(this);
        update();
    }

    /**
     * 	@throws Throwable
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

    /**
     */
    public void update() {
        updateSize();
        updateView();
    }

    /**
     */
    public void updateSize() {}

    /**
     */
    public void updateView() {
//    	if (getModel()!= null && getModel().getCurrentCase()!=null && getModel().getCurrentCase().title!=null)
    	try {
    		caseLabel.setText("<html><b>Active Case:</b> " + getModel().getCurrentCase().title + "</html>");
//    	else
    	} catch (Exception exception) {
    		caseLabel.setText(defaultText);
    	}
    	
    	caseLabel.updateUI();
    	repaint();
    }

    /**
     *  @return
     */
    protected CaseManagerModel getModel() {
        if (model != null) return model;

        return model;
    }
}
