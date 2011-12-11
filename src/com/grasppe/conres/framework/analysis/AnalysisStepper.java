/*
 * @(#)BlockMapTest.java   11/08/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.analysis;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.framework.analysis.model.AnalysisManagerModel;
import com.grasppe.conres.framework.analysis.model.AnalysisStepperModel;
import com.grasppe.conres.framework.analysis.stepping.BlockMap;
import com.grasppe.conres.framework.analysis.stepping.BlockState;
import com.grasppe.conres.framework.analysis.stepping.SetAndStep;
import com.grasppe.conres.framework.analysis.stepping.SmartBlockState;
import com.grasppe.conres.framework.analysis.stepping.StepBack;
import com.grasppe.conres.framework.analysis.stepping.StepDown;
import com.grasppe.conres.framework.analysis.stepping.StepLeft;
import com.grasppe.conres.framework.analysis.stepping.StepNext;
import com.grasppe.conres.framework.analysis.stepping.StepRight;
import com.grasppe.conres.framework.analysis.stepping.StepUp;
import com.grasppe.conres.framework.analysis.stepping.SteppingStrategy;
import com.grasppe.conres.framework.analysis.view.AnalysisStepperView;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.framework.targets.model.grid.ConResPatch;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

/**
 * @author daflair
 */
public class AnalysisStepper extends AbstractController {

    int								dbg             = 0;
    protected AnalysisManager		analysisManager = null;
    protected AnalysisStepperView	stepperView     = null;

    /**
     * Constructs and attaches a new controller and a new model.
     *  @param analysisManager
     */
    public AnalysisStepper(AnalysisManager analysisManager) {
        this(analysisManager, new AnalysisStepperModel());

        return;
    }

    /**
     * Constructs a new controller and attaches it to the unattached model.
     *  @param analysisManager
     * @param model
     */
    private AnalysisStepper(AnalysisManager analysisManager, AnalysisStepperModel model) {
        super(model);
        this.analysisManager = analysisManager;
        stepperView          = new AnalysisStepperView(this);
        attachView(stepperView);
    }

    /**
     * @param keyCode
     * @param keyModifiers
     */
    public void BlockStepKey(int keyCode, int keyModifiers) {		// SteppingStrategy thisStep) {

        SmartBlockState		smartState = new SmartBlockState(getModel().getBlockState());
        SteppingStrategy	thisStep   = new StepNext(smartState);

        boolean				snapState  = true;
        boolean				goBack     = false;

        switch (keyCode) {

        case KeyEvent.VK_SPACE :
            if (keyModifiers == 1) {
                goBack = true;
            } else
                thisStep = new StepNext(smartState);

            break;

        case KeyEvent.VK_BACK_SPACE :
            goBack = true;
            break;

        case KeyEvent.VK_UP :
            if (keyModifiers == 0) thisStep = new StepUp(smartState);
            break;

        case KeyEvent.VK_DOWN :
            if (keyModifiers == 0) thisStep = new StepDown(smartState);

            break;

        case KeyEvent.VK_LEFT :
            if (keyModifiers == 0) thisStep = new StepLeft(smartState);

            break;

        case KeyEvent.VK_RIGHT :
            if (keyModifiers == 0) thisStep = new StepRight(smartState);

            break;

        case KeyEvent.VK_G :
            if (keyModifiers == 0) thisStep = new SetAndStep(smartState, 2);

            break;

        case KeyEvent.VK_A :
            if (keyModifiers == 0) thisStep = new SetAndStep(smartState, 1);

            break;

        case KeyEvent.VK_R :
            if (keyModifiers == 0) thisStep = new SetAndStep(smartState, -1);

            break;

        default :
            return;
        }

        if (goBack) {
            if (!getModel().getHistory().isEmpty()) {
                thisStep = new StepBack(smartState, getModel().getHistory());
                getModel().getHistory().remove(getModel().getHistory().size() - 1);
            } else
                return;

            snapState = false;
        }

        if (snapState) {
            int		row          = getModel().getBlockState().getRow();
            int		column       = getModel().getBlockState().getColumn();
            int[]	currentState = { row, column };

            getModel().getHistory().add(currentState);
        }

        thisStep.execute();

        setNewBlockState(thisStep.getFinalState());
    }

    /**
     * 	@param blockState
     */
    public void loadBlockFiles(BlockState blockState) {
        setScratchEnabled(false);

        try {
            loadCSVFile(blockState);
            setScratchEnabled(true);
        } catch (Exception exception) {
            GrasppeKit.debugError("Loading Analysis Grid File", exception, 5);
        }

        try {
            if (!isScratchEnabled()) loadScratchFile(blockState);
        } catch (Exception exception) {
            GrasppeKit.debugError("Load Analysis Scratch File", exception, 5);
        }

        setScratchEnabled(true);
        update();
    }

    /**
     * 	@param blockState
     *      @throws Exception
     */
    public void loadCSVFile(BlockState blockState) throws Exception {
            String	filename = getTargetManager().generateFilename("a.csv");

            blockState.readFile(filename);
    }

    /**
     * 	@param blockState
     *      @throws Exception
     */
    public void loadScratchFile(BlockState blockState) throws Exception {
            String	filename = getTargetManager().generateFilename("s.csv");

            blockState.readFile(filename);
            setScratchEnabled(true);
    }

    /**
     * @param argv
     */
    public static void oldMain(String argv[]) {

//      Frame                 frame  = new Frame("BlockMap");
//      final AnalysisStepper canvas = new AnalysisStepper();
//
//      canvas.setSize(500, 500);
//      canvas.setBackground(Color.blue);
//
//      JPanel        panel     = new JPanel();
//      BoxLayout layout    = new BoxLayout(panel, BoxLayout.X_AXIS);
//      String        labelText = "<html>" + "<h3>ConRes Stepping Logic Simulator</h3>"
//                              + "<pre>Marking patches: (and step over)</pre>" + "<pre>    <b>  Q     </b>"
//                              + "Good</pre>" + "<pre>    <b>  W     </b>" + "Accept</pre>"
//                              + "<pre>    <b>  E     </b>" + "Reject</pre>" + "<pre></pre>"
//                              + "<pre>Moving around:</pre>" + "<pre>    <b>  \u2191     </b>" + "Up</pre>"
//                              + "<pre>    <b>  \u2193     </b>" + "Down</pre>" + "<pre>    <b>  \u2190    </b>"
//                              + "Left</pre>" + "<pre>    <b>  \u2192    </b>" + "Right</pre>" + "<pre></pre>"
//      + "<pre>    <b>  SP    </b>" + "Step Over</pre>" + "<pre>    <b>\u21E7 SP   </b>" + "Step Back</pre>"
//                                   + "</html>";
//
//      JLabel    label = new JLabel(labelText, JLabel.LEFT);
//
//      label.setVerticalTextPosition(JLabel.TOP);
//      label.setVerticalAlignment(JLabel.TOP);
//
////    JPanel labelPanel = new JPanel(new BorderLayout());
////    labelPanel.add(label,BorderLayout.NORTH);
//      // panel.setSize(500,500);
//      panel.add(canvas);
//      panel.add(Box.createRigidArea(new Dimension(10, 0)));
//      panel.add(label);
//      panel.add(Box.createRigidArea(new Dimension(10, 0)));
//      frame.add(panel, BorderLayout.CENTER);
//      frame.setResizable(false);
//      frame.pack();
//
//      // label.setSize(label.getWidth(),500);
//      GraphicsEnvironment   graphicsEnvironment = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
//      int                   x                   = graphicsEnvironment.getCenterPoint().x - frame.getWidth() / 2;
//      int                   y                   = graphicsEnvironment.getCenterPoint().y - frame.getHeight() / 2;
//      
//      frame.setLocation(x, y);
//      frame.setVisible(true);

//      WindowListener    windowListener = new WindowAdapter() {
//
//          public void windowClosing(WindowEvent we) {
//              System.exit(0);
//          }
//      };

//      frame.addWindowListener(windowListener);

//      fail("Not yet implemented"); // TODO
    }

    /**
     */
    private void pushUpdates() {
        getModel().notifyObservers();
    }

    /**
     */
    public void saveScratchFile() {
        if (!isScratchEnabled()) return;

        try {
            String	filename = getTargetManager().generateFilename("s.csv");

            getModel().getBlockState().writeFile(filename);
        } catch (Exception exception) {
            GrasppeKit.debugError("Saving Analysis Scratch", exception, 2);
        }
    }

    /**
     */
    public void showView() {
        getTargetManager().loadImage();
        update();
        updateActiveBlock();
        getStepperView().setVisible(true);
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
        } catch (HeadlessException exception) {
            GrasppeKit.debugError("Generating Buffered Image", exception, 2);
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
     * @see com.grasppe.lure.components.AbstractController#update()
     */

    /**
     */
    @Override
    public void update() {
        super.update();
    }

    /**
     */
    public void updateActiveBlock() {

        ConResBlock	activeBlock = getTargetManager().getModel().getActiveBlock();

        if (activeBlock == null) {
            getModel().setActiveBlock(null);
            setNewBlockState(null);
            setScratchEnabled(false);
            getStepperView().setVisible(false);

            return;
        }

        if (getModel().getActiveBlock() == activeBlock) return;

        setScratchEnabled(false);

        getStepperView().setVisible(false);

        getModel().setActiveBlock(getTargetManager().getModel().getActiveBlock());

        int			blockColumns = getActiveBlock().getXAxis().getValues().length;
        int			blockRows    = getActiveBlock().getYAxis().getValues().length;

        int			firstColumn  = getTargetManager().getFirstColumnIndex();

        BlockState	blockState   = new BlockState(blockRows, blockColumns, firstColumn);	// , BlockState.fudgeMap1());

        loadBlockFiles(blockState);
        setNewBlockState(blockState);

        if (getModel().getBlockState() == null) {
            getModel().setBlockState(new BlockState(blockRows, blockColumns, firstColumn));
            pushUpdates();
        }

        setScratchEnabled(true);

    }

    /**
     *  @param row
     *  @param column
     */
    public void updatePatchPreviews(int row, int column) {
        try {
            if ((row < 0) || (column < 0)) {
                getModel().setPatchImage(null);
                getModel().setImage(null);

                return;
            }

            Image		patchImage = getTargetManager().getPatchImage(row, column);
            BlockMap	blockMap   = new BlockMap(getModel().getBlockState());

            getModel().setPatchImage(patchImage);
            getModel().setImage(blockMap.getImage());
        } catch (Exception exception) {
            GrasppeKit.debugError("Updating Patch Preview", exception, 2);
        }
    }

    /**
     *  @return
     */
    public ConResBlock getActiveBlock() {
        return getModel().getActiveBlock();
    }

    /**
     * @return the analysisManager
     */
    public AnalysisManager getAnalysisManager() {
        return analysisManager;
    }

    /**
     *  @return
     */
    public AnalysisManagerModel getAnalysisManagerModel() {
        return getAnalysisManager().getModel();
    }

    /**
     * @return the analyzer
     */
    protected ConResAnalyzer getAnalyzer() {
        return getAnalysisManager().getAnalyzer();
    }

    /**
     * Returns a correctly-cast model.
     * @return
     */
    public AnalysisStepperModel getModel() {
        return (AnalysisStepperModel)model;
    }

    /**
     * @return the selectorView
     */
    public AnalysisStepperView getStepperView() {
        return stepperView;
    }

    /**
     *  @return
     */
    public TargetManager getTargetManager() {
        return getAnalysisManager().getTargetManager();
    }

    /**
     * 	@return
     */
    public boolean isScratchEnabled() {
        return getModel().isScratchEnabled();
    }

    /**
     *  @param newState
     */
    public void setNewBlockState(BlockState newState) {

        try {
            if (newState == null) {
                getModel().setActivePatch(null);
                getModel().setBlockState(null);
                updatePatchPreviews(-1, -1);
                pushUpdates();
            } else {
                getModel().setBlockState(newState);

                BlockState	currentState = getModel().getBlockState();
                int			row          = currentState.getRow();
                int			column       = currentState.getColumn();

                saveScratchFile();

                ConResPatch	patchModel = getTargetManager().getPatch(row, column);

                getModel().setActivePatch(patchModel);
                updatePatchPreviews(row, column);

                pushUpdates();
            }
        } catch (Exception exception) {
            GrasppeKit.debugError("Setting Block State", exception, 2);
        }

    }

    /**
     * 	@param scratchEnabled
     */
    public void setScratchEnabled(boolean scratchEnabled) {
        getModel().setScratchEnabled(scratchEnabled);
    }
}
