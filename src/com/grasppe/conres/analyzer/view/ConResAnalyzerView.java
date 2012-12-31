/*
 * @(#)ConResAnalyzerView.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer.view;

import apple.dts.samplecode.osxadapter.OSXAdapter;

import com.apple.eawt.Application;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.conres.framework.cases.operations.OpenCase;
import com.grasppe.conres.framework.cases.view.CaseView;
import com.grasppe.conres.io.model.CaseFolder;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractCommand.Types;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.components.ObservableObject;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 *     Class description
 *     @version        $Revision: 0.1, 11/11/08
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ConResAnalyzerView extends AbstractView implements Observer {

  /** Field description */

//ConResAnalyzerMenu                menu;
  String												name        = "ConResAnalyzer";
  boolean												finalizable = true;
  int														activeCalls = 0;
  JFrame												mainFrame;
  HashMap<String, JMenu>				menuMap             = new HashMap<String, JMenu>();
  ArrayList<String>							menuMapKeys         = new ArrayList<String>();
  private String								menuToInsertBefore  = "view";
  private JMenuBar							mainMenu            = new JMenuBar();
  private ArrayList<Container>	containers          = new ArrayList<Container>();
  private Container							activeContainer     = null;
  private Container							backgroundContainer = null;
  private Container							defaultContainer    = null;
  ArrayList<JMenuItem>					menuItems           = new ArrayList<JMenuItem>();

  /**
   * Constructs a new ConResAnalyzerView with a predefined controller.
   * @param controller
   */
  public ConResAnalyzerView(ConResAnalyzer controller) {
    super(controller);
  }
  /**
   *  @param container
   */
  public void addContainer(Container container) {
    if (!containers.contains(container)) containers.add(container);

    if (container instanceof JComponent) {
      setupDropTarget((JComponent)container);
    }
  }

  /**
   * @return
   */
  public boolean canFinalize() {
    finalizable = (activeCalls == 0);
    GrasppeKit.debugText("Finalize / Active Calls", activeCalls + " remaining.");

    return finalizable;
  }

  /**
   *  @param menu
   *  @param command
   */
  public void createMenuItem(JMenu menu, AbstractCommand command) {			// AbstractCommand[] commands) {

    if (command.isIgnoreMenu()) return;

    JMenuItem	menuItem = new JMenuItem(command);

    int				mnemonic = command.getMnemonicKey();

    if (mnemonic > 0) menuItem.setAccelerator(KeyStroke.getKeyStroke(mnemonic, GrasppeKit.getControlModifierMask()));

    String	grouping = command.getMenuGrouping();
    String	name     = command.getClass().getSimpleName(); //.getDisplayText();

    command.attachObserver(this);
    this.menuItems.add(menuItem);

    if (GrasppeKit.OperatingSystem.isMac()) {
      try {
        if (name.equals("Quit")) {
          OSXAdapter.setQuitHandler(command, command.getClass().getDeclaredMethod("perfomCommand", (Class[])null));
          //menuItem=null;
        } else if (name.equals("ShowPreferences")) {
          OSXAdapter.setPreferencesHandler(command, command.getClass().getDeclaredMethod("perfomCommand", (Class[])null));
          //menuItem=null;
        } else if (name.equals("About")) {
          OSXAdapter.setAboutHandler(command, command.getClass().getDeclaredMethod("perfomCommand", (Class[])null));
          //menuItem=null;
        } else {}
      } catch (SecurityException exception) {

        // TODO Auto-generated catch block
        exception.printStackTrace();
      } catch (NoSuchMethodException exception) {

        // TODO Auto-generated catch block
        exception.printStackTrace();
      }
    }
    
    if (menuItem!=null) {
	    if ((menu.getItemCount() > 0) &&!grouping.isEmpty()) {
	      Action	previousAction = menu.getItem(menu.getItemCount() - 1).getAction();
	
	      if (previousAction instanceof AbstractCommand) {
	        String	previousGrouping = ((AbstractCommand)previousAction).getMenuGrouping();
	
	        if (!previousGrouping.equals(grouping)) menu.addSeparator();
	      }
	    }
	
	    menu.add(menuItem);
    }

    GrasppeKit.debugText("Command Menu Created", GrasppeKit.lastSplit(command.toString()));
  }

  /**
   * Builds the graphical user interface window and elements.
   */
  public void createView() {

    if (mainFrame != null) return;

    mainFrame = new JFrame("Tags");

    // mainFrame.setUndecorated(true);

    // mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    updateSize();
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    mainFrame.setVisible(true);

    CaseView	caseView = new CaseView(getController().getCaseManager());		// .getModel());

    mainFrame.add(caseView, BorderLayout.NORTH);

    setContainer(getDefaultContainer());

    prepareMenu();
  }
  
  public void detatch() throws Throwable {
	  if (mainFrame != null)
		  mainFrame.dispose();
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
   *  @param event
   */
  public void handleDropEvent(DropTargetDropEvent event) {
    int	dbg = 2;

    GrasppeKit.debugText("Handling Drop Event", event.toString(), dbg);

    // Ref: http://stackoverflow.com/questions/811248/how-can-i-use-drag-and-drop-in-swing-to-get-file-path
//  List<File> dropppedFiles = (List<File>)transferable.getTransferData(DataFlavor.javaFileListFlavor);

    // Ref : http://stackoverflow.com/questions/1697936/java-drag-and-drop-on-mac-os-x

    try {

      // Get the object to be transferred
      Transferable	tr      = event.getTransferable();
      DataFlavor[]	flavors = tr.getTransferDataFlavors();

      // If flavors is empty get flavor list from DropTarget
      flavors = (flavors.length == 0) ? event.getCurrentDataFlavors()
                                      : flavors;

      // Select best data flavor
      DataFlavor	flavor = DataFlavor.selectBestTextFlavor(flavors);

      // Flavor will be null on Windows
      // In which case use the 1st available flavor
      flavor = (flavor == null) ? flavors[0]
                                : flavor;

      // Flavors to check
      DataFlavor	Linux    = new DataFlavor("text/uri-list;class=java.io.Reader");
      DataFlavor	Windows  = DataFlavor.javaFileListFlavor;

      boolean			canOpen  = false;
      String			fileName = "";

      // On Linux (and OS X) file DnD is a reader
      if (flavor.equals(Linux)) {
        event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

        BufferedReader	read = new BufferedReader(flavor.getReaderForText(tr));

        // Remove 'file://' from file name
        fileName = read.readLine().substring(7).replace("%20", " ");

        // Remove 'localhost' from OS X file names
        if (fileName.substring(0, 9).equals("localhost")) {
          fileName = fileName.substring(9);
        }

        read.close();

        event.dropComplete(true);
        System.out.println("File Dragged:" + fileName);

//      mainWindow.openFile(fileName);
        canOpen = true;
      }

      // On Windows file DnD is a file list
      else if (flavor.equals(Windows)) {
        event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

        @SuppressWarnings("unchecked") List<File>	list = (List<File>)tr.getTransferData(flavor);

        event.dropComplete(true);

        if (list.size() == 1) {
          System.out.println("File Dragged: " + list.get(0));

          fileName = list.get(0).toString();

          canOpen  = true;
        }
      } else {
        System.err.println("DnD Error");
        event.rejectDrop();
      }

      if (canOpen && (fileName != null) &&!fileName.isEmpty()) {

//      mainFrame.setVisible(true);
//      mainFrame.toFront();
        Application.getApplication().requestForeground(true);
        openCaseFolder(fileName);
      }

    }

    // TODO: OS X Throws ArrayIndexOutOfBoundsException on first DnD
    catch (ArrayIndexOutOfBoundsException e) {
      System.err.println("DnD not initalized properly, please try again.");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } catch (UnsupportedFlavorException e) {
      System.err.println(e.getMessage());
    } catch (ClassNotFoundException e) {
      System.err.println(e.getMessage());
    }

//  event.acceptDrop(dropAction)
  }

  /**
   *  @param key
   */
  private void newMenu(String key) {
    newMenu(key, null);
  }

  /**
   *  @param index
   *  @param key
   */
  private void newMenu(int index, String key) {
    if ((key == null) || key.trim().isEmpty()) return;
    key = key.toLowerCase().trim();

    String	name = GrasppeKit.humanCase(key);
    JMenu		menu = new JMenu(name);

    menuMap.put(key, menu);

    if (index > -1) menuMapKeys.add(index, key);
    else menuMapKeys.add(key);
  }

  /**
   *  @param key
   *  @param insertBeforeKey
   */
  private void newMenu(String key, String insertBeforeKey) {
    if ((key == null) || key.trim().isEmpty()) return;
    key = key.toLowerCase().trim();
    if (menuMap.containsKey(key)) return;

    int	index = -1;

    if (insertBeforeKey != null) {
      insertBeforeKey = insertBeforeKey.toLowerCase().trim();

      if (!menuMap.containsKey(insertBeforeKey)) newMenu(insertBeforeKey, null);

      index = menuMapKeys.indexOf(insertBeforeKey);
    } else {
      if (!menuMapKeys.contains(menuToInsertBefore)) newMenu(-1, menuToInsertBefore);
      if (menuMapKeys.contains(menuToInsertBefore) &&!key.equals(menuToInsertBefore))
        index = menuMapKeys.indexOf(menuToInsertBefore);
    }

    newMenu(index, key);
  }

  /**
   *    @param casePath
   */
  public void openCaseFolder(String casePath) {
    OpenCase		openCaseCommand = new OpenCase(getController().getCaseManager(), getController().getCaseManager());
    CaseFolder	caseFolder      = new CaseFolder(casePath);

    try {
      if (openCaseCommand.verifyCaseFolder(caseFolder) && openCaseCommand.confirmCaseClose())
        openCaseCommand.openCase(caseFolder);
    } catch (Exception exception) {
      GrasppeKit.debugError("Opening Dropped Case", exception, 2);
    }
  }

  /**
   */
  public void prepareMenu() {

    LinkedHashMap<String, AbstractCommand>	commands        = controller.getCommands();
    Collection<AbstractCommand>							commandSet      = commands.values();
    Iterator<AbstractCommand>								commandIterator = commandSet.iterator();

    // Define essential menu items to enforce predefined menu sequence
    ArrayList<String>	menus = new ArrayList<String>();

    newMenu(Types.FILE);
    newMenu(Types.CASE);
    newMenu(Types.EDIT);
    newMenu(Types.VIEW);
    newMenu(Types.WINDOW);
    newMenu(Types.HELP);

    // Find all the command types (for menu classifications)
    // Create all menu items and insert
    while (commandIterator.hasNext()) {
      AbstractCommand	command     = commandIterator.next();

      String					commandType = "tools";

      if ((command != null) && (command.getMenuKey() != null)) commandType = command.getMenuKey().toLowerCase().trim();

      JMenu	menu = getMenu(commandType);

      createMenuItem(menu, command);
    }

    Iterator<String>	menuKeyIterator = menuMapKeys.iterator();

    while (menuKeyIterator.hasNext()) {
      String	menuKey           = menuKeyIterator.next();
      JMenu		menu              = menuMap.get(menuKey);
      int			menuIndex         = getMenuIndex(menuKey);
      int			insertBeforeIndex = getMenuIndex(menuToInsertBefore);

      if (menuIndex == -1) if (insertBeforeIndex > -1) mainMenu.add(menu, insertBeforeIndex);
      else mainMenu.add(menu);

      menu.setVisible(menu.getItemCount() > 0);
    }

    mainFrame.setJMenuBar(mainMenu);
    update();
  }

  /**
   *  @param container
   */
  public void removeContainer(Container container) {
    if (container == null) return;

    if (!containers.contains(container)) return;

    if ((activeContainer != null) && (activeContainer == container)) setContainer(getDefaultContainer());

  }

  /**
   *  @param component
   */
  public void setupDropTarget(JComponent component) {

    // Ref: http://massapi.com/class/java/awt/dnd/DropTargetAdapter.java.html

    DropTargetAdapter	dropTargetAdapter = new DropTargetAdapter() {

      public void drop(DropTargetDropEvent event) {
        handleDropEvent(event);
      }
    };
    DropTarget	newDropTarget = new DropTarget(component, dropTargetAdapter);
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
    
//    if ((menuItems == null) || menuItems.isEmpty()) return;
    if ((menuItems == null) || menuItems.isEmpty()) {}
    else {
	    for (JMenuItem menuItem : menuItems) {
	      Action	action = menuItem.getAction();
	
	      if ((action != null) && (action instanceof AbstractCommand)) {
	        menuItem.setEnabled(((AbstractCommand)action).canExecute());
	      }
	    }
	    if ((activeContainer == null) ||!activeContainer.isValid() ||!activeContainer.isVisible())
	    	setContainer(null);
    }
    
    
    super.update();
  }

  /**
   */
  public void updateSize() {
    if (mainFrame == null) return;
    mainFrame.validate();
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

//
//  DisplayMode   displayMode =
//        GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]
//                .getDisplayMode();
//  int   displayWidth  = displayMode.getWidth();
//  int   displayHeight = displayMode.getHeight();
//  int frameWidth = displayWidth - 150;
//  int frameHeight = displayHeight - 150;
//  mainFrame.setMinimumSize(new Dimension(frameWidth, frameHeight));
//  mainFrame.setLocation((displayWidth - frameWidth) / 2, ((displayHeight - frameHeight) / 2) - 50);
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
  public ConResAnalyzer getController() {
    return (ConResAnalyzer)controller;
  }

  /**
   *  @return
   */
  public Container getDefaultContainer() {
    if (defaultContainer != null) return defaultContainer;

    defaultContainer = new JPanel(new BorderLayout());
    defaultContainer.setBackground(Color.DARK_GRAY);

//  setContainer(defaultContainer);
    return (defaultContainer);

  }

  /**
   *  @return
   */
  public JFrame getFrame() {
    if (mainFrame == null) createView();

    return mainFrame;
  }

  /**
   *  @param key
   *  @return
   */
  public JMenu getMenu(String key) {
    if ((key == null) || key.trim().isEmpty()) return null;
    if (!menuMap.containsKey(key.trim().toLowerCase())) newMenu(key);

    return menuMap.get(key);
  }

  /**
   *  @param key
   *  @return
   */
  private int getMenuIndex(String key) {
    if (key != null) key.toLowerCase().trim();
    if ((key == null) || key.isEmpty()) return -1;
    if (!menuMap.containsKey(key)) return -1;
    for (int i = 0; i < mainMenu.getMenuCount(); i++)
      if (mainMenu.getMenu(i) == menuMap.get(key)) return i;

    return -1;
  }

  /**
   *  @return
   */
  @Override
  public ConResAnalyzerModel getModel() {
    return (ConResAnalyzerModel)super.getControllerModel();
  }

  /**
   *  @param container
   */
  public void setContainer(Container container) {
    if (container == null) {
      setContainer(getDefaultContainer());

      return;
    }

    if ((activeContainer != null) && (activeContainer == container)) return;

    addContainer(container);		// if (!containers.contains(container)) containers.add(container);

    if ((activeContainer != null) && (getContentPane() != null) && (activeContainer.getParent() == getContentPane())) {
      activeContainer.setVisible(false);
      getContentPane().remove(activeContainer);
      backgroundContainer = activeContainer;
    }

    createView();

    if (activeContainer != container) {
      activeContainer = container;
      getContentPane().add(container, BorderLayout.CENTER);

//    KeyListener   keyListener = new KeyAdapter() {
//
//        public void keyPressed(KeyEvent ke) {
//            if (ke.isConsumed()) return;
//            if (nudgeROI(ke))ke.consume(); //.getKeyCode(), ke.getModifiers())) 
//        }
//    };
//    viewContainer.addKeyListener(keyListener);
      container.setFocusable(true);
      setComponentFocus(container);
      container.setVisible(true);
      container.requestFocus();
      container.validate();
      mainFrame.validate();
    }

    if ((activeContainer != null) && (backgroundContainer != null) && (activeContainer == backgroundContainer))
      backgroundContainer = null;

  }

  /**
   * Class description
   *  @version        $Revision: 1.0, 11/12/11
   *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
   */
  private class RichMenuItem extends JMenuItem {

    /*
     *  (non-Javadoc)
     * @see javax.swing.AbstractButton#setText(java.lang.String)
     */

    /**
     *  @param text
     */
    @Override
    public void setText(String text) {
      super.setText(text);
    }
  }

@Override
public void detatch(Observable oberservableObject) {
	// TODO Auto-generated method stub
	
}
}
