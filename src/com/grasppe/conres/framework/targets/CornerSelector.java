/*
 * @(#)CornerSelector.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets;

import com.grasppe.conres.framework.imagej.CornerSelectorView;
import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
import com.grasppe.conres.framework.targets.model.TargetDimensions;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.framework.targets.model.roi.PatchSetROI;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.lure.components.AbstractController;

import ij.gui.PointRoi;
import ij.gui.Roi;
import ij.plugin.frame.RoiManager;

import org.apache.commons.io.FilenameUtils;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileNotFoundException;

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

    /**
     * Constructs and attaches a new controller and a new model.
     *  @param targetManager
     */
    public CornerSelector(TargetManager targetManager) {
        this(targetManager, new CornerSelectorModel());
        selectorView = new CornerSelectorView(this);
        getManagerModel().attachObserver(targetManager);

//      getManagerModel().attachObserver(this);
        attachView(selectorView);
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

//      model.getTargetDimensions(targetManager.)
        getManagerModel().attachObserver(this);
    }

    /**
     * @throws FileNotFoundException 
     */
    public void loadPatchCenterROIs() throws FileNotFoundException, InvalidActivityException {
    	
    	String roiFilePath = getPatchCenterROIFilePath();
    	// TODO: Check if can get a file path
    	if (roiFilePath==null || roiFilePath.trim().isEmpty()) throw new InvalidActivityException("Unable to determine a vlid path to load roi file");
    	
    	File roiFile = new File(roiFilePath);
    	
    	// TODO: Check that file exists
    	if (!roiFile.exists()) throw new FileNotFoundException(roiFilePath + " was not found");
    	
    	// TODO: now load ROIs
    	
        RoiManager	roiManager = new RoiManager(true);
        roiManager.runCommand("Open", roiFilePath);
        
        Roi[] fileROIs = roiManager.getRoisAsArray();
        
        if (fileROIs.length>0 && (fileROIs[0] instanceof PointRoi)) {
        		getModel().setPatchSetROI((PointRoi)fileROIs[0]);
        		if (fileROIs.length==2)
        			getModel().setBlockROI((PointRoi)fileROIs[1]);
        		//getModel().setOverlayROI(getModel().getPatchSetROI());
        		PatchSetROI patchSetROI = getModel().getPatchSetROI();
        		getSelectorView().setOverlayROI(patchSetROI);
        }

    	return;
    	
    }
    
    public PatchSetROI getPatchSetROI() {
    	try {
    		if (!isSelectionValid())
    			loadPatchCenterROIs();
    	} catch (Exception exception) {
    		exception.printStackTrace();
    	}
    	if (isSelectionValid())
    		return getModel().getPatchSetROI();
    	
    	return null;
    }
    
    public void loadImage() {
    	targetManager.loadImage();
    }

    /**
     */
    public void savePatchCenterROIs() {
        if (!isSelectionValid()) return;

        RoiManager	roiManager = new RoiManager(true);

        // TODO: Add points to manager
        roiManager.addRoi(getModel().getPatchSetROI());
        roiManager.addRoi(getModel().getBlockROI());

        roiManager.runCommand("Save", getPatchCenterROIFilePath());

    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractController#update()
     */

    /**
     */
    @Override
    public void update() {

//      if (isSelectionValid())
//          savePatchCenterROIs();
        getModel().setTargetImageFile(getBlockImage());
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

        // TODO: Determine roi filename
        if (getBlockImage() == null) return null;

        String	filename = FilenameUtils.getBaseName(getBlockImage().getName()) + ".roi.zip";

        return getBlockImage().getParentFile() + File.separator + filename;

    }

    /**
     * @return the selectorView
     */
    public CornerSelectorView getSelectorView() {
        return selectorView;
    }
    
    public void showSelectorView(){
    	getSelectorView().run("");
    	try {
    		getSelectorView().clearBlockPoints();
    		loadPatchCenterROIs();
    	} catch (Exception exception) {
    		exception.printStackTrace();
    	}
    }

    /**
     *  @return
     */
    public boolean isSelectionValid() {
    	if (getModel()==null) return false;
        return getModel().isValidSelection();
    }

    /**
     * @param selectorView the selectorView to set
     */
    public void setSelectorView(CornerSelectorView selectorView) {
        this.selectorView = selectorView;
    }
}
