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
public class PatchSetROI extends PointRoi implements IAssessableObject {
	
	protected boolean validated = true;
	protected boolean verified = true;

	protected int requiredLength=0;

	/**
	 * @param poly
	 */
	public PatchSetROI(Polygon poly, int requiredLength) {
		super(poly);
		this.requiredLength = requiredLength;
	}

	/**
	 * @param ox
	 * @param oy
	 */
	public PatchSetROI(int ox, int oy, int requiredLength) {
		super(ox, oy);
		this.requiredLength = requiredLength;
	}

	/**
	 * @param ox
	 * @param oy
	 * @param points
	 */
	public PatchSetROI(int[] ox, int[] oy, int points, int requiredLength) {
		super(ox, oy, points);
		this.requiredLength = requiredLength;
	}

	/**
	 * @param sx
	 * @param sy
	 * @param imp
	 */
	public PatchSetROI(int sx, int sy, ImagePlus imp, int requiredLength) {
		super(sx, sy, imp);
		this.requiredLength = requiredLength;
		
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
