package com.grasppe.conres.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.awt.BorderLayout;
import java.awt.event.InputEvent;
import java.util.Collection;

import javax.swing.JMenuBar;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.operations.CloseCase;
import com.grasppe.conres.framework.cases.operations.OpenCase;
import com.grasppe.lure.components.AbstractCommand;
import com.grasppe.lure.framework.GrasppeKit;

import javax.swing.JMenu;

public class AnalysisWindow {

	private JFrame frame;
	private ConResAnalyzer analyzer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AnalysisWindow window = new AnalysisWindow(new ConResAnalyzer());
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AnalysisWindow(ConResAnalyzer analyzer) {
		this.analyzer = analyzer;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		Collection<AbstractCommand> commands = analyzer.getCommands().values();
		
		if (commands.size()>0) { 
			AbstractCommand[] commandsArray = commands.toArray(new AbstractCommand[0]);
			createMenuItems(fileMenu, commandsArray);
		}
	}
	
	public void createMenuItems(JMenu menu, AbstractCommand[] commands) {
		for (AbstractCommand command : commands) {
			JMenuItem menuItem = new JMenuItem(command);
			
			int mnemonic = command.getMnemonicKey();
			
			if (mnemonic>0)
				menuItem.setAccelerator(KeyStroke.getKeyStroke(mnemonic, GrasppeKit.getControlModifierMask()));
			
			menu.add(menuItem);
		}
	}

}
