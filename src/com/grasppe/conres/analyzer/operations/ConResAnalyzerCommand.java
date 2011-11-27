package com.grasppe.conres.analyzer.operations;

import java.awt.event.ActionListener;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.lure.components.AbstractCommand;

/**
 * Defines Case Manager's Close Case actions and command, using the EAC pattern.
 *
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public abstract class ConResAnalyzerCommand extends AbstractCommand {

    /**
     * Constructs a realization of AbstractCommand.
     *
     * @param listener
     * @param name
     */
    public ConResAnalyzerCommand(ActionListener listener, String name) {
        super(listener, name, false);
        setModel(((ConResAnalyzer)listener).getModel());
    }

    /**
     * Returns the correctly-cast model.
     *
     * @return
     */
    @Override
	public ConResAnalyzerModel getModel() {
        return (ConResAnalyzerModel)model;
    }
}