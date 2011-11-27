/*
 * @(#)ISteppingStrategy.java   11/08/25
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.analysis.stepping;

/**
 * @author daflair
 *
 */
public interface ISteppingStrategy {

    /**
     * Method description
     *
     *
     * @return
     */
    boolean execute();

    /**
     * Method description
     *
     *
     * @return
     */
    boolean isValid();

    /**
     * Method description
     *
     *
     * @return
     */
    boolean undo();
}
