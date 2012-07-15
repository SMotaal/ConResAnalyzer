/*
 * @(#)PatchParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conreslabs.panels.patchgenerator;

import com.grasppe.jive.components.JiveParametersPanel;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ScanningParametersPanel extends JiveParametersPanel {

  /**
   * Create the panel.
   */
  public ScanningParametersPanel() {
    super("Scanning-Panel", "Scanning");
  }

  /**
   */
  @Override
  protected void createFields() {

    addField(TinyJiveFieldFactory().createNumericField("Resolution", "Resolution", 1200, 100, 4800, "dpi"));
    addField(TinyJiveFieldFactory().createNumericField("Scale", "Scale", 100, 10, 1000, "%"));
  }
}
