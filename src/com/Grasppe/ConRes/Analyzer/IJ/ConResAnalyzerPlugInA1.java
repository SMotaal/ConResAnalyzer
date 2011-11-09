/*
 * @(#)ConResAnalyzerPlugInA1.java   11/11/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.Grasppe.ConRes.Analyzer.IJ;

import com.Grasppe.Components;
import com.Grasppe.Components.AbstractCommand;
import com.Grasppe.Components.AbstractController;
import com.Grasppe.Components.AbstractModel;
import com.Grasppe.Components.AbstractView;

import ij.IJ;

import ij.plugin.PlugIn;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 *
 */
public class ConResAnalyzerPlugInA1 implements PlugIn, WindowListener {

    protected ConResAnalyzer	analyzer;
    protected Components		components = new Components();
    protected String			name       = this.getClass().getSimpleName();

    /**
     * @deprecated  Java exits on last window anyway!
     */
    @Deprecated
    public void delayedExit() {
        IJ.showMessage(name, "Exiting if no windows are open in 10 seconds!");

        int				delay         = 10000;		// milliseconds
        ActionListener	taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (Frame.getFrames().length == 0) System.exit(0);
            }
        };

        new Timer(delay, taskPerformer).start();
    }

    /**
     * Method description
     *
     * @param args
     */
    public static void main(String[] args) {

        // TODO Auto-generated method stub
        PlugIn	plugIn = new ConResAnalyzerPlugInA1();

        plugIn.run("");
    }

    /**
     * Method description
     *
     * @param arg
     */
    public void run(String arg) {
        IJ.showMessage(name, "Hello world!");
        analyzer = new ConResAnalyzer();

        ConResAnalyzerModel	analyzerModel = analyzer.getModel();
        ConResAnalyzerView	analyzerView  = new ConResAnalyzerView(analyzer);

        analyzerView.prepareView(this);

        // analyzerView.show();
        // analyzerView.close();
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowActivated(WindowEvent e) {
        IJ.showMessage(name, e.toString());
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowClosed(WindowEvent e) {
        if (Frame.getFrames().length == 0) delayedExit();
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        IJ.showMessage(name, e.toString());
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

        // TODO Auto-generated method stub
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

        // TODO Auto-generated method stub
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowIconified(WindowEvent e) {

        // TODO Auto-generated method stub
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowOpened(WindowEvent e) {

        // TODO Auto-generated method stub
    }

    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseFileManager {}


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseFileManagerModel {}


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseFileManagerViews {}


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseManager extends AbstractController implements ActionListener {

        protected LinkedHashMap<String, AbstractCommand>	commands;
        protected ActionListener							actionListener;
        protected AbstractController						commandHandler = this;

        /**
         * Constructs ...
         */
        public CaseManager() {
            this(new CaseManagerModel());
        }

        /**
         * Constructs ...
         *
         * @param model
         */
        public CaseManager(CaseManagerModel model) {
            components.super(model);
            updateCommands();
        }

        /**
         * Method description
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getCommand(e.getActionCommand()).execute();
            } catch (Exception exception) {
                IJ.showMessage(this.getClass().getSimpleName(),
                               this.getClass().getSimpleName() + " Command Not Found: "
                               + e.getActionCommand());
            }

            if (actionListener != null) actionListener.actionPerformed(e);
        }

        /**
         * Method description
         */
        public void printCommandKeys() {

            /*
             * Enumeration<String> keys = commands.keys();
             *
             * System.out.print("Command keys:\t");
             *
             * while (keys.hasMoreElements()) {
             *
             * /              str = (String) names.nextElement();
             *   System.out.print((String)keys.nextElement() + "\n\t");
             * }
             *
             * System.out.println("");
             */
        }

        /**
         * Method description
         *
         * @param command
         */
        public void putCommand(AbstractCommand command) {
            commands.put(command.getName(), command);
            IJ.showMessage(this.getClass().getSimpleName(),
                           this.getClass().getSimpleName() + " Command Added: " + command.getName()
                           + " :: " + command.toString());
        }

        /**
         * Method description
         */
        public void updateCommands() {
            if (commands == null) {

                // Create all commands from scratch
                commands = new LinkedHashMap<String, Components.AbstractCommand>();		// <String, AbstractCommand>

                // commands.put(NewCase.name, new NewCase(this));
                // commands.put(CloseCase.name, new CloseCase(this));
                // commands.put(OpenCase.name, new OpenCase(this));
                putCommand(new NewCase(this));
                putCommand(new OpenCase(this));
                putCommand(new CloseCase(this));
            }
        }

        /**
         * Method description
         *
         * @param listener
         *
         * @return
         */
        public CaseManager withActionListener(ActionListener listener) {
            actionListener = listener;

            return this;
        }

        /**
         * Method description
         *
         * @param name
         *
         * @return
         */
        public AbstractCommand getCommand(String name) {
            return commands.get(name);
        }

        /**
         * @return the commands
         */
        public LinkedHashMap<String, AbstractCommand> getCommands() {
            return commands;
        }

        /**
         * Method description
         *
         * @return
         */
        @Override
        public CaseManagerModel getModel() {
            return (CaseManagerModel)super.getModel();
        }

        /**
         * Method description
         *
         * @param newModel
         *
         * @throws IllegalAccessException
         */
        public void setModel(CaseManagerModel newModel) throws IllegalAccessException {
            super.setModel(newModel);
        }

        /**
         * Class description
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class CloseCase extends AbstractCommand {

            protected static final String	name = "CloseCase";

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             */
            public CloseCase(ActionListener listener) {
                components.super(listener, name, false);
            }

            /**
             * Method description
             */
            public void execute() {
                super.execute();
                if (!canExecute()) return;
                IJ.showMessageWithCancel(name, "Do you want to close the current case?");
            }

            /**
             * Method description
             */
            @Override
            public void update() {

                // TODO Enable if open case, else disable
                CaseManagerModel	model = getModel();

                canExecute(model.hasCurrentCase());
            }

            /**
             * Method description
             *
             * @return
             */
            public CaseManagerModel getModel() {
                return (CaseManagerModel)model;
            }
        }


        /**
         * Class description
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class NewCase extends AbstractCommand {

            protected static final String	name = "NewCase";

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             */
            public NewCase(ActionListener listener) {
                components.super(listener, name, true);
            }

            /**
             * Method description
             */
            public void execute() {
                super.execute();
                if (!canExecute()) return;

                // TODO: Show imageFolderChooser, if can create new case with images, confirm and close case before creating new case
                // TODO: Show imageFolderChooser
                // TODO: Validate imageFolder structure (if not Show imageFolderChooser)
                // TODO: Confirm and close current case before attempting to create new case
                commandHandler.getCommand("CloseCase").execute();

                // TODO: Create new case in metadata entry state
            }
        }


        /**
         * Class description
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class OpenCase extends AbstractCommand {

            protected static final String	name = "OpenCase";

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             */
            public OpenCase(ActionListener listener) {
                components.super(listener, name, true);
            }

            /**
             * Method description
             */
            public void execute() {
                super.execute();
                if (!canExecute()) return;

                // TODO: Show caseChooser, then confirm and close case before opening chosen case
                // TODO: Show caseFolderChooser, if can create open case, confirm and close case before opening the new case
            }
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseManagerModel extends AbstractModel {

        /** Field description */
        public CaseModel	currentCase;

        /** Field description */
        public CaseModel	newCase;

        /**
         * Constructs a new model object with no predefined controller.
         */
        public CaseManagerModel() {
            components.super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public CaseManagerModel(CaseManager controller) {
            components.super(controller);
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean hasCurrentCase() {
            return (currentCase != null);
        }

        /**
         * Class description
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class CaseModel {

            /** Field description */
            public String	path;

            /** Field description */
            public String	title;
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseManagerView extends AbstractView {

        /**
         * Constructs a new ConResAnalyzerView with a predefined controller.
         *
         * @param controller
         */
        public CaseManagerView(CaseManager controller) {
            components.super(controller);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class ConResAnalyzer extends AbstractController implements ActionListener {

        protected LinkedHashMap<String, AbstractCommand>	commands;
        protected CaseManager								caseManager;

        /**
         * Constructs and attaches a new controller and a new model.
         */
        public ConResAnalyzer() {
            this(new ConResAnalyzerModel());
            caseManager = new CaseManager().withActionListener(this);
        }

        /**
         * Constructs a new controller and attaches it to the unattached model.
         *
         * @param model
         */
        public ConResAnalyzer(ConResAnalyzerModel model) {
            components.super(model);

            // TODO Auto-generated constructor stub
        }

        /**
         * Method description
         *
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            // TODO Handle controller actions
            IJ.showMessage(this.getClass().getSimpleName(),
                           this.getClass().getSimpleName() + " Action Performed: "
                           + e.getActionCommand().toString());
        }

        /**
         * Method description
         */
        public void updateCommands() {
            if (commands != null) {}
        }

        /**
         * Method description
         *
         * @return
         */
        public LinkedHashMap<String, AbstractCommand> getCommands() {
            LinkedHashMap<String, AbstractCommand>	allCommands = new LinkedHashMap<String,
                                                                      AbstractCommand>();

            if (commands != null) allCommands.putAll(commands);
            if (caseManager.getCommands() != null) allCommands.putAll(caseManager.getCommands());

            return allCommands;
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
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class ConResAnalyzerModel extends AbstractModel {

        /**
         * Constructs a new model object with no predefined controller.
         */
        public ConResAnalyzerModel() {
            components.super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public ConResAnalyzerModel(ConResAnalyzer controller) {
            components.super(controller);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class ConResAnalyzerView extends AbstractView {

        /** Field description */
        JFrame				frame;
        ConResAnalyzerMenu	menu;
        String				name        = "ConResAnalyzer";
        boolean				finalizable = true;
        int					activeCalls = 0;

        /**
         * Constructs a new ConResAnalyzerView with a predefined controller.
         *
         * @param controller
         */
        public ConResAnalyzerView(ConResAnalyzer controller) {
            components.super(controller);

            // TODO Auto-generated constructor stub
        }

        /**
         * Method description
         *
         * @param listener
         */
        public void addWindowListener(WindowListener listener) {
            frame.addWindowListener(listener);
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean canFinalize() {
            finalizable = (activeCalls == 0);
            IJ.showMessage(name + " activeCalls: " + activeCalls);

            return finalizable;
        }

        /**
         * Method description
         */
        public void close() {
            if (finalizeView()) frame.dispose();
        }

        /**
         * Completes graphical user interface operations before closing.
         *
         * @return
         */
        public boolean finalizeView() {
            if (!canFinalize()) return false;

            return true;
        }

        /**
         * Hides the graphical frame
         */
        public void hide() {
            frame.setVisible(false);	// frame.toBack();
        }

        /**
         * Builds the graphical user interface window.
         */
        public void prepareFrame() {
            frame = new JFrame(name);
            frame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    close();
                }

            });
        }

        /**
         * Method description
         */
        public void prepareMenu() {
            menu = new ConResAnalyzerMenu();

            LinkedHashMap<String, AbstractCommand>	commands        = controller.getCommands();
            Collection<AbstractCommand>				commandSet      = commands.values();
            Iterator<AbstractCommand>				commandIterator = commandSet.iterator();

            while (commandIterator.hasNext()) {
                AbstractCommand	command = commandIterator.next();

                IJ.showMessage(this.getClass().getSimpleName(),
                               this.getClass().getSimpleName() + " Command Added: "
                               + command.toString());
                menu.createButton(command);
            }
        }

        /**
         * Builds the graphical user interface window and elements.
         */
        public void prepareView() {
            this.prepareMenu();
            this.prepareFrame();
        }

        /**
         * Builds the graphical user interface window and elements and adds the specified WindowListener to the frame.
         *
         * @param listener
         */
        public void prepareView(WindowListener listener) {
            this.prepareView();
            this.addWindowListener(listener);
        }

        /**
         * Shows the graphical frame
         */
        public void show() {
            frame.setVisible(true);		// frame.toFront();
        }

        /**
         * Method called by observable object during notifyObserver calls.
         */
        public void update() {}

        /**
         * @return the title
         */
        protected String getTitle() {
            return frame.getTitle();
        }

        /**
         * @param title the title to set
         */
        protected void setTitle(String title) {
            frame.setTitle(title);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class TargetManager extends AbstractController {

        /**
         * Constructs and attaches a new controller and a new model.
         */
        public TargetManager() {
            this(new TargetManagerModel());
        }

        /**
         * Constructs a new controller and attaches it to the unattached model.
         *
         * @param model
         */
        public TargetManager(TargetManagerModel model) {
            components.super(model);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class TargetManagerModel extends AbstractModel {

        /**
         * Constructs a new model object with no predefined controller.
         */
        public TargetManagerModel() {
            components.super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public TargetManagerModel(TargetManager controller) {
            components.super(controller);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class TargetManagerViews {}
}
