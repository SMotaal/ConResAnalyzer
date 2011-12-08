/*
 * @(#)SteppingStrategy.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

/**
 * @author daflair
 */
public abstract class SteppingStrategy implements ISteppingStrategy {

    /** Field description */
    protected BlockState	startState,	undoState;
    private BlockState		finalState;
    protected BlockState	backState;

    /**
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
     * @return
     */
    public boolean execute() {

        // TODO Auto-generated method stub
        this.startState = this.getFinalState();

        return false;
    }

    /**
     * @param row
     * @param column
     * @return
     */
    protected boolean moveBy(int row, int column) {
        return this.moveTo(this.getFinalState().getRow() + row,
                           this.getFinalState().getColumn() + column);
    }

    /**
     * @param row
     * @param column
     * @return
     */
    protected boolean moveTo(int row, int column) {
        this.undoState = this.getFinalState();
        this.setFinalState(this.getFinalState().copy());
        this.getFinalState().setRow(row);//    = row;
        this.getFinalState().setColumn(column);//    = row;
//        System.out.println("Move to Row " + row + "/" + this.startState.getRows() + " Column " + column + "/" + this.startState.getColumns());

        return this.isValidPosition();
    }

    /**
     * @return
     */
    private boolean notEquivalent() {
        return !this.getFinalState().equivalent(startState);
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingStrategy#undo()
     */

    /**
     * @return
     */
    public boolean undo() {
        // this.finalState = this.startState.copy();
//        System.out.println("Undo step!");
        this.setFinalState(this.undoState);

        return true;
    }

    /**
     * @param valid
     * @return
     */
    protected boolean validMove(boolean valid) {
        if (!valid) this.undo();

        return valid;
    }

    /**
     *  @return
     */
    public BlockState getFinalState() {
        return finalState;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conresalpha.steppingLogic.ISteppingStrategy#isValid()
     */

    /**
     * @return
     */
    public boolean isValid() {
//        System.out.println("Position " + this.isValidPosition() + " Unique " + this.notEquivalent());

        return this.isValidPosition() && this.notEquivalent();
    }

    /**
     * @return
     */
    private boolean isValidPosition() {
        int	finalRow     = this.getFinalState().getRow();
        int	finalColumn  = this.getFinalState().getColumn();
        int	blockRows    = this.startState.getRows();
        int	blockColumns = this.startState.getColumns();

//        System.out.print(">> Row " + finalRow + "/" + blockRows + " Column " + finalColumn + "/"
//                         + blockColumns);

        if ((finalColumn >= 0) && (finalColumn < blockColumns) && (finalRow >= 0)
                && (finalRow < blockRows)) {
//            System.out.print(" is on the grid\n");

            return true;
        }

//        System.out.print(" is off the grid\n");

        return false;
    }

    /**
     *  @param finalState
     */
    public void setFinalState(BlockState finalState) {
        this.finalState = finalState;
    }
}
