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
import com.grasppe.conres.framework.analysis.stepping.BlockMap;
import com.grasppe.conres.framework.analysis.stepping.BlockState;
import com.grasppe.conres.framework.analysis.stepping.SteppingStrategy;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.lure.components.AbstractView;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
//    protected BlockState		blockState = new BlockState(10, 10, 0, 0, BlockState.fudgeMap0());
    int dbg = 3;

    /**
     * @param controller
     */
    public AnalysisStepperView(AnalysisStepper controller) {
        super(controller);
//        getModel().attachObserver(this);
    }
    
    public AnalysisManager getAnalysisManager() {
        return getController().getAnalysisManager();
    }
    
    public AnalysisManagerModel getAnalysisManagerModel() {
    	return getController().getAnalysisManagerModel();
    }
    
    public BlockState getBlockState() {
    	return getModel().getBlockState();
    }

    /**
     */
    public void prepareView() {
    	
    	// if (getTargetManager().getModel().getActiveBlock() != getModel().getActiveBlock())
//    	getTargetManager().LoadImage();
    	
    	getController().updateActiveBlock();
    	
        if (frame!=null) {
        	frame.dispose();
        	//if (!frame.isVisible()) frame.setVisible(true);
        	//return;
        }
        // SteppingPreview canvas
        canvas = new SteppingPreview(getBlockState());

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
        frame = new JFrame("Analyze Block");

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
    	super.update();
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
        private BufferedImage	image, patchImage;

        /**
         *  @param initialBlockState
         */
        public SteppingPreview(BlockState initialBlockState) {
            super();
//            blockState = initialBlockState;

            // BlockState    finalState = blockState;
//            this.image = (new BlockMap(getBlockState())).getImage();
//            this.repaint(1000);
            
            updateStep();

            return;
        }

        /**
         * @param g
         */
        public void paint(Graphics g) {
            int	iScale  = 5;
            int	iWidth  = this.image.getWidth() * iScale;
            int	iHeight = this.image.getHeight() * iScale;
//            int iScale = Math.min(this.getWidth()/iWidth,this.getHeight()/iHeight);
            try {
            	g.drawImage(this.patchImage, 0, 0, 550, 550, this);
            } catch(Exception exception) {}

            g.drawImage(this.image, 500-iWidth, 0, iWidth, iHeight,this); //iWidth * iScale, iHeight * iScale, this);
            
        }

        /**
         */
        public void updateStep() {

            // blockState = thisStep.getFinalState();

        	BlockState blockState = getBlockState();
            BlockMap	blockMap = new BlockMap(blockState);

            int row = blockState.getRow();
            int column = blockState.getColumn();
            
            Image patchImage = getTargetManager().getPatchImage(row, column);
            
            this.patchImage = toBufferedImage(patchImage);
            
//            this.image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(image.getWidth(null), image.getHeight(null));
//            
//            this.image.
            
            this.image = blockMap.getImage();
            
//            this.image = new BufferedImage(300, 300,BufferedImage.TYPE_INT_RGB);
            
            
//            this.image.getGraphics().draw
            //this.image.getGraphics().drawImage(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9)
            
            this.repaint(1000);
            
            
            return;
        }
    }
    
    
    // This method returns a buffered image with the contents of an image
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean hasAlpha = false; // hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }
    
    /**
     * 	@return
     */
    public TargetManager getTargetManager() {
        return getAnalysisManager().getTargetManager();
    }
}
