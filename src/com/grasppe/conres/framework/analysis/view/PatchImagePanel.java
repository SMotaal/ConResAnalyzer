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
import com.grasppe.conres.framework.targets.model.PatchDimensions;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import ij.ImagePlus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/08
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PatchImagePanel extends PatchBoundView implements ComponentListener {

    int						patchWidth   = 550,
							patchHeight  = 550;
    private BufferedImage	previewImage = null;

    /**
     * Create the panel with a model.
     *  @param model
     */
    public PatchImagePanel(AnalysisStepperModel model) {
        super(model);
        patchWidth  = getModel().getPatchPreviewSize();
        patchHeight = getModel().getPatchPreviewSize();

        this.addComponentListener(this);

        setBackground(Color.GRAY);
        update();
    }

    /**
     *  @param arg0
     */
    public void componentHidden(ComponentEvent arg0) {

    }

    /**
     *  @param arg0
     */
    public void componentMoved(ComponentEvent arg0) {

    }

    /**
     *  @param arg0
     */
    public void componentResized(ComponentEvent arg0) {

    }

    /**
     *  @param arg0
     */
    public void componentShown(ComponentEvent arg0) {
        update();
    }

    /**
     *  @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        GrasppeKit.debugText("Patch Preview", "Painting Patch Preview...", 0);

        try {
            g.setColor(getBackground());
            g.drawRect(0, 0, getWidth(), getHeight());
            g.fillRect(0, 0, getWidth(), getHeight());
        } catch (Exception exception) {
            GrasppeKit.debugError("Painting Patch Preview", exception, 2);
        }

        try {
            if (previewImage != null)
                g.drawImage(previewImage, (getWidth() - previewImage.getWidth()) / 2,
                            (getHeight() - previewImage.getHeight()) / 2, this);
        } catch (Exception exception) {
            GrasppeKit.debugError("Painting Patch Preview", exception, 2);
        }
    }

    /**
     */
    private void updatePreviewImage() {
        try {

            AnalysisStepperModel	model = getModel();

            PatchDimensions	patchDimensions  = model.getPatchDimensions();
            
            model.setImageDPI(model.getBlockImage().getResolution().value);
            
            if (model.getImageDPI()<550) return;
            double	resolutionRatio = model.getImageDPI()/model.getDisplayDPI(); // model.getResolutionRatio();
            
            if (getModel().getScaleRatio()<1)
            	getModel().setScaleRatio(1);
            double	scaleRatio      = model.getScaleRatio();
            
            double	windowRatio     = model.getWindowRatio();

            double	previewRatio    = scaleRatio / resolutionRatio;

            int		sourceWidth     = (int)Math.round(patchDimensions.getXSpan() * windowRatio),
					sourceHeight    = (int)Math.round(patchDimensions.getYSpan() * windowRatio);
            int		previewWidth    = (int)Math.round(sourceWidth * previewRatio),
					previewHeight   = (int)Math.round(sourceHeight * previewRatio);
            
            if (previewWidth > sourceWidth || previewHeight > sourceHeight){
            	previewWidth=sourceWidth;
            	previewHeight=sourceHeight;
            	getModel().setScaleRatio(resolutionRatio);
            }

            int		hintMode        = Image.SCALE_SMOOTH;
            
            ImagePlus imagePlus = getModel().getBlockImagePlus();
            
            if (imagePlus==null) return;

            Image	image           = imagePlus.getImage();

            if (image == null) return;

            int				sourceLeft         = (int)patchDimensions.getXCenter() - sourceWidth/2,
							sourceTop          = (int)patchDimensions.getYCenter() - sourceHeight/2;

            ImageProducer	patchImageProducer = new FilteredImageSource(image.getSource(),
                                                   new CropImageFilter(sourceLeft, sourceTop,
                                                       sourceWidth, sourceWidth));

            Image	patchImage = Toolkit.getDefaultToolkit().createImage(patchImageProducer);
            
            if (previewImage!=null) previewImage.flush();

            previewImage = new BufferedImage(previewWidth, previewHeight,
                                             BufferedImage.TYPE_INT_RGB);

            Graphics	g = previewImage.getGraphics();

            g.setColor(Color.BLACK);

            g.fillRect(0, 0, previewWidth, previewHeight);

            g.drawImage(patchImage, 0, 0, previewWidth, previewHeight, Color.BLACK, null);

            int	xCenter = (int)Math.round(sourceWidth * previewRatio / 2.0),
				yCenter = (int)Math.round(sourceHeight * previewRatio / 2.0);

            int	xSpan   = (int)Math.round(patchDimensions.getXSpan() * previewRatio),
				ySpan   = (int)Math.round(patchDimensions.getYSpan() * previewRatio);

            int	xRepeat = (int)Math.round(patchDimensions.getXRepeat() * previewRatio),
				yRepeat = (int)Math.round(patchDimensions.getYRepeat() * previewRatio);

            int	pLeft   = xCenter - xSpan / 2,
				pRight  = xCenter + xSpan / 2,
				pWidth  = pRight - pLeft;
            int	rLeft   = pLeft,
				rRight  = pLeft + xRepeat,
				rWidth  = rRight - rLeft;

            int	pTop    = yCenter - ySpan / 2,
				pBottom = yCenter + ySpan / 2,
				pHeight = pBottom - pTop;
            int	rTop    = pTop,
				rBottom = pTop + yRepeat,
				rHeight = rBottom - rTop;

            
            boolean fullOverlay = false;
            
            if (!fullOverlay) {
            	
            	int[] cX = new int[]{pLeft, pLeft+pWidth, pLeft+pWidth, pLeft},
            			cY = new int[]{pTop, pTop, pTop+pHeight, pTop+pHeight};
            	
            	int cS = (int) Math.min(4.0, Math.max(2.0,(2.0*(scaleRatio/2.0)))); //*previewRatio);
            	
            	for (int i : new int[]{0,1,2,3}) {
            		int x = cX[i],
            				y = cY[i];
            		g.setColor(Color.GREEN);
            		g.fillRect(x-cS, y-cS, cS*2, cS*2);         		
            		g.setColor(Color.BLACK);
            		g.drawRect(x-cS, y-cS, cS*2, cS*2);         		
            	}
            	
            } else{ 
	            g.setColor(Color.GREEN);
	            g.drawOval(xCenter - xSpan / 2, yCenter - ySpan / 2, xSpan,	ySpan);
	            
	            int gap = 35;
	            int o = (int)Math.round(gap*previewRatio);
	            g.setColor(Color.RED);
	            g.drawRect(rLeft+xSpan, rTop, rWidth-xSpan, rHeight);
	
	            for (int i : new int[] { gap, gap++, gap++, gap++, gap++}) { // , , 6 }) {		// 9, 10, 11 })     // = 5; i < 10; i++)
	            	o = (int)Math.round(i*previewRatio);
	                g.setColor(Color.GREEN);
	                g.drawRect(pLeft - o, pTop - o, pWidth + o * 2, pHeight + o * 2);
	            }
            }
            
            this.repaint();

        } catch (Exception exception) {
            previewImage = null;
            GrasppeKit.debugError("Painting Patch Preview", exception, 2);
        }

    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.framework.analysis.view.PatchBoundView#updateSize()
     */

    /**
     */
    @Override
    public void updateSize() {
        super.updateSize();

        try {

//          BufferedImage patchImage  = getModel().getPatchImage();
//          int               patchWidth  = patchImage.getWidth(),
//                        patchHeight = patchImage.getHeight();
//
//          if (!((this.patchWidth != patchWidth) || (this.patchHeight != patchHeight))) return;
            AnalysisStepperModel	model = getModel();

//
            this.patchWidth  = 550;		// model.getPatchPreviewSize();
            this.patchHeight = 550;		// model.getPatchPreviewSize();
        } catch (NullPointerException exception) {
            GrasppeKit.debugError("Updating Patch Preview Size", exception, 5);
        }

        setPreferredSize(new Dimension(this.patchWidth, this.patchHeight));
        setMinimumSize(new Dimension(this.patchWidth, this.patchHeight));
        setSize(new Dimension(this.patchWidth, this.patchHeight));
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.framework.analysis.view.PatchBoundView#updateView()
     */

    /**
     */
    @Override
    public void updateView() {
        updatePreviewImage();
        super.updateView();
    }

    /*
     *  (non-Javadoc)
     * @see javax.swing.JComponent#setBackground(java.awt.Color)
     */

    /**
     *  @param bg
     */
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
    }
}
