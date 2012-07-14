/*
 * @(#)PatchParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conreslabs.panels.patchgenerator;

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
public class ScanningParametersPanel extends ModuleParametersPanel implements PropertyChangeListener {

  private double    resolutionValue = 1200,
                    scaleValue      = 100;
  NumericValueField resolutionComponent, scaleComponent;

  /**
   * Create the panel.
   */
  public ScanningParametersPanel() {

    this.addPropertyChangeListener(this);
    this.setName("Scan-Panel");
    
    this.setTitle("Scanning");

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

    // Scanning Resolution (dpi)
    resolutionComponent = (NumericValueField)createNumericField("Scan-Resolution", resolutionValue, "Resolution", 100, 4800,
                                                                "dpi", 2);

    // Image Scale (%)
    scaleComponent = (NumericValueField)createNumericField("Scan-Scale", scaleValue, "Scale", 0, 100, "%", 4);

//  JLabel lblResolution = new JLabel("Resolution");
//  
//  add(lblResolution, "2, 2, right, default");
//  
//  JLabel lblResolutionSuffix = new JLabel("dpi");
//  
//  add(lblResolutionSuffix, "6, 2, left, default");
//  
//  NumericValueField txtResolution = new NumericValueField(100, 3600);
//  
//  txtResolution.setHorizontalAlignment(SwingConstants.TRAILING);
//  txtResolution.setValue(resolutionValue);
//  txtResolution.addPropertyChangeListener("value", this);
//  add(txtResolution, "4, 2");
//  
//  JLabel lblScale = new JLabel("Scale");
//  
//  add(lblScale, "2, 4, right, default");
//  
//  JLabel lblScaleSuffix = new JLabel("");
//  
//  add(lblScaleSuffix, "6, 4, left, default");
//  
//  ToneValueField txtScale = new ToneValueField(0, 200 / 100);
//  
//  txtScale.setHorizontalAlignment(SwingConstants.TRAILING);
//  txtScale.setValue(scaleValue);
//  txtScale.addPropertyChangeListener("value", this);
//  add(txtScale, "4, 4");

  }

  /**
   *    @param args
   */
  public static void main(String[] args) {

    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        JFrame frame = new JFrame("ScreeningParametersPanel Demo");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add contents to the window.
        frame.getContentPane().add(new ScanningParametersPanel());

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

//    if (sourceName != null) System.out.println(sourceName + ":" + propertyName);
//    else System.out.println(evt.getSource().getClass().getSimpleName() + ":" + propertyName);

    if (sourceName.matches("Scan-Panel")) {

      // Panel Property Changed... Update Field
      if (propertyName.matches("Scan-Resolution")) resolutionComponent.setValue(resolutionValue);

      else if (propertyName.matches("Scan-Scale")) scaleComponent.setValue(scaleValue);

      else {
        return;
      }

      this.notifyObservers();

    } else {

      // Panel Field Changed... Update Property
      if (sourceName.matches("Scan-Resolution")) setResolutionValue(((Number)resolutionComponent.getValue()).doubleValue());

      else if (sourceName.matches("Scan-Scale")) setScaleValue(((Number)scaleComponent.getValue()).doubleValue());

      else {
        return;
      }

      // this.notifyObservers(); Not needed due to cascade from setting values
    }

  }

  /**
   *   @return the resolutionValue
   */
  public double getResolutionValue() {
    return resolutionValue;
  }

  /**
   * @return the scaleValue
   */
  public double getScaleValue() {
    return scaleValue;
  }

  /**
   *    @param newValue
   */
  public void setResolutionValue(double newValue) {
    double oldValue = this.resolutionValue;

    this.resolutionValue = newValue;
    this.firePropertyChange("Scan-Resolution", oldValue, newValue);
  }

  /**
   *    @param newValue
   */
  public void setScaleValue(double newValue) {
    double oldValue = this.scaleValue;

    this.scaleValue = newValue;
    this.firePropertyChange("Scan-Scale", oldValue, newValue);
  }
}
