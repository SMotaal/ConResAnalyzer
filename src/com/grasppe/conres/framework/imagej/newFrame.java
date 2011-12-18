/*
 * @(#)newFrame.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.imagej;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

/**
 * @author daflair
 */
public class newFrame extends JFrame {

    /**
     * @throws HeadlessException
     */
    public newFrame() throws HeadlessException {

    }

    /**
     * @param arg0
     */
    public newFrame(GraphicsConfiguration arg0) {
        super(arg0);

    }

    /**
     * @param arg0
     * @throws HeadlessException
     */
    public newFrame(String arg0) throws HeadlessException {
        super(arg0);

    }

    /**
     * @param arg0
     * @param arg1
     */
    public newFrame(String arg0, GraphicsConfiguration arg1) {
        super(arg0, arg1);

    }
}
