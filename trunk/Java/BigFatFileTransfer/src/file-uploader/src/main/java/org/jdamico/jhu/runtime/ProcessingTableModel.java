package org.jdamico.jhu.runtime;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

// This class manages the upload table's data.
public class ProcessingTableModel extends AbstractTableModel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2740117506937325535L;

	// These are the names for the table's columns.
	private static final String[] columnNames = { "Parent File","Name", "Size", "Progress",
			"Status","Before Checksum","After Checksum" };

	// These are the classes for each column's values.
	private static final Class[] columnClasses = { String.class,String.class, String.class,
			JProgressBar.class, String.class, String.class,String.class};

	// The table's list of uploads.
	private ArrayList<ProcessEntry> processFileList = new ArrayList<ProcessEntry>();

	// Add a new upload to the table.
	public void addProcessFile(ProcessEntry processentry) {

		// Register to be notified when the upload changes.
		processentry.addObserver(this);

		processFileList.add(processentry);

		// Fire table row insertion notification to table.
		fireTableRowsInserted(getRowCount(), getRowCount());
	}

	// Get a upload for the specified row.
	public ProcessEntry getProcessEntry(int row) {
		return processFileList.get(row);
	}

	// Remove a upload from the list.
	public void clearProcessEntry(int row) {
		processFileList.remove(row);

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
		return processFileList.size();
	}

	// Get value for a specific row and column combination.
	public Object getValueAt(int row, int col) {

		ProcessEntry pEntry = processFileList.get(row);
		switch (col) {
		case 0: //Upload File Name
			return pEntry.getParentName();
		case 1: // Split File Name
			return pEntry.getName();
		case 2: // Size
			long size = pEntry.getSize();
			return (size == -1) ? "" : Long.toString(size);
		case 3: // Progress
			return new Float(pEntry.getProgress());
		case 4: // Status
			return ProcessEntry.STATUSES[pEntry.getStatus()];
		case 5:
			return pEntry.getBeforeChecksum();
		case 6:
			return pEntry.getAfterChecksum();
		}
		return "";
	}

	/*
	 * Update is called when a Upload notifies its observers of any changes
	 */
	public void update(Observable o, Object arg) {
		int index = processFileList.indexOf(o);

		// Fire table row update notification to table.
		fireTableRowsUpdated(index, index);
	}
}