/*
 * @(#)PatchImagePanel.java   11/12/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.analysis.view;

import com.grasppe.conres.framework.analysis.model.AnalysisStepperModel;
import com.grasppe.conres.framework.analysis.stepping.BlockGrid;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/08
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class BlockMapImagePanel extends PatchBoundView implements javax.swing.SwingConstants {		// JPanel implements Observer {

    AnalysisStepperModel	model               = null;
    int						minimumScale        = 5;
    int						minimumWidth        = 100,
							minimumHeight       = 100;
    int						imageScale          = 10;
    int						imageWidth          = 100,
							imageHeight         = 100;
    int						imageLeft           = 0,
							imageTop            = 0;
    int						blockMapWidth       = 10,
							blockMapHeight      = 10;
    int						verticalAlignment   = CENTER,
							horizontalAlignment = CENTER;
    boolean					autoFit             = true;
    boolean					canPaint            = false;

    /**
     * Create the panel with a model.
     *  @param model
     */
    public BlockMapImagePanel(AnalysisStepperModel model) {
        super(model);
        autoFit = true;
        prepareView();
    }

    /**
     *  @param g
     */
    public void paint(Graphics g) {
        super.paint(g);

        try {
            g.setColor(Color.BLUE);		// getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        } catch (Exception exception) {
            GrasppeKit.debugError("Painting Block Map", exception, 2);
        }

        try {
            if (canPaint)
                g.drawImage(getBlockMapImage(), imageLeft, imageTop, imageWidth, imageHeight, this);
        } catch (Exception exception) {
            GrasppeKit.debugError("Painting Block Map", exception, 2);
        }
    }

    /**
     */
    public void prepareView() {
        setMinimumSize(new Dimension(imageWidth, imageHeight));
        setSize(getMinimumSize());		// new Dimension(imageWidth, imageHeight));

        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                update();
            }

        });

    }

    /**
     *  @param srcLength
     *  @param dstLength
     *  @return ceil (dstLength / srcLength)
     */
    protected int scaleValue(int srcLength, int dstLength) {
        if (srcLength == 0) return 0;

        return (int)Math.ceil((double)dstLength / (double)srcLength);
    }

    /**
     *  @param srcWidth
     *  @param srcHeight
     *  @param dstWidth
     *  @param dstHeight
     *  @return min ( ceil (dstWidth / srcWidth) , ceil (dstHeight / srcHeight)
     */
    protected int scaleValue(int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
        return (int)Math.min(scaleValue(srcWidth, dstWidth), scaleValue(srcHeight, dstHeight));
    }

    /**
     */
    public void updateSize() {
        try {
            int	blockMapWidth  = 0,
				blockMapHeight = 0;

            if ((getModel() != null) && (getModel().getBlockMapImage() != null)) {
                blockMapWidth  = getModel().getBlockMapImage().getWidth();
                blockMapHeight = getModel().getBlockMapImage().getHeight();
            }

            this.blockMapWidth  = blockMapWidth;
            this.blockMapHeight = blockMapHeight;

//            int	viewWidth  = getWidth(),
//				viewHeight = getHeight();

            int	newScale   = scaleValue(blockMapWidth, blockMapHeight, getWidth(), getHeight());

            if (newScale == 0) return;

            if (isAutoFit()) {
                newScale    = Math.max(minimumScale, newScale);
                imageWidth  = Math.max(blockMapWidth * newScale, minimumWidth);
                imageHeight = Math.max(blockMapHeight * newScale, minimumWidth);

                int	imageLength = Math.min(imageWidth, imageHeight);

                setMaximumSize(new Dimension(imageLength, imageLength));
                setPreferredSize(new Dimension(imageLength, imageLength));
                setMinimumSize(new Dimension(imageLength, imageLength));
            } else {
                if (imageWidth < minimumWidth) imageWidth = minimumWidth;
                if (imageHeight < minimumHeight) imageHeight = minimumHeight;
                setPreferredSize(new Dimension(imageWidth, imageHeight));
                setMinimumSize(new Dimension(imageWidth, imageHeight));
                newScale = Math.max(minimumScale,
                                    scaleValue(blockMapWidth, imageWidth, blockMapHeight,
                                               imageHeight));
            }

            imageScale = newScale;

            if (horizontalAlignment == LEFT) imageLeft = 0;
            else if (horizontalAlignment == CENTER)
                     imageLeft = (int)Math.round((getWidth() - imageWidth) / 2.0);
            else if (horizontalAlignment == RIGHT) imageLeft = getWidth() - imageWidth;

            if (verticalAlignment == TOP) imageTop = 0;
            else if (verticalAlignment == CENTER)
                     imageTop = (int)Math.round((getHeight() - imageHeight) / 2.0);
            else if (verticalAlignment == BOTTOM) imageTop = getHeight() - imageHeight;

            canPaint = true;

        } catch (NullPointerException exception) {
            GrasppeKit.debugError("Updating Block Map Size", exception, 2);
            canPaint = false;
        }
    }

    /**
     *  @return new BlockGrid(getModel().getBlockState()).getImage();
     */
    protected BufferedImage getBlockMapImage() {
        try {
            return new BlockGrid(getModel().getBlockState()).getImage();

        } catch (NullPointerException exception) {
            return null;
        }
    }

    /**
     * @return the horizontalAlignment
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * @return the imageHeight
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * @return the imageWidth
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     *  @return
     */
    protected int getMaxmimumBlockDimension() {
        return Math.max(getModel().getBlockState().getColumns(),
                        getModel().getBlockState().getRows());
    }

    /**
     *  @return
     */
    protected int getMaxmimumPanelDimension() {
        return (int)Math.max(Math.max(getSize().getWidth(), getSize().getHeight()), imageWidth);
    }

    /**
     * @return the verticalAlignment
     */
    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * @return the autoFit
     */
    public boolean isAutoFit() {
        return autoFit;
    }

    /**
     * @param autoFit the autoFit to set
     */
    public void setAutoFit(boolean autoFit) {
        if (this.autoFit != autoFit) {
            this.autoFit = autoFit;
            updateSize();
        } else
            this.autoFit = autoFit;
    }

    /**
     * @param horizontalAlignment the horizontalAlignment to set
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    /**
     * @param imageHeight the imageHeight to set
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * @param imageWidth the imageWidth to set
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * @param verticalAlignment the verticalAlignment to set
     */
    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }
}
