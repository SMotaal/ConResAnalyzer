/*
 * @(#)CaseFilesModel.java   11/11/14
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.io.model;

import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractModel;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 * @author daflair
 *
 */
public class CaseFilesModel extends AbstractModel {

    protected File		caseFolder;
    protected File[]	otherFiles,	imageFiles,	plotFiles, mapFile;

    /**
     *
     */
    public CaseFilesModel() {
        super();
    }

    /**
     * @param controller
     */
    public CaseFilesModel(AbstractController controller) {
        super(controller);
    }

    /**
     * Constructs ...
     *
     * @param controller
     * @param caseFolder
     */
    public CaseFilesModel(AbstractController controller, File caseFolder) {}

    /**
     * Method description
     */
    public void traverseFolder() {}

    /**
     * Traverses case folder to determine folder status by type checking and pooling files
     * @return
     */
    public boolean validateFolder() {
        return true;
    }

    /**
     * Method description
     *
     * @return
     */
    public boolean verifyFolder() {
        return true;
    }

    /**
     * @return the caseFolder
     */
    public File getCaseFolder() {
        return caseFolder;
    }

    /**
     * @return the imageFiles
     */
    public File[] getImageFiles() {
        return imageFiles;
    }

    /**
     * @return the mapFile
     */
    public File[] getMapFile() {
        return mapFile;
    }

    /**
     * @return the otherFiles
     */
    public File[] getOtherFiles() {
        return otherFiles;
    }

    /**
     * @return the plotFiles
     */
    public File[] getPlotFiles() {
        return plotFiles;
    }

    /**
     * @param caseFolder the caseFolder to set
     */
    public void setCaseFolder(File caseFolder) {
        this.caseFolder = caseFolder;
    }

    /**
     * @param imageFiles the imageFiles to set
     */
    public void setImageFiles(File[] imageFiles) {
        this.imageFiles = imageFiles;
    }

    /**
     * @param mapFile the mapFile to set
     */
    public void setMapFile(File[] mapFile) {
        this.mapFile = mapFile;
    }

    /**
     * @param otherFiles the otherFiles to set
     */
    public void setOtherFiles(File[] otherFiles) {
        this.otherFiles = otherFiles;
    }

    /**
     * @param plotFiles the plotFiles to set
     */
    public void setPlotFiles(File[] plotFiles) {
        this.plotFiles = plotFiles;
    }
}
