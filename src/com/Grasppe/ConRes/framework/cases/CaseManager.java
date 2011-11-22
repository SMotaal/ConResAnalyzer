package com.grasppe.conres.framework.cases;


import java.awt.event.ActionListener;
import java.util.LinkedHashMap;


import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.conres.framework.cases.operations.CloseCase;
import com.grasppe.conres.framework.cases.operations.NewCase;
import com.grasppe.conres.framework.cases.operations.OpenCase;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractModel;

/**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseManager extends AbstractController implements ActionListener {

//        /**
//         * Constructs ...
//         */
//        public CaseManager() {
//            super(new CaseManagerModel());
//        }
//        
//        public CaseManager(ActionListener listener) {
//        	super(new CaseManagerModel(),listener);
//        }
//
//        /**
//         * Constructs ...
//         *
//         * @param model
//         */
//        public CaseManager(CaseManagerModel model) {
//            super(model);
//        }
    	
    	
    	
    	
    	

        /**
	 * 
	 */
	public CaseManager() {
		super();
	}

	/**
	 * @param model
	 * @param listener
	 */
	public CaseManager(AbstractModel model, ActionListener listener) {
		super(model, listener);
	}

	/**
	 * @param model
	 */
	public CaseManager(AbstractModel model) {
		super(model);
	}

	/**
	 * @param listener
	 */
	public CaseManager(ActionListener listener) {
		super(listener);
	}

	/* (non-Javadoc)
	 * @see com.grasppe.AbstractController#getNewModel()
	 */
	@Override
	protected CaseManagerModel getNewModel() {
		return new CaseManagerModel();
	}

		/**
         * Create and populate all commands from scratch.
         */
        public void createCommands() {
            commands = new LinkedHashMap<String, AbstractCommand>();
            putCommand(new NewCase(this, this));
            putCommand(new OpenCase(this, this));
            putCommand(new CloseCase(this));
        }

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
    }