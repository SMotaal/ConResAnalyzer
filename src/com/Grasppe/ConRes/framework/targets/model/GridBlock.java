/*
 * @(#)TargetBlockModel.java   11/10/26
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.targets.model;

import com.grasppe.lure.components.ObservableObject;

/**
 * @author daflair
 *
 */
public class GridBlock extends ObservableObject {

    /** Field description */
    protected ConResPatch[][]	testPatches;

    /**
     * Method description
     *
     *
     * @param rowIndex
     * @param columnIndex
     *
     * @return
     */
    public GridPatch getPatch(int rowIndex, int columnIndex) {
        return testPatches[rowIndex][columnIndex];
    }
}
