/*
 * @(#)BlockState.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

/**
 * @author daflair
 */
public class BlockState implements ISteppingBlockState {

    /* (non-Javadoc)
	 * @see com.grasppe.conres.framework.analysis.stepping.ISteppingBlockState#getFirstColumn()
	 */
	public int getFirstColumn() {
		return firstColumn;
	}

	/** Field description */
    protected int	blockMap[][];

    /** Field description */
    protected int	rows, columns, row,	column,	firstColumn;
    int				dbg = 2;

    /**
     */
    protected BlockState() {}

    /**
     * @param sourceState
     */
    protected BlockState(ISteppingBlockState sourceState) {
        this.rows    = sourceState.getRows();
        this.columns = sourceState.getColumns();
        this.row     = sourceState.getRow();
        this.firstColumn = sourceState.getFirstColumn();
        // this.column   = sourceState.getColumn();
        setColumn(sourceState.getColumn());
        this.blockMap = sourceState.getBlockMap();
    }

    /**
     * @param rows
     * @param columns
     * 	@param firstColumn
     */
    public BlockState(int rows, int columns, int firstColumn) {
        this(rows, columns, 0, firstColumn);
        this.firstColumn = firstColumn;
    }

    /**
     * @param rows
     * @param columns
     * @param row
     * @param column
     */
    public BlockState(int rows, int columns, int row, int column) {
        this.rows    = rows;
        this.columns = columns;
        this.row     = row;

        // this.column   = column;
        setColumn(column);
        this.blockMap = new int[columns][rows];
    }

    /**
     * @param rows
     * @param columns
     * @param row
     * @param column
     * @param blockMap
     */
    public BlockState(int rows, int columns, int row, int column, int blockMap[][]) {
        this.rows    = rows;
        this.columns = columns;
        this.row     = row;

        // this.column   = column;
        setColumn(column);
        this.blockMap = blockMap;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingBlockState#copy()
     */

    /**
     * @return
     */
    public BlockState copy() {

        // TODO Auto-generated method stub
        BlockState	stateCopy = new BlockState(this);

        return stateCopy;
    }

    /**
     * @param otherState
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

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingBlockState#equivalent(com.grasppe.conresalpha.steppingLogic.ISteppingBlockState)
     */

    /**
     * @param otherState
     * @return
     */
    public boolean equivalent(ISteppingBlockState otherState) {

        // TODO Auto-generated method stub
        return this.equivalent(otherState);
    }

    /**
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
     * @param blockData
     */
    public static void printBlock(int[] blockData) {

        // System.out.println();
        for (int m = 0; m < blockData.length; m++)
            System.out.print("\t" + blockData[m]);
        System.out.print("\n");
    }

    /**
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
     * @param blockData
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
     * @return the blockMap
     */
    public int[][] getBlockMap() {
        return blockMap;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return the columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param row
     * @param column
     * @return
     */
    public int getValue(int row, int column) {

//      GrasppeKit.debugText("BlockState", "Get Value: " + row + ", " + column + " [" + blockMap[0].length + "x" + blockMap.length + "]", dbg);
        return this.blockMap[column][row];
    }

    /**
     * @param column the column to set
     */
    public void setColumn(int column) {
        if (column < firstColumn) this.column = firstColumn;
        else this.column = column;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @param value
     * @param row
     * @param column
     */
    public void setValue(int value, int row, int column) {
        this.blockMap[column][row] = value;
    }
}
