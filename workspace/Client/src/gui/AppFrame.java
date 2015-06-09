package gui;

import java.awt.Color;

import javax.swing.JFrame;

public abstract class AppFrame extends JFrame {
	public abstract void display(String msg, Color color);
	public abstract void updateList();
}
