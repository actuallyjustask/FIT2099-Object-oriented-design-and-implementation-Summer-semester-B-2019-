package harrypotter.entities;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.Capability;
import harrypotter.HPEntity;
import harrypotter.HPWorld;
import harrypotter.actions.Leave;
import harrypotter.actions.Take;

/**
 * The wand is an entity that, through its WAND capability, allows an HPActor to cast spells (as long as it is physically possible, and they know the spell).
 * 
 * @author James Amodeo
 *
 */
public class Wand extends HPEntity {
	
	/**
	 * Constructor for wand objects. Initialises attributes according to game design and then adds Take affordance and WAND capability to set up functionality.
	 * @param m the message renderer for the object to write to the console
	 */
	public Wand(MessageRenderer m, HPWorld world) {
		super(m);
		
		this.shortDescription = "a wand";
		this.longDescription = "a long wand made from holly wood, with a phoenix feather core.";
		this.hitpoints = 500;
		
		this.addAffordance(new Take(this, m));
		this.addAffordance(new Leave(null, this, m, world));
		this.capabilities.add(Capability.WAND);
	}
	
	@Override
	public String getSymbol() {
		return "!";
	}
}