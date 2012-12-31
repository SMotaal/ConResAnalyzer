/*
 * @(#)AbstractCommand.java   11/12/03
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.lure.components;

import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *     Class description
 *     @version        $Revision: 1.0, 11/11/08
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AbstractCommand extends AbstractAction implements Observer, Observable {

    /**
	 * @return the displayText
	 */
	public String getDisplayText() {
		if (displayText==null)
			displayText = GrasppeKit.humanCase(getName(), true);
		return displayText;
	}

	/**
	 * @return the ignoreMenu
	 */
	public boolean isIgnoreMenu() {
		return ignoreMenu;
	}

	/**
	 * @return the commandGrouping
	 */
	public String getMenuGrouping() {
		if (commandGrouping==null && model!=null) return model.getClass().getSimpleName(); //.toLowerCase();
		if (commandGrouping==null && actionListener!=null) return actionListener.getClass().getSimpleName(); //.toLowerCase();
		if (commandGrouping==null) return "";
		return commandGrouping;
	}
	
	

	protected ActionListener		actionListener;
    protected boolean				executable = false;
    protected boolean				executed   = false;
    protected boolean				executing  = false;
    protected boolean				useModel   = false;
    protected AbstractModel			model;
    protected AbstractController	commandHandler;
    protected Observers				observers = new Observers(this);
    protected KeyEvent				keyEvent;
    protected String				description = "";
    protected String				commandMenu        = null;
    protected String	commandGrouping = null;
    protected String displayText = null;
    protected boolean ignoreMenu = false;
    int								dbg         = 0;

//  protected GrasppeKit        grasppeKit     = GrasppeKit.getInstance();

    /** Field description */
    public int	mnemonicKey;

    /** Field description */
    public String	name = getClass().getSimpleName();

    /**
     * Constructs a new command with the specified listener.
     * @param listener
     * @param text
     */
    public AbstractCommand(ActionListener listener, String text) {
        super(text);	// , icon);

        initializeActionFields();

//      attachObserver(this);

//          debugText("Abstract Command",
//              "mnemonicKey = char '" + mnemonicKey + "'; int '" + (int)mnemonicKey + "'",
//              3);     // ; boolean '" + (boolean) mnemonicKey + "'",3);
//          if (mnemonicKey != '\u0000') setMnemonicKey(mnemonicKey);
        actionListener = listener;
    }

    /**
     * Constructs a new command with the specified listener.
     * @param listener
     * @param text
     * @param executable
     */
    public AbstractCommand(ActionListener listener, String text, boolean executable) {
        this(listener, text);
        canExecute(executable);
    }

    /**
     * Called when action is triggered and passes the ActionEvent to actionListener, which is responsible for calling the execute() method.
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        executed = false;
        actionListener.actionPerformed(e);
    }

    /**
     * @return
     */
    public boolean altPressed() {
        if (getKeyEvent() != null) return getKeyEvent().isAltDown();

        return false;
    }

    /**
     * Attaches an observer through the observers object which will include the observer in future update() notify calls.
     * @param observer
     */
    public void attachObserver(Observer observer) {
        observers.attachObserver(observer);
    }

    /**
     * Returns the current executable state. Override this method to implement any additional checks needed to determine the executable state.
     * @return  current executable state
     */
    public boolean canExecute() {

        return executable;
    }

    /**
     * Sets and returns the current executable state. Observers will be notified only if the canExecute() returned a state different from the initial state. Override the canExecute() method to implement any additional checks when needed.
     * @param state executable state
     * @return  new executable state
     */
    public final boolean canExecute(boolean state) {
        boolean	initialState = executable;

        executable = state;

        boolean	newState = canExecute();

        if (initialState != newState) notifyObservers();

        return newState;
    }

    /**
     * Sets executed to true when called by execute() after perfomCommand() returns true.
     * @return  true only if executed changed from false to true!
     */
    public boolean completed() {
        if (!executed) {
            executed = true;
            GrasppeKit.debugText("Command Execution Succeeded", GrasppeKit.lastSplit(toString()),
                                 dbg);

            return true;
        }

        GrasppeKit.debugText("Command Execution Duplicity Error", GrasppeKit.lastSplit(toString()),
                             dbg);

        return false;
    }

    /**
     * @return Control down for PC or Command down for Mac!
     */
    public boolean controlKeyPressed() {
        if (getKeyEvent() == null) return false;
        if (GrasppeKit.OperatingSystem.isMac()) return getKeyEvent().isMetaDown();
        else return getKeyEvent().isControlDown();
    }

    /**
     * @return
     */
    public boolean controlPressed() {
        if (getKeyEvent() != null) return getKeyEvent().isControlDown();

        return false;
    }

    /**
     * Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
     * @param observer
     */
    public void detachObserver(Observer observer) {
        observers.detachObserver(observer);
    }

    /**
     * Called by the actionListener, following a call to actionPerformed(). Returns false if performCommand() or complete() return false.
     * @return  true if actions completed successfully!
     * @throws ExecutionException
     */
    public final boolean execute() throws ExecutionException {
        if (executing) return false;
        if (hasExecuted()) return false;
        if (!canExecute()) update();
        if (!canExecute())
            throw new IllegalStateException(getName() + " could not execute in its current state.");
        GrasppeKit.debugText("Command Execution Started", GrasppeKit.lastSplit(toString()), dbg);

        try {
            executing = true;

            if (!perfomCommand() ||!completed()) {
                update();
                throw new InternalError(
                    this.getName() + " failed to complete successfully due to an internal error.");
            }

            executing = false;
            executed  = true;
            GrasppeKit.debugText("Command Execution Ends", GrasppeKit.lastSplit(toString()), dbg);

        } catch (Exception exception) {
            executing = false;
            executed  = false;

            String	name = GrasppeKit.humanCase(getName(), true);

//          ExecutionException executionException = new ExecutionException(exception.getClass().getSimpleName() + " - " + exception.getMessage() + " - " + GrasppeKit.lastSplit(toString()), exception);
//          GrasppeKit.debugError("Executing " + name, executionException, 2);
            GrasppeKit.debugError("Executing " + name, exception, 4);

            return false;
        }

        return executed;
    }

    /**
     * @param e
     * @return
     */
    public final boolean execute(KeyEvent e) {
        setKeyEvent(e);

        try {
            execute();
        } catch (Exception exception) {
            GrasppeKit.debugError("Executing " + GrasppeKit.lastSplit(toString()), exception, 4);
        }

        setKeyEvent();

        return executed;
    }

    /**
     * Detaches from the model when being finalize through garbage collection.
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
    	GrasppeKit.debugText("Finalizing", getClass().getSimpleName() , 1);
//        try {
//            model.detachObserver(this);
//            observers.detachObservers();
//            
//            GrasppeKit.debugText("Command Finalize/Detatch Succeeded",
//                                 model.getClass().getSimpleName() + " is no longer attached to "
//                                 + GrasppeKit.lastSplit(toString()), dbg);
//        } catch (Exception e) {
//
//            // Command has no model and can finalize immediately
//            GrasppeKit.debugText("Command Finalize/Detatch Unnecessary",
//                                 "No models were attached to " + GrasppeKit.lastSplit(toString()),
//                                 2);
//        }
    	
    	detatch();

        super.finalize();
    }
    
    public void detatch() {
    	GrasppeKit.debugText("Detatching", getClass().getSimpleName() , 1);
        try {
            model.detachObserver(this);
            observers.detachObservers();
            
            GrasppeKit.debugText("Command Finalize/Detatch Succeeded",
                                 model.getClass().getSimpleName() + " is no longer attached to "
                                 + GrasppeKit.lastSplit(toString()), dbg);
        } catch (Exception e) {

            // Command has no model and can finalize immediately
            GrasppeKit.debugText("Command Finalize/Detatch Unnecessary",
                                 "No models were attached to " + GrasppeKit.lastSplit(toString()),
                                 2);
        }
        
    }

    /**
     *  @return
     */
    public final boolean forceExecute() {
        GrasppeKit.debugText("Command Execution Forced", GrasppeKit.lastSplit(toString()), dbg);

        executed = false;
        canExecute(true);

        try {
            execute();
        } catch (Exception exception) {
            GrasppeKit.debugError("Executing " + GrasppeKit.lastSplit(toString()), exception, 4);
        }

        return executed;
    }

    /**
     *  @param e
     *  @return
     */
    public final boolean forceExecute(KeyEvent e) {
        setKeyEvent(e);
        forceExecute();
        setKeyEvent();

        return executed;
    }

    /**
     */
    protected void initializeActionFields() {

        /*
         * Action Properties (http://docs.oracle.com/javase/tutorial/uiswing/misc/action.html)
         *
         * This table defines the properties that can be set on an action. The second column lists which components automatically use the properties (and what method is specifically called). For example, setting the ACCELERATOR_KEY on an action that is then attached to a menu item, means that JMenuItem.setAccelerator(KeyStroke) is called automatically.
         *
         * ACCELERATOR_KEY - (setAccelerator): JMenuItem
         * The KeyStroke to be used as the accelerator for the action. For a discussion of accelerators versus mnemonics, see Enabling Keyboard Operation. Introduced in 1.3.
         *
         * ACTION_COMMAND_KEY - (setActionCommand): AbstractButton, JCheckBox, JRadioButton
         * The command string associated with the ActionEvent.
         *
         * LONG_DESCRIPTION - : none
         * The longer description for the action. Can be used for context-sensitive help.
         *
         * MNEMONIC_KEY - (setMnemonic): AbstractButton, JMenuItem, JCheckBox, JRadioButton
         * The mnemonic for the action. For a discussion of accelerators versus mnemonics, see Enabling Keyboard Operation. Introduced in 1.3.
         *
         * NAME - (setText): AbstractButton, JMenuItem, JCheckBox, JRadioButton
         * The name of the action. You can set this property when creating the action using the AbstractAction(String) or AbstractAction(String, Icon) constructors.
         *
         * SHORT_DESCRIPTION - (setToolTipText): AbstractButton, JCheckBox, JRadioButton
         * The short description of the action.
         */
        try {
            String	name = GrasppeKit.humanCase(getName(), true);

            if (!name.isEmpty()) putValue(Action.NAME, name);

            String	description = getDescription();

            if (!description.trim().isEmpty()) putValue(Action.SHORT_DESCRIPTION, description);

            int	mnemonicKey = getMnemonicKey();

            if (mnemonicKey > 0) putValue(Action.MNEMONIC_KEY, mnemonicKey);
        } catch (Exception exception) {
            GrasppeKit.debugText("Initialize Action Fields Error",
                                 "The key '" + this.mnemonicKey + "' is assigned to " + name, 2);
        }
    }

    /**
     * @return
     */
    public boolean metaPressed() {
        if (getKeyEvent() != null) return getKeyEvent().isMetaDown();

        return false;
    }

    /**
     *  @param observer
     */
    public void notifyObserver(Observer observer) {
        observers.notifyObserver(observer);
    }

    /**
     * Notifies all observer through the observers object which calls update().
     */
    public void notifyObservers() {
        observers.notifyObservers();
    }

    /**
     * Called by execute to complete execution of command actions. This method must be overloaded and return true for the action to complete.
     * @return  false unless otherwise overridden!
     */
    protected boolean perfomCommand() {
        return false;
    }

    /**
     * @return
     */
    public boolean shiftPressed() {
        if (getKeyEvent() != null) return getKeyEvent().isShiftDown();

        return false;
    }

    /**
     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
     */
    public void update() {

//      notifyObservers();
        // canExecute(!useModel || (model != null));     // either not using model or model is not empty!
        initializeActionFields();

        if (canExecute()) {
            GrasppeKit.debugText("Abstract Command Update", getName() + " can execute.", dbg);
        } else {
            GrasppeKit.debugText("Abstract Command Update", getName() + " cannot execute.", dbg);
        }

    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    /**
     * @return the mnemonicKey
     */
    public int getMnemonicKey() {
        return mnemonicKey;
    }

    /**
     * Returns the current model. Override this method with a specific model commandMenu.
     * @return
     */
    public AbstractModel getModel() {
        return model;
    }

    /**
     * Returns the name of the command.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return the commandMenu
     */
    public String getMenuKey() {
        return null;
    }

    /**
     * Called during execute() to determine if the event command has already executed. Override this method to change the execute-once behavior as needed.
     * @return  executed since actionPerofmed()
     */
    public boolean hasExecuted() {
        return executed;
    }

    /**
     */
    public void setKeyEvent() {
        keyEvent = null;
    }

    /**
     * @param e
     */
    public void setKeyEvent(KeyEvent e) {
        keyEvent = e;
    }

    /**
     * @param mnemonicKey the mnemonicKey to set
     */
    public void setMnemonicKey(int mnemonicKey) {
        this.mnemonicKey = mnemonicKey;
        GrasppeKit.debugText("Setting Action Mnemonic",
                             "The key '" + this.mnemonicKey + "' is assigned to " + getName(), dbg);

        // super.putValue(Action.MNEMONIC_KEY, mnemonicKey);
    }

    /**
     * Attaches the command to the specified model and calls update() to reflect the state of the model.
     * @param model
     */
    public void setModel(AbstractModel model) {
        model.attachObserver(this);
        this.model = model;
        useModel   = true;
        update();
    }

    /**
     * Class description
     *  @version        $Revision: 1.0, 11/12/11
     *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public static class Types {

        /** Field description */
        public static final String	FILE = "file";
        
        /** Field description */
        public static final String	CASE = "case";        

        /** Field description */
        public static final String	EDIT = "edit";

        /** Field description */
        public static final String	VIEW = "view";

        /** Field description */
        public static final String	TOOLS = "tools";

        /** Field description */
        public static final String	WINDOW = "window";

        /** Field description */
        public static final String	HELP = "help";
    }


	@Override
	public void detatch(Observable oberservableObject) {
		// TODO Auto-generated method stub
		
	}
}
