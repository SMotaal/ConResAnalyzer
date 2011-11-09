/*
 * @(#)GrasppeCommon.java   11/11/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.Grasppe;

import ij.IJ;

//~--- JDK imports ------------------------------------------------------------

import com.sun.xml.internal.ws.util.StringUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.InvalidObjectException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;

/**
 * Abstract super-classes with common design patterns.
 *
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class Components {

    /** Field description */
    public static int	debugLevel = 3;		// default level is 3

    /**
     * Output debug text with extended StackTraceElement details.
     *
     * @param text  the body of the debug text
     */
    public static void debugText(String text) {
        debugText(text, 4);
    }

    /**
     * Output debug text with extended StackTraceElement details.
     *
     * @param text  the body of the debug text
     * @param level
     */
    public static void debugText(String text, int level) {
        if (level > debugLevel) return;

        StackTraceElement	caller     = myCaller();
        String				classname  = lastSplit(caller.getClassName());
        String				methodName = caller.getMethodName();
        int					lineNumber = caller.getLineNumber();

        IJ.showMessage(text + "\t\t[" + classname + "." + methodName + ":" + lineNumber + "]");		// text);
    }

    /**
     * Method description
     *
     * @param headline
     * @param text
     */
    public static void debugText(String headline, String text) {
        debugText(headline, text, 4);
    }

    /**
     * Output debug text with extended StackTraceElement details.
     *
     * @param text  the body of the debug text
     * @param headline  the parent component or operation
     * @param level
     */
    public static void debugText(String headline, String text, int level) {
        debugText(headline + ": " + text, level);
    }

    /**
     * Converts from camel case to human readable string. Base on: http://www.malethan.com/article/humanise_camel_case_in_java.html
     *
     *
     * @param text
     *
     * @return
     */
    public static String humanCase(String text) {
        return humanCase(text, false);
    }

    /**
     * Converts from camel case to human readable string. Base on: http://www.malethan.com/article/humanise_camel_case_in_java.html
     *
     * @param text
     * @param titleCase
     *
     * @return
     */
    public static String humanCase(String text, boolean titleCase) {
        Pattern			pattern = Pattern.compile("([A-Z]|[a-z])[a-z]*");
        Vector<String>	tokens  = new Vector<String>();
        Matcher			matcher = pattern.matcher(text);
        String			acronym = "";

        while (matcher.find()) {
            String	found = matcher.group();

            if (found.matches("^[A-Z]$")) acronym += found;
            else {
                if (acronym.length() > 0) tokens.add(acronym);		// we have an acronym to add before we continue
                acronym = "";
                tokens.add(found.toLowerCase());
            }
        }

        if (acronym.length() > 0) tokens.add(acronym);
        if (tokens.size() > 0) text = StringUtils.capitalize(tokens.remove(0));
        for (String s : tokens)
            text += (titleCase) ? " " + StringUtils.capitalize(s)
                                : " " + s;

        return text;
    }

    /**
     * Method description
     *
     * @param text
     *
     * @return
     */
    public static String lastSplit(String text) {
        return lastSplit(text, "\\.");
    }

    /**
     * Method description
     *
     * @param text
     * @param regex
     *
     * @return
     */
    public static String lastSplit(String text, String regex) {
        String[]	splitText = text.split(regex);

        if (splitText.length == 0) return text;

        return splitText[splitText.length - 1];
    }

    /**
     * Traverse the call stack to determine and return where a method was called from.
     *
     * @return fourth stack trace element
     */
    public static StackTraceElement myCaller() {
        StackTraceElement[]	stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement	caller             = stackTraceElements[4];

        return caller;
    }

    /**
     * Traverse and output the call stack.
     */
    public static void showCallStack() {
        StackTraceElement[]	stackTraceElements = Thread.currentThread().getStackTrace();
        String				str                = "";

        for (int i = 2; i < stackTraceElements.length; i++) {
            StackTraceElement	ste        = stackTraceElements[i];
            String				classname  = lastSplit(ste.getClassName());
            String				methodName = ste.getMethodName();
            int					lineNumber = ste.getLineNumber();

            str += "\n\t\t" + classname + "." + methodName + ":" + lineNumber;
        }

        debugText("Call Stack", str, 3);
    }

    /**
     * Traverse the call stack to determine and return where a method was called from.
     *
     * @return the class name, method name, and line number of the fourth stack trace element
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
    public class AbstractCommand extends AbstractAction implements Observer, Observable {

        protected ActionListener	actionListener;
        protected boolean			executable = false;
        protected boolean			executed   = false;
        protected boolean			useModel   = false;
        protected AbstractModel		model;
        protected Observers			observers = new Observers();

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
            executed = false;
            actionListener.actionPerformed(e);
        }

        /**
         * Attaches an observer through the observers object which will include the observer in future update() notify calls.
         *
         * @param observer
         */
        @Override
        public void attachObserver(Observer observer) {
            observers.attachObserver(observer);
        }

        /**
         * Returns the current executable state. Override this method to implement any additional checks needed to determine the executable state.
         *
         * @return  current executable state
         */
        public boolean canExecute() {

            // update();
            return executable;
        }

        /**
         * Sets and returns the current executable state. Observers will be notified only if the canExecute() returned a state different from the initial state. Override the canExecute() method to implement any additional checks when needed.
         *
         * @param state executable state
         *
         * @return  new executable state
         */
        public final boolean canExecute(boolean state) {
            boolean	initialState = executable;

            executable = state;

            boolean	newState = canExecute();

            if (initialState != newState) notifyObservers();

            return newState;
        }

        /**
         * Sets executed to true when called by execute() after perfomCommand() returns true.
         * @return  true only if executed changed from false to true!
         */
        public boolean completed() {
            if (!executed) {
                executed = true;
                debugText("Command Execution Succeeded", lastSplit(toString()), 3);

                return true;
            }

            debugText("Command Execution Duplicity Error", lastSplit(toString()), 2);

            return false;
        }

        /**
         * Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
         *
         * @param observer
         */
        @Override
        public void detachObserver(Observer observer) {
            observers.detachObserver(observer);
        }

        /**
         * Called by the actionListener, following a call to actionPerformed(). Returns false if performCommand() or complete() return false.
         * @return  true if actions completed successfully!
         */
        public final boolean execute() {
            if (hasExecuted()) return false;

            if (!canExecute()) {
                update();
                if (!canExecute())
                    throw new IllegalStateException(getName()
                        + " could not execute in its current state.");
            }

            debugText("Command Execution Started", lastSplit(toString()), 3);

            if (!perfomCommand() ||!completed()) {
                debugText("Command Execution Failed", lastSplit(toString()), 2);

                return false;
            }

            executed = true;
            debugText("Command Execution Ends", lastSplit(toString()), 3);

            return executed;
        }

        /**
         * Method description
         *
         * @param forcedAction
         *
         * @return
         */
        public final boolean execute(boolean forcedAction) {
            debugText("Command Execution Forced", lastSplit(toString()), 3);

            // update();
            // if (forcedAction)
            executed = false;

            // if (forcedAction)
            canExecute(forcedAction);

            return execute();
        }

        /**
         * Detaches from the model when being finalize through garbage collection.
         *
         * @throws Throwable
         */
        @Override
        protected void finalize() throws Throwable {
            try {
                model.detachObserver(this);
                debugText("Command Finalize/Detatch Succeeded",
                          model.getClass().getSimpleName() + " is no longer attached to "
                          + lastSplit(toString()), 3);
            } catch (Exception e) {

                // Command has no model and can finalize immediately
                debugText("Command Finalize/Detatch Unnecessary",
                          "No models were attached to " + lastSplit(toString()), 2);
            }

            super.finalize();
        }

        /**
         * Notifies all observer through the observers object which calls update().
         *
         */
        @Override
        public void notifyObservers() {
            observers.notifyObservers();
        }

        /**
         * Called by execute to complete execution of command actions. This method must be overloaded and return true for the action to complete.
         * @return  false unless otherwise overridden!
         */
        protected boolean perfomCommand() {
            return false;
        }

        /**
         * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
         */
        @Override
        public void update() {
            canExecute(!useModel || (model != null));		// either not using model or model is not empty!
        }

        /**
         * Returns the current model. Override this method with a specific model type.
         *
         * @return
         */
        public AbstractModel getModel() {
            return model;
        }

        /**
         * Returns the name of the command.
         *
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * Called during execute() to determine if the event command has already executed. Override this method to change the execute-once behavior as needed.
         *
         * @return  executed since actionPerofmed()
         */
        public boolean hasExecuted() {
            return executed;
        }

        /**
         * Attaches the command to the specified model and calls update() to reflect the state of the model.
         *
         * @param model
         */
        public void setModel(AbstractModel model) {
            model.attachObserver(this);
            this.model = model;
            useModel   = true;
            update();
        }
    }


    /**
     * Controllers handle the logic portion of a component. A controller directly accesses a model. The controller is responsible for all attach/detach calls. A model is responsible for safely handling attach/detach class for views.
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class AbstractController implements Observer, ActionListener {

        protected AbstractModel								model;
        protected boolean									detachable = true;
        protected LinkedHashMap<String, AbstractCommand>	commands;
        protected ActionListener							actionListener;
        protected AbstractController						commandHandler = this;

        /**
         * Constructs ...
         *
         * @param model
         */
        public AbstractController(AbstractModel model) {
            super();
            this.attachModel(model);
            updateCommands();
        }

        /**
         * Called when action event is triggered. An attempt to locally handle AbstractCommand execute will be made using the getCommand(String).execute(). Finally, the action event is passed to actionListener if one is defined, to fulfill the chain of responsibility.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getCommand(e.getActionCommand()).execute();
                debugText("Command Event Handled", e.getActionCommand(), 3);
            } catch (Exception exception) {
                debugText("Command Event Ignored", e.getActionCommand(), 2);
            }

            if (actionListener != null) actionListener.actionPerformed(e);		// Pass up the chain of responsibility
        }

        /**
         * Method description
         *
         * @param controller
         *
         * @return
         */
        public LinkedHashMap<String,
                             AbstractCommand> appendCommands(AbstractController controller) {
            return appendCommands(null, controller.getCommands());
        }

        /**
         * Method description
         *
         * @param allCommands
         * @param controller
         *
         * @return
         */
        public LinkedHashMap<String, AbstractCommand> appendCommands(LinkedHashMap<String,
                             AbstractCommand> allCommands, AbstractController controller) {
            return appendCommands(allCommands, controller.getCommands());
        }

        /**
         * Appends specified commands map to the controllers commands maps. To use this, override getCommands() to pool commands from controllers down the chain of responsibility using the appendCommands(someController.getCommands())
         * @param   allCommands
         * @param   newCommands
         * @return  allCommands, starting with the controller's commands elements.
         */
        public LinkedHashMap<String, AbstractCommand> appendCommands(LinkedHashMap<String,
                             AbstractCommand> allCommands, LinkedHashMap<String,
                                 AbstractCommand> newCommands) {
            if (allCommands == null) allCommands = new LinkedHashMap<String, AbstractCommand>();
            if (allCommands.isEmpty())
                if ((commands != null) &&!commands.isEmpty()) allCommands.putAll(commands);
            if ((newCommands != null) &&!newCommands.isEmpty()) allCommands.putAll(newCommands);

            return allCommands;
        }

        /**
         * Attaches the controller to the current model object.
         */
        public void attachModel() {

            // TODO: update() on attachModel()
            // TODO: handle attach will fail if model is empty
            this.model.attachObserver(this);
        }

        /**
         * Attaches the controller to the specified model object.
         *
         * @param newModel
         */
        public void attachModel(AbstractModel newModel) {

            // TODO: update() on attachModel()
            // TODO: handle attach will fail if newModel is empty
            this.model = newModel;
            this.attachModel();
        }

        /**
         * Attaches the specified view to the current model.
         *
         * @param view
         */
        public void attachView(AbstractView view) {

            // TODO: update() on attachView()
            // TODO: handle attach will fail if view is empty
            // TODO: handle attach will fail if model is empty
            this.model.attachView(view);
        }

        /**
         * Returns the state of detachable.
         *
         * @return
         */
        public boolean canDetach() {
            return detachable;
        }

        /**
         * Needs to be overridden to populate commands with subclasses of the AbstractCommand using putCommand(new SomeCommand(this)).
         */
        public void createCommands() {

            // putCommand(new CloseCase(this));
            // TODO: Auto populate enclosed AbstractCommands class declarations using getClass().getDeclaredClasses()
        }

        /**
         * Detaches the controller from the current model object.
         */
        public void detachModel() {

            // TODO: update() on detachModel()
            this.model.detachObserver(this);
        }

        /**
         * Method description
         *
         * @param view
         */
        public void detachView(AbstractView view) {

            // TODO: update() on detachView()
            this.model.detachObserver(view);
        }

        /**
         * Outputs the controller's commands.keySet().
         */
        public void printCommands() {
            Iterator<String>	keyIterator = commands.keySet().iterator();		// Set<String> keys = commands.keySet();
            String	str = "";

            while (keyIterator.hasNext()) {
                if (str.length() > 0) str += ", ";
                str += (String)keyIterator.next();
            }

            debugText("Command Keys (" + getClass().getSimpleName() + ")", str, 2);
        }

        /**
         * Adds the specified AbstractCommand to the controller's commands map. Syntax: putCommand(new SomeCommand(this)).
         *
         * @param command
         */
        public void putCommand(AbstractCommand command) {
            commands.put(command.getName(), command);

//          debugText("Command Added", command.toString())
//          IJ.showMessage(this.getClass().getSimpleName(),
//                         this.getClass().getSimpleName() + " Command Added: " + command.getName()
//                         + " :: " + command.toString());
        }

        /**
         * Initializes controller's commands map with new LinkedHashMap of Strings and AbstractCommands. It is called during updateCommands if commands are null.
         */
        public void resetCommands() {
            commands = new LinkedHashMap<String, AbstractCommand>();
        }

        /**
         * Method called by observable object during notifyObserver calls.
         */
        @Override
        public void update() {

            // TODO Auto-generated method stub
        }

        /**
         * Called on instantiation (and other situations in the future) calling both resetCommands() and createCommands() if commands is null or is empty. Updating commands should indirectly occur through observer notifications.
         */
        public void updateCommands() {

            // resetCommands(); // to test the logic
            if (commands == null) resetCommands();
            if (commands.isEmpty()) createCommands();
        }

        /**
         * Decorator-style assignment of a listener to an AbstractModel.
         *
         * @param listener
         *
         * @return
         */
        public AbstractController withActionListener(ActionListener listener) {
            actionListener = listener;

            return this;
        }

        /**
         * Returns the local command with the specified name from the commands map.
         *
         * @param   name is the case-sensitive class name of the AbstractCommand subclass nested within this controller.
         *
         * @return
         */
        public AbstractCommand getCommand(String name) {
            try {
                return commands.get(name.replaceAll(" ", ""));
            } catch (Exception exception) {
                return null;
            }
        }

        /**
         * Returns the commands map. Override this method to pool commands from controllers down the chain of responsibility using the appendCommands(someController.getCommands())
         * @return the commands
         */
        public LinkedHashMap<String, AbstractCommand> getCommands() {
            return commands;
        }

        /**
         * Return the controller's model object.
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
         * Attaches an observer through the observers object which will include the observer in future update() notify calls.
         *
         * @param observer
         */
        @Override
        public void attachObserver(Observer observer) {
            observers.attachObserver(observer);
        }

        /**
         * Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
         *
         * @param observer
         */
        @Override
        public void detachObserver(Observer observer) {
            observers.detachObserver(observer);
        }

        /**
         * Notifies all observer through the observers object which calls update().
         *
         */
        @Override
        public void notifyObservers() {
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
            debugText("Observer Attaching", lastSplit(observer.toString()));
            observerSet.add(observer);

            // TODO Implement throwing exceptions for attach of existing element
        }

        /**
         * Method description
         *
         * @param observer
         */
        public void detachObserver(Observer observer) {
            debugText("Observer Detaching" + lastSplit(observer.toString()));
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
