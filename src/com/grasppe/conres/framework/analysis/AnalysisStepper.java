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
import com.grasppe.conres.framework.analysis.stepping.BlockGrid;
import com.grasppe.conres.framework.analysis.stepping.BlockState;
import com.grasppe.conres.framework.analysis.stepping.SetAndStep;
import com.grasppe.conres.framework.analysis.stepping.SmartBlockState;
import com.grasppe.conres.framework.analysis.stepping.StepBack;
import com.grasppe.conres.framework.analysis.stepping.StepDown;
import com.grasppe.conres.framework.analysis.stepping.StepLeft;
import com.grasppe.conres.framework.analysis.stepping.StepOver;
import com.grasppe.conres.framework.analysis.stepping.StepRight;
import com.grasppe.conres.framework.analysis.stepping.StepUp;
import com.grasppe.conres.framework.analysis.stepping.SteppingStrategy;
import com.grasppe.conres.framework.analysis.view.AnalysisStepperView;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.conres.preferences.PreferencesAdapter;
import com.grasppe.conres.preferences.PreferencesAdapter.AnalysisPreferences;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.KeyCode;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 * @author daflair
 */
public class AnalysisStepper extends AbstractController {

    int								dbg             = 0;
    protected AnalysisManager		analysisManager = null;
    protected AnalysisStepperView	stepperView     = null;
    protected ConResBlock			lastBlock       = null;
    protected BlockState			loadBlockState  = null;
    protected ActionListener		blinkListener   = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            blinkCursor();
        }

    };
    protected Timer		blinkTimer  = new Timer(350, blinkListener);
    protected String	scratchFile = null;

    /**
     * Constructs and attaches a new controller and a new model.
     *  @param analysisManager
     */
    public AnalysisStepper(AnalysisManager analysisManager) {
        this(analysisManager, new AnalysisStepperModel());

        getModel().setPatchPreviewSize(getTargetManager().patchPreviewSize);

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

        try {
            stepperView = AnalysisStepperView.getInstance(this);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        attachView(stepperView);

        getAnalyzer().getCaseManager().getModel().attachObserver(this);
    }

    /**
     * @param keyCodeValue
     * @param keyModifiers
     *  @return
     */
    public boolean BlockStepKey(int keyCodeValue, int keyModifiers) {		// SteppingStrategy thisStep) {

        SmartBlockState	smartState = new SmartBlockState(getModel().getBlockState());

        if ((smartState == null) || (smartState.getBlockMap() == null)) return false;

        SteppingStrategy	thisStep        = null;		// new StepNext(smartState);

        boolean				snapState       = true;
        boolean				goBack          = false;

        double				scaleMultiplier = 1.15;

        KeyCode				keyCode         = KeyCode.get(keyCodeValue);

        switch (keyCode) {

        case VK_EQUALS :
            getModel().setScaleRatio(getModel().getScaleRatio() * scaleMultiplier);
            break;

        case VK_MINUS :
            getModel().setScaleRatio(getModel().getScaleRatio() / scaleMultiplier);
            break;

        case VK_0 :
            getModel().setScaleRatio(2.5);
            break;

        case VK_1 :
            getModel().setScaleRatio(1);
            break;

        case VK_2 :
            getModel().setScaleRatio(1.5);
            break;

        case VK_3 :
            getModel().setScaleRatio(2);
            break;

        case VK_4 :
            getModel().setScaleRatio(2.5);
            break;

        case VK_5 :
            getModel().setScaleRatio(3);
            break;

        case VK_6 :
            getModel().setScaleRatio(3.5);
            break;

        case VK_7 :
            getModel().setScaleRatio(4);
            break;

        case VK_8 :
            getModel().setScaleRatio(4.5);
            break;

        case VK_9 :
            getModel().setScaleRatio(5);
            break;

        case VK_SPACE :
            if (keyModifiers == 1) {
                goBack = true;
            } else
                thisStep = new StepOver(smartState);

            break;

        case VK_BACK_SPACE :
            goBack = true;
            break;

        case VK_UP :
            if (keyModifiers == 0) thisStep = new StepUp(smartState);
            break;

        case VK_DOWN :
            if (keyModifiers == 0) thisStep = new StepDown(smartState);

            break;

        case VK_LEFT :
            if (keyModifiers == 0) thisStep = new StepLeft(smartState);

            break;

        case VK_RIGHT :
            if (keyModifiers == 0) thisStep = new StepRight(smartState);
            
            break;

//
//          break;
//
//        case VK_G :
//
//          break;
//
//        case VK_W :
//
//          break;
//
//        case VK_R :
//
//          break;
//
//        case VK_C :
//          break;

        default :
            if (keyCode == PreferencesAdapter.getKeyCode(AnalysisPreferences.PASS_KEYCODE)) {
                if (keyModifiers == 0) thisStep = new SetAndStep(smartState, BlockState.PASS);
            } else if (keyCode == PreferencesAdapter.getKeyCode(AnalysisPreferences.FAIL_KEYCODE)) {
                if (keyModifiers == 0) thisStep = new SetAndStep(smartState, BlockState.FAIL);
            } else if (keyCode
                       == PreferencesAdapter.getKeyCode(AnalysisPreferences.MARGINAL_KEYCODE)) {
                if (keyModifiers == 0) thisStep = new SetAndStep(smartState, BlockState.MARGINAL);
            } else if (keyCode
                       == PreferencesAdapter.getKeyCode(AnalysisPreferences.CLEAR_COLUMN_KEYCODE)) {
                if (keyModifiers == 0) thisStep = new SetAndStep(smartState, BlockState.CLEAR);
            } else
                return false;
        }

        try {
            if (goBack) {
                if (!getModel().getHistory().isEmpty()) {
                    thisStep = new StepBack(smartState, getModel().getHistory());
                    getModel().getHistory().remove(getModel().getHistory().size() - 1);
                    snapState = false;
                }
            }

            if (snapState) {
                int		row          = getModel().getBlockState().getRow();
                int		column       = getModel().getBlockState().getColumn();
                int[]	currentState = { row, column };

                getModel().getHistory().add(currentState);
            }

            if (thisStep != null)		// thisStep = new StepNext(smartState);
                thisStep.execute();

            setNewBlockState(thisStep.getFinalState());

        } catch (Exception exception) {
            GrasppeKit.debugError("Handling Stepper Key Event", exception, 2);
            update();
        }

        return true;
    }

    /**
     */
    public synchronized void blinkCursor() {
        try {
            BlockGrid.blinkValue = !BlockGrid.blinkValue;
            getModel().setImage(new BlockGrid(getModel().getBlockState()).getImage());
            pushUpdates();
        } catch (Exception exception) {
            blinkTimer.stop();
        }
    }

    /**
     */
    public void finalizeLoading() {
        if (loadBlockState != null) {
            loadBlockFiles(loadBlockState);
            setNewBlockState(loadBlockState);
            loadBlockState = null;
        }

        setScratchEnabled(true);

//      updatePatchPreviews();
        getModel().notifyObservers();
    }

    /**
     *  @param blockState
     */
    public void loadBlockFiles(BlockState blockState) {
        getTargetManager().loadImage();

//      setScratchEnabled(false);

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
     */
    public void loadBlockResources() {}

    /**
     *  @param blockState
     *      @throws Exception
     */
    public void loadCSVFile(BlockState blockState) throws Exception {
        String	filename = getTargetManager().generateFilename("a.csv", "Data");

        blockState.readFile(filename);
    }

    /**
     *  @param blockState
     *      @throws Exception
     */
    public void loadScratchFile(BlockState blockState) throws Exception {
        String	filename = getTargetManager().generateFilename("s.csv", "Resources");

        blockState.readFile(filename);

        scratchFile = filename;

        updatePatchPreviews();
        setScratchEnabled(true);
    }

    /**
     */
    public void saveScratchFile() {
        if (!isScratchEnabled()) return;

        try {
            String	filename = getTargetManager().generateFilename("s.csv", "Resources");

            getModel().getBlockState().writeFile(filename);
        } catch (Exception exception) {
            GrasppeKit.debugError("Saving Analysis Scratch", exception, 2);
        }
    }

    /**
     */
    public void showView() {
        getTargetManager().loadImage();
        updateActiveBlock();
        update();
        getStepperView().setVisible(true);
        finalizeLoading();
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

            // Determine the commandMenu of transparency of the new buffered image
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

        if (getAnalyzer().getCaseManager().getModel().hasCurrentCase() == false)
            getStepperView().setVisible(false);
    }

    /**
     */
    public void updateActiveBlock() {

        if (loadBlockState != null) {
            finalizeLoading();

            return;
        }

        ConResBlock	activeBlock = getTargetManager().getModel().getActiveBlock();

        if (activeBlock == null) {
            getModel().setBlockImage(null);
            getModel().setActiveBlock(null);
            getModel().setBlockImagePlus(null);
            setNewBlockState(null);
            setScratchEnabled(false);

            return;
        }

        if (activeBlock == lastBlock) {

//          forceUpdates();
            updatePatchPreviews();

            return;
        }

        scratchFile = null;

//      setScratchEnabled(false);

        if (getTargetManager().getBlockImage() == null) return;

        ImageFile	imageFile       = getTargetManager().getBlockImage();

        double		imageDPI        = imageFile.getResolution().value;

        double		resolutionRatio = imageDPI / getTargetManager().displayDPI;

        getModel().setResolutionRatio(resolutionRatio);
        getModel().setBlockImage(getTargetManager().getBlockImage());
        getModel().setBlockImagePlus(getTargetManager().getModel().getActiveImagePlus());		// .getImagePlus());
        getModel().setActiveBlock(getTargetManager().getModel().getActiveBlock());

        int			blockColumns = getActiveBlock().getXAxis().getValues().length;
        int			blockRows    = getActiveBlock().getYAxis().getValues().length;

        int			firstColumn  = getTargetManager().getFirstColumnIndex();

        BlockState	blockState   = new BlockState(blockRows, blockColumns, firstColumn);	// , BlockState.fudgeMap1());

        blockState.setColumn(0);
        blockState.setRow(0);

        getTargetManager().loadImage();
        setScratchEnabled(false);

        loadBlockState = blockState;

//      finalizeLoading();

        lastBlock = activeBlock;

//      updatePatchPreviews();
//      getModel().notifyObservers();
//      }

        // AnalysisStepperModel model = getModel();
//      updatePatchPreviews(); //getModel().getActivePatch().getPatchRow(),getModel().getActivePatch().getPatchColumn());
//      getStepperView().update();
//      getStepperView().update();

    }

    /**
     */
    public void updatePatchPreviews() {
        try {
            getModel().setBlockImagePlus(getTargetManager().getModel().getActiveImagePlus());		// .getImagePlus());
            updatePatchPreviews(getModel().getActivePatch().getPatchRow(),
                                getModel().getActivePatch().getPatchColumn());
        } catch (Exception exception) {
            GrasppeKit.debugError("Updating Patch Preview (using model position)", exception, 2);
        }
    }

    /**
     *  @param row
     *  @param column
     */
    public void updatePatchPreviews(int row, int column) {
        try {

//          if ((row < 0) || (column < 0)) {
//              getModel().setPatchImage(null);
//              getModel().setImage(null);
//
//              return;
//          }

//          Image patchImage = getTargetManager().getPatchImage(row, column);
//
//          getModel().setPatchImage(patchImage);

            BlockGrid	blockGrid = new BlockGrid(getModel().getBlockState());

            getModel().setImage(blockGrid.getImage());

            blinkTimer.start();

        } catch (Exception exception) {
            GrasppeKit.debugError("Updating Patch Preview (using selected position)", exception, 2);
        }

        pushUpdates();
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
    public ConResAnalyzer getAnalyzer() {
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
     *  @return
     */
    public boolean isScratchEnabled() {

        return scratchFile != null;

        // return getModel().isScratchEnabled();
    }

    /**
     *  @param newState
     */
    public void setNewBlockState(BlockState newState) {

        try {
            if (newState == null) {
                getModel().setActivePatch(null);
                getModel().setBlockState(null);
                pushUpdates();
            } else {

//              if (newState != getModel().getBlockState()) {
                getModel().setBlockState(newState);

                int	row    = newState.getRow();
                int	column = newState.getColumn();

                saveScratchFile();

                getModel().setBlockImage(getTargetManager().getBlockImage());
                getModel().setActivePatch(getTargetManager().getPatch(row, column));
                getModel().setPatchDimensions(getTargetManager().getPatchDimensions(row, column));
                getModel().setPatchPreviewSize(getTargetManager().patchPreviewSize);

//              getModel().setImageDPI(getTargetManager().imageDPI);
//              getModel().setDisplayDPI(getTargetManager().displayDPI);
//              getModel().setResolutionRatio(getTargetManager().dpiRatio);
//              getModel().setScaleRatio(getTargetManager().scaleRatio);
//              getModel().setWindowRatio(getTargetManager().windowRatio);
//                }

                updatePatchPreviews(row, column);

                pushUpdates();
            }
        } catch (Exception exception) {
            GrasppeKit.debugError("Setting Block State", exception, 2);
        }

    }

    /**
     *  @param scratchEnabled
     */
    public void setScratchEnabled(boolean scratchEnabled) {
        getModel().setScratchEnabled(scratchEnabled);
    }
}
