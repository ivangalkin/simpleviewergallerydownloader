package org.simpleviewergallerydownloader;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

/**
 * Simple UI for {@link Downloader}
 * 
 * @author ivangalkin
 * 
 */
public class SwingUI extends JPanel implements DocumentListener,
		IDownloaderListener {

	private static final long serialVersionUID = 1L;

	private JTextField urlTextField;
	private JTextField pathTextField;
	private JButton downloadButton;
	private JProgressBar progressBar;
	private JFileChooser directoryChooser;
	private JTextArea loggingArea;
	private JLabel statusLabel;

	private int totalPicturesCount = 0;

	/**
	 * Create the panel.
	 */
	public SwingUI() {
		setLayout(new MigLayout("", "[228px,grow][100px]",
				"[15px][][][][][][grow]"));

		JLabel urlTextFieldLabel = new JLabel(
				"URL to Simpleviewer Gallery's XML");
		add(urlTextFieldLabel, "cell 0 0");

		directoryChooser = new JFileChooser();
		directoryChooser.setCurrentDirectory(new java.io.File("."));
		directoryChooser
				.setDialogTitle("Choose a directory for downloaded files");
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		urlTextField = new JTextField();
		urlTextField
				.setToolTipText("URL to Gallery's XML file. Please don't forget to input a protocol like 'http://' or 'file://'");
		urlTextField.getDocument().addDocumentListener(this);
		urlTextFieldLabel.setLabelFor(urlTextField);
		add(urlTextField, "cell 0 1,growx");

		JLabel pathTextFieldLabel = new JLabel("Output directory");
		add(pathTextFieldLabel, "cell 0 2");

		pathTextField = new JTextField();
		pathTextField
				.setToolTipText("Directory where downloaded files will be stored in");
		pathTextField.getDocument().addDocumentListener(this);
		pathTextFieldLabel.setLabelFor(pathTextField);
		add(pathTextField, "cell 0 3,growx");
		pathTextField.setColumns(10);

		JButton selectPathButton = new JButton("Select path");
		selectPathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (directoryChooser.showOpenDialog(SwingUI.this) == JFileChooser.APPROVE_OPTION) {
					pathTextField.setText(directoryChooser.getSelectedFile()
							.getAbsolutePath());
				}
			}
		});
		add(selectPathButton, "cell 1 3");
		progressBar = new JProgressBar();
		add(progressBar, "cell 0 4,growx");

		statusLabel = new JLabel("");
		add(statusLabel, "cell 1 4");

		downloadButton = new JButton("Download");
		downloadButton.setEnabled(false);
		downloadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					final URL xmlUrl = new URL(urlTextField.getText());
					final File downloadDirectory = new File(pathTextField
							.getText());
					new Thread(new Runnable() {
						public void run() {
							new Downloader(xmlUrl, downloadDirectory,
									SwingUI.this);
						}
					}).start();
				} catch (MalformedURLException me) {
					loggingArea
							.append("Malformed URL for XML file was entered\n");
				}
			}
		});
		add(downloadButton, "cell 0 5");

		loggingArea = new JTextArea();
		add(new JScrollPane(loggingArea), "cell 0 6,grow");

	}

	private void checkInput() {
		boolean urlOk = false;
		boolean pathOk = false;

		try {
			new URL(urlTextField.getText());
			urlTextField.setForeground(Color.BLACK);
			urlOk = true;
		} catch (MalformedURLException e1) {
			urlTextField.setForeground(Color.RED);
		}

		pathOk = !pathTextField.getText().isEmpty();

		downloadButton.setEnabled(urlOk && pathOk);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		checkInput();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		checkInput();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		checkInput();
	}

	@Override
	public void onDownloadingError(String errorMessage) {
		loggingArea.append(errorMessage);
		loggingArea.append("\n");
	}

	@Override
	public void setNowDownloading(String image, int number) {
		loggingArea.append(String.format("Downloading file %s (%d/%d)\n",
				image, number + 1, totalPicturesCount));
		statusLabel.setText(String.format("(%d/%d)", number + 1,
				totalPicturesCount));
		progressBar.setValue(number + 1);
	}

	@Override
	public void setTotalPicturesCount(int count) {
		this.totalPicturesCount = count;
		progressBar.setMaximum(count);
	}

}
