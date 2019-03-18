package paint2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Line implements Shape {

	private int startX, startY, endX, endY;
	private String type = "Line";
	private Color color = Color.BLACK;

	public Line(int startX, int startY, int endX, int endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}

	public void paint(Graphics2D shape) {
		shape.drawLine(startX, startY, endX, endY);
	}

	private double getDis(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	public boolean contain(int x, int y) {
		double dis1, dis2, dis3;
		dis1 = getDis(x, y, startX, startY);
		dis2 = getDis(x, y, endX, endY);
		dis3 = getDis(startX, startY, endX, endY);
		return Math.abs(dis3 - (dis1 + dis2)) <= 5;
	}

	public int getcornerX() {

		return Math.min(startX, endX);
	}

	public int getCornerY() {

		return Math.min(startY, endY);
	}

	public int getMaxX() {
		return Math.max(startX, endX);
	}

	public int getMaxY() {

		return Math.max(startY, endY);
	}

	public String getType() {
		return type;
	}

	public void color(Color c) {
		color = c;
	}

	public Color Getcolor() {
		return color;
	}

	public int GetX1() {
		return startX;
	}

	public int GetY1() {
		return startY;
	}

	public int GetX2() {
		return endX;
	}

	public int GetY2() {
		return endY;
	}

	public int GetX3() {
		return 0;
	}

	public int GetY3() {
		return 0;
	}
}
