/*
 * @(#)ConResAnalyzerView.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer.view;

import com.grasppe.conres.analyzer.PreferencesManager;
import com.grasppe.conres.analyzer.model.PreferencesManagerModel;
import com.grasppe.conres.preferences.Preferences.Tags;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.components.ObservableObject;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;
import com.sun.snippets.ListDialog;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *     Class description
 *     @version        $Revision: 0.1, 11/11/08
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PreferencesManagerView extends AbstractView implements Observer, ActionListener {

  /** Field description */

//ConResAnalyzerMenu                menu;
  String									name        = "PreferencesManager";
  boolean									finalizable = true;
  int											activeCalls = 0;
  JDialog									mainFrame;
  HashMap<String, JPanel>	controlsMap     = new HashMap<String, JPanel>();
  ArrayList<String>				controlsMapKeys = new ArrayList<String>();
  private JPanel					mainPanel       = new JPanel();

  /**
   * Constructs a new ConResAnalyzerView with a predefined controller.
   * @param controller
   */
  public PreferencesManagerView(PreferencesManager controller) {
    super(controller);

    createView();
  }

	/**
   * @return
   */
  public boolean canFinalize() {
    finalizable = (activeCalls == 0);
    GrasppeKit.debugText("Finalize / Active Calls", activeCalls + " remaining.");

    return finalizable;
  }
  
  HashMap<Tags, PreferencesFieldView> fieldMap = new HashMap<Tags, PreferencesFieldView>();

  /**
   * Builds the graphical user interface window and elements.
   */
  public void createView() {

    if (mainFrame != null) return;
    mainFrame = new JDialog(); //getController().getAnalyzer().getMainFrame(),"Preferences", true);

  mainFrame.setUndecorated(true);
//    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    EmptyBorder	emptyBorder = new EmptyBorder(10, 10, 10, 10);

    mainPanel.setBorder(emptyBorder);

    Tags[]	preferences = Tags.values();

    for (Tags preference : preferences) {
      PreferencesFieldView	fieldView = PreferencesFieldView.buildFieldView(preference, getController());
      fieldMap.put(preference, fieldView);
      mainPanel.add(fieldView);
    }
    
//    JPanel bottomPanel = new 
    
    // Create and initialize the buttons.
    final JButton	cancelButton = new JButton("Cancel");

    cancelButton.addActionListener(this);
    KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
    Action escapeAction = new AbstractAction() {
        // close the frame when the user presses escape
        public void actionPerformed(ActionEvent e) {
        	cancelButton.doClick();
        }
    }; 
    mainFrame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
    mainFrame.getRootPane().getActionMap().put("ESCAPE", escapeAction);
    
    final JButton	setButton = new JButton("Set");

    setButton.setActionCommand("Set");
    setButton.addActionListener(this);
    mainFrame.getRootPane().setDefaultButton(setButton);
    
    JPanel	buttonPane = new JPanel();

    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
    buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    buttonPane.add(Box.createHorizontalGlue());
    buttonPane.add(cancelButton);
    buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
    buttonPane.add(setButton);
    

    Container	pane = mainFrame.getContentPane();

    pane.add(mainPanel, BorderLayout.CENTER);
    pane.add(buttonPane, BorderLayout.PAGE_END);

    mainFrame.pack();
    mainFrame.setLocationRelativeTo(null);

    updateSize();

//  setContainer(getDefaultContainer());
  }

  /**
   */
  @Override
  protected void finalizeUpdates() {}

  /**
   * Completes graphical user interface operations before closing.
   * @return
   */
  public boolean finalizeView() {
    if (!canFinalize()) return false;

    return true;
  }

  /**
   */
  public void show() {
    if (mainFrame == null) return;
    
    SwingUtilities.invokeLater(new Runnable() {
		
		@Override
		public void run() {
		    mainFrame.pack();
		    mainFrame.setLocationRelativeTo(null);	  
		    mainFrame.setVisible(true);
		}
	});
  }

  /*
   *  (non-Javadoc)
   * @see com.grasppe.lure.components.AbstractView#update()
   */

  /**
   */
  @Override
  public void update() {
    updateSize();
    if (fieldMap!=null && fieldMap.size()>0)
    for (PreferencesFieldView field : fieldMap.values())
    	field.update();
    super.update();
  }

  /**
   */
  public void updateSize() {

//  if (mainFrame == null) return;
//  mainFrame.validate();
//  mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
  }

  /**
   *  @return
   */
  public Container getContentPane() {
    if (mainFrame != null) return mainFrame.getContentPane();
    else return null;
  }

  /**
   *  @return
   */
  public PreferencesManager getController() {
    return (PreferencesManager)controller;
  }

  /**
   *  @return
   */
  public Window getFrame() {
    if (mainFrame == null) createView();

    return mainFrame;
  }

  /**
   *  @return
   */
  @Override
  public PreferencesManagerModel getModel() {
    return (PreferencesManagerModel)super.getControllerModel();
  }

@Override
public void actionPerformed(ActionEvent e) {
    if ("Set".equals(e.getActionCommand())) {
//        ListDialog.value = (String)(list.getSelectedValue());
    	storeValues();
    } else {
//    	ListDialog.value = "";
    }
    mainFrame.setVisible(false);

	
}

protected void storeValues() {
    for (PreferencesFieldView field : fieldMap.values())
    	field.storeValue();
}

@Override
public void detatch(Observable oberservableObject) {
	// TODO Auto-generated method stub
	
}
}
