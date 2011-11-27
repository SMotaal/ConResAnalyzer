/*
 * @(#)TargetManagerCommand.java   11/11/26
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.targets.operations;

import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.lure.components.AbstractCommand;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;

/**
 * Defines Case Manager's Close Case actions and command, using the EAC pattern.
 *
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public abstract class TargetManagerCommand extends AbstractCommand {

    /**
     * Constructs a realization of AbstractCommand.
     *
     * @param listener
     * @param name
     */
    public TargetManagerCommand(ActionListener listener, String name) {
        super(listener, name, false);
        setModel(((TargetManager)listener).getModel());
    }

    /**
     * Returns the correctly-cast model.
     *
     * @return
     */
    @Override
    public TargetManagerModel getModel() {
        return (TargetManagerModel)model;
    }
}
