package harrypotter;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.actions.Heal;

import java.security.MessageDigest;

/**
 * This class sets up a potion for the HPEntitity to utilise.
 *
 * @author Arsalan Shahid
 *
 */


public class HPPotion extends HPEntity implements HPEntityInterface {
	
	//declarations
	HPWorld world;
	int health;
	String symbol;

	String shortDescription;
	String longDescription;
	

	//potion constuctor
	public HPPotion(MessageRenderer m, HPWorld initWorld) {
		super(m);
		world = initWorld;
		health = (int) Math.round(100 * Math.random()) + 1;
		//
		health = health > 100 ? 100 : health;

		shortDescription = "A potion";
		longDescription = "A potion that heals the actor with a random hitpoints";
		symbol = "p";
		this.addAffordance(new Heal(this, m));

	}

	@Override
	public String getSymbol() {
		return symbol;
	}

	@Override
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public int getHitpoints() {
		return health;
	}

	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	@Override
	public String getLongDescription() {
		return longDescription;
	}

	@Override
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	@Override
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
}
