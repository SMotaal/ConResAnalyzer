/*
 * @(#)ISteppingBlockState.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

/**
 * @author daflair
 */
public interface ISteppingBlockState {

    /**
     * @return
     */
    ISteppingBlockState copy();

    /**
     * @param otherState
     * @return
     */
    boolean equivalent(ISteppingBlockState otherState);

    /**
     * @return
     */
    int[][] getBlockMap();

    /**
     * @return
     */
    int getColumn();

    /**
     * @return
     */
    int getColumns();

    /**
     * @return
     */
    int getRow();

    /**
     * @return
     */
    int getRows();
    
    int getFirstColumn();
}
