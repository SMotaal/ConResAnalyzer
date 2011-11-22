package com.grasppe.conres.framework.cases.operations;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.lure.framework.GrasppeKit;

/**
         * Defines Case Manager's New Case actions and command, using the EAC pattern.
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class OpenCase extends CaseManagerCommand {

            /**
			 * 
			 */
			private CaseManager caseManager;
			protected static final String	name        = "OpenCase";
            protected static final int		mnemonicKey = KeyEvent.VK_O;

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             * @param caseManager TODO
             */
            public OpenCase(CaseManager caseManager, ActionListener listener) {
                super(listener, name);
				this.caseManager = caseManager;
                super.mnemonicKey = mnemonicKey;
                update();
            }

            /**
             * Performs the command operations when called by execute().
             *
             * @return
             */
            public boolean perfomCommand() {
                boolean				canProceed       = canExecute();
                SelectCaseFolder	selectCaseFolder = new SelectCaseFolder();

                GrasppeKit.debugText("Open Case Attempt", "Call SelectCaseFolder", 3);

                canProceed = selectCaseFolder.quickSelect();
                
                if (canProceed)
                    GrasppeKit.debugText("Open Case Selected",
                                         "SelectCaseFolder returned "
                                         + selectCaseFolder.getSelectedFile().getAbsolutePath(), 3);
                else
                    GrasppeKit.debugText("Open Case Cancled", "SelectCaseFolder was not completed",
                                         3);
                if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario

                // TODO: Verify case folder!
                // TODO: Confirm and close current case before attempting to switching cases
                canProceed =
                    ((CloseCase)this.caseManager.getCommandHandler().getCommand("CloseCase")).quickClose(getKeyEvent());
                if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario

                // if a folder is selected!
                getModel().newCase = getModel().newCaseModel();		// getModel().Case//new getModel()canProceed..CaseModel();
                getModel().newCase.path  = selectCaseFolder.getSelectedFile().getAbsolutePath();
                getModel().newCase.title = selectCaseFolder.getSelectedFile().getName();
                getModel().currentCase   = getModel().newCase;
                getModel().notifyObservers();
                GrasppeKit.debugText("Open Case Success",
                                     "Created new CaseModel for " + getModel().currentCase.title
                                     + " and reorganize cases in the case manager model.", 3);

                return true;
            }

            /**
             * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
             */
            @Override
            public void update() {
                super.update();
                canExecute(true);		// getModel().hasCurrentCase());
            }

//          /**
//           * Method description
//           */
//          public void execute() {
//              super.execute();
//              if (!canExecute()) return;
//
//              // TODO: Show caseChooser, then confirm and close case before opening chosen case
//              // TODO: Show caseFolderChooser, if can create open case, confirm and close case before opening the new case
//          }
        }