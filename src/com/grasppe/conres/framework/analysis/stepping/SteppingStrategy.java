/*
 * @(#)SteppingStrategy.java   11/08/25
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
public abstract class SteppingStrategy implements ISteppingStrategy {

    /** Field description */
    protected BlockState	startState,	undoState;
	private BlockState finalState;
	protected BlockState backState;

    /**
     *
     *
     * @param blockState
     */
    public SteppingStrategy(BlockState blockState) {

        // TODO Auto-generated constructor stub
        this.startState = blockState;
        this.backState  = blockState.copy();
        this.setFinalState(this.startState.copy());
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingStrategy#execute()
     */

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean execute() {

        // TODO Auto-generated method stub
        this.startState = this.getFinalState();

        return false;
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
    protected boolean moveBy(int row, int column) {
        return this.moveTo(this.getFinalState().getRow() + row, this.getFinalState().getColumn() + column);
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
    protected boolean moveTo(int row, int column) {
        this.undoState         = this.getFinalState();
        this.setFinalState(this.getFinalState().copy());
        this.getFinalState().row    = row;
        this.getFinalState().column = column;
        System.out.println("Move to Row " + row + "/" + this.startState.getRows() + " Column " + column + "/"
                           + this.startState.getColumns());

        return this.isValidPosition();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingStrategy#isValid()
     */

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isValid() {

        // TODO Auto-generated method stub
        System.out.println("Position " + this.isValidPosition() + " Unique " + this.notEquivalent());

        return this.isValidPosition() && this.notEquivalent();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    private boolean notEquivalent() {
        return !this.getFinalState().equivalent(startState);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    private boolean isValidPosition() {
        int	finalRow     = this.getFinalState().getRow();
        int	finalColumn  = this.getFinalState().getColumn();
        int	blockRows    = this.startState.getRows();
        int	blockColumns = this.startState.getColumns();

        System.out.print(">> Row " + finalRow + "/" + blockRows + " Column " + finalColumn + "/" + blockColumns);

        if ((finalColumn >= 0) && (finalColumn < blockColumns) && (finalRow >= 0) && (finalRow < blockRows)) {
            System.out.print(" is on the grid\n");

            return true;
        }

        System.out.print(" is off the grid\n");

        return false;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingStrategy#undo()
     */

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean undo() {

        // TODO Auto-generated method stub
        // this.finalState = this.startState.copy();
        System.out.println("Undo step!");
        this.setFinalState(this.undoState);

        return true;
    }

    /**
     * Method description
     *
     *
     * @param valid
     *
     * @return
     */
    protected boolean validMove(boolean valid) {
        if (!valid) this.undo();

        return valid;
    }

	public BlockState getFinalState() {
		return finalState;
	}

	public void setFinalState(BlockState finalState) {
		this.finalState = finalState;
	}
}
