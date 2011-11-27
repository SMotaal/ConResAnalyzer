package com.grasppe.conres.analyzer.operations;

import java.awt.event.ActionListener;

import com.grasppe.conres.alpha.ConResBootCamp;
import com.grasppe.lure.framework.GrasppeKit;

/**
  * Last Patch EAC pattern.
  *
  * @version        $Revision: 1.0, 11/11/08
  * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
  */
 public class LastPatch extends ConResAnalyzerCommand {
//
     protected static final String	name        = "Last Patch";
//     protected static final int		mnemonicKey = KeyEvent.VK_COMMA;
//
//     /**
//      * Constructs a realization of AbstractCommand.
//      *
//      * @param listener
//      */
     public LastPatch(ActionListener listener) {
         super(listener, name);
//         super.mnemonicKey = mnemonicKey;
//         executable        = true;
//         update();
     }
//
//     /**
//      * Performs the command operations when called by execute().
//      *
//      * @return
//      */
//     @Override
//		public boolean perfomCommand() {
//
//         // TODO: Replace test code
//         ConResBootCamp.magnifyLastPatch();
//
//         return true;	// Action responded to in intended scenario
//     }
//
//     /**
//      * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
//      */
//     @Override
//     public void update() {
//         super.update();
//         if (ConResBootCamp.canMagnifyPatch()) {
//         	GrasppeKit.debugText("ConResBootCamp.canMagnifyPatch()",3);
//         }
//         // TODO: Enable if open case, else disable
//         canExecute(ConResBootCamp.canMagnifyPatch());
//     }
 }