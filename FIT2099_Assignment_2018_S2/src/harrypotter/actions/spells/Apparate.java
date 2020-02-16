package harrypotter.actions.spells;

import edu.monash.fit2099.gridworld.Grid;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import harrypotter.HPActor;
import harrypotter.HPLocation;
import harrypotter.Spells;

import java.util.Random;
import java.util.Scanner;

/**
 * This class adds a new spell to the program that can be taught by a teacher.
 *
 * @author Arsalan Shahid
 *
 */

public class Apparate extends ActionSpell {
    //constructor of this class
    public Apparate(MessageRenderer m) {
        super(m);
        super.spellEnum = Spells.APPARATE;
    }

    @SuppressWarnings("resource")
	@Override
    public void act(HPActor a) {    	
    	// prompt and receive user input
    	System.out.println("Enter the location in format x,y: ");

        //scanning teleport location
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the location '(x,y)': ");
        String input = reader.nextLine();

        int x,y;
        boolean invalid = true;

        // check whether user enters the location within correct format.
        while (invalid) {
            try {
            	// get convert user input into integer-represented coordinates
                x = Integer.parseInt(input.split(",")[0]);
                y = Integer.parseInt(input.split(",")[1]);
                invalid = false;
                // apparate if entered position can be apparated to
                HPLocation destination = a.world.myGrid.getLocationByCoordinates(x, y);
                if (a.world.getEntityManager().contents(destination) == null) {
                	a.teleport(destination);
                } else {
                	// otherwise, damage actor the actor between (1-10)*10 then move destination in a random direction until apparatable
                	a.takeDamage(((new Random()).nextInt(9)+1)*10);
                	while (a.world.getEntityManager().contents(destination) != null) {
                		Grid.CompassBearing randomDirection;
                		do {
                			randomDirection = Grid.CompassBearing.getRandomBearing();
                		} while (destination.getNeighbour(randomDirection) == null);
                		destination = (HPLocation) destination.getNeighbour(randomDirection);
                    }
                	System.out.println(destination);
                	a.teleport(destination);
                }
            } catch (Exception e){
            	// repeat request for user input
                System.out.println("Please enter in format x,y: ");
                System.out.println("Please enter in format '(x,y)': ");
                input = reader.nextLine();
            }
        }
        
        a.say(a.getShortDescription() + " has cast Apparate on themself!");
    }
}
