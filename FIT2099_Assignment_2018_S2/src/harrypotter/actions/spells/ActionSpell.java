package harrypotter.actions.spells;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.Capability;
import harrypotter.HPAction;
import harrypotter.HPActor;
import harrypotter.Spells;

/**
 * This abstract class serves as a template for all spells that act as actions.
 * 
 * @author James Amodeo
 *
 */
public abstract class ActionSpell extends HPAction {

	protected Spells spellEnum;
	
	public ActionSpell(MessageRenderer m) {
		super(m);
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
	public int getDuration() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "cast " + spellEnum.getName();
	}
}
