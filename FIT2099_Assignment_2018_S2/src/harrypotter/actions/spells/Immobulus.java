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
public class Immobulus extends AffordanceSpell {
	
	public Immobulus(HPEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		super.spellEnum = Spells.IMMOBULUS;
	}

	@Override
	public void act(HPActor a) {
		if (((HPActor) getTarget()).shielded()) {
			a.say(a.getShortDescription() + "'s Immobulus spell was blocked by " + target.getShortDescription() + "'s shield charm!");
		} else {
			HPActor targetActor = (HPActor) getTarget();
			targetActor.setMobility(false);
			a.say(a.getShortDescription() + " has frozen " + target.getShortDescription() + " in place!");
		}
	}
}
