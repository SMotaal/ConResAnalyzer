/*
 * @(#)CornerSelector.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets;

import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
import com.grasppe.conres.framework.targets.model.TargetDimensions;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.framework.targets.model.roi.PatchSetROI;
import com.grasppe.conres.framework.targets.view.CornerSelectorView;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.lure.components.AbstractController;

import ij.gui.PointRoi;
import ij.gui.Roi;

import ij.plugin.frame.RoiManager;

import org.apache.commons.io.FilenameUtils;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
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
     *  @param pX
     *  @param pY
     *  @return
     */
    public static int euclideanDistance(int pX, int pY) {
        return (int)Math.round(Math.pow(Math.pow(pX, 2) + Math.pow(pY, 2), 0.5));
    }

    /**
     *  @param suffix
     *  @return
     */
    public String generateFilename(String suffix) {

        // TODO: Determine roi filename
        if (getBlockImage() == null) return null;

        String	imageName = FilenameUtils.getBaseName(getBlockImage().getName());
        String	fileName  = imageName.substring(0, imageName.length() - 1) + suffix;

        return getBlockImage().getParentFile() + File.separator + fileName;

    }

    /**
     */
    public void loadImage() {
        targetManager.loadImage();
    }

    /**
     * @throws FileNotFoundException
     *  @throws InvalidActivityException
     */
    public void loadPatchCenterROIs() throws FileNotFoundException, InvalidActivityException {

        String	roiFilePath = getPatchCenterROIFilePath();

        // TODO: Check if can get a file path
        if ((roiFilePath == null) || roiFilePath.trim().isEmpty())
            throw new InvalidActivityException("Unable to determine a vlid path to load roi file");

        File	roiFile = new File(roiFilePath);

        // TODO: Check that file exists
        if (!roiFile.exists()) throw new FileNotFoundException(roiFilePath + " was not found");

        // TODO: now load ROIs

        RoiManager	roiManager = new RoiManager(true);

        roiManager.runCommand("Open", roiFilePath);

        Roi[]	fileROIs = roiManager.getRoisAsArray();

        if ((fileROIs.length > 0) && (fileROIs[0] instanceof PointRoi)) {
            getModel().setPatchSetROI((PointRoi)fileROIs[0]);
            if (fileROIs.length == 2) getModel().setBlockROI((PointRoi)fileROIs[1]);

            // getModel().setOverlayROI(getModel().getPatchSetROI());
            PatchSetROI	patchSetROI = getModel().getPatchSetROI();

            getSelectorView().setOverlayROI(patchSetROI);
        }

        return;

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

    /**
     */
    public void showSelectorView() {
        getSelectorView().run("");

        try {
            getSelectorView().clearBlockPoints();
            loadPatchCenterROIs();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

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
        return generateFilename("i.roi.zip");
    }

    /**
     *  @return
     */
    public PatchSetROI getPatchSetROI() {
        try {
            if (!isSelectionValid()) loadPatchCenterROIs();
        } catch (Exception exception) {
            exception.printStackTrace();
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
}
