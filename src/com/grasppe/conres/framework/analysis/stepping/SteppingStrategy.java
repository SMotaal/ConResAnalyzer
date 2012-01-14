/*
 * @(#)SteppingStrategy.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis.stepping;

import com.grasppe.conres.framework.analysis.stepping.BlockState.PatchDesignation;

/**
 * @author daflair
 */
public abstract class SteppingStrategy implements ISteppingStrategy {

    protected static PatchDesignation	ASSUMED_FAIL     = PatchDesignation.ASSUMED_FAIL;
    protected static PatchDesignation	ASSUMED_MARGINAL = PatchDesignation.ASSUMED_MARGINAL;
    protected static PatchDesignation	ASSUMED_PASS     = PatchDesignation.ASSUMED_PASS;
    protected static PatchDesignation	CLEAR            = PatchDesignation.CLEAR;
    protected static PatchDesignation	FAIL             = PatchDesignation.FAIL;
    protected static PatchDesignation	MARGINAL         = PatchDesignation.MARGINAL;
    protected static PatchDesignation	PASS             = PatchDesignation.PASS;

    /** Field description */
    protected BlockState	startState,	undoState;
    protected BlockState	finalState;
    protected int			row     = -1,
							column  = -1;
    protected int			rows    = -1,
							columns = -1;

//  private int               maxRow      = -1;
//  private int               minRow      = -1;
//  private int               maxColumn   = -1;
//  private int               minColumn   = -1;
    protected int	startValue  = -1;
    protected int	finalRow    = -1;
    protected int	finalColumn = -1;

    /**
     * @param blockState
     */
    public SteppingStrategy(BlockState blockState) {
        startState = blockState;
        row        = startState.row;
        column     = startState.column;
        rows       = startState.rows;
        columns    = startState.columns;
        startValue = startState.getValue(row, column);

        setFinalState(startState.clone());
    }

    /**
     *  @param row
     *  @return
     */
    protected boolean checkAbove(int row) {
        PatchDesignation	alternate = PASS;
        PatchDesignation	current   = finalState.getDesignation(row, column);
        PatchDesignation	expecting = current;

        for (int r = row - 1; r >= getMinRow(); r--) {
            current = finalState.getDesignation(r, column);

            if (((current != MARGINAL) || (current != expecting)) && (current != alternate))
                return false;

            expecting = current;
        }

        return true;
    }

    /**
     *  @param row
     *  @return
     */
    protected boolean checkBelow(int row) {
        PatchDesignation	alternate = FAIL;
        PatchDesignation	current   = finalState.getDesignation(row, column);
        PatchDesignation	expecting = current;

        for (int r = row + 1; r <= getMaxRow(); r++) {
            current = finalState.getDesignation(r, column);

            if (((current != MARGINAL) || (current != expecting)) && (current != alternate))
                return false;

            expecting = current;
        }

        return true;
    }

    /**
     * @return  true if successfully changed start state to final state
     */
    public boolean execute() {
        startState = getFinalState();

        return true;
    }

    /**
     * 	@return
     */
    public BlockState executedState() {
        if (execute()) return getFinalState();
        else return startState;
    }

    /**
     *  @param value
     *  @param min
     *  @param max
     *  @return
     */
    protected boolean fallsBetween(int value, int min, int max) {
        return (value > min) && (value < max);
    }

    /**
     *  @param value
     *  @param min
     *  @param max
     *  @return
     */
    protected boolean fallsWithin(int value, int min, int max) {
        return (value >= min) && (value <= max);
    }

    /**
     *  @param blockState
     *  @param startRow
     *  @param endRow
     *  @param column
     *  @param newValue
     *  @return
     */
    public static BlockState fillValues(BlockState blockState, int startRow, int endRow,
            int column, PatchDesignation newValue) {
        fillValues(blockState, startRow, endRow, column, null, newValue);

        return blockState;
    }

    /**
     *  @param blockState
     *  @param startRow
     *  @param endRow
     *  @param column
     *  @param testValue
     *  @param newValue
     *  @return
     */
    public static BlockState fillValues(BlockState blockState, int startRow, int endRow,
            int column, PatchDesignation testValue, PatchDesignation newValue) {
        if (startRow < endRow) {
            for (int r = startRow; r <= endRow; r++) {
                if ((testValue == null) || (blockState.getDesignation(r, column) != testValue))
                    blockState.setValue(newValue, r, column);
            }
        } else if (startRow > endRow) {
            for (int r = startRow; r >= endRow; r--) {
                if ((testValue == null) || (blockState.getDesignation(r, column) != testValue))
                    blockState.setValue(newValue, r, column);
            }
        } else if (startRow == endRow) {
            if ((testValue == null) || (blockState.getDesignation(endRow, column) != testValue))
                blockState.setValue(newValue, endRow, column);        	
        }

        return blockState;
    }

    /**
     *  @param blockState
     *  @param row
     *  @param column
     *  @param designation
     *  @return last row below row with designation or -1 if not found
     */
    public static int findNextIs(BlockState blockState, int row, int column,
                                 PatchDesignation designation) {
        int	maxRow = blockState.rows - 1;

        while ((row <= maxRow) && (blockState.getDesignation(row, column) != designation))
            row++;

        return (row > maxRow) ? -1
                              : row;
    }

    /**
     *  @param blockState
     *  @param row
     *  @param column
     *  @param designation
     *  @return last row below row with designation or -1 if not found
     */
    public static int findNextNot(BlockState blockState, int row, int column,
                                  PatchDesignation designation) {
        int	maxRow = blockState.rows - 1;

        while ((row <= maxRow) && (blockState.getDesignation(row, column) == designation))
            row++;

        return (row > maxRow) ? -1 
                              : row;
    }

    /**
     *  @param blockState
     *  @param row
     *  @param column
     *  @param designation
     *  @return first row above row with designation or -1 if not found
     */
    public static int findPastIs(BlockState blockState, int row, int column,
                                 PatchDesignation designation) {
        int	minRow = 0;

        while ((row > minRow) && (blockState.getDesignation(row, column) != designation))
            row--;

        return row;
    }

    /**
     *  @param blockState
     *  @param row
     *  @param column
     *  @param designation
     *  @return first row above row with designation or -1 if not found
     */
    public static int findPastNot(BlockState blockState, int row, int column,
                                  PatchDesignation designation) {
        int	minRow = 0;

        while ((row > minRow) && (blockState.getDesignation(row, column) == designation))
            row--;

        return row;

    }

    /**
     *  @param blockState
     *  @param row
     *  @param column
     *  @param designation
     *  @return first row above row with designation or -1 if not found
     */
    public static int findSecondLastIs(BlockState blockState, int row, int column,
                                       PatchDesignation designation) {
        int	firstRow = findPastIs(blockState, row, column, designation);

        return (firstRow == -1) ? -1
                                : findPastIs(blockState, firstRow, column, designation);
    }

    /**
     *  @param blockState
     *  @param row
     *  @param column
     *  @param designation
     *  @return first row above row with designation or -1 if not found
     */
    public static int findSecondNextIs(BlockState blockState, int row, int column,
                                       PatchDesignation designation) {
        int	firstRow = findNextIs(blockState, row, column, designation);

        return (firstRow == -1) ? -1
                                : findNextIs(blockState, firstRow, column, designation);
    }

    /**
     * @param rowSteps
     * @param columnSteps
     * @return
     */
    protected boolean moveBy(int rowSteps, int columnSteps) {
        return moveTo(getFinalState().getRow() + rowSteps,
                      getFinalState().getColumn() + columnSteps);
    }

    /**
     * @return
     */
    protected boolean moveDown() {
        return validMove(moveBy(1, 0));
    }

    /**
     * @return true if could moved over one above last pass regardless of validity above and below
     */
    protected boolean moveOver() {		// after accept
        if (column == getMaxColumn()) return false;

        // Otherwise find Good boundary and move one above
//      int firstNotPass = findNextNot(finalState, getMinRow(), column, PASS);
        int	lastPass = findPastIs(finalState, getMaxRow(), column, PASS);

//      int   lastMarginal = findPastIs(finalState, getMaxRow(), column, MARGINAL);
        int	firstMarginal = findNextIs(finalState, getMinRow(), column, MARGINAL);

        if (lastPass > 0) return moveTo(lastPass - 1, column + 1);		// else if (lastIsPass == 0) return moveTo(lastIsPass, column + 1);

        if ((lastPass < 0) && (firstMarginal > 0)) return moveTo(firstMarginal - 1, column + 1);

        if (row > 0) return moveTo(row - 1, column + 1);
        else return moveTo(row, column + 1);

    }

    /**
     * @param newRow
     * @param newColumn
     * @return
     */
    protected boolean moveTo(int newRow, int newColumn) {
        undoState = finalState;

        setFinalState(finalState.clone());

        finalState.setRow(newRow);
        finalRow = newRow;

        finalState.setColumn(newColumn);
        finalColumn = newColumn;

        boolean	moved = isValidPosition();

        return moved;
    }

    /**
     * @return
     */
    protected boolean moveUp() {	// after accept
        return validMove(moveBy(-1, 0));
    }

    /**
     * @return
     */
    private boolean notEquivalent() {
        return !getFinalState().equivalent(startState);
    }

    /**
     * @return
     */
    public boolean undo() {
        setFinalState(undoState);

        return true;
    }

    /**
     * @param valid
     * @return
     */
    protected boolean validMove(boolean valid) {
        if (!valid) undo();

        return valid;
    }

    /**
     *  @return
     */
    public BlockState getFinalState() {
        return finalState;
    }

    /**
     *  @return
     */
    protected int getMaxColumn() {
        return columns - 1;
    }

    /**
     *  @return
     */
    protected int getMaxRow() {
        return rows - 1;
    }

    /**
     *  @return
     */
    protected int getMinColumn() {
        return 0;
    }

    /**
     *  @return
     */
    protected int getMinRow() {
        return 0;
    }

    /**
     * @return
     */
    public boolean isValid() {
        return isValidPosition() && notEquivalent();
    }

    /**
     * @return
     */
    private boolean isValidPosition() {
        boolean	validColumn = fallsWithin(finalColumn, getMinColumn(), getMaxColumn());
        boolean	validRow    = fallsWithin(finalRow, getMinRow(), getMaxRow());

        return validColumn && validRow;
    }

    /**
     *  @param finalState
     */
    public void setFinalState(BlockState finalState) {
        this.finalState = finalState;
    }

//  /**
//   *    @param maxColumn
//   */
//  protected void setMaxColumn(int maxColumn) {
//      this.maxColumn = maxColumn;
//  }
//
//  /**
//   *    @param maxRow
//   */
//  protected void setMaxRow(int maxRow) {
//      this.maxRow = maxRow;
//  }

//  /**
//   *    @param minColumn
//   */
//  protected void setMinColumn(int minColumn) {
//      this.minColumn = minColumn;
//  }
//
//  /**
//   *    @param minRow
//   */
//  protected void setMinRow(int minRow) {
//      this.minRow = minRow;
//  }
}
