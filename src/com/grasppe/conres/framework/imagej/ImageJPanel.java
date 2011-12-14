/*
 * @(#)ImageJPanel.java   11/12/11
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.imagej;

import com.grasppe.lure.framework.GrasppeKit;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;

import ij.gui.ImageCanvas;

import ij.io.Opener;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/*
* Implementation of basic IJ ImageWindow functionality
 */

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/11
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ImageJPanel extends JPanel implements ComponentListener {

    /** Field description */
    public static final int	MINIMUM_WIDTH = 128;

    /** Field description */
    public static final int	MINIMUM_HEIGHT = 128;
    int						dbg            = 0;
    private int				imageWidth     = 0,
							imageHeight    = 0;
    protected ImageCanvas	imageCanvas;
    protected ImagePlus		imagePlus;
    protected ImageJ		imageJ;
    private boolean			fitImage = true;

    /**
     * @param imagePlus
     */
    public ImageJPanel(ImagePlus imagePlus) {
        super(); //new BorderLayout());

//      setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        imageJ         = IJ.getInstance();

        this.imagePlus = imagePlus;

        imageCanvas    = new ImageCanvas(imagePlus);

        imageWidth     = imagePlus.getWidth();
        imageHeight    = imagePlus.getHeight();

        setFocusTraversalKeysEnabled(false);

        add(imageCanvas); //, BorderLayout.CENTER);

        addComponentListener(this);
        
//      setBackground(Color.WHITE);
//        imageCanvas.setBackground(Color.BLACK);

        setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));

        updateSize(false);

    }

    /**
     *  @param e
     */
    public void componentHidden(ComponentEvent e) {}

    /**
     *  @param e
     */
    public void componentMoved(ComponentEvent e) {}

    /**
     *  @param e
     */
    public void componentResized(ComponentEvent e) {
        updateSize();
    }

    /**
     *  @param e
     */
    public void componentShown(ComponentEvent e) {}

    /**
     *  @param args
     */
    public static void main(String[] args) {
        Opener		opener    = new Opener();
        String		dpiText   = "1200";		// "1200"
        ImagePlus	imagePlus =
            opener.openImage("/Users/daflair/Documents/Data/ConRes/ConRes-Approval-" + dpiText
                             + "/ConRes-Approval-" + dpiText + "-15i.jpg");
        ImageJPanel	imagePanel = new ImageJPanel(imagePlus);

        JFrame		frame      = new JFrame("ImageJPanel Test");
        ImageCanvas	canvas     = imagePanel.getImageCanvas();

        Container	pane       = frame.getContentPane();
        
        pane.setLayout(new BorderLayout());

        pane.add(imagePanel,BorderLayout.CENTER);
        
//        imagePanel.setBounds(0, 0, 120, 120);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        pane.validate();
        imagePanel.updateSize();
        frame.setVisible(true);
    }

    /**
     */
    public void updateSize() {
        updateSize(true);
    }

    /**
     *  @param updating
     */
    private void updateSize(boolean updating) {

        int	newWidth  = getWidth();
        int	newHeight = getHeight();
        
        int canvasWidth = newWidth;
        int canvasHeight = newHeight;
        
        if (!updating)
        	imageCanvas.setBounds(0, 0, newWidth, newHeight);

        if (fitImage) {
            double	magnification = getMagnification(newWidth, newHeight, imageWidth, imageHeight);

            GrasppeKit.debugText("Update Size",
                                 "Width: " + imageWidth + " / " + newWidth + "\t" + "Height: "
                                 + imageHeight + " / " + newHeight + "\t" + "Magnification: "
                                 + magnification, dbg);
            imageCanvas.setSourceRect(new Rectangle(0, 0, imageWidth, imageHeight));
            
            canvasWidth = (int)Math.floor(imageWidth * magnification);
            canvasHeight = (int)Math.floor(imageHeight * magnification);
            
            Dimension canvasSize = new Dimension(canvasWidth, canvasHeight);
            
            imageCanvas.setDrawingSize(canvasWidth, canvasHeight);
            imageCanvas.setSize(canvasSize);
            imageCanvas.setMinimumSize(canvasSize);
            imageCanvas.setMaximumSize(canvasSize);
            imageCanvas.setPreferredSize(canvasSize);
            imageCanvas.setMagnification(magnification);
//            repaint();
        } else {
            imageCanvas.setDrawingSize(newWidth, newHeight);
        }        
               
        imageCanvas.setLocation((newWidth-canvasWidth)/2, (newHeight-canvasHeight)/2);

    }

    /**
     * @return the imageCanvas
     */
    public ImageCanvas getImageCanvas() {
        return imageCanvas;
    }

    /**
     * @return the imageHeight
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * @return the imagePlus
     */
    public ImagePlus getImagePlus() {
        return imagePlus;
    }

    /**
     * @return the imageWidth
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     *  @param width
     *  @param height
     *  @param imageWidth
     *  @param imageHeight
     *  @return
     */
    private static double getMagnification(int width, int height, int imageWidth, int imageHeight) {

        if ((width <= 0) || (height <= 0)) return 1.0;

        double	widthFactor  = ((double)imageWidth / (double)width);
        double	heightFactor = ((double)imageHeight / (double)height);

        return Math.min(1.0 / widthFactor, 1.0 / heightFactor);
    }

    /**
     *  @return
     */
    public String getTitle() {
        if (imagePlus != null) return imagePlus.getTitle();
        else return null;
    }

    /**
     * @return the fitImage
     */
    public boolean isFitImage() {
        return fitImage;
    }

    /**
     * @param fitImage the fitImage to set
     */
    public void setFitImage(boolean fitImage) {
        this.fitImage = fitImage;
    }
}
