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
import com.grasppe.conres.framework.targets.model.grid.ConResTarget;
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

import ij.ImagePlus;

import ij.io.Opener;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.LinkedHashMap;

import javax.activity.InvalidActivityException;
import javax.imageio.ImageIO;

/**
 * Class description
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public class TargetManager extends AbstractController implements IAuxiliaryCaseManager {

    /** Field description */
    public ConResAnalyzer	analyzer;

    /** Field description */
    public TargetManagerModel	backgroundModel = null;

    /** Field description */
    public CornerSelector	cornerSelector = null;
    int						dbg            = 3;

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
     */
    public void loadImage() {

//      try {
    	try {
    		getCornerSelector().loadPatchCenterROIs();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	
        Opener		opener    = new Opener();
        ImagePlus	imagePlus = opener.openImage(getBlockImage().getAbsolutePath());

        getCornerSelectorModel().setImagePlus(imagePlus);
        

//      } catch (Exception exception) {
//          exception.printStackTrace();
//      }
    }

    /**
     *  @throws IllegalAccessException
     */
    public void backgroundCurrentCase() throws IllegalAccessException {
        getModel().backgroundCurrentTarget();
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
     *  @param targetDefinitionFile
     *  @throws IOException
     */
    public void loadTargetDefinitionFile(TargetDefinitionFile targetDefinitionFile)
            throws IOException {

        // TODO: Create reader and read target from Case Manager current case
        // TargetDefinitionFile file = targetDefinitionFile.getTargetDefinitionFile();
        TargetDefinitionReader	reader = new TargetDefinitionReader(targetDefinitionFile);

    }

    /**
     *  @throws IllegalAccessException
     */
    public void restoreBackgroundCase() throws IllegalAccessException {
        getModel().restoreBackgroundTarget();
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
        return getBlockImage(getModel().getActiveBlock());
    }

    /**
     *  @param block
     *  @return
     */
    public ImageFile getBlockImage(ConResBlock block) {
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
        if (cornerSelector == null) cornerSelector = new CornerSelector(this);

        return cornerSelector;
    }

    /**
     * 	@return
     */
    private CornerSelectorModel getCornerSelectorModel() {

//      if (cornerSelector == null) return null;
        return getCornerSelector().getModel();
    }

    /**
     * 	@return
     */
    private ImagePlus getImagePlus() {
        CornerSelectorModel	model = getCornerSelectorModel();

//      if (model==null) model = getCornerSelector().getModel();
//      if (model==null) return null;
        try {
            if (model.getImagePlus() == null) loadImage();
        } catch (Exception exception) {
            exception.printStackTrace();

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
     * 	@param row
     * 	@param column
     * 	@return
     */
    public int getFirstColumnIndex() {
    	//AbstractValue[] blockValues = getModel().getActiveTarget().getzAxis().getValues();//getCornerSelectorModel().getTargetDefinitionFile().getBlockToneValues();
    	AbstractValue[] contrastValues = getModel().getActiveBlock().getXAxis().getValues();
    	int blockValue = (int)getModel().getActiveBlock().getZValue().getValue();
    	
    	int i = 0;
    	
    	for (i = 0; i < contrastValues.length; i++) {
    		int columnValue = (int)Math.round(contrastValues[i].getValue()/2);
    		int lowValue = blockValue - columnValue;
    		int highValue = blockValue + columnValue;
    		if (lowValue>=0 && highValue <= 100)
    			return i;
    	}
    	return i;
    }

    /**
     * 	@param row
     * 	@param column
     * 	@return
     */
    public Image getPatchImage(int row, int column) {
    	
    	int dbg = 2;
    	


        try {

            int			stepsY      = getCornerSelectorModel().getTargetDimensions().getYCenters().length;
            int			cI          = (column * stepsY) + row;

            PatchSetROI	patchSetROI = getPatchSetROI();
            Polygon polygon = patchSetROI.getPolygon();
            int			cX          = polygon.xpoints[cI];
            int			cY          = polygon.ypoints[cI];
            
            

//            int			cW          = (int)(polygon.xpoints[1] - polygon.xpoints[0]);//getCornerSelectorModel().getTargetDimensions().getXSpan() * 1.15);
            int			cH          = (int)((polygon.ypoints[1] - polygon.ypoints[0])*1.5);
            
            cH = Math.max(cH, 550);
            
            int cW = cH;

            int			x1          = cX - (cW / 2),
						x2          = cX + (cW / 2);
            int			y1          = cY - cH / 2,
						y2          = cY + cH / 2;

            GrasppeKit.debugText("Get Patch Image", "Patch: " + row + ", " + column + "\tCenter:" + cX + ", " + cY + "\tCorner:" + x1 + ", " + y1);
            
            ImagePlus	imagePlus   = getImagePlus();

            if (imagePlus == null) return null;

            Image	image = imagePlus.getImage();

            if (image == null) return null;
            
            ImageProducer patchImageProducer = new FilteredImageSource(image.getSource(), new CropImageFilter(x1, y1, cW, cH));
            
            Toolkit toolkit= Toolkit.getDefaultToolkit();
            
            Image patchImage = toolkit.createImage(patchImageProducer);
            
            return patchImage;
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * 	@return
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
}
