package party.lemons.biomemakeover.entity.camel;

public interface InventoryMount {

    default boolean hasChest(){
        return true;
    }
    default int getInventoryColumns(){
        return 5;
    }
}
