package org.jdamico.jhu.runtime;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/*import org.jdamico.jhu.components.Controller;
import org.jdamico.jhu.components.FilePartition;
import org.vikulin.utils.Constants;*/

// This class renders a JProgressBar in a table cell.
class ProgressRenderer extends JProgressBar implements TableCellRenderer {




	// Constructor for ProgressRenderer.
	public ProgressRenderer(int min, int max) {
		super(min, max);
	}

	/*
	 * Returns this JProgressBar as the renderer for the given table cell.
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// Set JProgressBar's percent complete value.
		setValue((int) ((Float) value).floatValue());
		return this;
	}
	
	/*public void repaint()
	{
		Controller control = new Controller();
		
		for (int i=0;i<tableModel1.getRowCount();i++)
		{
			if (tableModel1.getUpload(i).getStatus() == Upload.UPLOADING)
			{
				for (int j=0;j<tableModel1.getUpload(i).tableModel2.getRowCount();j++)
				{
					ProcessEntry pe = tableModel1.getUpload(i).tableModel2.getProcessEntry(j);
					
					try
					{
						long lCurSize = control.getRemoteFileSize(pe.getName(),Constants.conf.getServerHost(),Constants.conf.getServerPort(),tableModel1.getUpload(i));
						pe.setProgress((int) ((lCurSize / pe.getSize()) * 100));
					}
					catch (Exception e)
					{
						//not going to do anything here
					}
					
					
				}
				
				
			}
			
			
		}
		super.repaint();
	}*/
	
	
}