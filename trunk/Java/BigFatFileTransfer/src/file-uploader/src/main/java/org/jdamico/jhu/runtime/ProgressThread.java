package org.jdamico.jhu.runtime;


public class ProgressThread extends Thread {
	
	private Upload upload;
	
	public boolean done = false;
	
	public ProgressThread(Upload upload)
	{
		this.upload = upload;
		
	}
	
	
	public void run() {
		
			
			while (!done == true)
			{
		
				upload.updateProcessEntitiesProgress();
			
			
				upload.repaint();
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException ie)
				{
					if (done == true)
						break;
					
				}
			}
			
	}
		
		
	

}
