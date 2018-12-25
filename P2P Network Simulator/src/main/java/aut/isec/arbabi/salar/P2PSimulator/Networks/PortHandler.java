package aut.isec.arbabi.salar.P2PSimulator.Networks;

import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantNumbers;
import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantStrings;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class PortHandler {
    private HashMap<Integer, Boolean> portInUse;

    public PortHandler() {
        this.portInUse = new HashMap<>();
    }

    public int getFreeP2PTransferPort() {
        for (int portNumber = ConstantNumbers.FILE_SHARING_minFileSharingPort;
             portNumber < ConstantNumbers.FILE_SHARING_maxFileSharingPort;
             portNumber++)
            if (isPortAvailable(portNumber)) {
                portInUse.put(portNumber, true);
                return portNumber;
            }
        return -1;
    }

    public void freePort(int portNumber){
        portInUse.put(portNumber, false);
    }

    private boolean isPortAvailable(int portNumber) {
        if (portInUse.get(portNumber) == null) {
            portInUse.put(portNumber, true);
            return true;
        }
        return !portInUse.get(portNumber);
    }
}
