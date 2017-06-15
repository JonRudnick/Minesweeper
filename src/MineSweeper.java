/* names: Katie Filosa & Jonathan Rudnick
 * date: Dec 3, 2013
 * description: The game of minesweeper. Flag all 10 mines to win
 */

import java.util.*; //import Scanner and Random
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;

public class MineSweeper extends JApplet implements Runnable {

	private Thread runner = null;
	final private int WIDTH  = 500;
	final private int HEIGHT = 300;
	private Image    image; //the board
	private Graphics graphics; //draws most of the graphics
	private Graphics timer; //created so that timer doesn't turn red 
	private Graphics blueText; //is blue
	private Graphics greenText; //is green
	private Graphics redText; //is red
	private Graphics magentaText; //is magenta
	private Graphics yellowText; //is yellow
	//I imagine there was an easier way to do these, however I don't know of it

	Square[][] square = new Square[8][8]; //holds the 64-square board
	Random rand = new Random();
	boolean gameOver = false; //has the game been lost?
	boolean gameWon = false; //has the game been won?
	private AudioClip sound; //for the EXPLOSION
	int numMines = 0; //the number of mines on the board
	int randX;
	int randY;
	long startTime = System.currentTimeMillis(); //the time at game start
	long endTime; //the time at game end


	public void init()
	{

		image    = createImage( WIDTH, HEIGHT ); 
		graphics = image.getGraphics();
		timer = image.getGraphics();
		blueText = image.getGraphics();
		greenText = image.getGraphics();
		redText = image.getGraphics();
		magentaText = image.getGraphics();
		yellowText = image.getGraphics();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				square[i][j] = new Square(i, j); //loops through each member of the array and initializes a square	
			}
		}

		while (numMines < 10) { //loops until there are 10 mines
			randX = rand.nextInt(8);
			randY = rand.nextInt(8);
			if (!square[randX][randY].getMine()) { //if there is not a mine on this space
				square[randX][randY].setMine(); //put one there
				numMines++;
			}
		}

		//register mouse event listener
		addMouseListener(new MyMouseListener());

		setSize(WIDTH,HEIGHT);

		sound = getAudioClip(getDocumentBase(), "explosion.wav"); //EXPLOSION

	} //end of init method

	public void start() {
		// Create a new thread -- DON'T TOUCH THIS METHOD
		if ( runner == null ) {
			runner = new Thread( this );
			runner.start();
		}
	}

	public void run() {
		//refresh the screen -- DON'T TOUCH THIS METHOD
		while (runner != null) {
			repaint();
			try {
				Thread.sleep( 20 );
			} catch ( InterruptedException e ) {
				// do nothing
			}
		}
	}

	public void paint( Graphics g ) {
		// clear the background to white -- THESE SHOULD BE THE FIRST 3 LINES IN THIS METHOD
		graphics.setColor( Color.white ); // THESE SHOULD BE THE FIRST 3 LINES IN THIS METHOD
		graphics.fillRect( 0, 0,  WIDTH, HEIGHT ); //THESE SHOULD BE THE FIRST 3 LINES IN THIS METHOD

		//draw boundary box
		graphics.setColor(Color.black);  
		graphics.drawRect(0,0,WIDTH-1,HEIGHT-1);

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				square[i][j].draw(graphics);
				graphics.setColor(Color.black);  
				graphics.drawRect(i*30,j*30,30,30);	//gives the squares borders so you can see them
			}
		}
		
		if(gameWon) {
			graphics.setColor(Color.green);
			graphics.setFont(new Font("SansSerif", Font.BOLD, 24));
			graphics.drawString("YOU WIN", 250,100); //displays game won message
			timer.drawString((endTime - startTime) / 1000 + " seconds",250,10); //displays ending time
			timer.drawString("Mines Remaining: " + numMines, 250, 25); //displays final number of mines (should be 0)
		}
		else if(!gameOver) {
			long time = (System.currentTimeMillis() - startTime) / 1000;
			timer.drawString(time + " seconds",250,10); //displays current time
			timer.drawString("Mines Remaining: " + numMines, 250, 25); //displays current number of mines
		}
		else {
			graphics.setColor(Color.red);
			graphics.setFont(new Font("SansSerif", Font.BOLD, 24));
			graphics.drawString("GAME OVER", 250,100); //displays game lost message
			timer.drawString((endTime - startTime) / 1000 + " seconds",250,10); //displays ending time
			timer.drawString("Mines Remaining: " + numMines, 250, 25); //displays final number of mines
		}

		blueText.setColor(Color.blue);
		blueText.drawString("Blue boxes are bordered by 1 mine", 250, 180);
		greenText.setColor(Color.green);
		greenText.drawString("Green boxes are bordered by 2 mines", 250, 195);
		redText.setColor(Color.red);
		redText.drawString("Red boxes are bordered by 3 mines", 250, 210);
		magentaText.setColor(Color.magenta);
		magentaText.drawString("Purple boxes are bordered by 4+ mines", 250, 225);
		yellowText.setColor(Color.yellow);
		yellowText.drawString("Yellow boxes represent flags", 250, 240);

		// copy buffer to screen -- THESE SHOULD BE THE LAST 2 LINES IN THIS METHOD
		g.drawImage( image, 0, 0, this ); // THESE SHOULD BE THE LAST 2 LINES IN THIS METHOD
	}


	private class MyMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			int x = e.getX() / 30; //finds x position of square
			int y = e.getY() / 30; //finds y position of square
			//i am now quite sure there was an easier way to do this, but this is what we have
			if (!square[x][y].getClicked()) {
				if (e.getButton() == 1) { //if it's a left click
					if (square[x][y].getMine()) { //if there's a mine on that space
						gameOver(); //you lose
					}
					else {
						bombsNearby(x,y); //check how many mines are touching
					}
				}
				else { //if it's a right click
						square[x][y].setFlagged(); //flag or unflag
						checkFlags(); //check how many flags and mines there are, and check for victory
				}
			}

		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}
		public void mousePressed(MouseEvent e) {
		}
	}

	public void bombsNearby(int squareX, int squareY) { //checks the 8 squares around a box for mines
		int bombsNearby = 0;
		square[squareX][squareY].setClicked();
		for (int x = squareX - 1 ; x <= squareX + 1 ; x++) {
			for (int y = squareY - 1 ; y <= squareY + 1 ; y++) {
				while (true) {
					if (x < 0 || y < 0 || x > 7 || y > 7) { //makes sure it doesn't go out of bounds
						break;
					}
					if (square[x][y].getMine()) {
						bombsNearby++; //adds to the total bombs touching the space
					}
					break;
				}
			}
		}
		if(bombsNearby == 0) {
			square[squareX][squareY].setColor(Color.gray);
			clickAllEight(squareX,squareY); //reveals all eight squares around this one
		}
		else if(bombsNearby == 1) {
			square[squareX][squareY].setColor(Color.blue);
		}
		else if(bombsNearby == 2) {
			square[squareX][squareY].setColor(Color.green);
		}
		else if(bombsNearby == 3) {
			square[squareX][squareY].setColor(Color.red);
		}
		else {
			square[squareX][squareY].setColor(Color.magenta);
		}
	}
	public void clickAllEight(int squareX, int squareY) { //calls bombsNearby on all spaces adjacent to a zero
		for (int x = squareX - 1 ; x <= squareX + 1 ; x++) {
			for (int y = squareY - 1 ; y <= squareY + 1 ; y++) {
				while (true) {
					if (x < 0 || y < 0 || x > 7 || y > 7 || square[x][y].getClicked()) { //makes sure it doesn't go out of bounds or check a square twice
						break;
					}
					else {
						bombsNearby(x,y);
					}
					break;
				}
			}
		}
	}
	public void gameOver() {
		endTime = (System.currentTimeMillis());
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (square[i][j].getMine()) {
					square[i][j].setColor(Color.black); //reveals all mines					
				}
			}
		}
		sound.play(); //EXPLOSION
		gameOver = true;
	}
	public void checkFlags() { //checks how many mines are remaining and for victory
		int flaggedCorrectly = 0;
		int numFlags = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (square[i][j].getMine() && square[i][j].getFlagged()) { //all correctly flagged spaces
					flaggedCorrectly++; //for victory
					numFlags++; //for mines remaining
				}
				else if (square[i][j].getFlagged()) { //incorrect flags
					flaggedCorrectly--; //otherwise you could win by flagging every space
					numFlags++; //for mines remaining
				}
			}
		}
		if (flaggedCorrectly == 10) { //if all mines are flagged
			endTime = (System.currentTimeMillis());
			gameWon = true;
		}
		numMines = 10 - numFlags; //mines remaining
	}
}
