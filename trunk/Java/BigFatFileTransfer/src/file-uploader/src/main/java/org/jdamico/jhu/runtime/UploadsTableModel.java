package org.jdamico.jhu.runtime;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

// This class manages the upload table's data.
class UploadsTableModel extends AbstractTableModel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2740117506937325535L;

	// These are the names for the table's columns.
	private static final String[] columnNames = { "Name", "Size", "Progress",
			"Status" };

	// These are the classes for each column's values.
	private static final Class[] columnClasses = { String.class, String.class,
			JProgressBar.class, String.class };

	// The table's list of uploads.
	private ArrayList<Upload> uploadList = new ArrayList<Upload>();

	// Add a new upload to the table.
	public void addUpload(Upload upload) {

		// Register to be notified when the upload changes.
		upload.addObserver(this);

		uploadList.add(upload);

		// Fire table row insertion notification to table.
		fireTableRowsInserted(getRowCount(), getRowCount());
	}

	// Get a upload for the specified row.
	public Upload getUpload(int row) {
		return uploadList.get(row);
	}

	// Remove a upload from the list.
	public void clearUpload(int row) {
		uploadList.remove(row);

		// Fire table row deletion notification to table.
		fireTableRowsDeleted(row, row);
	}

	// Get table's column count.
	public int getColumnCount() {
		return columnNames.length;
	}

	// Get a column's name.
	public String getColumnName(int col) {
		return columnNames[col];
	}

	// Get a column's class.
	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

	// Get table's row count.
	public int getRowCount() {
		return uploadList.size();
	}

	// Get value for a specific row and column combination.
	public Object getValueAt(int row, int col) {

		Upload download = uploadList.get(row);
		switch (col) {
		case 0: // URL
			return download.getUrl();
		case 1: // Size
			long size = download.getSize();
			return (size == -1) ? "" : Long.toString(size);
		case 2: // Progress
			return new Float(download.getProgress());
		case 3: // Status
			return Upload.STATUSES[download.getStatus()];
		}
		return "";
	}

	/*
	 * Update is called when a Upload notifies its observers of any changes
	 */
	public void update(Observable o, Object arg) {
		int index = uploadList.indexOf(o);

		// Fire table row update notification to table.
		fireTableRowsUpdated(index, index);
	}
}