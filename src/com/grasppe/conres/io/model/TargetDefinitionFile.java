/*
 * @(#)ImageFile.java   11/11/15
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.io.model;

import com.grasppe.conres.framework.targets.model.TargetMeasurements;
import com.grasppe.conres.io.IGrasppeFileReader;
import com.grasppe.conres.io.TargetDefinitionReader;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import java.net.URI;

import java.util.Arrays;

/**
 * @author daflair
 */
public class TargetDefinitionFile extends CaseFile implements IConResTargetDefinition {

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
    protected int[]					blockToneValues = new int[0];
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
     * @return
     */
    @Override
    public boolean verify() {

        return super.verify();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.io.model.IConResTargetDefinition#getBlockToneValues()
     */

    /**
     *  @return
     */
    public int[] getBlockToneValues() {
        return blockToneValues;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.io.model.IConResTargetDefinition#getFiducialIDs()
     */

    /**
     *  @return
     */
    public String[] getFiducialIDs() {
        return fiducialIDs;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.io.model.IConResTargetDefinition#getFiducials()
     */

    /**
     *  @return
     */
    public float[][] getFiducials() {
        return fiducials;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#getFileFilter()
     */

    /**
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

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.io.model.IConResTargetDefinition#getMeasurements()
     */

    /**
     *  @return
     */
    public TargetMeasurements getMeasurements() {
        return measurements;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.io.model.IConResTargetDefinition#getName()
     */

    /**
     *  @return
     */
    public String getName() {
        return name;
    }

    /**
     *  @return
     */
    @Override
    public IGrasppeFileReader getReader() {
        try {
            return new TargetDefinitionReader(this);
        } catch (Exception exception) {
            exception.printStackTrace();

            return null;
        }
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#isValidated()
     */

    /**
     * @return
     */
    @Override
    public boolean isValidated() {

        return super.isValidated();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#isVerified()
     */

    /**
     * @return
     */
    @Override
    public boolean isVerified() {

        return super.isVerified();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.io.model.IConResTargetDefinition#setBlockToneValues(float[])
     */

    /**
     *  @param blockToneValues
     */
    public void setBlockToneValues(int[] blockToneValues) {
        this.blockToneValues = blockToneValues;
        
        this.measurements.setZValues(null);
        if (blockToneValues==null || blockToneValues.length==0)
        	return;

       	float[] floatValues = new float[blockToneValues.length];
       	for (int i = 0; i < blockToneValues.length; i++)
       		floatValues[i] = new Float(blockToneValues[i]);
       	
       	this.measurements.setZValues(floatValues);
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.io.model.IConResTargetDefinition#setFiducialIDs(java.lang.String[])
     */

    /**
     *  @param fiducialIDs
     */
    public void setFiducialIDs(String[] fiducialIDs) {
        this.fiducialIDs = fiducialIDs;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.io.model.IConResTargetDefinition#setFiducials(float[][])
     */

    /**
     *  @param fiducials
     */
    public void setFiducials(float[][] fiducials) {
        this.fiducials = fiducials;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#setFileFilter(java.io.FileFilter)
     */

    /**
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

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.io.model.IConResTargetDefinition#setMeasurements(com.grasppe.conres.framework.targets.model.TargetMeasurements)
     */

    /**
     *  @param measurements
     */
    public void setMeasurements(TargetMeasurements measurements) {
        this.measurements = measurements;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.io.model.IConResTargetDefinition#setName(java.lang.String)
     */

    /**
     *  @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
