import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("serial")
public class IhmGui extends JFrame implements ActionListener
{
	//Arguments
	private static JPanel pGlobal;
	private static GridLayout lGlobal = new GridLayout(0,1);
	private static ArrayList<JLabel> alIp = new ArrayList<JLabel>();
	private static ArrayList<JLabel> alPing = new ArrayList<JLabel>();
	private static ArrayList<JPanel> alPan = new ArrayList<JPanel>();
	public  static boolean bRefresh = true;
	public  static boolean bRun = true;
	private static fenetreSaisie saisie;
	private static Command ping = new Command(null);
	private static JMenuItem iMenuAction1;
	private static JMenuItem iMenuAction2;
	private static JMenuItem iMenuAction3;
	private static JPanel graphExtentionPanel;
	private static GraphPanel graph;
	private static final int STEPS = 15;
	private static int[] tabPings = new int[STEPS];
	private static final String DEFAULT_IP = "www.google.fr";
	private static GridLayout mainLayout;
	
	//Constructor
	public IhmGui() throws IOException
	{
	    this.setTitle("PingCheck");
	    this.setSize(250, 90);
	    this.setLocation(800,400);             
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    mainLayout = new GridLayout(1,1);
	    this.setLayout(mainLayout);
	    
	    //Base font
	    Font font = new Font("Arial",Font.BOLD,15);
	    
	    //Graph
	    graphExtentionPanel = new JPanel(new BorderLayout());
	    Arrays.fill(tabPings, 0);
	    graph = new GraphPanel(tabPings,300,200,STEPS,10);
	    JLabel graphTitle = new JLabel("Graphic of "+DEFAULT_IP);
	    graphTitle.setFont(font);
	    graphExtentionPanel.add(graphTitle,BorderLayout.NORTH);
	    graphExtentionPanel.add(graph,BorderLayout.CENTER);
	    
	    /*-- Menu --*/
	    JMenuBar menuBar = new JMenuBar();
	    JMenu menuActions = new JMenu("Actions");
	    iMenuAction1 = new JMenuItem("Add an IP");
	    iMenuAction2 = new JMenuItem("Show Graphic");
	    iMenuAction3 = new JMenuItem("Quit");
	    iMenuAction1.addActionListener(this);
	    iMenuAction2.addActionListener(this);
	    iMenuAction3.addActionListener(this);
	    //menuActions.add(iMenuAction1);
	    menuActions.add(iMenuAction2);
	    menuActions.addSeparator();
	    menuActions.add(iMenuAction3);
	    menuBar.add(menuActions);
	    menuBar.setPreferredSize(new Dimension(250, 20));
	    setJMenuBar(menuBar);
	    /*----------*/
	    
	    // Creation du label de l'ip default qui est affiché et celui qui va nous servir de base.
	    JLabel defaultIp = new JLabel(DEFAULT_IP);
	    defaultIp.setFont(font);
	    alIp.add(defaultIp);
	
	    // Creation du label de l'ip default qui est affiché et celui qui va nous servir de base.
	    JLabel defaultPing = new JLabel( ping.pingIp(alIp.get(0).getText(),"1000") );
	    defaultPing.setFont(font);
	    alPing.add(defaultPing);
	    
	    // Panneau général
	    pGlobal = new JPanel(lGlobal);
	    
	    alPan.add( new JPanel(new BorderLayout()) );
	    alPan.get(0).add(alIp.get(0),BorderLayout.WEST);
	    alPan.get(0).add(alPing.get(0),BorderLayout.EAST);

	    pGlobal.setBackground(new Color(0,0,0));
	    pGlobal.add(alPan.get(0));
	    add(pGlobal);
	    this.setVisible(true);
	}
	//Cf ActionListener
	public void actionPerformed(ActionEvent e) { 
		if( e.getSource() == iMenuAction1)	//Open add frame
		{
			saisie = new fenetreSaisie(this);
			IhmGui.bRefresh = false;
		}
		if( e.getSource() == iMenuAction2)	//Extend frame for adding Graph
		{
			mainLayout.setColumns(mainLayout.getColumns()+1);
			this.setLayout(mainLayout);
			this.add(graphExtentionPanel);
			this.pack();
			this.setBounds(0, 0, this.getWidth()+20, this.getHeight()+10);
		}
		else if( e.getSource() == iMenuAction3)	//Quit
		{
			IhmGui.bRefresh = false;
			IhmGui.bRun = false;
			setVisible(false);  // You can't see me!
			dispose(); 			// Destroy the FRAME !
		}
		else if( e.getSource() == saisie.bValider)	//Validate the adding of the ip
		{
			try {
				this.ajouter();
			} catch (IOException e1) {}
			saisie.dispose();
			IhmGui.bRefresh = true;
		}
	}
	//Refresh and set the color of ping
	//Refresh all ping and set the color
	public static void refreshPing() throws IOException {
		int cpt = 0;
		for( JLabel l : alIp )
		{
			try {
				 String res = ping.pingIp(l.getText(),"1000");
				 alPing.get(cpt).setText( res );
				 // Colorization of ping
				 if ( res.equals(">1000 ms") || res.equals("Error") )
					 alPing.get(cpt).setForeground(new Color(155,20,20));	// Rouge
				 else if( Integer.parseInt(res.substring(0, res.indexOf(" "))) < 150 )
					 alPing.get(cpt).setForeground(new Color(20,155,20));	// Vert
				 else
					 alPing.get(cpt).setForeground(new Color(200,125,25));	// Orange
				 
				 //Adding in alPings for graph
				 if(cpt==0)
				 {
					 res = res.replaceAll(">", "");
					 for(int i=0; i<tabPings.length-1 ;i++)
					 {
						 tabPings[i] = tabPings[i+1];
					 }
					 tabPings[STEPS-1] = Integer.parseInt(res.substring(0, res.indexOf(" ms")));
				 }
				 
				 cpt++;
			} catch (IOException e1) { System.out.println("Refresh Error"); }
		}
		graph.repaint();
	}
	//Add a line for a test of an ip
	//Add and ip
	public void ajouter() throws IOException
	{
		alIp.add( new JLabel(saisie.getIp()) );
		alPing.add( new JLabel( ping.pingIp(saisie.getIp(),"1000") ) );
		
	    alPan.add( new JPanel(new BorderLayout()) );
	    alPan.get(alPan.size()-1).add(alIp.get(alPan.size()-1),BorderLayout.WEST);
	    alPan.get(alPan.size()-1).add(alPing.get(alPan.size()-1),BorderLayout.EAST);
		
		this.setSize(this.getWidth(), this.getHeight()+50);
		for( JPanel p : alPan )
		{
			pGlobal.add(p);
		}
		lGlobal.setRows( (this.getHeight()-40)/50);
		repaint();
		revalidate();
	}
	//
	public static void refreshMemoryTest(String s) throws IOException {
		System.out.println(s);
		System.out.println("test");
	}
	//Main
	public static void main(String [] args) throws IOException {
    	  
        new IhmGui();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        PingTestThread refreshPing = new PingTestThread();
        MemoryTestThread memTest   = new MemoryTestThread();
        
        executor.execute(refreshPing);
        executor.execute(memTest);
    }
}
