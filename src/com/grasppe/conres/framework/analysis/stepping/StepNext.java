/*
 * @(#)StepNext.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

/**
 * @author daflair
 */
public class StepNext extends SteppingStrategy {

    /** Field description */
    protected int	step = 1;

    /** Field description */
    protected SmartBlockState	smartState;

    /**
     * @param blockState
     */
    public StepNext(BlockState blockState) {
        super(blockState);
        this.smartState = new SmartBlockState(blockState);

        // TODO Auto-generated constructor stub
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.SteppingStrategy#execute()
     */

    /**
     * @return
     */
    @Override
    public boolean execute() {

        // TODO Auto-generated method stub
        int	column         = this.smartState.getColumn();
        int	row            = this.smartState.getRow();
        int	rows           = this.smartState.getRows();
        int	columnBoundary = this.smartState.getColumnBoundary(column);
        int	acceptBoundary = this.smartState.getColumnAcceptBoundary(column);
        int	rejectBoundary = this.smartState.getColumnRejectBoundary(column);

        System.out.println("Accept " + acceptBoundary + " Reject " + rejectBoundary + " Boundary "
                           + columnBoundary);
        BlockState.printBlock(this.smartState.getBlockMap()[column]);

        if (this.smartState.isComplete()) {
            System.out.println("Moving Nowhere! Game Over");

            return true;
        }

        if (columnBoundary > -1) {
            System.out.println("Moving Over");

            return this.moveOver(columnBoundary);
        }

        if (rejectBoundary == 0) {
            System.out.println("Moving Over from Reject Boundary");

            return this.moveOver(columnBoundary);
        }

        if (acceptBoundary == this.smartState.getRows() - 1) {
            System.out.println("Moving Over from Accept Boundary");

            return this.moveOver(columnBoundary);
        }

        if ((rejectBoundary > -1) && (nextRow(rejectBoundary, column) != row)) {	// -this.step!=row && rejectBoundary!=0) {
            System.out.println("Moving Up from Reject Boundary " + row + " > "
                               + nextRow(rejectBoundary, column));

            return this.moveUp(rejectBoundary);
        }

        if ((acceptBoundary > -1) && (nextRow(acceptBoundary, column) != row)) {	// +this.step!=row && acceptBoundary!=rows-1) {
            System.out.println("Moving Down from Accept Boundary " + row + " > "
                               + nextRow(acceptBoundary, column));

            return this.moveDown(acceptBoundary);
        }

        System.out.println("Moving Over Anyway");

        return this.moveOver(-1);
    }

    /**
     * @param boundary
     * @return
     */
    private boolean moveDown(int boundary) {	// after accept
        System.out.println("\tmove down");

        return this.validMove(this.moveTo(boundary + this.step, this.smartState.getColumn()));

        // return this.validMove(this.moveBy(1, 0));
    }

    /**
     * @param boundary
     * @return
     */
    private boolean moveOver(int boundary) {
        System.out.println("\tmove over");

        int	column  = this.smartState.getColumn();
        int	row     = this.smartState.getRow();
        int	columns = this.smartState.getColumns();
        int	rows    = this.smartState.getRows();

        if (this.smartState.isComplete()) return true;
        column = (column + 1) % columns;
        if (boundary > -1) row = nextRow(boundary, column);

        boolean	movedOver = this.validMove(this.moveTo(row, column));

        System.out.println("\tMOVED OVER " + movedOver);

        return movedOver;
    }

    /**
     * @return
     */
    private boolean moveRight() {
        System.out.println("\tmove backwards");

        return this.validMove(this.moveBy(0, -1));
    }

//  private int bound(int value, int min, int max) {
//      return Math.min(    Math.max(value, min), max);
//  }

    /**
     * @param boundary
     * @return
     */
    private boolean moveUp(int boundary) {		// after reject
        System.out.println("\tmove up");

        return this.validMove(this.moveTo(boundary - this.step, this.smartState.getColumn()));
    }

    /**
     * @param row
     * @param column
     * @return
     */
    private int nextRow(int row, int column) {

        /*
         *  TODO
         * * Next over aligns up with 2nd of the good patches, not the accept ones.
         * * Finding rows that are not making sense! Priority to most recent.
         */
        int	columns        = this.smartState.getColumns();
        int	rows           = this.smartState.getRows();
        int	columnBoundary = this.smartState.getColumnBoundary(column);
        int	acceptBoundary = this.smartState.getColumnAcceptBoundary(column);
        int	rejectBoundary = this.smartState.getColumnRejectBoundary(column);

        if (this.smartState.isColumnClear(column)) return Math.max(row - 1, 0);
        if (rejectBoundary > -1) return Math.max(rejectBoundary - this.step, 0);
        if (acceptBoundary > -1) return Math.min(acceptBoundary + this.step, rows - 1);

        return row;
    }
}
