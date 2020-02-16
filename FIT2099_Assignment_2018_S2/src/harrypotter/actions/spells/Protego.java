package harrypotter.actions.spells;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.HPActor;
import harrypotter.Spells;

/**
 * This class realises the ActionSpell abstract class and implements its functionality through the act function
 * 
 * @author James Amodeo
 *
 */
public class Protego extends ActionSpell {
	
	public Protego(MessageRenderer m) {
		super(m);
		super.spellEnum = Spells.PROTEGO;
	}

	@Override
	public void act(HPActor a) {
		a.shield();
		a.say(a.getShortDescription() + " has cast Protego on themself!");
	}
}
