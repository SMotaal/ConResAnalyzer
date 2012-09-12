/*
 * @(#)PatchGeneratorParametersPanel.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.components;

import com.google.common.base.Preconditions;

import com.grasppe.conreslabs.panels.imageprocessors.DisplayModulePanel;
import com.grasppe.conreslabs.panels.imageprocessors.FourierModulePanel;
import com.grasppe.conreslabs.panels.imageprocessors.FunctionModulePanel;
import com.grasppe.conreslabs.panels.patchgenerator.PatchModulePanel;
import com.grasppe.conreslabs.panels.patchgenerator.PrintingModulePanel;
import com.grasppe.conreslabs.panels.patchgenerator.ScanningModulePanel;
import com.grasppe.conreslabs.panels.patchgenerator.ScreeningModulePanel;
import com.grasppe.jive.fields.JiveNumericField;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observer;

import com.javapractices.snippets.TextTransfer;

import com.oracle.layout.SpringUtilities;

//~--- JDK imports ------------------------------------------------------------

import com.sun.snippets.TextEditorDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.lang.reflect.Method;

import java.text.ParseException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class JiveModuleContainer extends JPanel implements Observer, ActionListener, PropertyChangeListener {

  protected static int  addNumber = 0;
  private static Logger logger    = Logger.getLogger("com.grasppe.Jive.JiveModuleContainer");

///** Field description */
//public static ColumnSpec[] COLUMN_SPEC = new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("50dlu"),
//                                                            ColumnSpec.decode("2dlu"), ColumnSpec.decode("max(30dlu;default)"),
//                                                            ColumnSpec.decode("0dlu"), ColumnSpec.decode("30dlu:grow"),
//                                                            FormFactory.RELATED_GAP_COLSPEC, };
  // private JButton                           btnApply;
  protected LinkedList<JiveModulePanel> modules = new LinkedList<JiveModulePanel>();

  // GUI Components
  protected JPanel      containerPanel;
  protected JScrollPane scrollPane;
  protected JPanel      scrollPanel;
  protected JPanel      contentPanel;
  protected JPanel      buttonPanel;

  // Buttons
  protected JButton applyButton, addButton, removeButton, upButton, downButton, copyButton, pasteButton;

  // GUI Appearance
  protected Font listFont    = new Font("Sans Serif", Font.PLAIN, 11);
  protected Font headingFont = listFont.deriveFont(
                                   Font.BOLD);		// .deriveFont(listFont.getSize()); //new Font("Sans Serif", Font.BOLD, 12);
  protected Color controlColor = SystemColor.control;
  protected float controlHSB[] = Color.RGBtoHSB(controlColor.getRed(), controlColor.getGreen(), controlColor.getBlue(), null);
  protected Color listColor    = Color.getHSBColor(controlHSB[0], controlHSB[1],
                                                Math.min(controlHSB[2] * 1.1f, 0.97f));			// 0.5f * ( 0.75f + hsbVals[2] ));
  protected Color                            headingColor      = Color.getHSBColor(controlHSB[0], controlHSB[1], 0.85f * controlHSB[2]);
  protected String                           buttonType        = "segmentedCapsule";		// "segmentedRoundRect";     // "segmentedTextured"
  protected String                           MacButtonType     = "JButton.buttonType";
  protected String                           MacButtonPosition = "JButton.segmentPosition";
  protected HashMap<KeyStroke, Action>       keyMap            = new HashMap<KeyStroke, Action>();
  private int                                moduleHashCode    = 0;
  protected HashMap<String, JiveModulePanel> modulesHashMap    = null;
  protected boolean                          debugBorders      = false;

  /**
   * Create the panel.
   */
  public JiveModuleContainer() {

    initializePanel();

    createPanelContainer();

    createPanels();

    createButtons();

  }

  /**
   *    @param e
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

    if (source.equals(copyButton)) {
      GrasppeKit.debugText("ContainerPanel>" + e.getActionCommand(), getValues().toString(), 1);
      editValues();

//    copyValues();
    }

    if (source.equals(pasteButton)) {
      GrasppeKit.debugText("ContainerPanel>" + e.getActionCommand(), getValues().toString(), 1);

//    pasteValues();
      updateGrid();
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
  public boolean add(JiveModulePanel module) {
    return modules.add(module);
  }

  /**
   *     @param index
   *     @param module
   *     @see java.util.LinkedList#add(int, java.lang.Object)
   */
  public void add(int index, JiveModulePanel module) {
    modules.add(index, module);
  }

  /**
   */
  protected void addNewPanel() {
    String[] panelTypes = { "Function", "Fourier", "Display" };

//  String   panelType  = ListDialog.showDialog(null, null, "Available Components", "Create New Component", panelTypes, "Function",
//                                           "Function         ");
//  createNewPanel(panelType, null);

    createNewPanel("Function", null);

    firePropertyChange("Panel.Layout", null, null);

  }

  /**
   *    @param array
   *    @return
   */
  public String arrayString(Object[] array) {
    return Arrays.asList(array).toString();
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
   */
  protected void copyValues() {

    String valueString = getValueString().toString().trim();

    valueString = valueString.substring(1, valueString.length() - 1);

    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(valueString), new ClipboardOwner() {

      @Override
      public void lostOwnership(Clipboard clipboard, Transferable contents) {

        // TODO Auto-generated method stub

      }
    });

    pasteValues();

//  valueString  = TextEditorDialog.showDialog(null, null, "Module Configuration", "Edit Configuration", valueString);
//      
//  Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(valueString), new ClipboardOwner() {
//    
//    @Override
//    public void lostOwnership(Clipboard clipboard, Transferable contents) {
//        // TODO Auto-generated method stub
//        
//    }
//  });
//  
//  getToolkit().getSystemClipboard().setContents(TextTransfer., owner)

    // createNewPanel(panelType, null);

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

        if (focusOwner instanceof JiveNumericField) {
          JiveNumericField textOwner = ((JiveNumericField)focusOwner);

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
    addButton.setName("AddPanelButton");

    removeButton = this.createButton("Ñ", "Remove",
            KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,
              KeyEvent.META_DOWN_MASK));		// KeyEvent.VK_A,0)); // KeyEvent.META_DOWN_MASK));
    removeButton.setName("RemovePanelButton");

    upButton = this.createButton("\u2191", "MoveUp", KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.META_DOWN_MASK));
    upButton.setName("MovePanelUpButton");

    downButton = this.createButton("\u2193", "MoveDown", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.META_DOWN_MASK));
    downButton.setName("MovePanelDownButton");

    applyButton = this.createButton("\u23ce", "Apply", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
    applyButton.setName("ApplySettingsButton");

    copyButton = this.createButton("\u2397", "Copy",
                                   KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
    copyButton.setName("CopySettingsButton");

    pasteButton = this.createButton("\u2398", "Paste",
                                    KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
    pasteButton.setName("PasteSettingsButton");

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
    setNSButtonImage(copyButton, "NSSmartBadgeTemplate", "Copy Setup");			// NSActionTemplate //NSRevealFreestandingTemplate
    setNSButtonImage(pasteButton, "NSRefreshTemplate", "Paste Setup");			// NSRefreshFreestandingTemplate
    setNSButtonImage(applyButton, "NSQuickLookTemplate", "Apply");

    if (isMacOSX()) {
      addButton.putClientProperty(MacButtonType, buttonType);
      addButton.putClientProperty(MacButtonPosition, "first");

      //
      upButton.putClientProperty(MacButtonType, buttonType);
      upButton.putClientProperty(MacButtonPosition, "middle");
      downButton.putClientProperty(MacButtonType, buttonType);
      downButton.putClientProperty(MacButtonPosition, "middle");
      removeButton.putClientProperty(MacButtonType, buttonType);
      removeButton.putClientProperty(MacButtonPosition, "last");

      //
      copyButton.putClientProperty(MacButtonType, buttonType);
      copyButton.putClientProperty(MacButtonPosition, "only");
      pasteButton.putClientProperty(MacButtonType, buttonType);
      pasteButton.putClientProperty(MacButtonPosition, "middle");

      //
      applyButton.putClientProperty(MacButtonType, buttonType);
      applyButton.putClientProperty(MacButtonPosition, "only");
    }

    buttonPanel.add(copyButton);
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(addButton);

//  buttonPanel.add(copyButton);
    buttonPanel.add(upButton);

//  buttonPanel.add(applyButton);
    buttonPanel.add(downButton);

//  buttonPanel.add(pasteButton);
    buttonPanel.add(removeButton);
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(applyButton);

    // buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

    // copyButton.setVisible(false);
    // pasteButton.setVisible(false);
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
   *    @param panelType
   *    @param index
   *    @return
   */
  protected JiveModulePanel createNewPanel(String panelType, Integer index) {
    JiveModulePanel newPanel = null;

    if (panelType.equals("Function")) newPanel = new FunctionModulePanel();
    else if (panelType.equals("Fourier")) newPanel = new FourierModulePanel();
    else if (panelType.equals("Display")) newPanel = new DisplayModulePanel();
    else if (panelType.equals("Patch")) newPanel = new PatchModulePanel();
    else if (panelType.equals("Screening")) newPanel = new ScreeningModulePanel();
    else if (panelType.equals("Printing")) newPanel = new PrintingModulePanel();
    else if (panelType.equals("Scanning")) newPanel = new ScanningModulePanel();

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
   *    @param index
   */
  protected void createPanel(JiveModulePanel panel, String title, Integer index) {
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

//  contentPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

    if (debugBorders) contentPanel.setBorder(new LineBorder(Color.red));

    // Button Container
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setOpaque(false);

//  buttonPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

    if (debugBorders) buttonPanel.setBorder(new LineBorder(Color.cyan));

    // Scroll Pane
    scrollPanel = new JPanel();
    scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
    scrollPanel.add(contentPanel);
    scrollPanel.add(Box.createVerticalGlue());
    scrollPanel.setBackground(listColor);

//  scrollPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

    if (debugBorders) scrollPanel.setBorder(new LineBorder(Color.magenta));

    scrollPane = new JScrollPane(scrollPanel);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setBorder(null);			// new LineBorder(SystemColor.controlShadow));
    scrollPane.setBackground(listColor);

//  scrollPane.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

    if (debugBorders) scrollPane.setBorder(new LineBorder(Color.yellow));

//  scrollPane.add(Box.createVerticalGlue());

    // Parent Container
    containerPanel = this;
    containerPanel.setLayout(new BorderLayout());

    // containerPanel.setBorder(new EmptyBorder(1,1,1,1));

    containerPanel.add(scrollPane, BorderLayout.CENTER);
    containerPanel.add(buttonPanel, BorderLayout.SOUTH);

    if (debugBorders) containerPanel.setBorder(new LineBorder(Color.black));

//  containerPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

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
            JiveAbstractPanel panel = (JiveAbstractPanel)SwingUtilities.getAncestorOfClass(JiveAbstractPanel.class, focusOwner);

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

              if (focusOwner instanceof JComponent) {			// (!(focusOwner instanceof JiveNumericField)) {
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

//  contentPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
//  contentPanel.setSize(new Dimension(500, contentPanel.getHeight()));    
//  buttonPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
//  buttonPanel.setSize(new Dimension(500, buttonPanel.getHeight()));
//  scrollPane.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
//  scrollPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
//  
    revalidate();

//  
//  containerPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
//  containerPanel.setSize(new Dimension(500, containerPanel.getHeight()));    

  }

  /**
   */
  protected void createPanels() {
    int rows    = 25;
    int columns = 1;

    for (int i = 1; i < rows + 1; i++) {

      // JLabel newLabel = new JLabel(i + "");
      JiveModulePanel newPanel;

      if ((i) % 4 == 0) newPanel = new ScanningModulePanel();
      else if ((i) % 3 == 0) newPanel = new PrintingModulePanel();
      else if ((i) % 2 == 0) newPanel = new ScreeningModulePanel();
      else newPanel = new PatchModulePanel();

      this.createPanel(newPanel, i + "", null);

    }

  }

  /**
   *    @param panel
   *    @param title
   */
  protected void createPermanentPanel(JiveModulePanel panel, String title) {
    panel.setPermanent(true);
    createPanel(panel, title, null);
  }

  /**
   */
  protected void editValues() {

    // String valueString = new TextTransfer().getClipboardContents().replaceAll("}, ", "},\n");

    String valueString = getValueString().trim().replaceAll("}, ", "},\n");

    valueString = valueString.substring(1, valueString.length() - 1);

//  if ((contents instanceof String) && (contents != null)) valueString = (String)contents;

    valueString = TextEditorDialog.showDialog(null, null, "Module Configuration", "Edit Configuration", valueString);

    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(valueString), new ClipboardOwner() {

      @Override
      public void lostOwnership(Clipboard clipboard, Transferable contents) {}
    });

    setValues(valueString);

  }

  /**
   */
  public void enableFullScreenForAncestor() {
    Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, this);

    // JiveModuleContainer.enableFullScreenMode(window);
    JiveModuleContainer.enableOSXFullscreen(window);

    com.apple.eawt.Application.getApplication().requestToggleFullScreen(window);
  }

  /**
   *    @param window
   */
  public static void enableFullScreenMode(Window window) {
    String className  = "com.apple.eawt.FullScreenUtilities";
    String methodName = "setWindowCanFullScreen";

    try {
      Class<?> clazz  = Class.forName(className);
      Method   method = clazz.getMethod(methodName, new Class<?>[] { Window.class, boolean.class });

      method.invoke(null, window, true);
    } catch (Throwable t) {
      System.err.println("Full screen mode is not supported");

      // t.printStackTrace();
    }
  }

  /**
   * @param window
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static void enableOSXFullscreen(Window window) {
    Preconditions.checkNotNull(window);

    try {
      Class  util     = Class.forName("com.apple.eawt.FullScreenUtilities");
      Class  params[] = new Class[] { Window.class, Boolean.TYPE };
      Method method   = util.getMethod("setWindowCanFullScreen", params);

      method.invoke(util, window, true);
    } catch (ClassNotFoundException e1) {}
    catch (Exception e) {
      logger.log(Level.WARNING, "OS X Fullscreen FAIL", e);
    }
  }

  /**
   */
  protected void initializePanel() {
    this.addAncestorListener(new AncestorListener() {

      @Override
      public void ancestorRemoved(AncestorEvent e) {}

      @Override
      public void ancestorMoved(AncestorEvent e) {}

      @Override
      public void ancestorAdded(AncestorEvent e) {
        ((JiveModuleContainer)e.getComponent()).enableFullScreenForAncestor();
      }
    });

    this.addComponentListener(new ComponentAdapter() {

      /*
       *  (non-Javadoc)
       *   @see java.awt.event.ComponentAdapter#componentShown(java.awt.event.ComponentEvent)
       */
      @Override
      public void componentShown(ComponentEvent e) {
        super.componentShown(e);
        revalidate();
      }

      /*
       *    (non-Javadoc)
       *   @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
       */
      @Override
      public void componentResized(ComponentEvent e) {
        JComponent component = (JComponent)e.getComponent();
      }
    });

  }

  /**
   * @return
   * @see java.util.AbstractSequentialList#iterator()
   */
  public Iterator<JiveModulePanel> iterator() {
    return modules.iterator();
  }

  /**
   *    @param steps
   */
  protected void moveActivePanel(int steps) {

    if (steps == 0) return;

    JiveModulePanel activePanel = (JiveModulePanel)JiveModulePanel.activePanel;

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

    try {
      focusOwner.requestFocus();
    } catch (Exception exception) {
      GrasppeKit.debugError("ContainerPanel>Move>Focus", exception, 2);
    }
  }

  /**
   */
  protected void pasteValues() {

//  Object contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
//  
//  if (contents instanceof TransferableProxy) contents = ((TransferableProxy) contents).getTransferData(arg0) 

    String valueString = new TextTransfer().getClipboardContents().replaceAll("}, ", "},\n");

//  if ((contents instanceof String) && (contents != null)) valueString = (String)contents;

    valueString = TextEditorDialog.showDialog(null, null, "Module Configuration", "Edit Configuration", valueString);

    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(valueString), new ClipboardOwner() {

      @Override
      public void lostOwnership(Clipboard clipboard, Transferable contents) {}
    });

    setValues(valueString);
  }

  /**
   *    @param evt
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {

    if (evt.getPropertyName().equals("Title")) {
      moduleHashCode = 0;
    }

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
  protected void removeActivePanel() {
    JiveModulePanel activePanel = (JiveModulePanel)JiveModulePanel.activePanel;

    if ((activePanel == null) || (modules == null)) return;

    if (activePanel.isPermanent()) return;

    if (!modules.contains(activePanel)) return;

    int currentIndex = modules.indexOf(activePanel);

    GrasppeKit.debugText("ContainerPanel>RemovePanel", activePanel.title + " will be removed", 1);

    modules.remove(currentIndex);

    updatePanels();

    currentIndex = Math.min(currentIndex, modules.size() - 1);

    if (currentIndex > -1) modules.get(currentIndex).requestFocus();

    firePropertyChange("Panel.Layout", null, null);

  }

  /**
   */
  protected void removeAllPanel() {

//  for (JiveModulePanel panel : modules.get)
//  JiveModulePanel activePanel = (JiveModulePanel)JiveModulePanel.activePanel;
//
//  if ((activePanel == null) || (modules == null)) return;
//
//  if (activePanel.isPermanent()) return;
//
//  if (!modules.contains(activePanel)) return;
//
//  int currentIndex = modules.indexOf(activePanel);
//
//  GrasppeKit.debugText("ContainerPanel>RemovePanel", activePanel.title + " will be removed", 1);

    modules.clear();		// (currentIndex);

    updatePanels();

    firePropertyChange("Panel.Layout", null, null);

  }

  /**
   */
  public void snatchFocus() {
    final JComponent component = this;

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        component.grabFocus();
        component.requestFocus();
        component.transferFocus();
      }
    });

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

    revalidate();

    contentPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
    buttonPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
    scrollPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
    scrollPane.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
    containerPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

    try {
//    	assert getParent() != null;
      getParent().validate();
      getParent().invalidate();
      getParent().validate();
    } catch (NullPointerException exception) {
        GrasppeKit.debugError("ContainerPanel>Revalidate>Parent", exception, 5);      
//    } catch (AssertionError error) {
//        GrasppeKit.debugError("ContainerPanel>Revalidate>Parent", error, 5);
    } catch (Exception exception) {
      GrasppeKit.debugError("ContainerPanel>Revalidate>Parent", exception, 2);
    }

//  revalidate();       

    // Toolkit.getDefaultToolkit().beep();

//  SpringUtilities.makeGrid(contentPanel, rows, columns, 3, 3, 3, 3);
  }

  /**
   */
  protected void updatePanels() {
    contentPanel.removeAll();

    contentPanel.validate();
    contentPanel.repaint();

    contentPanel.setFont(listFont);

//  contentPanel.setSize(new Dimension(400, contentPanel.getSize().height));

    Iterator<JiveModulePanel> panelIterator = this.iterator();

    while (panelIterator.hasNext()) {
      final JiveModulePanel panel = panelIterator.next();
      final JLabel          label = new JLabel(panel.getTitle());

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

//    updateGrid();

//    
      // panel.revalidate();

      panel.flowResize();

//    panel.setMaximumSize(new Dimension(600, 99999));
//    
//    panel.validate();
//    panel.setMinimumSize(panel.getPreferredSize());
    }

    // contentPanel.add(Box.createVerticalGlue());

//  setSize(new Dimension(500, getHeight()));

    // this.enableFullScreenForAncestor();

    contentPanel.revalidate();
    contentPanel.repaint();

    revalidate();
    updateGrid();

    contentPanel.validate();
    contentPanel.repaint();

//    

  }

  /**
   * @param arg0
   * @return
   * @see java.util.LinkedList#get(int)
   */
  public JiveModulePanel get(int arg0) {
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
  public JiveModulePanel getFirst() {
    return modules.getFirst();
  }

  /**
   * @return
   * @see java.util.LinkedList#getLast()
   */
  public JiveModulePanel getLast() {
    return modules.getLast();
  }

  /**
   *    @return
   */
  public HashMap<String, JiveModulePanel> getModules() {

    if (modules.hashCode() != moduleHashCode) {
      GrasppeKit.debugText("ContainerPanel>Map", "Generating new hash map for hash code: " + modules.hashCode(), 2);

      modulesHashMap = new HashMap<String, JiveModulePanel>();

      Iterator<JiveModulePanel> panelIterator = this.iterator();

      while (panelIterator.hasNext()) {
        JiveModulePanel module = panelIterator.next();
        String          name   = module.getTitle();

        modulesHashMap.put(name, module);
      }

      moduleHashCode = modules.hashCode();
    }

    return modulesHashMap;
  }

  /**
   *    @return
   */
  public String getValueString() {
    LinkedHashMap<String, HashMap> values      = getValues();
    String                         valueString = "{";
    int                            m           = 0;

    for (String moduleKey : values.keySet()) {
      if (m++ > 0) valueString = valueString + "\n";		// ", ";
      valueString = valueString + moduleKey + "={";

      HashMap<String, Object> moduleValues = values.get(moduleKey);

      int                     n            = 0;

      for (String fieldKey : moduleValues.keySet()) {
        Object fieldValue = moduleValues.get(fieldKey);

        if (n++ > 0) valueString = valueString + " | ";
        valueString = valueString + fieldKey + "=" + fieldValue.toString();
      }

      valueString = valueString + "}";
    }

//  for (Object moduleValues : values.values()) {
//      System.out.println((HashMap)moduleValues);
//      
//  }
    // String string = "";
    valueString = valueString + "}";

    return valueString;
  }

  /**
   *    @return
   */
  public LinkedHashMap<String, HashMap> getValues() {
    LinkedHashMap<String, HashMap> values        = new LinkedHashMap<String, HashMap>();

    Iterator<JiveModulePanel>      panelIterator = this.iterator();

    while (panelIterator.hasNext()) {
      JiveModulePanel module = panelIterator.next();
      String          name   = module.getTitle();

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
   *    @return
   */
  private static boolean isMacOSX() {
    return System.getProperty("os.name").indexOf("Mac OS X") >= 0;
  }

  /**
   * @param arg0
   * @param arg1
   * @return
   * @see java.util.LinkedList#set(int, java.lang.Object)
   */
  public JiveModulePanel set(int arg0, JiveModulePanel arg1) {
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
   *    @param allValues
   */
  public void setValues(HashMap<String, HashMap> allValues) {

    for (String moduleName : allValues.keySet()) {
      if (getModules().containsKey(moduleName)) {
        JiveModulePanel          module       = getModules().get(moduleName);
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

  /**
   *    @param valueString
   */
  public void setValues(String valueString) {
    String[]                    sourceStrings   = new String[8];

    LinkedList<JiveModulePanel> previousModules = (LinkedList<JiveModulePanel>)modules.clone();			// new LinkedList<JiveModulePanel>();

    modules.clear();

    int n = 0;

    sourceStrings[n++] = valueString.replaceAll("\\n", "");

//  sourceString[n++] = sourceString[n-2].replaceAll("\\{\\{", "\\{");
//  sourceString[n++] = sourceString[n-2].replaceAll("\\}\\}", "\\}");
//  sourceString[n++] = sourceString[n-2].replaceAll("\\=\\{", "\\|");
//  sourceString[n++] = sourceString[n-2].replaceAll("\\=", "\\|");
    // sourceStrings[n++] = sourceStrings[n-2].replaceAll("\\;\\}\\{", "###{");
    sourceStrings[n++] = sourceStrings[n - 2].replaceAll("\\},", "###");
    sourceStrings[n++] = sourceStrings[n - 2].replaceAll("\\}", "###");

    // sourceString[n++] = sourceString[n-2].replaceAll(",", "\\|");
    sourceStrings[n++] = sourceStrings[n - 2].replaceAll("\\{", "");

    // sourceStrings[n++] = sourceStrings[n-2].replaceAll("\\}", "");

    GrasppeKit.debugText("SetValues>SourceStrings", arrayString(sourceStrings), 2);

    String   sourceString   = sourceStrings[n - 1];

    String[] processStrings = sourceString.split("###");

    GrasppeKit.debugText("SetValues>ProcessStrings", arrayString(processStrings), 2);

    // LinkedHashMap<String, HashMap> values = new LinkedHashMap<String, HashMap>();

    for (String processString : processStrings) {
      try {

        // LinkedHashMap<String, HashMap> moduleValues = new LinkedHashMap<String, HashMap>();

        String   moduleName    = processString.split("=")[0];
        String   moduleType    = moduleName.split("-")[0];

        String[] moduleStrings = processString.substring(moduleName.length() + 1).split(" \\| ");

        GrasppeKit.debugText("SetValues>ModuleStrings", arrayString(moduleStrings), 2);

        GrasppeKit.debugText("SetValues>ModuleName", moduleName, 2);
        GrasppeKit.debugText("SetValues>ModuleType", moduleType, 2);

        String[] valuePairs = moduleStrings;		// Arrays.copyOfRange(moduleStrings, 1, moduleStrings.length);

//      Arrays.asList(moduleStrings).toString();

        GrasppeKit.debugText("SetValues>ValuePairs", arrayString(valuePairs), 2);

        JiveModulePanel newPanel = createNewPanel(moduleType, null);

        for (String valuePair : valuePairs) {
          String[] pairStrings = valuePair.split("\\=");		// "(?=[\\w-])\\="

          String   field       = pairStrings[0];
          String   value       = valuePair.substring(field.length() + 1);

          // Pattern p = Pattern.compile("[\\w-]*[(;\\s|}");
          // Matcher m = p.matcher("aaaaab");
          // System.out.println();
          // System.out.println(valuePair);
          // System.out.println(pairStrings);

          GrasppeKit.debugText("SetValues>PairStrings", field + "  =  " + value + ";", 2);

          try {
            Object currentValue = newPanel.getValue(field);

            if (currentValue instanceof Double) newPanel.setValue(field, new Double(value).doubleValue());
            else if (currentValue instanceof Integer) newPanel.setValue(field, new Double(value).intValue());
            else newPanel.setValue(field, value);
          } catch (Exception exception) {
            GrasppeKit.debugError("SetValues>Pairs", exception, 1);
          }
        }
      } catch (Exception exception) {
        GrasppeKit.debugError("SetValues>Modules", exception, 1);
      }
    }

    if (modules.size() == 0) {
      GrasppeKit.debugText("SetValues>ResetModules", modules.toString(), 2);
      modules = previousModules;
    }

    updatePanels();

    revalidate();

//
//  Iterator<JiveModulePanel> panelIterator = this.iterator();
//
//  while (panelIterator.hasNext()) {
//    JiveModulePanel module = panelIterator.next();
//    String              name   = module.getTitle();
//
//    values.put(name, module.getValues());
//  }
//
//  return values;

    System.out.println(sourceString);

//  ={    |
//  = |
//  },    ;
//  { 
//  } 
    // String[] processStrings = sourceString. // ("(?<=\\=\\{)[^\\}](?=\\})");
  }
}
