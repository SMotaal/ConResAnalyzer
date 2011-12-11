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

//~--- JDK imports ------------------------------------------------------------

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
public class BlockMapImagePanel extends PatchBoundView {	// JPanel implements Observer {

    AnalysisStepperModel	model          = null;
    int						defaultScale     = 5;
    int						defaultWidth     = 100,
							defaultHeight    = 100;
    int						imageScale     = 10;
    int						imageWidth     = 100,
							imageHeight    = 100;
    int						blockMapWidth  = 10,
							blockMapHeight = 10;

    /**
     * Create the panel with a model.
     *  @param model
     */
    public BlockMapImagePanel(AnalysisStepperModel model) {
        super(model);
        prepareView();
    }

    /**
     *  @param g
     */
    public void paint(Graphics g) {
        super.paint(g);

        try {
            g.drawImage(getBlockMapImage(), 0, 0, imageWidth, imageHeight, this);
        } catch (Exception exception) {}
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
     */
    public void updateSize() {
        try {
            BufferedImage	blockMapImage  = getModel().getBlockMapImage();
            int				blockMapWidth  = blockMapImage.getWidth(),
							blockMapHeight = blockMapImage.getHeight();
            int				imageScale     = this.imageScale;		// (int)Math.floor(getMaxmimumPanelDimension()/getMaxmimumBlockDimension());

            if ((this.imageScale != imageScale) || (this.blockMapWidth != blockMapWidth)
                    || (this.blockMapHeight != blockMapHeight)) {
            	
            	if (imageScale<defaultScale) imageScale = defaultScale;
                this.imageScale     = imageScale;

                this.blockMapWidth  = blockMapWidth;
                this.blockMapHeight = blockMapHeight;

                this.imageWidth     = blockMapWidth * imageScale;
                this.imageHeight    = blockMapHeight * imageScale;
                

            }
            if (this.imageWidth < defaultWidth) this.imageWidth = defaultWidth;
            if (this.imageHeight < defaultHeight) this.imageHeight = defaultHeight;

            setPreferredSize(new Dimension(imageWidth, imageHeight));
//            setSize(new Dimension(imageWidth, imageHeight));
//            setMinimumSize(new Dimension(imageWidth, imageHeight));

        } catch (NullPointerException exception) {

            // There is no image!
        }
    }

    /**
     *  @return
     */
    protected BufferedImage getBlockMapImage() {
        try {
            return getModel().getBlockMapImage();
        } catch (NullPointerException exception) {
            return null;
        }
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
}
