/**
 * Class that represents an Actor (i.e. something that can perform actions) in the harrypotter world.
 * 
 * @author ram
 * 
 * @modified 20130414 dsquire
 * 	- changed constructor so that affordances that all HPActors must have can be added
 * 	- changed team to be an enum rather than a string
 */
/*
 * Change log
 * 2017-01-20: Added missing Javadocs and improved comments (asel)
 * 2017-02-08: Removed the removeEventsMethod as it's no longer required.
 * 			   Removed the tick and act methods for HPActor as they are never called
 */
package harrypotter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import edu.monash.fit2099.gridworld.Grid.CompassBearing;
import edu.monash.fit2099.simulator.matter.ActionInterface;
import edu.monash.fit2099.simulator.matter.Actor;
import edu.monash.fit2099.simulator.matter.Affordance;
import edu.monash.fit2099.simulator.space.Location;
import edu.monash.fit2099.simulator.time.Scheduler;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.actions.*;
import harrypotter.actions.spells.*;
import harrypotter.entities.Broomstick;
import harrypotter.entities.actors.Player;
import harrypotter.interfaces.HPGridController;

public abstract class HPActor extends Actor<HPActionInterface> implements HPEntityInterface {
	
	static final double NPC_LEARN_CHANCE = 0.5;
	static final int INVENTORY_SIZE = 3;

	public MessageRenderer message;
	
	/**the <code>Team</code> to which this <code>HPActor</code> belongs to**/
	private Team team;
	
	/**The amount of <code>hitpoints</code> of this actor. If the hitpoints are zero or less this <code>Actor</code> is dead*/
	private int hitpoints;
	
	/**The world this <code>HPActor</code> belongs to.*/
	public HPWorld world;
	
	/**Scheduler to schedule this <code>HPActor</code>'s events*/
	protected static Scheduler scheduler;

	private ArrayList<HPEntityInterface> inventory;
	
	/**The item carried by this <code>HPActor</code>. <code>itemCarried</code> is null if this <code>HPActor</code> is not carrying an item*/
	private HPEntityInterface itemCarried;
	
	/**If or not this <code>HPActor</code> is human controlled. <code>HPActor</code>s are not human controlled by default*/
	protected boolean humanControlled = false;
	
	private boolean mobile = true;
	
	private boolean shielded = false;
	
	private HashSet<Spells> knownSpells = new HashSet<Spells>();
	
	/**A string symbol that represents this <code>HPActor</code>, suitable for display*/
	private String symbol;
	
	/**A set of <code>Capabilities</code> of this <code>HPActor</code>*/
	protected HashSet<Capability> capabilities = new HashSet<Capability>();
	
	private boolean givenBroomstick = false;;
	
	/**
	 * Constructor for the <code>HPActor</code>.
	 * <p>
	 * The constructor initializes the <code>actions</code> list of this <code>HPActor</code>.
	 * <p>
	 * By default,
	 * <ul>
	 * 	<li>All <code>HPActor</code>s can be attacked.</li>
	 * 	<li>Have their symbol set to '@'</li>
	 * </ul>
	 * 
	 * @param 	team to which this <code>HPActor</code> belongs to
	 * @param 	hitpoints initial hitpoints of this <code>HPActor</code> to start with
	 * @param 	m	message renderer for this <code>HPActor</code> to display messages
	 * @param 	world the <code>World</code> to which <code>HPActor</code> belongs to
	 * 
	 * @see 	#team
	 * @see 	#hitpoints
	 * @see 	#world
	 * @see 	harrypotter.actions.Attack
	 */
	public HPActor(Team team, int hitpoints, MessageRenderer m, HPWorld world) {
		super(m);
		this.message = m;
		actions = new HashSet<HPActionInterface>();
		this.team = team;
		this.hitpoints = hitpoints;
		this.world = world;
		this.symbol = "@";
		this.inventory = new ArrayList<HPEntityInterface>();
			
		//HPActors are given the Attack affordance hence they can be attacked
		HPAffordance attack = new Attack(this, m);
		this.addAffordance(attack);
		
		HPAffordance give = new Give(null, null, this, m, world);
		this.addAffordance(give);

		HPAction enter = new Enter(m);
		this.addAction(enter);
		
		HPAction cast = new Cast(m);
		this.addAction(cast);
		
		HPAction protego = new Protego(m);
		this.addAction(protego);

		HPAction apparate = new Apparate(m);
		this.addAction(apparate);
		
		HPAffordance avadakedavra = new AvadaKedavra(this, m);
		this.addAffordance(avadakedavra);
		
		HPAffordance expelliarmus = new Expelliarmus(this, m, world, scheduler);
		this.addAffordance(expelliarmus);
		
		HPAffordance immobulus = new Immobulus(this, m);
		this.addAffordance(immobulus);
		
		HPAffordance sectumsempra = new SectumSempra(this, m);
		this.addAffordance(sectumsempra);
	}

	
	/**
	 * Sets the <code>scheduler</code> of this <code>HPActor</code> to a new <code>Scheduler s</code>
	 * 
	 * @param	s the new <code>Scheduler</code> of this <code>HPActor</code> 
	 * @see 	#scheduler
	 */
	public static void setScheduler(Scheduler s) {
		scheduler = s;
	}
	
	/**
	 * Returns the team to which this <code>HPActor</code> belongs to.
	 * <p>
	 * Useful in comparing the teams different <code>HPActor</code> belong to.
	 * 
	 * @return 	the team of this <code>HPActor</code>
	 * @see 	#team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Returns the hit points of this <code>HPActor</code>.
	 * 
	 * @return 	the hit points of this <code>HPActor</code> 
	 * @see 	#hitpoints
	 * @see 	#isDead()
	 */
	@Override
	public int getHitpoints() {
		return hitpoints;
	}

	//modifier added
	// allows actor's hitpoints to be changed
	public boolean setHitpoints(int value) {
		
		boolean result = false;
		
		if (!this.isDead()) {
			this.hitpoints += value;
			result = true;
		}
		
		return result;
	}
	
	
	/**
	 * Returns an ArrayList containing this Actor's available Actions, including the Affordances of items
	 * that the Actor is holding.
	 * 
	 * @author ram
	 */
	public ArrayList<HPActionInterface> getActions() {
		ArrayList<HPActionInterface> actionList = super.getActions();
		
		//If the HobbitActor is carrying anything, look for its affordances and add them to the list
		HPEntityInterface item = getItemCarried();
		if (item != null)
			for (Affordance aff : item.getAffordances())
				if (aff instanceof HPAffordance)
				actionList.add((HPAffordance)aff);
		
		for (HPEntityInterface inventoryItem : inventory) {
			for (Affordance aff : inventoryItem.getAffordances())
				if (aff instanceof HPAffordance)
				actionList.add((HPAffordance)aff);
		}
		
		return actionList;
	}
	
	/**
	 * Returns the item carried by this <code>HPActor</code>. 
	 * <p>
	 * This method only returns the reference of the item carried 
	 * and does not remove the item held from this <code>HPActor</code>.
	 * <p>
	 * If this <code>HPActor</code> is not carrying an item this method will return null.
	 * 
	 * @return 	the item carried by this <code>HPActor</code> or null if no item is held by this <code>HPActor</code>
	 * @see 	#itemCarried
	 */
	public HPEntityInterface getItemCarried() {
		return itemCarried;
	}

	/**
	 * Sets the team of this <code>HPActor</code> to a new team <code>team</code>.
	 * <p>
	 * Useful when the <code>HPActor</code>'s team needs to change dynamically during the simulation.
	 * For example, a bite from an evil actor makes a good actor bad.
	 *
	 * @param 	team the new team of this <code>HPActor</code>
	 * @see 	#team
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * Method insists damage on this <code>HPActor</code> by reducing a 
	 * certain amount of <code>damage</code> from this <code>HPActor</code>'s <code>hitpoints</code>
	 * 
	 * @param 	damage the amount of <code>hitpoints</code> to be reduced
	 * @pre 	<code>damage</code> should not be negative
	 */
	@Override
	public void takeDamage(int damage) {
		//Precondition 1: Ensure the damage is not negative. Negative damage could increase the HPActor's hitpoints
		//assert (damage >= 0)	:"damage on HPActor must not be negative";
		this.hitpoints -= damage;
	}

	/**
	 * Assigns this <code>HPActor</code>'s <code>itemCarried</code> to 
	 * a new item <code>target</code>
	 * <p>
	 * This method will replace items already held by the <code>HPActor</code> with the <code>target</code>.
	 * A null <code>target</code> would signify that this <code>HPActor</code> is not carrying an item anymore.
	 * 
	 * @param 	target the new item to be set as item carried
	 * @see 	#itemCarried
	 */
	public void setItemCarried(HPEntityInterface target) {
		this.itemCarried = target;
	}
	
	@Override
	public void act() {
		if (this.hasCapability(Capability.TEACHER) && !this.isDead()) {
			for (HPEntityInterface entity : HPWorld.getEntitymanager().contents(HPWorld.getEntitymanager().whereIs(this))) {
				if (!this.equals(entity) && entity instanceof HPActor && ((HPActor) entity).getTeam().equals(this.getTeam()) && !teachableSpells((HPActor) entity).isEmpty()) {
					HPActor student = (HPActor) entity;
					offerTeaching(student);
					if (student.knownSpells.size() > 2 && !student.givenBroomstick && student.canReceiveItem()) {
						offerBroomstick(student);
					}
				}
			}
		}
	}
	
	/** A local function to offer a list of spells for a Player or other actor to choose from to learn from this actor.
	 * If the student is the Player, they will be able to choose from a list of spells or to reject any learning altogether through user input.
	 * Otherwise, if they are not a Player, they will choose a random spell out of those being offered, if they don't choose to reject being taught.
	 * 
	 * @param student The actor that is being taught by this actor.
	 */
	private void offerTeaching(HPActor student) {
		ArrayList<Spells> teachableSpells = teachableSpells(student);
		
		if (student instanceof Player) {
			// have the student select from a list of the list of learnable spells
			ArrayList<String> spellList = new ArrayList<String>();
			for (Spells spell : teachableSpells(student)) {
				spellList.add(spell.getName());
			}
			spellList.add("Reject offer");
			say(this.shortDescription + " offers to teach you a spell!");
			int choice = HPGridController.getDecision(spellList);
			// have the student learn the spell that they chose, if they didn't reject the offer
			if (choice < teachableSpells.size()) {
				student.learnSpell(teachableSpells.get(choice));
				say(this.shortDescription + " has taught you " + spellList.get(choice) + "!");
			}
		} else {
			if (Math.random() < NPC_LEARN_CHANCE && teachableSpells.size() > 0) {
				student.learnSpell(teachableSpells.get(new Random().nextInt(teachableSpells.size())));
			}
		}
	}
	
	/**
	 * A local function to construct a list of spells that can be taught, ie. spells that this actor knows but the student doesn't.
	 * @param student The actor that is being taught by this actor
	 * @return An ArrayList of spells that this actor can teach a student actor
	 */
	private ArrayList<Spells> teachableSpells(HPActor student) {
		ArrayList<Spells>teachableSpells = new ArrayList<Spells>();
		for (Spells spell : this.knownSpells) {
			if (!student.getSpells().contains(spell)) {
				teachableSpells.add(spell);
			}
		}
		return teachableSpells;
	}
	
	/**
	 * A local function to handle the offering and giving of a Broomstick to a student.
	 * @param student The actor that is being offered a broomstick by this actor
	 */
	private void offerBroomstick(HPActor student) {
		if (student instanceof Player) {
			// build a list of choices with an accept and a reject option
			ArrayList<String> broomstickChoices = new ArrayList<String>();
			broomstickChoices.add("Accept broomstick");
			broomstickChoices.add("Reject broomstick");
			say(this.shortDescription + " offers to teach you a spell!");
			// offer the choices to the user, and if they accept the broomstick give it to them
			if (HPGridController.getDecision(broomstickChoices) == 0) {
				// give the student a broomstick
				world.getEntityManager().setLocation(new Broomstick(messageRenderer, world), (HPLocation) world.find(this));
				student.givenBroomstick = true;
			}
		} else {
			// give the student a broomstick
			world.getEntityManager().setLocation(new Broomstick(messageRenderer, world), (HPLocation) world.find(this));
			student.givenBroomstick = true;
		}
	}
	
	/**
	 * Returns true if this <code>HPActor</code> is dead, false otherwise.
	 * <p>
	 * A <code>HPActor</code> is dead when it's <code>hitpoints</code> are less than or equal to zero (0)
	 *
	 * @author 	ram
	 * @return 	true if and only if this <code>HPActor</code> is dead, false otherwise
	 * @see 	#hitpoints
	 */
	public boolean isDead() {
		return hitpoints <= 0;
	}
	

	@Override
	public String getSymbol() {
		return symbol;
	}
	

	@Override
	public void setSymbol(String s) {
		symbol = s;
	}
	
	/**
	 * Returns if or not this <code>HPActor</code> is human controlled.
	 * <p>
	 * Human controlled <code>HPActors</code>' <code>HPActions</code> are selected by the user as commands from the Views.
	 * 
	 * @return 	true if the <code>HPActor</code> is controlled by a human, false otherwise
	 * @see 	#humanControlled
	 */
	public boolean isHumanControlled() {
		return humanControlled;
	}
	

	@Override
	public boolean hasCapability(Capability c) {
		return capabilities.contains(c);
	}
	
	/**
	 * This method will poll this <code>HPActor</code>'s current <code>Location loc</code>
	 * to find potential exits, and replaces all the instances of <code>Move</code>
	 * in this <code>HPActor</code>'s command set with <code>Moves</code> to the new exits.
	 * <p>
	 * This method doesn't affect other non-movement actions in this <code>HPActor</code>'s command set.
	 *  
	 * @author 	ram
	 * @param 	loc this <code>HPActor</code>'s location
	 * @pre		<code>loc</code> is the actual location of this <code>HPActor</code>
	 */
	public void resetMoveCommands(Location loc) {
		HashSet<HPActionInterface> newActions = new HashSet<HPActionInterface>();
		
		// Copy all the existing non-movement options to newActions
		for (HPActionInterface a: actions) {
			if (!a.isMoveCommand())
				newActions.add(a);
		}
		
		// add new movement possibilities
		for (CompassBearing d: CompassBearing.values()) { 														  
			if (loc.getNeighbour(d) != null) //if there is an exit from the current location in direction d, add that as a Move command
				newActions.add(new Move(d,messageRenderer, world)); 
		}
		
		// replace command list of this HPActor
		this.actions = newActions;		
		
		// TODO: This assumes that the only actions are the Move actions. This will clobber any others. Needs to be fixed.
		/* Actually, that's not the case: all non-movement actions are transferred to newActions before the movements are transferred. --ram */
	}
	
	public void setMobility(boolean argMobile) {
		mobile = argMobile;
	}

	public boolean getMobility() {
		return mobile;
	}
	
	public void shield() {
		shielded = true;
	}
	
	public boolean shielded() {
		return shielded;
	}
	
	public void learnSpell(Spells spellToLearn) {
		assert !knownSpells.contains(spellToLearn) : "Actor already knows this spell";
		
		knownSpells.add(spellToLearn);
	}
	
	@SuppressWarnings("unchecked")
	public HashSet<Spells> getSpells() {
		return (HashSet<Spells>) knownSpells.clone();
	}

	public void teleport(HPLocation destination) {
		world.getEntityManager().setLocation(this, destination);
		this.resetMoveCommands(world.find(this));
	}
	
	public boolean inventoryFull() {
		if (capabilities.contains(Capability.INVENTORY) && inventory.size() < INVENTORY_SIZE) {
			return false;
		} else {
			return true;
		}
	}
	
	public void addToInventory(HPEntityInterface item) {
		inventory.add(item);
		item.addAffordance(new Carry(item, messageRenderer));
	}
	
	public void removeFromInventory(HPEntityInterface item) {
		inventory.remove(item);
	}
	
	/**
	 * Has this actor receive an item into their inventory or held item slot.
	 * @param item The item being received by this actor
	 */
	public void receiveItem(HPEntityInterface item) {
		assert canReceiveItem() : "Cannot receive item";
		
		if (this.getItemCarried() == null) {
			this.setItemCarried(item);
		} else {
			this.addToInventory(item);
		}
	}
	
	/**
	 * Finds out whether or not this actor can receive an item.
	 * @return Whether or not the actor has space in their inventory or held item slot to receive an item
	 */
	public boolean canReceiveItem() {
		if (this.getItemCarried() != null && inventoryFull()) {
			return false;
		} else {
			return true;
		}
	}
}
