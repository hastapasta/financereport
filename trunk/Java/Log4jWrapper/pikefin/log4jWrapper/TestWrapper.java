package pikefin.log4jWrapper;

public class TestWrapper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		MainWrapper cbw = new MainWrapper();
		
		cbw.writeln("STATUS1 message",Logs.STATUS1,"TW1");
		cbw.writeln("STATUS2 message",Logs.STATUS2,"TW2");
		cbw.writeln("WARN message",Logs.WARN,"TW3");
		cbw.writeln("ERROR message",Logs.ERROR,"TW4");
		cbw.writeln("SQL message",Logs.SQL,"TW5");
		//cbw.writeln("FULL message",Logs.FULL,"TW6");
		cbw.writeln("THREAD message",Logs.THREAD,"TW7");
		
		cbw.logStatus1("STATUS1 message","TW8");
		cbw.logStatus2("STATUS2 message","TW9");
		cbw.logWarn("WARN message","TW10");
		cbw.logError("ERROR message","TW11");
		cbw.logSQL("SQL message","TW12");
		//cbw.logFull("FULL message","TW13");
		cbw.logThread("THREAD message","TW14");
		
		
		cbw.writeln(new TestException());
		

	}
	


}

class TestException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4547042436535280724L;

	public String getMessage() {
		return("This is the TestException class.");
	}
}
