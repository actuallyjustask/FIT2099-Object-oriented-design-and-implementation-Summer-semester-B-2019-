package harrypotter.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.HPAction;
import harrypotter.HPActor;
import harrypotter.HPAffordance;
import harrypotter.HPEntityInterface;

public class Heal extends HPAffordance {

    HPEntityInterface potion;

    public Heal(HPEntityInterface initPotion, MessageRenderer m){
        super(initPotion, m);
        potion = initPotion;
    }

    @Override
    public boolean canDo(HPActor a) {
        return !a.isDead();
    }

    @Override
    public void act(HPActor a) {
        a.takeDamage(-potion.getHitpoints());

        HPAction.getEntitymanager().remove(target);
        target.removeAffordance(this);
    }

    @Override
    public String getDescription() {
        return "Heal Player";
    }
}
