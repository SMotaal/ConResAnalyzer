/*
 * @(#)AnalyzeBlock.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis.operations;

import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.analysis.AnalysisStepper;
import com.grasppe.conres.framework.analysis.model.AnalysisManagerModel;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.lure.framework.FloatingAlert;

//~--- JDK imports ------------------------------------------------------------

import ij.IJ;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.text.DefaultEditorKit.BeepAction;

import org.apache.commons.io.FilenameUtils;

/**
 *         Defines Case Manager's New Case actions and command, using the EAC pattern.
 *         @version        $Revision: 1.0, 11/11/08
 *         @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ExportAnalysis extends AnalysisCommand {



	protected static final String	name        = "ExportAnalysis";
    protected static final int		mnemonicKey = KeyEvent.VK_X;
    protected static final String description = "Export the current analysis grid data to a CSV file with the same name as the block image file.";
    protected static final String type = "file";
    /**
	 * @return the commandMenu
	 */
	public String getMenuKey() {
		return type;
	}


    /**
     */
    private AnalysisManager	controller;

    /**
     * Constructs a realization of AbstractCommand.
     * @param listener
     * @param controller 
     */
    public ExportAnalysis(AnalysisManager controller, ActionListener listener) {
        super(listener, name);
        this.controller   = controller;
        super.mnemonicKey = mnemonicKey;
        super.description = description;
//        controller.getModel().attachObserver(this);
        update();
    }

    /**
     * Performs the command operations when called by execute().
     * @return
     */
    @Override
    public boolean perfomCommand() {
        boolean			canProceed      = canExecute();

        try {
	        String filename = controller.getTargetManager().generateFilename("a.csv","Data");
	        if (!controller.getAnalysisStepper().getModel().getBlockState().isValid() && !IJ.showMessageWithCancel(name, "Analysis incomplete.\n\n" +
	        		"Do you want to export the block?"))
	        	return true;
	        
        	controller.getAnalysisStepper().getModel().getBlockState().writeFile(filename);
	        new FloatingAlert("Export Complete!").flashView(500);
	        controller.getAnalysisStepper().getModel().setModified(false);
        } catch (Exception exception) {
//        	exception.printStackTrace();
        	Toolkit.getDefaultToolkit().beep();
        }

        return true;
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    @Override
    public void update() {
        canExecute(canExportAnalysis());		// canMarkBlocks());       // getModel().hasCurrentCase());
        super.update();
    }
    
    /**
     *  @return
     */
    protected boolean canExportAnalysis() {
        try {
//          ConResBlock	block = getModel().getActiveBlock();
//        	return block != null;
            return controller.getAnalysisStepper().getModel().getBlockState()!=null;
        } catch (Exception exception) {
            return false;
        }

    }
    
    /**
     * Returns a correctly-cast model.
     * @return
     */
    public AnalysisManagerModel getModel() {
        return controller.getModel();
    }    
}
