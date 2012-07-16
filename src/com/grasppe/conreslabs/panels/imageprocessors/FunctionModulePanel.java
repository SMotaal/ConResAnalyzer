/*
 * @(#)PatchModulePanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conreslabs.panels.imageprocessors;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.grasppe.jive.components.JiveModulePanel;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class FunctionModulePanel extends JiveModulePanel {
	
	protected static int instances = 0;

  /**
   * Create the panel.
   */
  public FunctionModulePanel() {
    super("Function-Panel", "Function-" + instances++);
  }

  /**
   */
  @Override
  protected void createFields() {
	  
    addField(DefaultJiveFieldFactory().createTextField("ID", "ID", getTitle(), ""));
    addField(LongJiveFieldFactory().createTextField("Expression", "\u0192(\uD835\uDCCD)", "x;", ""));
//    addField(DefaultFactory().createTextField(getTitle() + "-ID", "ID", getTitle(), ""));
//    addField(LongFactory().createTextField(getTitle() + "-Expression", "\u0192(\uD835\uDCCD)", "x;", ""));

  }
  

  /* (non-Javadoc)
	 * @see com.grasppe.conreslabs.panels.imageprocessors.JiveParametersPanel#setValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setValue(String name, Object newValue) {
		super.setValue(name, newValue);
		
		if (name.equals("ID")) setTitle((String) newValue);
		
	}  

  /**
   *    @param args
   */
  public static void main(String[] args) {

    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        JFrame frame = new JFrame("FunctionModulePanel Demo");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add contents to the window.
        frame.getContentPane().add(new FunctionModulePanel());

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }

    });
  }
}
