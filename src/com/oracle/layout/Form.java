/*
 * @(#)Form.java   12/07/16
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.oracle.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

/**
 * An example of using SpringLayout to create a forms-type layout.
 */
public class Form {

  /**
   * Creates a container that uses a SpringLayout to present
   * pairs of components. The resulting layout is similar to
   * that of a form.  For example:
   * <pre>
   * LLLL  RRR
   * LL    RRR
   * L     RRR
   * LLLLL RRR
   * </pre>
   * where the max of the widths of the L (left) components dictates the
   * x location of the R (right) components. The width of the Rs is
   * locked to that of the container so that all extra space is given
   * to them.
   *
   * @param leftComponents the components in the left column
   *                (the first item of each pair);
   *                typically these are labels
   * 	@param initialX
   * 	@param initialY
   * 	@param xPad
   * 	@param yPad
   *
   * @param rightComponents the components in the right column
   *                (the second item of each pair);
   *                typically these are text fields
   *
   * 	@return
   */
  public static Container createForm(Component[] leftComponents, Component[] rightComponents, int initialX, int initialY, int xPad, int yPad) {
    SpringLayout layout  = new SpringLayout();
    int          numRows = Math.max(leftComponents.length, rightComponents.length);

    // The constant springs we'll use to enforce spacing.
    Spring xSpring       = Spring.constant(initialX);
    Spring ySpring       = Spring.constant(initialY);
    Spring xPadSpring    = Spring.constant(xPad);
    Spring yPadSpring    = Spring.constant(yPad);
    Spring negXPadSpring = Spring.constant(-xPad);

    // Create the container and add the components to it.
    JPanel parent = new JPanel(layout);

    for (int i = 0; i < numRows; i++) {
      parent.add(leftComponents[i]);
      parent.add(rightComponents[i]);
    }

    // maxEastSpring will contain the highest min/pref/max values
    // for the right edges of the components in the first column
    // (i.e. the largest X coordinate in a first-column component).
    // We use layout.getConstraint instead of layout.getConstraints
    // (layout.getConstraints(comp).getConstraint("East"))
    // because we need a proxy -- not the current Spring.
    // Otherwise, it won't take the revised X position into account
    // for the initial layout.
    Spring maxEastSpring = layout.getConstraint("East", leftComponents[0]);

    for (int row = 1; row < numRows; row++) {
      maxEastSpring = Spring.max(maxEastSpring, layout.getConstraint("East", leftComponents[row]));
    }

    // Lay out each pair. The left column's x is constrained based on
    // the passed x location. The y for each component in the left column
    // is the max of the previous pair's height. In the right column, x is
    // constrained by the max width of the left column (maxEastSpring),
    // y is constrained as in the left column, and the width is
    // constrained to be the x location minus the width of the
    // parent container. This last constraint makes the right column fill
    // all extra horizontal space.
    SpringLayout.Constraints lastConsL       = null;
    SpringLayout.Constraints lastConsR       = null;
    Spring                   parentWidth     = layout.getConstraint("East", parent);
    Spring                   rWidth          = null;
    Spring                   maxHeightSpring = null;
    Spring                   rX              = Spring.sum(maxEastSpring, xPadSpring);			// right col location
    Spring                   negRX           = Spring.minus(rX);													// negative of rX

    for (int row = 0; row < numRows; row++) {
      SpringLayout.Constraints consL = layout.getConstraints(leftComponents[row]);
      SpringLayout.Constraints consR = layout.getConstraints(rightComponents[row]);

      consL.setX(xSpring);
      consR.setX(rX);

      rWidth = consR.getWidth();		// get the spring that tracks this

      // component's min/pref/max width after
      // setting the X spring but before
      // setting the width spring (to avoid
      // a circularity); we really only
      // need to do this once for the
      // textfield case, since they have the
      // same size info
      // XXX To account for other cases,
      // XXX we should probably take the max
      // XXX of the widths.
      // This is used to set the container's
      // width after this loop.
      consR.setWidth(Spring.sum(Spring.sum(parentWidth, negRX), negXPadSpring));

      if (row == 0) {
        consL.setY(ySpring);
        consR.setY(ySpring);
        maxHeightSpring = Spring.sum(ySpring, Spring.max(consL.getHeight(), consR.getHeight()));
      } else {		// row > 0
        Spring y = Spring.sum(Spring.max(lastConsL.getConstraint("South"), lastConsR.getConstraint("South")), yPadSpring);

        consL.setY(y);
        consR.setY(y);
        maxHeightSpring = Spring.sum(yPadSpring, Spring.sum(maxHeightSpring, Spring.max(consL.getHeight(), consR.getHeight())));
      }

      lastConsL = consL;
      lastConsR = consR;
    }							// end of for loop

    // Wire up the east/south of the container so that the its preferred
    // size is valid.  The east spring is the distance to the right
    // column (rX) + the right component's width (rWidth) + the final
    // padding (xPadSpring).
    // The south side is maxHeightSpring + the final padding (yPadSpring).
    SpringLayout.Constraints consParent = layout.getConstraints(parent);

    consParent.setConstraint("East", Spring.sum(rX, Spring.sum(rWidth, xPadSpring)));
    consParent.setConstraint("South", Spring.sum(maxHeightSpring, yPadSpring));

    return parent;
  }

  /**
   * 	@param args
   */
  public static void main(String[] args) {
    int          numPairs   = 5;
    JLabel[]     labels     = new JLabel[numPairs];
    JTextField[] textFields = new JTextField[numPairs];

    // Create the labels (first column).
    labels[0] = new JLabel("Name:");
    labels[1] = new JLabel("Phone:");
    labels[2] = new JLabel("Fax:");
    labels[2].setFont(labels[2].getFont().deriveFont(Font.PLAIN, 24f));
    labels[3] = new JLabel("Email:");
    labels[4] = new JLabel("Address:");

    // Create the text fields (second column).
    for (int i = 0; i < numPairs; i++) {
      textFields[i] = new JTextField(10);
    }

    // For accessibility's sake, associate each label with its text field.
    for (int i = 0; i < numPairs; i++) {
      labels[i].setLabelFor(textFields[i]);
    }

    // Call the method that does all the layout work.
    Container container = createForm(labels, textFields, 5, 5, 5, 5);

    // Set up the frame and show it.
    JFrame frame = new JFrame("Form");

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(container);
    frame.pack();
    frame.setVisible(true);
  }
}
