import java.io.IOException;

public class PingTestThread extends Thread {

	public void run() {
		try {
	        while( IhmGui.bRun )
	        {
	        	if( IhmGui.bRefresh )
	        	{
		        	IhmGui.refreshPing();
		        	try { Thread.sleep(500); }               //Wait 500ms
		        	catch(InterruptedException ex) { Thread.currentThread().interrupt();}
	        	}
	        }
		} catch (IOException e) {}
	} 
}