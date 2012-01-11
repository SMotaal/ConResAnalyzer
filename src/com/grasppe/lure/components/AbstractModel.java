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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ChangeListener;

import com.grasppe.lure.framework.GrasppeKit;

/**
 *     Models handle the data portion of a component. A model indirectly notifies a controller and any number of views of any changes to the data. The controller is responsible for initiating all attach/detach calls, however the model keeps track of observer view objects with special implementation.
 *     @version        $Revision: 0.1, 11/11/08
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AbstractModel extends ObservableComponent {
	
	boolean locked = false;

    /* (non-Javadoc)
	 * @see com.grasppe.lure.components.ObservableComponent#notifyObservers()
	 */
	@Override
	public void notifyObservers() { //synchronized
		if (locked) return;
		locked = true;
		super.notifyObservers();
		changeList.clear();
		locked = false;
	}

	/**
	 * @return the controller
	 */
	public AbstractController getController() {
		return controller;
	}

	/**
	 * @return the views
	 */
	public HashSet<AbstractView> getViews() {
		return views;
	}

	protected AbstractController	controller;
    protected HashSet<AbstractView>		views = new HashSet<AbstractView>();

    /**
     * Constructs a new model object with no predefined controller.
     */
    public AbstractModel() {
        super();
    }
    
    public AbstractView getView(){
    	return getView(0);
    }
    
    public AbstractView getView(int index){
//    	if (getModel()==null) return null;
    	AbstractView[] views = this.views.toArray(new AbstractView[0]);
    	if (views.length > index) return views[index];
    	return null;
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
    
    public boolean hasChanged(String fieldName){
    	boolean didChange = changeList.contains(fieldName); 
    	GrasppeKit.debugText("Checking Field", fieldName + ((didChange) ? " has not changed" : " has changed"), 2);
    	return didChange;
    }
    
    public void changeField(String fieldName) {
    	if (changeList.contains(fieldName)) return;
    		GrasppeKit.debugText("Changing Field", fieldName, 2);
    		changeList.add(fieldName);
    }
    
    
    
    protected ArrayList<String> changeList = new ArrayList<String>();

    /**
     * @param view
     */
    protected void attachView(AbstractView view) {
    	if (!(view instanceof AbstractComponent)) return;
    	try {
	    	AbstractComponent castView = (AbstractComponent) view;
	        if (views.contains(view)) return;
	        super.attachObserver(view);
	        views.add(view);
    	} catch (Exception exception) {
    		// attachView(view);
    	}
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
