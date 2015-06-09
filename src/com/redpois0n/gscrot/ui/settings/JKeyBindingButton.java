package com.redpois0n.gscrot.ui.settings;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class JKeyBindingButton extends JButton implements KeyListener {

	/**
	 * Maximum keys allowed
	 */
	public static final int MAX_KEYS = 3;

	private int[] keys = new int[MAX_KEYS];
	private int index;

	public JKeyBindingButton() {
		setFocusable(true);

		addKeyListener(this);
		
		super.setPreferredSize(new Dimension(150, 15));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (index >= keys.length) {
			index = 0;
		}

		keys[index++] = e.getKeyCode();

		setKeyText();
	}

	private void setKeyText() {
		String text = "";

		for (int i = 0; i < index; i++) {
			text += KeyEvent.getKeyText(keys[i]);

			if (i + 1 < index) {
				text += " + ";
			}
		}

		setText(text);
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
	
	public int[] getKeys() {
		return this.keys;
	}

}