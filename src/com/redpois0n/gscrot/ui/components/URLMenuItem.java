package com.redpois0n.gscrot.ui.components;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class URLMenuItem extends JMenuItem implements ActionListener {
	
	private String url;
	
	public URLMenuItem(String text, String url) {
		this(text, null, url);
	}
	
	public URLMenuItem(String text, ImageIcon icon, String url) {
		super(text, icon);
		this.url = url;
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
