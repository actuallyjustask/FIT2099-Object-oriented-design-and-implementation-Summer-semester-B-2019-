package harrypotter.entities;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.Capability;
import harrypotter.HPEntity;
import harrypotter.HPWorld;
import harrypotter.actions.Leave;
import harrypotter.actions.Take;

public class Broomstick extends HPEntity {

	public Broomstick(MessageRenderer m, HPWorld world) {
		super(m);
		
		this.shortDescription = "a broomstick";
		this.longDescription = "a slender wooden broomstick, with a magical aura surrounding it.";
		this.hitpoints = 50;
		
		this.addAffordance(new Take(this, m));
		this.addAffordance(new Leave(null, this, m, world));
	}
	
	@Override
	public String getSymbol() {
		return "Y";
	}
}
