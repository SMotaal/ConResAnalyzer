/*
 * @(#)PatchModulePanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conreslabs.panels.patchgenerator;

import com.grasppe.conreslabs.fields.ResolutionValueField;
import com.grasppe.jive.JiveComponentFactory;
import com.grasppe.jive.components.JiveModulePanel;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PrintingModulePanel extends JiveModulePanel {

  /**
   * Create the panel.
   */
  public PrintingModulePanel() {
    super("Printing-Panel", "Printing");
  }

  /**
   */
  @Override
  protected void createFields() {

    // // Mean Tone (aka Reference Tone Value)
    // meanToneComponent = (JiveNumericField)createNumericField("Patch-Mean", meanToneValue, "Tone", 0, 100, "%", 2);
    //
    // // Contrast
    // contrastComponent = (JiveNumericField)createNumericField("Patch-Contrast", contrastValue, "Contrast", 0, 100, "%", 4);
    //
    // // Resolution (lp/mm)
    // resolutionComponent = (JiveNumericField)createResolutionField("Patch-Resolution", resolutionValue, "Resolution", 0, 10,
    // "lp/mm", 6);
    //
    // // Patch Size (mm)
    // patchSizeComponent = (JiveNumericField)createNumericField("Patch-Size", patchSizeValue, "Patch Size", 3.0, 16.0, "mm", 8);

    addField(TinyJiveFieldFactory().createNumericField("Gain", "Dot Gain", 0, 0, 100, "%"));
    addField(TinyJiveFieldFactory().createNumericField("Noise", "Noise", 0, 0, 100, "%"));
    addField(TinyJiveFieldFactory().createNumericField("Blur", "Blur", 0, 0, 100, "%"));
    addField(TinyJiveFieldFactory().createNumericField("Radius", "Radius", 0, 0, 100, "spots"));
  }
}

///*
// * @(#)PatchModulePanel.java   12/07/07
// *
// * Copyright (c) 2011 Saleh Abdel Motaal
// *
// * This code is not licensed for use and is the property of it's owner.
// *
// */
//
//
//
//package com.grasppe.conreslabs.panels.patchgenerator;
//
//import com.grasppe.conreslabs.panels.PatchGeneratorParametersPanel;
//import com.grasppe.jive.components.JiveAbstractPanel;
//import com.grasppe.jive.fields.JiveNumericField;
//
//import com.jgoodies.forms.factories.FormFactory;
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.RowSpec;
//
////~--- JDK imports ------------------------------------------------------------
//
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//
//import javax.swing.JComponent;
//import javax.swing.JFrame;
//import javax.swing.SwingUtilities;
//
///**
// * Class description
// *  @version        $Revision: 1.0, 12/07/07
// *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
// */
//public class PrintingModulePanel extends JiveAbstractPanel implements PropertyChangeListener {
//
//  private double    dotgainValue = 0 / 100,
//                    noiseValue   = 0 / 100,
//                    spreadValue  = 0,
//                    unsharpValue = 0 / 100;
//  JiveNumericField dotgainComponent, noiseComponent, spreadComponent, unsharpComponent;
//
//  /**
//   * Create the panel.
//   */
//  public PrintingModulePanel() {
//
//    this.addPropertyChangeListener(this);
//    this.setName("Print-Panel");
//    
//    this.setTitle("Printing");
//
//    setLayout(new FormLayout(PatchGeneratorParametersPanel.COLUMN_SPEC, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
//                                                                                        FormFactory.DEFAULT_ROWSPEC,
//                                                                                        FormFactory.RELATED_GAP_ROWSPEC,
//                                                                                        FormFactory.DEFAULT_ROWSPEC,
//                                                                                        FormFactory.RELATED_GAP_ROWSPEC,
//                                                                                        FormFactory.DEFAULT_ROWSPEC,
//                                                                                        FormFactory.RELATED_GAP_ROWSPEC,
//                                                                                        FormFactory.DEFAULT_ROWSPEC,
//                                                                                        FormFactory.RELATED_GAP_ROWSPEC, }));
//
//    createPanelFields();
//  }
//
//  /**
//   */
//  private void createPanelFields() {
//
//    // Dot Gain (aka TVI, Tone Value Increase)
//    dotgainComponent = (JiveNumericField)createNumericField("Print-DotGain", dotgainValue, "Dot Gain", 0, 100, "%", 2);
//
//    // JLabel lblDotGain = new JLabel("Dot Gain");
//    // add(lblDotGain, "2, 2, right, default");
//    // JLabel lblDotGainSuffix = new JLabel("%");
//    // add(lblDotGainSuffix, "6, 2, left, default");
//    // JiveNumericField txtDotGain = new JiveNumericField(0, 100);
//    // txtDotGain.setHorizontalAlignment(SwingConstants.TRAILING);
//    // txtDotGain.setName("Print-DotGain");
//    // txtDotGain.setValue(dotgainValue);
//    // dotgainComponent = txtDotGain;
//    // txtDotGain.addPropertyChangeListener("value", this);
//    // add(txtDotGain, "4, 2");
//
//    // Imaging Noise
//    noiseComponent = (JiveNumericField)createNumericField("Print-Noise", noiseValue, "Noise", 0, 100, "%", 4);
//
//    // JLabel lblNoise = new JLabel("Noise");
//    // add(lblNoise, "2, 4, right, default");
//    // JLabel lblNoiseSuffix = new JLabel("%");
//    // add(lblNoiseSuffix, "6, 4, left, default");
//    // JiveNumericField txtNoise = new JiveNumericField(0, 100);
//    // txtNoise.setHorizontalAlignment(SwingConstants.TRAILING);
//    // txtNoise.setName("Print-Noise");
//    // txtNoise.setValue(noiseValue);
//    // noiseComponent = txtNoise;
//    // txtNoise.addPropertyChangeListener("value", this);
//    // add(txtNoise, "4, 4");
//
//    // Imaging Spread (aka Gaussian Blur Radius);
//    spreadComponent = (JiveNumericField)createNumericField("Print-Spread", spreadValue, "Spread", 0, 100, "spots", 6);
//
//    // JLabel lblSpread = new JLabel("Spread");
//    // add(lblSpread, "2, 6, right, default");
//    // JLabel lblSpreadSuffix = new JLabel("spots");
//    // add(lblSpreadSuffix, "6, 6, left, default");
//    // JiveNumericField txtSpread = new JiveNumericField(0, 999);
//    // txtSpread.setHorizontalAlignment(SwingConstants.TRAILING);
//    // txtSpread.setName("Print-Spread");
//    // txtSpread.setValue(spreadValue);
//    // spreadComponent = txtSpread;
//    // txtSpread.addPropertyChangeListener("value", this);
//    // add(txtSpread, "4, 6");
//
//    // Imaging Spread (aka Gaussian Blur Strength);
//    unsharpComponent = (JiveNumericField)createNumericField("Print-Unsharp", unsharpValue, "Blur", 0, 100, "%", 8);
//
//    // JLabel lblUnsharp = new JLabel("Blur");
//    // add(lblUnsharp, "2, 8, right, default");
//    // JLabel lblUnsharpSuffix = new JLabel("%");
//    // add(lblUnsharpSuffix, "6, 8, left, default");
//    // JiveNumericField txtUnsharp = new JiveNumericField(0, 100);
//    // txtUnsharp.setHorizontalAlignment(SwingConstants.TRAILING);
//    // txtUnsharp.setName("Print-Unsharp");
//    // txtUnsharp.setValue(unsharpValue);
//    // unsharpComponent = txtUnsharp;
//    // txtUnsharp.addPropertyChangeListener("value", this);
//    // add(txtUnsharp, "4, 8");
//
//  }
//
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
//        JFrame frame = new JFrame("ScreeningModulePanel Demo");
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // Add contents to the window.
//        PrintingModulePanel panel = new PrintingModulePanel();
//
//        frame.getContentPane().add(panel);
//
//        // Display the window.
//        frame.pack();
//        frame.setVisible(true);
//
//        panel.setDotgainValue(100);
//      }
//
//    });
//  }
//
//  /**
//   *    @param evt
//   */
//  @Override
//  public void propertyChange(PropertyChangeEvent evt) {
//
//    // TODO Auto-generated method stub
//    // Object source = evt.getSource();
//    String sourceName   = ((JComponent)evt.getSource()).getName();
//    String propertyName = evt.getPropertyName();
//
////    if (sourceName != null) System.out.println(sourceName + ":" + propertyName);
////    else System.out.println(evt.getSource().getClass().getSimpleName() + ":" + propertyName);
//
//    if (sourceName.matches("Print-Panel")) {
//
//      // Panel Field Changed... Update Property
//      if (propertyName.matches("Print-DotGain")) dotgainComponent.setValue(dotgainValue);
//
//      else if (propertyName.matches("Print-Noise")) noiseComponent.setValue(noiseValue);
//
//      else if (propertyName.matches("Print-Spread")) spreadComponent.setValue(spreadValue);
//
//      else if (propertyName.matches("Print-Unsharp")) unsharpComponent.setValue(unsharpValue);
//
//      else {
//        return;
//      }
//
//      this.notifyObservers();
//
//    } else {
//
//      // Panel Field Changed... Update Property
//      if (sourceName.matches("Print-DotGain")) setDotgainValue(((Number)dotgainComponent.getValue()).doubleValue());
//
//      else if (sourceName.matches("Print-Noise")) setNoiseValue(((Number)noiseComponent.getValue()).doubleValue());
//
//      else if (sourceName.matches("Print-Spread")) setSpreadValue(((Number)spreadComponent.getValue()).doubleValue());
//
//      else if (sourceName.matches("Print-Unsharp")) setUnsharpValue(((Number)unsharpComponent.getValue()).doubleValue());
//
//      else {
//        return;
//      }
//
//      // this.notifyObservers(); Not needed due to cascade from setting values
//    }
//
////  switch (key) {
////  case value:
////    
////    break;
////
////  default:
////    break;
////  }
//
//  }
//
//  /**
//   *   @return the dotgainValue
//   */
//  public double getDotgainValue() {
//    return dotgainValue;
//  }
//
//  /**
//   * @return the noiseValue
//   */
//  public double getNoiseValue() {
//    return noiseValue;
//  }
//
//  /**
//   * @return the spreadValue
//   */
//  public double getSpreadValue() {
//    return spreadValue;
//  }
//
//  /**
//   * @return the unsharpValue
//   */
//  public double getUnsharpValue() {
//    return unsharpValue;
//  }
//
//  /**
//   * @param dotgainValue the dotgainValue to set
//   */
//  public void setDotgainValue(double dotgainValue) {
//    double oldValue = this.dotgainValue;
//
//    this.dotgainValue = dotgainValue;
//    this.firePropertyChange("Print-DotGain", oldValue, dotgainValue);
//  }
//
//  /**
//   * @param noiseValue the noiseValue to set
//   */
//  public void setNoiseValue(double noiseValue) {
//    double oldValue = this.noiseValue;
//
//    this.noiseValue = noiseValue;
//    this.firePropertyChange("Print-Noise", oldValue, noiseValue);
//  }
//
//  /**
//   * @param spreadValue the spreadValue to set
//   */
//  public void setSpreadValue(double spreadValue) {
//    double oldValue = this.spreadValue;
//
//    this.spreadValue = spreadValue;
//    this.firePropertyChange("Print-Spread", oldValue, spreadValue);
//  }
//
//  /**
//   * @param unsharpValue the unsharpValue to set
//   */
//  public void setUnsharpValue(double unsharpValue) {
//
////  this.unsharpValue = unsharpValue;
//    double oldValue = this.unsharpValue;
//
//    this.unsharpValue = unsharpValue;
//    this.firePropertyChange("Print-Unsharp", oldValue, unsharpValue);
//  }
//}
