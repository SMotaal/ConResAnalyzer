package com.Grasppe.ConRes.Analyzer.IJ;

import ij.IJ;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.TreeSet;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.Grasppe.GrasppeKit;
import com.Grasppe.ConRes.Analyzer.IJ.CaseManager.CaseManagerCommand;
import com.Grasppe.ConRes.Analyzer.IJ.CaseManager.CaseManagerOperation;
import com.Grasppe.ConRes.Analyzer.IJ.CaseManager.CloseCase;
import com.Grasppe.ConRes.Analyzer.IJ.CaseManager.FileChooserOperation;
import com.Grasppe.ConRes.Analyzer.IJ.CaseManager.NewCase;
import com.Grasppe.ConRes.Analyzer.IJ.CaseManager.OpenCase;
import com.Grasppe.ConRes.Analyzer.IJ.CaseManager.SelectCaseFolder;
import com.Grasppe.GrasppeKit.AbstractCommand;
import com.Grasppe.GrasppeKit.AbstractController;
import com.Grasppe.GrasppeKit.AbstractOperation;
import com.Grasppe.GrasppeKit.FileSelectionMode;

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
            GrasppeKit.getInstance().super(model);
        }

//      /**
//       * Method description
//       *
//       * @param e
//       */
//      @Override
//      public void actionPerformed(ActionEvent e) {
//          try {
//              getCommand(e.getActionCommand()).execute();
//          } catch (Exception exception) {
//              IJ.showMessage(this.getClass().getSimpleName(),
//                             this.getClass().getSimpleName() + " Command Not Found: "
//                             + e.getActionCommand());
//          }
//
//          if (actionListener != null) actionListener.actionPerformed(e);
//      }
//
//      /**
//       * Method description
//       *
//       * @param command
//       */
//      public void putCommand(AbstractCommand command) {
//          commands.put(command.getName(), command);
//          IJ.showMessage(this.getClass().getSimpleName(),
//                         this.getClass().getSimpleName() + " Command Added: " + command.getName()
//                         + " :: " + command.toString());
//      }

        /**
         * Create and populate all commands from scratch.
         */
        public void createCommands() {
            commands = new LinkedHashMap<String, GrasppeKit.AbstractCommand>();
            putCommand(new NewCase(this));
            putCommand(new OpenCase(this));
            putCommand(new CloseCase(this));
        }

//
//      /**
//       * Method description
//       *
//       * @param name
//       *
//       * @return
//       */
//      public AbstractCommand getCommand(String name) {
//          return commands.get(name);
//      }
//
//      /**
//       * @return the commands
//       */
//      public LinkedHashMap<String, AbstractCommand> getCommands() {
//          return commands;
//      }

        /**
         * Return the controller's correctly-cast model
         *
         * @return
         */
        @Override
        public CaseManagerModel getModel() {
            return (CaseManagerModel)super.getModel();
        }

        /**
         * Sets controller's model to a CaseManagerModel and not any AbstractModel.
         *
         * @param newModel
         *
         * @throws IllegalAccessException
         */
        public void setModel(CaseManagerModel newModel) throws IllegalAccessException {
            super.setModel(newModel);
        }

        /**
         * Defines Case Manager's Close Case actions and command, using the EAC pattern.
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public abstract class CaseManagerCommand extends AbstractCommand {

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             * @param name
             */
            public CaseManagerCommand(ActionListener listener, String name) {
                GrasppeKit.getInstance().super(listener, name, false);
                setModel(((CaseManager)listener).getModel());
            }

            /**
             * Returns the correctly-cast model.
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
         * @version        $Revision: 1.0, 11/11/09
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public abstract class CaseManagerOperation extends AbstractOperation {

            /**
             * @param name
             */
            public CaseManagerOperation(String name) {
                GrasppeKit.getInstance().super(name);
            }
        }


        /**
         * Defines Case Manager's Close Case actions and command, using the EAC pattern.
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class CloseCase extends CaseManagerCommand {

            protected static final String	name        = "CloseCase";
            protected static final int		mnemonicKey = KeyEvent.VK_C;

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             */
            public CloseCase(ActionListener listener) {
                super(listener, name);
                super.mnemonicKey = mnemonicKey;
                update();
            }

            /**
             * Performs the command operations when called by execute().
             *
             * @return
             */
            public boolean perfomCommand() {
                boolean	canProceed = !isCaseClosed();		// canExecute();

                GrasppeKit.debugText("Close Case Attempt", "will be checking isCaseClosed()", 4);
                if (!canProceed) return true;		// Action responded to in alternative scenario
                if (!altPressed())
                    canProceed = IJ.showMessageWithCancel(name,
                        "Do you want to close the current case?");
                if (!canProceed) return true;		// Action responded to in alternative scenario
                GrasppeKit.debugText("Close Case Proceeds", "User confirmed close.", 3);
                getModel().backgroundCase = getModel().currentCase;
                getModel().currentCase    = null;
                GrasppeKit.debugText("Closed Case Success",
                                     "Moved current case to background and cleared current case.",
                                     4);
                getModel().notifyObservers();

                // update();
                return true;	// Action responded to in intended scenario
            }

            /**
             * Method description
             *
             * @param keyEvent
             *
             * @return
             */
            public boolean quickClose(KeyEvent keyEvent) {
                boolean	canProceed;

                try {
                    canProceed = execute(true, keyEvent);		// Don't care if a case was closed
                    canProceed = isCaseClosed();				// Only care that no case is open!

                    // getModel().notifyObservers();
                } catch (Exception e) {

                    // forget about current case!
                    GrasppeKit.debugText("Close Case Attempt",
                                         "Failed to close case or no case was open!" + "\n\n"
                                         + e.toString(), 2);
                    canProceed = false;
                }

                return canProceed;
            }

            /**
             * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
             */
            @Override
            public void update() {
                super.update();

                // TODO: Enable if open case, else disable
                canExecute(!isCaseClosed());	//

                // notifyObservers();
            }

            /**
             * Method description
             *
             * @return
             */
            public boolean isCaseClosed() {
                boolean	value = !(getModel().hasCurrentCase());

                GrasppeKit.debugText("isCaseClose", "" + value, 3);

                return value;
            }
        }


        /**
         * Class description
         *
         * @version        $Revision: 1.0, 11/11/10
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public abstract class FileChooserOperation extends CaseManagerOperation {

            String	defaultChooserPath =
                "/Users/daflair/Documents/MATLAB/ConResAlpha/data/samples/Approval_Scans_ConRes26_FS";
            File				selectedFile;
            protected boolean	executable = true;
            JFileChooser		fileChooser;
            FileSelectionMode	fileSelectionMode = FileSelectionMode.FILES_AND_DIRECTORIES;
            TreeSet<FileFilter>	filters           = new TreeSet<FileFilter>();

            /**
             * Constructs ...
             *
             * @param name
             */
            public FileChooserOperation(String name) {
                super(name);
                prepareFileChooser();
            }

            /*
             *  (non-Javadoc)
             * @see com.Grasppe.GrasppeKit.AbstractOperation#perfomOperation()
             */

            /**
             * Method description
             *
             * @return
             */
            @Override
            protected boolean perfomOperation() {
                prepareFileChooser();
                if (fileChooser.showOpenDialog(GrasppeKit.commonFrame)
                        == JFileChooser.CANCEL_OPTION)
                    return false;

                // TODO: Inspect & Verify Scan Images / TDF are in selectedFile
                selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.isDirectory()) selectedFile = selectedFile.getParentFile();

                return true;
            }

            /**
             * Method description
             */
            public void prepareFileChooser() {
                fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(fileSelectionMode.value());

                // Add filters
                while (filters.iterator().hasNext())
                    fileChooser.addChoosableFileFilter(filters.iterator().next());

                // Setting initial chooser selection
                try {
                    File	defaultPath = new File(defaultChooserPath);

                    fileChooser.setSelectedFile(defaultPath);
                } catch (NullPointerException exception) {

                    // Not setting initial chooser selection
                }
            }

            /**
             * Method description
             *
             * @return
             */
            public boolean quickSelect() {
                boolean	canProceed = false;
                String	finalName  = GrasppeKit.humanCase(getName());

                try {
                    canProceed = execute(true);
                } catch (Exception e) {
                    GrasppeKit.debugText(finalName + " Failed",
                                         finalName + " threw a " + e.getClass().getSimpleName()
                                         + "\n\n" + e.toString(), 2);
                }

                return canProceed;
            }

            /**
             * @return the selectedFile
             */
            protected File getSelectedFile() {
                return selectedFile;
            }

            /**
             * @param selectedFile the selectedFile to set
             */
            protected void setSelectedFile(File selectedFile) {
                this.selectedFile = selectedFile;
            }
        }


        /**
         * Defines Case Manager's New Case actions and command, using the EAC pattern.
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class NewCase extends CaseManagerCommand {

            protected static final String	name        = "NewCase";
            protected static final int		mnemonicKey = KeyEvent.VK_N;

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             */
            public NewCase(ActionListener listener) {
                super(listener, name);
                super.mnemonicKey = mnemonicKey;
                update();
            }

            /**
             * Performs the command operations when called by execute().
             *
             * @return
             */
            public boolean perfomCommand() {
                boolean	canProceed = canExecute();

                // if (!canProceed) return true;     // Action responded to in alternative scenario
                // TODO: Show imageFolderChooser, if can create new case with images, confirm and close case before creating new case
                // TODO: Show imageFolderChooser
                // TODO: Validate imageFolder structure (if not Show imageFolderChooser)
//              try {
//                  CloseCase closeCase = (CloseCase)commandHandler.getCommand("CloseCase");
//
//                  canProceed = closeCase.execute(false, getKeyEvent());     // Don't care if a case was closed
//                  canProceed = closeCase.isCaseClosed();        // Only care that no case is open!
//                  getModel().notifyObservers();
//              } catch (IllegalStateException e) {
//                  GrasppeKit.debugText("New Case Attempt",
//                                       "Failed to close case or no case was open!", 3);
//                  canProceed = true;
//              } catch (Exception e) {
//
//                  // forget about current case!
//                  GrasppeKit.debugText("New Case Attempt",
//                                       "Failed to close case or no case was open!" + "\n\n"
//                                       + e.toString(), 2);
//                  canProceed = false;
//              }
                // TODO: Confirm and close current case before attempting to switching cases
                canProceed =
                    ((CloseCase)commandHandler.getCommand("CloseCase")).quickClose(getKeyEvent());
                if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario

                // TODO: Create new case in metadata entry state
                GrasppeKit.debugText("New Case Creation",
                                     "New case will be created and passed for metadata entry", 4);

                try {
                    getModel().newCase = getModel().newCaseModel();		// getModel().Case//new getModel()canProceed..CaseModel();
                    getModel().notifyObservers();
                    canProceed = true;
                } catch (Exception e) {
                    GrasppeKit.debugText("New Case Failure",
                                         "Failed to create new case" + "\n\n" + e.toString(), 2);
                    canProceed = false;
                }

                if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario

                try {
                    getModel().currentCase    = getModel().newCase;		// Make current the new case
                    getModel().newCase        = null;					// Clear new case
                    getModel().backgroundCase = null;					// clear background case
                    canProceed                = true;
                    getModel().notifyObservers();
                } catch (Exception e) {
                    GrasppeKit.debugText("New Case Failure",
                                         "Failed to reorganize cases in the case manager model."
                                         + "\n\n" + e.toString(), 2);
                    canProceed = false;
                }

                if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario
                GrasppeKit.debugText(
                    "New Case Success",
                    "Created new case and reorganize cases in the case manager model.", 3);

                return true;	// Action responded to in intended scenario
            }

            /**
             * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
             */
            @Override
            public void update() {
                super.update();

                // TODO: Enable if open case, else disable
                canExecute(true);		// getModel().hasCurrentCase());
            }

//          /**
//           * @return the mnemonicKey
//           */
//          @Override
//          public int getMnemonicKey() {
//              return mnemonicKey;
//          }
        }


        /**
         * Defines Case Manager's New Case actions and command, using the EAC pattern.
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class OpenCase extends CaseManagerCommand {

            protected static final String	name        = "OpenCase";
            protected static final int		mnemonicKey = KeyEvent.VK_O;

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             */
            public OpenCase(ActionListener listener) {
                super(listener, name);
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

//              try {
//                  canProceed = selectCaseFolder.execute(true);
//              } catch (Exception e) {
//                  GrasppeKit.debugText("Open Case Failed",
//                                       "SelectCaseFolder threw a " + e.getClass().getSimpleName()
//                                       + "\n\n" + e.toString(), 2);
//              }
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
                    ((CloseCase)commandHandler.getCommand("CloseCase")).quickClose(getKeyEvent());
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


        /**
         * Class description
         *
         * @version        $Revision: 1.0, 11/11/09
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class SelectCaseFolder extends FileChooserOperation {

            protected static final String	name = "SelectCaseFolder";

//          String                            defaultCaseFolder =
//              "/Users/daflair/Documents/MATLAB/ConResAlpha/data/samples/Approval_Scans_ConRes26_FS";
//          File              selectedFile;
//          protected boolean executable = true;
            protected static final String	defaultChooserPath =
                "/Users/daflair/Documents/MATLAB/ConResAlpha/data/samples/Approval_Scans_ConRes26_FS";

            // File              selectedFile;
            protected boolean	executable = true;

            // JFileChooser      fileChooser;
            protected final FileSelectionMode	fileSelectionMode =
                FileSelectionMode.DIRECTORIES_ONLY;

            // TreeSet<FileFilter>   filters           = new TreeSet<FileFilter>();

            /**
             * Constructs ...
             */
            public SelectCaseFolder() {
                super(name);
            }
        }
    }