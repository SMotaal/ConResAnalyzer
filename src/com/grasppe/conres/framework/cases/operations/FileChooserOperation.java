package com.grasppe.conres.framework.cases.operations;

import java.io.File;
import java.util.TreeSet;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.FileSelectionMode;

/**
 * Class description
 *
 * @version        $Revision: 1.0, 11/11/10
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public abstract class FileChooserOperation extends CaseManagerOperation {

    String	defaultChooserPath = CaseManagerModel.defaultChooserPath;
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
     * @see com.grasppe.GrasppeKit.AbstractOperation#perfomOperation()
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
        
        if (!verifySelection(fileChooser.getSelectedFile()))
        	return false;

        // TODO: Inspect & Verify Scan Images / TDF are in selectedFile
        selectedFile = fileChooser.getSelectedFile();
        if (!selectedFile.isDirectory()) selectedFile = selectedFile.getParentFile();

        return true;
    }
    
    protected boolean verifySelection(File selectedFile) {
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
    public File getSelectedFile() {
        return selectedFile;
    }

    /**
     * @param selectedFile the selectedFile to set
     */
    protected void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }
}