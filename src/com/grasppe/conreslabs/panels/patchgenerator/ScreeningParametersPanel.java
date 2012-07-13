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
 *  @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public class ScreeningParametersPanel extends ModuleParametersPanel implements PropertyChangeListener {

  private double    addressabilityValue = 2450,
                    resolutionValue     = 175,
                    angleValue          = 37.5;
  NumericValueField addressabilityComponent, resolutionComponent, angleComponent;

  /**
   * Create the panel.
   */
  public ScreeningParametersPanel() {

    this.addPropertyChangeListener(this);
    this.setName("Screen-Panel");
    
    setLayout(new FormLayout(PatchGeneratorParametersPanel.COLUMN_SPEC, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
                                                                                        FormFactory.DEFAULT_ROWSPEC,
                                                                                        FormFactory.RELATED_GAP_ROWSPEC,
                                                                                        FormFactory.DEFAULT_ROWSPEC,
                                                                                        FormFactory.RELATED_GAP_ROWSPEC,
                                                                                        FormFactory.DEFAULT_ROWSPEC,
                                                                                        FormFactory.RELATED_GAP_ROWSPEC, }));

    createPanelFields();
  }

  /**
   */
  private void createPanelFields() {

    // Addressability (spi)
    addressabilityComponent = (NumericValueField)createNumericField("Screen-Addressability", addressabilityValue,
                                                                    "Addressability", 300, 3600, "spi", 2);

    // Resolution (lpi)
    resolutionComponent = (NumericValueField)createNumericField("Screen-Resolution", resolutionValue, "Resolution", 50, 600,
                                                                "lpi", 4);

    // angle (�)
    angleComponent = (NumericValueField)createNumericField("Screen-Angle", angleValue, "Angle", 0, 359, "�", 6);

//  JLabel lblAddressability = new JLabel("Addressability");
//  
//  add(lblAddressability, "2, 2, right, default");
//  
//  JLabel lblAddressabilitySuffix = new JLabel("spi");
//  
//  add(lblAddressabilitySuffix, "6, 2, left, default");
//  
//  NumericValueField txtAddressability = new NumericValueField(300, 3600);
//  
//  txtAddressability.setHorizontalAlignment(SwingConstants.TRAILING);
//  txtAddressability.setValue(addressabilityValue);
//  txtAddressability.addPropertyChangeListener("value", this);
//  add(txtAddressability, "4, 2");
//  
//  JLabel lblResolution = new JLabel("Resolution");
//  
//  add(lblResolution, "2, 4, right, default");
//  
//  JLabel lblResolutionSuffix = new JLabel("lpi");
//  
//  add(lblResolutionSuffix, "6, 4, left, default");
//  
//  NumericValueField txtResolution = new NumericValueField(50, 600);
//  
//  txtResolution.setHorizontalAlignment(SwingConstants.TRAILING);
//  txtResolution.setValue(resolutionValue);
//  txtResolution.addPropertyChangeListener("value", this);
//  add(txtResolution, "4, 4");
//  
//  JLabel lblAngle = new JLabel("Angle");
//  
//  add(lblAngle, "2, 6, right, default");
//  
//  JLabel lblAngleSuffix = new JLabel("�");
//  
//  add(lblAngleSuffix, "6, 6, left, default");
//  
//  NumericValueField txtAngle = new NumericValueField(0, 360);
//  
//  txtAngle.setHorizontalAlignment(SwingConstants.TRAILING);
//  txtAngle.setValue(angleValue);
//  txtAngle.addPropertyChangeListener("value", this);
//  add(txtAngle, "4, 6");

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
        frame.getContentPane().add(new ScreeningParametersPanel());

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }

    });
  }

  /**
   *    @param evt
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {

    String sourceName   = ((JComponent)evt.getSource()).getName();
    String propertyName = evt.getPropertyName();

//    if (sourceName != null) System.out.println(sourceName + ":" + propertyName);
//    else System.out.println(evt.getSource().getClass().getSimpleName() + ":" + propertyName);

    if (sourceName.matches("Screen-Panel")) {

      // Panel Property Changed... Update Field
      if (propertyName.matches("Screen-Addressability")) addressabilityComponent.setValue(addressabilityValue);

      else if (propertyName.matches("Screen-Resolution")) resolutionComponent.setValue(resolutionValue);

      else if (propertyName.matches("Screen-Angle")) angleComponent.setValue(angleValue);

      else {
        return;
      }

      this.notifyObservers();

    } else {

      // Panel Field Changed... Update Property
      if (sourceName.matches("Screen-Addressability"))
        setAddressabilityValue(((Number)addressabilityComponent.getValue()).doubleValue());

      else if (sourceName.matches("Screen-Resolution"))
             setResolutionValue(((Number)resolutionComponent.getValue()).doubleValue());

      else if (sourceName.matches("Screen-Angle")) setAngleValue(((Number)angleComponent.getValue()).doubleValue());

      else {
        return;
      }

      // this.notifyObservers(); Not needed due to cascade from setting values
    }

  }

  /**
   *   @return the addressabilityValue
   */
  public double getAddressabilityValue() {
    return addressabilityValue;
  }

  /**
   * @return the angleValue
   */
  public double getAngleValue() {
    return angleValue;
  }

  /**
   * @return the resolutionValue
   */
  public double getResolutionValue() {
    return resolutionValue;
  }

  /**
   *    @param newValue
   */
  public void setAddressabilityValue(double newValue) {
    double oldValue = this.addressabilityValue;

    this.addressabilityValue = newValue;
    this.firePropertyChange("Screen-Addressability", oldValue, newValue);
  }

  /**
   *    @param newValue
   */
  public void setAngleValue(double newValue) {
    double oldValue = this.angleValue;

    this.angleValue = newValue;
    this.firePropertyChange("Screen-Angle", oldValue, newValue);
  }

  /**
   *    @param newValue
   */
  public void setResolutionValue(double newValue) {
    double oldValue = this.resolutionValue;

    this.resolutionValue = newValue;
    this.firePropertyChange("Screen-Resolution", oldValue, newValue);
  }
}
