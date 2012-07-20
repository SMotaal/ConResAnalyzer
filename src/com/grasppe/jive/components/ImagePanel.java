/**
 * 
 */
package com.grasppe.jive.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.grasppe.lure.framework.GrasppeKit;

/**
 * @author daflair
 *
 */
public class ImagePanel extends JPanel {
	
    /**
	 * @return the previewImage
	 */
	public BufferedImage getPreviewImage() {
		return previewImage;
	}

	/**
	 * @param previewImage the previewImage to set
	 */
	public void setPreviewImage(BufferedImage previewImage) {
		this.previewImage = previewImage;
		update();
	}

	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		update();
	}
	
    /**
     */
    public void update() {
    	repaint(100);
    	System.out.println("update!");
    }	

	protected BufferedImage	previewImage = null;

    protected Color backgroundColor = Color.GRAY;
    

	/**
	 * 
	 */
	public ImagePanel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ImagePanel(LayoutManager arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ImagePanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ImagePanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	protected void initializePanel() {
        setBackground(backgroundColor);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
    /**
     *  @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        
        try {
            g.setColor(backgroundColor);
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
        
        GrasppeKit.debugText("Patch Preview", "Painting Patch Preview...", 0);
    }	

}
