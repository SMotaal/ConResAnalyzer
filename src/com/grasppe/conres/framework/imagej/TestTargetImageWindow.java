/*
 * @(#)TestTargetImageWindow.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.imagej;

import ij.ImagePlus;

import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;

/**
 * @author daflair
 */
public class TestTargetImageWindow extends ImageWindow {

    /**
     * @param imp
     */
    public TestTargetImageWindow(ImagePlus imp) {
        super(imp);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param title
     */
    public TestTargetImageWindow(String title) {
        super(title);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     * @param arg1
     */
    public TestTargetImageWindow(ImagePlus arg0, ImageCanvas arg1) {
        super(arg0, arg1);

        // TODO Auto-generated constructor stub
    }
}
