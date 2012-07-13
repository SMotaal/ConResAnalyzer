/*
 * @(#)PatchGeneratorParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conreslabs.panels;

import com.grasppe.conreslabs.panels.patchgenerator.PatchParametersPanel;
import com.grasppe.conreslabs.panels.patchgenerator.PrintingParametersPanel;
import com.grasppe.conreslabs.panels.patchgenerator.ScanningParametersPanel;
import com.grasppe.conreslabs.panels.patchgenerator.ScreeningParametersPanel;
import com.grasppe.jive.components.ModulePanelContainer;
import com.grasppe.lure.framework.GrasppeKit.Observer;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.oracle.layout.SpringUtilities;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

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

  /*
   *  (non-Javadoc)
   *   @see com.grasppe.conreslabs.matlab.ModulePanelContainer#createPanels()
   */

  /**
   */
  @Override
  protected void createPanels() {
//    Font   headingFont = new Font("Sans Serif", Font.BOLD, 12);
//
//    Border panelBorder = new EtchedBorder(EtchedBorder.LOWERED, null, null);		// new LineBorder(SystemColor.controlShadow, 1, true);

    // Patch
//    patchParametersPanel = new PatchParametersPanel();
//    patchParametersPanel.setBorder(BorderFactory.createTitledBorder("Patch Parameters"));
//    contentPanel.add(patchParametersPanel);
    
    patchParametersPanel = new PatchParametersPanel();
    screeningParametersPanel = new ScreeningParametersPanel();
    printingParametersPanel = new PrintingParametersPanel();
    scanningParametersPanel = new ScanningParametersPanel();
    
    this.createPanel(patchParametersPanel, "Patch Parameters");
    this.createPanel(screeningParametersPanel, "Screen Parameters");
    this.createPanel(printingParametersPanel, "Print Parameters");
    this.createPanel(scanningParametersPanel, "Scan Parameters");

    //    // Screening
    //    screeningParametersPanel = new ScreeningParametersPanel();
    //    screeningParametersPanel.setBorder(BorderFactory.createTitledBorder("Screening Parameters"));
    //    contentPanel.add(screeningParametersPanel);
    //
    //    // Printing
    //    printingParametersPanel = new PrintingParametersPanel();
    //    printingParametersPanel.setBorder(BorderFactory.createTitledBorder("Printing Parameters"));
    //    contentPanel.add(printingParametersPanel);
    //
    //    // Scanning
    //    scanningParametersPanel = new ScanningParametersPanel();
    //    scanningParametersPanel.setBorder(BorderFactory.createTitledBorder("Scanning Parameters"));
    //    contentPanel.add(scanningParametersPanel);
    //
    //    SpringUtilities.makeCompactGrid(contentPanel, 4, 1, 3, 3, 3, 3);
    
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
        frame.getContentPane().add(new PatchGeneratorParametersPanel(),BorderLayout.EAST);
        

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
//    System.out.println("Update!");
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
