package com.grasppe.lure.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
     * Controllers handle the logic portion of a component. A controller directly accesses a model. The controller is responsible for all attach/detach calls. A model is responsible for safely handling attach/detach class for views.
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class AbstractController implements Observer, ActionListener {

        /**
	 * @return the actionListener
	 */
	public ActionListener getActionListener() {
		return actionListener;
	}

	/**
	 * @param actionListener the actionListener to set
	 */
	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	/**
	 * @return the commandHandler
	 */
	public AbstractController getCommandHandler() {
		if (commandHandler==null)
			return this;
		else
			return commandHandler;
	}

	/**
	 * @param commandHandler the commandHandler to set
	 */
	public void setCommandHandler(AbstractController commandHandler) {
		this.commandHandler = commandHandler;
	}

	/**
	 * @param commands the commands to set
	 */
	public void setCommands(LinkedHashMap<String, AbstractCommand> commands) {
		this.commands = commands;
	}

		protected AbstractModel								model;
        protected boolean									detachable = true;
        protected LinkedHashMap<String, AbstractCommand>	commands;
        protected ActionListener							actionListener;
        protected AbstractController						commandHandler;
        
        public AbstractController() {
        	super();
        	attachModel(getNewModel());
        }

        /**
         * Constructs ...
         *
         * @param model
         */
        public AbstractController(AbstractModel model) {
            super();
            attachModel(model);
        }

        public AbstractController(ActionListener listener) {
        	this();
        	attachListener(listener);        	
        }
        
        public AbstractController(AbstractModel model, ActionListener listener) {
        	this(model);
        	attachListener(listener);
        }
        

        /**
         * Called when action event is triggered. An attempt to locally handle AbstractCommand execute will be made using the getCommand(String).execute(). Finally, the action event is passed to actionListener if one is defined, to fulfill the chain of responsibility.
         *
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            try {
                getCommand(e.getActionCommand()).execute();
                GrasppeKit.debugText("Command Event Handled", e.getActionCommand());
            } catch (Exception exception) {
                GrasppeKit.debugText("Command Event Ignored", e.getActionCommand(), 2);
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
        	this.model.attachController(this);
//            this.model.attachObserver(this);
        }
        
        public void attachListener(ActionListener listener) {
        	this.actionListener = listener;
        	updateCommands();
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
                str += keyIterator.next();
            }

            GrasppeKit.debugText("Command Keys (" + getClass().getSimpleName() + ")", str, 2);
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

        public void update() {

            // TODO Auto-generated method stub
        }
        
        public boolean hasModel() {
        	return getModel()!=null;
        }
        
        public boolean hasView() {
        	return getModel().hasViews();
        }
        
        public boolean hasListener() {
        	return actionListener!=null;
        }

        /**
         * Called on instantiation (and other situations in the future) calling both resetCommands() and createCommands() if commands is null or is empty. Updating commands should indirectly occur through observer notifications.
         */
        public void updateCommands() {
            if (commands == null) resetCommands();
            if (commands.isEmpty()) createCommands();
        }

//        /**
//         * Decorator-style assignment of a listener to an AbstractModel.
//         *
//         * @param listener
//         *
//         * @return
//         */
//        public static AbstractController withActionListener(AbstractModel model, ActionListener listener) {
//            AbstractController newController = new AbstractController(model);
//            newController.attachListener(listener);
//            return newController;
//        }

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
        
        protected AbstractModel getNewModel() {
        	return null;
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