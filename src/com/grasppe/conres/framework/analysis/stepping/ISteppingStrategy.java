/*
 * @(#)ISteppingStrategy.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

/**
 * @author daflair
 */
public interface ISteppingStrategy {

    /**
     * @return
     */
    boolean execute();

    /**
     * @return
     */
    boolean undo();

    /**
     * @return
     */
    boolean isValid();
}
