/*
 * @(#)BlockMap.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis.stepping;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * @author daflair
 */
public class BlockMap {

    /** Field description */
    BlockState	blockState;

    /**
     * @param blockState
     */
    public BlockMap(BlockState blockState) {
        this.blockState = blockState;
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
        BufferedImage	image  = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster	raster = image.getRaster();
        int[]			data   = new int[3];
        int[][]			colors = {
            { 0, 0, 0 },			// 0
            { 31, 31, 31 },			// 1
            { 63, 63, 63 },			// 2
            { 127, 127, 127 },		// 3
            { 159, 159, 159 },		// 4
            { 195, 195, 195 },		// 5
            { 223, 223, 223 },		// 6
            { 254, 254, 254 },		// 7
        };
        int[]			red    = { 255, 0, 0 };
        int				value;
        int				column  = this.blockState.getColumn();
        int				row     = this.blockState.getRow();
        int				columns = this.blockState.getColumns();
        int				rows    = this.blockState.getRows();

        // System.out.println("Rows "+ rows + " Columns " + columns);
        for (int m = 0; m < rows; m++) {
            for (int n = 0; n < columns; n++) {
                data  = colors[0];
                value = this.blockState.getValue(m,n);//m, n);

                switch (value) {

                case -2 :
                    data = colors[2];

                    break;

                case -1 :
                    data = colors[3];

                    break;

                case 0 :
                    data = colors[4];

                    break;

                case 1 :
                    data = colors[5];

                    break;

                case 2 :
                    data = colors[6];

                    break;

//              default:
//                  if (value<-2)
//                      data = colors[1];
//                  else if (value>2)
//                      data = colors[7];                   
//                  else
//                      data = colors[0];
//                  break;
                }

                // System.out.println("Row "+ n + " Column " + m + " Value " + value);
                raster.setPixel(n, m, (int[])data);
            }
            
//            m = m;
        }

        raster.setPixel(column, row, (int[])red);

        return image;
    }
}
