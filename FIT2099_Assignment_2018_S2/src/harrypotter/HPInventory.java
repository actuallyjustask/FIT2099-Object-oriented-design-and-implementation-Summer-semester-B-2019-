package harrypotter;


/**
 * This class sets up an inventory for the actor to be able to add three items.
 *
 * @author Arsalan Shahid
 *
 */

public class HPInventory {

    //declarations
    HPEntityInterface[] Inventory;
    int items;

    //constructor
    public HPInventory() {

        Inventory = new HPEntityInterface[3];
        items = 0;
    }

    //method to add items to inventory
    public boolean addItem(HPEntityInterface item) {
        return items < 3;
    }
}
