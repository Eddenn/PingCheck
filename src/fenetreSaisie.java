import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class fenetreSaisie extends JFrame{
	//Arguments
	public JTextField tfIp = new JTextField();
	public JTextField tfPingMax = new JTextField();
	public JButton bValider = new JButton("Ok");
	//Constructor
	public fenetreSaisie(IhmGui ihm)
	{
		 this.setTitle("Seizure of IP");
		 this.setSize(250, 100);
		 this.setLocation(800,270);             
		 this.setResizable(false);
		 addWindowListener( new WindowAdapter() {	public void windowClosing(WindowEvent w) {
				 										IhmGui.bRefresh = true;
			 										}}
				 		  );
		 this.setLayout(new BorderLayout());
		 add(tfIp , BorderLayout.CENTER);
		 bValider.addActionListener(ihm);
		 add(bValider , BorderLayout.EAST);
		 add(new JLabel("Write an ip or an address to ping.") , BorderLayout.SOUTH);
		 
		 setVisible(true);
	}
	//Get ip in the textfield (tfIp)
	public String getIp()
	{
		return tfIp.getText();
	}
}
