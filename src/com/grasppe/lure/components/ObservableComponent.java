package com.grasppe.lure.components;

import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

public class ObservableComponent extends AbstractComponent implements Observable {
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

	@Override
	protected void finalizeUpdates() {
		if(getController()!=null)
			getController().finalizeUpdates();
		
		if(getView()!=null)
			getView().finalizeUpdates();

	}
    

}
