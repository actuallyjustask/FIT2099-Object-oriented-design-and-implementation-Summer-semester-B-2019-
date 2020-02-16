package harrypotter.actions;

import edu.monash.fit2099.simulator.space.Location;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.HPAction;
import harrypotter.HPActor;

/**
 * This Enter class extends to the HPAction for the user to use the secret tunnel at the given tunnel entry points.
 *
 * @author Arsalan Shahid
 *
 */


public class Enter extends HPAction {

    Location location;

    public Enter(MessageRenderer m){
        super(m);
    }

    @Override
    public boolean canDo(HPActor a) {
        boolean result = false;

        location = a.world.find(a);

        //Checking if actor is at the door location
        if (location == a.world.getGrid().getLocationByCoordinates(7,4)
        || location == a.world.getGrid().getLocationByCoordinates(0,1)){
            result = true;
        }
        return result;
    }

    @Override
    public void act(HPActor a) {
        //if location is valid then the action to use the tunnel works
        if (location == a.world.getGrid().getLocationByCoordinates(7,4))
            a.world.getEntityManager().setLocation(a, a.world.getGrid().getLocationByCoordinates(0,1));
        else
            a.world.getEntityManager().setLocation(a, a.world.getGrid().getLocationByCoordinates(7,4));

        a.resetMoveCommands(a.world.find(a));
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public String getDescription() {
        return "Enter tunnel";
    }
}
