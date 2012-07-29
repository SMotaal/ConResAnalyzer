/*
 * @(#)PatchGeneratorParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conreslabs.panels;

import com.grasppe.conreslabs.panels.imageprocessors.DisplayModulePanel;
import com.grasppe.conreslabs.panels.imageprocessors.FourierModulePanel;
import com.grasppe.conreslabs.panels.imageprocessors.FunctionModulePanel;
import com.grasppe.conreslabs.panels.patchgenerator.PatchModulePanel;
import com.grasppe.conreslabs.panels.patchgenerator.PrintingModulePanel;
import com.grasppe.conreslabs.panels.patchgenerator.ScanningModulePanel;
import com.grasppe.conreslabs.panels.patchgenerator.ScreeningModulePanel;
import com.grasppe.jive.components.JiveModulePanel;
import com.grasppe.jive.components.JiveModuleContainer;
import com.grasppe.lure.framework.GrasppeKit.Observer;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PatchGeneratorParametersPanel extends JiveModuleContainer implements Observer {

  /** Field description */
  public static ColumnSpec[] COLUMN_SPEC = new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("40dlu"),
                                                              ColumnSpec.decode("2dlu"), ColumnSpec.decode("max(40dlu;default)"),
                                                              ColumnSpec.decode("0dlu"), ColumnSpec.decode("20dlu:grow"),
                                                              FormFactory.RELATED_GAP_COLSPEC, };

//private JButton                  btnApply;
  private PatchModulePanel     patchModulePanel;
  private ScreeningModulePanel screeningModulePanel;
  private PrintingModulePanel  printingModulePanel;
  private ScanningModulePanel  scanningModulePanel;
  private JiveModulePanel   fourierModulePanel;
  private FunctionModulePanel  functionModulePanel;
  private DisplayModulePanel displayModulePanel;

  /*
   *  (non-Javadoc)
   *   @see com.grasppe.conreslabs.matlab.ModulePanelContainer#createPanels()
   */

  /**
   */
  @Override
  protected void createPanels() {

    patchModulePanel     = new PatchModulePanel();
    screeningModulePanel = new ScreeningModulePanel();
    printingModulePanel  = new PrintingModulePanel();
    scanningModulePanel  = new ScanningModulePanel();

    // fourierModulePanel   = new FourierModulePanel();
    // displayModulePanel   = new DisplayModulePanel();
    functionModulePanel  = new FunctionModulePanel();

    this.createPermanentPanel(patchModulePanel, null);					// "Patch Parameters");
    this.createPermanentPanel(screeningModulePanel, null);			// "Screen Parameters");
    this.createPermanentPanel(printingModulePanel, null);			// "Print Parameters");
    this.createPermanentPanel(scanningModulePanel, null);			// "Scan Parameters");
    this.createPanel(functionModulePanel, null, null);				// "Fourier Parameters");
    // this.createPanel(fourierModulePanel, null, null);				// "Fourier Parameters");
    // this.createPanel(displayModulePanel, null, null);				// "Fourier Parameters");
  }

  /**
   *    @param args
   */
  public static void main(String[] args) {

    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        JFrame frame = new JFrame("PatchGeneratorParametersPanel Demo");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(new PatchGeneratorParametersPanel(), BorderLayout.EAST);

        // Display the window.
//        frame.pack();
        frame.setVisible(true);
        frame.pack(); 
      }

    });
  }

  /**
   */
  @Override
  public void update() {

//  System.out.println("Update!");
  }

  /**
   *   @return the patchModulePanel
   */
  public PatchModulePanel getPatchModulePanel() {
    return patchModulePanel;
  }

  /**
   * @return the printingModulePanel
   */
  public PrintingModulePanel getPrintingModulePanel() {
    return printingModulePanel;
  }

  /**
   * @return the scanningModulePanel
   */
  public ScanningModulePanel getScanningModulePanel() {
    return scanningModulePanel;
  }

  /**
   * @return the screeningModulePanel
   */
  public ScreeningModulePanel getScreeningModulePanel() {
    return screeningModulePanel;
  }
}
