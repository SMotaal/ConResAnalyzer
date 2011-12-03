/*
 * @(#)ConResAnalyzerCommand.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer.operations;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.lure.components.AbstractCommand;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;

/**
 * Defines Case Manager's Close Case actions and command, using the EAC pattern.
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public abstract class ConResAnalyzerCommand extends AbstractCommand {

    /**
     * Constructs a realization of AbstractCommand.
     * @param listener
     * @param name
     */
    public ConResAnalyzerCommand(ActionListener listener, String name) {
        super(listener, name, false);
        setModel(((ConResAnalyzer)listener).getModel());
    }

    /**
     * Returns the correctly-cast model.
     * @return
     */
    @Override
    public ConResAnalyzerModel getModel() {
        return (ConResAnalyzerModel)model;
    }
}
