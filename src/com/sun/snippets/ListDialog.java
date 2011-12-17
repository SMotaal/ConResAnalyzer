/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc. All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  - Neither the name of Sun Microsystems nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */



/*
* ListDialog.java is meant to be used by programs such as ListDialogRunner. It
* requires no additional files.
 */

package com.sun.snippets;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.grasppe.lure.framework.GrasppeKit.KeyCode;

/**
 * Use this modal dialog to let the user choose one string from a long list. See
 * ListDialogRunner.java for an example of using ListDialog. The basics:
 * <pre>
 * String[] choices = { &quot;A&quot;, &quot;long&quot;, &quot;array&quot;, &quot;of&quot;, &quot;strings&quot; };
 * String selectedName = ListDialog.showDialog(componentInControllingFrame,
 *     locatorComponent, &quot;A description of the list:&quot;, &quot;Dialog Title&quot;, choices,
 *     choices[0]);
 * </pre>
 */
public class ListDialog extends JDialog implements ActionListener {

    private static ListDialog	dialog;
    private static String		value = "";
    private JList				list;

    /**
     *  @param frame
     *  @param locationComp
     *  @param labelText
     *  @param title
     *  @param data
     *  @param initialValue
     *  @param longValue
     */
    private ListDialog(Frame frame, Component locationComp, String labelText, String title,
                       Object[] data, String initialValue, String longValue) {
        super(frame, title, true);
        
        setUndecorated(true);

        // Create and initialize the buttons.
        final JButton	cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(this);
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            // close the frame when the user presses escape
            public void actionPerformed(ActionEvent e) {
            	cancelButton.doClick();
            }
        }; 
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);

        
        
        final JButton	setButton = new JButton("Set");

        setButton.setActionCommand("Set");
        setButton.addActionListener(this);
        getRootPane().setDefaultButton(setButton);

        // main part of the dialog
        list = new JList(data) {

            // Subclass JList to workaround bug 4832765, which can cause the
            // scroll pane to not let the user easily scroll up to the beginning
            // of the list. An alternative would be to set the unitIncrement
            // of the JScrollBar to a fixed value. You wouldn't get the nice
            // aligned scrolling, but it should work.
            public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation,
                    int direction) {
                int	row;

                if ((orientation == SwingConstants.VERTICAL) && (direction < 0)
                        && (row = getFirstVisibleIndex()) != -1) {
                    Rectangle	r = getCellBounds(row, row);

                    if ((r.y == visibleRect.y) && (row != 0)) {
                        Point	loc = r.getLocation();

                        loc.y--;

                        int			prevIndex = locationToIndex(loc);
                        Rectangle	prevR     = getCellBounds(prevIndex, prevIndex);

                        if ((prevR == null) || (prevR.y >= r.y)) {
                            return 0;
                        }

                        return prevR.height;
                    }
                }

                return super.getScrollableUnitIncrement(visibleRect, orientation, direction);
            }
        };

        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setFont(list.getFont().deriveFont(14.0F));

        if (longValue != null) {
            list.setPrototypeCellValue(longValue + "   ");		// get extra space
        }
        

        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    setButton.doClick();	// emulate button click
                }
            }
        });

        JScrollPane	listScroller = new JScrollPane(list);

        listScroller.setPreferredSize(new Dimension(250, 375));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        // Create a container so that we can add a title around
        // the scroll pane. Can't add a title directly to the
        // scroll pane because its background would be white.
        // Lay out the label and scroll pane from top to bottom.
        JPanel	listPane = new JPanel();

        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));

        JLabel	label = new JLabel(labelText);
        
        label.setFont(label.getFont().deriveFont(16.0F));

        label.setLabelFor(list);
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Lay out the buttons from left to right.
        JPanel	buttonPane = new JPanel();

        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(setButton);

        // Put everything together, using the content pane's BorderLayout.
        Container	contentPane = getContentPane();

        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        // Initialize values.
        setValue(initialValue);
        pack();
        setLocationRelativeTo(locationComp);
    }

    // Handle clicks on the Set and Cancel buttons.

    /**
     *  @param e
     */
    public void actionPerformed(ActionEvent e) {
        if ("Set".equals(e.getActionCommand())) {
            ListDialog.value = (String)(list.getSelectedValue());
        } else {
        	ListDialog.value = "";
        }

        ListDialog.dialog.setVisible(false);
    }

    /**
     * Set up and show the dialog. The first Component argument determines which
     * frame the dialog depends on; it should be a component in the dialog's
     * controlling frame. The second Component argument should be null if you want
     * the dialog to come up with its left corner in the center of the screen;
     * otherwise, it should be the component on top of which the dialog should
     * appear.
     *  @param frameComp
     *  @param locationComp
     *  @param labelText
     *  @param title
     *  @param possibleValues
     *  @param initialValue
     *  @param longValue
     *  @return
     */
    public static String showDialog(Component frameComp, Component locationComp, String labelText,
                                    String title, String[] possibleValues, String initialValue,
                                    String longValue) {
        Frame	frame = JOptionPane.getFrameForComponent(frameComp);

        dialog = new ListDialog(frame, locationComp, labelText, title, possibleValues,
                                initialValue, longValue);
        dialog.setVisible(true);

        return value;
    }

    /**
     *  @param newValue
     */
    private void setValue(String newValue) {
        value = newValue;
        list.setSelectedValue(value, true);
    }
}
