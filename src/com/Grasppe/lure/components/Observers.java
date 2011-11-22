package com.grasppe.lure.components;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 *
 */
public class Observers implements Observable {
	
	public Observers() {
		super();
	}
	
	public Observers(Observable observable) {
		this.observable = observable;
	}
	
	protected Observable observable;

    /**
	 * @return the observerSet
	 */
	public Set<Observer> getObserverSet() {
		return observerSet;
	}



	/** Field description */
    protected Set<Observer>	observerSet = new HashSet<Observer>();

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
     * Method description
     *
     */
    public void notifyObservers() {
        Iterator<Observer>	observerIterator = observerSet.iterator();

        if (!observerIterator.hasNext()) GrasppeKit.debugText("Observer Update Failed", toString());

        while (observerIterator.hasNext()) {
            Observer	thisObserver = (Observer)observerIterator.next();

            GrasppeKit.debugText("Observer Update", " ==> " + GrasppeKit.lastSplit(thisObserver.toString()));
            
            if (observable==null)
            	notifyObserver(thisObserver);
            else
            	observable.notifyObserver(thisObserver);
        }

        // TODO Implement throwing exceptions for update of missing element
    }
    
    public Iterator<Observer> getIterator(){
    	return observerSet.iterator();//new HashSet<Observer>(observerSet).iterator();
    }

	@Override
	public void notifyObserver(Observer observer) {
		observer.update();
	}
}