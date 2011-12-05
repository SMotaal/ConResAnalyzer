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
import com.grasppe.conres.framework.targets.model.grid.ConResPatch;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeKit;

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
import java.awt.Toolkit;
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

    protected SteppingPreview	canvas = null;
    protected JFrame			frame  = null;
    protected JLabel			label  = null;

//  protected BlockState      blockState = new BlockState(10, 10, 0, 0, BlockState.fudgeMap0());
    int		dbg            = 3;
    boolean	scratchEnabled = false;

    /**
     * @param controller
     */
    public AnalysisStepperView(AnalysisStepper controller) {
        super(controller);

//      getModel().attachObserver(this);
    }

    /**
     */
    public void loadBlockFiles() {
        scratchEnabled = false;

        try {
            loadCSVFile();
            scratchEnabled = true;
        } catch (Exception exception) {
            GrasppeKit.debugText("Load CSV Error", exception.getMessage(), 2);
        }

        try {
            if (!scratchEnabled) loadScratchFile();
        } catch (Exception exception) {
            GrasppeKit.debugText("Load Scratch Error", exception.getMessage(), 2);
        }		// exception2.printStackTrace();
    }

    /**
     *  @throws Exception
     */
    public void loadCSVFile() throws Exception {
        try {
            String	filename = getTargetManager().getCornerSelector().generateFilename("a.csv");

            getModel().getBlockState().readFile(filename);		// File(filename);
        } catch (Exception exception) {

//          exception.printStackTrace();
//          Toolkit.getDefaultToolkit().beep();
            throw exception;
        }
    }

    /**
     *  @throws Exception
     */
    public void loadScratchFile() throws Exception {
        try {
            String	filename = getTargetManager().getCornerSelector().generateFilename("s.csv");

            getModel().getBlockState().readFile(filename);		// File(filename);
        } catch (Exception exception) {

//          exception.printStackTrace();
//          Toolkit.getDefaultToolkit().beep();
            throw exception;
        }
    }

    /**
     */
    public void prepareView() {

//      int dbg = 2;

        // if (getTargetManager().getModel().getActiveBlock() != getModel().getActiveBlock())
//      getTargetManager().LoadImage();
        scratchEnabled = false;

        getController().updateActiveBlock();

        scratchEnabled = true;

        if (frame != null) {
            frame.dispose();

            // if (!frame.isVisible()) frame.setVisible(true);
            // return;
        }

        // SteppingPreview canvas
        canvas = new SteppingPreview(getBlockState());

        canvas.setSize(700, 600);

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

        label = new JLabel(labelText, JLabel.LEFT);

        label.setVerticalTextPosition(JLabel.TOP);
        label.setVerticalAlignment(JLabel.TOP);
        
//        label.setPreferredSize(new Dimension(250,canvas.getPreferredSize().height));
        label.setPreferredSize(new Dimension(canvas.getSize().width, 100));
        label.setMaximumSize(label.getPreferredSize());
        label.setMinimumSize(label.getPreferredSize());
        label.setFont(label.getFont().deriveFont(11.0F));

        // Assemble Panel
        JPanel		panel  = new JPanel();
        BoxLayout	layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);

        panel.add(Box.createVerticalBox());
        panel.add(canvas);
        //panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(label);
        
        panel.setLayout(layout);
        // createRigidArea(new Dimension(10, 0)));

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

    /**
     */
    public void saveScratchFile() {
        if (!scratchEnabled) return;

        try {
            String	filename = getTargetManager().getCornerSelector().generateFilename("s.csv");

            getModel().getBlockState().writeFile(filename);
        } catch (Exception exception) {
            exception.printStackTrace();
            Toolkit.getDefaultToolkit().beep();
        }
    }

    // This method returns a buffered image with the contents of an image

    /**
     *  @param image
     *  @return
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean	hasAlpha = false;		// hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage		bimage = null;
        GraphicsEnvironment	ge     = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try {

            // Determine the type of transparency of the new buffered image
            int	transparency = Transparency.OPAQUE;

            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice			gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration	gc = gs.getDefaultConfiguration();

            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null),
                                              transparency);
        } catch (HeadlessException e) {

            // The system does not have a screen
        }

        if (bimage == null) {

            // Create a buffered image using the default color model
            int	type = BufferedImage.TYPE_INT_RGB;

            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }

            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics	g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
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
            int	padding = 5;
            int	pWidth  = 550;
            int	pHeight = pWidth;
            int	iMax    = Math.max(getModel().getBlockState().getColumns(),
                                getModel().getBlockState().getRows());
            int	iScale  = 200 / iMax;
            int	iWidth  = this.image.getWidth() * iScale;
            int	iHeight = this.image.getHeight() * iScale;

            try {
                g.drawImage(this.patchImage, padding, padding, pWidth, pHeight, this);
            } catch (Exception exception) {}

            g.drawImage(this.image, pWidth + padding * 4, padding, iWidth, iHeight, this);		// iWidth * iScale, iHeight * iScale, this);

        }

        /**
         */
        public void updateStep() {

            // blockState = thisStep.getFinalState();
        	int dbg = 2;

            BlockState	blockState = getBlockState();
            BlockMap	blockMap   = new BlockMap(blockState);

            int			row        = blockState.getRow();
            int			column     = blockState.getColumn();

//          int firstColumn = getTargetManager().getFirstColumnIndex();
//          if (column<firstColumn) blockState.setColumn(firstColumn);

            Image	patchImage = getTargetManager().getPatchImage(row, column);

            this.patchImage = toBufferedImage(patchImage);

            this.image      = blockMap.getImage();

            try {
                ConResPatch	patchModel = getTargetManager().getPatch(row, column);

                getModel().setActivePatch(patchModel);

                patchModel = getModel().getActivePatch();
                GrasppeKit.debugText("Patch String", patchModel.toString(), dbg);
                String labelString = "<html>" + patchModel.toString().replace("\n", "<br/>") + "</html>";
                if (patchModel != null) label.setText(labelString);
            } catch (Exception exception) {}

            this.repaint(1000);

            return;
        }
    }
}
