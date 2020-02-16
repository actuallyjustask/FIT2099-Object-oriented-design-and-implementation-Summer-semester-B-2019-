package harrypotter.entities.actors;

import java.util.ArrayList;

import edu.monash.fit2099.gridworld.Grid;
import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.HPActor;
import harrypotter.HPLocation;
import harrypotter.HPWorld;
import harrypotter.Team;
import harrypotter.actions.Move;
import harrypotter.entities.actors.behaviors.AttackInformation;
import harrypotter.entities.actors.behaviors.AttackNeighbours;

/**
 * The Dementor is an HPActor subclass that represents an enemy in the Harry Potter game.
 * It's act function includes both attacking and moving, the functionality of which are separated into two separate local functions.
 * A Dementor can be used in the game by creating it as an object and setting its location in an HPWorld's entityManager.
 * 
 * @author James Amodeo
 *
 */
public class Dementor extends HPActor {
	
	public static final int ATTACK_DAMAGE = 40;
	
	static final int MIN_REST = 1;
	static final int MAX_REST = 5;
	static final int MIN_TRAVEL = 1;
	static final int MAX_TRAVEL = 3;
	
	private HPLocation homeBase;
	private Grid.CompassBearing heading;
	private int restingTime = 0;
	private int travelRemaining;
	private boolean returning;
	
	/**
	 * Constructor for Dementor objects. Simply passes its parameters to  its superclass's constructor.
	 * @param hitpoints how many hitpoints the Dementor will have
	 * @param m the message renderer used to write to the console
	 * @param world the world in which the Dementor exists
	 */
	public Dementor(int hitpoints, MessageRenderer m, HPWorld world) {
		super(Team.EVIL, hitpoints, m, world);
	}
	
	/**
	 * Performs the actions of a Dementor.
	 * This includes output of its location, setting of its home base if none is set, attack functionality, then movement functionality.
	 * None of this is done if the Dementor is dead.
	 * 
	 */
	@Override
	public void act() {
		super.act();
		if (isDead()) {
			return;
		}
		say(describeLocation());
		
		// set home base
		if (homeBase == null) {
			homeBase = this.world.getEntityManager().whereIs(this);
		}
		
		attack();
		handleMovement();
	}
	
	private void attack() {
		// attack all neighbours
		ArrayList<AttackInformation> attackables = AttackNeighbours.getAttackables(this, this.world, false, false);
		for (AttackInformation attack : attackables) {
			say(getShortDescription() + " has attacked " + attack.entity.getShortDescription());
			scheduler.schedule(attack.affordance, this, 1);
		}
	}
	
	private void handleMovement() {
		// check if finished return from travel
		if (this.world.getEntityManager().whereIs(this).equals(homeBase) && returning) {
			// begin rest
			heading = null;
			returning = false;
			restingTime = randomIntInRange(MIN_REST, MAX_REST);
		}
		// check if resting
		if (heading == null) {
			// rest
			restingTime--;
			if (restingTime <= 0) {
				// get ready to travel
				heading = Grid.CompassBearing.getRandomBearing();
				travelRemaining = randomIntInRange(MIN_TRAVEL, MAX_TRAVEL);
			}
		// otherwise is traveling
		} else {
			// check if should return
			if (!returning && (travelRemaining <= 0 || !(HPWorld.getEntitymanager().seesExit(this, heading)))) {
				// turn around
				heading = Grid.CompassBearing.opposite(heading);
				returning = true;
			}
			// check if can travel
			if (HPWorld.getEntitymanager().seesExit(this, heading)) {
				// travel
				move(heading);
				travelRemaining--;
			}
		}
	}
	
	private void move(Direction d) {
		say(getShortDescription() + " is heading " + heading + " next.");
		Move myMove = new Move(heading, messageRenderer, world);
		scheduler.schedule(myMove, this, 1);
	}
	
	private int randomIntInRange(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}
	
	@Override
	public String getShortDescription() {
		return "a Dementor";
	}

	@Override
	public String getLongDescription() {
		return this.getShortDescription();
	}

	private String describeLocation() {
		HPLocation location = this.world.getEntityManager().whereIs(this);
		return this.getShortDescription() + " [" + this.getHitpoints() + "] is at " + location.getShortDescription();

	}
}
