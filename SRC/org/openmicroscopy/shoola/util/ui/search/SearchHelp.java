/*
 * org.openmicroscopy.shoola.util.ui.search.SearchHelp 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2008 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.util.ui.search;


//Java imports
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.util.ui.IconManager;
import org.openmicroscopy.shoola.util.ui.TitlePanel;
import org.openmicroscopy.shoola.util.ui.UIUtilities;

/** 
 * Dialog presenting how to use the Search widget.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
public class SearchHelp 
	extends JDialog
{
	/** Button to close the window. */
	private JButton closeButton;
	
	private String helpURL = "";
	
	/** Indicates if there was a problem opening the webbrowser */
	private boolean helpWebbrowserError = false;
	

    /** Closes and disposes. */
	private void close()
	{
		setVisible(false);
		dispose();
	}
	
	/** Initializes the components composing the display. */
	private void initComponents()
	{
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				close();
			}
		
		});
		getRootPane().setDefaultButton(closeButton);
	}
	
	/** 
	 * Builds and lays out the main component.
	 * 
	 * @return See above.
	 */
	private JPanel buildMain()
	{
		JPanel content = new JPanel();
		BoxLayout lay = new BoxLayout(content, BoxLayout.PAGE_AXIS);
		content.setLayout(lay);
		content.setBorder(new TitledBorder(""));
		content.add(new JLabel(formatText()));
		content.add(linkout("OMERO Help Website", helpURL));
		return content;
	}
	
	/**
	 * Builds and lays out the various controls.
	 * 
	 * @return See above.
	 */
	private JPanel buildControl()
	{
		JPanel content = new JPanel();
		content.add(closeButton);
		return UIUtilities.buildComponentPanelRight(content);
	}
	
	/** 
	 * Formats and returns the help text.
	 * 
	 * @return See above. 
	 */
	private String formatText()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("<html><body bgcolor=#F0F0F0>");
		buf.append("<h3 bgcolor=#FFFFF0>Wildcard Searches</h3>");
		buf.append("<p>To perform a single character wildcard search, use " +
				"the (\"?\") symbol. For example:</p>");
		buf.append("<p bgcolor=#FFFFFF>Aurora?</p>");
		buf.append("<p><b>Will match items labelled AuroraA and AuroraB</b></p>");
		buf.append("<p>To perform a multiple character wildcard search, " +
				"use the (\"*\") symbol. For example</p>");
		buf.append("<p bgcolor=#FFFFFF>Mito*</p>");
		buf.append("<p><b>Will return any item labelled beginning " +
				"with Mito</b></p>");
		buf.append("<h3 bgcolor=#FFFFF0>AND Searches</h3>");
		buf.append("<p>To search for multiple compulsory terms, use the AND keyword. For example:</p> ");
		buf.append("<p bgcolor=#FFFFFF>GFP AND H2B</p>");
		buf.append("<p><b>Results will contain both terms, GFP and H2B</b></p>");
		buf.append("<p/>");
		buf.append("<p>For more information see:</p>");
		buf.append("</body></html>");
		return buf.toString();
	}
	
	/**
	 * Creates a clickable 'link' label, which opens a browser with
	 * the provided URL
	 * @param name Name of the link
	 * @param url The URL to open
	 * @return
	 */
	private JLabel linkout(final String name, final String url) {
        JLabel l = new JLabel("<html><a href=\"\">" + name + "</a></html>");

        l.setCursor(new Cursor(Cursor.HAND_CURSOR));
        l.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    helpWebbrowserError = true;
                    close();
                }
            }
        });

        return l;
	}
	
	/**
	 * Checks if there was an error opening the webbrowser
	 * @return
	 */
	public boolean hasError() {
	    return helpWebbrowserError;
	}
	
	/** Builds and lays out the UI. */
	private void buildGUI()
	{
		Container c = getContentPane();
		IconManager icons = IconManager.getInstance();
		TitlePanel title = new TitlePanel("Search Tips", "Tips about search",
							icons.getIcon(IconManager.HELP_48));
		c.add(title, BorderLayout.NORTH);
		c.add(buildMain(), BorderLayout.CENTER);
		c.add(buildControl(), BorderLayout.SOUTH);
	}
	
	/** 
	 * Creates a new instance. 
	 * 
	 * @param owner The owner of the frame.
	 * @param helpURL URL for the search help website
	 */
	public SearchHelp(JFrame owner, String helpURL)
	{
		super(owner);
		this.helpURL = helpURL;
		setModal(true);
		setResizable(false);
		initComponents();
		buildGUI();
		//setSize(520, 350);
		pack();
	}
	
}
