package com.grasppe.conres.framework.cases.operations;

import java.awt.event.ActionListener;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.lure.components.AbstractCommand;

/**
 * Defines Case Manager's Close Case actions and command, using the EAC pattern.
 *
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public abstract class CaseManagerCommand extends AbstractCommand {

    /**
     * Constructs a realization of AbstractCommand.
     *
     * @param listener
     * @param name
     */
    public CaseManagerCommand(ActionListener listener, String name) {
        super(listener, name, false);
        setModel(((CaseManager)listener).getModel());
    }

    /**
     * Returns the correctly-cast model.
     *
     * @return
     */
    @Override
	public CaseManagerModel getModel() {
        return (CaseManagerModel)model;
    }
}