package harrypotter.actions.spells;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.Capability;
import harrypotter.HPActor;
import harrypotter.HPAffordance;
import harrypotter.HPEntityInterface;
import harrypotter.Spells;

/**
 * This abstract class serves as a template for all spells that act as affordances.
 * 
 * @author James Amodeo
 *
 */
public abstract class AffordanceSpell extends HPAffordance {
	
	protected Spells spellEnum;
	
	public AffordanceSpell(HPEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
	}

	@Override
	public boolean canDo(HPActor a) {
		if (a.getItemCarried() != null) {
			return !a.isDead() && a.getItemCarried().hasCapability(Capability.WAND) && a.getSpells().contains(spellEnum);
		} else {
			return false;
		}
	}

	@Override
	public abstract void act(HPActor a);

	@Override
	public String getDescription() {
		return "cast " + spellEnum.getName() + " on " + target.getShortDescription();
	}
}
