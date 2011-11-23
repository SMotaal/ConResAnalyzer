/*
 * @(#)ConResPluginMenu.java   11/11/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.analyzer.view;

import ij.plugin.frame.PlugInFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.framework.GrasppeEventDispatcher;
import com.grasppe.lure.framework.GrasppeEventHandler;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
 * @author daflair
 *
 */
public class ConResAnalyzerMenu extends PlugInFrame implements ActionListener, GrasppeEventHandler {

    // protected GrasppeKit  grasppeKit = GrasppeKit.getInstance();

    /** Field description */
    protected Hashtable<Actions, PluginMenuAction>	actionMap;

    /** Field description */
    protected Hashtable<Actions, PluginMenuItem>	buttonMap;

    /** Field description */
    protected JToolBar								pluginBar = new JToolBar();
    protected String								name      = this.getClass().getSimpleName();	// "ConResAnalyzerMenu";
    protected Hashtable<Integer, PluginMenuItem>	keyMap    = new Hashtable<Integer, PluginMenuItem>();

    /**
     * Constructs ...
     */
    public ConResAnalyzerMenu() {
        super("ConResAnalyzerMenu");
        setAlwaysOnTop(true);
        setUndecorated(true);
        setSize(450, 350);

        WindowListener	wndCloser = new WindowAdapter() {

            @Override
			public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };

        addWindowListener(wndCloser);

        GrasppeEventDispatcher	eventDispatcher = GrasppeEventDispatcher.getInstance();

        eventDispatcher.attachEventHandler(this);
        
        super.add(pluginBar, BorderLayout.NORTH);
        
        pack();
        setVisible(true);
    }

    /**
     * Enum description
     *
     */
    enum Actions {
        NEW_CASE, OPEN_CASE, CLOSE_CASE, BLOCK_MAPPER, BLOCK_ANALYZER, CASE_PLOTTER, NEXT_BLOCK,
        LAST_BLOCK, QUIT;

        /**
         * Method description
         *
         * @param e an ActionEvent (i.e. ones passed to Action Listeners.actionPerformed)
         *
         * @return  a boolean indicating the simpleForm string match of the getActionCommand and this Action value
         */
        public boolean equals(ActionEvent e) {
            String	cmd = simpleForm(e.getActionCommand());

            return toString().equals(cmd);
        }

        /**
         * Standardizes string formats to help match value.toString() with human-readable action names
         *
         * @param name  the name of an enum value
         *
         * @return  the standardized format of the value name specified
         */
        public static String simpleForm(String name) {

            // Reference: http://www.mkyong.com/java/how-to-remove-whitespace-between-string-java/

            /* converts NEWCASE to newcase and New Case to newcase */
            return name.toLowerCase().replaceAll("\\s+", "");

            // TODO Change to convert from ENUM_VALUE to "Enum Value" and "Enum Value..." to "Enum Value"

            /* converts NEW_CASE to New Case and New Case... to New Case */

            // return name.toLowerCase().replaceAll("\\s+", "");
        }

        /**
         * Method description
         *
         * @return  the standardized format of the enum value
         */
        @Override
		public String toString() {
            return simpleForm(name().toString());
        }
    }

    /**
     * Method description
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {

//      IJ.showMessage("ConResAnalyzerPlugInA0Menu Action Performed: "
//                     + Actions.simpleForm(e.getActionCommand().toString()) + " - ");
//
//      // showCallStack();
//      // TODO Attach each Actions item using item-level equals() -- simpleForm string matching
//      if (Actions.NEW_CASE.equals(e)) return;       // TODO Create new case with scanned images from input folder to be specified by user
//      else if (Actions.OPEN_CASE.equals(e)) return;     // TODO Open a case at case folder to be specified by user
//      else if (Actions.CLOSE_CASE.equals(e)) return;        // TODO Close the open case if one is open
//      else if (Actions.BLOCK_MAPPER.equals(e)) return;  // TODO Open block mapper view for the current block to allow the user to define the block corners
//      else if (Actions.BLOCK_ANALYZER.equals(e)) return;        // TODO Open block analyzer view for the current block to allow the user to conduct patch-level visual analysis
//      else if (Actions.LAST_BLOCK.equals(e)) return;        // TODO Set current block to last block in queue backwards
//      else if (Actions.NEXT_BLOCK.equals(e)) return;        // TODO Set current block to next block in queue forward
//      else if (Actions.CASE_PLOTTER.equals(e)) return;  // TODO Open case plotter view for the current case to allow the user to view and tweak the gamut plot
//      else if (Actions.QUIT.equals(e)) return;
    }

    /**
     * Method description
     *
     * @param command
     */
    public void createButton(AbstractCommand command) {

        // TODO Create actions and menu button for this action value and add it to the actionMap HashTable

        /*
         * PluginMenuAction    thisAction = new PluginMenuAction(this, command.toString());
         *
         * actionMap.put(action, thisAction);
         * IJ.showMessage("ConResAnalyzerPlugInA0Menu Action Created: " + thisAction.toString());
         */
        PluginMenuItem	thisButton   = new PluginMenuItem(command);
        int				thisMnemonic = thisButton.getMnemonic();

        GrasppeKit.debugText("Menu Button Created",
                             GrasppeKit.lastSplit(thisButton.getActionCommand()) + " ("
                             + (char)thisMnemonic + " : int " + thisMnemonic + ")", 3);
        if (thisMnemonic > 0) keyMap.put(thisMnemonic, thisButton);

        // buttonMap.put((command, thisButton);
        // GrasppeKit.debugText("Menu Button Created", grasppeKit.lastSplit(thisButton.toString()));
//      IJ.showMessage(this.getClass().getSimpleName() + " Menu Button Created: "
//                     + thisButton.toString());
        pluginBar.add(thisButton);
        pack();
    }

    /**
     * Method description
     */
    public void createButtonsFromActions( /* Enum<T> actions */) {
        pluginBar = new JToolBar();
        actionMap = new Hashtable<Actions, PluginMenuAction>();
        buttonMap = new Hashtable<Actions, PluginMenuItem>();

        for (Actions action : Actions.values()) {

            // TODO Create actions and menu button for this action value and add it to the actionMap HashTable
            PluginMenuAction	thisAction = new PluginMenuAction(this, action.toString());

            actionMap.put(action, thisAction);
            GrasppeKit.debugText("Menu Action Created",
                                 GrasppeKit.lastSplit(thisAction.toString()));

//          IJ.showMessage(this.getClass().getSimpleName() + " Menu Action Created: "
//                         + thisAction.toString());
            PluginMenuItem	thisButton = new PluginMenuItem(thisAction);

            buttonMap.put(action, thisButton);
            GrasppeKit.debugText("Menu Button Created",
                                 GrasppeKit.lastSplit(thisButton.toString()));

//          IJ.showMessage(this.getClass().getSimpleName() + " Menu Button Created: "
//                         + thisButton.toString());
            pluginBar.add(thisButton);
        }

        super.add(pluginBar, BorderLayout.NORTH);

        // getContentPane().add(pluginBar, BorderLayout.NORTH);
    }

    /**
     * Method description
     *
     * @param e
     *
     * @return
     */
    public boolean dispatchedKeyEvent(KeyEvent e) {
        int	keyCode = e.getKeyCode();

        //if (!isActive()) return false;

        String	keyString = GrasppeKit.keyEventString(e);

        if (!keyMap.containsKey(keyCode)) return false;

        PluginMenuItem	thisButton = keyMap.get(keyCode);

        thisButton.keyPressed(e);

        return true;
    }

    /**
     * Method description
     */
    public void update() {

        // TODO Auto-generated method stub

    }

    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/09
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    private class MyDispatcher implements KeyEventDispatcher {

        /** Field description */
        public int	lastKey;

        /** Field description */
        public int	lastModifier;

        /**
         * Method description
         *
         * @param e
         *
         * @return
         */
        public boolean dispatchKeyEvent(KeyEvent e) {

            // if ((e.getKeyCode() < 65) || (e.getKeyCode() > 90)) return false;     // () >= 65 &&) return false;
            if (e.getID() == KeyEvent.KEY_PRESSED) {			// System.out.println("tester");
                return dispatchedKeyEvent(e);
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {	// System.out.println("2test2");
            } else if (e.getID() == KeyEvent.KEY_TYPED) {

                // System.out.println("3test3");
            }

            return false;
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class PluginMenuAction extends AbstractAction {

        private ActionListener	actionListener;

        /**
         * Constructs ...
         *
         * @param listener
         * @param text
         */
        public PluginMenuAction(ActionListener listener, String text) {

            // http://download.oracle.com/javase/tutorial/uiswing/misc/action.html
            super(text);	// , icon);
            actionListener = listener;

//          putValue(SHORT_DESCRIPTION, desc);
//          putValue(MNEMONIC_KEY, mnemonic);
        }

        /**
         * Method description
         *
         * @param e
         */
		public void actionPerformed(ActionEvent e) {
            actionListener.actionPerformed(e);
        }
    }


    /**
     * PluginMenuItem is a modified implementation of SmallButton (http://www.java2s.com/Code/Java/Swing-JFC/Simpletoolbar.htm)
     * designed to simplify the process of creating plugin menu buttons.
     *
     *
     * @version        Enter version here..., 11/11/06
     * @author         Enter your name here...
     */
    public class PluginMenuItem extends JButton implements MouseListener, Observer {

        /** Field description */
        protected Border	m_raised;

        /** Field description */
        protected Border	m_lowered;

        /** Field description */
        protected Border	m_inactive;
        protected Font		m_font        = this.getFont().deriveFont((float)14.0);
        protected Dimension	m_minimumSize = new Dimension(100, 20);

        /**
         * Constructs ...
         *
         * @param action
         */
        public PluginMenuItem(AbstractAction action) {
            super(action);		// addActionListener(action);

            // super((Icon)act.getValue(Action.SMALL_ICON));
            m_raised   = new BevelBorder(BevelBorder.RAISED);
            m_lowered  = new BevelBorder(BevelBorder.LOWERED);
            m_inactive = new EmptyBorder(2, 2, 2, 2);
            super.setFont(m_font);
            setBorder(m_inactive);
            setMargin(new Insets(1, 1, 1, 1));
            super.setMinimumSize(m_minimumSize);
            super.setMaximumSize(m_minimumSize);
            super.setPreferredSize(m_minimumSize);
            addMouseListener(this);
            setRequestFocusEnabled(false);

            String	actionName,	actionDescription;
            int		actionMnemonic;

            // Tooltip Text
            try {
                actionDescription = (String)action.getValue(Action.SHORT_DESCRIPTION);
            } catch (Exception exception) {
                actionDescription = action.toString();
            }

            setToolTipText(actionDescription);

            // Button Text
            try {
                actionName = ((AbstractCommand)action).getName();
                GrasppeKit.debugText("Command Menu Item",
                                     "getName ==> " + actionName + " ==> "
                                     + GrasppeKit.humanCase(actionName), 4);
            } catch (Exception exception) {
                actionName = action.toString();
                GrasppeKit.debugText("Command Menu Item",
                                     "toString ==> " + actionName + " ==> "
                                     + GrasppeKit.humanCase(actionName), 4);
            }

            setText(GrasppeKit.humanCase(actionName, true));

//          // Mnemonic
            try {
                actionMnemonic = ((AbstractCommand)action).getMnemonicKey();
                GrasppeKit.debugText("Command Menu Item",
                                     "getMnemonicKey ==> " + (char)actionMnemonic + " (int "
                                     + actionMnemonic + ")", 4);
            } catch (Exception exception) {
                actionMnemonic = actionName.charAt(0);
                GrasppeKit.debugText("Command Menu Item",
                                     "actionName ==> " + (char)actionMnemonic + " (int "
                                     + actionMnemonic + ")", 4);
            }

            setMnemonic(actionMnemonic);

            try {
                ((AbstractCommand)action).attachObserver(this);
            } catch (Exception exception) {
                GrasppeKit.debugText("Command Menu Item",
                                     "Cannot attach menu item to observe command.", 4);
            }

            update();
        }

        /**
         * Constructs ...
         *
         * @param action
         * @param tooltip
         */
        public PluginMenuItem(PluginMenuAction action, String tooltip) {
            this(action);
            setToolTipText(tooltip);
        }

        /**
         * Method description
         *
         * @param e
         */
        public void keyPressed(KeyEvent e) {
            try {
                ((AbstractCommand)getAction()).setKeyEvent(e);		// execute(e);
                this.doClick();
                ((AbstractCommand)getAction()).setKeyEvent();
                GrasppeKit.debugText("Command Menu KeyEvent Handled", GrasppeKit.keyEventString(e),
                                     4);
            } catch (Exception exception) {
                GrasppeKit.debugText("Command Menu KeyEvent Exception",
                                     GrasppeKit.keyEventString(e) + " ==> Exception thrown\n\n"
                                     + exception.getMessage(), 2);
            }
        }

        /**
         * Method description
         *
         * @param e
         */
		public void mouseClicked(MouseEvent e) {}

        /**
         * Method description
         *
         * @param e
         */
		public void mouseEntered(MouseEvent e) {
            if (isEnabled()) setBorder(m_raised);
        }

        /**
         * Method description
         *
         * @param e
         */
		public void mouseExited(MouseEvent e) {
            setBorder(m_inactive);
        }

        /**
         * Method description
         *
         * @param e
         */
		public void mousePressed(MouseEvent e) {
            if (isEnabled()) setBorder(m_lowered);
        }

        /**
         * Method description
         *
         * @param e
         */
		public void mouseReleased(MouseEvent e) {
            setBorder(m_inactive);
        }

        /*
         *  (non-Javadoc)
         * @see com.grasppe.GrasppeKit.Observer#update()
         */

        /**
         * Method description
         */
        public void update() {

            // super.getAction().
            try {
                if (((AbstractCommand)getAction()).canExecute()) {
                    GrasppeKit.debugText("Command Menu Update",
                                         ((AbstractCommand)getAction()).getName()
                                         + " menu item can execute.");
                    super.setEnabled(true);
                } else {
                    GrasppeKit.debugText("Command Menu Update",
                                         ((AbstractCommand)getAction()).getName()
                                         + " menu item cannot execute.");
                    super.setEnabled(false);
                }
            } catch (Exception exception) {
                GrasppeKit.debugText("Command Menu Update", "Cannot update() menu item.", 3);
            }
        }

        /**
         * Method description
         *
         * @return
         */
        @Override
		public float getAlignmentY() {
            return 0.5f;
        }
    }
}
