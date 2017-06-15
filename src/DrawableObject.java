/* description: A generic DrawableObject -- note this class should be subclasssed
 */

import java.awt.*;

public class DrawableObject {

	protected int width;
	protected int height;
	protected int xPosition;
	protected int yPosition;
	protected Color color;

	/**
	 * draw -- must be overridden by subclass
	 * @param g
	 */
	public void draw(Graphics g){
		//code will be implemented by subclass
	}

	//accessor and mutator methods
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getXPosition() {
		return xPosition;
	}

	public void setXPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public int getYPosition() {
		return yPosition;
	}

	public void setYPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}