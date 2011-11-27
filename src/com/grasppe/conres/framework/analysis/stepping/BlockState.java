/*
 * @(#)BlockState.java   11/08/25
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
public class BlockState implements ISteppingBlockState {

    /** Field description */
    protected int	blockMap[][];

    /** Field description */
    protected int	rows, columns, row,	column;

    /**
     * Constructs ...
     *
     */
    protected BlockState() {}

    /**
     *
     *
     * @param sourceState
     */
    protected BlockState(ISteppingBlockState sourceState) {
        this.rows     = sourceState.getRows();
        this.columns  = sourceState.getColumns();
        this.row      = sourceState.getRow();
        this.column   = sourceState.getColumn();
        this.blockMap = sourceState.getBlockMap();
    }

    /**
     * Constructs ...
     *
     *
     * @param rows
     * @param columns
     * @param row
     * @param column
     * @param blockMap
     */
    public BlockState(int rows, int columns, int row, int column, int blockMap[][]) {
        this.rows     = rows;
        this.columns  = columns;
        this.row      = row;
        this.column   = column;
        this.blockMap = blockMap;
    }

    /**
     * @return the rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return the columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * @return the blockMap
     */
    public int[][] getBlockMap() {
        return blockMap;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Method description
     *
     *
     * @param row
     * @param column
     *
     * @return
     */
    public int getValue(int row, int column) {
        return this.blockMap[column][row];
    }

    /**
     * Method description
     *
     *
     * @param value
     * @param row
     * @param column
     */
    public void setValue(int value, int row, int column) {
        this.blockMap[column][row] = value;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public static int[][] fudgeMap0() {
        int[][]	map = {		// 0   1   2   3   4   5   6   7   8   9
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 0
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 1
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 2
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 3
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 4
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 5
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 6
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 7
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 8
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            }				// 9
        };

        return transpose(map);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public static int[][] fudgeMap1() {
        int[][]	map = {		// 0   1   2   3   4   5   6   7   8   9
            {
                0, 0, 0, 0, 1, 0, 1, 0, -1, 0
            },				// 0
            {
                0, 0, 1, 0, -1, 0, 0, 0, 0, 0
            },				// 1
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 2
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 3
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 4
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 5
            {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 6
            {
                1, 0, 0, -1, 0, 0, 0, 0, 0, 0
            },				// 7
            {
                -1, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },				// 8
            {
                0, 0, 0, 0, 0, -1, 0, 1, 0, 0
            }				// 9
        };

        return transpose(map);
    }

    /**
     * Method description
     *
     *
     * @param blockData
     *
     * @return
     */
    public static int[][] transpose(int[][] blockData) {
        int		s1      = blockData.length;
        int		s2      = blockData[0].length;
        int[][]	newData = new int[s1][s2];

        for (int m = 1; m < s1; m++)
            if (s2 != blockData[m].length) return null;
        for (int m = 0; m < s1; m++)
            for (int n = 0; n < s2; n++)
                newData[n][m] = blockData[m][n];

        return newData;
    }

    /**
     * Method description
     *
     *
     * @param blockData
     */
    public static void printBlock(int[][] blockData) {

        // System.out.println();
        for (int m = 0; m < blockData.length; m++) {
            printBlock(blockData[m]);

//          System.out.print("\t");
//      
//          for(int c=0; c < blockData.length; c++)
//              System.out.print("\t" + blockData[r][c]);
//          
//          System.out.print("\n");
        }
    }

    /**
     * Method description
     *
     *
     * @param blockData
     */
    public static void printBlock(int[] blockData) {

        // System.out.println();
        for (int m = 0; m < blockData.length; m++)
            System.out.print("\t" + blockData[m]);
        System.out.print("\n");
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingBlockState#copy()
     */

    /**
     * Method description
     *
     *
     * @return
     */
    public BlockState copy() {

        // TODO Auto-generated method stub
        BlockState	stateCopy = new BlockState(this);

        return stateCopy;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingBlockState#equivalent(com.grasppe.conresalpha.steppingLogic.ISteppingBlockState)
     */

    /**
     * Method description
     *
     *
     * @param otherState
     *
     * @return
     */
    public boolean equivalent(ISteppingBlockState otherState) {

        // TODO Auto-generated method stub
        return this.equivalent(otherState);
    }

    /**
     * Method description
     *
     *
     * @param otherState
     *
     * @return
     */
    public boolean equivalent(BlockState otherState) {
        if (this.rows != otherState.rows) return false;
        if (this.columns != otherState.columns) return false;
        if (this.row != otherState.row) return false;
        if (this.column != otherState.column) return false;
        if (this.blockMap != otherState.blockMap) return false;

        return true;
    }
}
