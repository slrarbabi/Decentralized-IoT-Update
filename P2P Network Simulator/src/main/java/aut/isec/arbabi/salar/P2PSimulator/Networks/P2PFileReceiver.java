package aut.isec.arbabi.salar.P2PSimulator.Networks;

import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantStrings;
import aut.isec.arbabi.salar.P2PSimulator.Graphics.UpdateHandlerGUI;

import java.io.*;
import java.net.Socket;


public class P2PFileReceiver extends Thread {

    private Socket clientSocket;
    private int port;
    private IoTDeviceUpdate update;
    private UpdateHandlerGUI updateHandlerGUI;
    private PortHandler portHandler;

    public P2PFileReceiver(int port, IoTDeviceUpdate update, UpdateHandlerGUI updateHandlerGUI,
                           PortHandler portHandler) {
        this.port = port;
        this.update = update;
        this.updateHandlerGUI = updateHandlerGUI;
        this.portHandler = portHandler;
    }

    public void run() {
        System.out.println("Receiver running");
        try {
            clientSocket = new Socket(ConstantStrings.localHostIP, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveFile();
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateUI();
        log();
        portHandler.freePort(port);
    }

    private void saveFile() {
        try {
            System.out.println("Receiver receiving files");
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            FileOutputStream fos =
                    new FileOutputStream(update.getReceiver().getUpdateFilePath(update.getUpdateFileName()));
            byte[] buffer = new byte[update.getBandWidth()];
            int read;
            int remaining = update.getUpdateFileSize();
            while ((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                remaining -= read;
                fos.write(buffer, 0, read);
            }
            fos.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        update.endUpdate();
    }

    private void log() {
        System.out.println("Receiver logging");
        String log = ConstantStrings.CONNECTION_LOG_seperator + ConstantStrings.newLineString +
                "New update added for device: (" + update.getReceiver().getIPAdress() + "), with file name: (" +
                update.getUpdateFileName() + "), size: (" + update.getUpdateFileSize() + "), hash: (" +
                update.getHashFile() + "), at time: (" + update.getDoneTimeStamp() + ")," + "device owner was: (" +
                update.getReceiver().getOwner() +
                ConstantStrings.newLineString + "Sender was: (" +
                update.getSender().getIPAdress() + "), with update price of: " + update.getUpdatePrice() +
                "), and transfer price of: (" + update.getTransferPrice() + "), sender owner was: (" +
                update.getSender().getOwner() + ")" +
                ConstantStrings.newLineString +
                ConstantStrings.CONNECTION_LOG_seperator + ConstantStrings.newLineString;
        try (FileWriter fw = new FileWriter(update.getReceiver().getUpdateHistoryFilePath(), true);
             BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            out.println(log);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        System.out.println("Receiver updating UI");
        updateHandlerGUI.removePendingData(update.getReceiver().getIPAdress());
        String[] newData = new String[updateHandlerGUI.getDoneDataSize()];
        newData[UpdateHandlerGUI.DoneDataTemplate.DeviceIP.index] = update.getReceiver().getIPAdress();
        newData[UpdateHandlerGUI.DoneDataTemplate.ReqTime.index] = update.getRequestTimeStamp();
        newData[UpdateHandlerGUI.DoneDataTemplate.FinishedTime.index] = update.getDoneTimeStamp();
        newData[UpdateHandlerGUI.DoneDataTemplate.Sender.index] = update.getSender().getIPAdress();
        newData[UpdateHandlerGUI.DoneDataTemplate.FileName.index] = update.getUpdateFileName();
        newData[UpdateHandlerGUI.DoneDataTemplate.FileHash.index] = update.getHashFile();
        newData[UpdateHandlerGUI.DoneDataTemplate.UpdatePrice.index] = update.getUpdatePrice();
        newData[UpdateHandlerGUI.DoneDataTemplate.TransferPrice.index] = update.getTransferPrice();
        updateHandlerGUI.addNewData(newData, UpdateHandlerGUI.StateType.DONE);
    }
}