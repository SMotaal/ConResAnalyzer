/**
 * 
 */
package com.grasppe.conres.framework.targets.model.roi;

import java.awt.Polygon;

import com.grasppe.lure.framework.IAssessableObject;

import ij.ImagePlus;
import ij.gui.PointRoi;

/**
 * @author daflair
 *
 */
public class BlockROI extends PointRoi implements IAssessableObject {
	
	protected boolean validated = true;
	protected boolean verified = true;

	protected int requiredLength=4;

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
	 * @param ox
	 * @param oy
	 * @param points
	 */
	public BlockROI(int[] ox, int[] oy, int points) {
		super(ox, oy, points);
	}

	/**
	 * @param sx
	 * @param sy
	 * @param imp
	 */
	public BlockROI(int sx, int sy, ImagePlus imp) {
		super(sx, sy, imp);		
	}

	public boolean assess() {
		return validate() && verify();
	}

	public boolean validate() {
		boolean validLength = getNCoordinates() > 0 && getNCoordinates() == requiredLength;
		
		validated = validLength;

		return isValid();
	}

	public boolean verify() {
		return isVerified();
	}

	public String[] getAssessmentProblems() {
		return null;
	}

	public String[] getValidationProblems() {
		return null;
	}

	public String[] getVerificationProblems() {
		return null;
	}

	public boolean isValid() {
		return validated;
	}

	public boolean isVerified() {
		return verified;
	}

}
