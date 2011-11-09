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
package com.Grasppe.ConRes.Analyzer.IJ;

import com.Grasppe.Components;
import com.Grasppe.Components.AbstractCommand;

import ij.plugin.frame.PlugInFrame;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * @author daflair
 *
 */
public class ConResAnalyzerMenu extends PlugInFrame implements ActionListener {

    protected Components	components = new Components();

    /** Field description */
    protected Hashtable<Actions, PluginMenuAction>	actionMap;

    /** Field description */
    protected Hashtable<Actions, PluginMenuItem>	buttonMap;

    /** Field description */
    protected JToolBar	pluginBar = new JToolBar();
    protected String	name      = this.getClass().getSimpleName();	// "ConResAnalyzerMenu";

    /**
     * Constructs ...
     */
    public ConResAnalyzerMenu() {
        super("ConResAnalyzerMenu");
        setSize(450, 350);

        WindowListener	wndCloser = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };

        addWindowListener(wndCloser);

        // createButtonsFromActions();
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
        public String toString() {
            return simpleForm(name().toString());
        }
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
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
        PluginMenuItem	thisButton = new PluginMenuItem(command);

        // buttonMap.put((command, thisButton);
        components.debugText("Menu Button Created", components.lastSplit(thisButton.toString()));

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
            components.debugText("Menu Action Created",
                                 components.lastSplit(thisAction.toString()));

//          IJ.showMessage(this.getClass().getSimpleName() + " Menu Action Created: "
//                         + thisAction.toString());
            PluginMenuItem	thisButton = new PluginMenuItem(thisAction);

            buttonMap.put(action, thisButton);
            components.debugText("Menu Button Created",
                                 components.lastSplit(thisButton.toString()));

//          IJ.showMessage(this.getClass().getSimpleName() + " Menu Button Created: "
//                         + thisButton.toString());
            pluginBar.add(thisButton);
        }

        super.add(pluginBar, BorderLayout.NORTH);

        // getContentPane().add(pluginBar, BorderLayout.NORTH);
    }

    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/08
     * @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
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
    public class PluginMenuItem extends JButton implements MouseListener {

        /** Field description */
        protected Border	m_raised;

        /** Field description */
        protected Border	m_lowered;

        /** Field description */
        protected Border	m_inactive;

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
            setBorder(m_inactive);
            setMargin(new Insets(1, 1, 1, 1));
            addMouseListener(this);
            setRequestFocusEnabled(false);

            String	actionName,	actionDescription;

            // Tooltip Text
            try {
                actionDescription = (String)action.getValue(TOOL_TIP_TEXT_KEY);
            } catch (Exception exception) {
                actionDescription = action.toString();
            }

            setToolTipText(actionDescription);

            // Button Text
            try {
                actionName = ((AbstractCommand)action).getName();
                components.debugText("Command Menu Item",
                                     "getName ==>" + actionName + " ==> "
                                     + components.humanCase(actionName), 3);
            } catch (Exception exception) {
                actionName = action.toString();
                components.debugText("Command Menu Item",
                                     "toString ==>" + actionName + " ==> "
                                     + components.humanCase(actionName), 3);
            }

            setText(components.humanCase(actionName, true));
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
        public void mouseClicked(MouseEvent e) {}

        /**
         * Method description
         *
         * @param e
         */
        public void mouseEntered(MouseEvent e) {
            setBorder(m_raised);
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
            setBorder(m_lowered);
        }

        /**
         * Method description
         *
         * @param e
         */
        public void mouseReleased(MouseEvent e) {
            setBorder(m_inactive);
        }

        /**
         * Method description
         *
         * @return
         */
        public float getAlignmentY() {
            return 0.5f;
        }
    }
}
