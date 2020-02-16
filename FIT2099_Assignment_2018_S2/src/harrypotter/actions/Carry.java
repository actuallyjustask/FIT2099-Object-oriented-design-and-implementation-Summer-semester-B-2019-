package harrypotter.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.HPActor;
import harrypotter.HPAffordance;
import harrypotter.HPEntityInterface;

/**
 * This Carry class is an affordance that is given to items put into an inventory, and allows them to be taken out and held by an actor.
 * @author James
 *
 */
public class Carry extends HPAffordance {
	
	/**
	 * Constructor function for Carry affordances. All that is done is the calling of the superclass constructor.
	 * @param theTarget the item to be carried (taken out of the inventory and held)
	 * @param m <code>MessageRenderer</code> to display messages
	 */
	public Carry(HPEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
	}

	@Override
	public boolean canDo(HPActor a) {
		return true;
	}

	@Override
	public void act(HPActor a) {
		if (a.getItemCarried() != null) {
			a.addToInventory(a.getItemCarried());
		}
		a.setItemCarried(getTarget());	
		a.removeFromInventory(getTarget());
		getTarget().removeAffordance(this);
	}

	@Override
	public String getDescription() {
		return "carry " + target.getShortDescription();
	}
}
