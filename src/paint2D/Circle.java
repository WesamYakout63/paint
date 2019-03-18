package paint2D;

import java.awt.Graphics2D;

public class Circle extends Ellipse {

	private int radius;

	public Circle(int firstX, int firstY, int secondX, int secondY) {
		super(firstX, firstY, secondX, secondY);
		radius = super.getHorizontalAxis();
		super.setVerticalAxis(radius);
		if (secondY < firstY) {
			super.setCornerY(firstY - radius);
		}
		super.setType("Circle");

	}

	public int getRadius() {
		return radius;
	}

}
