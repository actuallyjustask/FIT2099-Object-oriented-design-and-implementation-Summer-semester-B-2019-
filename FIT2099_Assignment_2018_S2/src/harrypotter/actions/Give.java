package harrypotter.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.HPActor;
import harrypotter.HPAffordance;
import harrypotter.HPEntityInterface;
import harrypotter.HPLocation;
import harrypotter.HPWorld;

/**
 * This Give class extends to the HPAffordance for the user to be give the item it is currently carrying to another actor.
 *
 * @author Arsalan Shahid
 *
 */




public class Give extends HPAffordance {
	
	//declarations
	private HPActor actor1;
	private HPActor actor2;
	private HPWorld world;
	
	//constructor 
	public Give(HPActor initActor1, HPActor initActor2, HPEntityInterface theTarget, MessageRenderer m, HPWorld initWorld) {
		super(theTarget, m);
		actor1 = initActor1;
		actor2 = (HPActor) theTarget;
		world = initWorld;
	}
	
	//
	//boolean to check if actor2 is in the location where actor 1
	//left the item
	public boolean atSameLocation() {
		return world.find(actor1) == world.find(actor2);
	}

	@Override
	public boolean canDo(HPActor a) {
		return !a.isDead() && a.getItemCarried() != null;
	}

	@Override
	//method to give item
	public void act(HPActor a) {
		HPEntityInterface item = a.getItemCarried();
		
		double probability = 1;
		
		//checking if actor2 is player controlled
		if(!actor2.isHumanControlled())
			probability = 0.5;
		
		//checking if probability is more than randomness
		if(probability > Math.random()) {
			HPEntityInterface oldItem = actor2.getItemCarried();
			if (oldItem != null) {
				world.getEntityManager().setLocation(oldItem, (HPLocation) world.find(actor2));
				oldItem.addAffordance(new Take(oldItem, messageRenderer));
			}
			actor2.setItemCarried(item);
			a.setItemCarried(null);
			System.out.println("Accepted");
		}
		else
			System.out.println("Refused");
	}

	@Override
	public String getDescription() {
		return "give held item to " + target.getShortDescription();
	}
}
