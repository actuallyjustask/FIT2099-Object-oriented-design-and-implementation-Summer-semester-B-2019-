package harrypotter.actions.spells;

import edu.monash.fit2099.simulator.time.Scheduler;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.HPActor;
import harrypotter.HPEntityInterface;
import harrypotter.HPWorld;
import harrypotter.Spells;
import harrypotter.actions.Leave;

/**
 * This class realises the AffordanceSpell abstract class and implements its functionality through the act function
 * 
 * @author James Amodeo
 *
 */
public class Expelliarmus extends AffordanceSpell {
	
	private HPWorld world;
	private Scheduler scheduler;
	
	public Expelliarmus(HPEntityInterface theTarget, MessageRenderer m, HPWorld world, Scheduler scheduler) {
		super(theTarget, m);
		super.spellEnum = Spells.EXPELLIARMUS;
		this.world = world;
		this.scheduler = scheduler;
	}

	@Override
	public void act(HPActor a) {
		if (((HPActor) getTarget()).shielded()) {
			a.say(a.getShortDescription() + "'s Expelliarmus spell was blocked by " + target.getShortDescription() + "'s shield charm!");
		} else {
			// make target drop held item
			Leave drop = new Leave(null, ((HPActor) getTarget()).getItemCarried(), messageRenderer, world);
			scheduler.schedule(drop, (HPActor) getTarget(), 1);
			a.say(a.getShortDescription() + " has knocked " + ((HPActor) getTarget()).getItemCarried() + " out of " + target.getShortDescription() + "'s hands using Expelliarmus!");
		}
	}
}
