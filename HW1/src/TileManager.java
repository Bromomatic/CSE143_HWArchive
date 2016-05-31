// Shawn Stern, CSE 143, Assignment 1
//
// This Java class represents a TileManager object.  As the name implies this object
// stores Tile objects for a client program, and has a series of methods which modify the order
// in which the tiles are drawn, allow the creation/removal of Tiles, and shuffle the order and
// position of Tiles.

import java.util.*;
import java.awt.*;

public class TileManager {
	private ArrayList<Tile> Tiles;  			// Array List to store the order of tiles
	private Random r;								// To generate randomness for shuffle method
	
	// Basic constructor, creates a TileManager object
	public TileManager() {
		Tiles = new ArrayList<Tile>();
		r = new Random();
	}
	
	// Appends a passed tile to the end of the tile list
	public void addTile(Tile rect) {
		Tiles.add(rect);
	}
	
	// Draws all tiles onto the screen, in order from first tile to last.
	// As a result, the first tile in the list is the tile at the 'bottom'
	public void drawAll(Graphics g) {
		for(int i = 0; i < Tiles.size(); i++) {
			Tiles.get(i).draw(g);
			//	This was my original solution...
			//	Tile temp = Tiles.get(i);
			//	temp.draw(g);
		}
	}
	
	// Moves the topmost selected tile (if any) to the end of the list
	public void raise(int x, int y) {
		int i = findTile(x, y);
		Tiles.add(Tiles.get(i)); 		// Append current tile to end
		Tiles.remove(i); 				// Clear the current tile, since it's been moved
	}
	
	// Moves the topmost selected tile (if any) to the beginning of the list
	public void lower(int x, int y) {
		int i = findTile(x, y);
		Tiles.add(0, Tiles.get(i)); 	// Move current tile to the front of the list
		Tiles.remove(i + 1); 			// Remove the original copy of the tile
	}
	
	// Removes the topmost selected tile (if any) from the list
	public void delete(int x, int y) {
		int i = findTile(x, y);
		Tiles.remove(i); 				// Remove the tile
	}
	
	// Removes ALL tiles (if any) that occupy the same x/y coordinates as the mouse from the list
	public void deleteAll(int x, int y) {
		for (int i = Tiles.size() - 1; i >= 0; i--) {
			if (inBounds(Tiles.get(i), x, y)) {
				Tiles.remove(i); 		// Clear the current tile
			}
		}
	}
	
	// Randomizes the vertical order of tiles, in addition to randomizing their positions
	public void shuffle(int width, int height) {
		Collections.shuffle(Tiles);
		
		for (int i = Tiles.size() - 1; i >= 0; i--) {
			// The new coordinates are a random value between 0 and the width of the canvas
			// minus the dimensions of the tile.  This keeps the entire tile visible
			Tiles.get(i).setX(r.nextInt(width - Tiles.get(i).getWidth()));
			Tiles.get(i).setY(r.nextInt(height - Tiles.get(i).getHeight()));
		}
	}
	
	// Checks if a mouse click coincides with a tile; returns true if so, false otherwise
	// This code was repeated heavily, so I decided to make it a helper method
	private boolean inBounds(Tile a, int x, int y) {
		int x1 = a.getX();
		int x2 = x1 + a.getWidth();
		int y1 = a.getY();
		int y2 = y1 + a.getHeight();
		
		// If the x/y coordinates are both within the dimensions of the tile, true!
		if ((x >= x1 && x < x2) && (y >= y1 && y <= y2)) {
			return true;
		} else {
			return false;
		}
	}
	
	// Returns the i value of the desired tile
	private int findTile(int x, int y) {
		int i;
		for (i = Tiles.size() - 1; i >= 0; i--) {
		    if (inBounds(Tiles.get(i), x, y)) {
		        break;
		    }
		}
		
		return i;
	}
}