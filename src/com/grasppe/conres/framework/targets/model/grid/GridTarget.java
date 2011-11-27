/*
 * @(#)GridTarget.java   11/10/26
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.targets.model.grid;

import com.grasppe.conres.framework.targets.model.axis.GridAxis;
import com.grasppe.lure.components.ObservableObject;

/**
 * @author daflair
 *
 */
public class GridTarget extends ObservableObject {

    /** Field description */
    protected GridBlock[]	testBlockes;

    /** Field description */
    protected int	blockCount,	rowCount, columnCount;

    /** Field description */
    protected GridAxis	blockAxis, rowAxis,	columnAxis;

    /**
     * Constructs ...
     *
     */
    public GridTarget() {
        super();
    }

    /**
     * Constructs ...
     *
     *
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
     * Constructs ...
     *
     *
     */
    protected void createAxesValues() {
        blockAxis.generateValues();
        rowAxis.generateValues();
        columnAxis.generateValues();
        System.out.println("Created axis values successfully.");
    }

    /**
     * Method description
     *
     *
     * @param blockSteps
     */
    protected void setBlockSteps(double blockSteps[]) {
        blockAxis.setSteps(blockSteps);
    }

    /**
     * Method description
     *
     *
     * @param rowSteps
     */
    protected void setRowSteps(double rowSteps[]) {
        rowAxis.setSteps(rowSteps);
    }

    /**
     * Method description
     *
     *
     * @param columnSteps
     */
    protected void setColumnSteps(double columnSteps[]) {
        columnAxis.setSteps(columnSteps);
    }

    /**
     * Method description
     *
     *
     * @param blockIndex
     *
     * @return
     */
    public GridBlock getBlock(int blockIndex) {
        return testBlockes[blockIndex];
    }

    /**
     * Method description
     *
     *
     * @param blockIndex
     * @param rowIndex
     * @param columnIndex
     *
     * @return
     */
    public GridPatch getPatch(int blockIndex, int rowIndex, int columnIndex) {
        return testBlockes[blockIndex].getPatch(rowIndex, columnIndex);
    }
}
