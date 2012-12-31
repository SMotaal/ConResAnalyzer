/*
 * @(#)ObservableObject.java   11/12/03
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.lure.components;

import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public abstract class ObservableObject implements Observable {

    /** Field description */
    protected Observers	observers = new Observers(this);

    /**
     * Attaches an observer through the observers object which will include the observer in future update() notify calls.
     * @param observer
     */
    public void attachObserver(Observer observer) {
        observers.attachObserver(observer);
    }

    /**
     * Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
     * @param observer
     */
    public void detachObserver(Observer observer) {
        observers.detachObserver(observer);
    }
    
    public void notifyDetatchObserver(Observer observer){
    	observers.notifyDetatchObserver(observer);
    }
    
    public void notifyDetatchObservers(){
    	observers.notifyDetatchObservers();
    }    
    
    /**
     * Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
     * @param observer
     */
    public void detachObservers() {
        observers.detachObservers();
    }    
    

    /**
     *  @param observer
     */
    public void notifyObserver(Observer observer) {
        observer.update();
    }

    /**
     * Notifies all observer through the observers object which calls update().
     */

    public void notifyObservers() {
        observers.notifyObservers();
    }

    /**
     *  @return
     */
    public String observerString() {
        return observers.toString();
    }
}
