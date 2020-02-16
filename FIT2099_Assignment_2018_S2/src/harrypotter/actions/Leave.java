package harrypotter.actions;
import edu.monash.fit2099.simulator.space.*;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.HPAction;
import harrypotter.HPActor;
import harrypotter.HPAffordance;
import harrypotter.HPEntityInterface;
import harrypotter.HPLocation;
import harrypotter.HPWorld;

/**
 * This Leave class extends to the HPAffordance for the user to be able to drop the item it is currently carrying.
 *
 * @author Arsalan Shahid
 *
 */

public class Leave extends HPAffordance {
	
	//declarations
	private HPActor actor;
	private HPEntityInterface item;
	private HPWorld world;
	
	//constructor
	public Leave(HPActor initActor, HPEntityInterface theTarget, MessageRenderer m, HPWorld initWorld) {
		super(theTarget, m);
		actor = initActor;
		world = initWorld;
	}
	
	//boolean to check if actor is holding item
	public boolean isHoldingItem() {
		return actor.getItemCarried() != null;
	}

	@Override
	public boolean canDo(HPActor a) {
		return a.getItemCarried() == getTarget();
	}

	@Override
	//method for the actor to leave item
	public void act(HPActor a) {
		item = a.getItemCarried();
		
		Location location = world.find(a);
		
		world.getEntityManager().setLocation(item, (HPLocation)location);
		item.addAffordance(new Take(item, messageRenderer));
		
		a.setItemCarried(null);		
	}

	@Override
	public String getDescription() {
		return "leave " + target.getShortDescription();
	}
	
}
