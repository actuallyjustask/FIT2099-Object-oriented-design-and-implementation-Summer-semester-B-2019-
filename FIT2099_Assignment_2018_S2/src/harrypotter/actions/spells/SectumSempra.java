package harrypotter.actions.spells;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.HPActor;
import harrypotter.HPEntityInterface;
import harrypotter.Spells;

/**
 * This class realises the AffordanceSpell abstract class and implements its functionality through the act function
 * 
 * @author James Amodeo
 *
 */
public class SectumSempra extends AffordanceSpell {
	
	static final int INVISIBLE_SWORD_DAMAGE = 100;
		
	public SectumSempra(HPEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		super.spellEnum = Spells.SECTUM_SEMPRA;
	}

	@Override
	public void act(HPActor a) {
		if (((HPActor) getTarget()).shielded()) {
			a.say(a.getShortDescription() + "'s Sectum Sempra spell was blocked by " + target.getShortDescription() + "'s shield charm!");
		} else {
			getTarget().takeDamage(INVISIBLE_SWORD_DAMAGE);
			a.say(a.getShortDescription() + " has attacked " + target.getShortDescription() + " as if by an invisible sword!");
			
			if (getTarget().getHitpoints() <= 0) {
				target.setLongDescription(target.getLongDescription() + ", that was killed by a spell");
				getTarget().removeAffordance(this);			
			}
		}
	}
}
