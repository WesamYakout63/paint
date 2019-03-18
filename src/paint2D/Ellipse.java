package paint2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class Ellipse implements Shape {

	private int horizontalAxis = 0;
	private int verticalAxis = 0;
	private int cornerX, cornerY;
	private int maxX, maxY, x1, y1, x2, y2;
	private Color color = Color.BLACK;
	private String type = "Ellipse";

	public Ellipse(int firstX, int firstY, int secondX, int secondY) {
		cornerX = Math.min(secondX, firstX);
		cornerY = Math.min(firstY, secondY);
		this.x1 = firstX;
		this.y1 = firstY;
		this.x2 = secondX;
		this.y2 = secondY;
		evaluateBoundries(firstX, firstY, secondX, secondY);
	}

	private void evaluateBoundries(int firstX, int firstY, int secondX, int secondY) {
		horizontalAxis = secondX - firstX;
		if (horizontalAxis < 0) {
			horizontalAxis *= -1;
		}
		verticalAxis = secondY - firstY;
		if (verticalAxis < 0) {
			verticalAxis *= -1;
		}
		maxX = cornerX + horizontalAxis;
		maxY = cornerY + verticalAxis;

	}

	public int getHorizontalAxis() {
		return horizontalAxis;
	}

	public int getVerticalAxis() {
		return verticalAxis;
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

	public void setVerticalAxis(int value) {
		verticalAxis = value;
		maxY = cornerY + verticalAxis;

	}

	public void setCornerX(int value) {
		cornerX = value;
	}

	public void setCornerY(int value) {
		cornerY = value;
	}

	public void paint(Graphics2D shape) {
		shape.drawOval(cornerX, cornerY, horizontalAxis, verticalAxis);

	}

	public boolean contain(int x, int y) {

		Ellipse2D ellipse = new Ellipse2D.Float(cornerX, cornerY, horizontalAxis, verticalAxis);

		return ellipse.contains(x, y);
	}

	public String getType() {
		return type;
	}

	public void setType(String value) {
		type = value;
	}

	public void color(Color c) {
		color = c;
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

