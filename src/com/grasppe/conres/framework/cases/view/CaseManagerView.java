/*
 * @(#)CaseManagerView.java   11/12/02
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.cases.view;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Class description
 *
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CaseManagerView extends AbstractView implements Observer {

    static String	separator            = ": ";
    static String	frameString          = "Case Manager";
    static String	currentCaseString    = "";
    static String	newCaseString        = "newCase";
    static String	backgroundCaseString = "backgroundCase";
    static String	lockedString         = "locked";
    JFrame			frame;		// = new JFrame(frameString);
    JLabel			currentCaseLabel, newCaseLabel,	backgroundCaseLabel, lockedLabel;

//  JLabel    currentCaseLabel     = new JLabel(currentCaseString + separator);
//  JLabel    newCaseLabel         = new JLabel(newCaseString + separator);
//  JLabel    backgroundCaseLabel  = new JLabel(backgroundCaseString + separator);
//  JLabel    lockedLabel          = new JLabel(lockedString + separator);

//    boolean   isPrepared = false;

    /**
     * Constructs a new ConResAnalyzerView with a predefined controller.
     *
     * @param controller
     */
    public CaseManagerView(CaseManager controller) {
        super(controller);
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractView#prepareDebugView()
     */

    /**
     */
    @Override
    protected void prepareDebugView() {

        if (debugPanel != null) return;

        createDebugView();

        createDebugLabel("currentCase");
        createDebugLabel("newCase");
        createDebugLabel("backgroundCase");
        createDebugLabel("Observers");
//        createDebugLabel("locked");

//      currentCaseLabel    = new JLabel(currentCaseString + separator);
//      newCaseLabel        = new JLabel(newCaseString + separator);
//      backgroundCaseLabel = new JLabel(backgroundCaseString + separator);
//      lockedLabel         = new JLabel(lockedString + separator);
//
//      debugPanel.add(currentCaseLabel);
//      debugPanel.add(newCaseLabel);
//      debugPanel.add(backgroundCaseLabel);
//      debugPanel.add(lockedLabel);

        super.prepareDebugView();

    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractView#updateDebugLabels()
     */

    /**
     */
    @Override
    protected void updateDebugLabels() {
        CaseManagerModel	model = getModel();

        if (model == null) return;

        updateDebugLabel("currentCase", model.getCurrentCase());
        updateDebugLabel("newCase", model.getNewCase());
        updateDebugLabel("backgroundCase", model.getBackgroundCase());
        updateDebugLabel("Observers",model.observerString());
//        updateDebugLabel("locked", model.isLocked(), "Locked", "Unlocked");
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractView#updateDebugView()
     */

    /**
     *  @return
     */

//  @Override
//  protected void updateDebugView() {
//
//    CaseManagerModel    model = getModel();
//      if (model == null) return;
//      
//      updateDebugLabel("currentCase", currentCaseString + separator + model.getCurrentCase());
//      updateDebugLabel("newCase", newCaseString + separator + model.getNewCase());
//      updateDebugLabel("backgroundCase", backgroundCaseString + separator + model.getBackgroundCase());
//      updateDebugLabel("locked", (model.isLocked()) ? "Locked" : "Unlocked");
//
////      super.updateDebugView();
//  }

//  /**
//   */
//  public void prepareFrame() {
//  if (frame!=null) return;
//    frame = new JFrame(frameString);
//
//    Container pane = frame.getContentPane();
//    BoxLayout layout = new BoxLayout(pane, BoxLayout.Y_AXIS);
//    
//    frame.setLayout(layout);
//    frame.setAlwaysOnTop(true);
//    
//    frame.setLocation(0, 75);
//
//    currentCaseLabel    = new JLabel(currentCaseString + separator);
//    newCaseLabel        = new JLabel(newCaseString + separator);
//    backgroundCaseLabel = new JLabel(backgroundCaseString + separator);
//    lockedLabel         = new JLabel(lockedString + separator);
//
//    pane.add(currentCaseLabel);
//    pane.add(newCaseLabel);
//    pane.add(backgroundCaseLabel);
//    pane.add(lockedLabel);
//    isPrepared = true;
//    update();
//  }

//  /**
//   */
//  public void update() {
////    try {
////        CaseManagerModel model = getModel();
////        if (model==null) return;
////        currentCaseLabel.setText(currentCaseString + separator + model.getCurrentCase());
////        newCaseLabel.setText(newCaseString + separator + model.getNewCase());
////        backgroundCaseLabel.setText(backgroundCaseString + separator
////                                    + model.getBackgroundCase());
////        lockedLabel.setText((model.isLocked()) ? "Locked" : "Unlocked");
////
//////            frame.pack();
//////            if (frame.isVisible()) return;
//////
//////            frame.setVisible(true);
////    } catch (Exception exception) {
////        //if (isPrepared) return;
//////          exception.printStackTrace();
////        prepareFrame();
////    }
//  }

    /**
     *  @return
     */
    protected CaseManagerModel getModel() {
        return (CaseManagerModel)super.getModel();
    }
}
