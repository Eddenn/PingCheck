import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel{
	
	private int[] tabValues;
	private int x;
	private int y;
	private int stepsX;
	private int stepsY;
	
	public GraphPanel(int[] tabValues,int x,int y,int stepsX,int stepsY)
	{
		this.tabValues =tabValues;
		this.x = x;
		this.y = y;
		this.stepsX = stepsX;
		this.stepsY = stepsY;
	}
	
	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		//Draw border
		g2d.setColor(new Color(220,75,75));
		g2d.drawRect(0, 0, this.x, this.y);
		
		//Draw grid
		g2d.setColor(new Color(220,130,130));
		for(int i=this.x/this.stepsX; i<=this.x ; i=i+this.x/this.stepsX ){
			g2d.drawLine(i, 0, i, this.y);
		}
		for(int i=this.y/this.stepsY; i<=this.y ; i=i+this.y/this.stepsY ){
			g2d.drawLine(0, i, this.x, i);
		}
		
		//Drawn point with alValues
		int stepWidth = this.x/this.stepsX;
		int value,valueBefore;
		for(int i=stepWidth; i<this.tabValues.length*stepWidth ; i=i+stepWidth)
		{
			value = this.tabValues[i/(stepWidth)];
			valueBefore = this.tabValues[i/(stepWidth)-1];
			if(value > 450)	value = 450;
			if(valueBefore > 450)	valueBefore = 450;
			
			g2d.setColor(new Color(220,75,75));
			g2d.fillRect(i-3, y-(value)/2-3, 6, 6);
			g2d.drawLine(i-stepWidth, y-(valueBefore)/2, i, y-(value)/2 );
		}
	}
	
    public Dimension getPreferredSize(){
    	return new Dimension(x,y);
    }

}