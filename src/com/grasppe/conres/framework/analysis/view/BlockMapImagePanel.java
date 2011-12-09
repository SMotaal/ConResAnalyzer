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
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/08
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class BlockMapImagePanel extends JPanel implements Observer {

    AnalysisStepperModel	model          = null;
    int						imageScale     = 10;
    int						imageWidth     = 200,
							imageHeight    = 200;
    int						blockMapWidth  = 10,
							blockMapHeight = 10;

    /**
     * Create the panel.
     */
    public BlockMapImagePanel() {
    	//setBackground(Color.BLUE);

    	setMinimumSize(new Dimension(imageWidth, imageHeight));
    	setSize(getMinimumSize()); //new Dimension(imageWidth, imageHeight));

        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                update();
            }

        });

//        setLayout(new BorderLayout(0, 0));
    }

    /**
     * Create the panel with a model.
     * 	@param model
     */
    public BlockMapImagePanel(AnalysisStepperModel model) {
        this();
        attachModel(model);        
    }

    /**
     *  @param model
     */
    public void attachModel(AnalysisStepperModel model) {
//        model.attachObserver(this);
        this.model = model;
        update();
    }

    /**
     *  @param g
     */
    public void paint(Graphics g) {
    	super.paint(g);
        try {
            g.drawImage(getBlockMapImage(), 0,0,imageWidth, imageHeight, this);
        } catch (Exception exception) {}
    }

    /**
     */
    public void update() {
        updateSize();
        this.repaint(); //100);
    }

    /**
     */
    public void updateSize() {
        try {
            BufferedImage	blockMapImage  = getModel().getBlockMapImage();
            int				blockMapWidth  = blockMapImage.getWidth(),
							blockMapHeight = blockMapImage.getHeight();
            int				imageScale     = this.imageScale;//(int)Math.floor(getMaxmimumPanelDimension()/getMaxmimumBlockDimension());

            if ((this.imageScale != imageScale) || (this.blockMapWidth != blockMapWidth)
                    || (this.blockMapHeight != blockMapHeight)) {
                this.imageScale     = imageScale;

                this.blockMapWidth  = blockMapWidth;
                this.blockMapHeight = blockMapHeight;

                this.imageWidth     = blockMapWidth * imageScale;
                this.imageHeight    = blockMapHeight * imageScale;
                
                //setMinimumSize(new Dimension(imageWidth, imageHeight));
            }

            setSize(new Dimension(imageWidth, imageHeight));
            setMinimumSize(new Dimension(imageWidth, imageHeight));
            setPreferredSize(new Dimension(imageWidth, imageHeight));

        } catch (NullPointerException exception) {

            // There is no image!
        }
    }

    /**
     * 	@return
     */
    protected BufferedImage getBlockMapImage() {
        try {
            return getModel().getBlockMapImage();
        } catch (NullPointerException exception) {
            return null;
        }
    }

    /**
     * 	@return
     */
    protected int getMaxmimumBlockDimension() {
        return Math.max(getModel().getBlockState().getColumns(),
                        getModel().getBlockState().getRows());
    }

    /**
     * 	@return
     */
    protected int getMaxmimumPanelDimension() {
        return (int)Math.max(Math.max(getSize().getWidth(), getSize().getHeight()),imageWidth);
    }

    /**
     *  @return
     */
    protected AnalysisStepperModel getModel() {
        return model;
    }
}
