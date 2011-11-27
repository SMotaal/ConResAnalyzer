package com.grasppe.lure.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class AbstractCommand extends AbstractAction implements Observer, Observable {

        protected ActionListener	actionListener;
        protected boolean			executable = false;
        protected boolean			executed   = false;
        protected boolean			executing  = false;
        protected boolean			useModel   = false;
        protected AbstractModel		model;
        protected AbstractController commandHandler;
        protected Observers			observers = new Observers();
        protected KeyEvent			keyEvent;

//      protected GrasppeKit        grasppeKit     = GrasppeKit.getInstance();

        /** Field description */
        public int	mnemonicKey;

        /** Field description */
        public String	name = getClass().getSimpleName();

        /**
         * Constructs a new command with the specified listener.
         *
         * @param listener
         * @param text
         */
        public AbstractCommand(ActionListener listener, String text) {
            super(text);	// , icon);

//          debugText("Abstract Command",
//                    "mnemonicKey = char '" + mnemonicKey + "'; int '" + (int)mnemonicKey + "'",
//                    3);     // ; boolean '" + (boolean) mnemonicKey + "'",3);
//          if (mnemonicKey != '\u0000') setMnemonicKey(mnemonicKey);
            actionListener = listener;
        }

        /**
         * Constructs a new command with the specified listener.
         *
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
         *
         * @param e
         */
		public void actionPerformed(ActionEvent e) {
            executed = false;
            actionListener.actionPerformed(e);
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean altPressed() {
            if (getKeyEvent() != null) return getKeyEvent().isAltDown();

            return false;

//          boolean   altDown     = getKeyEvent().isAltDown();
//          boolean   controlDown = getKeyEvent().isControlDown();
//          boolean   shiftDown   = getKeyEvent().isShiftDown();
//          boolean   metaDown    = getKeyEvent().isMetaDown();
        }

        /**
         * Attaches an observer through the observers object which will include the observer in future update() notify calls.
         *
         * @param observer
         */
        public void attachObserver(Observer observer) {
            observers.attachObserver(observer);
        }

        /**
         * Returns the current executable state. Override this method to implement any additional checks needed to determine the executable state.
         *
         * @return  current executable state
         */
        public boolean canExecute() {

            // update();
            return executable;
        }

        /**
         * Sets and returns the current executable state. Observers will be notified only if the canExecute() returned a state different from the initial state. Override the canExecute() method to implement any additional checks when needed.
         *
         * @param state executable state
         *
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
                GrasppeKit.debugText("Command Execution Succeeded", GrasppeKit.lastSplit(toString()), 3);

                return true;
            }

            GrasppeKit.debugText("Command Execution Duplicity Error", GrasppeKit.lastSplit(toString()), 2);

            return false;
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean controlPressed() {
            return getKeyEvent().isControlDown();
        }

        /**
         * Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
         *
         * @param observer
         */
        public void detachObserver(Observer observer) {
            observers.detachObserver(observer);
        }

        /**
         * Called by the actionListener, following a call to actionPerformed(). Returns false if performCommand() or complete() return false.
         * @return  true if actions completed successfully!
         */
        public final boolean execute() {
            if (executing) return false;
            if (hasExecuted()) return false;
            if (!canExecute()) update();
            if (!canExecute())
                throw new IllegalStateException(getName()
                    + " could not execute in its current state.");
            GrasppeKit.debugText("Command Execution Started", GrasppeKit.lastSplit(toString()));
            try {
	            executing = true;
	
	            if (!perfomCommand() ||!completed()) 
	            	throw new InternalError(this.getName() + " failed to complete successfully due to an internal error.");
	            
	            executing = false;
	            executed  = true;
	            GrasppeKit.debugText("Command Execution Ends", GrasppeKit.lastSplit(toString()));
            
            } catch (Exception exception) {
            	GrasppeKit.debugText("Command Execution Failed", GrasppeKit.lastSplit(toString()), 2);
            	executing = false;
            	executed  = false;
            	return false;            	
            }

            return executed;
        }

        /**
         * Method description
         *
         * @param forcedAction
         *
         * @return
         */
        public final boolean execute(boolean forcedAction) {
            GrasppeKit.debugText("Command Execution Forced", GrasppeKit.lastSplit(toString()), 3);

            // update();
            // if (forcedAction)
            executed = false;

            // if (forcedAction)
            canExecute(forcedAction);

            return execute();
        }

        /**
         * Method description
         *
         * @param e
         *
         * @return
         */
        public final boolean execute(KeyEvent e) {
            setKeyEvent(e);
            execute();
            setKeyEvent();

            return executed;
        }

        /**
         * Method description
         *
         *
         * @param forcedAction
         * @param e
         *
         * @return
         */
        public final boolean execute(boolean forcedAction, KeyEvent e) {
            setKeyEvent(e);
            execute(forcedAction);
            setKeyEvent();

            return executed;
        }

        /**
         * Detaches from the model when being finalize through garbage collection.
         *
         * @throws Throwable
         */
        @Override
        protected void finalize() throws Throwable {
            try {
                model.detachObserver(this);
                GrasppeKit.debugText("Command Finalize/Detatch Succeeded",
                          model.getClass().getSimpleName() + " is no longer attached to "
                          + GrasppeKit.lastSplit(toString()), 3);
            } catch (Exception e) {

                // Command has no model and can finalize immediately
                GrasppeKit.debugText("Command Finalize/Detatch Unnecessary",
                          "No models were attached to " + GrasppeKit.lastSplit(toString()), 2);
            }

            super.finalize();
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean metaPressed() {
            return getKeyEvent().isMetaDown();
        }

        /**
         * Notifies all observer through the observers object which calls update().
         *
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
         * Method description
         *
         * @return
         */
        public boolean shiftPressed() {
            return getKeyEvent().isShiftDown();
        }

        /**
         * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
         */
        public void update() {
            canExecute(!useModel || (model != null));		// either not using model or model is not empty!

            if (canExecute()) {
                GrasppeKit.debugText("Abstract Command Update", getName() + " can execute.");
            } else {
                GrasppeKit.debugText("Abstract Command Update", getName() + " cannot execute.");
            }

            notifyObservers();
        }

        /**
         * Method description
         *
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
         * Returns the current model. Override this method with a specific model type.
         *
         * @return
         */
        public AbstractModel getModel() {
            return model;
        }

        /**
         * Returns the name of the command.
         *
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * Called during execute() to determine if the event command has already executed. Override this method to change the execute-once behavior as needed.
         *
         * @return  executed since actionPerofmed()
         */
        public boolean hasExecuted() {
            return executed;
        }

        /**
         * Method description
         */
        public void setKeyEvent() {
            keyEvent = null;
        }

        /**
         * Method description
         *
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
                      "The key '" + this.mnemonicKey + "' is assigned to " + getName(), 3);

            // super.putValue(Action.MNEMONIC_KEY, mnemonicKey);
        }

        /**
         * Attaches the command to the specified model and calls update() to reflect the state of the model.
         *
         * @param model
         */
        public void setModel(AbstractModel model) {
            model.attachObserver(this);
            this.model = model;
            useModel   = true;
            update();
        }

		public void notifyObserver(Observer observer) {
			observers.notifyObserver(observer);
		}
    }