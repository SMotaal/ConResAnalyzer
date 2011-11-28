package com.grasppe.conres.analyzer.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JFrame;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeKit;

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
            super(controller);

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

            // IJ.showMessage(name + " activeCalls: " + activeCalls);
            GrasppeKit.debugText("Finalize / Active Calls", activeCalls + " remaining.");

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

                @Override
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
            menu.caseLabel.caseManager = getController().getCaseManager();
            getController().getCaseManager().getModel().attachObserver(menu.caseLabel);


            LinkedHashMap<String, AbstractCommand>	commands        = controller.getCommands();
            Collection<AbstractCommand>				commandSet      = commands.values();
            Iterator<AbstractCommand>				commandIterator = commandSet.iterator();

            while (commandIterator.hasNext()) {
                AbstractCommand	command = commandIterator.next();

                GrasppeKit.debugText("Command Button Creation",
                                     GrasppeKit.lastSplit(command.toString()));

//              IJ.showMessage(this.getClass().getSimpleName(),
//                             this.getClass().getSimpleName() + " Command Added: "
//                             + command.toString());
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
        @Override
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
        
        protected ConResAnalyzer getController(){
        	return (ConResAnalyzer)controller;
        }
    }