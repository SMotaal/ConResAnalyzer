	/*
	 * @(#)CornerSelector.java   11/11/26
	 * Copyright (c) 2011 Saleh Abdel Motaal
	 * This code is not licensed for use and is the property of it's owner.
	 */
	
	
	
	package com.grasppe.conres.framework.targets;
	
	import com.grasppe.conres.analyzer.ConResAnalyzer;
	import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
	import com.grasppe.conres.framework.targets.model.TargetDimensions;
	import com.grasppe.conres.framework.targets.model.TargetManagerModel;
	import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
	import com.grasppe.conres.framework.targets.model.roi.PatchSetROI;
	import com.grasppe.conres.framework.targets.view.CornerSelectorView;
	import com.grasppe.conres.io.model.ImageFile;
	import com.grasppe.lure.components.AbstractController;
	import com.grasppe.lure.framework.GrasppeKit;
	
	
	
	
	//~--- JDK imports ------------------------------------------------------------
	
	import java.awt.Container;
	import java.io.FileNotFoundException;
	
	import java.util.Arrays;
	import java.util.List;
	
	import javax.activity.InvalidActivityException;
	
	/**
	 * Class description
	 * @version $Revision: 0.1, 11/11/08
	 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
	 */
	public class CornerSelector extends AbstractController {
	
	    /** Field description */
	    public CornerSelectorView	selectorView = null;
	
	    /** Field description */
	    public TargetManager	targetManager = null;
	    
	    private ConResAnalyzer analyzer = null;
	
	    /**
	     * Constructs and attaches a new controller and a new model.
	     *  @param targetManager
	     */
	    public CornerSelector(TargetManager targetManager) {
	        this(targetManager, new CornerSelectorModel());
	        selectorView = new CornerSelectorView(this);
	        analyzer = targetManager.getAnalyzer();
	        getManagerModel().attachObserver(targetManager);
	
	//      getManagerModel().attachObserver(this);
	        attachView(selectorView);
	        
	        getAnalyzer().getCaseManager().getModel().attachObserver(this);
	    }
	    
	    /**
	     * @return the analyzer
	     */
	    public ConResAnalyzer getAnalyzer() {
	        return analyzer;
	    }
	
	    /**
	     * Constructs a new controller and attaches it to the unattached model.
	     *  @param targetManager
	     * @param model
	     */
	    private CornerSelector(TargetManager targetManager, CornerSelectorModel model) {
	        super(model);
	        this.targetManager = targetManager;
	
	        TargetDimensions	targetDimensions =
	            targetManager.getModel().getActiveTarget().getDimensions();
	
	        model.setTargetDimensions(targetDimensions);
	
	        getManagerModel().attachObserver(this);
	    }
	
	    /**
	     *  @param pX
	     *  @param pY
	     *  @return
	     */
	    public static int euclideanDistance(int pX, int pY) {
	        return (int)Math.round(Math.pow(Math.pow(pX, 2) + Math.pow(pY, 2), 0.5));
	    }
	
	    /**
	     */
	    public void loadImage() {
	        targetManager.loadImage();
	    }
	
	    /**
		 * @throws FileNotFoundException
		 *  @throws InvalidActivityException
		 * @deprecated Use {@link com.grasppe.conres.framework.targets.TargetManager#loadPatchCenterROIs(com.grasppe.conres.framework.targets.CornerSelector)} instead
		 */
		public void loadPatchCenterROIs() throws FileNotFoundException, InvalidActivityException {
			getTargetManager().loadPatchCenterROIs(this);
		}
	
	    /**
	     */
	    public void showSelectorView() {
	        getSelectorView().run("");
	
	        try {
	            getSelectorView().clearBlockPoints();
	            getTargetManager().loadPatchCenterROIs(this);
	        }  catch (Exception exception) {
	        	GrasppeKit.debugError("Displaying Corner Selector", exception, 5);
	        }
	    }
	    
	//    /**
	//     */
	//    public void refereshSelectorView() {
	//    	
	//    }
	
	    /**
	     *  @param xCoordinates
	     *  @param yCoordinates
	     *  @return points sorted in clockwise sequence
	     */
	    public static int[] sortRectangleROIIndex(int[] xCoordinates, int[] yCoordinates) {
	
	        // TODO: Confirm that xCoordinates and yCoordinates have 4 values
	
	        // TODO: Prepare Index & Value integers
	        int	dXYMin = 0, 	// minimum euclidean distance from origin
				dXYMax = 0, 	// maximum euclidean distance from origin
				d2XMin = 0,
				d2YMin = 0;
	        int	iXYMin = -1,	// top-left --> point 0;
				iXYMax = -1,	// bottom-right --> point 2;
				i2XMin = -1,	// bottom-left --> point 3;
	        	i2YMin = -1;	// top-right --> point 1;
	
	        // TODO: Find the origin point[0] and the extreme point[2] using dXY method
	        for (int p = 0; p < 4; p++) {
	            int		pX       = xCoordinates[p],
						pY       = yCoordinates[p];
	            int		dXY      = euclideanDistance(pX, pY);		// (int)Math.round(Math.pow(Math.pow(pX, 2) + Math.pow(pY, 2), 0.5));
	
	            if ((dXY < dXYMin) || (iXYMin == -1)) {
	                dXYMin   = dXY;
	                iXYMin   = p;
	            }
	
	            if ((dXY > dXYMax) || (iXYMax == -1)) {
	                dXYMax   = dXY;
	                iXYMax   = p;
	            }
	        }
	
	        // TODO: Sort out top-right point[1] and bottom-left point[3]
	        for (int p = 0; p < 4; p++) {
	        	if (p==iXYMin || p==iXYMax) continue;
	        	
	            int	pX = xCoordinates[p],
					pY = yCoordinates[p];
	
	            if (i2XMin == -1) {	// First mid point
	                d2XMin = pX;
	                d2YMin = pY;
	                i2XMin = p;
	                i2YMin = p;
	                continue;
	            }
	
	            if ((pX < d2XMin) && (pY > d2YMin)) {	// Second mid point is bottom-left
	                d2XMin = pX;
	                i2XMin = p;
	            } else if ((pX > d2XMin) && (pY < d2YMin)) {	// Second mid point is top-right
	                d2YMin = pY;
	                i2YMin = p;
	            } else {				// This should not happen if valid points
	                return new int[] {};
	            }
	        }
	        
	        // TODO: Return index array
	        return new int[]{iXYMin, i2YMin, iXYMax, i2XMin};	// sortedIndex
	
	    }
	
	    
	
	    /*
	     *  (non-Javadoc)
	     * @see com.grasppe.lure.components.AbstractController#update()
	     */
	    
	    public void hideSelectorView() {
	    	getSelectorView().setVisible(false);
	    }
	
	    /**
	     */
	    @Override
	    public void update() {
	    	
	
	        ConResBlock activeBlock =  getTargetManager().getModel().getActiveBlock();
	        
	        
	        try {
	        	if (activeBlock==null  || getModel().getImagePlus()==null)
	        		hideSelectorView();
	        } catch (Exception exception) {}
	        
	        ImageFile blockImage =  getBlockImage();
	        ImageFile modelImage = getModel().getTargetImageFile(); 
	        CornerSelectorView view = getSelectorView();
	        if (blockImage!=modelImage && view!=null && view.getImageWindow()!=null) // && view.getImageWindow().isVisible())
	        		showSelectorView();
	    	if (getBlockImage()!=null)
	    		getModel().setTargetImageFile(getBlockImage());
	    	else
	    		getModel().setTargetImageFile(null);
	    	
	    	
	        super.update();
	    }
	
	    /**
	     *  @param model
	     */
	    protected static void validateSelection(CornerSelectorModel model) {
	        try {
	            boolean	validTDF       = model.getTargetImageFile().validate();
	            boolean	validImage     = model.getTargetImageFile().validate();
	            boolean	validCornerROI = model.getBlockROI().validate();
	            boolean	validCenterROI = model.getPatchSetROI().validate();
	            boolean	validSelection = validTDF && validImage && validCornerROI && validCenterROI;
	
	            model.setValidSelection(validSelection);
	        } catch (Exception exception) {
	            model.setValidSelection(false);
	        }
	
	    }
	
	    /**
	     *  @return
	     */
	    public ImageFile getBlockImage() {
	        return targetManager.getBlockImage();
	    }
	
	    /**
	     *  @return
	     */
	    public TargetManagerModel getManagerModel() {
	        return targetManager.getModel();
	    }
	
	    /**
	     * Returns a correctly-cast model.
	     * @return
	     */
	    public CornerSelectorModel getModel() {
	        return (CornerSelectorModel)model;
	    }
	
	    /**
	     *  @return
	     */
	    public String getPatchCenterROIFilePath() {
	        return getTargetManager().generateFilename("i.roi.zip","Resources");
	    }
	
	    /**
	     *  @return
	     */
	    public PatchSetROI getPatchSetROI() {
	        try {
	            if (!isSelectionValid()) getTargetManager().loadPatchCenterROIs(this);
	        } catch (FileNotFoundException exception) {
	        	GrasppeKit.debugError("Loading ROI File", exception, 5);
	        } catch (InvalidActivityException exception) {
	        	GrasppeKit.debugError("Loading ROI File", exception, 5);
	        } catch (Exception exception) {
	        	GrasppeKit.debugError("Loading ROI File", exception, 2);
	        }
	
	        if (isSelectionValid()) return getModel().getPatchSetROI();
	
	        return null;
	    }
	
	    /**
	     * @return the selectorView
	     */
	    public CornerSelectorView getSelectorView() {
	        return selectorView;
	    }
	
	    /**
	     *  @return
	     */
	    public boolean isSelectionValid() {
	        if (getModel() == null) return false;
	
	        return getModel().isValidSelection();
	    }
	
	    /**
	     * @param selectorView the selectorView to set
	     */
	    public void setSelectorView(CornerSelectorView selectorView) {
	        this.selectorView = selectorView;
	    }
	    
	    /**
	     *  @return
	     */
	    public TargetManager getTargetManager() {
	        return targetManager;
	    }
	}
