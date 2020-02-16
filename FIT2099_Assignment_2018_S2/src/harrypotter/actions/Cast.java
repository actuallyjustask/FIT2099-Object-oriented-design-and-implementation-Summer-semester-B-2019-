package harrypotter.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.Capability;
import harrypotter.HPAction;
import harrypotter.HPActor;

public class Cast extends HPAction {

	public Cast(MessageRenderer m) {
		super(m);
	}

	@Override
	public boolean canDo(HPActor a) {
		if (a.getItemCarried() != null) {
			return !a.isDead() && a.getItemCarried().hasCapability(Capability.WAND);
		} else {
			return false;
		}
	}

	@Override
	public void act(HPActor a) {
		
	}

	@Override
	public int getDuration() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "cast spell";
	}
}
