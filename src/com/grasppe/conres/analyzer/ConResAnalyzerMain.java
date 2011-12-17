/*
 * @(#)ConResAnalyzerMain.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer;

import java.awt.FileDialog;

import javax.swing.JFrame;

import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.conres.analyzer.view.ConResAnalyzerView;
import com.grasppe.conres.framework.cases.operations.OpenCase;
import com.grasppe.conres.io.model.CaseFolder;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/03
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ConResAnalyzerMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	
////    	public static void main(String[] args) throws UnsupportedLookAndFeelException {
//    	    JFrame frame = new JFrame();
//    	    System.setProperty("apple.awt.fileDialogForDirectories", "true");
//    	    FileDialog d = new FileDialog(frame);
//    	    d.setVisible(true);
////    	}

    	// Thread	runningThread = 
    	new Thread() {
			
			public void run() {
                GrasppeKit.setupHooks();
                


//              new ConResBootCamp().run("");
                ConResAnalyzer		analyzer      = new ConResAnalyzer();
                ConResAnalyzerModel	analyzerModel = analyzer.getModel();
                ConResAnalyzerView	analyzerView  = (ConResAnalyzerView)analyzer.getView();		// new ConResAnalyzerView(analyzer);
                
                if(!GrasppeKit.isRunningJar()) initializeTest(analyzer);
				
			}
		}.start();

    }
    
    public static void initializeTest(ConResAnalyzer analyzer) {
        CaseFolder	caseFolder =
                new CaseFolder("/Users/daflair/Documents/Data/ConRes/ConRes-Approval-0600");
//        		new CaseFolder("/Users/daflair/Documents/Data/ConRes/Approval_Scans_ConRes26_FS");
        
            ((OpenCase)analyzer.getCaseManager().getCommand("OpenCase")).openCase(caseFolder);
            
            //analyzer.targetManager.loadImage();
            
//            ((MarkBlock)analyzer.getTargetManager().getCommand("MarkBlock")).execute();
    }
}
