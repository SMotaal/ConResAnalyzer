/*
 * @(#)Caller.java   11/12/03
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.lure.framework;

/**
 *     Class description
 *     @version        $Revision: 1.0, 11/11/11
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class Caller {

    /** Field description */
    public StackTraceElement[]	stackTraceElements;

    /** Field description */
    public StackTraceElement	caller;

    /** Field description */
    public String	className;

    /** Field description */
    public String	simpleName;

    /** Field description */
    public String	methodName;

    /** Field description */
    public int	lineNumber;

    /**
     */
    public Caller() {
        super();
    }

    /**
     * @param stackTraceElements
     * @param caller
     * @param className
     * @param methodName
     * @param lineNumber
     */
    public Caller(StackTraceElement[] stackTraceElements, StackTraceElement caller,
                  String className, String methodName, int lineNumber) {
        this();
        this.stackTraceElements = stackTraceElements;
        this.caller             = caller;
        this.className          = className;
        this.simpleName         = GrasppeKit.lastSplit(className);
        this.methodName         = methodName;
        this.lineNumber         = lineNumber;
    }

//  public static Caller  newCaller(StackTraceElement[] stackTraceElements, StackTraceElement caller,
//                String className, String methodName, int lineNumber) {
//    return new Caller(stackTraceElements, caller, className, methodName, lineNumber);
//  }
}
