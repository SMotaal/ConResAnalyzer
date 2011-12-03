/*
 * @(#)AnalysisStepperView.java   11/11/27
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis.view;

import com.grasppe.conres.framework.analysis.AnalysisStepper;
import com.grasppe.conres.framework.analysis.model.AnalysisStepperModel;
import com.grasppe.conres.framework.analysis.stepping.BlockMap;
import com.grasppe.conres.framework.analysis.stepping.BlockState;
import com.grasppe.conres.framework.analysis.stepping.SteppingStrategy;
import com.grasppe.lure.components.AbstractView;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AnalysisStepperView extends AbstractView {

    protected SteppingPreview	canvas     = null;
    protected JFrame			frame      = null;
    protected BlockState		blockState = new BlockState(10, 10, 0, 0, BlockState.fudgeMap0());

    /**
     * @param controller
     */
    public AnalysisStepperView(AnalysisStepper controller) {
        super(controller);
        getModel().attachObserver(this);
    }

    /**
     */
    public void prepareView() {

        // SteppingPreview canvas
        canvas = new SteppingPreview(blockState);

        canvas.setSize(500, 500);

        // canvas.setBackground(Color.blue);

        // Instructions Label
        String	labelText = "<html>" + "<h3>ConRes Stepping Logic Simulator</h3>"
                           + "<pre>Marking patches: (and step over)</pre>"
                           + "<pre>    <b>  G     </b>" + "Good</pre>" + "<pre>    <b>  A     </b>"
                           + "Accept</pre>" + "<pre>    <b>  R     </b>" + "Reject</pre>"
                           + "<pre></pre>" + "<pre>Moving around:</pre>"
                           + "<pre>    <b>  \u2191     </b>" + "Up</pre>"
                           + "<pre>    <b>  \u2193     </b>" + "Down</pre>"
                           + "<pre>    <b>  \u2190    </b>" + "Left</pre>"
                           + "<pre>    <b>  \u2192    </b>" + "Right</pre>" + "<pre></pre>"
                           + "<pre>    <b>  SP    </b>" + "Step Over</pre>"
                           + "<pre>    <b>\u21E7 SP   </b>" + "Step Back</pre>" + "</html>";
        JLabel	label = new JLabel(labelText, JLabel.LEFT);

        label.setVerticalTextPosition(JLabel.TOP);
        label.setVerticalAlignment(JLabel.TOP);

        // Assemble Panel
        JPanel		panel  = new JPanel();
        BoxLayout	layout = new BoxLayout(panel, BoxLayout.X_AXIS);

        panel.add(canvas);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));

        // Assemble Frame
        frame = new JFrame("SteppingPrewview");

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        frame.add(panel, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.pack();

        GraphicsEnvironment	graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        int					frameLeft           = graphicsEnvironment.getCenterPoint().x - frame.getWidth() / 2;
        int					frameTop            = graphicsEnvironment.getCenterPoint().y - frame.getHeight() / 2;

        frame.setLocation(frameLeft, frameTop);

        KeyListener	keyListener = new KeyAdapter() {

            public void keyPressed(KeyEvent ke) {
                getController().BlockStepKey(ke.getKeyCode(), ke.getModifiers());
            }
        };

        frame.addKeyListener(keyListener);

        // Show Frame
        frame.setVisible(true);
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractView#update()
     */

    /**
     */
    @Override
    public void update() {
        if (canvas == null) return;
        canvas.updateStep();	// getModel().getBlockState())
    }

    /**
     *  @return
     */
    public AnalysisStepper getController() {
        return (AnalysisStepper)this.controller;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractView#getModel()
     */

    /**
     *  @return
     */
    @Override
    protected AnalysisStepperModel getModel() {
        return (AnalysisStepperModel)super.getControllerModel();
    }

    /**
     * Class description
     *  @version        $Revision: 1.0, 11/11/27
     *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class SteppingPreview extends Canvas {

        private List<SteppingStrategy>	steppingHistory = new ArrayList<SteppingStrategy>();	// @SuppressWarnings("rawtypes")
        private BufferedImage	image;

        /**
         *  @param initialBlockState
         */
        public SteppingPreview(BlockState initialBlockState) {
            super();
            blockState = initialBlockState;

            // BlockState    finalState = blockState;
            this.image = (new BlockMap(blockState)).getImage();
            this.repaint(1000);

            return;
        }

        /**
         * @param g
         */
        public void paint(Graphics g) {
            int	iScale  = 50;
            int	iWidth  = this.image.getWidth();
            int	iHeight = this.image.getHeight();

            g.drawImage(this.image, 0, 0, iWidth * iScale, iHeight * iScale, this);
        }

        /**
         */
        public void updateStep() {

            // blockState = thisStep.getFinalState();

            BlockMap	blockMap = new BlockMap(getModel().getBlockState());

            this.image = blockMap.getImage();
            this.repaint(1000);
        }
    }
}
