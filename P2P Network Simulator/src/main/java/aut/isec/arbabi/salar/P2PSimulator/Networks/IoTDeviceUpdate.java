package aut.isec.arbabi.salar.P2PSimulator.Networks;

import aut.isec.arbabi.salar.P2PSimulator.Constants.GetCurrentDate;
import aut.isec.arbabi.salar.P2PSimulator.Nodes.EndPeer;
import aut.isec.arbabi.salar.P2PSimulator.Nodes.FullNode;
import org.apache.commons.codec.digest.DigestUtils;

public class IoTDeviceUpdate {
    private EndPeer receiver;
    private FullNode sender;
    private byte[] updateFileContent;
    private int bandWidth;
    private String requestTimeStamp;
    private String doneTimeStamp;
    private String updateFileName;
    private String hashFile;
    private String transferPrice;
    private String updatePrice;

    public IoTDeviceUpdate(EndPeer target, byte[] updateFile, String updateFileName, String updatePrice) {
        this.receiver = target;
        this.requestTimeStamp = GetCurrentDate.getCurrentDate(false);
        this.updateFileContent = updateFile;
        this.hashFile = DigestUtils.sha256Hex(updateFile);
        this.updateFileName = updateFileName;
        this.updatePrice = updatePrice;
    }

    public void finalize(FullNode fullNode, String price, int inputBandWidth) {
        sender = fullNode;
        bandWidth = inputBandWidth;
        sender.setInUse(bandWidth);
        this.transferPrice = price;
    }

    public void endUpdate() {
        doneTimeStamp = GetCurrentDate.getCurrentDate(false);
        sender.setFree(bandWidth);
    }

    public FullNode getSender() {
        return this.sender;
    }

    public EndPeer getReceiver() {
        return receiver;
    }

    public String getUpdateFileName() {
        return this.updateFileName;
    }

    public byte[] getUpdateFileContent() {
        return this.updateFileContent;
    }

    public String getRequestTimeStamp() {
        return this.requestTimeStamp;
    }

    public String getDoneTimeStamp() {
        return this.doneTimeStamp;
    }

    public String getHashFile() {
        return this.hashFile;
    }

    public int getUpdateFileSize() {
        return updateFileContent.length;
    }

    public int getBandWidth() {
        return bandWidth;
    }

    public String getTransferPrice() {
        return transferPrice;
    }

    public String getUpdatePrice() {
        return updatePrice;
    }

}
