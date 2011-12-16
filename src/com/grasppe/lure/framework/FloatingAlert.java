/*
 * @(#)FloatingAlert.java   11/12/15
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.lure.framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * @author daflair
 *
 */
public class FloatingAlert extends JFrame {
	
	/**
	 * @return the pending
	 */
	public boolean isPending() {
		return pending;
	}
	/**
	 * @param messageText
	 * @throws HeadlessException
	 */
	public FloatingAlert(String messageText) throws HeadlessException {
		super();
		this.messageText = messageText;
		createView();
//		flashView(0);
	}
	
	/**
	 * @param messageText
	 * @throws HeadlessException
	 */
	public FloatingAlert(String messageText, boolean flashNow) throws HeadlessException {
		super();
		this.messageText = messageText;
		createView();
		if (flashNow) flashView(0);
	}	
	protected String messageText = "Oops!";
	protected float initialOpacity = 0.75F;
	protected boolean pending = true;
	
	public void flashView() {
		flashView(1);
	}
	public void flashView(int duration) {
		final int flashDuration = duration; 
		
		pending = true;
		
		final Runnable doFlash = new Runnable() {
				public void run() {
					setVisible(true);
					repaint();
					
					pending = false;
					
					if (flashDuration>0) {
						Timer timer = new Timer(flashDuration, new ActionListener () {
							public void actionPerformed(ActionEvent e) {
								fadeView();
							}
						});
						timer.setRepeats(false);
						timer.start();
					}

				}
		};
		Thread flashThread = new Thread() {
		     public void run() {
		         try {
		             SwingUtilities.invokeLater(doFlash);
		         } catch (Exception e) { }
		     }
			
		};
		flashThread.setPriority(Thread.MAX_PRIORITY);
		flashThread.start();
		
	}
	
	protected int frameRate = Math.round(1000/36);
	protected int runLength = 300;
	protected int frames = Math.round(runLength/frameRate);
	protected int frame = 0;
	protected Timer fadeTimer = null;
	
	public void fadeView() {
		
//		SwingUtilities.invokeLater(
//
//		new Thread() {
//			
//			public void run() {
				setVisible(true);
				fadeTimer = new Timer(frameRate, new ActionListener () {
					public void actionPerformed(ActionEvent e) {
						setFadeOpactiy();
						frame++;
					}
				});
				
				fadeTimer.setRepeats(true);
				fadeTimer.start();

//			}
//		});	
	}
	
	public void setFadeOpactiy() {
		if (fadeTimer==null) return;
		float fadeRate = (float) (Math.log(((double)frames-(double)frame))/Math.log((double)frames));
		float newOpacity = (initialOpacity)*fadeRate;
		if (newOpacity>0)
			GrasppeKit.setFrameOpacity(this, newOpacity);
		if (frame>=frames || newOpacity<=0) {
			fadeTimer.stop();
			setVisible(false);
		}
	}
			

    /**
     */
    public void createView() {
        setBackground(Color.BLACK);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setEnabled(false);
        setFocusable(false);
        setFocusableWindowState(false);
        
        GrasppeKit.setFrameOpacity(this, initialOpacity);
        
        JLabel label = new JLabel(messageText);
        
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(16.0F)); //.deriveFont(Font.BOLD)
//        label.setMinimumSize(new Dimension(250,100));
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        
        Container pane = getContentPane();
        
        pane.setLayout(new BorderLayout());
        
        pane.add(label,BorderLayout.CENTER);
        
        setMinimumSize(new Dimension(250,100));
        
        pack();
        
        setLocationRelativeTo(null);

    }
}
