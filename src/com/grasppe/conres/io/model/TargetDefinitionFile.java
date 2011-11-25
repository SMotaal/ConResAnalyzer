/*
 * @(#)ImageFile.java   11/11/15
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

import com.grasppe.conres.framework.targets.model.TargetMeasurements;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import java.net.URI;

import java.util.Arrays;

/**
 * @author daflair
 *
 */
public class TargetDefinitionFile extends CaseFile {

    protected static FileFilter	fileFilter = new CaseFileFilter(Arrays.asList(new String[] {
                                                 "*.log" }));
    protected static FilenameFilter	filenameFilter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".log");
        }
    };
    protected float[][]				fiducials       = new float[4][2];
    protected String[]				fiducialIDs     = new String[] { "ULC", "URC", "LRC", "LLC" };
    protected TargetMeasurements	measurements    = new TargetMeasurements();
    protected float[]				blockToneValues = new float[0];
    protected String				name            = "";

    /**
     * @param pathname
     */
    public TargetDefinitionFile(String pathname) {
        super(pathname);
    }

    /**
     * @param uri
     */
    public TargetDefinitionFile(URI uri) {
        super(uri);
    }

    /**
     * @param parent
     * @param child
     */
    public TargetDefinitionFile(File parent, String child) {
        super(parent, child);
    }

    /**
     * @param parent
     * @param child
     */
    public TargetDefinitionFile(String parent, String child) {
        super(parent, child);
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#validate()
     */

    /**
     *
     * @return
     */
    @Override
    public boolean validate() {
        return super.validate();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#verify()
     */

    /**
     * Method description
     *
     * @return
     */
    @Override
    public boolean verify() {

        // TODO Auto-generated method stub
        return super.verify();
    }

    /**
     * @return the blockToneValues
     */
    public float[] getBlockToneValues() {
        return blockToneValues;
    }

    /**
     * @return the fiducialIDs
     */
    public String[] getFiducialIDs() {
        return fiducialIDs;
    }

    /**
     * @return the fiducials
     */
    public float[][] getFiducials() {
        return fiducials;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#getFileFilter()
     */

    /**
     * Method description
     *
     * @return
     */
    public static FileFilter getFileFilter() {
        return fileFilter;
    }

    /**
     * @return the filenameFilter
     */
    public static FilenameFilter getFilenameFilter() {
        return filenameFilter;
    }

    /**
     * @return the measurements
     */
    public TargetMeasurements getMeasurements() {
        return measurements;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#isValidated()
     */

    /**
     * Method description
     *
     * @return
     */
    @Override
    public boolean isValidated() {

        // TODO Auto-generated method stub
        return super.isValidated();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#isVerified()
     */

    /**
     * Method description
     *
     * @return
     */
    @Override
    public boolean isVerified() {

        // TODO Auto-generated method stub
        return super.isVerified();
    }

    /**
     * @param blockToneValues the blockToneValues to set
     */
    public void setBlockToneValues(float[] blockToneValues) {
        this.blockToneValues = blockToneValues;
    }

    /**
     * @param fiducialIDs the fiducialIDs to set
     */
    public void setFiducialIDs(String[] fiducialIDs) {
        this.fiducialIDs = fiducialIDs;
    }

    /**
     * @param fiducials the fiducials to set
     */
    public void setFiducials(float[][] fiducials) {
        this.fiducials = fiducials;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#setFileFilter(java.io.FileFilter)
     */

    /**
     * Method description
     *
     *
     * @param newFilter
     */
    public static void setFileFilter(FileFilter newFilter) {
        fileFilter = newFilter;
    }

    /**
     * @param filenameFilter the filenameFilter to set
     */
    public static void setFilenameFilter(FilenameFilter filenameFilter) {
        ImageFile.filenameFilter = filenameFilter;
    }

    /**
     * @param measurements the measurements to set
     */
    public void setMeasurements(TargetMeasurements measurements) {
        this.measurements = measurements;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
