package model;

/**
 *  Model for Inhouse part.
 *
 * @author Joshua Nantroup
 */

public class InHouse extends Part {

    private int machineId;
    public InHouse(int id, String name, Double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    // Getter for machineId.
    public int getMachineId() { return machineId; }

    // Setter for machineId.
    public void setMachineId(int machineId)  {
        this.machineId = machineId;
    }
}
