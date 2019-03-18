package paint2D;

import java.awt.Color;
import java.awt.Graphics2D;

public interface Shape {

	public String getType();
	public void paint(Graphics2D shape);
    public void color(Color c);
    public Color Getcolor();
	public boolean contain(int x,int y);
	public int getcornerX();
	public int getCornerY();
	public int getMaxX();
	public int getMaxY();
	public int GetX1();
	public int GetY1();
	public int GetX2();
	public int GetY2();
	public int GetX3();
	public int GetY3();

		
	
}
