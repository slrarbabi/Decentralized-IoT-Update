package aut.isec.arbabi.salar.P2PSimulator.Constants;

public final class ConstantNumbers {
    // Safe Ports: 49152–65535
    public static final int FILE_SHARING_minFileSharingPort = 49200;
    public static final int FILE_SHARING_maxFileSharingPort = 65530;
    public static final int LISTENER_connectionListenerPort = 49155;
    public static final int LISTENER_backlog = 49155;

    public static final int FULL_NODES_minNumberOfFullNodesRandomGenerator = 10;
    public static final int FULL_NODES_maxNumberOfFullNodesRandomGenerator = 30;
    public static final int END_PEERS_minNumberOfEndPeersRandomGenerator = 20;
    public static final int END_PEERS_maxNumberOfEndPeersRandomGenerator = 50;

    public static final int FULL_NODES_minFullNodeCapacityRandomGenerator = 50;
    public static final int FULL_NODES_maxFullNodeCapacityRandomGenerator = 1000;

    public static final int FULL_NODES_minFullNodeOwnerIndex = 5;
    public static final int FULL_NODES_maxFullNodeOwnerIndex = 10;

    public static final int END_PEERS_minEndPeerOwnerIndex = 1;
    public static final int END_PEERS_maxEndPeerOwnerIndex = 5;

    //Connection Numbers
    public static final int CONNECTION_success = 200;
    public static final int CONNECTION_error = 0;
    public static final int CONNECTION_UPDATE_REQUEST_commandSize = 6;
    public static final int CONNECTION_UPDATE_FINALIZE_commandSize = 5;
    public static final int CONNECTION_UPDATE_CANCEL_commandSize = 2;
    public static final int CONNECTION_UPDATE_Calculate_commandSize = 3;
//--------------------------------------------------------Connection Strings

    public static final int TRANSFER_BASE_PRICE = 8000; //szabo scale
    public static final int TRANSFER_MIN_PRICE = 10; //szabo scale

    public static final int SCALE_KILO = 1000;

}
