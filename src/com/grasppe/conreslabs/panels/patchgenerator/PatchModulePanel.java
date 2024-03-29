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
 *  @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public class PatchModulePanel extends JiveModulePanel {

  /**
   * Create the panel.
   */
  public PatchModulePanel() {
    super("Patch-Panel", "Patch");
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

    addField(TinyJiveFieldFactory().createNumericField("Mean", "Tone", 50, 0, 100, "%"));
    addField(TinyJiveFieldFactory().createNumericField("Contrast", "Contrast", 100, 0, 100, "%"));
    addField(
        TinyJiveFieldFactory().createCustomField("Resolution", "Resolution",
                                        JiveComponentFactory.createCustomFieldComponent("Resolution",
                                                                                    new ResolutionValueField(
                                                                                        0.0, 0.0, 10.0)), "lp/mm"));
    addField(TinyJiveFieldFactory().createNumericField("Size", "Size", 5.3, 3.0, 16.0, "mm"));
  }
}



// /*
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
// package com.grasppe.conreslabs.panels.patchgenerator;
//
// import com.grasppe.conreslabs.panels.PatchGeneratorParametersPanel;
// import com.grasppe.jive.components.JiveAbstractPanel;
// import com.grasppe.jive.fields.JiveNumericField;
// import com.grasppe.jive.fields.ResolutionValueField;
//
// import com.jgoodies.forms.factories.FormFactory;
// import com.jgoodies.forms.layout.FormLayout;
// import com.jgoodies.forms.layout.RowSpec;
//
// //~--- JDK imports ------------------------------------------------------------
//
// import java.beans.PropertyChangeEvent;
// import java.beans.PropertyChangeListener;
//
// import javax.swing.JComponent;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.SwingConstants;
// import javax.swing.SwingUtilities;
//
// /**
// * Class description
// *  @version        $Revision: 1.0, 12/07/07
// *  @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
// */
// public class PatchModulePanel extends JiveAbstractPanel implements PropertyChangeListener {
//
// //private ToneValueField txtMeanToneValue, txtContrast;
// private double    meanToneValue   = 50,
// contrastValue   = 100,
// resolutionValue = 0.63,
// patchSizeValue  = 5.3;
// JiveNumericField contrastComponent, resolutionComponent, meanToneComponent, patchSizeComponent;
//
// /**
// * Create the panel.
// */
// public PatchModulePanel() {
//
// this.addPropertyChangeListener(this);
// this.setName("Patch-Panel");
//
// this.setTitle("Patch");
//
// setLayout(new FormLayout(PatchGeneratorParametersPanel.COLUMN_SPEC, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
// FormFactory.DEFAULT_ROWSPEC,
// FormFactory.RELATED_GAP_ROWSPEC,
// FormFactory.DEFAULT_ROWSPEC,
// FormFactory.RELATED_GAP_ROWSPEC,
// FormFactory.DEFAULT_ROWSPEC,
// FormFactory.RELATED_GAP_ROWSPEC,
// FormFactory.DEFAULT_ROWSPEC,
// FormFactory.RELATED_GAP_ROWSPEC, }));
// createPanelFields();
// }
//
// /**
// */
// private void createPanelFields() {
//
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
//
// // JLabel lblMeanToneValue = new JLabel("Mean Tone");
// // add(lblMeanToneValue, "2, 2, right, default");
// // JLabel lblMeanToneValueSuffix = new JLabel("");
// // add(lblMeanToneValueSuffix, "6, 2, left, default");
// // JiveNumericField txtMeanTone = new JiveNumericField(0, 100);
// // txtMeanTone.setHorizontalAlignment(SwingConstants.TRAILING);
// // txtMeanTone.setValue(meanToneValue);
// // meanToneComponent = txtMeanTone;
// // txtMeanTone.addPropertyChangeListener("value", this);
// // add(txtMeanTone, "4, 2");
//
// // JLabel lblContrast = new JLabel("Contrast");
// // add(lblContrast, "2, 4, right, default");
// // JLabel lblContrastSuffix = new JLabel("");
// // add(lblContrastSuffix, "6, 4, left, default");
// // JiveNumericField txtContrast = new JiveNumericField(0, 100);
// // txtContrast.setHorizontalAlignment(SwingConstants.TRAILING);
// // txtContrast.setValue(contrastValue);
// // contrastComponent = txtContrast;
// // txtContrast.addPropertyChangeListener("value", this);
// // add(txtContrast, "4, 4");
//
// // JLabel lblResolution = new JLabel("Resolution");
// // add(lblResolution, "2, 6, right, default");
// // JLabel lblResolutionSuffix = new JLabel("lp/mm");
// // add(lblResolutionSuffix, "6, 6, left, default");
// // JiveNumericField txtResolution = new ResolutionValueField(0, 10);
// // txtResolution.setHorizontalAlignment(SwingConstants.TRAILING);
// // txtResolution.setValue(resolutionValue);
// // functionComponent = txtResolution;
// // txtMeanTone.addPropertyChangeListener("value", this);
// // add(txtResolution, "4, 6");
//
// // JLabel lblPatchSize = new JLabel("Size");
// // add(lblPatchSize, "2, 8, right, default");
// // JLabel lblPatchSizeSuffix = new JLabel("mm");
// // add(lblPatchSizeSuffix, "6, 8, left, default");
// // JiveNumericField txtPatchSize = new JiveNumericField(3.0, 16.0);
// // txtPatchSize.setHorizontalAlignment(SwingConstants.TRAILING);
// // txtPatchSize.setValue(patchSize);
// // txtPatchSize.addPropertyChangeListener("value", this);
// // add(txtPatchSize, "4, 8");
//
// }
// //
// //  /**
// //   *    @param name
// //   *    @param value
// //   *    @param label
// //   *    @param minimum
// //   *    @param maximum
// //   *    @param suffix
// //   *    @param row
// //   *    @return
// //   */
// //  private JComponent createResolutionField(String name, double value, String label, double minimum, double maximum, String suffix, int row) {
// //    JLabel lblName = new JLabel(label);
// //
// //    add(lblName, "2, " + row + ", right, default");
// //
// //    JLabel lblSuffix = new JLabel(suffix);
// //
// //    add(lblSuffix, "6, " + row + ", left, default");
// //
// //    ResolutionValueField txtField = new ResolutionValueField(minimum, maximum);
// //
// //    txtField.setHorizontalAlignment(SwingConstants.TRAILING);
// //    txtField.setName(name);
// //    txtField.setValue(value);
// //    txtField.addPropertyChangeListener("value", this);
// //    add(txtField, "4, " + row);
// //
// //    return txtField;
// //  }
//
// //}
//
// /**
// *    @param args
// */
// public static void main(String[] args) {
//
// // Schedule a job for the event dispatch thread:
// // creating and showing this application's GUI.
// SwingUtilities.invokeLater(new Runnable() {
//
// public void run() {
//
// JFrame frame = new JFrame("PatchModulePanel Demo");
//
// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
// // Add contents to the window.
// frame.getContentPane().add(new PatchModulePanel());
//
// // Display the window.
// frame.pack();
// frame.setVisible(true);
// }
//
// });
// }
//
// /**
// *    @param evt
// */
// public void propertyChange(PropertyChangeEvent evt) {
//
// String sourceName   = ((JComponent)evt.getSource()).getName();
// String propertyName = evt.getPropertyName();
//
// //    if (sourceName != null) System.out.println(sourceName + ":" + propertyName);
// //    else System.out.println(evt.getSource().getClass().getSimpleName() + ":" + propertyName);
//
// if (sourceName.matches("Patch-Panel")) {
//
// // Panel Property Changed... Update Field
//
// if (propertyName.matches("Patch-Mean")) meanToneComponent.setValue(meanToneValue);
//
// else if (propertyName.matches("Patch-Contrast")) contrastComponent.setValue(contrastValue);
//
// else if (propertyName.matches("Patch-Resolution")) resolutionComponent.setValue(resolutionValue);
//
// else if (propertyName.matches("Patch-Size")) patchSizeComponent.setValue(patchSizeValue);
//
// else {
// return;
// }
//
// this.notifyObservers();
//
// } else {
//
// // Panel Field Changed... Update Property
// if (sourceName.matches("Patch-Mean")) setMeanToneValue(((Number)meanToneComponent.getValue()).doubleValue());
//
// else if (sourceName.matches("Patch-Contrast")) setContrastValue(((Number)contrastComponent.getValue()).doubleValue());
//
// else if (sourceName.matches("Patch-Resolution")) setResolutionValue(((Number)resolutionComponent.getValue()).doubleValue());
//
// else if (sourceName.matches("Patch-Size")) setPatchSizeValue(((Number)patchSizeComponent.getValue()).doubleValue());
//
// else {
// return;
// }
//
// // this.notifyObservers(); Not needed due to cascade from setting values
// }
// }
//
// /**
// * @return the contrastValue
// */
// public double getContrastValue() {
// return contrastValue;
// }
//
// /**
// *   @return the meanToneValue
// */
// public double getMeanToneValue() {
// return meanToneValue;
// }
//
// /**
// * @return the patchSizeValue
// */
// public double getPatchSizeValue() {
// return patchSizeValue;
// }
//
// /**
// * @return the resolutionValue
// */
// public double getResolutionValue() {
// return resolutionValue;
// }
//
// /**
// * @param newValue the contrastValue to set
// */
// public void setContrastValue(double newValue) {
// double oldValue = this.contrastValue;
//
// this.contrastValue = newValue;
// this.firePropertyChange("Patch-Contrast", oldValue, newValue);
// }
//
// /**
// * @param newValue the meanToneValue to set
// */
// public void setMeanToneValue(double newValue) {
// double oldValue = this.meanToneValue;
//
// this.meanToneValue = newValue;
// this.firePropertyChange("Patch-Mean", oldValue, newValue);
// }
//
// /**
// * @param newValue the patchSizeValue to set
// */
// public void setPatchSizeValue(double newValue) {
// double oldValue = this.patchSizeValue;
//
// this.patchSizeValue = newValue;
// this.firePropertyChange("Patch-Size", oldValue, newValue);
// }
//
// /**
// * @param newValue the resolutionValue to set
// */
// public void setResolutionValue(double newValue) {
// double oldValue = this.resolutionValue;
//
// this.resolutionValue = newValue;
// this.firePropertyChange("Patch-Resolution", oldValue, newValue);
// }
// }
