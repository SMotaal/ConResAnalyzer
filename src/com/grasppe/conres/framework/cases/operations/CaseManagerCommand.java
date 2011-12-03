/*
 * @(#)CaseManagerCommand.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.cases.operations;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.lure.components.AbstractCommand;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;

/**
 * Defines Case Manager's Close Case actions and command, using the EAC pattern.
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public abstract class CaseManagerCommand extends AbstractCommand {

    /**
     * Constructs a realization of AbstractCommand.
     * @param listener
     * @param name
     */
    public CaseManagerCommand(ActionListener listener, String name) {
        super(listener, name, false);
        setModel(((CaseManager)listener).getModel());
    }

    /**
     * Returns the correctly-cast model.
     * @return
     */
    @Override
    public CaseManagerModel getModel() {
        return (CaseManagerModel)model;
    }
}
