package com.grasppe.lure.components;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.grasppe.lure.framework.GrasppeKit;


/**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/09
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class AbstractOperation extends AbstractAction {		// implements Observer, Observable {

        protected boolean	executable = false;
        protected boolean	executed   = false;
        protected boolean	executing  = false;
        protected double	progress   = 0.0;
        
    	public enum ExitCodes {
    		SUCCESS,	// Normal completion of operation
    		FAILED,	// Interrupted execution with unrecoverable internal error
    		CANCELED, // User canceled the operation
    		FORCED, // Interrupted execution initiated externally
    		INVALIDSELECTION, // User selection could not be validated
    		UNVERIFIEDSELECTION, // User selection could not be verified
    		INACCESSIBLERESOURCE, // Interrupted operation with unrecoverable due to an inaccessible resource
    	}

//      protected GrasppeKit    grasppeKit     = GrasppeKit.getInstance();

        /** Field description */
        public String	name = getClass().getSimpleName();

        /**
         * Constructs ...
         */
        public AbstractOperation() {
            super();
        }

        /**
         * @param name
         */
        public AbstractOperation(String name) {
            super(name);
        }

        /**
         * @param name
         * @param icon
         */
        public AbstractOperation(String name, Icon icon) {
            super(name, icon);
        }

        /**
         * Method description
         *
         * @param arg0
         */
        public void actionPerformed(ActionEvent arg0) {
            execute();
        }

        /**
         * Called by an initiator to performOperation(). Returns false if performOperation() did not follow the intended scenario.
         * @return  true if execution follow intended scenario
         */
        public final boolean execute() {
            if (executing) return false;
            if (isExecuted()) return false;		// TODO: Implement duplicity resolution
            if (!isExecutable())
                throw new IllegalStateException(getName() + " is not currently executable.");
            GrasppeKit.debugText("Operation Execution Started", GrasppeKit.lastSplit(toString()), 3);
            executing = true;
            setExecuted(perfomOperation());
            executing = false;
            if (isExecuted()) GrasppeKit.debugText("Operation Execution Ends", GrasppeKit.lastSplit(toString()), 3);
            else GrasppeKit.debugText("Operation Execution Failed", GrasppeKit.lastSplit(toString()), 2);

            return isExecuted();
        }

        /**
         * Method description
         *
         * @param forcedAction
         *
         * @return
         */
        public final boolean execute(boolean forcedAction) {
            GrasppeKit.debugText("Operation Execution Forced", GrasppeKit.lastSplit(toString()), 3);

            // if (forcedAction)
            setExecuted(false);
            setExecutable(true);

            return execute();
        }

        /**
         * Called by execute to complete execution of command actions. This method must be overloaded and return true for the action to complete.
         * @return  false unless otherwise overridden!
         */
        protected boolean perfomOperation() {
            return false;
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
         * @return the progress
         */
        protected double getProgress() {
            return progress;
        }

        /**
         * @return the executable
         */
        protected boolean isExecutable() {
            return executable;
        }

        /**
         * @return the executed
         */
        protected boolean isExecuted() {
            return executed;
        }

        /**
         * @param executable the executable to set
         */
        protected void setExecutable(boolean executable) {
            this.executable = executable;
        }

        /**
         * @param executed the executed to set
         */
        protected void setExecuted(boolean executed) {
            this.executed = executed;
        }

        /**
         * @param progress the progress to set
         */
        protected void setProgress(double progress) {
            this.progress = progress;
        }
    }