/*
 * @(#)IAssessableObject.java   11/11/26
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.lure.framework;

/**
 * @author daflair
 *
 */
public interface IAssessableObject {

    /**
     * 	@return
     */
    public boolean assess();

    /**
     * 	@return
     */
    public boolean validate();

    /**
     * 	@return
     */
    public boolean verify();

    /**
     * 	@return
     */
    public String[] getAssessmentProblems();

    /**
     * 	@return
     */
    public String[] getValidationProblems();

    /**
     * 	@return
     */
    public String[] getVerificationProblems();

    /**
     * 	@return
     */
    public boolean isValid();

    /**
     * 	@return
     */
    public boolean isVerified();
}
