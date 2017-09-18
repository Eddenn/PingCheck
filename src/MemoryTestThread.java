import java.io.IOException;
import java.util.ArrayList;

public class MemoryTestThread extends Thread{
	
	private static ArrayList<String> memoryCommandLine = new ArrayList<String>();
	private Command memory;
	public void run() {
	    //Memory command initialization
	    memoryCommandLine.add("systeminfo");
		memory = new Command(memoryCommandLine);
        while( IhmGui.bRun )
        {
        	if( IhmGui.bRefresh )
        	{
				try {
					IhmGui.refreshMemoryTest(memory.getMemory(memory.doCommand()));
				} catch (IOException e) {}
        	}
        }
	}
}
