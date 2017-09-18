import java.io.*;
import java.util.*;

public class Command
{
	private ArrayList<String> commandLine = new ArrayList<String>();
	
	public Command(List<String> list)
	{
		this.commandLine = (ArrayList<String>) list;
	}
	//Ping an ip in parameters
	public String pingIp(String ip, String waitTime ) throws IOException
	{
		this.commandLine = new ArrayList<String>();
		this.commandLine.add("ping");
		this.commandLine.add("-n");
		this.commandLine.add("1");
		this.commandLine.add("-w");
		this.commandLine.add(waitTime);
		this.commandLine.add(ip);
		return ( this.treatIpCommand(this.doCommand()) );
	}
	
	//Treat the result of a ping command
	public String treatIpCommand(String s)
	{
		String s1 = "";
		int cpt = 1;
		int index = 0;

		if ( s == null )
			return "Error";
		
		//Find the line of the ping test
		int indexOfPingLine = s.indexOf("\n", s.indexOf("\n", 0)+1)+1;
		s = s.substring(indexOfPingLine, s.indexOf("\n", indexOfPingLine)+1);

		if( s.indexOf("<") == -1 )
			index = s.indexOf( "=" , s.indexOf("=")+1 ); // Return the position of the second "=" if it is here, else -1
		else
			index = s.indexOf("<");

		if (index > 0) // If we found the second "=" on the line
		{
			cpt = 1;
			while (s.charAt(index+cpt) != ' ' && s.charAt(index+cpt) != 'm')
			{
				s1 += s.charAt(index+cpt);
				cpt ++;
			}
		}
		if (!( s1.equals("") ))
			return s1+" ms";
		else
			return ">1000 ms";
	}
	
	//Treat the result of a memory command
	public String getMemory(String s)
	{
		if ( s == null )
			return "Error";
		
		String[] split = s.split("\n");
		String totalMemory = split[24];
		String freeMemory = split[25];
		
		String memory = totalMemory +"/"+ freeMemory;
		memory = memory.replaceAll("[^0-9/]", "");
		return memory;
	}
	
	//Do the DOS command and return the result the command
	public String doCommand() throws IOException
	{
		String sRet = "";
		String s = "";

		ProcessBuilder pb = new ProcessBuilder(this.commandLine);
		Process process = pb.start();

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

		while((s=stdInput.readLine()) != null)
		{
			sRet += s+"\n";
		}
		return sRet;
	}
}