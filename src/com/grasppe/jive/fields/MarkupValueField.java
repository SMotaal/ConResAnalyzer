/*
 * @(#)JiveNumericField.java   12/07/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.jive.fields;

import com.google.gdata.util.common.html.HtmlToText;

import com.grasppe.jive.MatLab;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;

/**
 * Class description
 *  @version        $Revision: 1.0, 12/07/07
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class MarkupValueField extends JEditorPane implements ValueField, NamedField, NameValueField {			// PropertyChangeListener,

  protected static com.mathworks.jmi.Matlab matlab         = null;
  protected String                          renderFunction = "Grasppe.Kit.Syntax.Highlight";
  protected String                          plainText, markupText;

  /**
   */
  public MarkupValueField() {
    this("");

    // TODO Auto-generated constructor stub
  }

  /**
   *    @param text
   */
  public MarkupValueField(String text) {
    super("text/html", text);
    
    //java.lang.System.setProperty( 'awt.useSystemAAFontSettings', 'on' );
    super.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);

    setPreferredSize(new Dimension(300, 100));

    final MarkupValueField textField = this;

    getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        updateLabel(e);
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        updateLabel(e);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        updateLabel(e);
      }

      private void updateLabel(DocumentEvent e) {
        java.awt.EventQueue.invokeLater(new Runnable() {

          @Override
          public void run() {

            // textField.setValue(textField.getText());
            // textField.firePropertyChange("text", textField.getValue(), textField.getText());
            textField.updateText();
          }
        });
      }
    });

//  addPropertyChangeListener("text", this);

    addFocusListener(new FocusAdapter() {

      /*
       *  (non-Javadoc)
       *   @see java.awt.event.FocusAdapter#focusLost(java.awt.event.FocusEvent)
       */
      @Override
      public void focusLost(FocusEvent evt) {

        // TODO Auto-generated method stub
        super.focusLost(evt);
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
        	setPlainText(getText());
            renderDisplayText();
          }

        });
      }

      public void focusGained(FocusEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            renderEditText();
            selectAll();
          }

        });
      }

    });

  }

  /**
   *    @param args
   */
  public static void main(String[] args) {

    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        JFrame frame = new JFrame("MarkupValueField Demo");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
        frame.getContentPane().add(new MarkupValueField());			// , BorderLayout.CENTER);
        frame.getContentPane().add(new MarkupValueField());			// , BorderLayout.CENTER);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }

    });
  }

  /**
   */
  protected void renderDisplayText() {
    try {

      // Grasppe.Kit.Syntax.ShowHTML(Grasppe.Kit.Syntax.Highlight('initialize.m'))
    	String plainText = getPlainText();
      String escapeText = plainText; //HtmlToText.htmlToPlainText(plainText);

      // super.getToolkit().
      String escapeStatement = escapeText.replace("'", "''");			// StringEscapeUtils.unescapeHtml(plainText);
      String renderStatement = renderFunction + "(escapeStatement);"; // + escapeStatement + "');";
      String renderResult    = null;

      // if (hasMatlab())
      // renderResult = (String)getMatlab().mtEval(renderStatement, 1);

      // renderResult = (String)Matlab.mtEval(renderStatement, 1);

      MatLab.Initialize();
      
//      System.out.println(renderStatement.replace("\n","\r"));
      
      MatLab.getProxy().setVariable("escapeStatement", escapeStatement);
      
      Object[] results = MatLab.getProxy().returningEval(renderStatement, 1);
      
      

      renderResult = (String)(results[0]);

      setMinimumSize(new Dimension(300, 100));

      super.setContentType("text/html");
      super.setFont(new Font("Monospaced", Font.PLAIN, 11));

      if (renderResult != null) {
        setMarkupText(renderResult);
        super.setText(getMarkupText());
      } else
        renderEditText();

    } catch (Exception exception) {
      //System.out.println(exception);
      //renderEditText();
    }
  }

  /**
   */
  protected void renderEditText() {

    // org.apache.commons.lang.StringEscapeUtils.unescapeHtml(arg0)
	super.setContentType("text/plain");
	  
    super.setText(getPlainText());

    super.setFont(new Font("Monospaced", Font.PLAIN, 11));
  }

  /**
   */
  private void updateText() {
	String currentText = super.getText(); //.replace("</p>", "\n</p>");
	//System.out.println(currentText);
	//System.out.println(HtmlToText.htmlToPlainText(currentText));
    setPlainText(HtmlToText.htmlToPlainText(currentText));
    //firePropertyChange("value", "", super.getText());
  }

  /**
   * @return the markupText
   */
  public String getMarkupText() {
    return markupText;
  }

  /**
   * @return the matlab
   */
  public static com.mathworks.jmi.Matlab getMatlab() {
    return matlab;
  }

  /**
   * @return the plainText
   */
  public String getPlainText() {
    return plainText;
  }

///**
// *      @param evt
// */
//public void propertyChange(PropertyChangeEvent evt) {
//
//  System.out.println(evt);
//
//  if (!(evt.getSource() instanceof JTextField)) return;
//
//  JTextField source = (JTextField)evt.getSource();
//
//  try {
//
//    String value    = source.getText();
//    String newValue = (String)evt.getNewValue();
//    String oldValue = (String)evt.getOldValue();
//
//    if (newValue != oldValue) this.setValue(value);
//
//  } catch (NullPointerException exception) {
//    System.out.println(exception);
//  }
//
//}

  /**
   *    @return
   */
  public String getValue() {
    return super.getText();
  }

  /**
   * 	@return
   */
  public static boolean hasMatlab() {
    return matlab != null;
  }

  /**
   * @param markupText the markupText to set
   */
  public void setMarkupText(String markupText) {
    this.markupText = markupText;
  }

  /**
   * @param matlab the matlab to set
   */
  public static void setMatlab(com.mathworks.jmi.Matlab matlab) {
    MarkupValueField.matlab = matlab;
  }

  /**
   * @param plainText the plainText to set
   */
  public void setPlainText(String plainText) {
    this.plainText = plainText;
  }

  /**
   *    @param value
   */
  @Override
  public void setValue(Object value) {
    setValue((String)value);
  }

  /**
   *      @param value
   */
  public void setValue(String value) {
    String newValue = value;

//  if (super.getText().equals(newValue))
//    return;
//  
//  try {
//
//    //firePropertyChange("value", super.getText(), newValue);
//  
//  } catch (Exception exception) {
//    System.out.println(newValue);
//    System.out.println(exception);
//  }    

    super.setText(newValue);
    
    super.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "<br/>");

    renderDisplayText();

    this.selectAll();
  }

@Override
public void upateValue() {
	// TODO Auto-generated method stub
	
}
}
