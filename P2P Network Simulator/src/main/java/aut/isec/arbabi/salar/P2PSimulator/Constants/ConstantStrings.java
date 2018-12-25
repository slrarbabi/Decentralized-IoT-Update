package aut.isec.arbabi.salar.P2PSimulator.Constants;

public final class ConstantStrings {
    //Font Strings
    public static final String FONT_timesFont = "Times";
//--------------------------------------------------------Font Strings

    //Common Strings
    public static final String emptyString = "";
    public static final String dashString = "-";
    public static final String ownerString = "Owner";
    public static final String slashString = "/";
    public static final String commaString = ",";
    public static final String newLineString = "\n";
    public static final String spaceString = " ";
//--------------------------------------------------------Common Strings

    //Network Initiator Strings
    public static final String NETWORK_INITIATOR_GUI_fullNodesLabel = "Full Nodes:";
    public static final String NETWORK_INITIATOR_GUI_endPeersLabel = "End Peers:";
    public static final String NETWORK_INITIATOR_GUI_networkInitiaterFrameTitle = "New Network Configuration";
    public static final String NETWORK_INITIATOR_GUI_fullNodesCapacityPlaceHolder = "BandWidth (kBps)";
    public static final String NETWORK_INITIATOR_GUI_fullNodesAddressPalceHolder =
            "Please Enter the address of Full Nodes";
    public static final String NETWORK_INITIATOR_GUI_OwnerSelector = "Owner:";
    public static final String NETWORK_INITIATOR_GUI_endPeersAddressPlaceHolder = "Please Enter the address of devices";
    public static final String NETWORK_INITIATOR_GUI_networkNewConfigInquiryFrameTitle = "Change Configuration";
    public static final String NETWORK_INITIATOR_GUI_plusString = "+";
    public static final String NETWORK_INITIATOR_GUI_minusString = "-";
    public static final String NETWORK_INITIATOR_GUI_yesString = "Yes";
    public static final String NETWORK_INITIATOR_GUI_noString = "No";
    public static final String NETWORK_INITIATOR_GUI_confirmString = "Confirm";
    public static final String NETWORK_INITIATOR_GUI_autoGenerateString = "Auto Generate";
    public static final String NETWORK_INITIATOR_GUI_networkNewConfigInquiryQuestion =
            "Do you want to change the existing network config?";
    //Errors
    public static final String NETWORK_INITIATOR_GUI_ERROR_invalidIP = "IP inserted is invalid";
    public static final String NETWORK_INITIATOR_GUI_ERROR_IPAlreadyExists = "IP inserted already exists";
    public static final String NETWORK_INITIATOR_GUI_ERROR_IPDoesNotExist = "IP inserted doesn't exist";
    public static final String NETWORK_INITIATOR_GUI_ERROR_invalidCapacity = "Capacity inserted is invalid";
    public static final String NETWORK_INITIATOR_GUI_ERROR_notEnoughInput = "Not enough input to build network";
    public static final String NETWORK_INITIATOR_GUI_ERROR_IPEqualsLocalHost = "IP inserted can't be localhost";

    public static final String[] NETWORK_INITIATOR_GUI_FULL_NODE_SELECTOR_OPTIONS =
            {"Owner-5", "Owner-6", "Owner-7", "Owner-8", "Owner-9"};
    public static final String[] NETWORK_INITIATOR_GUI_END_PEER_SELECTOR_OPTIONS =
            {"Owner-1", "Owner-2", "Owner-3", "Owner-4"};
//--------------------------------------------------------Network Initiator Strings

    public static final String localHostIP = "127.0.0.1";
    public static final String CONFIG_LABEL_fullNodeLabel = "Full Node";
    public static final String CONFIG_LABEL_endPeerLabel = "End Peer";
    public static final String CONFIG_LABEL_CSVIdentifier = "Node Type, IP, Bandwidth Capacity(kBps) (if full node), Owner";

    //Path Strings
    public static final String PATH_networkConfigurationPath = "Configurations/NetworkConfiguration.csv";
    public static final String PATH_updateHistoryDirectoryName = "Update History";
    public static final String PATH_updateHistoryFileName = "Update History.txt";
//--------------------------------------------------------Path Strings

    //Connection Strings
    public static final String CONNECTION_HEADER_accessKey = "Access-Control-Allow-Origin";
    public static final String CONNECTION_HEADER_accessValue = "*";
    public static final String CONNECTION_HEADER_contentTypeKey = "Content-Type";

    public static final String CONNECTION_HEADER_contentTypeValue_CSVFile = "text/csv";
    public static final String CONNECTION_HEADER_contentTypeValue_plainText = "text/plain";

    public static final String CONNECTION_COMMAND_initializeNetworkCommand = "initializeNetwork";

    public static final String CONNECTION_COMMAND_FileTransferReq_command = "fileTransfer";
    public static final String CONNECTION_COMMAND_FileTransferReq_fileTransferNodeTarget = "target";
    public static final String CONNECTION_COMMAND_FileTransferReq_fileTransferFileUpdate = "updateFile";
    public static final String CONNECTION_COMMAND_FileTransferReq_fileTransferFileUpdateName = "updateFileName";
    public static final String CONNECTION_COMMAND_FileTransferReq_fileTransferFileUpdatePrice = "updatePrice";

    public static final String CONNECTION_COMMAND_FileTransferFinal_command = "finalizePendingFileTransfer";
    public static final String CONNECTION_COMMAND_FileTransferFinal_nodeTarget = "target";
    public static final String CONNECTION_COMMAND_FileTransferFinal_sender = "sender";
    public static final String CONNECTION_COMMAND_FileTransferFinal_bandwidth = "bandwidth";
    public static final String CONNECTION_COMMAND_FileTransferFinal_price = "transferPrice";

    public static final String CONNECTION_COMMAND_FileTransferCancel_command = "cancelPendingFileTransfer";
    public static final String CONNECTION_COMMAND_FileTransferCancel_nodeTarget = "target";

    public static final String CONNECTION_COMMAND_CALCULATE_command = "calculate";
    public static final String CONNECTION_COMMAND_CALCULATE_nodeTarget = "target";
    public static final String CONNECTION_COMMAND_CALCULATE_fullNodeIP = "fullNodeIPs";

    public static final String CONNECTION_RESPONSE_ERROR = "ERROR: Response Error";
    public static final String CONNECTION_RESPONSE_SUCCESS = "SUCCESS";

    public static final String CONNECTION_INPUT_FORMAT_rightInputFormat = "RIGHT-INPUT-FORMAT";
    public static final String CONNECTION_INPUT_FORMAT_ERROR_initialize =
            "ERROR: Network initialize command format error";
    public static final String CONNECTION_INPUT_FORMAT_ERROR_wrongFormat =
            "ERROR: Wrong command format error";
    public static final String CONNECTION_INPUT_FORMAT_ERROR_wrongContent =
            "ERROR: Wrong command content error";

    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferCommandSize =
            "ERROR: File transfer request command with wrong size";
    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferCommandType =
            "ERROR: File transfer request command type error";
    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferCommandValue =
            "ERROR: File Transfer request command value error";
    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferTargetNodeType =
            "ERROR: File Transfer request command target node type error";
    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferTargetNodeExist =
            "ERROR: File Transfer request command target node doesn't exist";
    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileContentType =
            "ERROR: File Transfer request command file content type error";
    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileContentTitle =
            "ERROR: File Transfer request command file content title error";
    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileContent =
            "ERROR: File Transfer request command file content format error";
    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileNameType =
            "ERROR: File Transfer request command file name type error";
    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileNameTitle =
            "ERROR: File Transfer request command file name title error";
    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileName =
            "ERROR: File Transfer request command file name format error";

    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferPriceType =
            "ERROR: File Transfer request command price type error";

    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferPriceTitle =
            "ERROR: File Transfer request command price title error";

    public static final String CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferPriceValue =
            "ERROR: File Transfer request command price value error";

    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferCommandSize =
            "ERROR: File transfer finalize command with wrong size";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferCommandType =
            "ERROR: File transfer finalize command type error";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferTargetNodeType =
            "ERROR: File Transfer finalize command target node type error";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferTargetNodeTitle =
            "ERROR: File Transfer finalize command target name error";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferTargetNodeExist =
            "ERROR: File Transfer finalize command target node doesn't exist";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferSenderType =
            "ERROR: File Transfer finalize command sender type error";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferSenderTitle =
            "ERROR: File Transfer finalize command sender name error";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferSenderExists =
            "ERROR: File Transfer finalize command sender doesn't exist";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferBWType =
            "ERROR: File Transfer finalize command bandwidth type error";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferBWTitle =
            "ERROR: File Transfer finalize command bandwidth name error";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferBWContent =
            "ERROR: File Transfer finalize command bandwidth is not valid";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferPriceType =
            "ERROR: File Transfer finalize command price type error";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferPriceTitle =
            "ERROR: File Transfer finalize command price name error";
    public static final String CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferPriceContent =
            "ERROR: File Transfer finalize command price is not valid";

    public static final String CONNECTION_INPUT_FORMAT_CancelTransfer_ERROR_fileTransferCommandSize =
            "ERROR: File transfer cancel command with wrong size";
    public static final String CONNECTION_INPUT_FORMAT_CancelTransfer_ERROR_fileTransferCommandType =
            "ERROR: File transfer cancel command type error";
    public static final String CONNECTION_INPUT_FORMAT_CancelTransfer_ERROR_fileTransferTargetNodeType =
            "ERROR: File Transfer cancel command target node type error";
    public static final String CONNECTION_INPUT_FORMAT_CancelTransfer_ERROR_fileTransferTargetNodeTitle =
            "ERROR: File Transfer cancel command target name error";
    public static final String CONNECTION_INPUT_FORMAT_CancelTransfer_ERROR_fileTransferTargetNodeExist =
            "ERROR: File Transfer cancel command target node doesn't exist";

    public static final String CONNECTION_INPUT_FORMAT_Calculate_ERROR_commandSize =
            "ERROR: Calculate command with wrong size";
    public static final String CONNECTION_INPUT_FORMAT_Calculate_ERROR_commandType =
            "ERROR: Calculate command with wrong command type";
    public static final String CONNECTION_INPUT_FORMAT_Calculate_ERROR_targetType =
            "ERROR: Calculate command with wrong target type";
    public static final String CONNECTION_INPUT_FORMAT_Calculate_ERROR_targetTitle =
            "ERROR: Calculate command with wrong target title";
    public static final String CONNECTION_INPUT_FORMAT_Calculate_ERROR_targetExists =
            "ERROR: Calculate command Target end peer node doesn't exist";
    public static final String CONNECTION_INPUT_FORMAT_Calculate_ERROR_senderType =
            "ERROR: Calculate command with wrong full node type";
    public static final String CONNECTION_INPUT_FORMAT_Calculate_ERROR_senderTitle =
            "ERROR: Calculate command with wrong full node title";
    public static final String CONNECTION_INPUT_FORMAT_Calculate_ERROR_senderExists =
            "ERROR: Calculate command Target full node doesn't exist";


    public static final String CONNECTION_LOG_seperator =
            "----------------------------------------------------------";
    public static final String CONNECTION_LOG_listening =
            "Listening to incomming commands on port number: " + ConstantNumbers.LISTENER_connectionListenerPort;
    public static final String CONNECTION_LOG_handling =
            "Incomming command to execute, Command Number: ";
    public static final String CONNECTION_LOG_commandType =
            "Command Type: ";
    public static final String CONNECTION_LOG_commandFormatStatus =
            "Command format status: ";
    public static final String CONNECTION_LOG_responseSuccess =
            "Command was handled and responded successfully";
//--------------------------------------------------------Connection Strings


}
