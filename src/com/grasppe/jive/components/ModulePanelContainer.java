/*
 * @(#)PatchGeneratorParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.components;

import com.grasppe.conreslabs.panels.imageprocessors.FourierParametersPanel;
import com.grasppe.conreslabs.panels.imageprocessors.FunctionParametersPanel;
import com.grasppe.conreslabs.panels.imageprocessors.JiveParametersPanel;
import com.grasppe.conreslabs.panels.patchgenerator.PatchParametersPanel;
import com.grasppe.conreslabs.panels.patchgenerator.PrintingParametersPanel;
import com.grasppe.conreslabs.panels.patchgenerator.ScanningParametersPanel;
import com.grasppe.conreslabs.panels.patchgenerator.ScreeningParametersPanel;
import com.grasppe.jive.fields.NumericValueField;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observer;

import com.oracle.layout.SpringUtilities;

//~--- JDK imports ------------------------------------------------------------

import com.sun.snippets.ListDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.text.ParseException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ModulePanelContainer extends JPanel implements Observer, ActionListener, PropertyChangeListener {

  protected static int addNumber = 0;

///** Field description */
//public static ColumnSpec[] COLUMN_SPEC = new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("50dlu"),
//                                                            ColumnSpec.decode("2dlu"), ColumnSpec.decode("max(30dlu;default)"),
//                                                            ColumnSpec.decode("0dlu"), ColumnSpec.decode("30dlu:grow"),
//                                                            FormFactory.RELATED_GAP_COLSPEC, };
  // private JButton                           btnApply;
  protected LinkedList<JiveParametersPanel> modules = new LinkedList<JiveParametersPanel>();

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
  protected Color                                headingColor      = Color.getHSBColor(controlHSB[0], controlHSB[1], 0.85f * controlHSB[2]);
  protected String                               buttonType        = "segmentedCapsule";		// "segmentedRoundRect";     // "segmentedTextured"
  protected String                               MacButtonType     = "JButton.buttonType";
  protected String                               MacButtonPosition = "JButton.segmentPosition";
  protected HashMap<KeyStroke, Action>           keyMap            = new HashMap<KeyStroke, Action>();
  private int                                    moduleHashCode    = 0;
  protected HashMap<String, JiveParametersPanel> modulesHashMap    = null;

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
   * 	@param e
   */
  @Override
  public void actionPerformed(ActionEvent e) {

    Object source = e.getSource();

    if (source.equals(applyButton)) {
      GrasppeKit.debugText("ContainerPanel>" + e.getActionCommand(), getValues().toString(), 1);
    }

    if (source.equals(addButton)) {
      GrasppeKit.debugText("ContainerPanel>" + e.getActionCommand(), getValues().toString(), 1);

      // setValues(getValues());
      addNewPanel();
    }

    if (source.equals(removeButton)) {
      GrasppeKit.debugText("ContainerPanel>" + e.getActionCommand(), getValues().toString(), 1);
      removeActivePanel();
    }

    if (source.equals(downButton)) {
      GrasppeKit.debugText("ContainerPanel>" + e.getActionCommand(), getValues().toString(), 1);
      moveActivePanel(1);
    }

    if (source.equals(upButton)) {
      GrasppeKit.debugText("ContainerPanel>" + e.getActionCommand(), getValues().toString(), 1);
      moveActivePanel(-1);
    }

    GrasppeKit.debugText("ContainerPanel>" + e.getActionCommand(), e.toString(), 1);

  }

  /**
   * @param module
   * @return
   * @see java.util.LinkedList#add(java.lang.Object)
   */
  public boolean add(JiveParametersPanel module) {
    return modules.add(module);
  }

  /**
   *     @param index
   *     @param module
   *     @see java.util.LinkedList#add(int, java.lang.Object)
   */
  public void add(int index, JiveParametersPanel module) {
    modules.add(index, module);
  }

  /**
   */
  protected void addNewPanel() {
    String[] panelTypes = { "Function", "Fourier" };

    String   panelType  = ListDialog.showDialog(null, null, "Available Components", "Create New Component", panelTypes, "Function",
                                             "Function         ");

    createNewPanel(panelType, null);

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
      public void actionPerformed(ActionEvent e) {
        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

//      System.out.println(focusOwner);

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

        if (focusOwner instanceof JComboBox) {
          JComboBox listOwner = (JComboBox)focusOwner;

          try {
            InputMap input = listOwner.getInputMap();

//          System.out.println(input.keys());
          } catch (Exception exception) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            listOwner.requestFocus();

            return;
          }

        }

        button.doClick();

      }

    };

    action.putValue(Action.NAME, name + "-Button");

    button.setFocusable(false);

    attachKeyListener(keyStroke, action);

    button.addActionListener(this);

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

//  //applyButton.setIcon(new Icon)
//  try {
//    applyButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("NSImage://NSQuickLookTemplate")));
//    applyButton.setToolTipText(applyButton.getText());
//    applyButton.setText("");
//  } finally {}
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

  /**
   * 	@param panelType
   * 	@param index
   * 	@return
   */
  protected JiveParametersPanel createNewPanel(String panelType, Integer index) {
    JiveParametersPanel newPanel = null;

    if (panelType.equals("Function")) newPanel = new FunctionParametersPanel();
    else if (panelType.equals("Fourier")) newPanel = new FourierParametersPanel();

    if (newPanel == null) return null;

    GrasppeKit.debugText("ContainerPanel>Add", "Adding new " + newPanel.getClass().getSimpleName(), 1);

    Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

    String    title      = newPanel.getTitle();

    if (getModules().keySet().contains(title)) newPanel.setTitle(title + "-" + addNumber++);

    createPanel(newPanel, newPanel.getTitle(), index);

    focusOwner.requestFocus();

    return newPanel;
  }

  /**
   *    @param panel
   *    @param title
   * 	@param index
   */
  protected void createPanel(JiveParametersPanel panel, String title, Integer index) {
    if (this.contains(panel)) return;

    if ((title != null) &&!title.isEmpty()) panel.setTitle(title);

    panel.addPropertyChangeListener("Title", this);

    if (index == null) this.add(panel);
    else this.add(index, panel);

    this.updatePanels();
  }

  /**
   */
  protected void createPanelContainer() {

    // Content Container
    contentPanel = new JPanel();

//  contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); //(new SpringLayout());
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
    scrollPane.add(Box.createVerticalGlue());

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

        JComponent focusOwner = (JComponent)evt.getNewValue();

        if (contentPanel.isAncestorOf(focusOwner)) {
          ((JComponent)focusOwner.getParent().getParent()).scrollRectToVisible(focusOwner.getParent().getBounds());

          try {
            ModuleParametersPanel panel = (ModuleParametersPanel)SwingUtilities.getAncestorOfClass(ModuleParametersPanel.class,
                                                                                                   focusOwner);

            panel.makeActive();
          } catch (Exception exception) {
            GrasppeKit.debugError("ContainerPanel>Focus", exception, 2);
          }

          Iterator<KeyStroke> keyIterator = keyMap.keySet().iterator();

          while (keyIterator.hasNext()) {
            KeyStroke keyStroke = keyIterator.next();

            try {
              InputMap     ownerInput = focusOwner.getInputMap(WHEN_FOCUSED);
              Action       action     = keyMap.get(keyStroke);
              final String name       = ((String)action.getValue(Action.NAME)).toLowerCase();

              if (focusOwner instanceof JComponent) {			// (!(focusOwner instanceof NumericValueField)) {
                final JComponent owner          = (JComponent)focusOwner;
                final Action     localAction    = action;
                final Object     ownerActionKey = ownerInput.get(keyStroke);

                if (ownerActionKey != null) {

//                System.out.println(ownerActionKey);

                  try {

                    final String ownerActionName;

                    if (!(ownerActionKey instanceof String)) ownerActionName = ownerActionKey.toString();
                    else ownerActionName = (String)ownerActionKey;

                    if (!((String)ownerActionName).startsWith("wrap-" + name)) {
                      Action wrapperAction = new AbstractAction(name) {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                          ActionEvent e2 = new ActionEvent(e.getSource(), e.getID(), "Test", e.getWhen(), e.getModifiers());

                          // e2.
                          owner.getActionMap().get(ownerActionKey).actionPerformed(e);
                          localAction.actionPerformed(e2);
                        }
                      };

                      String wrapperName = "wrap-" + name + "-" + ownerActionName;

                      ownerInput.put(keyStroke, wrapperName);
                      owner.getActionMap().put(wrapperName, wrapperAction);
                    }
                  } catch (Exception exception) {
                    System.out.println(exception);
                  }

                } else {

//                System.out.println(focusOwner);
//                  ownerInput.put(keyStroke, name);
//                  focusOwner.getActionMap().put(name, action);
                }
              } else {

//              System.out.println(focusOwner);

              }

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
      JiveParametersPanel newPanel;

      if ((i) % 4 == 0) newPanel = new ScanningParametersPanel();
      else if ((i) % 3 == 0) newPanel = new PrintingParametersPanel();
      else if ((i) % 2 == 0) newPanel = new ScreeningParametersPanel();
      else newPanel = new PatchParametersPanel();

      this.createPanel(newPanel, i + "", null);

    }

  }

  /**
   *    @param panel
   *    @param title
   */
  protected void createPermanentPanel(JiveParametersPanel panel, String title) {
    panel.setPermanent(true);
    createPanel(panel, title, null);
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
  public Iterator<JiveParametersPanel> iterator() {
    return modules.iterator();
  }

  /**
   * 	@param steps
   */
  protected void moveActivePanel(int steps) {

    if (steps == 0) return;

    JiveParametersPanel activePanel = (JiveParametersPanel)JiveParametersPanel.activePanel;

    if ((activePanel == null) || (modules == null)) return;

    if (!modules.contains(activePanel)) return;

    int currentIndex = modules.indexOf(activePanel);
    int newIndex     = currentIndex + steps;

    GrasppeKit.debugText("ContainerPanel>MovePanel", activePanel.title + " " + currentIndex + " > " + newIndex, 1);

    if ((newIndex < 0) || (newIndex > modules.size() - 1)) return;

    Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

    modules.remove(currentIndex);

    modules.add(newIndex, activePanel);

    updatePanels();

    focusOwner.requestFocus();
  }

  /**
   * 	@param evt
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {

    if (evt.getPropertyName().equals("Title")) {
      moduleHashCode = 0;
    }

  }

///**
// *    @param args
// */
//public static void main(String[] args) {
//
//  // Schedule a job for the event dispatch thread:
//  // creating and showing this application's GUI.
//  SwingUtilities.invokeLater(new Runnable() {
//
//    public void run() {
//
//      JFrame frame = new JFrame("PatchGeneratorParametersPanel Demo");
//
//      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//      // Add contents to the window.
//      frame.getContentPane().setLayout(new BorderLayout());
//      frame.getContentPane().add(new ModulePanelContainer(), BorderLayout.EAST);
//
//      // Display the window.
//      frame.pack();
//
////      Dimension  preferredSize = frame.getPreferredSize();
////      Dimension  maximumSize   = frame.getMaximumSize();        // getPreferredSize();
////      Dimension  newSize       = new Dimension(maximumSize.width, preferredSize.height);
////      
////      frame.setMaximumSize(maximumSize);
//
//      frame.setVisible(true);
//    }
//
//  });
//}

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
  protected void removeActivePanel() {
    JiveParametersPanel activePanel = (JiveParametersPanel)JiveParametersPanel.activePanel;

    if ((activePanel == null) || (modules == null)) return;

    if (activePanel.isPermanent()) return;

    if (!modules.contains(activePanel)) return;

    int currentIndex = modules.indexOf(activePanel);

    GrasppeKit.debugText("ContainerPanel>RemovePanel", activePanel.title + " will be removed", 1);

    modules.remove(currentIndex);

    updatePanels();

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
//    SpringUtilities.makeGrid(contentPanel, rows, columns, 3, 3, 3, 3);
  }

  /**
   */
  protected void updatePanels() {
    contentPanel.removeAll();

    contentPanel.setFont(listFont);

    contentPanel.setSize(new Dimension(400, contentPanel.getSize().height));

    Iterator<JiveParametersPanel> panelIterator = this.iterator();

    while (panelIterator.hasNext()) {
      final JiveParametersPanel panel = panelIterator.next();
      final JLabel              label = new JLabel(panel.getTitle());

      label.setOpaque(true);
      label.setFont(headingFont);
      label.setBackground(headingColor);
      label.setBorder(new EmptyBorder(0, 2, 0, 2));			// LineBorder(label.getBackground(), 1, true));

      panel.addPropertyChangeListener("Title", new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          label.setText((String)evt.getNewValue());
        }
      });

      contentPanel.add(label);

      panel.setOpaque(false);			// panel.setBorder(BorderFactory.createTitledBorder(panel.getTitle()));
      panel.setFont(contentPanel.getFont());
      contentPanel.add(panel);

      updateGrid();

//      
      revalidate();

      panel.flowResize();

//    panel.setMaximumSize(new Dimension(600, 99999));
//    
//    panel.validate();
//    panel.setMinimumSize(panel.getPreferredSize());
    }

    contentPanel.add(Box.createVerticalGlue());

    revalidate();

  }

  /**
   * @param arg0
   * @return
   * @see java.util.LinkedList#get(int)
   */
  public JiveParametersPanel get(int arg0) {
    return modules.get(arg0);
  }

///**
// * @param scanningParametersPanel the scanningParametersPanel to set
// */
//public void setScanningParametersPanel(
//        FourierParametersPanelAlpha scanningParametersPanel) {
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
  public JiveParametersPanel getFirst() {
    return modules.getFirst();
  }

  /**
   * @return
   * @see java.util.LinkedList#getLast()
   */
  public JiveParametersPanel getLast() {
    return modules.getLast();
  }

  /**
   * 	@return
   */
  public HashMap<String, JiveParametersPanel> getModules() {

    if (modules.hashCode() != moduleHashCode) {
      GrasppeKit.debugText("ContainerPanel>Map", "Generating new hash map for hash code: " + modules.hashCode(), 2);

      modulesHashMap = new HashMap<String, JiveParametersPanel>();

      Iterator<JiveParametersPanel> panelIterator = this.iterator();

      while (panelIterator.hasNext()) {
        JiveParametersPanel module = panelIterator.next();
        String              name   = module.getTitle();

        modulesHashMap.put(name, module);
      }

      moduleHashCode = modules.hashCode();
    }

    return modulesHashMap;
  }

  /**
   * 	@return
   */
  public LinkedHashMap<String, HashMap> getValues() {
    LinkedHashMap<String, HashMap> values = new LinkedHashMap<String, HashMap>();

    // int                     i      = -1;

    Iterator<JiveParametersPanel> panelIterator = this.iterator();

    while (panelIterator.hasNext()) {
      JiveParametersPanel module = panelIterator.next();
      String              name   = module.getTitle();

      values.put(name, module.getValues());
    }

    return values;
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
  public JiveParametersPanel set(int arg0, JiveParametersPanel arg1) {
    return modules.set(arg0, arg1);
  }

  /**
   *    @param button
   *    @param imageName
   *    @param toolText
   */
  public void setNSButtonImage(JButton button, String imageName, String toolText) {

    // applyButton.setIcon(new Icon)
    try {
      button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("NSImage://" + imageName)));

      if ((toolText == null) || toolText.isEmpty()) {
        button.setToolTipText(button.getText());
      } else {
        button.setToolTipText(toolText);
      }

      button.setText("");
    } finally {}

  }

  /**
   * 	@param allValues
   */
  public void setValues(HashMap<String, HashMap> allValues) {

    for (String moduleName : allValues.keySet()) {
      if (getModules().containsKey(moduleName)) {
        JiveParametersPanel      module       = getModules().get(moduleName);
        HashMap<String, HashMap> moduleValues = allValues.get(moduleName);

        for (String propertyName : moduleValues.keySet()) {
          try {
            Object value = moduleValues.get(propertyName);

            if (value instanceof Number) value = (Double)value + 0.1;

            module.setValue(propertyName, value);
          } catch (Exception exception) {
            GrasppeKit.debugError("ContainerPanel>Map", exception, 1);
          }
        }
      } else
        GrasppeKit.debugText("ContainerPanel>Set", "Module " + moduleName + " is not found!", 2);
    }

  }
}
