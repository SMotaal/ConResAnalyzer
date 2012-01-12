/*
 * @(#)CommandKeyCodeFieldView.java   12/01/11
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.analyzer.view;

import java.awt.Dimension;
import java.awt.FontMetrics;

import com.grasppe.conres.analyzer.PreferencesManager;
import com.grasppe.conres.preferences.Preferences.Tags;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JTextField;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/01/11
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CommandKeyCodeFieldView extends KeyCodeFieldView {

  protected JTextField	commandField = null;

  /**
   *    @param preference
   *    @param manager
   */
  protected CommandKeyCodeFieldView(Tags preference, PreferencesManager manager) {
    super(preference, manager);
  }

  /**
   */
  protected void createCommandField() {
    if (commandField != null) return;
    commandField = new JTextField(GrasppeKit.getControlSymbol());
    commandField.setEditable(false);
    commandField.setColumns(2);
    
    FontMetrics fm = getFontMetrics( commandField.getFont() );
    int textWidth = fm.stringWidth( commandField.getText() );
    
    commandField.setMaximumSize(new Dimension(textWidth,1000));
    commandField.setHorizontalAlignment(JTextField.CENTER);
    
    add(commandField);

  }

  /**
   */
  @Override
  protected void createView() {
    createLabel();
    createCommandField();
    createTextField();
  }
}
