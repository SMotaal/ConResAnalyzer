/*
 * @(#)PatchGeneratorParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.matlab;

import com.grasppe.forms.fields.NumericValueField;
import com.grasppe.lure.framework.GrasppeKit.Observer;

import com.oracle.layout.SpringUtilities;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.text.ParseException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.hamcrest.core.IsNull;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ModulePanelContainer extends JPanel implements Observer {

///** Field description */
//public static ColumnSpec[] COLUMN_SPEC = new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("50dlu"),
//                                                            ColumnSpec.decode("2dlu"), ColumnSpec.decode("max(30dlu;default)"),
//                                                            ColumnSpec.decode("0dlu"), ColumnSpec.decode("30dlu:grow"),
//                                                            FormFactory.RELATED_GAP_COLSPEC, };
  // private JButton                           btnApply;
  protected LinkedList<ModuleParametersPanel> modules = new LinkedList<ModuleParametersPanel>();

  // GUI Components
  protected JPanel      containerPanel;
  protected JScrollPane scrollPane;
  protected JPanel      scrollPanel;
  protected JPanel      contentPanel;
  protected JPanel      buttonPanel;

  // Buttons
  protected JButton applyButton, addButton, removeButton, upButton, downButton;

  // GUI Appearance
  protected Font listFont    = new Font("Sans Serif", Font.PLAIN, 11);
  protected Font headingFont = listFont.deriveFont(
                                   Font.BOLD);		// .deriveFont(listFont.getSize()); //new Font("Sans Serif", Font.BOLD, 12);
  protected Color controlColor = SystemColor.control;
  protected float controlHSB[] = Color.RGBtoHSB(controlColor.getRed(), controlColor.getGreen(), controlColor.getBlue(), null);
  protected Color listColor    = Color.getHSBColor(controlHSB[0], controlHSB[1],
                                                Math.min(controlHSB[2] * 1.1f, 0.97f));			// 0.5f * ( 0.75f + hsbVals[2] ));
  protected Color                      headingColor      = Color.getHSBColor(controlHSB[0], controlHSB[1], 0.85f * controlHSB[2]);
  protected String                     buttonType        = "segmentedCapsule"; // "segmentedRoundRect";		// "segmentedTextured"
  protected String                     MacButtonType     = "JButton.buttonType";
  protected String                     MacButtonPosition = "JButton.segmentPosition";
  protected HashMap<KeyStroke, Action> keyMap            = new HashMap<KeyStroke, Action>();

  /**
   * Create the panel.
   */
  public ModulePanelContainer() {

//  GraphicsEnvironment e           = GraphicsEnvironment.getLocalGraphicsEnvironment();
//
//  
//
//  Border              panelBorder = new EtchedBorder(EtchedBorder.LOWERED, null, null);         // new LineBorder(SystemColor.controlShadow, 1, true);

    initializePanel();

    createPanelContainer();

    createPanels();

    createButtons();

  }

  /**
   * @param arg0
   * @return
   * @see java.util.LinkedList#add(java.lang.Object)
   */
  public boolean add(ModuleParametersPanel arg0) {
    return modules.add(arg0);
  }

  /**
   *     @param arg0
   *     @param arg1
   *     @see java.util.LinkedList#add(int, java.lang.Object)
   */
  public void add(int arg0, ModuleParametersPanel arg1) {
    modules.add(arg0, arg1);
  }

  /**
   *    @param keyStroke
   *    @param action
   */
  private void attachKeyListener(KeyStroke keyStroke, Action action) {
    InputMap input = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    String   name  = ((String)action.getValue(Action.NAME)).toLowerCase();

    input.put(keyStroke, name);
    this.getActionMap().put(name, action);

    keyMap.put(keyStroke, action);
  }

  /**
   *
   * @see java.util.LinkedList#clear()
   */
  public void clear() {
    modules.clear();
  }

  /**
   * @param arg0
   * @return
   * @see java.util.LinkedList#contains(java.lang.Object)
   */
  public boolean contains(Object arg0) {
    return modules.contains(arg0);
  }

  /**
   *    @param text
   *    @param name
   *    @param keyStroke
   *    @return
   */
  protected JButton createButton(String text, String name, KeyStroke keyStroke) {
    final JButton button = new JButton(text);

    Action        action = new AbstractAction(name) {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

        if (focusOwner instanceof NumericValueField) {
          NumericValueField textOwner = ((NumericValueField)focusOwner);

          try {
            textOwner.commitEdit();
          } catch (ParseException exception) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            textOwner.requestFocus();

            return;
          }

          KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
          textOwner.requestFocus();

        }

        button.doClick();

      }
    };

    button.setFocusable(false);

    attachKeyListener(keyStroke, action);

    // KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);

    return button;
  }

  /**
   */
  protected void createButtons() {

    // buttonPanel.setLayout(new SpringLayout());

    addButton = this.createButton("+", "Add",
            KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.META_DOWN_MASK));		// KeyEvent.VK_A,0)); // KeyEvent.META_DOWN_MASK));

    removeButton = this.createButton("Ñ", "Remove",
            KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,
              KeyEvent.META_DOWN_MASK));		// KeyEvent.VK_A,0)); // KeyEvent.META_DOWN_MASK));

    upButton    = this.createButton("\u2191", "MoveUp", KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.META_DOWN_MASK));

    downButton  = this.createButton("\u2193", "MoveDown", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.META_DOWN_MASK));

    applyButton = this.createButton("Apply", "Apply", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
    
//    //applyButton.setIcon(new Icon)
//    try {
//    	applyButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("NSImage://NSQuickLookTemplate")));
//    	applyButton.setToolTipText(applyButton.getText());
//    	applyButton.setText("");
//    } finally {}
    setNSButtonImage(addButton, "NSAddTemplate", "New");
    setNSButtonImage(removeButton, "NSRemoveTemplate", "Delete");
    setNSButtonImage(upButton, "NSLeftFacingTriangleTemplate", "Move Up");
    setNSButtonImage(downButton, "NSRightFacingTriangleTemplate", "Move Down");
    setNSButtonImage(applyButton, "NSQuickLookTemplate", "Apply");

    addButton.putClientProperty(MacButtonType, buttonType);
    addButton.putClientProperty(MacButtonPosition, "first");
    upButton.putClientProperty(MacButtonType, buttonType);
    upButton.putClientProperty(MacButtonPosition, "middle");
    downButton.putClientProperty(MacButtonType, buttonType);
    downButton.putClientProperty(MacButtonPosition, "middle");
    removeButton.putClientProperty(MacButtonType, buttonType);
    removeButton.putClientProperty(MacButtonPosition, "last");

    applyButton.putClientProperty(MacButtonType, buttonType);
    applyButton.putClientProperty(MacButtonPosition, "middle");

    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(addButton);
    buttonPanel.add(upButton);
    buttonPanel.add(applyButton);
    buttonPanel.add(downButton);
    buttonPanel.add(removeButton);
    buttonPanel.add(Box.createHorizontalGlue());

    // buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

    

    // applyButton.setVisible(false);
//  addButton.addComponentListener(new ComponentAdapter() {
//
//    /* (non-Javadoc)
//     * @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
//     */
//    @Override
//    public void componentResized(ComponentEvent arg0) {
//        addButton.setMaximumSize(new Dimension(addButton.getHeight()+1, addButton.getHeight()));
//        addButton.setPreferredSize(new Dimension(addButton.getHeight()+1, addButton.getHeight()));
//        addButton.setSize(new Dimension(addButton.getHeight()+1, addButton.getHeight()));
//        super.componentResized(arg0);
//    }
//    
//  });

//  applyButton = new JButton("Apply");
//
//  
//
//  Action applyAction = new AbstractAction("Apply") {
//
//    @Override
//    public void actionPerformed(ActionEvent arg0) {
//      applyButton.doClick();
//    }
//  };
//  
//  applyButton.setFocusable(false);
//
//  attachKeyListener(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0), applyAction);

  }
  
  public void setNSButtonImage(JButton button, String imageName, String toolText) {
	  
	    //applyButton.setIcon(new Icon)
	    try {
	    	button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("NSImage://" + imageName)));
	    	
	    	if (toolText==null || toolText.isEmpty()) {
		    	button.setToolTipText(button.getText());
	    	} else {
	    		button.setToolTipText(toolText);
	    	}
	    		
		    	button.setText("");
	    } finally {}	  
	  
  }

  /**
   *    @param panel
   *    @param title
   */
  protected void createPanel(ModuleParametersPanel panel, String title) {
    if (this.contains(panel)) return;

    panel.setTitle(title);

    this.add(panel);

    this.updatePanels();
  }

  /**
   */
  protected void createPanelContainer() {

    // Content Container
    contentPanel = new JPanel();
    contentPanel.setLayout(new SpringLayout());
    contentPanel.setOpaque(false);

    // Button Container
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setOpaque(false);

    // Scroll Pane
    scrollPanel = new JPanel();
    scrollPanel.add(contentPanel);
    scrollPanel.add(Box.createVerticalGlue());
    scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
    scrollPanel.setBackground(listColor);

    scrollPane = new JScrollPane(scrollPanel);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setBorder(null);			// new LineBorder(SystemColor.controlShadow));
    scrollPane.setBackground(listColor);

    // Parent Container
    containerPanel = this;
    containerPanel.setLayout(new BorderLayout());

    // containerPanel.setBorder(new EmptyBorder(1,1,1,1));

    containerPanel.add(scrollPane, BorderLayout.CENTER);
    containerPanel.add(buttonPanel, BorderLayout.SOUTH);

    // containerPanel.setBackground(SystemColor.controlLtHighlight);

    // Auto-Scroll Focus Behavior
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (!(evt.getNewValue() instanceof JComponent)) return;

        JComponent focused = (JComponent)evt.getNewValue();

        if (contentPanel.isAncestorOf(focused)) {
          ((JComponent)focused.getParent().getParent()).scrollRectToVisible(focused.getParent().getBounds());

          Iterator<KeyStroke> keyIterator = keyMap.keySet().iterator();

          while (keyIterator.hasNext()) {
            KeyStroke keyStroke = keyIterator.next();

            try {
              InputMap input  = focused.getInputMap(WHEN_FOCUSED);
              Action   action = keyMap.get(keyStroke);
              String   name   = ((String)action.getValue(Action.NAME)).toLowerCase();

              input.put(keyStroke, name);
              focused.getActionMap().put(name, action);
            } finally {}

          }

        }

//      Object keys = focused.getActionMap().;

        // focused.getActionMap().keys()

      }

    });

  }

  /**
   */
  protected void createPanels() {
    int rows    = 25;
    int columns = 1;

    for (int i = 1; i < rows + 1; i++) {

      // JLabel newLabel = new JLabel(i + "");
      ModuleParametersPanel newPanel;

      if ((i) % 4 == 0) newPanel = new ScanningParametersPanel();
      else if ((i) % 3 == 0) newPanel = new PrintingParametersPanel();
      else if ((i) % 2 == 0) newPanel = new ScreeningParametersPanel();
      else newPanel = new PatchParametersPanel();

      this.createPanel(newPanel, i + "");

//    newPanel.setBorder(BorderFactory.createTitledBorder(i + ""));
//
//    contentPanel.add(newPanel);

    }

    // SpringUtilities.makeCompactGrid(contentPanel, rows, columns, 3, 3, 3, 3);
  }

  /**
   */
  protected void initializePanel() {
    this.addComponentListener(new ComponentAdapter() {

      /*
       *  (non-Javadoc)
       * @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
       */
      @Override
      public void componentResized(ComponentEvent e) {
        JComponent component     = (JComponent)e.getComponent();

        Dimension  preferredSize = component.getPreferredSize();
        Dimension  maximumSize   = component.getMaximumSize();		// getPreferredSize();
        Dimension  newSize       = new Dimension(preferredSize.width, maximumSize.height);

        component.setMaximumSize(newSize);
      }
    });
  }

  /**
   * @return
   * @see java.util.AbstractSequentialList#iterator()
   */
  public Iterator<ModuleParametersPanel> iterator() {
    return modules.iterator();
  }

  /**
   *    @param args
   */
  public static void main(String[] args) {

    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        JFrame frame = new JFrame("PatchGeneratorParametersPanel Demo");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add contents to the window.
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new ModulePanelContainer(), BorderLayout.EAST);

        // Display the window.
        frame.pack();

//      Dimension  preferredSize = frame.getPreferredSize();
//      Dimension  maximumSize   = frame.getMaximumSize();        // getPreferredSize();
//      Dimension  newSize       = new Dimension(maximumSize.width, preferredSize.height);
//      
//      frame.setMaximumSize(maximumSize);

        frame.setVisible(true);
      }

    });
  }

  /**
   * @param arg0
   * @see java.util.LinkedList#remove(int)
   */
  public void remove(int arg0) {
    modules.remove(arg0);
  }

  /**
   * @param arg0
   * @return
   * @see java.util.LinkedList#remove(java.lang.Object)
   */
  public boolean remove(Object arg0) {
    return modules.remove(arg0);
  }

  /**
   */
  @Override
  public void update() {

//  System.out.println("Update!");        // this.getName())
  }

  /**
   */
  protected void updateGrid() {
    int rows    = contentPanel.getComponentCount();
    int columns = 1;

    SpringUtilities.makeCompactGrid(contentPanel, rows, columns, 3, 3, 3, 3);
  }

  /**
   */
  protected void updatePanels() {
    contentPanel.removeAll();

    contentPanel.setFont(listFont);

    Iterator<ModuleParametersPanel> panelIterator = this.iterator();

    while (panelIterator.hasNext()) {
      ModuleParametersPanel panel = panelIterator.next();
      JLabel                label = new JLabel(panel.getTitle());

      label.setOpaque(true);
      label.setFont(headingFont);
      label.setBackground(headingColor);
      label.setBorder(new EmptyBorder(0, 2, 0, 2));			// LineBorder(label.getBackground(), 1, true));
      label.setHorizontalTextPosition(JLabel.CENTER);

      contentPanel.add(label);

      panel.setOpaque(false);														// panel.setBorder(BorderFactory.createTitledBorder(panel.getTitle()));
      panel.setFont(contentPanel.getFont());
      contentPanel.add(panel);
    }

    this.updateGrid();
  }

  /**
   * @param arg0
   * @return
   * @see java.util.LinkedList#get(int)
   */
  public ModuleParametersPanel get(int arg0) {
    return modules.get(arg0);
  }

///**
// * @param scanningParametersPanel the scanningParametersPanel to set
// */
//public void setScanningParametersPanel(
//        ScanningParametersPanel scanningParametersPanel) {
//    this.scanningParametersPanel = scanningParametersPanel;
//}

  /**
   *     @return the btnApply
   */
  public JButton getApplyButton() {
    return applyButton;
  }

  /**
   * @return
   * @see java.util.LinkedList#getFirst()
   */
  public ModuleParametersPanel getFirst() {
    return modules.getFirst();
  }

  /**
   * @return
   * @see java.util.LinkedList#getLast()
   */
  public ModuleParametersPanel getLast() {
    return modules.getLast();
  }

  /**
   * @return
   * @see java.util.AbstractCollection#isEmpty()
   */
  public boolean isEmpty() {
    return modules.isEmpty();
  }

  /**
   * @param arg0
   * @param arg1
   * @return
   * @see java.util.LinkedList#set(int, java.lang.Object)
   */
  public ModuleParametersPanel set(int arg0, ModuleParametersPanel arg1) {
    return modules.set(arg0, arg1);
  }

//
///**
// * @param btnApply the btnApply to set
// */
//public void setBtnApply(JButton btnApply) {
//    this.btnApply = btnApply;
//}

///**
// * @param screeningParametersPanel the screeningParametersPanel to set
// */
//public void setScreeningParametersPanel(
//        ScreeningParametersPanel screeningParametersPanel) {
//    this.screeningParametersPanel = screeningParametersPanel;
//}

///**
// * @param printingParametersPanel the printingParametersPanel to set
// */
//public void setPrintingParametersPanel(
//        PrintingParametersPanel printingParametersPanel) {
//    this.printingParametersPanel = printingParametersPanel;
//}

///**
// * @param patchParametersPanel the patchParametersPanel to set
// */
//public void setPatchParametersPanel(PatchParametersPanel patchParametersPanel) {
//    this.patchParametersPanel = patchParametersPanel;
//}

}
