package org.jdamico.jhu.runtime;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.vikulin.runtime.Configuration;

//The Upload Manager.
public class UploadManager extends JFrame implements Observer {
	
	private static final Logger log = Logger.getLogger(UploadManager.class);

//	public static Set<String> uFileName = Collections.synchronizedSet(new HashSet<String>());
	
	private static final long serialVersionUID = 6193105081704707641L;

	// Add upload text field.
	private JTextField addTextField;

	// Upload table's data model.
	private UploadsTableModel tableModel;

	// Table listing uploads.
	private JTable table;

	// These are the buttons for managing the selected download.
	private JButton pauseButton, resumeButton;
	private JButton cancelButton, clearButton;

	// Currently selected upload.
	private Upload selectedUpload;

	// Flag for whether or not table selection is being cleared.
	private boolean clearing;

	private File[] selectedFiles;

	// Constructor for Upload Manager.
	public UploadManager() {
		// Set application title.
		setTitle("Upload Manager");

		// Set window size.
		setSize(640, 480);

		// Handle window closing events.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				actionExit();
			}
		});

		// Set up file menu.
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenuItem fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		fileExitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionExit();
			}
		});
		fileMenu.add(fileExitMenuItem);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);

		// Set up add panel.
		JPanel addPanel = new JPanel();
		
		JButton addMultipleFile = new JButton("...");
		addMultipleFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionAddMultipleFile();
			}
		});
		JButton addFile = new JButton("File");
		addFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionAddFile();
			}
		});

		addPanel.add(addFile);

		addTextField = new JTextField(30);
		addPanel.add(addTextField);
		addPanel.add(addMultipleFile);
		
		JButton addUpload = new JButton("Upload");
		addUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionAddUpload();
			}
		});
		addPanel.add(addUpload);

		// Set up Uploads table.
		tableModel = new UploadsTableModel();
		table = new JTable(tableModel);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						tableSelectionChanged(e);
					}
				});
		// Allow only one row at a time to be selected.
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Set up ProgressBar as renderer for progress column.
		ProgressRenderer renderer = new ProgressRenderer(0, 100);
		renderer.setStringPainted(true); // show progress text
		table.setDefaultRenderer(JProgressBar.class, renderer);

		// Set table's row height large enough to fit JProgressBar.
		table.setRowHeight((int) renderer.getPreferredSize().getHeight());

		// Set up downloads panel.
		JPanel downloadsPanel = new JPanel();
		downloadsPanel.setBorder(BorderFactory.createTitledBorder("Uploads"));
		downloadsPanel.setLayout(new BorderLayout());
		downloadsPanel.add(new JScrollPane(table), BorderLayout.CENTER);

		// Set up buttons panel.
		JPanel buttonsPanel = new JPanel();
		// pauseButton = new JButton("Pause");
		// pauseButton.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// actionPause();
		// }
		// });
		// pauseButton.setEnabled(false);
		// buttonsPanel.add(pauseButton);
		// resumeButton = new JButton("Resume");
		// resumeButton.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// actionResume();
		// }
		// });
		// resumeButton.setEnabled(false);
		// buttonsPanel.add(resumeButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionCancel();
			}
		});
		cancelButton.setEnabled(false);
		buttonsPanel.add(cancelButton);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionClear();
			}
		});
		clearButton.setEnabled(false);
		buttonsPanel.add(clearButton);

		// Add panels to display.
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(addPanel, BorderLayout.NORTH);
		getContentPane().add(downloadsPanel, BorderLayout.CENTER);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

	}

	// Exit this program.
	private void actionExit() {
		System.exit(0);
	}

	// Add a new upload.
	private void actionAddUpload() {
		File file = verifyUri(addTextField.getText());
		if (file != null) {
			tableModel.addUpload(new Upload(file, this));
			addTextField.setText(""); // reset add text field
		} else {
			JOptionPane.showMessageDialog(this, "Invalid the File URL",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Add a new upload.
	private void actionAddMultipleUpload(File[] selectedFiles, boolean onlyFilesSelection) {
		if (this.selectedFiles==null || this.selectedFiles.length==0){
			JOptionPane.showMessageDialog(this, "Selected files are missed",
					"Error", JOptionPane.ERROR_MESSAGE);
		} else
		if (selectedFiles!=null && selectedFiles.length>0)	
			for(File fileToUpload : selectedFiles){
				if (fileToUpload.isDirectory() && !onlyFilesSelection){
					actionAddMultipleUpload(fileToUpload.listFiles(), true);
				} else {
					if (fileToUpload.isFile())
						tableModel.addUpload(new Upload(fileToUpload, this));
				}
			}
		else
			JOptionPane.showMessageDialog(this, "There are no files to upload",
					"Warning", JOptionPane.WARNING_MESSAGE);
	}

	// Add a new file.
	private void actionAddFile() {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println(selectedFile.getName());
			addTextField.setText(selectedFile.getAbsolutePath());
		}
	}
	
	// Add a new file.
	private void actionAddMultipleFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFiles = fileChooser.getSelectedFiles();
			System.out.println(Arrays.deepToString(selectedFiles));
			//perform splitting and upload here
			actionAddMultipleUpload(selectedFiles, false);
		}
	}

	// Verify file to upload.
	private File verifyUri(String uri) {

		File file = null;
		try {
			file = new File(uri);
		} catch (Exception e) {
			return null;
		}

		// Make sure URI specifies a file.
		if (!file.isFile())
			return null;

		return file;
	}

	// Called when table row selection changes.
	private void tableSelectionChanged(ListSelectionEvent e) {
		/*
		 * Unregister from receiving notifications from the last selected
		 * upload.
		 */
		if (selectedUpload != null)
			selectedUpload.deleteObserver(UploadManager.this);

		/*
		 * If not in the middle of clearing a upload, set the selected upload
		 * and register to receive notifications from it.
		 */
		if (!clearing) {
			selectedUpload = tableModel.getUpload(/* table.getSelectedRow() */e
					.getLastIndex());
			selectedUpload.addObserver(UploadManager.this);
			updateButtons();
		}
	}

	// Pause the selected upload.
	private void actionPause() {
		selectedUpload.pause();
		updateButtons();
	}

	// Resume the selected upload.
	private void actionResume() {
		selectedUpload.resume();
		updateButtons();
	}

	// Cancel the selected upload.
	private void actionCancel() {
		selectedUpload.cancel();
		updateButtons();
	}

	// Clear the selected upload.
	private void actionClear() {
		clearing = true;
		tableModel.clearUpload(table.getSelectedRow());
		clearing = false;
		selectedUpload = null;
		updateButtons();
	}

	/*
	 * Update each button's state based off of the currently selected upload's
	 * status.
	 */
	private void updateButtons() {
		if (selectedUpload != null) {
			int status = selectedUpload.getStatus();
			switch (status) {
			case Upload.SPLITTING:
				// pauseButton.setEnabled(false);
				// resumeButton.setEnabled(false);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(false);
				break;
			case Upload.UPLOADING:
				// pauseButton.setEnabled(true);
				// resumeButton.setEnabled(false);
				cancelButton.setEnabled(true);
				clearButton.setEnabled(false);
				break;
			case Upload.PAUSED:
				// pauseButton.setEnabled(false);
				// resumeButton.setEnabled(true);
				cancelButton.setEnabled(true);
				clearButton.setEnabled(false);
				break;
			case Upload.ERROR:
				// pauseButton.setEnabled(false);
				// resumeButton.setEnabled(true);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(true);
				break;
			case Upload.JOINING:
				// pauseButton.setEnabled(false);
				// resumeButton.setEnabled(false);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(false);
				break;
			default: // COMPLETE or CANCELLED or JOINNG
				// pauseButton.setEnabled(false);
				// resumeButton.setEnabled(false);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(true);
			}
		} else {
			// No upload is selected in table.
			// pauseButton.setEnabled(false);
			// resumeButton.setEnabled(false);
			cancelButton.setEnabled(false);
			clearButton.setEnabled(false);
		}
	}

	/*
	 * Update is called when a Upload notifies its observers of any changes.
	 */
	public void update(Observable o, Object arg) {
		// Update buttons if the selected upload has changed.
		if (selectedUpload != null && selectedUpload.equals(o))
			updateButtons();
	}

	// Run the Upload Manager.
	public static void main(String[] args) {
		Configuration.setClientServer(true);
		UploadManager manager = new UploadManager();
		manager.setVisible(true);
	}

}