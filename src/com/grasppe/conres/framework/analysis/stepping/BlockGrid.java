/*
 * @(#)BlockGrid.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

import com.grasppe.conres.framework.analysis.stepping.BlockState.PatchDesignation;
import com.grasppe.conres.preferences.Preferences;
import com.grasppe.conres.preferences.Preferences.Tags;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author daflair
 */
public class BlockGrid {

  protected static PatchDesignation	ASSUMED_FAIL     = PatchDesignation.ASSUMED_FAIL;
  protected static PatchDesignation	ASSUMED_MARGINAL = PatchDesignation.ASSUMED_MARGINAL;
  protected static PatchDesignation	ASSUMED_PASS     = PatchDesignation.ASSUMED_PASS;
  protected static PatchDesignation	CLEAR            = PatchDesignation.CLEAR;
  protected static PatchDesignation	FAIL             = PatchDesignation.FAIL;
  protected static PatchDesignation	MARGINAL         = PatchDesignation.MARGINAL;
  protected static PatchDesignation	PASS             = PatchDesignation.PASS;
  protected static PatchDesignation	VOID             = PatchDesignation.VOID;

  /** Field description */
  public static boolean	blinkValue = false;

  /** Field description */
  BlockState	blockState;

  /**
   * @param blockState
   */
  public BlockGrid(BlockState blockState) {
    this.blockState = blockState;
  }

  /**
   *  @param filename
   */
  public void writeFile(String filename) {

    // Ref: http://www.mkyong.com/java/how-to-export-data-to-csv-file-java/
    try {
      FileWriter	writer = new FileWriter(filename);

      for (int r = 0; r < blockState.rows; r++) {
        String[]	rowData = new String[blockState.columns];

        for (int c = 0; c < blockState.columns; c++) {
          rowData[c] = "" + blockState.blockMap[c][r];
        }

        writer.append(GrasppeKit.cat(rowData, ","));
      }

      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @return
   */
  public BufferedImage getImage() {
    int	columns = this.blockState.getColumns();
    int	rows    = this.blockState.getRows();

    return this.getImage(columns, rows);
  }

  /**
   * @param width
   * @param height
   * @return
   */
  public BufferedImage getImage(int width, int height) {

    BufferedImage			image  = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    WritableRaster		raster = image.getRaster();
    int[]							color  = new int[3];
    int[][]						colors = {
    	      { 63, 63, 63 },         // 0    VOID
    	      { 128, 0, 0 },          // 1    ASSUMED_FAIL
    	      { 228, 0, 0 },          // 2    FAIL
    	      { 255, 255, 0 },        // 3    MARGINAL
    	      { 0, 220, 0 },          // 4    PASS
    	      { 0, 128, 0 },          // 5    ASSUMED_PASS
    	      { 128, 128, 128 },     // 6    CLEAR
    	      { 32,32, 32 },            // 7    CURSOR    		
//      (int[])Preferences.get(Tags.VOID_COLOR),						// { 63, 63, 63 },         // 0    VOID
//      (int[])Preferences.get(Tags.ASSUMED_FAIL_COLOR),		// { 128, 0, 0 },          // 1    ASSUMED_FAIL
//      (int[])Preferences.get(Tags.FAIL_COLOR),						// { 228, 0, 0 },          // 2    FAIL
//      (int[])Preferences.get(Tags.MARGINAL_COLOR),				// { 255, 255, 0 },        // 3    MARGINAL
//      (int[])Preferences.get(Tags.PASS_COLOR),						// { 0, 220, 0 },          // 4    PASS
//      (int[])Preferences.get(Tags.ASSUMED_PASS_COLOR),		// { 0, 128, 0 },          // 5    ASSUMED_PASS
//      (int[])Preferences.get(Tags.CLEAR_COLOR),						// { 128, 128, 128 },     // 6    CLEAR
//      (int[])Preferences.get(Tags.CURSOR_COLOR),					// { 32,32, 32 },            // 7    CURSOR
    };
    PatchDesignation	value;
    int								column      = blockState.getColumn(),
											row         = blockState.getRow(),
											columns     = blockState.getColumns(),
											rows        = blockState.getRows(),
											firstColumn = blockState.getFirstColumn();

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {

        color = colors[6];
        value = this.blockState.getPatchValue(r, c);		// m, n);

        if ((value == VOID) || (c < firstColumn)) color = colors[0];
        else if (value == ASSUMED_FAIL) color = colors[1];
        else if (value == FAIL) color = colors[2];
        else if (value == MARGINAL) color = colors[3];
        else if (value == PASS) color = colors[4];
        else if (value == ASSUMED_PASS) color = colors[5];

        raster.setPixel(c, r, (int[])color);
      }
    }

    if (blinkValue) raster.setPixel(column, row, (int[])colors[7]);

    return image;
  }
}
