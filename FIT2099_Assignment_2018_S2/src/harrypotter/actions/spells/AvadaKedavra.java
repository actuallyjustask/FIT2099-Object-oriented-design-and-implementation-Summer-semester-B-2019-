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
public class AvadaKedavra extends AffordanceSpell {
	
	public AvadaKedavra(HPEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		super.spellEnum = Spells.AVADA_KEDAVRA;
	}

	@Override
	public void act(HPActor a) {
		// kill
		getTarget().takeDamage(getTarget().getHitpoints());
		
		a.say(a.getShortDescription() + " has cast Avada Kedavra on " + target.getShortDescription() + "!");
		
		if (getTarget().getHitpoints() <= 0) {
			target.setLongDescription(target.getLongDescription() + ", that was killed by a spell");
			getTarget().removeAffordance(this);			
		}
	}
}
