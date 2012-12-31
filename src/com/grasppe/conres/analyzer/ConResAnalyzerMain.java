/*
 * @(#)ConResAnalyzerMain.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer;

import java.util.Observable;
import java.util.Observer;

import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.conres.analyzer.view.ConResAnalyzerView;
import com.grasppe.conres.framework.cases.operations.OpenCase;
import com.grasppe.conres.io.model.CaseFolder;
import com.grasppe.lure.components.ObservableObject;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/03
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ConResAnalyzerMain {

  static ConResAnalyzer      analyzer;
  static ConResAnalyzerModel analyzerModel;
  static ConResAnalyzerView  analyzerView;

  /**
   * 	@param analyzer
   */
  public static void initializeTest(ConResAnalyzer analyzer) {
    CaseFolder caseFolder = new CaseFolder("/Users/daflair/Documents/Data/ConRes/ConRes-Approval-SA-0600");

//  new CaseFolder("/Users/daflair/Documents/Data/ConRes/Approval_Scans_ConRes26_FS");

    ((OpenCase)analyzer.getCaseManager().getCommand("OpenCase")).openCase(caseFolder);

    // analyzer.targetManager.loadImage();

//  ((MarkBlock)analyzer.getTargetManager().getCommand("MarkBlock")).execute();
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

//  //        public static void main(String[] args) throws UnsupportedLookAndFeelException {
//            JFrame frame = new JFrame();
//            System.setProperty("apple.awt.fileDialogForDirectories", "true");
//            FileDialog d = new FileDialog(frame);
//            d.setVisible(true);
//  //        }

    // Thread   runningThread =
    new Thread() {

      public void run() {
        GrasppeKit.setupHooks();

//      new ConResBootCamp().run("");
        analyzer      = new ConResAnalyzer();
        analyzerModel = analyzer.getModel();
        analyzerView  = (ConResAnalyzerView)analyzer.getView();			// new ConResAnalyzerView(analyzer);
               
        com.grasppe.lure.framework.GrasppeKit.Observer instanceObserver = new com.grasppe.lure.framework.GrasppeKit.Observer() {

			/* (non-Javadoc)
			 * @see com.grasppe.lure.framework.GrasppeKit.Observer#update()
			 */
			@Override
			public void update() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void detatch(
					com.grasppe.lure.framework.GrasppeKit.Observable oberservableObject) {
				//System.exit(0);
				
			}

		};
		
        analyzer.attachObserver(instanceObserver);
        
        if (!GrasppeKit.isRunningJar()) initializeTest(analyzer);
        
		
      }
    }.start();

  }

  /**
   * @return the analyzer
   */
  public static ConResAnalyzer getAnalyzer() {
    return analyzer;
  }

  /**
   * @return the analyzerModel
   */
  public static ConResAnalyzerModel getAnalyzerModel() {
    return analyzerModel;
  }

  /**
   * @return the analyzerView
   */
  public static ConResAnalyzerView getAnalyzerView() {
    return analyzerView;
  }
}
