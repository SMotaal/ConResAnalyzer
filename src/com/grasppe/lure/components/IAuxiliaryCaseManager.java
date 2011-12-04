/*
 * @(#)IAuxiliaryCaseManager.java   11/12/03
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 */
package com.grasppe.lure.components;

/**
 * @author daflair
 */
public interface IAuxiliaryCaseManager {

    /**
     *  @throws IllegalAccessException
     */
    public void backgroundCurrentCase() throws IllegalAccessException;

    /**
     *  @throws IllegalAccessException
     */
    public void discardBackgroundCase() throws IllegalAccessException;

    /**
     *  @throws IllegalAccessException
     */
    public void restoreBackgroundCase() throws IllegalAccessException;
}
