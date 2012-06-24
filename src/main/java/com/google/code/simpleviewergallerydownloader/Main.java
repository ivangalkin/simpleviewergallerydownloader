package com.google.code.simpleviewergallerydownloader;

import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * Start {@link Downloader} with {@link SwingUI}
 * 
 * @author ivangalkin
 * 
 */
public class Main {

	/**
	 * Create the application.
	 */
	public Main() {
		JFrame frame = new JFrame("Downloader for SimpleviewerGallery");
		frame.setBounds(100, 100, 450, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new SwingUI());
		frame.setVisible(true);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Main();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

}
