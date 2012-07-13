/*
 * @(#)PatchGeneratorParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.matlab;

import com.grasppe.lure.framework.GrasppeKit.Observer;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PatchGeneratorModulesPanel extends JPanel implements Observer {

  /** Field description */
  public static ColumnSpec[] COLUMN_SPEC = new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("50dlu"),
                                                              ColumnSpec.decode("2dlu"), ColumnSpec.decode("max(30dlu;default)"),
                                                              ColumnSpec.decode("0dlu"), ColumnSpec.decode("30dlu:grow"),
                                                              FormFactory.RELATED_GAP_COLSPEC, };
  private JButton                  btnApply;
  private PatchParametersPanel     patchParametersPanel;
  private ScreeningParametersPanel screeningParametersPanel;
  private PrintingParametersPanel  printingParametersPanel;
  private ScanningParametersPanel  scanningParametersPanel;

  /**
   * Create the panel.
   */
  public PatchGeneratorModulesPanel() {

    GraphicsEnvironment e           = GraphicsEnvironment.getLocalGraphicsEnvironment();

    Font                headingFont = new Font("Sans Serif", Font.BOLD, 12);

    Border              panelBorder = new EtchedBorder(EtchedBorder.LOWERED, null, null);			// new LineBorder(SystemColor.controlShadow, 1, true);

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JPanel containerPanel = new JPanel();

    add(containerPanel);
    containerPanel.setLayout(new BorderLayout());

    JPanel      contentPanel = new JPanel();

    JScrollPane scrollPane   = new JScrollPane(contentPanel);

    containerPanel.add(scrollPane, BorderLayout.CENTER);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    contentPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
                                                             ColumnSpec.decode("max(100dlu;default):grow(2)"),
                                                             FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
                                                               FormFactory.UNRELATED_GAP_ROWSPEC,
                                                               FormFactory.DEFAULT_ROWSPEC,			// RowSpec.decode("default:grow"),
                                                               FormFactory.NARROW_LINE_GAP_ROWSPEC,
                                                               FormFactory.DEFAULT_ROWSPEC,
                                                               FormFactory.UNRELATED_GAP_ROWSPEC,
                                                               FormFactory.DEFAULT_ROWSPEC,
                                                               FormFactory.NARROW_LINE_GAP_ROWSPEC,
                                                               FormFactory.DEFAULT_ROWSPEC,
                                                               FormFactory.UNRELATED_GAP_ROWSPEC,
                                                               FormFactory.DEFAULT_ROWSPEC,
                                                               FormFactory.NARROW_LINE_GAP_ROWSPEC,
                                                               FormFactory.DEFAULT_ROWSPEC,
                                                               FormFactory.UNRELATED_GAP_ROWSPEC,
                                                               FormFactory.DEFAULT_ROWSPEC,
                                                               FormFactory.NARROW_LINE_GAP_ROWSPEC,
                                                               FormFactory.DEFAULT_ROWSPEC,
                                                               FormFactory.UNRELATED_GAP_ROWSPEC,
                                                               FormFactory.DEFAULT_ROWSPEC,
                                                               FormFactory.NARROW_LINE_GAP_ROWSPEC,
                                                               FormFactory.DEFAULT_ROWSPEC,
                                                               FormFactory.UNRELATED_GAP_ROWSPEC, }));

    // Patch

    JLabel lblPatchParameters = new JLabel("Patch Parameters");

    lblPatchParameters.setFont(headingFont);

    contentPanel.add(lblPatchParameters, "2, 2");

    patchParametersPanel = new PatchParametersPanel();

    patchParametersPanel.setBorder(panelBorder);

    contentPanel.add(patchParametersPanel, "2, 4, fill, fill");

    // Screening

    JLabel lblScreeningParameters = new JLabel("Screening Parameters");

    lblScreeningParameters.setFont(headingFont);

    contentPanel.add(lblScreeningParameters, "2, 6");

    screeningParametersPanel = new ScreeningParametersPanel();

    screeningParametersPanel.setBorder(panelBorder);

    contentPanel.add(screeningParametersPanel, "2, 8, fill, fill");

    // Printing

    JLabel lblPrintingParameters = new JLabel("Printing Parameters");

    lblPrintingParameters.setFont(headingFont);

    contentPanel.add(lblPrintingParameters, "2, 10");

    printingParametersPanel = new PrintingParametersPanel();

    printingParametersPanel.setBorder(panelBorder);

    contentPanel.add(printingParametersPanel, "2, 12, fill, fill");

    printingParametersPanel.attachObserver(this);

    // Scanning

    JLabel lblScanningParameters = new JLabel("Scanning Parameters");

    lblScanningParameters.setFont(headingFont);

    contentPanel.add(lblScanningParameters, "2, 14");

    scanningParametersPanel = new ScanningParametersPanel();

    scanningParametersPanel.setBorder(panelBorder);

    contentPanel.add(scanningParametersPanel, "2, 16, fill, fill");

    // Apply Button

    btnApply = new JButton("Apply");

    // contentPanel.add(btnApply, "2, 20");
    
    containerPanel.add(btnApply, BorderLayout.SOUTH);
    
    attachKeyListener();

  }

  /**
   */
  private void attachKeyListener() {
    Action applyAction = new AbstractAction("Apply") {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        btnApply.doClick();
        // Toolkit.getDefaultToolkit().beep();
      }
    };

    InputMap input = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    
    KeyStroke applyKey = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0); // "enter");
    
    input.put(applyKey, "apply");
    this.getActionMap().put("apply", applyAction);

//  this.addKeyListener
//    (new KeyAdapter() {
//       public void keyPressed(KeyEvent e) {
//         int key = e.getKeyCode();
//         if (key == KeyEvent.VK_ENTER) {
//             btnApply.doClick();
//             Toolkit.getDefaultToolkit().beep();   
//            // System.out.println("ENTER pressed");
//            }
//         }
//       }
//    );
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

        // Add contents to the window.
        frame.getContentPane().add(new PatchGeneratorModulesPanel());

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

//    System.out.println("Update!");		// this.getName())
  }

///**
// * @param scanningParametersPanel the scanningParametersPanel to set
// */
//public void setScanningParametersPanel(
//        ScanningParametersPanel scanningParametersPanel) {
//    this.scanningParametersPanel = scanningParametersPanel;
//}

  /**
   *     @return the btnApply
   */
  public JButton getApplyButton() {
    return btnApply;
  }

//
///**
// * @param btnApply the btnApply to set
// */
//public void setBtnApply(JButton btnApply) {
//    this.btnApply = btnApply;
//}

  /**
   *   @return the patchParametersPanel
   */
  public PatchParametersPanel getPatchParametersPanel() {
    return patchParametersPanel;
  }

///**
// * @param screeningParametersPanel the screeningParametersPanel to set
// */
//public void setScreeningParametersPanel(
//        ScreeningParametersPanel screeningParametersPanel) {
//    this.screeningParametersPanel = screeningParametersPanel;
//}

  /**
   * @return the printingParametersPanel
   */
  public PrintingParametersPanel getPrintingParametersPanel() {
    return printingParametersPanel;
  }

///**
// * @param printingParametersPanel the printingParametersPanel to set
// */
//public void setPrintingParametersPanel(
//        PrintingParametersPanel printingParametersPanel) {
//    this.printingParametersPanel = printingParametersPanel;
//}

  /**
   * @return the scanningParametersPanel
   */
  public ScanningParametersPanel getScanningParametersPanel() {
    return scanningParametersPanel;
  }

///**
// * @param patchParametersPanel the patchParametersPanel to set
// */
//public void setPatchParametersPanel(PatchParametersPanel patchParametersPanel) {
//    this.patchParametersPanel = patchParametersPanel;
//}

  /**
   * @return the screeningParametersPanel
   */
  public ScreeningParametersPanel getScreeningParametersPanel() {
    return screeningParametersPanel;
  }
}
