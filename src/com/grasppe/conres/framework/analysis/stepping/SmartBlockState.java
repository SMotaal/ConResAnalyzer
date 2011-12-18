/*
 * @(#)SmartBlockState.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

/**
 * @author daflair
 */
public class SmartBlockState extends BlockState implements Cloneable {

    /**
     * @param sourceState
     */
    public SmartBlockState(ISteppingBlockState sourceState) {
        super(sourceState);
    }

    /**
     * @param rows	number of rows
     * @param columns	number of columns
     * @param row	current row
     * @param column	current column 
     * @param blockMap	current block values
     */
    protected SmartBlockState(int rows, int columns, int row, int column, int blockMap[][]) {
        super(rows, columns, row, column, blockMap);
    }

    /**
     * @return
     */
    public boolean checkBlockBoundries() {
        return firstMissingBoundary() == -1;		// !boundaryMissing;
    }

    /**
     * (non-Javadoc)
     * @return copy this state
     */
    @Override
    public SmartBlockState clone() {
        return new SmartBlockState(this);
    }

    /**
     * @return
     */
    public int firstMissingBoundary() {
        return firstMissingBoundary(0);
    }

    /**
     * @param startColumn
     * @return
     */
    public int firstMissingBoundary(int startColumn) {

        int	column = startColumn + 1;

        do {
            if (getColumnBoundary(column) == -1) {
                return column;
            }

            column += 1;
            if (column > this.getColumns()) column = 0;
        } while (column != startColumn);

        return -1;
    }

    /**
     * @param column
     * @return
     */
    public int getColumnAcceptBoundary(int column) {
        int	thisRow, nextRow;
        int	rows = this.getRows();

        for (int row = 0; row < rows - 1; row++) {
            thisRow = this.blockMap[column][row];
            if (row == rows - 1) nextRow = 0;
            else nextRow = this.blockMap[column][row + 1];
            if ((thisRow > 0) && (nextRow <= 0)) return row;
        }

        return -1;
    }
    
    /**
     * @param column
     * @return
     */
    public int getColumnGoodBoundary(int column) {
        int	thisRow, nextRow;
        int	rows = this.getRows();

        for (int row = 0; row < rows - 1; row++) {
            thisRow = this.blockMap[column][row];
            if (row == rows - 1) nextRow = 0;
            else nextRow = this.blockMap[column][row + 1];
            if ((thisRow ==2) && (nextRow!= 2)) return row;
        }

        return -1;
    }    

    /**
     * @param column
     * @return
     */
    public int getColumnBoundary(int column) {

        // TODO Look for the boundary row of a column at the first occurrence accept patch before a reject patch
        int	acceptBoundary = this.getColumnAcceptBoundary(column);
        int	rejectBoundary = this.getColumnRejectBoundary(column);

        
        if ((acceptBoundary <= this.getRows()) && (acceptBoundary + 1 == rejectBoundary))
            return acceptBoundary;

        return -1;
    }

    /**
     * @param column
     * @return
     */
    public int getColumnRejectBoundary(int column) {
        int	thisRow, nextRow;
        int	rows = this.getRows();

        for (int row = rows - 1; row >= 0; row--) {
            thisRow = this.blockMap[column][row];
            if (row == 0) nextRow = 0;
            else nextRow = this.blockMap[column][row - 1];
            if ((thisRow < 0) && (nextRow >= 0)) return row;
        }

        return -1;
    }

    /**
     * @param column
     * @return
     */
    public boolean isColumnClear(int column) {
        int	acceptBoundary = this.getColumnAcceptBoundary(column);
        int	rejectBoundary = this.getColumnRejectBoundary(column);

        return (acceptBoundary == -1) && (acceptBoundary == rejectBoundary);
    }

    /**
     * (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingBlockState#isComplete()
     * @return
     */
    public boolean isComplete() {
        // Check blockMap and determine if each column has an accept/reject boundry
        return checkBlockBoundries();
    }
}
