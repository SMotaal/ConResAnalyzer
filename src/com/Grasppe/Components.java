/*
 * @(#)GrasppeCommon.java   11/11/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.Grasppe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.InvalidObjectException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.swing.AbstractAction;

/**
 * Abstract super-classes with common design patterns.
 *
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class Components {

    /**
     * Traverse and output the call stack.
     */
    public static void showCallStack() {
        StackTraceElement[]	stackTraceElements = Thread.currentThread().getStackTrace();

        for (int i = 2; i < stackTraceElements.length; i++) {
            StackTraceElement	ste        = stackTraceElements[i];
            String				classname  = ste.getClassName();
            String				methodName = ste.getMethodName();
            int					lineNumber = ste.getLineNumber();

            System.out.println(classname + "." + methodName + ":" + lineNumber);
        }
    }

    /**
     * Traverse the call stack to determine and return where a method was called from.
     *
     * @return
     */
    public static String whoCalledMe() {
        StackTraceElement[]	stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement	caller             = stackTraceElements[4];
        String				classname          = caller.getClassName();
        String				methodName         = caller.getMethodName();
        int					lineNumber         = caller.getLineNumber();

        return classname + "." + methodName + ":" + lineNumber;
    }

    /**
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     *
     */
    public interface Observable {

        /**
         * Method description
         *
         * @param observer
         */
        public void attachObserver(Observer observer);

        /**
         * Method description
         *
         * @param observer
         */
        public void detachObserver(Observer observer);

        /**
         * Method description
         *
         */
        public void notifyObservers();
    }


    /**
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     *
     */
    public interface Observer {

        /**
         * Method called by observable object during notifyObserver calls.
         */
        public void update();
    }


    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class AbstractCommand extends AbstractAction implements Observer {

        protected ActionListener	actionListener;
        protected boolean			executable = false;
        protected AbstractModel		model;

        /** Field description */
        public String	name = getClass().getSimpleName();

        /**
         * Constructs a new command with the specified listener.
         *
         * @param listener
         * @param text
         */
        public AbstractCommand(ActionListener listener, String text) {
            super(text);	// , icon);
            actionListener = listener;
        }

        /**
         * Constructs a new command with the specified listener.
         *
         * @param listener
         * @param text
         * @param executable
         */
        public AbstractCommand(ActionListener listener, String text, boolean executable) {
            this(listener, text);
            canExecute(executable);
        }

        /**
         * Called when action is triggered and passes the ActionEvent to actionListener, which is responsible for calling the execute() method.
         *
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            actionListener.actionPerformed(e);
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean canExecute() {
            return executable;
        }

        /**
         * Method description
         *
         * @param state
         *
         * @return
         */
        public boolean canExecute(boolean state) {
            executable = true;

            return canExecute();
        }

        /**
         * Called by the actionListener, following a call to actionPerformed().
         */
        public void execute() {
            if (!canExecute()) throw new IllegalStateException(getName() + " cannot execute!");
            System.out.println(toString() + " Command Executing");
        }

        /*
         *  (non-Javadoc)
         * @see java.lang.Object#finalize()
         */

        /**
         * Method description
         *
         * @throws Throwable
         */
        @Override
        protected void finalize() throws Throwable {
            try {
                model.detachObserver(this);
            } catch (Exception e) {

                // Command has no model and can finalize immediately
            }

            super.finalize();
        }

        /**
         * Called by the model indirectly during notify.
         */
        @Override
        public void update() {}

        /**
         * Method description
         *
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * Method description
         *
         * @param model
         */
        public void setModel(AbstractModel model) {
            model.attachObserver(this);
            this.model = model;
        }
    }


    /**
     * Controllers handle the logic portion of a component. A controller directly accesses a model. The controller is responsible for all attach/detach calls. A model is responsible for safely handling attach/detach class for views.
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class AbstractController implements Observer {

        protected AbstractModel	model;
        protected boolean		detachable = true;

        /**
         * Constructs ...
         *
         * @param model
         */
        public AbstractController(AbstractModel model) {
            super();
            this.attachModel(model);
        }

        /**
         * Method description
         */
        public void attachModel() {
            this.model.attachObserver(this);
        }

        /**
         * Method description
         *
         * @param newModel
         */
        public void attachModel(AbstractModel newModel) {
            this.model = newModel;
            this.attachModel();
        }

        /**
         * Method description
         *
         * @param view
         */
        public void attachView(AbstractView view) {
            this.model.attachView(view);
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean canDetach() {
            return detachable;
        }

        /**
         * Method description
         */
        public void detachModel() {
            this.model.detachObserver(this);
        }

        /**
         * Method description
         *
         * @param view
         */
        public void detachView(AbstractView view) {
            this.model.detachObserver(view);
        }

        /**
         * Method called by observable object during notifyObserver calls.
         */
        @Override
        public void update() {

            // TODO Auto-generated method stub
        }

        /**
         * Method description
         *
         * @param name
         *
         * @return
         */
        public AbstractCommand getCommand(String name) {
            return null;
        }

        /**
         * Method description
         *
         * @return
         */
        public LinkedHashMap<String, AbstractCommand> getCommands() {
            return null;
        }

        /**
         * Method description
         *
         * @return
         */
        public AbstractModel getModel() {
            return this.model;
        }

        /**
         * Attaches a new model after safely detaching an existing one.
         *
         * @param newModel
         * @throws IllegalAccessException
         */
        public void setModel(AbstractModel newModel) throws IllegalAccessException {
            if (this.model != null & !this.canDetach())
                throw new IllegalAccessException("Cannot detach from current model");
            if (this.model != null) detachModel();
            attachModel(newModel);
        }
    }


    /**
     * Models handle the data portion of a component. A model indirectly notifies a controller and any number of views of any changes to the data. The controller is responsible for initiating all attach/detach calls, however the model keeps track of observer view objects with special implementation.
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class AbstractModel extends ObservableObject {

        protected AbstractController	controller;
        protected Set<AbstractView>		views = new HashSet<AbstractView>();

        /**
         * Constructs a new model object with no predefined controller.
         */
        public AbstractModel() {
            super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public AbstractModel(AbstractController controller) {
            this();
            attachController(controller);
        }

        /**
         * Method description
         *
         * @param controller
         */
        protected void attachController(AbstractController controller) {
            this.controller = controller;
        }

        /**
         * Method description
         *
         * @param view
         */
        protected void attachView(AbstractView view) {
            if (views.contains(view)) return;
            super.attachObserver(view);
            views.add(view);
        }

        /**
         * Detaches a properly attached view object. Detaching a view which has not been attached as a view object will cause an InvalidObjectException.
         *
         * @param view
         * @throws InvalidObjectException
         */
        protected void detachView(AbstractView view) throws InvalidObjectException {
            if (!views.contains(view))
                throw new InvalidObjectException("Cannot detach from an unattached view object.");
            super.detachObserver(view);
        }
    }


    /**
     * Views handle the user interface portion of a component. A view directly accesses a controller. A view indirectly accesses a model through the controller. The controller is responsible for all attach/detach calls.
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class AbstractView extends ObservableObject implements Observer {

        protected AbstractController	controller;
        protected AbstractModel			model;

        /**
         * Constructs ...
         *
         * @param controller
         */
        public AbstractView(AbstractController controller) {
            super();
            this.controller = controller;
        }

        /**
         * Method called by observable object during notifyObserver calls.
         */
        @Override
        public void update() {}

        /**
         * Returns model from view's controller
         * @return
         */
        protected AbstractModel getModel() {
            return controller.getModel();
        }
    }


    /**
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     *
     */
    public abstract class ObservableObject implements Observable {

        /** Field description */
        protected Observers	observers = new Observers();

        /**
         * Method description
         *
         * @param observer
         */
        @Override
        public void attachObserver(Observer observer) {

            // TODO Error handling for attach failure
            observers.attachObserver(observer);
        }

        /**
         * Method description
         *
         * @param observer
         */
        @Override
        public void detachObserver(Observer observer) {

            // TODO Error handling for detach failure
            observers.detachObserver(observer);
        }

        /**
         * Method description
         *
         */
        @Override
        public void notifyObservers() {

            // TODO Error handling for notify failure
            observers.notifyObservers();
        }
    }


    /**
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     *
     */
    public class Observers implements Observable {

        /** Field description */
        protected Set<Observer>	observerSet = new HashSet<Observer>();

        /**
         * Method description
         *
         * @param observer
         */
        public void attachObserver(Observer observer) {
            System.out.println("Attach: " + observer);
            observerSet.add(observer);

            // TODO Implement throwing exceptions for attach of existing element
        }

        /**
         * Method description
         *
         * @param observer
         */
        public void detachObserver(Observer observer) {
            observerSet.remove(observer);

            // TODO Implement throwing exceptions for detach of missing element
        }

        /**
         * Method description
         *
         */
        public void notifyObservers() {
            Iterator<Observer>	observerIterator = observerSet.iterator();

            while (observerIterator.hasNext()) {
                Observer	thisObserver = (Observer)observerIterator.next();

                thisObserver.update();
            }

            // TODO Implement throwing exceptions for update of missing element
        }
    }
}
