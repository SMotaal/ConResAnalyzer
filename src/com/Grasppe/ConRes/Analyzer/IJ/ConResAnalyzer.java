package com.Grasppe.ConRes.Analyzer.IJ;

import ij.IJ;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;

import com.Grasppe.GrasppeKit;
import com.Grasppe.ConRes.Analyzer.IJ.ConResAnalyzer.ConResAnalyzerCommand;
import com.Grasppe.ConRes.Analyzer.IJ.ConResAnalyzer.Quit;
import com.Grasppe.GrasppeKit.AbstractCommand;
import com.Grasppe.GrasppeKit.AbstractController;

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