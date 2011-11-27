/*
 * @(#)AbstractOperation.java   11/11/26
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.lure.components;

import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.ExitCodes;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 *     Class description
 *    
 *     @version        $Revision: 1.0, 11/11/09
 *     @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public class AbstractOperation extends AbstractAction {		// implements Observer, Observable {

    protected boolean	executable = false;
    protected boolean	executed   = false;
    protected boolean	executing  = false;
    protected double	progress   = 0.0;
    protected ExitCodes	exitCode   = ExitCodes.PENDING;

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
        GrasppeKit.debugText("Operation Execution Started", GrasppeKit.lastSplit(toString()));
        executing = true;
        setExecuted(perfomOperation());
        executing = false;
        if (isExecuted()) setExitCode(ExitCodes.SUCCESS);		// GrasppeKit.debugText("Operation Execution Ends", GrasppeKit.lastSplit(toString()));
        else setExitCode(ExitCodes.FAILED);		// GrasppeKit.debugText("Operation Execution Failed", GrasppeKit.lastSplit(toString()), 2);

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
        GrasppeKit.debugText("Operation Execution Forced", GrasppeKit.lastSplit(toString()));

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
     * 	@return
     */
    public ExitCodes getExitCode() {
        return exitCode;
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
     * 	@return
     */
    public boolean isExecuting() {
        return executing;
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
     * 	@param executing
     */
    public void setExecuting(boolean executing) {
        this.executing = executing;
    }

    /**
     * 	@param exitCode
     */
    public void setExitCode(ExitCodes exitCode) {
        this.exitCode = exitCode;

        switch (exitCode) {

        case SUCCESS :
            GrasppeKit.debugText("Operation completed successfuly",
                                 GrasppeKit.lastSplit(toString()));
            break;

        case FAILED :
            GrasppeKit.debugText("Operation execution failed", GrasppeKit.lastSplit(toString()));
            break;

        case CANCELED :
            GrasppeKit.debugText("Operation execution terminated by user",
                                 GrasppeKit.lastSplit(toString()));
            break;

        case FORCED :
            GrasppeKit.debugText("Operation execution interrupeted before completition",
                                 GrasppeKit.lastSplit(toString()));
            break;

        case UNEXPECTED :
            GrasppeKit.debugText(
                "Operation execution failed or terminated before yielding valid results",
                GrasppeKit.lastSplit(toString()));
            break;

        case INACCESSIBLERESOURCE :
            GrasppeKit.debugText("Operation execution failed to access the necessary resources",
                                 GrasppeKit.lastSplit(toString()));
            break;

        case PENDING :
        }
    }

    /**
     * @param progress the progress to set
     */
    protected void setProgress(double progress) {
        this.progress = progress;
    }
}
