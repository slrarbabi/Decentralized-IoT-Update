package aut.isec.arbabi.salar.P2PSimulator.Nodes;

import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantStrings;
import aut.isec.arbabi.salar.P2PSimulator.Constants.GetCurrentDate;

import java.io.*;

public class EndPeer {
    private final String IPAdress;
    private final String owner;
    private final String storageDirectoryPath;
    private final String updateHistoryPath;
    private boolean directorySetBefore;

    public EndPeer(String IPAdress, String owner) {
        this.IPAdress = IPAdress;
        this.owner = owner;
        this.directorySetBefore = false;
        storageDirectoryPath =
                ConstantStrings.PATH_updateHistoryDirectoryName + ConstantStrings.slashString + owner +
                        ConstantStrings.slashString + IPAdress;
        updateHistoryPath =
                storageDirectoryPath + ConstantStrings.slashString + IPAdress + ConstantStrings.spaceString +
                        ConstantStrings.PATH_updateHistoryFileName;
    }

    private void setDirectory() {
        if (!directorySetBefore) {
            new File(storageDirectoryPath).mkdirs();
            directorySetBefore = true;
        }
    }

    public String getUpdateHistoryFilePath() {
        setDirectory();
        return updateHistoryPath;
    }

    public String getUpdateFilePath(String fileName) {
        setDirectory();
        String dirPath = GetCurrentDate.getCurrentDate(true);
        new File(storageDirectoryPath + ConstantStrings.slashString + dirPath).mkdirs();
        return storageDirectoryPath + ConstantStrings.slashString + dirPath + ConstantStrings.slashString + fileName;
    }

    public String getIPAdress() {
        return IPAdress;
    }

    public String getOwner() {
        return owner;
    }
}
