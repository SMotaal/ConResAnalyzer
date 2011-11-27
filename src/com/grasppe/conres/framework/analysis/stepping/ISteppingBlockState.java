/*
 * @(#)ISteppingBlockState.java   11/08/25
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
public interface ISteppingBlockState {

    /**
     * Method description
     *
     *
     * @return
     */
    int getRow();

    /**
     * Method description
     *
     *
     * @return
     */
    int getColumn();

    /**
     * Method description
     *
     *
     * @return
     */
    int getRows();

    /**
     * Method description
     *
     *
     * @return
     */
    int getColumns();

    /**
     * Method description
     *
     *
     * @return
     */
    int[][] getBlockMap();

    /**
     * Method description
     *
     *
     * @return
     */
    ISteppingBlockState copy();

    /**
     * Method description
     *
     *
     * @param otherState
     *
     * @return
     */
    boolean equivalent(ISteppingBlockState otherState);
}
