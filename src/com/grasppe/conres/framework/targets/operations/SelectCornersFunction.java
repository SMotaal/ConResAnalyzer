/*
 * @(#)SelectCornersFunction.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets.operations;

import com.grasppe.conres.framework.targets.CornerSelector;
import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
import com.grasppe.conres.framework.targets.view.CornerSelectorView;
import com.grasppe.lure.components.AbstractComponent;
import com.grasppe.lure.components.AbstractModel;
import com.grasppe.lure.components.ObservableObject;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import javax.management.openmbean.InvalidOpenTypeException;

/**
 * Class description
 * @version        $Revision: 1.0, 11/11/09
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class SelectCornersFunction extends TargetManagerFunction implements Observer {

    protected static final String	name = "SelectCorners";
    AbstractModel					model;
    CornerSelectorView				view;		// = new CornerSelectorView();
    CornerSelector					controller;
    boolean							openWindow = false;
    
    int dbg = 0;

    /**
     */
    public SelectCornersFunction() {
        super(name);
    }

    /**
     *  @param controller
     */
    public SelectCornersFunction(CornerSelector controller) {
        this();
        this.controller = controller;
        setModel(controller.getModel());
        setView(controller.getSelectorView());
    }

    /**
     * @return
     */
    @Override
    protected boolean perfomOperation() {
    	
    	controller.showSelectorView();

        return true;
    }

    /**
     */
    public void prepareView() {
        view = new CornerSelectorView(null);
    }

    /**
     */
    public void showCornerSelector() {}

    /**
     */
    public void update() {
        GrasppeKit.debugText("OBSERVED", dbg);
    }

    /**
     */
    public void windowClosed() {
        openWindow = false;
    }

    /**
     * Returns a correctly-cast model.
     * @return
     */
    public CornerSelectorModel getModel() {
        return (CornerSelectorModel)model;
    }

    /**
     * Attaches the command to the specified model and calls update() to reflect the state of the model.
     * @param model
     */
    public void setModel(AbstractModel model) {
        model.attachObserver(this);
        this.model = model;
        update();
    }

    /**
     * Attaches the command to the specified model and calls update() to reflect the state of the model.
     *  @param view
     */
    public void setView(AbstractComponent view) {
        model.attachObserver(this);
        if (!(view instanceof CornerSelectorView)) throw new InvalidOpenTypeException();

        this.view = (CornerSelectorView)view;

        update();
    }

	@Override
	public void detatch(Observable oberservableObject) {
		// TODO Auto-generated method stub
		
	}
}
