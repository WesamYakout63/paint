package paint2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class Rectangle implements Shape {
	private int width = 0, height = 0;
	private int cornerX, cornerY, maxX, maxY, x1, x2, y1, y2;
	private String type = "Rectangle";
	private Color color = Color.BLACK;

	public Rectangle(int firstX, int firstY, int secondX, int secondY) {

		cornerX = Math.min(firstX, secondX);
		cornerY = Math.min(firstY, secondY);
		this.x1 = firstX;
		this.y1 = firstY;
		this.x2 = secondX;
		this.y2 = secondY;
		evaluateBoundries(firstX, firstY, secondX, secondY);
	}

	private void evaluateBoundries(int startX, int startY, int endX, int endY) {
		width = endX - startX;
		if (width < 0) {
			width *= -1;
		}
		height = endY - startY;
		if (height < 0) {
			height *= -1;
		}
		maxX = cornerX + width;
		maxY = cornerY + height;

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getcornerX() {

		return cornerX;
	}

	public int getCornerY() {
		return cornerY;
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

	public void setType(String value) {
		type = value;
	}

	public void setHeight(int value) {
		height = value;
		maxY = cornerY + value;
	}

	public void setWidth(int value) {
		width = value;
	}

	public void setCornerX(int value) {
		cornerX = value;
	}

	public void setCornerY(int value) {
		cornerY = value;
	}

	public void paint(Graphics2D shape) {
		shape.drawRect(cornerX, cornerY, width, height);
	}

	public boolean contain(int x, int y) {
		java.awt.Rectangle rec = new java.awt.Rectangle(cornerX, cornerY, width, height);
		return rec.contains(x, y);
	}

	public void color(Color c) {
		this.color = c;
	}

	public Color Getcolor() {
		return color;
	}

	public int GetX1() {
		return x1;
	}

	public int GetY1() {
		return y1;
	}

	public int GetX2() {
		return x2;
	}

	public int GetY2() {
		return y2;
	}

	public int GetX3() {
		return 0;
	}

	public int GetY3() {
		return 0;
	}
}
