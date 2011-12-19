/*
 * @(#)PatchBoundView.java   11/12/09
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.cases.view;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.framework.analysis.model.AnalysisManagerModel;
import com.grasppe.conres.framework.analysis.model.AnalysisStepperModel;
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.KeyCode;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Class description
 * 	@version        $Revision: 1.0, 11/12/09
 * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
 */
public class CaseView extends JPanel implements Observer {
	
	
	/**
	 * @return the controller
	 */
	public CaseManager getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(CaseManager controller) {
		this.controller = controller;
    	attachModel(controller.getModel());
    	getTargetModel().attachObserver(this);
	}

	protected static String CONTROL_SYMBOL = GrasppeKit.getControlSymbol();
	
	CaseManagerModel model = null;
	
	JLabel caseLabel = null,
			blockLabel = null,
			buildLabel = null;
	
	static float labelFontSize = 10F;
	
//	String defaultCaseText = "<html><b>Tags</b></html>";
	String defaultCaseText = "<html><<pre style='font-family: Sans-Serif;'>Press <i>" + CONTROL_SYMBOL + "+O</i> to open a case</pre></html>";
//	String defaultBlockText = "<html>Press <i>" + CONTROL_SYMBOL + "+B</i> to select target blocks</html>";
//	String defaultCaseText = "<html><b>"
	String defaultBuildText = "<html><pre style='font-family: Sans-Serif;'><b style='color:black'>Build: </b>" + ConResAnalyzer.BUILD + "   </pre></html>";
	
	static int sidePadding = 3,
			topPadding = 0,
			bottomPadding=0;
	
	static Color labelTextColor  = Color.DARK_GRAY;
	static Color labelColor  = Color.DARK_GRAY;


    /**
     */
    public CaseView() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(getWidth(),25));       
        
        createView();
        
        update();
    }
    
    public void createView() {

    	if (caseLabel!=null) return;

    	setBackground(Color.WHITE);

    	caseLabel = new JLabel(defaultCaseText); //getModel().getCurrentCase().title);
        formatLabel(caseLabel);
        add(caseLabel,BorderLayout.CENTER);
        
//        blockLabel = new JLabel(defaultBlockText);
//        formatLabel(blockLabel);
//        add(blockLabel,BorderLayout.EAST);

        buildLabel = new JLabel(defaultBuildText);
        formatLabel(buildLabel);
        add(buildLabel,BorderLayout.EAST);
       
        
        super.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
    }
    
    protected void formatLabel(JLabel label) {
        label.setForeground(labelTextColor);
        label.setBackground(getBackground());
//        label.setFont(label.getFont().deriveFont(labelFontSize));
        label.setBorder(new EmptyBorder(topPadding,sidePadding,bottomPadding,sidePadding));
    }

    /**
     * Create the panel with a model.
     *  @param model
     */
    public CaseView(CaseManager controller) {
    	this();
    	setController(controller);
    }
    
    protected CaseManager controller = null;
    
    protected TargetManagerModel getTargetModel() {
    	return getController().getTargetManager().getModel();
    }    
    
    
    /**
     * Create the panel with a model.
     *  @param model
     */
    private CaseView(CaseManagerModel model) {
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
    	
    	String labelText = "<html><pre style='font-family: Sans-Serif;'>";
    	try {
//    		blockLabel.setText("<html><b style='color:black'>Block: </b>" + getTargetModel().getActiveBlock().getZValue().value + "% Reference Tone</html>");
    		labelText+="<b style='color:black'>Block: </b>" + getTargetModel().getActiveBlock().getZValue().value + "% Reference Tone\t\t";
    	} catch (Exception exception) {
//    		blockLabel.setText(defaultBlockText);
    	}    	
    	try {
//    		caseLabel.setText("<html><b style='color:black'>Case:</b> " + getModel().getCurrentCase().title + " (<i>" + getModel().getCurrentCase().targetDefinitionFile.getName() + "</i>)</html>");
    		labelText+="<b style='color:black'>Case:</b> " + getModel().getCurrentCase().title + " (<i>" + getModel().getCurrentCase().targetDefinitionFile.getName().trim() + "</i>)";
    	} catch (Exception exception) {
    		caseLabel.setText(defaultCaseText);
    		return;
    	}
    	labelText+="</pre></html>";
    	caseLabel.setText(labelText);
//    	caseLabel.setFont(caseLabel.getFont().deriveFont(labelFontSize));
    	caseLabel.updateUI();
//    	blockLabel.updateUI();
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
