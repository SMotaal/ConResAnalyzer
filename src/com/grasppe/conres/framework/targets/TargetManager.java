/*
 * @(#)TargetManager.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseModel;
import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.framework.targets.model.grid.ConResPatch;
import com.grasppe.conres.framework.targets.model.grid.ConResTarget;
import com.grasppe.conres.framework.targets.model.grid.GridBlock;
import com.grasppe.conres.framework.targets.model.roi.PatchSetROI;
import com.grasppe.conres.framework.targets.operations.MarkBlock;
import com.grasppe.conres.framework.targets.operations.SelectBlock;
import com.grasppe.conres.framework.targets.view.TargetManagerView;
import com.grasppe.conres.io.TargetDefinitionReader;
import com.grasppe.conres.io.model.IConResTargetDefinition;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.conres.io.model.TargetDefinitionFile;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.IAuxiliaryCaseManager;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.morie.units.AbstractValue;
import com.grasppe.morie.units.spatial.SpatialFrequency;
import com.grasppe.morie.units.spatial.resolution.DotsPerInch;

import ij.ImagePlus;

import ij.gui.PointRoi;
import ij.gui.Roi;
import ij.io.Opener;

import ij.plugin.frame.RoiManager;

import org.apache.commons.io.FilenameUtils;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Image;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;

import java.util.LinkedHashMap;

import javax.activity.InvalidActivityException;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class TargetManager extends AbstractController implements IAuxiliaryCaseManager {

    /** Field description */
    public ConResAnalyzer	analyzer;

    /** Field description */
    public TargetManagerModel	backgroundModel = null;

    /** Field description */
    public CornerSelector	cornerSelector = null;
    int						dbg            = 0;
    
    public int	patchPreviewSize = 750;

    /**
     * @param listener
     */
    private TargetManager(ActionListener listener) {
        super(listener);
    }

    /**
     *  @param analyzer
     */
    public TargetManager(ConResAnalyzer analyzer) {
        this((ActionListener)analyzer);
        attachView(new TargetManagerView(this));
        setAnalyzer(analyzer);
        analyzer.getCaseManager().getModel().attachObserver(this);
    }

    /**
     *  @throws IllegalAccessException
     */
    public void backgroundCurrentCase() throws IllegalAccessException {
        getModel().backgroundCurrentTarget();
        getModel().notifyObservers();
    }

    /**
     *  @param targetDefinition
     *  @return
     */
    public static ConResTarget buildTargetModel(IConResTargetDefinition targetDefinition) {

//      return new ConResTarget(targetDefinition.getBlockToneValues(),
//                              targetDefinition.getMeasurements().getYValues(),
//                              targetDefinition.getMeasurements().getXValues());
        return new ConResTarget(targetDefinition.getMeasurements());
    }

    /**
     * Create and populate all commands from scratch.
     */
    @Override
    public void createCommands() {
        commands = new LinkedHashMap<String, AbstractCommand>();
        putCommand(new SelectBlock(this, this));
        putCommand(new MarkBlock(this, this));
    }

    /**
     */
    public void discardBackgroundCase() {
        getModel().discardBackgroundTarget();
    }

    /**
     *  @param suffix
     *  @return
     */
    public String generateFilename(String suffix) {

        if (getBlockImage() == null) return null;

        String	imageName = FilenameUtils.getBaseName(getBlockImage().getName());
        String	fileName  = imageName.substring(0, imageName.length() - 1) + suffix;

        return getBlockImage().getParentFile() + File.separator + fileName;

    }
    
    protected String loadedImagePath = "";

    /**
     */
    public void loadImage() {
    	
    	if (getBlockImage()!=null && getBlockImage().getAbsolutePath()!=loadedImagePath) {
	        try {
	            loadPatchCenterROIs(getCornerSelector());
	        } catch (FileNotFoundException exception) {
	        	GrasppeKit.debugError("Loading ROI File", exception, 5);	            
	        } catch (Exception exception) {
	        	GrasppeKit.debugError("Loading ROI File", exception, 2);
	        }
	
	        Opener		opener    = new Opener();
	        ImagePlus	imagePlus = opener.openImage(getBlockImage().getAbsolutePath());
	
	        getCornerSelectorModel().setImagePlus(imagePlus);
	        loadedImagePath = getBlockImage().getAbsolutePath();
	        
	        getModel().notifyObservers();
    	}

    }
    
    /**
     * @param activeBlock the activeBlock to set
     */
    public void setActiveBlock(ConResBlock activeBlock) {
        try {
        	if (getModel().getActiveBlock()!=null && getModel().getActiveBlock()==activeBlock)
        		return;
        	
        	getModel().setActiveBlock(activeBlock);
            if (activeBlock!=null)
            	loadImage();
            
        	getCornerSelector().update(); //hideSelectorView();
        } catch (Exception exception) {
        	GrasppeKit.debugError("Setting Active Block", exception, 2);
        }
        getModel().notifyObservers();
    }


    /**
     *  @param targetDefinitionFile
     *  @throws IOException
     */
    public void loadTargetDefinitionFile(TargetDefinitionFile targetDefinitionFile)
            throws IOException {

        // TODO: Create reader and read target from Case Manager current case
        // TargetDefinitionFile file = targetDefinitionFile.getTargetDefinitionFile();
    	try {
        TargetDefinitionReader	reader = new TargetDefinitionReader(targetDefinitionFile);
    	} catch (NullPointerException exception) {
    		IOException ioException = new IOException("Could not load a target definition file.", exception);
    		GrasppeKit.debugError("Loading Target Definition File",ioException,5);
    		throw ioException;
    	}

    }

    /**
     *  @throws IllegalAccessException
     */
    public void restoreBackgroundCase() throws IllegalAccessException {
        getModel().restoreBackgroundTarget();
    }

    /**
     * @param cornerSelector TODO
     */
    public static void savePatchCenterROIs(CornerSelector cornerSelector) {		// if (!cornerSelector.isSelectionValid()) return;

        RoiManager	roiManager = new RoiManager(true);

        roiManager.addRoi(cornerSelector.getModel().getPatchSetROI());
        roiManager.addRoi(cornerSelector.getModel().getBlockROI());

        roiManager.runCommand("Save", cornerSelector.getPatchCenterROIFilePath());

    }

    /**
     *  @return
     */
    public CaseModel getActiveCase() {
        return getCaseManager().getModel().getCurrentCase();
    }

    /**
     * @return the analyzer
     */
    public ConResAnalyzer getAnalyzer() {
        return analyzer;
    }

    /**
     *  @return
     */
    public ImageFile getBlockImage() {
    	if (getModel().getActiveBlock()==null) return null;
        return getBlockImage(getModel().getActiveBlock());
    }

    /**
     *  @param block
     *  @return
     */
    public ImageFile getBlockImage(ConResBlock block) {
    	if (block==null) return null;
        return getBlockImage(new Double(block.getZValue().value).intValue());
    }

    /**
     *  @param toneValue
     *  @return
     */
    public ImageFile getBlockImage(int toneValue) {
        if (getActiveCase() == null) return null;

        return getActiveCase().caseFolder.getImageFile(toneValue);
    }

    /**
     *  @return
     */
    public CaseManager getCaseManager() {
        return getAnalyzer().getCaseManager();
    }

    /**
     *  @return
     */
    public CornerSelector getCornerSelector() {
        if (cornerSelector == null) {
        	cornerSelector = new CornerSelector(this);
        	getModel().attachObserver(cornerSelector);
        }
        return cornerSelector;
    }

    /**
     *  @return
     */
    private CornerSelectorModel getCornerSelectorModel() {

//      if (cornerSelector == null) return null;
        return getCornerSelector().getModel();
    }

    /**
     *  @return
     */
    public int getFirstColumnIndex() {

        // AbstractValue[] blockValues = getModel().getActiveTarget().getzAxis().getValues();//getCornerSelectorModel().getTargetDefinitionFile().getBlockToneValues();
        AbstractValue[]	contrastValues = getModel().getActiveBlock().getXAxis().getValues();
        int				blockValue     = (int)getModel().getActiveBlock().getZValue().getValue();

        int				i              = 0;

        for (i = 0; i < contrastValues.length; i++) {
            int	columnValue = (int)Math.round(contrastValues[i].getValue() / 2);
            int	lowValue    = blockValue - columnValue;
            int	highValue   = blockValue + columnValue;

            if ((lowValue >= 0) && (highValue <= 100)) return i;
        }

        return i;
    }

    /**
     *  @return
     */
    private ImagePlus getImagePlus() {
        CornerSelectorModel	model = getCornerSelectorModel();

//      if (model==null) model = getCornerSelector().getModel();
//      if (model==null) return null;
        try {
            if (model.getImagePlus() == null) loadImage();
        } catch (Exception exception) {
        	GrasppeKit.debugError("Getting Block ImagePlus", exception, 2);
            return null;
        }

        return getCornerSelectorModel().getImagePlus();

    }

    /**
     * Returns a correctly-cast model.
     * @return
     */
    public TargetManagerModel getModel() {
        return (TargetManagerModel)model;
    }

    /**
     * @return
     */
    @Override
    protected TargetManagerModel getNewModel() {
        GrasppeKit.debugText(getClass().getSimpleName(), "Getting new Model", dbg);

        return new TargetManagerModel();
    }

    /**
     *  @param row
     *  @param column
     *  @return
     */
    public ConResPatch getPatch(int row, int column) {

        int	dbg = 0;

        try {
            getModel().getActiveBlock().setActivePatch(row, column);

            return getModel().getActiveBlock().getPatch(row, column);
        } catch (Exception exception) {
            GrasppeKit.debugError("Getting Patch", exception, 2);

            return null;
        }
    }
    
    
    /**
     * @return the maximum scale factor (below 1.0) used to render patch image previews
     */
    public double getMaximumScaleFactor() {
    	double scaleFactor = 1.0;
    	double maximumDPIValue = 1200.0;
    	ImageFile blockImage=null;
    	String blockImageString = "";
    	if (getBlockImage()!=null) {
    		try {
    			blockImage = getBlockImage();
    			blockImageString = "" + blockImage.getWidth() + "x" + blockImage.getHeight() ;
    			SpatialFrequency imageResolution = blockImage.getResolution();
				DotsPerInch imageDPI = new DotsPerInch(imageResolution.getValue());
				scaleFactor = maximumDPIValue/imageDPI.getValue();
			} catch (Exception exception) {
				exception.printStackTrace();
				GrasppeKit.debugError("Getting Maximum Scale", exception, 2);
			}
    	}
    	GrasppeKit.debugText("Maximum Scale Factor", blockImageString + " @ " + scaleFactor, dbg);
    	return Math.min(scaleFactor, 1.0);
    }

    /**
     *  @param row
     *  @param column
     *  @return
     */
    public Image getPatchImage(int row, int column) {
    	
    	if (getModel().getActiveBlock()==null) return null;

        int	dbg = 0;

        try {

            int			stepsY      = getCornerSelectorModel().getTargetDimensions().getYCenters().length;
            int			cI          = (column * stepsY) + row;

            PatchSetROI	patchSetROI = getPatchSetROI();
            Polygon		polygon     = patchSetROI.getPolygon();
            int			cX          = polygon.xpoints[cI];
            int			cY          = polygon.ypoints[cI];

//          int           cW          = (int)(polygon.xpoints[1] - polygon.xpoints[0]);//getCornerSelectorModel().getTargetDimensions().getXSpan() * 1.15);
            int	cH = (int)((polygon.ypoints[1] - polygon.ypoints[0]) * 1.5);

            cH = (int)Math.round(Math.max(cH, patchPreviewSize)/getMaximumScaleFactor());

            int	cW = cH;

            int	x1 = cX - (cW / 2),
				x2 = cX + (cW / 2);
            int	y1 = cY - cH / 2,
				y2 = cY + cH / 2;

            GrasppeKit.debugText("Get Patch Image",
                                 "Patch: " + row + ", " + column + "\tCenter:" + cX + ", " + cY
                                 + "\tCorner:" + x1 + ", " + y1);

            ImagePlus	imagePlus = getImagePlus();

            if (imagePlus == null) return null;

            Image	image = imagePlus.getImage();

            if (image == null) return null;

            ImageProducer	patchImageProducer = new FilteredImageSource(image.getSource(),
                                                   new CropImageFilter(x1, y1, cW, cH));

            Toolkit	toolkit    = Toolkit.getDefaultToolkit();

            Image	patchImage = toolkit.createImage(patchImageProducer);

            return patchImage;

        } catch (Exception exception) {
            GrasppeKit.debugError("Getting Patch Image", exception, 2);
        }

        return null;
    }

    /**
     *  @return
     */
    public PatchSetROI getPatchSetROI() {
        if (cornerSelector == null) return null;

        return cornerSelector.getPatchSetROI();		// PatchSetROI patchSetROI =

    }

    /**
     * @param analyzer the analyzer to set
     */
    public void setAnalyzer(ConResAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     *  @param targetDefinitionFile
     */
    public void setTargetDefinitionFile(TargetDefinitionFile targetDefinitionFile) {
        getModel().setActiveTarget(buildTargetModel(targetDefinitionFile));
    }

	/**
	 * @throws FileNotFoundException
	 *  @param cornerSelector TODO
	 * @throws InvalidActivityException
	 */
	public void loadPatchCenterROIs(CornerSelector cornerSelector) throws FileNotFoundException, InvalidActivityException {
	
	    String	roiFilePath = cornerSelector.getPatchCenterROIFilePath();
	
	    // TODO: Check if can get a file path
	    if ((roiFilePath == null) || roiFilePath.trim().isEmpty())
	        throw new InvalidActivityException("Could not determine valid path for ROI file.");
	
	    File	roiFile = new File(roiFilePath);
	
	    // TODO: Check that file exists
	    if (!roiFile.exists()) throw new FileNotFoundException(roiFilePath + " was not found.");
	
	    // TODO: now load ROIs
	
	    RoiManager	roiManager = new RoiManager(true);
	
	    roiManager.runCommand("Open", roiFilePath);
	
	    Roi[]	fileROIs = roiManager.getRoisAsArray();
	
	    if ((fileROIs.length > 0) && (fileROIs[0] instanceof PointRoi)) {
	        cornerSelector.getModel().setPatchSetROI((PointRoi)fileROIs[0]);
	        if (fileROIs.length == 2) cornerSelector.getModel().setBlockROI((PointRoi)fileROIs[1]);
	
	        // getModel().setOverlayROI(getModel().getPatchSetROI());
	        PatchSetROI	patchSetROI = cornerSelector.getModel().getPatchSetROI();
	
	        cornerSelector.getSelectorView().setOverlayROI(patchSetROI);
	    }
	
	    return;
	
	}
}
