/*
 * @(#)SmartBlockState.java   11/08/25
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
public class SmartBlockState extends BlockState {

    /**
     *
     *
     * @param sourceState
     */
    protected SmartBlockState(ISteppingBlockState sourceState) {
        super(sourceState);

//      
//      this.rows = sourceState.rows;
//      this.columns = sourceState.columns;
//      this.row = sourceState.row;
//      this.column = sourceState.column;
//      this.blockMap=sourceState.blockMap;
//      //this.blockMap = sourceState.blockMap;
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
    protected SmartBlockState(int rows, int columns, int row, int column, int blockMap[][]) {
        super(rows, columns, row, column, blockMap);
    }

    /**
     * (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingBlockState#copy()
     *
     * @return
     */
    @Override
    public SmartBlockState copy() {

        // TODO Auto-generated method stub
        SmartBlockState	stateCopy = new SmartBlockState(this);

        return stateCopy;
    }

    /**
     * (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingBlockState#isComplete()
     *
     * @return
     */
    public boolean isComplete() {

        // TODO Check blockMap and determine if each column has an accept/reject boundry
        // System.out.println("Complete " + checkBlockBoundries());
        return checkBlockBoundries();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean checkBlockBoundries() {

        // TODO
        // boolean boundaryMissing = false;
//      for (column = 0; column > this.columns; column++) {
//          if (getColumnBoundary(column) == -1) {
//              //boundaryMissing = true;
//              return false;
//          }
//      }
        return this.firstMissingBoundary() == -1;		// !boundaryMissing;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public int firstMissingBoundary() {
        return firstMissingBoundary(0);
    }

    /**
     * Method description
     *
     *
     * @param startColumn
     *
     * @return
     */
    public int firstMissingBoundary(int startColumn) {

        // for (int column = 0; column < this.columns; column++)
        int	column = startColumn + 1;

        do {
            if (getColumnBoundary(column) == -1) {

                // System.out.println(">> Missing Boundary " + column);
                return column;
            }

            column += 1;
            if (column > this.getColumns()) column = 0;
        } while (column != startColumn);

        return -1;
    }

    /**
     * Method description
     *
     *
     * @param column
     *
     * @return
     */
    public int getColumnBoundary(int column) {

        // TODO Look for the boundary row of a column at the first occurrence accept patch before a reject patch
        int	acceptBoundary = this.getColumnAcceptBoundary(column);
        int	rejectBoundary = this.getColumnRejectBoundary(column);

        // printBlock(this.blockMap[column]);
//      System.out.println(">> Column" + column + " Accept " + acceptBoundary + " Reject " + rejectBoundary);
        if ((acceptBoundary <= this.getRows()) && (acceptBoundary + 1 == rejectBoundary)) return acceptBoundary;

        return -1;
    }

    /**
     * Method description
     *
     *
     * @param column
     *
     * @return
     */
    public boolean isColumnClear(int column) {
        int	acceptBoundary = this.getColumnAcceptBoundary(column);
        int	rejectBoundary = this.getColumnRejectBoundary(column);

        return (acceptBoundary == -1) && (acceptBoundary == rejectBoundary);
    }

    /**
     * Method description
     *
     *
     * @param column
     *
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
     * Method description
     *
     *
     * @param column
     *
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
}
