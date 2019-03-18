package paint2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Triangle implements Shape {

	private final int numOfPoints = 3;
	private int cornerX, cornerY;
	private int maxX, maxY;
	private int[] XCoordinates = new int[numOfPoints];
	private int[] YCoordinates = new int[numOfPoints];
	private Color color;
	private String type = "Triangle";

	public Triangle(int firstX, int firstY, int secondX, int secondY, int thirdX, int thirdY) {

		this.XCoordinates[0] = firstX;
		this.YCoordinates[0] = firstY;
		this.XCoordinates[1] = secondX;
		this.YCoordinates[1] = secondY;
		this.XCoordinates[2] = thirdX;
		this.YCoordinates[2] = thirdY;
		cornerX = Math.min(firstX, Math.min(secondX, thirdX));
		cornerY = Math.min(firstY, Math.min(secondY, thirdY));
		maxX = Math.max(firstX, Math.max(secondX, thirdX));
		maxY = Math.max(firstY, Math.max(secondY, thirdY));
		if (firstX==thirdX){
			type="RTriangle";
		}

	}
	public int getCornerY() {
		return cornerY;
	}

	public int getcornerX() {

		return cornerX;
	}

	public int getMaxX() {

		return maxX;
	}

	public int getMaxY() {

		return maxY;
	}

	public String getType() {
		return type;
	}

	public void paint(Graphics2D shape) {

		shape.drawPolygon(XCoordinates, YCoordinates, numOfPoints);
	}
	public boolean contain(int x, int y) {
		Polygon triangle = new Polygon(XCoordinates, YCoordinates, numOfPoints);
		return triangle.contains(x, y);
	}
	
	public void color(Color c) {
		this.color = c;
	}

	public Color Getcolor() {
		return color;
	}
	public int GetX1() {
		return XCoordinates[0];
	}
	public int GetY1() {
		return YCoordinates[0];
	}
	public int GetX2() {
		return XCoordinates[1];
	}
	public int GetY2() {
		return YCoordinates[1];
	}
	public int GetX3() {
		return XCoordinates[2];
	}

	public int GetY3() {
		return YCoordinates[2];
	}
}
