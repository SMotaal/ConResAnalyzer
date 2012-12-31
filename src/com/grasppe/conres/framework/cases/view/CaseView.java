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
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.conres.framework.cases.model.CaseModel;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.io.model.TargetDefinitionFile;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractModel;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/09
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CaseView extends JPanel implements Observer {

  protected static String CONTROL_SYMBOL = GrasppeKit.getControlSymbol();
  static float            labelFontSize  = 10F;
  static int              sidePadding    = 3,
                          topPadding     = 0,
                          bottomPadding  = 0;
  static Color            labelTextColor = Color.DARK_GRAY;
  static Color            labelColor     = Color.DARK_GRAY;
  CaseManagerModel        model          = null;
  JLabel                  caseLabel      = null,
                          blockLabel     = null,
                          buildLabel     = null;
  private boolean         isMac          = GrasppeKit.OperatingSystem.isMac();

//String defaultCaseText = "<html><b>Tags</b></html>";
  String defaultCaseText = "<html><<pre style='font-family: Sans-Serif;'>Press <i>" + CONTROL_SYMBOL
                           + "+O</i> to open a case</pre></html>";

//String defaultBlockText = "<html>Press <i>" + CONTROL_SYMBOL + "+B</i> to select target blocks</html>";
//String defaultCaseText = "<html><b>"
  String defaultBuildText = "<html><pre style='font-family: Sans-Serif;'><b style='color:black'>Build: </b>"
                            + ConResAnalyzer.BUILD + "   </pre></html>";
  protected CaseManager controller = null;

  /**
   */
  public CaseView() {
    super(new BorderLayout());
    setPreferredSize(new Dimension(getWidth(), 25));

    createView();

    update();
  }

  /**
   * Create the panel with a model.
   *    @param controller
   */
  public CaseView(CaseManager controller) {
    this();
    setController(controller);
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
   */
  public void createView() {

    if (caseLabel != null) return;

    setBackground(Color.WHITE);

    caseLabel = new JLabel(defaultCaseText);		// getModel().getCurrentCase().title);
    formatLabel(caseLabel);
    add(caseLabel, BorderLayout.CENTER);

//  blockLabel = new JLabel(defaultBlockText);
//  formatLabel(blockLabel);
//  add(blockLabel,BorderLayout.EAST);

    buildLabel = new JLabel(defaultBuildText);
    formatLabel(buildLabel);
    add(buildLabel, BorderLayout.EAST);

    super.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
  }

  /**
   *    @param oberservableObject
   */
  @Override
  public void detatch(Observable oberservableObject) {

    // TODO Auto-generated method stub

  }

  /**
   *  @throws Throwable
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
   *    @param label
   */
  protected void formatLabel(JLabel label) {
    label.setForeground(labelTextColor);
    label.setBackground(getBackground());

//  label.setFont(label.getFont().deriveFont(labelFontSize));
    label.setBorder(new EmptyBorder(topPadding, sidePadding, bottomPadding, sidePadding));
  }

  /**
   */
  public void update() {
    updateSize();
    updateView();
    updateWindowDocument();
  }

  /**
   */
  public void updateSize() {}

  /**
   */
  public void updateView() {

    setVisible(!(getTopLevelAncestor() instanceof JFrame) || ((JFrame) getTopLevelAncestor()).isUndecorated());

    String labelText = "<html><pre style='font-family: Sans-Serif;'>";

    try {

//    blockLabel.setText("<html><b style='color:black'>Block: </b>" + getTargetModel().getActiveBlock().getZValue().value + "% Reference Tone</html>");
      labelText += "<b style='color:black'>Block: </b>" + getTargetModel().getActiveBlock().getZValue().value
                   + "% Reference Tone\t\t";
    } catch (Exception exception) {

//    blockLabel.setText(defaultBlockText);
    }

    try {

//    caseLabel.setText("<html><b style='color:black'>Case:</b> " + getModel().getCurrentCase().title + " (<i>" + getModel().getCurrentCase().targetDefinitionFile.getName() + "</i>)</html>");
      labelText += "<b style='color:black'>Case:</b> " + getModel().getCurrentCase().title + " (<i>"
                   + getModel().getCurrentCase().targetDefinitionFile.getName().trim() + "</i>)";
    } catch (Exception exception) {
      caseLabel.setText(defaultCaseText);

      return;
    }

    labelText += "</pre></html>";
    caseLabel.setText(labelText);

//  caseLabel.setFont(caseLabel.getFont().deriveFont(labelFontSize));
    caseLabel.updateUI();

//  blockLabel.updateUI();
    repaint();
  }

  /**
   */
  public void updateWindowDocument() {
    if (!(getTopLevelAncestor() instanceof JFrame)) return;

    JFrame    frame            = (JFrame)getTopLevelAncestor();
    JRootPane root             = frame.getRootPane();

    String    applicationTitle = "ConResAnalyzer (" + ConResAnalyzer.BUILD + ")";

    if (!(getController() instanceof AbstractController) ||!(getModel() instanceof AbstractModel)) {
      frame.setTitle(applicationTitle);
      if (isMac) root.putClientProperty("Window.documentFile", null);

      return;
    }

    String    documentTitle  = "";

    CaseModel activeCase     = getModel().getCurrentCase();
    Boolean   isCaseActive   = activeCase != null;
    Boolean   isTargetActive = isCaseActive && (activeCase.targetDefinitionFile instanceof TargetDefinitionFile);
    Boolean   isBlockActive  = isTargetActive && (getTargetModel().getActiveBlock() instanceof ConResBlock);

    File      caseFile       = null;
    String    caseTitle      = null;
    String    targetTitle    = null;
    String    blockTitle     = null;

    if (isCaseActive) {
      caseFile  = getModel().getCurrentCase().caseFolder;
      caseTitle = getModel().getCurrentCase().title;

      if (isMac) root.putClientProperty("Window.documentFile", caseFile);

      documentTitle += caseTitle;

      if (isBlockActive) {
    	  blockTitle = getTargetModel().getActiveBlock().getZValue().value + "%";
    	  documentTitle += " (" + blockTitle + ")";
    	  
    	  // controller.getAnalyzer().getAnalysisManager().getAc .getAnalysisStepper().getModel().getBlockState()
    	  //isModified
      }
      
      //      if (isTargetActive) targetTitle = getModel().getCurrentCase().targetDefinitionFile.getName();
      //      windowTitle         += "(" + targetTitle + ") ";      

      frame.setTitle(applicationTitle + " : " + documentTitle.trim());
    }

  }

  /**
   * @return the controller
   */
  public CaseManager getController() {
    return controller;
  }

  /**
   *  @return
   */
  protected CaseManagerModel getModel() {
    if (model != null) return model;

    return model;
  }

  /**
   *    @return
   */
  protected TargetManagerModel getTargetModel() {
    return getController().getTargetManager().getModel();
  }

  /**
   * @param controller the controller to set
   */
  public void setController(CaseManager controller) {
    this.controller = controller;
    attachModel(controller.getModel());
    getTargetModel().attachObserver(this);
  }
}
