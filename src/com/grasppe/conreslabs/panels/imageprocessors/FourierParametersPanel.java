/*
 * @(#)PatchParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conreslabs.panels.imageprocessors;

import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class FourierParametersPanel extends JiveParametersPanel implements PropertyChangeListener {


/**
   * Create the panel.
   */
  public FourierParametersPanel() {
    super("Fourier-Panel", "Fourier");
  }

  /**
   */
  @Override
  protected void createFields() {

//  ParameterField.setGroupOptions("long-text", GroupOptions.LONG_TEXT_OPTIONS);
//  ParameterField.setGroupOptions("short-text", GroupOptions.SHORT_TEXT_OPTIONS);

//  methodField = Factory.Default().createListField("Fourier-Method", "Method", 0, methodOptions, "");
//  addField(methodField);
//  modeField = Factory.Default().createListField("Fourier-Mode", "Mode", 0, modeOptions, "");
//  addField(modeField);

    String[] modeOptions   = { "Automatic", "Forward", "Reverse" };
    String[] methodOptions = { "Logarithmic Scaling", "Raw Power Spectrum" };

    addField(LongFactory().createListField("Method", "Method", 0, methodOptions, ""));

    addField(LongFactory().createListField("Mode", "Mode", 0, modeOptions, ""));

  }
  
  
  

//  /**
//   *    @param args
//   */
//  public static void main(String[] args) {
//
//    // Schedule a job for the event dispatch thread:
//    // creating and showing this application's GUI.
//    SwingUtilities.invokeLater(new Runnable() {
//
//      public void run() {
//
//        JFrame frame = new JFrame("FunctionParametersPanel Demo");
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // Add contents to the window.
//        frame.getContentPane().add(new FourierParametersPanel());
//
//        // Display the window.
//        frame.pack();
//        frame.setVisible(true);
//      }
//
//    });
//  }
  
}
