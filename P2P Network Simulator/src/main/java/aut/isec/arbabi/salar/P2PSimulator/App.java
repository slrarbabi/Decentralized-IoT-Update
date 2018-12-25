package aut.isec.arbabi.salar.P2PSimulator;

import aut.isec.arbabi.salar.P2PSimulator.Networks.NetworkInitiator;

public class App {
    public static void main(String[] args) throws Exception {
        NetworkInitiator networkInitiator = new NetworkInitiator();
        networkInitiator.configInquiry();
    }
}