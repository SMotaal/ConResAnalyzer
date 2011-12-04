/*
 * @(#)AbstractModel.java   11/12/03
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.lure.components;

import java.io.InvalidObjectException;

import java.util.HashSet;
import java.util.Set;

/**
 *     Models handle the data portion of a component. A model indirectly notifies a controller and any number of views of any changes to the data. The controller is responsible for initiating all attach/detach calls, however the model keeps track of observer view objects with special implementation.
 *     @version        $Revision: 0.1, 11/11/08
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AbstractModel extends ObservableObject {

    protected AbstractController	controller;
    protected Set<AbstractView>		views = new HashSet<AbstractView>();

//  protected GrasppeKit            grasppeKit     = GrasppeKit.getInstance();

    /**
     * Constructs a new model object with no predefined controller.
     */
    public AbstractModel() {
        super();
    }

    /**
     * Constructs a new model with a predefined controller.
     * @param controller
     */
    public AbstractModel(AbstractController controller) {
        this();
        attachController(controller);
    }

    /**
     * @param controller
     */
    public void attachController(AbstractController controller) {
        this.controller = controller;
    }

    /**
     * @param view
     */
    protected void attachView(AbstractView view) {
        if (views.contains(view)) return;
        super.attachObserver(view);
        views.add(view);
    }

    /**
     * Detaches a properly attached view object. Detaching a view which has not been attached as a view object will cause an InvalidObjectException.
     * @param view
     * @throws InvalidObjectException
     */
    protected void detachView(AbstractView view) throws InvalidObjectException {
        if (!views.contains(view))
            throw new InvalidObjectException("Cannot detach from an unattached view object.");
        super.detachObserver(view);
    }

    /**
     *  @return
     */
    public boolean hasViews() {
        return views != null | (views.size() > 0);
    }
}
