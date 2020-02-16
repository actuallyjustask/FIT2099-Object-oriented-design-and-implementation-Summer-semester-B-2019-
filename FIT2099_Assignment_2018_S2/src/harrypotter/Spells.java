package harrypotter;

/**
 * This enumeration class holds the names of the spells currently implemented into the game.
 * They can be used in the teaching of spells to an HPActor.
 * 
 * @author James Amodeo
 *
 */
public enum Spells {
	AVADA_KEDAVRA("Avada Kedavra"), // kills the actor it is cast on instantly
	EXPELLIARMUS("Expelliarmus"), // causes the actor it is cast on to drop any item they are holding
	IMMOBULUS("Immobulus"), // freezes the actor it is cast upon
	PROTEGO("Protego"), // protection against magical attacks
	SECTUM_SEMPRA("Sectum Sempra"), // causes wounds as if by an invisible sword
	APPARATE("Apparate"); // helps the actor to teleport
	
	private String name;
	
	Spells(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
