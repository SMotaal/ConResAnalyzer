/*
 * @(#)ConResAnalyzerView.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer.view;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.Action;
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
public class ConResAnalyzerView extends AbstractView {

    /** Field description */
    ConResAnalyzerMenu				menu;
    String							name        = "ConResAnalyzer";
    boolean							finalizable = true;
    int								activeCalls = 0;
    JFrame							mainFrame;
    HashMap<String, JMenu>			menuMap             = new HashMap<String, JMenu>();
    ArrayList<String>				menuMapKeys         = new ArrayList<String>();
    private String					menuToInsertBefore  = "view";
    private JMenuBar				mainMenu            = new JMenuBar();
    private ArrayList<Container>	containers          = new ArrayList<Container>();
    private Container				activeContainer     = null;
    private Container				backgroundContainer = null;
    private Container				defaultContainer    = null;

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
        containers.add(container);
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
    public void createMenuItem(JMenu menu, AbstractCommand command) {		// AbstractCommand[] commands) {
        JMenuItem	menuItem = new JMenuItem(command);

        int			mnemonic = command.getMnemonicKey();

        if (mnemonic > 0)
            menuItem.setAccelerator(KeyStroke.getKeyStroke(mnemonic,
                GrasppeKit.getControlModifierMask()));

        String	grouping = command.getMenuGrouping();

        if ((menu.getItemCount() > 0) &&!grouping.isEmpty()) {
            Action	previousAction = menu.getItem(menu.getItemCount() - 1).getAction();

            if (previousAction instanceof AbstractCommand) {
                String	previousGrouping = ((AbstractCommand)previousAction).getMenuGrouping();

                if (!previousGrouping.equals(grouping)) menu.addSeparator();
            }
        }

        menu.add(menuItem);
        GrasppeKit.debugText("Command Menu Created", GrasppeKit.lastSplit(command.toString()));
    }

    /**
     * Completes graphical user interface operations before closing.
     * @return
     */
    public boolean finalizeView() {
        if (!canFinalize()) return false;

        return true;
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
        JMenu	menu = new JMenu(name);

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
     */
    public void prepareMenu() {

        LinkedHashMap<String, AbstractCommand>	commands        = controller.getCommands();
        Collection<AbstractCommand>				commandSet      = commands.values();
        Iterator<AbstractCommand>				commandIterator = commandSet.iterator();

        // TODO: Define essential menu items to enforce predefined menu sequence
        ArrayList<String>	menus = new ArrayList<String>();

        newMenu("file");
        newMenu("edit");
        newMenu("view");
        newMenu("window");
        newMenu("help");

        // TODO: Find all the command types (for menu classifications)
        // TODO: Create all menu items and insert
        while (commandIterator.hasNext()) {
            AbstractCommand	command     = commandIterator.next();

            String			commandType = "tools";

            if ((command != null) && (command.getMenuKey() != null))
                commandType = command.getMenuKey().toLowerCase().trim();

            JMenu	menu = getMenu(commandType);

            createMenuItem(menu, command);
        }

        Iterator<String>	menuKeyIterator = menuMapKeys.iterator();

        while (menuKeyIterator.hasNext()) {
            String	menuKey           = menuKeyIterator.next();
            JMenu	menu              = menuMap.get(menuKey);
            int		menuIndex         = getMenuIndex(menuKey);
            int		insertBeforeIndex = getMenuIndex(menuToInsertBefore);

            if (menuIndex == -1) if (insertBeforeIndex > -1) mainMenu.add(menu, insertBeforeIndex);
            else mainMenu.add(menu);

            menu.setVisible(menu.getItemCount() > 0);
        }

        mainFrame.setJMenuBar(mainMenu);
        update();
    }

    /**
     * Builds the graphical user interface window and elements.
     */
    public void prepareView() {

        if (mainFrame != null) return;

        DisplayMode	displayMode =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]
                .getDisplayMode();
        int	displayWidth  = displayMode.getWidth();
        int	displayHeight = displayMode.getHeight();
        int frameWidth = displayWidth - 150;
        int frameHeight = displayHeight - 150;

        mainFrame = new JFrame("ConResAnalyzer");
        
        mainFrame.setUndecorated(true);

        mainFrame.setMinimumSize(new Dimension(frameWidth, frameHeight));
        mainFrame.setLocation((displayWidth - frameWidth) / 2, ((displayHeight - frameHeight) / 2) - 50);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setVisible(true);
        
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        prepareDefaultContainer();

        prepareMenu();
    }
    
    public void prepareDefaultContainer() {
    	if (defaultContainer!=null) return;
    	defaultContainer = new JPanel(new BorderLayout());
    	
    	defaultContainer.setBackground(Color.DARK_GRAY);
    	
    	setContainer(defaultContainer);
    }

    /**
     * 	@return
     */
    public Container getContentPane() {
        if (mainFrame != null) return mainFrame.getContentPane();
        else return null;
    }

    /**
     *  @return
     */
    protected ConResAnalyzer getController() {
        return (ConResAnalyzer)controller;
    }

    /**
     *  @return
     */
    public JFrame getFrame() {
        if (mainFrame == null) prepareView();

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
    protected ConResAnalyzerModel getModel() {
        return (ConResAnalyzerModel)super.getControllerModel();
    }
    
    public void removeContainer(Container container) {
    	if (container == null) return;
    	
    	if (!containers.contains(container)) return;
    	
    	if (activeContainer!=null && activeContainer==container)
    		setContainer(defaultContainer);
    		
    }

    /**
     *  @param container
     */
    public void setContainer(Container container) {
        if (container == null) return;

        if ((activeContainer != null) && (activeContainer == container)) return;

        if (!containers.contains(container)) containers.add(container);

        if ((activeContainer != null) && (getContentPane() != null)
                && (activeContainer.getParent() == getContentPane())) {
        	activeContainer.setVisible(false);
            getContentPane().remove(activeContainer);
            backgroundContainer = activeContainer;
        }

        prepareView();

        if (activeContainer != container) {
            activeContainer = container;
            getContentPane().add(container, BorderLayout.CENTER);
            container.setVisible(true);
            container.validate();
            mainFrame.validate();
        }

        if ((activeContainer != null) && (backgroundContainer != null)
                && (activeContainer == backgroundContainer))
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
}
