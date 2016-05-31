// Shawn Stern     CSE 143     HW_4
//
// This Assassin Manager maintains the list of stalkers and their respective stalkees,
// along with the graveyard which records the death of players along with who killed them.
// The game ends when there is only one person left alive in the stalker list.
// -----
// At any point where I use the words 'target' or 'victim' I am referring to 'person who is 
// about to be assassinated'; this is to save space in comments.

import java.util.*;

public class AssassinManager {
	private AssassinNode killRingFront;		// Refers to the linked list of living players
	private AssassinNode graveyardFront;	// Refers to the linked list of assassinated players
	
	// Constructs the linked lists for a given game of Assassin. (Graveyard is null by default)
	// Throws an IllegalArgumentException if the passed ArrayList is null or has no length.
	public AssassinManager(ArrayList<String> names) {
		if(names == null || names.size() == 0) {
			throw new IllegalArgumentException();
		}
		
		killRingFront = new AssassinNode(names.get(0));
		AssassinNode current = killRingFront;
		for(int i = 1; i < names.size(); i++) {
			current.next = new AssassinNode(names.get(i));
			current = current.next;
		}
	}
	
	// Prints an indented list of the current stalker pairs in the game, in the format of
	// "name is stalking name".  Each pair occupies its own line.
	// Precondition:  method is only called if the game is not over.  Behavior not specified
	//                otherwise.
	public void printKillRing() {
		AssassinNode current = killRingFront;
		// Iterate over the linked list, printing pairs, and stop one node short of the end
		while(current.next != null) {
			System.out.println("  " + current.name + " is stalking " + current.next.name);
			current = current.next;
		}
		// Print last pair of " 'last person' is stalking 'first person' "
		System.out.println("  " + current.name + " is stalking " + killRingFront.name);
	}
	
	// Prints an indented list of players who have been killed and who killed them.
	// Format is "name was killed by name".  Does nothing if the graveyard is empty.
	public void printGraveyard() {
		if (graveyardFront != null) {
			AssassinNode current = graveyardFront;
			// Iterate over the linked list, printing respective kill text
			while(current != null) {
				System.out.println("  " + current.name + " was killed by " + current.killer);
				current = current.next;
			}
		}
	}
	
	// Returns true if the given name matches the name of a living player, false otherwise.
	// Ignores case.
	// Pseudo-Pre Condition:  String is not type null (method should return false, though)
	public boolean killRingContains(String name) {
		AssassinNode current = killRingFront;
		return isNameInList(current, name);
	}
	
	// Returns true if the given name matches the name of a deceased player, false otherwise.
	// Ignores case.
	// Pseudo-Pre Condition:  String is not type null (method should return false, though)
	public boolean graveyardContains(String name) {
		AssassinNode current = graveyardFront;
		return isNameInList(current, name);
	}
	
	// Returns true if the game can be considered over, false otherwise.
	public boolean isGameOver() {
		// If the first node in the kill ring no longer points to another player, game is over
		// and the current node's associated name is the winnar!
		return killRingFront.next == null;
	}
	
	// If the game is over and there is a winner, return their name.
	// Otherwise, this method returns null.
	public String winner() {
		return (isGameOver()) ? killRingFront.name : null;
	}
	
	// Kills the player who's name matches the passed name.  Ignores the case of names.
	// -----
	// Throws an IllegalStateException if the game is over (i.e., there's nobody left to kill).
	// Throws an IllegalArgumentException if the passed name doesn't match a player.
	// IllegalStateException will take precedence if both failure conditions are met.
	// Pseudo-Pre Condition:  String is not type null; still, nobody should die if null is passed.
	public void kill(String name) {
		// Check for exceptions first
		if(isGameOver()) {
			throw new IllegalStateException();
		}
		if(!killRingContains(name)) {
			throw new IllegalArgumentException();
		}
		
		// Current is for traversing the linked list
		AssassinNode current = killRingFront;
		// Victim is for tracking the node which is moved around
		AssassinNode victim = killRingFront;
		
		// The following if/else loop's job is to assign the victim node to it's correct value,
		// which is then used to rearrange graveyard and the kill ring properly.
		// -----
		// Edge case when the target is the first person in the list
		if(current.name.equalsIgnoreCase(name)) {
			// Killer is person at the end of the list			
			while(current.next != null) {
				current = current.next;
			}
			victim.killer = current.name;
			killRingFront = killRingFront.next;
		} else {  // Otherwise, target is not the front of the list, kill 'normally'
			while(!current.next.name.equalsIgnoreCase(name)) {
				current = current.next;
			}
			current.next.killer = current.name;		// Set victim's killer to the killer's name
			victim = current.next;
			current.next = current.next.next;		// Give deceased player's target to killer
		}
		
		// If the graveyard has no nodes yet, add the victim node and snip off any lingering
		// 'next' elements.
		// Otherwise, point victim node to graveyard and 'overwrite' graveyard with the new,
		// correct order.
		if(graveyardFront == null) {
			graveyardFront = victim;
			graveyardFront.next = null;
		} else {	
			victim.next = graveyardFront;
			graveyardFront = victim;
		}
	}
	
	// Returns true if the passed name matches the name of any node in the passed list.
	// Ignores case.
	// Pseudo-Pre Condition:  String is not type null (method should return false, though)
	private boolean isNameInList(AssassinNode current, String name) {
		while(current != null) {
			// If names match, return true; else move to the next node.
			if(current.name.equalsIgnoreCase(name)) {
				return true;
			} else {
				current = current.next;
			}
		}
		
		return false;
	}
}