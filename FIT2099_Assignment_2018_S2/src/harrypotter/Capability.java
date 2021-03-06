package harrypotter;
/**
 * Capabilities that various entities may have.  This is useful in <code>canDo()</code> methods of 
 * <code>HPActionInterface</code> implementations.
 *  
 * @author 	ram
 * @see 	HPActionInterface
 */

public enum Capability {
	CHOPPER,//CHOPPER capability allows an entity to Chop another entity which has the Chop Affordance
	WEAPON,//WEAPON capability allows an entity to Attack another entity which has the Attack Affordance
	WAND,//WAND capability allows an entity to Cast a Spell
	TEACHER,//TEACHER capability allows an actor to teach spells to another actor on the same team
	INVENTORY,//INVENTORY capability allows an actor to carry additional items to the single item they are holding
}
