/*
 * @(#)BlockMapTest.java   11/08/25
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the properyty of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.analysis.stepping;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author daflair
 *
 */
@SuppressWarnings("serial")
public class BlockMapTestApp extends Canvas {

    /** Field description */
    private BlockState	blockState = new BlockState(10, 10, 0, 0, BlockState.fudgeMap0());

    /** Field description */
    private List	history = new ArrayList();		// @SuppressWarnings("rawtypes")

    /** Field description */
    private BufferedImage	image;		// private BufferedImage[] image = new BufferedImage[10];

    /**
     * Constructs ...
     *
     */
    public BlockMapTestApp() {
        BlockState	finalState = this.blockState;

        // new BlockState(10, 10, 0, 0, BlockState.fudgeMap1()); //this.blockState.copy();
        this.image = (new BlockMap(finalState)).getImage();

        return;
    }

    /**
     * Method description
     *
     *
     * @param keyCode
     * @param keyModifiers
     */
    public void BlockStepKey(int keyCode, int keyModifiers) {		// SteppingStrategy thisStep) {

        // SmartBlockState smartState = new SmartBlockState(this.blockState);
        SmartBlockState		smartState = new SmartBlockState(this.blockState);
        SteppingStrategy	thisStep   = new StepNext(smartState);

        System.out.println("Modifier " + keyModifiers);

        boolean	snapState = true;

        switch (keyCode) {
        case KeyEvent.VK_SPACE :
            if (keyModifiers == 1) {
                if (!this.history.isEmpty()) {
                    thisStep = new StepBack(smartState, this.history);
                    this.history.remove(this.history.size() - 1);
                } else
                    return;

                snapState = false;
            } else
                thisStep = new StepNext(smartState);

            break;

        case KeyEvent.VK_UP :
            thisStep = new StepUp(smartState);

            break;

        case KeyEvent.VK_DOWN :
            thisStep = new StepDown(smartState);

            break;

        case KeyEvent.VK_LEFT :
            thisStep = new StepLeft(smartState);

            break;

        case KeyEvent.VK_RIGHT :
            thisStep = new StepRight(smartState);

            break;

        case KeyEvent.VK_Q :
            thisStep = new SetAndStep(smartState, 2);

            break;

        case KeyEvent.VK_W :
            thisStep = new SetAndStep(smartState, 1);

            break;

        case KeyEvent.VK_E :
            thisStep = new SetAndStep(smartState, -1);

            break;

        default :
            return;
        }

        if (snapState) {
            int		row          = this.blockState.getRow();
            int		column       = this.blockState.getColumn();
            int[]	currentState = { row, column };

            this.history.add(currentState);
        }

        thisStep.execute();

        // smartState = new SmartBlockState(thisStep.finalState);
        this.blockState = thisStep.getFinalState();

        BlockMap	blockMap = new BlockMap(this.blockState);

        this.image = blockMap.getImage();
        this.repaint(1000);
    }

    /**
     * Method description
     *
     *
     * @param argv
     */
    public static void main(String argv[]) {
        Frame					frame  = new Frame("BlockMap");
        final BlockMapTestApp	canvas = new BlockMapTestApp();

        canvas.setSize(500, 500);
        canvas.setBackground(Color.blue);

        JPanel		panel     = new JPanel();
        BoxLayout	layout    = new BoxLayout(panel, BoxLayout.X_AXIS);
        String		labelText = "<html>" + "<h3>ConRes Stepping Logic Simulator</h3>"
                                + "<pre>Marking patches: (and step over)</pre>" + "<pre>    <b>  Q     </b>"
                                + "Good</pre>" + "<pre>    <b>  W     </b>" + "Accept</pre>"
                                + "<pre>    <b>  E     </b>" + "Reject</pre>" + "<pre></pre>"
                                + "<pre>Moving around:</pre>" + "<pre>    <b>  \u2191     </b>" + "Up</pre>"
                                + "<pre>    <b>  \u2193     </b>" + "Down</pre>" + "<pre>    <b>  \u2190    </b>"
                                + "Left</pre>" + "<pre>    <b>  \u2192    </b>" + "Right</pre>" + "<pre></pre>"

        // "<pre>    <b>  D     </b>" + "Reject</pre>"
        + "<pre>    <b>  SP    </b>" + "Step Over</pre>" + "<pre>    <b>\u21E7 SP   </b>" + "Step Back</pre>"
                                     + "</html>";

//
        JLabel	label = new JLabel(labelText, JLabel.LEFT);

        label.setVerticalTextPosition(JLabel.TOP);
        label.setVerticalAlignment(JLabel.TOP);

//      JPanel labelPanel = new JPanel(new BorderLayout());
//      labelPanel.add(label,BorderLayout.NORTH);
        // panel.setSize(500,500);
        panel.add(canvas);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        frame.add(panel, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.pack();

        // label.setSize(label.getWidth(),500);
        GraphicsEnvironment	graphicsEnvironment = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
        int					x                   = graphicsEnvironment.getCenterPoint().x - frame.getWidth() / 2;
        int					y                   = graphicsEnvironment.getCenterPoint().y - frame.getHeight() / 2;

        frame.setLocation(x, y);
        frame.setVisible(true);

        WindowListener	windowListener = new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        };

        frame.addWindowListener(windowListener);

        KeyListener	keyListener = new KeyAdapter() {

            public void keyPressed(KeyEvent ke) {
                canvas.BlockStepKey(ke.getKeyCode(), ke.getModifiers());
            }
        };

        frame.addKeyListener(keyListener);

//      fail("Not yet implemented"); // TODO
    }

    /**
     * Method description
     *
     *
     * @param g
     */
    public void paint(Graphics g) {
        int	iScale  = 50;
        int	iWidth  = this.image.getWidth();
        int	iHeight = this.image.getHeight();

        g.drawImage(this.image, 0, 0, iWidth * iScale, iHeight * iScale, this);
    }
}
