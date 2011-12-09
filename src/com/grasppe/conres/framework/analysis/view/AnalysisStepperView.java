/*
 * @(#)AnalysisStepperView.java   11/11/27
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis.view;

import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.analysis.AnalysisStepper;
import com.grasppe.conres.framework.analysis.model.AnalysisManagerModel;
import com.grasppe.conres.framework.analysis.model.AnalysisStepperModel;
import com.grasppe.conres.framework.analysis.stepping.BlockState;
import com.grasppe.conres.framework.analysis.stepping.SteppingStrategy;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.grid.ConResPatch;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public class AnalysisStepperView extends AbstractView {

    protected JFrame				frame              = null;
//    protected SteppingPreview		canvas             = null;
    protected PatchImagePanel		patchImagePanel    = null;
    protected BlockMapImagePanel	blockMapImagePanel = null;
    protected JLabel				label              = null;
    int								dbg                = 3;

    /**
     * @param controller
     */
    public AnalysisStepperView(AnalysisStepper controller) {
        super(controller);
        getModel().attachObserver(this);
    }

    public void setVisible(boolean visible) {
    	if (frame!=null && !visible)
    		frame.setVisible(false);
    	
    	if (!visible) return;
    	
    	if (frame!=null && !frame.isVisible()) // && visible) // visible==true;
    		frame.dispose();
    	
    	createView();
    }
    /**
     */
    private void createView() {
    	
        if (frame!=null)
        	setVisible(false);

//      int dbg = 2;

        // SteppingPreview canvas
//        canvas = new SteppingPreview(getBlockState());

//        canvas.setSize(100, 100);

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

        label = new JLabel(labelText, JLabel.LEFT);
        label.setVerticalTextPosition(JLabel.TOP);
        label.setVerticalAlignment(JLabel.TOP);
        label.setPreferredSize(new Dimension(100, 100));
        label.setMaximumSize(label.getPreferredSize());
        label.setMinimumSize(label.getPreferredSize());
        label.setFont(label.getFont().deriveFont(11.0F));

        // Assemble Panel
        JPanel		panel     = new JPanel();
        BoxLayout	layout    = new BoxLayout(panel, BoxLayout.LINE_AXIS);

        JPanel		subPanel  = new JPanel();
        BoxLayout	subLayout = new BoxLayout(subPanel, BoxLayout.PAGE_AXIS);

        blockMapImagePanel = new BlockMapImagePanel(getModel());
        patchImagePanel    = new PatchImagePanel(getModel());

        subPanel.add(label);
        subPanel.add(blockMapImagePanel);
        subPanel.setLayout(subLayout);

        panel.add(patchImagePanel);

        panel.add(subPanel);

        panel.setLayout(layout);

        // createRigidArea(new Dimension(10, 0)));

        // Assemble Frame
        
        frame = new JFrame("Analyze Block");

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        frame.add(panel, BorderLayout.CENTER);

//      frame.setResizable(false);
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

        update();
    }

    // This method returns a buffered image with the contents of an image

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractView#update()
     */

    /**
     */
    @Override
    public void update() {
        super.update();
//        if (canvas != null) canvas.updateStep();	// getModel().getBlockState())
        if (patchImagePanel != null) patchImagePanel.update();
        if (blockMapImagePanel != null) blockMapImagePanel.update();
    }

    /**
     *  @return
     */
    public AnalysisManager getAnalysisManager() {
        return getController().getAnalysisManager();
    }

    /**
     *  @return
     */
    public AnalysisManagerModel getAnalysisManagerModel() {
        return getController().getAnalysisManagerModel();
    }

    /**
     *  @return
     */
    public BlockState getBlockState() {
        return getModel().getBlockState();
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
     *  @return
     */
    public TargetManager getTargetManager() {
        return getAnalysisManager().getTargetManager();
    }

    /**
     * Class description
     *  @version        $Revision: 1.0, 11/11/27
     *  @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
     */
    public class SteppingPreview extends Canvas {

        private List<SteppingStrategy>	steppingHistory = new ArrayList<SteppingStrategy>();	// @SuppressWarnings("rawtypes")

        /**
         *  @param initialBlockState
         */
        public SteppingPreview(BlockState initialBlockState) {
            super();

//          blockState = initialBlockState;

            // BlockState    finalState = blockState;
//          this.image = (new BlockMap(getBlockState())).getImage();
//          this.repaint(1000);

            updateStep();

            return;
        }

        /**
         * @param g
         */
        public void paint(Graphics g) {
            super.paint(g);

            int	padding = 5;
            int	pWidth  = 550;
            int	pHeight = pWidth;
            int	iMax    = Math.max(getModel().getBlockState().getColumns(),
                                getModel().getBlockState().getRows());
            int	iScale  = 200 / iMax;
            int	iWidth  = getModel().getBlockMapImage().getWidth() * iScale;
            int	iHeight = getModel().getBlockMapImage().getHeight() * iScale;

//          try {
//              g.drawImage(getModel().getPatchImage(), padding, padding, pWidth, pHeight, this);
//          } catch (Exception exception) {}

            g.drawImage(getModel().getBlockMapImage(), pWidth + padding * 4, padding, iWidth,
                        iHeight, this);		// iWidth * iScale, iHeight * iScale, this);

        }

        /**
         */
        public void updateStep() {

            // blockState = thisStep.getFinalState();
            int	dbg = 4;

//
//          BlockState    blockState = getBlockState();
//          BlockMap  blockMap   = new BlockMap(blockState);
//
//          int           row        = blockState.getRow();
//          int           column     = blockState.getColumn();

//          int firstColumn = getTargetManager().getFirstColumnIndex();
//          if (column<firstColumn) blockState.setColumn(firstColumn);

//            Image patchImage = getTargetManager().getPatchImage(row, column);
//            
//            getModel().setPatchImage(patchImage);
//
//            getModel().setImage(blockMap.getImage());

            try {
                ConResPatch	patchModel = getModel().getActivePatch();

                GrasppeKit.debugText("Patch String", patchModel.toString(), dbg);

                String	labelString = "<html>" + patchModel.toString().replace("\n", "<br/>")
                                     + "</html>";

                if (patchModel != null) label.setText(labelString);
            } catch (Exception exception) {}

            this.repaint(1000);

            return;
        }
    }
}
