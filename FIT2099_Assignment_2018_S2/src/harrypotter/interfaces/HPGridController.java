package harrypotter.interfaces;

import java.util.ArrayList;

import edu.monash.fit2099.gridworld.GridController;
import edu.monash.fit2099.gridworld.GridRenderer;
import edu.monash.fit2099.simulator.matter.ActionInterface;
import harrypotter.HPActionInterface;
import harrypotter.HPActor;
import harrypotter.HPGrid;
import harrypotter.HPWorld;
import harrypotter.actions.Cast;
import harrypotter.actions.spells.*;

/**
 * Concrete implementation of the <code>GridController</code>.
 * <p>
 * This controller calls the UI methods to render map, messages and obtain user input.
 * 
 * @author 	Asel
 * @see 	{@link edu.monash.fit2099.gridworld.GridController}
 *
 */
public class HPGridController implements GridController {

	/**The user interface to be used by the controller. All user interfaces should be concrete 
	 * implementations of the <code>GridRenderer</code> interface
	 * 
	 * @see {@link edu.monash.fit2099.gridworld.GridRenderer}*/
	private static HPGridTextInterface ui; 
	
	/**HPgrid of the world*/
	private HPGrid grid;
	
	/**
	 * Constructor of this <code>HPGridController</code>
	 * <p>
	 * The constructor will initialize the <code>grid</code> and the user interface to be used by the controller.
	 * <p>
	 * If a different User Interface (also know as a View) is to be used it must be changed in this constructor.
	 * 
	 * @param 	world the world to be considered by the controller
	 * @pre 	the world should not be null
	 */
	public HPGridController(HPWorld world) {
		this.grid = world.getGrid();
		
		//change the user interface to be used here in the constructor
		HPGridController.ui = new HPGridTextInterface(this.grid); //use a Text Interface to interact
		//this.ui = new HPGridBasicGUI(this.grid); //Use a Basic GUI to interact
		//this.ui = new HPGridGUI(this.grid); //Use a GUI with better graphics to interact
	}

	@Override
	public void render() {
		//Call the UI to handle this
		ui.displayMap();		
	}

	@Override
	public void render(String message) {
		//call the UI to handle this too
		ui.displayMessage(message);
	}
	
	/**
	 * Will return a Action selected by the user.
	 * <p>
	 * This method will provide the user interface with a list of commands from which the user 
	 * needs to select one from and will return this selection.	
	 * 
	 * @param 	a the <code>HPActor</code> for whom an Action needs to be selected
	 * @return	the selected action for the <code>HPActor a</code>
	 */
	public static HPActionInterface getUserDecision(HPActor a) {
		
		// build a list containing all the actions the HPActor can perform (excluding spells)
		ArrayList<ActionInterface> cmds = new ArrayList<ActionInterface>();
		for (HPActionInterface ac : HPWorld.getEntitymanager().getActionsFor(a)) {
			if (ac.canDo(a) && !((ac instanceof ActionSpell)||(ac instanceof AffordanceSpell))) {
				cmds.add(ac);
			}
		}
		// Get the UI to display the commands to the user and get a selection
		HPActionInterface selectedAction = (HPActionInterface) ui.getSelection((cmds));
		
		if (selectedAction instanceof Cast) {
			// build a list containing all the actions the HPActor can perform (only spells)
			ArrayList<ActionInterface> spells = new ArrayList<ActionInterface>();
			for (HPActionInterface ac : HPWorld.getEntitymanager().getActionsFor(a)) {
				if (ac.canDo(a) && ((ac instanceof ActionSpell)||(ac instanceof AffordanceSpell))) {
					spells.add(ac);
				}
			}
			if (spells.size() > 0) {
				selectedAction = (HPActionInterface) ui.getSelection((spells));
			} else {
				ui.displayMessage("No spells available");
				selectedAction = null;
			}
		}
		
		//return selection
		return selectedAction;
	}
	
	public static int getDecision(ArrayList<String> choices) {
		assert (choices.size() > 0) : "No choices available";
		return ui.getStringSelection(choices);
	}
}
