/*
 * @(#)BlockGrid.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

import com.grasppe.conres.framework.analysis.stepping.BlockState.PatchDesignation;
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
  public static boolean		blinkValue           = false;
  protected static int[]	voidPatchColor       = { 63, 63, 63 };
  protected static int[]	assumeFailPatchColor = { 128, 0, 0 };
  protected static int[]	failPatchColor       = { 228, 0, 0 };
  protected static int[]	marginalPatchColor   = { 255, 255, 0 };
  protected static int[]	passPatchColor       = { 0, 220, 0 };
  protected static int[]	assumePassPatchColor = { 0, 128, 0 };
  protected static int[]	clearPatchColor      = { 128, 128, 128 };
  protected static int[]	blinkerColor         = { 32, 32, 32 };

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
   * @return the assumeFailPatchColor
   */
  protected static int[] getAssumeFailPatchColor() {
    return assumeFailPatchColor;
  }

  /**
   * @return the assumePassPatchColor
   */
  protected static int[] getAssumePassPatchColor() {
    return assumePassPatchColor;
  }

  /**
   * @return the blinkerColor
   */
  protected static int[] getBlinkerColor() {
    return blinkerColor;
  }

  /**
   * @return the clearPatchColor
   */
  protected static int[] getClearPatchColor() {
    return clearPatchColor;
  }

  /**
   * @return the failPatchColor
   */
  protected static int[] getFailPatchColor() {
    return failPatchColor;
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

    BufferedImage		image  = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    WritableRaster	raster = image.getRaster();
    int[]						color  = new int[3];

    PatchDesignation	value;
    int								column      = blockState.getColumn(),
											row         = blockState.getRow(),
											columns     = blockState.getColumns(),
											rows        = blockState.getRows(),
											firstColumn = blockState.getFirstColumn();

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {

        color = getClearPatchColor();
        value = this.blockState.getPatchValue(r, c);		// m, n);

        if ((value == VOID) || (c < firstColumn)) color = getVoidPatchColor();
        else if (value == ASSUMED_FAIL) color = getAssumeFailPatchColor();
        else if (value == FAIL) color = getFailPatchColor();
        else if (value == MARGINAL) color = getMarginalPatchColor();
        else if (value == PASS) color = getPassPatchColor();
        else if (value == ASSUMED_PASS) color = getAssumePassPatchColor();

        raster.setPixel(c, r, color);
      }
    }

    if (blinkValue) raster.setPixel(column, row, blinkerColor);

    return image;
  }

  /**
   * @return the marginalPatchColor
   */
  protected static int[] getMarginalPatchColor() {
    return marginalPatchColor;
  }

  /**
   * @return the passPatchColor
   */
  protected static int[] getPassPatchColor() {
    return passPatchColor;
  }

  /**
   *   @return the voidPatchColor
   */
  protected static int[] getVoidPatchColor() {
    return voidPatchColor;
  }

  /**
   * @param assumeFailPatchColor the assumeFailPatchColor to set
   */
  public static void setAssumeFailPatchColor(int[] assumeFailPatchColor) {
    BlockGrid.assumeFailPatchColor = assumeFailPatchColor;
  }

  /**
   * @param assumePassPatchColor the assumePassPatchColor to set
   */
  public static void setAssumePassPatchColor(int[] assumePassPatchColor) {
    BlockGrid.assumePassPatchColor = assumePassPatchColor;
  }

  /**
   * @param blinkerColor the blinkerColor to set
   */
  public static void setBlinkerColor(int[] blinkerColor) {
    BlockGrid.blinkerColor = blinkerColor;
  }

  /**
   * @param clearPatchColor the clearPatchColor to set
   */
  public static void setClearPatchColor(int[] clearPatchColor) {
    BlockGrid.clearPatchColor = clearPatchColor;
  }

  /**
   * @param failPatchColor the failPatchColor to set
   */
  public static void setFailPatchColor(int[] failPatchColor) {
    BlockGrid.failPatchColor = failPatchColor;
  }

  /**
   * @param marginalPatchColor the marginalPatchColor to set
   */
  public static void setMarginalPatchColor(int[] marginalPatchColor) {
    BlockGrid.marginalPatchColor = marginalPatchColor;
  }

  /**
   * @param passPatchColor the passPatchColor to set
   */
  public static void setPassPatchColor(int[] passPatchColor) {
    BlockGrid.passPatchColor = passPatchColor;
  }

  /**
   *   @param voidPatchColor the voidPatchColor to set
   */
  public static void setVoidPatchColor(int[] voidPatchColor) {
    BlockGrid.voidPatchColor = voidPatchColor;
  }
}
