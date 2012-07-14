/*
 * @(#)PatchParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conreslabs.panels.imageprocessors;

import com.grasppe.conreslabs.panels.PatchGeneratorParametersPanel;
import com.grasppe.jive.components.ModuleParametersPanel;
import com.grasppe.jive.fields.NumericValueField;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

//~--- JDK imports ------------------------------------------------------------

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class FourierParametersPanel extends ModuleParametersPanel implements PropertyChangeListener {

  private int    modeValue = 0,
                    methodValue = 0;
  JComponent modeComponent, methodComponent;

  /**
   * Create the panel.
   */
  public FourierParametersPanel() {

    this.addPropertyChangeListener(this);
    this.setName("FFT-Panel");
    
    this.setTitle("Fourier Transform");

    setLayout(new FormLayout(PatchGeneratorParametersPanel.COLUMN_SPEC, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
                                                                                        FormFactory.DEFAULT_ROWSPEC,
                                                                                        FormFactory.RELATED_GAP_ROWSPEC,
                                                                                        FormFactory.DEFAULT_ROWSPEC,
                                                                                        FormFactory.RELATED_GAP_ROWSPEC, }));

    createPanelFields();
  }

  /**
   */
  private void createPanelFields() {

    String[] fftModes = {"Automatic", "Forward", "Reverse"};
    
    modeComponent = createListField("FFT-Mode", modeValue, fftModes, "Mode", "", 2);    
    
    String[] fftMethods = {"Logarithmic Scaling", "Raw Power Spectrum"};
    
    methodComponent = createListField("FFT-Method", methodValue, fftMethods, "Method", "", 4);

  }

  /**
   *    @param args
   */
  public static void main(String[] args) {

    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        JFrame frame = new JFrame("FourierParametersPanel Demo");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add contents to the window.
        frame.getContentPane().add(new FourierParametersPanel());

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }

    });
  }

  /**
   *    @param evt
   */
  public void propertyChange(PropertyChangeEvent evt) {

    String sourceName   = ((JComponent)evt.getSource()).getName();
    String propertyName = evt.getPropertyName();

    if (sourceName.matches("FFT-Panel")) {

      // Panel Property Changed... Update Field
//       if (propertyName.matches("Scan-Resolution")) functionComponent.setValue(modeValue);
//
//      else if (propertyName.matches("Scan-Scale")) methodComponent.setValue(methodValue);
//
//      else {
//        return;
//      }

      this.notifyObservers();

    } else {

      // Panel Field Changed... Update Property
//      if (sourceName.matches("Scan-Resolution")) setResolutionValue(((Number)functionComponent.getValue()).doubleValue());
//
//      else if (sourceName.matches("Scan-Scale")) setScaleValue(((Number)methodComponent.getValue()).doubleValue());
//
//      else {
//        return;
//      }

      // this.notifyObservers(); Not needed due to cascade from setting values
    }

  }

  /**
   *   @return the modeValue
   */
  public int getModeValue() {
    return modeValue;
  }

  /**
   * @return the methodValue
   */
  public int getMethodValue() {
    return methodValue;
  }

  /**
   *    @param newValue
   */
  public void setModeValue(int newValue) {
    int oldValue = this.modeValue;

    this.modeValue = newValue;
    this.firePropertyChange("FFT-Mode", oldValue, newValue);
  }

  /**
   *    @param newValue
   */
  public void setMethodValue(int newValue) {
    int oldValue = this.methodValue;

    this.methodValue = newValue;
    this.firePropertyChange("FFT-Method", oldValue, newValue);
  }
}
