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
        GrasppeKit.debugText("Patch Preview", "Painting Patch Preview...", 0);
    }	

}
