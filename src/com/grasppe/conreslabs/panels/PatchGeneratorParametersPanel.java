/*
 * @(#)PatchGeneratorParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conreslabs.panels;

import com.grasppe.conreslabs.panels.imageprocessors.FourierParametersPanel;
import com.grasppe.conreslabs.panels.imageprocessors.FunctionParametersPanel;
import com.grasppe.conreslabs.panels.imageprocessors.JiveParametersPanel;
import com.grasppe.conreslabs.panels.patchgenerator.PatchParametersPanel;
import com.grasppe.conreslabs.panels.patchgenerator.PrintingParametersPanel;
import com.grasppe.conreslabs.panels.patchgenerator.ScanningParametersPanel;
import com.grasppe.conreslabs.panels.patchgenerator.ScreeningParametersPanel;
import com.grasppe.jive.components.ModulePanelContainer;
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
public class PatchGeneratorParametersPanel extends ModulePanelContainer implements Observer {

  /** Field description */
  public static ColumnSpec[] COLUMN_SPEC = new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("40dlu"),
                                                              ColumnSpec.decode("2dlu"), ColumnSpec.decode("max(40dlu;default)"),
                                                              ColumnSpec.decode("0dlu"), ColumnSpec.decode("20dlu:grow"),
                                                              FormFactory.RELATED_GAP_COLSPEC, };

//private JButton                  btnApply;
  private PatchParametersPanel     patchParametersPanel;
  private ScreeningParametersPanel screeningParametersPanel;
  private PrintingParametersPanel  printingParametersPanel;
  private ScanningParametersPanel  scanningParametersPanel;
  private JiveParametersPanel   fourierParametersPanel;
  private FunctionParametersPanel  functionParametersPanel;

  /*
   *  (non-Javadoc)
   *   @see com.grasppe.conreslabs.matlab.ModulePanelContainer#createPanels()
   */

  /**
   */
  @Override
  protected void createPanels() {

    patchParametersPanel     = new PatchParametersPanel();
    screeningParametersPanel = new ScreeningParametersPanel();
    printingParametersPanel  = new PrintingParametersPanel();
    scanningParametersPanel  = new ScanningParametersPanel();

    fourierParametersPanel   = new FourierParametersPanel();
    functionParametersPanel  = new FunctionParametersPanel();

    this.createPermanentPanel(patchParametersPanel, null);					// "Patch Parameters");
    this.createPermanentPanel(screeningParametersPanel, null);			// "Screen Parameters");
    this.createPermanentPanel(printingParametersPanel, null);			// "Print Parameters");
    this.createPermanentPanel(scanningParametersPanel, null);			// "Scan Parameters");
    this.createPanel(functionParametersPanel, null, null);				// "Fourier Parameters");
    this.createPanel(fourierParametersPanel, null, null);				// "Fourier Parameters");
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
        frame.pack();
        frame.setVisible(true);
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
   *   @return the patchParametersPanel
   */
  public PatchParametersPanel getPatchParametersPanel() {
    return patchParametersPanel;
  }

  /**
   * @return the printingParametersPanel
   */
  public PrintingParametersPanel getPrintingParametersPanel() {
    return printingParametersPanel;
  }

  /**
   * @return the scanningParametersPanel
   */
  public ScanningParametersPanel getScanningParametersPanel() {
    return scanningParametersPanel;
  }

  /**
   * @return the screeningParametersPanel
   */
  public ScreeningParametersPanel getScreeningParametersPanel() {
    return screeningParametersPanel;
  }
}
