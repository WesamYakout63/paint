package paint2D;

import java.awt.Graphics2D;


public class Square extends Rectangle {

	private int length = 0;

	public Square (int firstX, int firstY, int secondX, int secondY) {
		super(firstX, firstY, secondX, secondY);
		length = super.getWidth();
		super.setHeight(length);
		if (secondY<firstY){
			super.setCornerY(firstY-length);
		}
		super.setType("Square");
	}
}

