/*
 * @(#)AbstractComponent.java   11/12/13
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.lure.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

/**
 * Class description
 * 	@version        $Revision: 1.0, 11/12/13
 * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
 */
public abstract class AbstractComponent extends ObservableObject {

    protected AbstractController	controller;
    protected AbstractModel			model;
    protected AbstractView			view;
    protected ComponentType			componentType;
    
    int dbg = 3;

    /**
     */
    public AbstractComponent() {
        super();

        initializeComponent();

    }

    /**
     * Enumeration of different AbstractComponent types (model, view, controller... etc.)
     */
    public enum ComponentType {
        MODEL(2), VIEW(3), CONTROLLER(1), UNDEFINED(0), COMMAND(11), FUNCTION(12);

        private static final Map<Integer, ComponentType>	lookup = new HashMap<Integer,
                                                                      ComponentType>();

        static {
            for (ComponentType s : EnumSet.allOf(ComponentType.class))
                lookup.put(s.value(), s);
        }

        private int	code;

        /**
         * @param code   integer or constant variable for the specific enumeration
         */
        private ComponentType(int code) {
            this.code = code;
        }

        /**
         * @return the integer value for a specific enumeration
         */
        public int value() {
            return code;
        }

        /**
         * @param code   integer or constant variable for the specific enumeration
         * @return enumeration object
         */
        public static ComponentType get(int code) {
            return lookup.get(code);
        }
    }
    
    public void detatch() throws Throwable {
    	
    }

    /**
     */
    protected void initializeComponent() {
        Object	component = (Object)this;

        componentType = typeOf(this);

        switch (componentType) {

        case MODEL :
            initializeModel();
            break;

        case VIEW :
            initializeView();
            break;

        case CONTROLLER :
            initializeController();
            break;
            
        case COMMAND :
        	initializeCommand();
        	break;
        case FUNCTION :
        	initializeFunction();
        	break;
        }
    }

    /**
     */
    protected void initializeController() {
        Object	component = (Object)this;

        controller = (AbstractController)component;
    }
    
    protected void initializeCommand() {
    	Object	component = (Object)this;
    }
    
    protected void initializeFunction() {
    	Object	component = (Object)this;
    }

    /**
     */
    protected void initializeModel() {
        Object	component = (Object)this;

        model = (AbstractModel)component;

        if (getController() != null) {
            setView(getController().getView());
        }
    }

    /**
     */
    protected void initializeView() {
        Object	component = (Object)this;

        view = (AbstractView)component;
        
        if (getController() != null) {
            setModel(getController().getModel());
        }
    }

    /**
     * 	@param component
     * 	@return
     */
    protected static ComponentType typeOf(Object component) {
        if (component instanceof AbstractModel) return ComponentType.MODEL;

        if (component instanceof AbstractView) return ComponentType.VIEW;

        if (component instanceof AbstractController) return ComponentType.CONTROLLER;

        return ComponentType.UNDEFINED;
    }

    /**
     * @return the controller
     */
    public AbstractController getController() {
        return controller;
    }

    /**
     * @return the model
     */
    public AbstractModel getModel() {
        return model;
    }

    /**
     * @return the view
     */
    public AbstractView getView() {
        return view;
    }

    /**
     * @param controller the controller to set
     */
    protected void setController(AbstractController controller) {
        this.controller = controller;
    }

    /**
     * @param model the model to set
     */
    protected void setModel(AbstractModel model) {
        this.model = model;
    }

    /**
     * @param view the view to set
     */
    protected void setView(AbstractView view) {
        this.view = view;
    }
    
    private void delayUpdates() {
    	Timer lockTimer = new Timer(1000, new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				updatesAllowed = true;
				if (updatesPrending) forceUpdates();
			}
    		
    	});
    	lockTimer.setRepeats(false);
    	lockTimer.start();
    }
    
    boolean updatesAllowed = true;
    boolean updatesPrending = false;
    
    protected void forceUpdates() {
    	if (componentType!=ComponentType.MODEL) {
    		getModel().forceUpdates();
    		return;
    	}    	
    	updatesPrending = false;
    	delayUpdates();
    	getModel().notifyObservers();
    }
    
    protected void pushUpdates() {
    	
    	if (componentType!=ComponentType.MODEL) {
    		getModel().pushUpdates();
    		return;
    	}
    	
    	updatesPrending = true;
    	if (!updatesAllowed) return;
    	
    	forceUpdates();
    	
    }
    
    protected abstract void finalizeUpdates();
}
