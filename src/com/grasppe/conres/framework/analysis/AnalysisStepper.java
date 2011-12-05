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
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.lure.components.AbstractController;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.KeyEvent;

/**
 * @author daflair
 */
public class AnalysisStepper extends AbstractController {

    /* (non-Javadoc)
	 * @see com.grasppe.lure.components.AbstractController#update()
	 */
	@Override
	public void update() {
		super.update();
	}
	
	public void updateActiveBlock() {
		
		
		getModel().setActiveBlock(getAnalysisManagerModel().getActiveBlock());
		
		int blockColumns = getActiveBlock().getXAxis().getValues().length;
		int blockRows = getActiveBlock().getYAxis().getValues().length;
		
        BlockState	blockState = new BlockState(blockRows, blockColumns, 0, 0);		// , BlockState.fudgeMap1());

        getModel().setBlockState(blockState);
		
		
	}
	
	public ConResBlock getActiveBlock() {
		return getModel().getActiveBlock();
	}
	
    int dbg = 3;

	protected AnalysisManager		analysisManager = null;
    protected AnalysisStepperView	stepperView     = null;

    /**
     * Constructs and attaches a new controller and a new model.
     *  @param analysisManager
     */
    public AnalysisStepper(AnalysisManager analysisManager) {
        this(analysisManager, new AnalysisStepperModel());
        getAnalysisManager().getModel().attachObserver(this);

//        BlockState	fudgeState = new BlockState(10, 10, 0, 0);		// , BlockState.fudgeMap1());
//
//        getModel().setBlockState(fudgeState);
        
        //updateActiveBlock();
        

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

        // if () return;

        // SmartBlockState smartState = new SmartBlockState(this.blockState);
        SmartBlockState		smartState = new SmartBlockState(getModel().getBlockState());
        SteppingStrategy	thisStep   = new StepNext(smartState);

        System.out.println("Modifier " + keyModifiers);

        boolean	snapState = true;
        boolean	goBack    = false;

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

        getModel().setBlockState(thisStep.getFinalState());
        getStepperView().update();

        // smartState = new SmartBlockState(thisStep.finalState);
//      this.blockState = thisStep.getFinalState();
//
//      BlockMap  blockMap = new BlockMap(this.blockState);
//
//      this.image = blockMap.getImage();
//      this.repaint(1000);
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
    public void testRun() {
        getStepperView().prepareView();
    }

    /**
     * @return the analysisManager
     */
    public AnalysisManager getAnalysisManager() {
        return analysisManager;
    }

    /**
     * 	@return
     */
    public AnalysisManagerModel getAnalysisManagerModel() {
        return getAnalysisManager().getModel();
    }

    //
    // /**
    // *  @param targetDefinitionFile
    // *  @throws Exception
    // */
    // public void loadTargetDefinitionFile(TargetDefinitionFile targetDefinitionFile)
    // throws Exception {
    //
    // // TODO: Create reader and read target from Case Manager current case
    // // TargetDefinitionFile file = targetDefinitionFile.getTargetDefinitionFile();
    // TargetDefinitionReader    reader = new TargetDefinitionReader(targetDefinitionFile);
    //
    // getModel().setActiveTarget(buildTargetModel(targetDefinitionFile));
    // }

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

//
//  /**
//   * Method description
//   *
//   *
//   * @param g
//   */
//  public void paint(Graphics g) {
//      int   iScale  = 50;
//      int   iWidth  = this.image.getWidth();
//      int   iHeight = this.image.getHeight();
//
//      g.drawImage(this.image, 0, 0, iWidth * iScale, iHeight * iScale, this);
//  }
}
