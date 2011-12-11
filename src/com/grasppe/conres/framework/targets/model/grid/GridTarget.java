/*
 * @(#)GridTarget.java   11/10/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.targets.model.grid;

import com.grasppe.conres.framework.targets.model.axis.GridAxis;
import com.grasppe.lure.components.ObservableObject;

/**
 * @author daflair
 */
public class GridTarget extends ObservableObject {

    /** Field description */
    protected GridBlock[]	testBlockes;

    /** Field description */
    protected int	blockCount,	rowCount, columnCount;

    /** Field description */
    protected GridAxis	blockAxis, rowAxis,	columnAxis;
    protected GridBlock	activeBlock = null;

    /**
     */
    public GridTarget() {
        super();
    }

    /**
     * @param blockSteps
     * @param rowSteps
     * @param columnSteps
     */
    public GridTarget(double[] blockSteps, double[] rowSteps, double[] columnSteps) {
        this();
        setBlockSteps(blockSteps);
        setRowSteps(rowSteps);
        setColumnSteps(columnSteps);
        createAxesValues();
    }

    /**
     */
    protected void createAxesValues() {
        blockAxis.generateValues();
        rowAxis.generateValues();
        columnAxis.generateValues();
//        System.out.println("Created axis values successfully.");
    }

    /**
     * @return the activeBlock
     */
    public GridBlock getActiveBlock() {
        return activeBlock;
    }

    /**
     * @param blockIndex
     * @return
     */
    public GridBlock getBlock(int blockIndex) {
        return testBlockes[blockIndex];
    }

    /**
     * @param blockIndex
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public GridPatch getPatch(int blockIndex, int rowIndex, int columnIndex) {
        return testBlockes[blockIndex].getPatch(rowIndex, columnIndex);
    }

    /**
     * @param activeBlock the activeBlock to set
     */
    public void setActiveBlock(GridBlock activeBlock) {
        this.activeBlock = activeBlock;
    }

    /**
     * @param blockSteps
     */
    protected void setBlockSteps(double blockSteps[]) {
        blockAxis.setSteps(blockSteps);
    }

    /**
     * @param columnSteps
     */
    protected void setColumnSteps(double columnSteps[]) {
        columnAxis.setSteps(columnSteps);
    }

    /**
     * @param rowSteps
     */
    protected void setRowSteps(double rowSteps[]) {
        rowAxis.setSteps(rowSteps);
    }
}
