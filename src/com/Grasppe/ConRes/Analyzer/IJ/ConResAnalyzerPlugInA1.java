/*
 * @(#)ConResAnalyzerPlugInA1.java   11/11/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.Grasppe.ConRes.Analyzer.IJ;

import com.Grasppe.GrasppeKit;
import com.Grasppe.GrasppeKit.AbstractCommand;
import com.Grasppe.GrasppeKit.AbstractController;
import com.Grasppe.GrasppeKit.AbstractModel;
import com.Grasppe.GrasppeKit.AbstractOperation;
import com.Grasppe.GrasppeKit.AbstractView;
import com.Grasppe.GrasppeKit.FileSelectionMode;

import ij.IJ;

import ij.plugin.PlugIn;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeSet;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;

/**
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 *
 */
public class ConResAnalyzerPlugInA1 implements PlugIn, WindowListener {

    protected ConResAnalyzer	analyzer;

    // protected GrasppeKit      grasppeKit = GrasppeKit.getInstance();      // .getInstance();
    protected String	name = this.getClass().getSimpleName();

    /**
     * @deprecated  Java exits on last window anyway!
     */
    @Deprecated
    public void delayedExit() {

        // IJ.showMessage(name, "");
        GrasppeKit.debugText("Delayed Exit (" + name + ")",
                             "Exiting if no windows are open in 10 seconds!", 2);

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
        GrasppeKit.debugText("Window Activated (" + name + ")", e.toString());
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowClosed(WindowEvent e) {
        GrasppeKit.debugText("Window Closed (" + name + ")", e.toString());
        if (Frame.getFrames().length == 0) delayedExit();
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        GrasppeKit.debugText("Window Closing (" + name + ")", e.toString());
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowDeactivated(WindowEvent e) {
        GrasppeKit.debugText("Window Deactivated (" + name + ")", e.toString());
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowDeiconified(WindowEvent e) {
        GrasppeKit.debugText("Window Deiconified (" + name + ")", e.toString());
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowIconified(WindowEvent e) {
        GrasppeKit.debugText("Window Iconified (" + name + ")", e.toString());
    }

    /**
     * Method description
     *
     * @param e
     */
    @Override
    public void windowOpened(WindowEvent e) {
        GrasppeKit.debugText("Window Opened (" + name + ")", e.toString());
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


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseManagerModel extends AbstractModel {

        /** Holds a case that is about to close until currentCase is not null */
        public CaseModel	backgroundCase = null;

        /** Field description */
        public CaseModel	currentCase = null;

        /** Field description */
        public CaseModel	newCase = null;

        /**
         * Constructs a new model object with no predefined controller.
         */
        public CaseManagerModel() {
            GrasppeKit.getInstance().super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public CaseManagerModel(CaseManager controller) {
            GrasppeKit.getInstance().super(controller);
        }

        /**
         * Method description
         *
         * @return
         */
        public CaseModel newCaseModel() {
            return new CaseModel();
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean hasCurrentCase() {
            if (currentCase != null)
                GrasppeKit.debugText("Current Case", currentCase.toString(), 3);
            else GrasppeKit.debugText("Current Case", "null!", 3);

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
            GrasppeKit.getInstance().super(controller);
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
            caseManager = (CaseManager)new CaseManager().withActionListener(this);

            // GrasppeKit.setDebugTimeStamp(3);
        }

        /**
         * Constructs a new controller and attaches it to the unattached model.
         *
         * @param model
         */
        public ConResAnalyzer(ConResAnalyzerModel model) {
            GrasppeKit.getInstance().super(model);

            // TODO Auto-generated constructor stub
        }

        /**
         * Create and populate all commands from scratch.
         */
        public void createCommands() {

            // commands = new LinkedHashMap<String, GrasppeKit.AbstractCommand>();
            putCommand(new Quit(this));
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
                GrasppeKit.getInstance().super(listener, name, false);
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
            GrasppeKit.getInstance().super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public ConResAnalyzerModel(ConResAnalyzer controller) {
            GrasppeKit.getInstance().super(controller);
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
            GrasppeKit.getInstance().super(controller);

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

                GrasppeKit.debugText("Command Button Creation",
                                     GrasppeKit.lastSplit(command.toString()), 3);

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
            GrasppeKit.getInstance().super(model);
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
            GrasppeKit.getInstance().super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public TargetManagerModel(TargetManager controller) {
            GrasppeKit.getInstance().super(controller);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class TargetManagerView extends AbstractView {

        /**
         * Constructs ...
         *
         * @param controller
         */
        public TargetManagerView(AbstractController controller) {
            GrasppeKit.getInstance().super(controller);

            // TODO Auto-generated constructor stub
        }
    }
}
