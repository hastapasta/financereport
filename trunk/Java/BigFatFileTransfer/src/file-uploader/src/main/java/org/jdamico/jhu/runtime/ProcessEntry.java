package org.jdamico.jhu.runtime;

import java.io.File;
import java.util.Observable;

import javax.swing.JOptionPane;

import org.jdamico.jhu.components.FilePartition;
import org.vikulin.utils.Constants;

// This class uploads a file from a URL.
public class ProcessEntry extends Observable implements Runnable {

	// Max size of upload buffer.
	private static final int MAX_BUFFER_SIZE = 1024;

	// These are the status names.
	public static final String STATUSES[] = { "Uploading", "Paused",
			"Complete", "Cancelled", "Error", "Splitting", "Joining", "Calculating Initial Checksums","Waiting for Upload",
			"Waiting for Checksum"};

	// These are the status codes.
	public static final int UPLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;
	public static final int SPLITTING = 5;
	public static final int JOINING = 6;
	public static final int CALCULATION_CHECKSUMS = 7;
	public static final int WAITING_UPLOAD = 8;
	public static final int WAITING_CHECKSUM= 9;
	
	public boolean resume;
	
	public String strBeforeChecksum;
	public String strAfterChecksum;

	private File fileParent;
	private File file; // upload URL
	private long size; // size of upload in bytes
	// private int uploaded; // number of bytes uploaded
	private int status; // current status of upload

	private int progress;

	//private UploadManager uploadManager;

	private volatile Thread thread;

	// Constructor for Upload.
	public ProcessEntry(File file, File fileParent) {
		
		this.file = file;
		this.fileParent= fileParent;
		//this.uploadManager = uploadManager;
		size = -1l;
		
		//this.resume = resume;

		upload();
	}

	// Get this upload's URL.
	public String getUrl() {
		return file.toString();
	}
	
	public String getName() {
		return file.getName();
	}
	
	public String getParentName() {
		return fileParent.getName();
	}

	// Get this upload's size.
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}
	
	public void setBeforeChecksum(String strInput) {
		this.strBeforeChecksum = strInput;
	}
	

	public void setAfterChecksum(String strInput) {
		this.strAfterChecksum = strInput;
	}
	

	public String getBeforeChecksum() {
		return strBeforeChecksum;
	}
	

	public String getAfterChecksum() {
		return strAfterChecksum;
	}

	// Get this upload's progress.
	public float getProgress() {
		return progress;// ((float) uploaded / size) * 100;
	}

	// Get this upload's status.
	public int getStatus() {
		return status;
	}

	// Set this upload's status.
	public void setStatus(int status) {
		if (this.status==ERROR) return;
		this.status = status;
		if (status != COMPLETE)	setProgress(0);
		else stateChanged(); 
	}

	// Pause this upload.
	public void pause() {
		status = PAUSED;
		stateChanged();
	}

	// Resume this upload.
	public void resume() {
		status = UPLOADING;
		stateChanged();
		upload();
	}

	// Cancel this upload.
	public void cancel() {
		status = CANCELLED;
		if (thread != null)
			thread.interrupt();
		stateChanged();
	}

	// Mark this upload as having an error.
	public void error() {
		status = ERROR;
		stateChanged();
	}

	public void error(String message) {
		status = ERROR;
		stateChanged();
		//JOptionPane.showMessageDialog(uploadManager, message, "Error",
				//JOptionPane.ERROR_MESSAGE);
	}

	// Start or resume uploading.
	private void upload() {
		thread = new Thread(this);
		thread.start();
	}

	// Get file name portion of URL.
	private String getFileName(File file) {
		String fileName = file.getName();
		return fileName.substring(fileName.lastIndexOf('/') + 1);
	}

	// Upload file.
	public void run() {
		
	}

	// Notify observers that this upload's status has changed.
	private void stateChanged() {
		setChanged();
		notifyObservers();
	}

	public void setProgress(int percent) {
		this.progress = percent;
		stateChanged();
	}
	

}