package com.google.code.simpleviewergallerydownloader;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

/**
 * Simple UI for {@link Downloader}
 * 
 * @author ivangalkin
 * 
 */
public class SwingUI extends JPanel implements DocumentListener,
		IDownloaderListener {

	private static final String PATH_HINT = "Directory where downloaded files will be stored in";

	private static final String URL_HINT = "URL to Gallery's XML file. Please don't forget to input a protocol like 'http://' or 'file://'";

	private static final long serialVersionUID = 1L;

	private JTextField urlTextField;
	private JTextField pathTextField;
	private JButton downloadButton;
	private JProgressBar progressBar;
	private JFileChooser directoryChooser;
	private JTextArea loggingArea;
	private JLabel statusLabel;

	private int totalPicturesCount = 0;
	private JCheckBox chckbxProtectedGallery;
	private JLabel lblUsername;
	private JTextField usernameTextField;
	private JLabel lblPassword;
	private JTextField passwordTextField;
	private JCheckBox chckbxEnumeratePictures;

	/**
	 * Create the panel.
	 */
	public SwingUI() {
		setLayout(new MigLayout("", "[300px,grow][100px]",
				"[15px][][][][][][][][][][][][120.00,grow]"));

		JLabel urlTextFieldLabel = new JLabel(
				"URL to Simpleviewer Gallery's XML");
		add(urlTextFieldLabel, "cell 0 0");

		directoryChooser = new JFileChooser();
		directoryChooser.setCurrentDirectory(new java.io.File("."));
		directoryChooser
				.setDialogTitle("Choose a directory for downloaded files");
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		urlTextField = new JTextField();
		urlTextField.setToolTipText(URL_HINT);
		urlTextField.getDocument().addDocumentListener(this);
		urlTextFieldLabel.setLabelFor(urlTextField);
		add(urlTextField, "cell 0 1,growx");

		JLabel pathTextFieldLabel = new JLabel("Output directory");
		add(pathTextFieldLabel, "cell 0 2");

		pathTextField = new JTextField();
		pathTextField.setToolTipText(PATH_HINT);
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

		chckbxEnumeratePictures = new JCheckBox("Enumerate output files");
		add(chckbxEnumeratePictures, "cell 0 4");

		chckbxProtectedGallery = new JCheckBox("Protected gallery");
		add(chckbxProtectedGallery, "cell 0 5");

		lblUsername = new JLabel("Username");
		add(lblUsername, "cell 0 6");

		usernameTextField = new JTextField();
		lblUsername.setLabelFor(usernameTextField);
		add(usernameTextField, "cell 0 7,growx");
		usernameTextField.setColumns(10);

		lblPassword = new JLabel("Password");
		add(lblPassword, "cell 0 8");

		passwordTextField = new JPasswordField();
		lblPassword.setLabelFor(passwordTextField);
		add(passwordTextField, "cell 0 9,growx");
		passwordTextField.setColumns(10);
		progressBar = new JProgressBar();
		add(progressBar, "cell 0 10,growx");

		statusLabel = new JLabel("");
		add(statusLabel, "cell 1 10");

		downloadButton = new JButton("Download");
		downloadButton.setEnabled(false);
		downloadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					final URL xmlUrl = DownloaderUtils.getURL(urlTextField
							.getText());
					final File downloadDirectory = new File(pathTextField
							.getText());
					new Thread(new Runnable() {
						public void run() {
							if (chckbxProtectedGallery.isSelected()) {
								new Downloader(xmlUrl, downloadDirectory,
										usernameTextField.getText(),
										passwordTextField.getText(),
										chckbxEnumeratePictures.isSelected(),
										SwingUI.this);
							} else {
								new Downloader(xmlUrl, downloadDirectory,
										chckbxEnumeratePictures.isSelected(),
										SwingUI.this);
							}
						}
					}).start();
				} catch (MalformedURLException me) {
					loggingArea
							.append("Malformed URL for XML file was entered\n");
				}
			}
		});
		add(downloadButton, "cell 0 11");

		loggingArea = new JTextArea();
		loggingArea.setEditable(false);
		add(new JScrollPane(loggingArea), "cell 0 12,grow");
		initDataBindings();

	}

	private void checkInput() {
		boolean urlOk = false;
		boolean pathOk = false;

		try {
			DownloaderUtils.getURL(urlTextField.getText());
			urlTextField.setForeground(Color.BLACK);
			urlTextField.setToolTipText(URL_HINT);
			urlOk = true;
		} catch (MalformedURLException e) {
			urlTextField.setForeground(Color.RED);
			urlTextField
					.setToolTipText((urlTextField.getText().isEmpty()) ? URL_HINT
							: ExceptionUtils.getMessage(e));
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

	protected void initDataBindings() {
		BeanProperty<JCheckBox, Boolean> jCheckBoxBeanProperty = BeanProperty
				.create("selected");
		BeanProperty<JTextField, Boolean> jTextFieldBeanProperty = BeanProperty
				.create("enabled");
		AutoBinding<JCheckBox, Boolean, JTextField, Boolean> autoBinding = Bindings
				.createAutoBinding(UpdateStrategy.READ, chckbxProtectedGallery,
						jCheckBoxBeanProperty, usernameTextField,
						jTextFieldBeanProperty, "UsernameEnabledBinding");
		autoBinding.bind();
		//
		AutoBinding<JCheckBox, Boolean, JTextField, Boolean> autoBinding_1 = Bindings
				.createAutoBinding(UpdateStrategy.READ, chckbxProtectedGallery,
						jCheckBoxBeanProperty, passwordTextField,
						jTextFieldBeanProperty, "PasswordEnabledBinding");
		autoBinding_1.bind();
	}
}
