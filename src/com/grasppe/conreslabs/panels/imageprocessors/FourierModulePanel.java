/*
 * @(#)PatchModulePanel.java   12/07/07
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

import com.grasppe.jive.components.JiveField;
import com.grasppe.jive.components.JiveModulePanel;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class FourierModulePanel extends FunctionModulePanel { // implements PropertyChangeListener {


/**
   * Create the panel.
   */
  public FourierModulePanel() {
    super("Fourier-Panel", "Fourier");
  }

  /**
   */
  @Override
  protected void createFields() {

    String[] modeOptions   = { "Automatic", "Forward", "Reverse" };
    String[] methodOptions = { "Logarithmic Scaling", "Raw Power Spectrum" };
    
    JiveField expressionField = LongJiveFieldFactory().createTextField("Expression", "Expression", "fourier(Mode);","");
    
    expressionField.setVisible(false);
    
    addField(expressionField);

    // addField(LongJiveFieldFactory().createListField("Method", "Method", 0, methodOptions, ""));

    addField(LongJiveFieldFactory().createListField("Mode", "Mode", 0, modeOptions, ""));

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
//        JFrame frame = new JFrame("FunctionModulePanel Demo");
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // Add contents to the window.
//        frame.getContentPane().add(new FourierModulePanel());
//
//        // Display the window.
//        frame.pack();
//        frame.setVisible(true);
//      }
//
//    });
//  }
  
}
