/*
 * @(#)IConResTargetDefinition.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.io.model;

import com.grasppe.conres.framework.targets.model.TargetMeasurements;

/**
 *  @version        $Revision: 1.0, 11/11/26
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public interface IConResTargetDefinition {

    /**
     * @return the blockToneValues
     */
    public abstract int[] getBlockToneValues();

    /**
     * @return the fiducialIDs
     */
    public abstract String[] getFiducialIDs();

    /**
     * @return the fiducials
     */
    public abstract float[][] getFiducials();

    /**
     * @return the measurements
     */
    public abstract TargetMeasurements getMeasurements();

    /**
     * @return the name
     */
    public abstract String getName();

    /**
     * @param blockToneValues the blockToneValues to set
     */
    public abstract void setBlockToneValues(int[] blockToneValues);

    /**
     * @param fiducialIDs the fiducialIDs to set
     */
    public abstract void setFiducialIDs(String[] fiducialIDs);

    /**
     * @param fiducials the fiducials to set
     */
    public abstract void setFiducials(float[][] fiducials);

    /**
     * @param measurements the measurements to set
     */
    public abstract void setMeasurements(TargetMeasurements measurements);

    /**
     * @param name the name to set
     */
    public abstract void setName(String name);
}
