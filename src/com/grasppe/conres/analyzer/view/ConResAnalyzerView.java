/*
 * @(#)ConResAnalyzerView.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer.view;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 *     Class description
 *     @version        $Revision: 0.1, 11/11/08
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ConResAnalyzerView extends AbstractView {

    /** Field description */
    ConResAnalyzerMenu	menu;
    String				name        = "ConResAnalyzer";
    boolean				finalizable = true;
    int					activeCalls = 0;

    /**
     * Constructs a new ConResAnalyzerView with a predefined controller.
     * @param controller
     */
    public ConResAnalyzerView(ConResAnalyzer controller) {
        super(controller);
    }

    /**
     * @return
     */
    public boolean canFinalize() {
        finalizable = (activeCalls == 0);
        GrasppeKit.debugText("Finalize / Active Calls", activeCalls + " remaining.");

        return finalizable;
    }

    /**
     * Completes graphical user interface operations before closing.
     * @return
     */
    public boolean finalizeView() {
        if (!canFinalize()) return false;

        return true;
    }

    /**
     */
    public void prepareMenu() {
        menu                       = new ConResAnalyzerMenu();
        menu.caseLabel.caseManager = getController().getCaseManager();
        getController().getCaseManager().getModel().attachObserver(menu.caseLabel);

        LinkedHashMap<String, AbstractCommand>	commands        = controller.getCommands();
        Collection<AbstractCommand>				commandSet      = commands.values();
        Iterator<AbstractCommand>				commandIterator = commandSet.iterator();

        while (commandIterator.hasNext()) {
            AbstractCommand	command = commandIterator.next();

            GrasppeKit.debugText("Command Button Creation",
                                 GrasppeKit.lastSplit(command.toString()));

            menu.createButton(command);
        }
    }

    /**
     * Builds the graphical user interface window and elements.
     */
    public void prepareView() {
        this.prepareMenu();
    }

    /**
     *  @return
     */
    protected ConResAnalyzer getController() {
        return (ConResAnalyzer)controller;
    }

    /**
     *  @return
     */
    @Override
    protected ConResAnalyzerModel getModel() {
        return (ConResAnalyzerModel)super.getControllerModel();
    }
}
