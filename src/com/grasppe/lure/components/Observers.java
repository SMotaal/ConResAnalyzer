/*
 * @(#)Observers.java   11/11/27
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.lure.components;

import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 *
 */
public class Observers implements Observable {

    protected Observable	observable;

    /** Field description */
    protected Set<Observer>	observerSet = new HashSet<Observer>();

    /**
     */
    public Observers() {
        super();
    }

    /**
     * 	@param observable
     */
    public Observers(Observable observable) {
        this.observable = observable;
    }

    /**
     * Method description
     *
     * @param observer
     */
    public void attachObserver(Observer observer) {
        GrasppeKit.debugText("Observer Attaching", GrasppeKit.lastSplit(observer.toString()));
        observerSet.add(observer);

        // TODO Implement throwing exceptions for attach of existing element
    }

    /**
     * Method description
     *
     * @param observer
     */
    public void detachObserver(Observer observer) {
        GrasppeKit.debugText("Observer Detaching" + GrasppeKit.lastSplit(observer.toString()));
        observerSet.remove(observer);

        // TODO Implement throwing exceptions for detach of missing element
    }

    /**
     * 	@param observer
     */
    public void notifyObserver(Observer observer) {
        observer.update();
    }

    /**
     * Method description
     *
     */
    public void notifyObservers() {

        observerSet.toArray();

        for (Object object : observerSet) {
            Observer	observer = (Observer)object;

            try {
                if (observable == null) notifyObserver(observer);
                else observable.notifyObserver(observer);
            } catch (Exception exception) {
            	observerSet.remove(observer);
            	exception.printStackTrace();
            }
        }

//      Iterator<Observer>    observerIterator = observerSet.iterator();
//
//      if (!observerIterator.hasNext()) GrasppeKit.debugText("Observer Update Failed", toString());
//
//      while (observerIterator.hasNext()) {
//          Observer  thisObserver = observerIterator.next();
//
//          GrasppeKit.debugText("Observer Update", " ==> " + GrasppeKit.lastSplit(thisObserver.toString()));
//          
//          if (observable==null)
//            notifyObserver(thisObserver);
//          else
//            observable.notifyObserver(thisObserver);
//      }

        // TODO Implement throwing exceptions for update of missing element
    }

    /**
     * 	@return
     */
    public Iterator<Observer> getIterator() {
        return observerSet.iterator();		// new HashSet<Observer>(observerSet).iterator();
    }

    /**
     * @return the observerSet
     */
    public Set<Observer> getObserverSet() {
        return observerSet;
    }

    /**
     * @param observerSet the observerSet to set
     */
    public void setObserverSet(Set<Observer> observerSet) {
        this.observerSet = observerSet;
    }
}
