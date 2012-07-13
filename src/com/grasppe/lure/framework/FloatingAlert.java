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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.sun.xml.internal.ws.org.objectweb.asm.Label;

/**
 * @author daflair
 *
 */
public class FloatingAlert {// extends JFrame {

    protected String	messageText    = "Oops!";
    protected float		initialOpacity = 0.75F;
    protected boolean	pending        = true;
    protected int		frameRate      = Math.round(1000 / 36);
    protected int		runLength      = 300;
    protected int		frames         = Math.round(runLength / frameRate);
    protected int		frame          = 0;
    protected Timer		fadeTimer      = null;
    
    protected static final JFrame floatingFrame = new JFrame();
    protected static final JLabel floatingLabel = new JLabel();

    /**
     * @param messageText
     * @throws HeadlessException
     */
    public FloatingAlert(String messageText) throws HeadlessException {
        this(messageText, false);

//      super();
//      this.messageText = messageText;
//      createView();
////        flashView(0);
    }

    /**
     * @param messageText
     *  @param flashNow
     * @throws HeadlessException
     */
    public FloatingAlert(final String messageText, final boolean flashNow)
            throws HeadlessException {
        super();

//
//      final Runnable    doFlash = new Runnable() {
//
//          public void run() {
        setMessageText(messageText);
        createView();
        if (flashNow) flashView(0);

//      }
//        };
//        Thread    flashThread = new Thread() {
//
//      public void run() {
//          try {
//              SwingUtilities.invokeAndWait(doFlash);
//          } catch (Exception e) {}
//      }
//
//        };
//
//        flashThread.setPriority(Thread.MAX_PRIORITY);
//        flashThread.start();
    }

    /**
     */
    public void createView() {
    	
    	if (floatingFrame!=null) {
    		floatingFrame.setVisible(false);
    	}
    	
//    	floatingFrame = new JFrame();
    	try {
	    	floatingFrame.setBackground(Color.BLACK);
	    	floatingFrame.setUndecorated(true);
	    	floatingFrame.setAlwaysOnTop(true);
	    	floatingFrame.setEnabled(false);
	    	floatingFrame.setFocusable(false);
	    	floatingFrame.setFocusableWindowState(false);	
    	} catch (Exception exception) {
//    		exception.printStackTrace();
    	}
    	
        GrasppeKit.setFrameOpacity(floatingFrame, initialOpacity);    	

//        JLabel	label = new JLabel(messageText);
        

        // label.set
        floatingLabel.setText(messageText);

        floatingLabel.setForeground(Color.WHITE);
        floatingLabel.setFont(floatingLabel.getFont().deriveFont(16.0F));		// .deriveFont(Font.BOLD)
        floatingLabel.setMinimumSize(new Dimension(250, 100));
        floatingLabel.setVerticalAlignment(JLabel.CENTER);
        floatingLabel.setHorizontalAlignment(JLabel.CENTER);

        EmptyBorder	marginBorder = new EmptyBorder(30, 30, 30, 30);

        floatingLabel.setBorder(marginBorder);

        Container	pane = floatingFrame.getContentPane();

        // pane.setLayout(new BorderLayout());

        pane.add(floatingLabel);	// ,BorderLayout.CENTER);
        
//        floatingLabel = floatingLabel;
        
    	try {
	        floatingFrame.setMinimumSize(new Dimension(250, 100));
	
	        floatingFrame.pack();
	
	        floatingFrame.setLocationRelativeTo(null);
        
    	} catch (Exception exception) {
//    		exception.printStackTrace();
    	}        

    }

    /**
     */
    public void fadeView() {

//      SwingUtilities.invokeLater(
//
//      new Thread() {
//          
//          public void run() {
    	floatingFrame.setVisible(true);
        fadeTimer = new Timer(frameRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setFadeOpactiy();
                frame++;
            }

        });

        fadeTimer.setRepeats(true);
        fadeTimer.start();

//      }
//      });
    }

    /**
     */
    public void flashView() {
        flashView(1);
    }

    /**
     *  @param duration
     */
    public void flashView(int duration) {
        final int	flashDuration = duration;

        pending = true;

//      
//      final Runnable doFlash = new Runnable() {
//              public void run() {
        floatingFrame.setVisible(true);
        floatingFrame.repaint();

        pending = false;

        if (flashDuration > 0) {
            Timer	timer = new Timer(flashDuration, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fadeView();
                }

            });

            timer.setRepeats(false);
            timer.start();
        }

//      }
//      };
//      Thread flashThread = new Thread() {
//           public void run() {
//       try {
//           SwingUtilities.invokeAndWait(doFlash);
//       } catch (Exception e) { }
//           }
//          
//      };
//      flashThread.setPriority(Thread.MAX_PRIORITY);
//      flashThread.start();

    }

    /**
     * @return the messageText
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * @return the pending
     */
    public boolean isPending() {
        return pending;
    }

    /**
     */
    public void setFadeOpactiy() {
        if (fadeTimer == null) return;

        float	fadeRate = (float)(Math.log(((double)frames - (double)frame))
                                 / Math.log((double)frames));
        float	newOpacity = (initialOpacity) * fadeRate;

        if (newOpacity > 0) GrasppeKit.setFrameOpacity(floatingFrame, newOpacity);

        if ((frame >= frames) || (newOpacity <= 0)) {
            fadeTimer.stop();
            floatingFrame.setVisible(false);
        }
    }

    /**
     * @param messageText the messageText to set
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
        if (floatingLabel!=null)
        	floatingLabel.setText(messageText);
    }
}
