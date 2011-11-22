/*
 * @(#)ConResAnalyzer.java   11/11/15
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.analyzer;

import com.grasppe.conres.alpha.ConResBootCamp;
import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.framework.GrasppeKit;

import ij.IJ;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Class description
 *
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ConResAnalyzer extends AbstractController implements ActionListener {

    protected CaseManager	caseManager;

    // protected LinkedHashMap<String, AbstractCommand>  commands;

    /**
     * Constructs and attaches a new controller and a new model.
     */
    public ConResAnalyzer() {
        this(new ConResAnalyzerModel());
        updateCommands();
        caseManager = new CaseManager(this);
    }

    /**
     * Constructs a new controller and attaches it to the unattached model.
     *
     * @param model
     */
    public ConResAnalyzer(ConResAnalyzerModel model) {
        super(model);

        // TODO Auto-generated constructor stub
    }

    /**
     * Create and populate all commands from scratch.
     */
    public void createCommands() {

        // commands = new LinkedHashMap<String, GrasppeKit.AbstractCommand>();
        putCommand(new Quit(this));
        putCommand(new LastPatch(this));
        putCommand(new NextPatch(this));
    }
    
    public void forceCommandUpdates() {
    	Iterator<AbstractCommand> commandIterator = getCommands().values().iterator();
    	
    	while(commandIterator.hasNext())
    		commandIterator.next().update();
    }

    /**
     * Method description
     *
     * @return
     */
    public LinkedHashMap<String, AbstractCommand> getCommands() {
        return appendCommands(caseManager);
    }

    /**
     * Method description
     *
     * @return
     */
    @Override
    public ConResAnalyzerModel getModel() {

        // TODO Auto-generated method stub
        return (ConResAnalyzerModel)super.getModel();
    }

    /**
     * Method description
     *
     * @param newModel
     *
     * @throws IllegalAccessException
     */
    public void setModel(ConResAnalyzerModel newModel) throws IllegalAccessException {

        // TODO Auto-generated method stub
        super.setModel(newModel);
    }

    /**
     * Defines Case Manager's Close Case actions and command, using the EAC pattern.
     *
     * @version        $Revision: 1.0, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public abstract class ConResAnalyzerCommand extends AbstractCommand {

        /**
         * Constructs a realization of AbstractCommand.
         *
         * @param listener
         * @param name
         */
        public ConResAnalyzerCommand(ActionListener listener, String name) {
            super(listener, name, false);
            setModel(((ConResAnalyzer)listener).getModel());
        }

        /**
         * Returns the correctly-cast model.
         *
         * @return
         */
        public ConResAnalyzerModel getModel() {
            return (ConResAnalyzerModel)model;
        }
    }


    /**
     * Last Patch EAC pattern.
     *
     * @version        $Revision: 1.0, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class LastPatch extends ConResAnalyzerCommand {

        protected static final String	name        = "Last Patch";
        protected static final int		mnemonicKey = KeyEvent.VK_COMMA;

        /**
         * Constructs a realization of AbstractCommand.
         *
         * @param listener
         */
        public LastPatch(ActionListener listener) {
            super(listener, name);
            super.mnemonicKey = mnemonicKey;
            executable        = true;
            update();
        }

        /**
         * Performs the command operations when called by execute().
         *
         * @return
         */
        public boolean perfomCommand() {

            // TODO: Replace test code
            ConResBootCamp.magnifyLastPatch();

            return true;	// Action responded to in intended scenario
        }

        /**
         * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
         */
        @Override
        public void update() {
            super.update();
            if (ConResBootCamp.canMagnifyPatch()) {
            	GrasppeKit.debugText("ConResBootCamp.canMagnifyPatch()",3);
            }
            // TODO: Enable if open case, else disable
            canExecute(ConResBootCamp.canMagnifyPatch());
        }
    }


    /**
     * Defines Case Manager's Close Case actions and command, using the EAC pattern.
     *
     * @version        $Revision: 1.0, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class Quit extends ConResAnalyzerCommand {

        protected static final String	name        = "Quit";
        protected static final int		mnemonicKey = KeyEvent.VK_Q;

        /**
         * Constructs a realization of AbstractCommand.
         *
         * @param listener
         */
        public Quit(ActionListener listener) {
            super(listener, name);
            super.mnemonicKey = mnemonicKey;
            executable        = true;
            update();
        }

        /**
         * Performs the command operations when called by execute().
         *
         * @return
         */
        public boolean perfomCommand() {
            if (altPressed() || IJ.showMessageWithCancel(name, "Do you really want to quit?"))
                System.exit(0);

            return true;	// Action responded to in intended scenario
        }

        /**
         * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
         */
        @Override
        public void update() {
            super.update();

            // TODO: Enable if open case, else disable
            canExecute(true);
        }
    }


	/**
	 * Last Patch EAC pattern.
	 *
	 * @version        $Revision: 1.0, 11/11/08
	 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
	 */
	public class NextPatch extends ConResAnalyzerCommand {
	
	    protected static final String	name        = "Next Patch";
	    protected static final int		mnemonicKey = KeyEvent.VK_PERIOD;
	
	    /**
	     * Constructs a realization of AbstractCommand.
	     *
	     * @param listener
	     */
	    public NextPatch(ActionListener listener) {
	        super(listener, name);
	        super.mnemonicKey = mnemonicKey;
	        executable        = true;
	        update();
	    }
	
	    /**
	     * Performs the command operations when called by execute().
	     *
	     * @return
	     */
	    public boolean perfomCommand() {
	
	        // TODO: Replace test code
	        ConResBootCamp.magnifyNextPatch();
	
	        return true;	// Action responded to in intended scenario
	    }
	
	    /**
	     * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
	     */
	    @Override
	    public void update() {
	        super.update();
	
	        // TODO: Enable if open case, else disable
	        canExecute(ConResBootCamp.canMagnifyPatch());
	    }
	}
}
