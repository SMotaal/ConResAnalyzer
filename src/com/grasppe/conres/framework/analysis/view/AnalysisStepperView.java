/*
 * @(#)AnalysisStepperView.java   11/11/27
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.analysis.view;

import com.grasppe.conres.analyzer.view.ConResAnalyzerView;
import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.analysis.AnalysisStepper;
import com.grasppe.conres.framework.analysis.model.AnalysisManagerModel;
import com.grasppe.conres.framework.analysis.model.AnalysisStepperModel;
import com.grasppe.conres.framework.analysis.stepping.BlockState;
import com.grasppe.conres.framework.analysis.stepping.SteppingStrategy;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.framework.targets.model.grid.ConResPatch;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AnalysisStepperView extends AbstractView implements IChildView {

    protected JPanel	viewContainer = null;

//  protected SteppingPreview     canvas             = null;
    protected PatchBoundView		patchImagePanel       = null;
    protected BlockMapImagePanel	blockMapImagePanel    = null;
    protected PatchInformationPanel	patchInformationPanel = null;
    protected JLabel				label                 = null;
    int								dbg                   = 0;

    /**
     * @param controller
     */
    private AnalysisStepperView(AnalysisStepper controller) throws Exception {
        super(controller);
//        if (instance!=null) throw new Exception("What the f***!");
        instances.add(this);
        getModel().attachObserver(this);
    }
    
    protected static AnalysisStepperView instance = null;
    
    protected static ArrayList<AnalysisStepperView> instances = new ArrayList<AnalysisStepperView>();
    
    public static AnalysisStepperView getInstance(AnalysisStepper controller)  throws Exception {
    	if (instance==null || controller!=instance.getController()) {
    		if (instance!=null)
				try {
					instance.finalize();
				} catch (Throwable exception) {
					exception.printStackTrace();
				}
    		instance = new AnalysisStepperView(controller);
    	}
    	return instance;
    }

    /**
     */
    private void createView() {
    	
//    	SwingUtilities.invokeLater( new Runnable() {	public void run() {

        // Assemble Panel
        JPanel	panel = new JPanel();		// panel.setBackground(Color.DARK_GRAY);

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JPanel	previewPanel     = new JPanel(new BorderLayout());
        JPanel	informationPanel = new JPanel();

        informationPanel.setLayout(new BoxLayout(informationPanel, BoxLayout.Y_AXIS));

        int		pad = 1;
        Border	padBorder = BorderFactory.createMatteBorder(0, pad, pad, pad, panel.getBackground());
        Border	padLeft = BorderFactory.createMatteBorder(1, 2, 1, 1, panel.getBackground());
        Border	padRight = BorderFactory.createMatteBorder(1, 1, 1, 2, panel.getBackground());

        blockMapImagePanel = new BlockMapImagePanel(getModel());	// blockMapImagePanel.setBackground(Color.DARK_GRAY);
        patchInformationPanel = new PatchInformationPanel(getModel());		// patchInformationPanel.setBackground(Color.DARK_GRAY);
        patchImagePanel = new PatchImagePanel(getModel());

        patchImagePanel.setBackground(Color.DARK_GRAY);

        informationPanel.add(blockMapImagePanel);		// informationPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        informationPanel.add(patchInformationPanel);
        informationPanel.add(Box.createVerticalGlue());

//      informationPanel.setLayout(subLayout);

        previewPanel.add(patchImagePanel, BorderLayout.CENTER);
        previewPanel.setBackground(Color.DARK_GRAY);

        int			patchPreviewSize = getModel().getPatchPreviewSize();
        Dimension	previewDimension = new Dimension(patchPreviewSize, patchPreviewSize);

//      previewPanel.setMaximumSize(new Dimension(patchPreviewSize,patchPreviewSize));
        previewPanel.setPreferredSize(previewDimension);
        previewPanel.setSize(previewDimension);

        panel.add(previewPanel);
        panel.add(informationPanel);

//      panel.setLayout(layout);

        informationPanel.setBorder(padLeft);
        previewPanel.setBorder(padRight);

        // Assemble Frame

        viewContainer = new JPanel(new BorderLayout());

        viewComponents.add(viewContainer);


        // setFrameMenu(viewContainer);

//      viewContainer.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        viewContainer.add(panel, BorderLayout.CENTER);

//      viewContainer.setResizable(false);
//      viewContainer.pack();

//      GraphicsEnvironment   graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
//      int                   frameLeft           = graphicsEnvironment.getCenterPoint().x - viewContainer.getWidth() / 2;
//      int                   frameTop            = graphicsEnvironment.getCenterPoint().y - viewContainer.getHeight() / 2;
//
//      viewContainer.setLocation(frameLeft, frameTop);

        KeyListener	keyListener = new KeyAdapter() {

            public void keyPressed(KeyEvent ke) {
                if (ke.isConsumed()) return;
                if (getController().BlockStepKey(ke.getKeyCode(), ke.getModifiers())) ke.consume();
            }
        };
        viewContainer.addKeyListener(keyListener);
        viewContainer.setFocusable(true);
        setComponentFocus(viewContainer);
        
//        
//        for (Component component : viewContainer.getComponents()) {
//        	component.setFocusable(false);
//        }

        // viewContainer.addKeyListener(keyListener);
        try {
            getParentView().getFrame().removeKeyListener(keyListener);
        } catch (Exception exception) {

            // nothing to do!
        }

        getParentView().getFrame().setFocusTraversalKeysEnabled(false);
        getParentView().getFrame().addKeyListener(keyListener);
        getParentView().getFrame().setVisible(true);

        // Show Frame
        viewContainer.setVisible(true);

        getParentView().setContainer(viewContainer);

        update();
//    }});
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractView#finalize()
     */

    /**
     * 	@throws Throwable
     */
    @Override
    public void finalize() throws Throwable {
        if ((getParentView() != null) && (viewContainer != null))
            getParentView().removeContainer(viewContainer);
        super.finalize();
    }

    /**
     */
    @Override
    protected void finalizeUpdates() {

        // TODO Auto-generated method stub
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

//      if (canvas != null) canvas.updateStep();  // getModel().getBlockState())
        if (patchImagePanel != null) patchImagePanel.update();
        if (blockMapImagePanel != null) blockMapImagePanel.update();
        if (patchInformationPanel != null) patchInformationPanel.update();

        try {
            viewContainer.repaint();
            viewContainer.validate();
        } catch (Exception exception) {}
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
    public AnalysisStepperModel getModel() {
        return (AnalysisStepperModel)super.getControllerModel();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.framework.analysis.view.IChildView#getParentView()
     */

    /**
     * 	@return
     */
    public ConResAnalyzerView getParentView() {
        if (getController() == null) return null;
        if (getController().getAnalyzer() == null) return null;

        return getController().getAnalyzer().getView();
    }

    /**
     *  @return
     */
    public TargetManager getTargetManager() {
        return getAnalysisManager().getTargetManager();
    }

    /**
     * 	@param visible
     */
    public void setVisible(boolean visible) {
        if ((viewContainer != null) &&!visible) {

//          if(patchImagePanel!=null) patchImagePanel.setVisible(false);
////            if(patchImagePanel!=null) patchImagePanel.setVisible(false);
            viewContainer.setVisible(false);
            getParentView().removeContainer(viewContainer);

            return;
        }

        if (visible) createView();
    }

    /**
     * Class description
     *  @version        $Revision: 1.0, 11/11/27
     *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
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
//          this.image = (new BlockGrid(getBlockState())).getImage();
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
            int	dbg = 0;

//
//          BlockState    blockState = getBlockState();
//          BlockGrid  blockMap   = new BlockGrid(blockState);
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
    
    /**
     */
    @Override
    protected void updateDebugLabels() {
        AnalysisStepperModel	model = getModel();

        if (model == null) return;

        try {
            updateDebugLabel("activeBlock", model.getActiveBlock());
        } catch (Exception exception) {
            updateDebugLabel("activeBlock", "");
        }

        try {
            updateDebugLabel("activePatch", model.getActivePatch());
        } catch (Exception exception) {
            updateDebugLabel("activePatch", "");
        }
        try {
        	updateDebugLabel("instances", instances);
        } catch (Exception exception) {
        	updateDebugLabel("instances", "");
        }

//      updateDebugLabel("newCase", model.getNewCase());
//      updateDebugLabel("backgroundCase", model.getBackgroundCase());

        super.updateDebugLabels();
    }
}
