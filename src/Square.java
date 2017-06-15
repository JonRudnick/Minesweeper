/* description: A square class that will draw a mine square in an applet
 */

import java.awt.*;
import javax.swing.*;

public class Square extends DrawableObject {

	private boolean mineSquare;
	private boolean clickedSquare;
	private boolean flaggedSquare;

	/**
	 * Square constructor
	 * initialize a square with no mine
	 */
	public Square() {
		//
		color = Color.lightGray;
		width = 30;
		height = 30;
		xPosition = 0;
		yPosition = 0;
		mineSquare = false;
	}
	public Square(int i, int j) {
		//
		color = Color.lightGray;
		width = 30;
		height = 30;
		xPosition = i*30;
		yPosition = j*30;
		mineSquare = false;
	}

	public void draw(Graphics g) {
		//draw the paddle
		g.setColor(color);
		g.fillRect(xPosition, yPosition, width, height);
	}
	//accessor and mutator methods for the three states of a square: Flagged, Clicked, and Mine
	public void setMine() {
		this.mineSquare = true;
	}	
	public boolean getMine() {
		return mineSquare;
	}
	public void setClicked() {
		this.clickedSquare = true;
	}
	public boolean getClicked() {
		return clickedSquare;
	}
	public void setFlagged() {
		if (this.flaggedSquare) {
			this.flaggedSquare = false; //unflag
			this.color = Color.lightGray;
		}
		else if (!this.flaggedSquare) {
			this.flaggedSquare = true; //flag
			this.color = Color.yellow;
		}
	}
	public boolean getFlagged() {
		return flaggedSquare;
	}
}