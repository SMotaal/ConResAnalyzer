/*
 * @(#)BlockState.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author daflair
 */
public class BlockState implements ISteppingBlockState {

    /**
	 * Enumeration of java.awt.event.KeyEvent event id constants
	 */
	public enum PatchDesignation {
	    PASS(BlockState.PASS), MARGINAL(BlockState.MARGINAL), FAIL(BlockState.FAIL), CLEAR(BlockState.CLEAR),
	    ASSUMED_PASS(BlockState.ASSUMED_PASS), ASSUMED_MARGINAL(BlockState.ASSUMED_MARGINAL), ASSUMED_FAIL(BlockState.ASSUMED_FAIL),
	    CLEARED_GOOD(BlockState.CLEARED_PASS), CLEARED_MARGINAL(BlockState.CLEARED_MARGINAL), CLEARED_FAIL(BlockState.CLEARED_FAIL);
	
	    private static final Map<Integer, PatchDesignation>	lookup = new HashMap<Integer, PatchDesignation>();
	
	    static {
	        for (PatchDesignation s : EnumSet.allOf(PatchDesignation.class))
	            lookup.put(s.value(), s);
	    }
	
	    private int	code;
	
	    /**
	     * Private constructor
	     * @param code   integer or constant variable for the specific enumeration
	     */
	    private PatchDesignation(int code) {
	        this.code = code;
	    }
	
	    /**
	     * @return the integer value for a specific enumeration
	     */
	    public int value() {
	        return code;
	    }
	    
	    public static PatchDesignation designation(int code) {
	    	return get(code).designation();
	    }
	    
	    public PatchDesignation designation() {
	    	switch (lookup.get(code)) {
	    	case MARGINAL :
	    	case ASSUMED_MARGINAL :
	    		return MARGINAL;
	    	case PASS :
	    	case ASSUMED_PASS :
	    		return PASS;
	    	case FAIL :
	    	case ASSUMED_FAIL :
	    		return FAIL;
	    	case CLEAR :
	    	case CLEARED_MARGINAL:
	    	case CLEARED_GOOD :
	    	case CLEARED_FAIL :
			default :
	    		return CLEAR;
	    	}
	    }
	
	    /**
	     * @param code   integer or constant variable for the specific enumeration
	     * @return enumeration object
	     */
	    public static PatchDesignation get(int code) {
	        return lookup.get(code);
	    }
	}

	/** Field description */
    protected int	blockMap[][];

    /** Field description */
    protected int	rows, columns, row,	column,	firstColumn;
    int				dbg = 0;

    public static int JUDGED = 1;
	public static int ASSUMED = 10;
	public static int CLEARED = 100;

	public static int FAIL = -1;
	public static int MARGINAL = 1;
	public static int PASS = 2;

	public static int CLEAR = 0;
	public static int CLEARED_FAIL = FAIL*CLEARED;
	public static int CLEARED_MARGINAL = MARGINAL*CLEARED;
	public static int CLEARED_PASS = PASS*CLEARED;

	public static int ASSUMED_FAIL = FAIL*ASSUMED;
	public static int ASSUMED_MARGINAL = MARGINAL*ASSUMED;
	public static int ASSUMED_PASS = PASS*ASSUMED;
	


    /**
     */
    protected BlockState() {}

    /**
     * @param sourceState
     */
    protected BlockState(ISteppingBlockState sourceState) {
    	if (sourceState!=null) {
	        this.rows        = sourceState.getRows();
	        this.columns     = sourceState.getColumns();
	        this.row         = sourceState.getRow();
	        this.firstColumn = sourceState.getFirstColumn();
	
	        // this.column   = sourceState.getColumn();
	        setColumn(sourceState.getColumn());
	        this.blockMap = sourceState.getBlockMap();
    	}
    }

    /**
     * @param rows
     * @param columns
     *  @param firstColumn
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
     *  @param filename
     * @throws IOException 
     */
    public void readFile(String filename) throws IOException {
        File	file = new File(filename);

        try {
            BufferedReader		bufferedReader = new BufferedReader(new FileReader(file));
            int					fileRows       = 0;
            int					fileColumns    = 0;
            int					firstColumn    = 0;

            ArrayList<String>	lines          = new ArrayList<String>();
            String				line           = "";

            while ((line = bufferedReader.readLine()) != null) {
                int	rowLength = line.trim().split(",").length;

                fileColumns = Math.max(fileColumns, rowLength);
                if (rowLength == fileColumns) lines.add(line);
            }

            fileRows = lines.size();

            Iterator<String>	iterator = lines.iterator();

            int[][]				fileData = new int[fileColumns][fileRows];

            int					row      = 0, column;

            while (iterator.hasNext()) {
            	line = iterator.next();
                String	rowFields[] = line.trim().split(",");

                for (column = 0; column < fileColumns; column++) {
                    int	cellValue = new Integer(rowFields[column]).intValue();

                    if (cellValue == -2) {
                        cellValue   = 0;
                        firstColumn = Math.max(firstColumn, column);
                    }

                    fileData[column][row] = cellValue;
                }

                row++;
            }

            setRows(fileRows);
            setColumns(fileColumns);
            setRow(0);
            setColumn(0);
            setBlockMap(fileData);
        } catch (FileNotFoundException exception) {
        	GrasppeKit.debugError("Reading Analysis Grid File", exception, 5);
        	throw exception;        	
        } catch (IOException exception) {
        	GrasppeKit.debugError("Reading Analysis Grid File", exception, 3);
        	throw exception;
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
     *  @param filename
     */
    public void writeFile(String filename) {

        // Ref: http://www.mkyong.com/java/how-to-export-data-to-csv-file-java/
        try {
            FileWriter	writer = new FileWriter(filename);

            for (int r = 0; r < rows; r++) {
                String[]	rowData   = new String[columns];
                String		rowString = "";

                for (int c = 0; c < columns; c++) {
                    if (c < firstColumn) {
                        rowString = GrasppeKit.cat(rowString, "-2", ",");
                    } else {
                        String	cellString = "" + blockMap[c][r];		// (blockMap[c][r] == 0) ? "" : "" +blockMap[c][r];

                        rowData[c] = "" + cellString;
                        rowString  = GrasppeKit.cat(rowString, cellString, ",");
                    }
                }
                writer.append(rowString + "\n");
            }

            writer.flush();
            writer.close();
        } catch (IOException exception) {
        	GrasppeKit.debugError("Writing Analysis Grid Error", exception, 2);
        }
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

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.framework.analysis.stepping.ISteppingBlockState#getFirstColumn()
     */

    /**
     *  @return
     */
    public int getFirstColumn() {
        return firstColumn;
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
    
    public PatchDesignation getPatchDesignation(int row, int column) {
    	return PatchDesignation.get(getValue(row,column));
    }
    
    public PatchDesignation getDesignation(int row, int column) {
    	return getPatchDesignation(row, column).designation();
    }

    /**
     * @param blockMap the blockMap to set
     */
    protected void setBlockMap(int[][] blockMap) {
        this.blockMap = blockMap;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(int column) {
        if (column < firstColumn) this.column = firstColumn;
        else this.column = column;
    }

    /**
     * @param columns the columns to set
     */
    protected void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @param rows the rows to set
     */
    protected void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @param value
     * @param row
     * @param column
     */
    public void setValue(int value, int row, int column) {
        this.blockMap[column][row] = value;
    }

	public void setValue(PatchDesignation designation, int row, int column) {
		setValue(designation.value(), row, column);
	}
}
