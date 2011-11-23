package com.grasppe.lure.components;

import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
     * Views handle the user interface portion of a component. A view directly accesses a controller. A view indirectly accesses a model through the controller. The controller is responsible for all attach/detach calls.
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class AbstractView extends ObservableObject implements Observer {

        protected AbstractController	controller;
        protected AbstractModel			model;

//      protected GrasppeKit            grasppeKit     = GrasppeKit.getInstance();

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

        public void update() {}

        /**
         * Returns model from view's controller
         * @return
         */
        protected AbstractModel getModel() {
            return controller.getModel();
        }
    }