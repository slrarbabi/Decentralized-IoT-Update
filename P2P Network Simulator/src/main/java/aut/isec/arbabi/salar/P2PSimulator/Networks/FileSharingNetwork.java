package aut.isec.arbabi.salar.P2PSimulator.Networks;

import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantNumbers;
import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantStrings;
import aut.isec.arbabi.salar.P2PSimulator.Constants.GetCurrentDate;
import aut.isec.arbabi.salar.P2PSimulator.Graphics.UpdateHandlerGUI;
import aut.isec.arbabi.salar.P2PSimulator.Nodes.EndPeer;
import aut.isec.arbabi.salar.P2PSimulator.Nodes.FullNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileSharingNetwork {
    private HashMap<String, EndPeer> endPeers;
    private HashMap<String, FullNode> fullNodes;
    private HashMap<String, IoTDeviceUpdate> pendingUpdates;
    private UpdateHandlerGUI updateHandlerGUI;
    private PortHandler portHandler;


    public FileSharingNetwork(ArrayList<String> fullNodeAddresses, ArrayList<Integer> fullNodeCapacities,
                              ArrayList<String> endPeerAddresses, ArrayList<String> fullNodeOwners,
                              ArrayList<String> endPeerOwners) {
        this.endPeers = new HashMap<>();
        this.fullNodes = new HashMap<>();
        this.pendingUpdates = new HashMap<>();
        this.portHandler = new PortHandler();
        for (int i = 0; i < fullNodeAddresses.size(); i++)
            fullNodes.put(fullNodeAddresses.get(i),
                    new FullNode(fullNodeAddresses.get(i), fullNodeCapacities.get(i),
                            fullNodeOwners.get(i)));
        for (int i = 0; i < endPeerAddresses.size(); i++)
            endPeers.put(endPeerAddresses.get(i), new EndPeer(endPeerAddresses.get(i), endPeerOwners.get(i)));
        updateHandlerGUI = new UpdateHandlerGUI();
    }

    public void listenToCommands() {
        updateHandlerGUI.start();
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(ConstantNumbers.LISTENER_connectionListenerPort),
                    ConstantNumbers.LISTENER_backlog);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.createContext("/", new CommandHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private void P2PFileTransfer(IoTDeviceUpdate deviceUpdate) {
        int port = portHandler.getFreeP2PTransferPort();
        P2PFileReceiver receiver = new P2PFileReceiver(port, deviceUpdate, updateHandlerGUI, portHandler);
        P2PFileSender sender = new P2PFileSender(port, deviceUpdate, receiver);
        sender.start();
    }

    private enum REQUEST_FORMAT_INDEX {
        COMMAND(0), TARGET(1), DUE_DATE(2), UPDATE_FILE_CONTENT(3), UPDATE_FILE_NAME(4), UPDATE_PRICE(5);

        private final int index;

        REQUEST_FORMAT_INDEX(int index) {
            this.index = index;
        }
    }

    private enum FINALIZE_FORMAT_INDEX {
        COMMAND(0), TARGET(1), SENDER(2), TransferPrice(3), Bandwidth(4);

        private final int index;

        FINALIZE_FORMAT_INDEX(int index) {
            this.index = index;
        }
    }

    private class CommandHandler extends FormDataHandler {
        private int commandCounter;

        public CommandHandler() {
            System.out.println(ConstantStrings.CONNECTION_LOG_listening);
            this.commandCounter = 1;
        }

        @Override
        public void handle(HttpExchange httpExchange, List<MultiPart> inputCommands) {
            System.out.println(ConstantStrings.CONNECTION_LOG_seperator + ConstantStrings.newLineString +
                    ConstantStrings.CONNECTION_LOG_handling + commandCounter);
            for (MultiPart part : inputCommands) {
                System.out.println(part.name + " " + part.value);
            }
            String inputFormat = checkRightCommandFormat(inputCommands);
            System.out.println(ConstantStrings.CONNECTION_LOG_commandFormatStatus + inputFormat);
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.getResponseHeaders()
                    .set(ConstantStrings.CONNECTION_HEADER_accessKey,
                            ConstantStrings.CONNECTION_HEADER_accessValue);

            byte[] response = getResponseBody(inputCommands);
            boolean rightInputFormat = inputFormat.equals(ConstantStrings.CONNECTION_INPUT_FORMAT_rightInputFormat);
            if (!rightInputFormat || response.length == 0) {
                httpExchange.getResponseHeaders().set(ConstantStrings.CONNECTION_HEADER_contentTypeKey,
                        ConstantStrings.CONNECTION_HEADER_contentTypeValue_plainText);
                try {
                    httpExchange
                            .sendResponseHeaders(ConstantNumbers.CONNECTION_error, ConstantNumbers.CONNECTION_error
                            );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                httpExchange.getResponseHeaders().set(ConstantStrings.CONNECTION_HEADER_contentTypeKey,
                        getResponsHeaders(inputCommands.get(REQUEST_FORMAT_INDEX.COMMAND.index).value));
            }
            try {
                if (rightInputFormat) {
                    if (response != null && response.length != 0)
                        httpExchange.sendResponseHeaders(ConstantNumbers.CONNECTION_success, response.length);
                    else {
                        httpExchange
                                .sendResponseHeaders(ConstantNumbers.CONNECTION_error,
                                        ConstantNumbers.CONNECTION_error);
                    }
                }
                System.out.println(ConstantStrings.CONNECTION_LOG_responseSuccess);
                outputStream.write(response);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                System.out.println(ConstantStrings.CONNECTION_RESPONSE_ERROR);
                e.printStackTrace();
            }
            commandCounter++;
            System.out.println(ConstantStrings.CONNECTION_LOG_seperator + ConstantStrings.newLineString);
        }

        private byte[] getResponseBody(List<MultiPart> inputCommands) {
            byte[] response = null;
            switch (inputCommands.get(REQUEST_FORMAT_INDEX.COMMAND.index).value) {
                case ConstantStrings.CONNECTION_COMMAND_initializeNetworkCommand:
                    System.out.println(ConstantStrings.CONNECTION_LOG_commandType +
                            ConstantStrings.CONNECTION_COMMAND_initializeNetworkCommand);
                    File file = new File(ConstantStrings.PATH_networkConfigurationPath);
                    response = new byte[(int) file.length()];
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        fileInputStream.read(response);
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case ConstantStrings.CONNECTION_COMMAND_FileTransferReq_command:
                    System.out.println(ConstantStrings.CONNECTION_LOG_commandType +
                            ConstantStrings.CONNECTION_COMMAND_FileTransferReq_command);
                    response = createUpdate(inputCommands.get(REQUEST_FORMAT_INDEX.TARGET.index).value,
                            inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_FILE_CONTENT.index).bytes,
                            inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_FILE_NAME.index).value,
                            inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_PRICE.index).value);
                    break;
                case ConstantStrings.CONNECTION_COMMAND_FileTransferFinal_command:
                    response = ConstantStrings.CONNECTION_RESPONSE_SUCCESS.getBytes();
                    finalizeUpdate(inputCommands.get(FINALIZE_FORMAT_INDEX.TARGET.index).value,
                            inputCommands.get(FINALIZE_FORMAT_INDEX.SENDER.index).value,
                            inputCommands.get(FINALIZE_FORMAT_INDEX.TransferPrice.index).value,
                            inputCommands.get(FINALIZE_FORMAT_INDEX.Bandwidth.index).value);
                    System.out.println(ConstantStrings.CONNECTION_LOG_commandType +
                            ConstantStrings.CONNECTION_COMMAND_FileTransferFinal_command);
                    break;
                case ConstantStrings.CONNECTION_COMMAND_FileTransferCancel_command:
                    System.out.println(ConstantStrings.CONNECTION_LOG_commandType +
                            ConstantStrings.CONNECTION_COMMAND_FileTransferCancel_command);
                    cancelUpdate(inputCommands.get(REQUEST_FORMAT_INDEX.TARGET.index).value);
                    response = ConstantStrings.CONNECTION_RESPONSE_SUCCESS.getBytes();
                    break;
                case ConstantStrings.CONNECTION_COMMAND_CALCULATE_command:
                    response = calculateUpdatePrice(inputCommands.get(FINALIZE_FORMAT_INDEX.TARGET.index).value,
                            inputCommands.get(FINALIZE_FORMAT_INDEX.SENDER.index).value);
                    break;
                default:
                    response = new byte[0];
                    break;
            }
            return response;
        }

        private String getResponsHeaders(String command) {
            switch (command) {
                case ConstantStrings.CONNECTION_COMMAND_FileTransferReq_command:
                    return ConstantStrings.CONNECTION_HEADER_contentTypeValue_plainText;
                case ConstantStrings.CONNECTION_COMMAND_initializeNetworkCommand:
                    return ConstantStrings.CONNECTION_HEADER_contentTypeValue_CSVFile;
                case ConstantStrings.CONNECTION_COMMAND_FileTransferFinal_command:
                    return ConstantStrings.CONNECTION_HEADER_contentTypeValue_plainText;
                case ConstantStrings.CONNECTION_COMMAND_FileTransferCancel_command:
                    return ConstantStrings.CONNECTION_HEADER_contentTypeValue_plainText;
                case ConstantStrings.CONNECTION_COMMAND_CALCULATE_command:
                    return ConstantStrings.CONNECTION_HEADER_contentTypeValue_plainText;
                default:
                    return ConstantStrings.CONNECTION_HEADER_contentTypeValue_plainText;
            }
        }

        private byte[] calculateUpdatePrice(String targetIP, String fullNodeIPs) {
            String output = "";
            String[] allFullNodeIPS = fullNodeIPs.split(ConstantStrings.dashString);
            for (String fullNodeIP : allFullNodeIPS) {
                System.out.println("calculating this: " + fullNodeIP);
                int bandWidth = Math.min(pendingUpdates.get(targetIP).getUpdateFileSize(),
                        fullNodes.get(fullNodeIP).getFreeCapacity());
                if (bandWidth != 0) {
                    double filePercentage =
                            (double) pendingUpdates.get(targetIP).getUpdateFileSize() / bandWidth;
                    double freePercentage =
                            (double) fullNodes.get(fullNodeIP).getCapacity() /
                                    fullNodes.get(fullNodeIP).getFreeCapacity();
                    int transferPrice =
                            Math.max((int) (freePercentage * filePercentage * ConstantNumbers.TRANSFER_BASE_PRICE),
                                    ConstantNumbers.TRANSFER_MIN_PRICE);
                    output += transferPrice + ConstantStrings.dashString + bandWidth + ConstantStrings.dashString;
                } else {
                    output += 0 + ConstantStrings.dashString + 0 + ConstantStrings.dashString;
                }

            }
            System.out.println("out of calculation");
            return output.getBytes();
        }

        private void finalizeUpdate(String receiverIP, String senderIP, String price, String bandwidth) {
            IoTDeviceUpdate update = pendingUpdates.get(receiverIP);
            pendingUpdates.remove(update);
            update.finalize(fullNodes.get(senderIP), price, Integer.parseInt(bandwidth));
            updateHandlerGUI.setTransferingMode(receiverIP, senderIP, update.getBandWidth(), price);
            P2PFileTransfer(update);
        }

        private void cancelUpdate(String deviceIP) {
            IoTDeviceUpdate update = pendingUpdates.get(deviceIP);
            pendingUpdates.remove(deviceIP);
            updateHandlerGUI.removePendingData(deviceIP);
            String[] newData = new String[updateHandlerGUI.getCanceledDataSize()];
            newData[UpdateHandlerGUI.CanceledDataTemplate.DeviceIP.index] = deviceIP;
            newData[UpdateHandlerGUI.CanceledDataTemplate.ReqTime.index] = update.getRequestTimeStamp();
            newData[UpdateHandlerGUI.CanceledDataTemplate.CancelTime.index] = GetCurrentDate.getCurrentDate(false);
            updateHandlerGUI.addNewData(newData, UpdateHandlerGUI.StateType.CANCELED);
        }

        private byte[] createUpdate(String deviceIP, byte[] fileContent, String fileName, String updatePrice) {
            pendingUpdates
                    .put(deviceIP, new IoTDeviceUpdate(endPeers.get(deviceIP), fileContent, fileName, updatePrice));
            String[] newData = new String[updateHandlerGUI.getPendingDataSize()];
            newData[UpdateHandlerGUI.PendingDataTemplate.DeviceIP.index] = deviceIP;
            newData[UpdateHandlerGUI.PendingDataTemplate.Status.index] = "Pending";
            newData[UpdateHandlerGUI.PendingDataTemplate.FileName.index] = fileName;
            newData[UpdateHandlerGUI.PendingDataTemplate.FileHash.index] = pendingUpdates.get(deviceIP).getHashFile();
            newData[UpdateHandlerGUI.PendingDataTemplate.ReqTime.index] = GetCurrentDate.getCurrentDate(false);
            newData[UpdateHandlerGUI.PendingDataTemplate.Sender.index] = ConstantStrings.dashString;
            newData[UpdateHandlerGUI.PendingDataTemplate.BW.index] = ConstantStrings.dashString;
            newData[UpdateHandlerGUI.PendingDataTemplate.UpdatePrice.index] = updatePrice;
            updateHandlerGUI.addNewData(newData, UpdateHandlerGUI.StateType.PENDING);
            return pendingUpdates.get(deviceIP).getHashFile().getBytes();
        }

        private String checkRightCommandFormat(List<MultiPart> inputCommands) {
            if (inputCommands == null || inputCommands.size() == 0)
                return ConstantStrings.CONNECTION_INPUT_FORMAT_ERROR_wrongContent;
            switch (inputCommands.get(REQUEST_FORMAT_INDEX.COMMAND.index).value) {
                case ConstantStrings.CONNECTION_COMMAND_initializeNetworkCommand:
                    if (inputCommands.size() != 1)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_ERROR_initialize;
                    break;
                case ConstantStrings.CONNECTION_COMMAND_FileTransferReq_command:
                    if (inputCommands.size() != ConstantNumbers.CONNECTION_UPDATE_REQUEST_commandSize)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferCommandSize;
                    if (inputCommands.get(REQUEST_FORMAT_INDEX.COMMAND.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferCommandType;
                    if (inputCommands.get(REQUEST_FORMAT_INDEX.TARGET.index).type !=
                            PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferCommandValue;
                    if (!inputCommands.get(REQUEST_FORMAT_INDEX.TARGET.index).name
                            .equals(ConstantStrings.CONNECTION_COMMAND_FileTransferReq_fileTransferNodeTarget))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferTargetNodeType;
                    if (endPeers.get(inputCommands.get(REQUEST_FORMAT_INDEX.TARGET.index).value) == null)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferTargetNodeExist;
                    if (inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_FILE_CONTENT.index).type != PartType.FILE)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileContentType;
                    if (!inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_FILE_CONTENT.index).name
                            .equals(ConstantStrings.CONNECTION_COMMAND_FileTransferReq_fileTransferFileUpdate))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileContentTitle;
                    if (inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_FILE_CONTENT.index).bytes.length == 0)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileContent;
                    if (inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_FILE_NAME.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileNameType;
                    if (!inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_FILE_NAME.index).name
                            .equals(ConstantStrings.CONNECTION_COMMAND_FileTransferReq_fileTransferFileUpdateName))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileNameTitle;
                    if (inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_FILE_NAME.index).value.length() == 0)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferFileName;
                    if (inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_PRICE.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferPriceType;
                    if (!inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_PRICE.index).name
                            .equals(ConstantStrings.CONNECTION_COMMAND_FileTransferReq_fileTransferFileUpdatePrice))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferPriceTitle;
                    if (!isInteger(inputCommands.get(REQUEST_FORMAT_INDEX.UPDATE_PRICE.index).value))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_UPDATE_ERROR_fileTransferPriceValue;
                    break;
                case ConstantStrings.CONNECTION_COMMAND_FileTransferFinal_command:
                    if (inputCommands.size() != ConstantNumbers.CONNECTION_UPDATE_FINALIZE_commandSize)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferCommandSize;
                    if (inputCommands.get(FINALIZE_FORMAT_INDEX.COMMAND.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferCommandType;
                    if (inputCommands.get(FINALIZE_FORMAT_INDEX.TARGET.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferTargetNodeType;
                    if (!inputCommands.get(FINALIZE_FORMAT_INDEX.TARGET.index).name
                            .equals(ConstantStrings.CONNECTION_COMMAND_FileTransferFinal_nodeTarget))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferTargetNodeTitle;
                    if (pendingUpdates.get(inputCommands.get(FINALIZE_FORMAT_INDEX.TARGET.index).value) == null)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferTargetNodeExist;
                    if (inputCommands.get(FINALIZE_FORMAT_INDEX.SENDER.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferSenderType;
                    if (!inputCommands.get(FINALIZE_FORMAT_INDEX.SENDER.index).name
                            .equals(ConstantStrings.CONNECTION_COMMAND_FileTransferFinal_sender))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferSenderTitle;
                    if (fullNodes.get(inputCommands.get(FINALIZE_FORMAT_INDEX.SENDER.index).value) == null)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferSenderExists;
                    if (inputCommands.get(FINALIZE_FORMAT_INDEX.TransferPrice.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferPriceType;
                    if (!inputCommands.get(FINALIZE_FORMAT_INDEX.TransferPrice.index).name
                            .equals(ConstantStrings.CONNECTION_COMMAND_FileTransferFinal_price))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferPriceTitle;
                    if (!isInteger(inputCommands.get(FINALIZE_FORMAT_INDEX.TransferPrice.index).value))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferPriceContent;
                    if (inputCommands.get(FINALIZE_FORMAT_INDEX.Bandwidth.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferBWType;
                    if (!inputCommands.get(FINALIZE_FORMAT_INDEX.Bandwidth.index).name
                            .equals(ConstantStrings.CONNECTION_COMMAND_FileTransferFinal_bandwidth))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferBWTitle;
                    if (!isInteger(inputCommands.get(FINALIZE_FORMAT_INDEX.Bandwidth.index).value))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_FinalizeTransfer_ERROR_fileTransferBWContent;
                    break;
                case ConstantStrings.CONNECTION_COMMAND_FileTransferCancel_command:
                    if (inputCommands.size() != ConstantNumbers.CONNECTION_UPDATE_CANCEL_commandSize)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_CancelTransfer_ERROR_fileTransferCommandSize;
                    if (inputCommands.get(REQUEST_FORMAT_INDEX.COMMAND.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_CancelTransfer_ERROR_fileTransferCommandType;
                    if (inputCommands.get(REQUEST_FORMAT_INDEX.TARGET.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_CancelTransfer_ERROR_fileTransferTargetNodeType;
                    if (!inputCommands.get(REQUEST_FORMAT_INDEX.TARGET.index).name
                            .equals(ConstantStrings.CONNECTION_COMMAND_FileTransferCancel_nodeTarget))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_CancelTransfer_ERROR_fileTransferTargetNodeTitle;
                    if (pendingUpdates.get(inputCommands.get(REQUEST_FORMAT_INDEX.TARGET.index).value) == null)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_CancelTransfer_ERROR_fileTransferTargetNodeExist;
                    break;
                case ConstantStrings.CONNECTION_COMMAND_CALCULATE_command:
                    if (inputCommands.size() != ConstantNumbers.CONNECTION_UPDATE_Calculate_commandSize)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_Calculate_ERROR_commandSize;
                    if (inputCommands.get(FINALIZE_FORMAT_INDEX.COMMAND.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_Calculate_ERROR_commandType;
                    if (inputCommands.get(FINALIZE_FORMAT_INDEX.TARGET.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_Calculate_ERROR_targetType;
                    if (!inputCommands.get(FINALIZE_FORMAT_INDEX.TARGET.index).name
                            .equals(ConstantStrings.CONNECTION_COMMAND_CALCULATE_nodeTarget))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_Calculate_ERROR_targetTitle;
                    if (pendingUpdates.get(inputCommands.get(FINALIZE_FORMAT_INDEX.TARGET.index).value) ==
                            null)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_Calculate_ERROR_targetExists;
                    if (inputCommands.get(FINALIZE_FORMAT_INDEX.SENDER.index).type != PartType.TEXT)
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_Calculate_ERROR_senderType;
                    if (!inputCommands.get(FINALIZE_FORMAT_INDEX.SENDER.index).name
                            .equals(ConstantStrings.CONNECTION_COMMAND_CALCULATE_fullNodeIP))
                        return ConstantStrings.CONNECTION_INPUT_FORMAT_Calculate_ERROR_senderTitle;
                    String[] inputFullNodes = inputCommands.get(FINALIZE_FORMAT_INDEX.SENDER.index).value
                            .split(ConstantStrings.dashString);
                    for (String inputFullNode : inputFullNodes)
                        if (fullNodes.get(inputFullNode) == null)
                            return ConstantStrings.CONNECTION_INPUT_FORMAT_Calculate_ERROR_senderExists;
                    break;
                default:
                    return ConstantStrings.CONNECTION_INPUT_FORMAT_ERROR_wrongFormat;
            }
            return ConstantStrings.CONNECTION_INPUT_FORMAT_rightInputFormat;
        }

        private boolean isInteger(String input) {
            try {
                Integer.parseInt(input);
            } catch (NumberFormatException e) {
                return false;
            } catch (NullPointerException e) {
                return false;
            }
            return true;
        }
    }
}
