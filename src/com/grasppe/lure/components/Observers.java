/*
 * @(#)Observers.java   11/11/27
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.lure.components;

import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class Observers implements Observable {

    protected Observable	observable;
    protected boolean		updating = false;
    int dbg = 0;

    /** Field description */
    protected Set<Observer>	observerSet = new HashSet<Observer>();

    /**
     */
    private Observers() {
        super();
    }

    /**
     *  @param observable
     */
    public Observers(Observable observable) {
        this.observable = observable;
    }

    /**
     * @param observer
     */
    public void attachObserver(Observer observer) {
        if (observable == observer) return;
        
        if (observerSet.contains(observer)) return;

        GrasppeKit.debugText("Observer Attaching", GrasppeKit.lastSplit(observer.toString()),dbg);
        observerSet.add(observer);
        notifyObservers();
    }
    
    public void detachObservers() {
		Iterator<Observer> observerIterator = observerSet.iterator();

		while (observerIterator.hasNext()) {
			try {
				Observer observer = observerIterator.next();
				if (observer!=null)
					detachObserver(observer);
			} catch (Exception exception) {
				GrasppeKit.debugError("Detaching Observers", exception, 2);
			}
		}

    }

    /**
     * @param observer
     */
    public void detachObserver(Observer observer) {
        GrasppeKit.debugText("Observer Detaching" + GrasppeKit.lastSplit(observer.toString()),dbg);
        observerSet.remove(observer);
        notifyObservers();
    }

    /**
     *  @param observer
     */
    public void notifyObserver(Observer observer) {
        observer.update();
    }

    /**
     */
    public void notifyObservers() {

        if (updating) {
        	return;
        }

        updating = true;

        try {

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
        } catch (ConcurrentModificationException exception) {}

        updating = false;
    }

    /**
     *  @return
     */
    public String toString() {

        LinkedHashSet<String>	obsererStrings = new LinkedHashSet<String>();

        try {
            observerSet.toArray();
            for (Object object : observerSet)
                obsererStrings.add(((Observer)object).getClass().getSimpleName());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        String	observersText = "" + observerSet.size() + ((observerSet.size() > 1) ? " observers"
                : " observer");

        if (obsererStrings.size() > 0) observersText += ": " + GrasppeKit.cat(obsererStrings, ", ");

        return observersText;
    }

    /**
     *  @return
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
