/**
 * @(#)JointedArm.java
 *
 * JointedArm application
 *
 * @author Daniel Kennedy
 * @version 1.00 2015/10/29
 */
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

public class JointedArm
{
	public static final int SEGMENT_1_LENGTH=100;
	public static final int SEGMENT_2_LENGTH=100;
	public static final int ORIGIN_X=150;
	public static final int ORIGIN_Y=150;
	public static final int WINDOW_WIDTH=600;
	public static final int WINDOW_HEIGHT=600;

	public static void main(String[] args)
	{
		JFrame f=new JFrame("Arm with 2 Joints (and a bunch of math)");
		f.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
     	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     	f.setResizable(false);
        f.add(new GraphicsPanel());
		f.setVisible(true);
    }

	private static class GraphicsPanel extends JPanel implements MouseListener, MouseMotionListener
	{
		private int x0=ORIGIN_X, y0=ORIGIN_Y,w=0,h=0;
		private double x1=0, y1=0;
		private double x2=0, y2=0;
		private double r1=SEGMENT_1_LENGTH,r2=SEGMENT_2_LENGTH;
		private double t1=0, t2=0;
		private double t1d=0, t2d=0;
		private double d=0;
		private boolean outOfRange=false;
		private DecimalFormat form;

		public GraphicsPanel()
		{
   	   		addMouseListener(this);
   	   		addMouseMotionListener(this);
   	   		setBackground(Color.DARK_GRAY);
   	   		form = new DecimalFormat("#.#");
		}

		public void doStuff(int x, int y)
		{
			x2=(double)x-x0;
  	   		y2=y0-(double)y;
  	   		d=Math.sqrt(x2*x2+y2*y2);
			System.out.println("Coordinates chosen: "+x2+" and "+y2);

			if(d<r1+r2)
			{
				outOfRange=false;
				// t1 = arctan(y2/x2) - arccos( (r1^2 - r2^2 + x2^2 + y2^2) / (2 r1 sqrt( x2^2 + y2^2)) )
				t1=Math.atan(y2/x2)-Math.acos((r1*r1-r2*r2+x2*x2+y2*y2)/(2*r1*d));
				// t2 = arccos( (r1^2 + r2^2 - x2^2 - y2^2) / (2 r1 r2) )
				t2=Math.acos((r1*r1+r2*r2-x2*x2-y2*y2)/(2*r1*r2));
			}
			else
			{
				outOfRange=true;
				t1=Math.atan(y2/x2);
				t2=Math.PI;
			}
			if(x2<0)
				t1+=Math.PI;
			if(t1<0)
				t1+=2*Math.PI;

			//The t2 exterior angle instead of the interior angle (only necessary for graphical purposes)
			double t2ex=Math.PI-t2+t1;
			//The t1 and t2 angles, in degrees
			t1d=Math.toDegrees(t1);
			t2d=Math.toDegrees(t2);

   	   		x1=r1*Math.cos(t1);
   	   		y1=r1*Math.sin(t1);
   	   		x2=r2*Math.cos(t2ex)+x1;
   	   		y2=r2*Math.sin(t2ex)+y1;

   	   		repaint();
		}


		public void paintComponent(Graphics g)
   	   	{
   	   		super.paintComponent(g);
   	   		w=g.getClipBounds().width;
   	   		h=g.getClipBounds().height;
			y0=h-ORIGIN_Y;
   	   		x1=x0+x1; x2=x0+x2;
   	   		y1=y0-y1; y2=y0-y2;


   	   		g.setColor(Color.BLACK);
			g.drawLine(x0, h-0, x0, h-WINDOW_WIDTH);
			g.drawLine(0, y0 , WINDOW_HEIGHT, y0);

			g.setColor(Color.GREEN);
			g.drawLine((int)x0, (int)y0, (int)x1, (int)y1);
			g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);

			if(outOfRange) g.setColor(Color.RED);
			else g.setColor(Color.BLUE);
			g.drawArc((int)x0-20, (int)y0-20, 40, 40, 0, (int)t1d);
			g.drawArc((int)x1-20, (int)y1-20, 40, 40, (int)(180-t2d+t1d), (int)t2d);

			if(outOfRange) g.setColor(Color.RED);
			else g.setColor(new Color(0,240,255));
			g.drawString(form.format(t1d), (int)x0+2, (int)y0+12);
			g.drawString(form.format(t2d), (int)x1+2, (int)y1+12);

			if(outOfRange) System.out.println("  Point out of range");
   	   		System.out.println("  T1 (degrees) = "+t1d);
			System.out.println("  T2 (degrees) = "+t2d);
		}



		public void mouseDragged(MouseEvent e)
		{
			doStuff(e.getX(),e.getY());
		}
		public void mousePressed(MouseEvent e)
		{
			doStuff(e.getX(),e.getY());
		}
		public void mouseMoved(MouseEvent e){}
		public void mouseClicked(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
	}
}
