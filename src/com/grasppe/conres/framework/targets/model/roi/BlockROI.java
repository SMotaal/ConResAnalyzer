/*
 * @(#)BlockROI.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.targets.model.roi;

import com.grasppe.lure.framework.IAssessableObject;

import ij.ImagePlus;

import ij.gui.PointRoi;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Polygon;

/**
 * @author daflair
 */
public class BlockROI extends PointRoi implements IAssessableObject {

    protected boolean	validated      = true;
    protected boolean	verified       = true;
    protected int		requiredLength = 4;

    /**
     * @param poly
     */
    public BlockROI(Polygon poly) {
        super(poly);
    }

    /**
     * @param ox
     * @param oy
     */
    public BlockROI(int ox, int oy) {
        super(ox, oy);
    }

    /**
     * @param sx
     * @param sy
     * @param imp
     */
    public BlockROI(int sx, int sy, ImagePlus imp) {
        super(sx, sy, imp);
    }

    /**
     * @param ox
     * @param oy
     * @param points
     */
    public BlockROI(int[] ox, int[] oy, int points) {
        super(ox, oy, points);
    }

    /**
     *  @return
     */
    public boolean assess() {
        return validate() && verify();
    }

    /**
     *  @return
     */
    public boolean validate() {
        boolean	validLength = (getNCoordinates() > 0) && (getNCoordinates() == requiredLength);

        validated = validLength;

        return isValid();
    }

    /**
     *  @return
     */
    public boolean verify() {
        return isVerified();
    }

    /**
     *  @return
     */
    public String[] getAssessmentProblems() {
        return null;
    }

    /**
     *  @return
     */
    public String[] getValidationProblems() {
        return null;
    }

    /**
     *  @return
     */
    public String[] getVerificationProblems() {
        return null;
    }

    /**
     *  @return
     */
    public boolean isValid() {
        return validated;
    }

    /**
     *  @return
     */
    public boolean isVerified() {
        return verified;
    }
}
