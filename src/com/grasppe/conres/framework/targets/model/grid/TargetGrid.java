/*
 * @(#)TargetGrid.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.framework.targets.model.grid;

import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.TreeMap;

/**
 * @author daflair
 */
public class TargetGrid extends AbstractModel {

    protected TreeMap<Integer, Integer>	bMap;
    protected TreeMap<Integer, Integer>	rMap;
    protected TreeMap<Integer, Integer>	cMap;

    /**
     */
    public TargetGrid() {

        // TODO Auto-generated constructor stub
    }

    /**
     * @param controller
     */
    public TargetGrid(AbstractController controller) {
        super(controller);

        // TODO Auto-generated constructor stub
    }
}
