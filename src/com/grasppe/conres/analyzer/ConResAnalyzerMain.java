/*
 * @(#)ConResAnalyzerMain.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer;

import com.grasppe.conres.alpha.ConResBootCamp;
import com.grasppe.conres.analyzer.model.ConResAnalyzerModel;
import com.grasppe.conres.analyzer.view.ConResAnalyzerView;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/03
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ConResAnalyzerMain {
    /**
     * @param args
     */
    public static void main(String[] args) {
        GrasppeKit.setupHooks();
//        new ConResBootCamp().run("");
        ConResAnalyzer analyzer = new ConResAnalyzer();
        ConResAnalyzerModel	analyzerModel = analyzer.getModel();
        ConResAnalyzerView	analyzerView  = (ConResAnalyzerView) analyzer.getView(); //new ConResAnalyzerView(analyzer);

    }
}
