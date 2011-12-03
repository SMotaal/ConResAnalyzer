/*
 * @(#)GrasppeEventHandler.java   11/12/03
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 */
package com.grasppe.lure.framework;

import java.awt.event.KeyEvent;

/**
 * @author daflair
 */
public interface GrasppeEventHandler extends GrasppeKit.Observer {

    /**
     * 	@param e
     * 	@return
     */
    public boolean dispatchedKeyEvent(KeyEvent e);
}
