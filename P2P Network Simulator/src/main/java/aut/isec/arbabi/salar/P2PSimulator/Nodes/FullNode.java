package aut.isec.arbabi.salar.P2PSimulator.Nodes;

import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantNumbers;

public class FullNode {
    private final String IPAdress;
    private final String owner;
    private final int capacity;
    private int consumedCapacity;

    public FullNode(String IPAdress, int capacity, String owner) {
        this.IPAdress = IPAdress;
        this.owner = owner;
        this.capacity = capacity * ConstantNumbers.SCALE_KILO;
        this.consumedCapacity = 0;
    }

    public String getIPAdress() {
        return IPAdress;
    }

    public String getOwner() {
        return owner;
    }

    public int getFreeCapacity() {
        int output = capacity - consumedCapacity;
        if (output < 0)
            output = 0;
        return output;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setInUse(int bandWidth) {
        consumedCapacity += bandWidth;
    }

    public void setFree(int bandWidth) {
        consumedCapacity -= bandWidth;
    }

}
