package aut.isec.arbabi.salar.P2PSimulator.Networks;

import aut.isec.arbabi.salar.P2PSimulator.Constants.CheckValidIP;
import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantNumbers;
import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantStrings;
import aut.isec.arbabi.salar.P2PSimulator.Graphics.NetworkInitiatorGUI;
import com.google.common.net.InetAddresses;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class NetworkInitiator {

    private NetworkInitiatorGUI networkInitiatorGUI;
    private ArrayList<String> endPeerIPRange;
    private ArrayList<Integer> fullNodeCapacities;
    private ArrayList<String> fullNodeIPRange;
    private ArrayList<String> fullNodeOwners;
    private ArrayList<String> endPeerOwners;

    public NetworkInitiator() {
        this.endPeerIPRange = new ArrayList<>();
        this.fullNodeIPRange = new ArrayList<>();
        this.fullNodeCapacities = new ArrayList<>();
        this.fullNodeOwners = new ArrayList<>();
        this.endPeerOwners = new ArrayList<>();
        this.networkInitiatorGUI = new NetworkInitiatorGUI();
    }

    public void configInquiry() {
        File networkConfigFile = new File(ConstantStrings.PATH_networkConfigurationPath);
        if (networkConfigFile.exists() &&
                !networkConfigFile.isDirectory())
            networkInitiatorGUI
                    .newConfigInquiry(new ConfigInquiryPositiveListener(), new ConfigInquiryNegativeListener());
        else
            networkInitiatorGUI.getNetworkInitiaters(new NewConfigListener(), new AutoGenerateListener());
    }

    private void configFinal() {
        FileSharingNetwork sharingNetwork =
                new FileSharingNetwork(fullNodeIPRange, fullNodeCapacities, endPeerIPRange, fullNodeOwners,
                        endPeerOwners);
        networkInitiatorGUI.dispose();
        sharingNetwork.listenToCommands();
    }

    private void configureNetwork(boolean autoGenerate) {
        if (autoGenerate) {
            Random random = new Random();
            int numberOfFullNodes = random.nextInt(ConstantNumbers.FULL_NODES_maxNumberOfFullNodesRandomGenerator -
                    ConstantNumbers.FULL_NODES_minNumberOfFullNodesRandomGenerator) +
                    ConstantNumbers.FULL_NODES_minNumberOfFullNodesRandomGenerator;
            int numberOfEndPeers = random.nextInt(ConstantNumbers.END_PEERS_maxNumberOfEndPeersRandomGenerator -
                    ConstantNumbers.END_PEERS_minNumberOfEndPeersRandomGenerator) +
                    ConstantNumbers.END_PEERS_minNumberOfEndPeersRandomGenerator;
            for (int i = 0; i < numberOfFullNodes; i++) {
                fullNodeIPRange.add(IPGenerator());
                fullNodeCapacities.add((random.nextInt(ConstantNumbers.FULL_NODES_maxFullNodeCapacityRandomGenerator -
                        ConstantNumbers.FULL_NODES_minFullNodeCapacityRandomGenerator) +
                        ConstantNumbers.FULL_NODES_minFullNodeCapacityRandomGenerator));
                fullNodeOwners.add(ConstantStrings.ownerString + ConstantStrings.dashString + (random.nextInt(
                        ConstantNumbers.FULL_NODES_maxFullNodeOwnerIndex -
                                ConstantNumbers.FULL_NODES_minFullNodeOwnerIndex) +
                        ConstantNumbers.FULL_NODES_minFullNodeOwnerIndex));
            }
            for (int i = 0; i < numberOfEndPeers; i++) {
                endPeerIPRange.add(IPGenerator());
                endPeerOwners.add(ConstantStrings.ownerString + ConstantStrings.dashString + (random.nextInt(
                        ConstantNumbers.END_PEERS_maxEndPeerOwnerIndex -
                                ConstantNumbers.END_PEERS_minEndPeerOwnerIndex) +
                        ConstantNumbers.END_PEERS_minEndPeerOwnerIndex));
            }
        } else {
            fullNodeIPRange.addAll(networkInitiatorGUI.getFullNodesAddresses());
            fullNodeCapacities.addAll(networkInitiatorGUI.getFullNodesCapacities());
            endPeerIPRange.addAll(networkInitiatorGUI.getEndPeerAddresses());
            fullNodeOwners.addAll(networkInitiatorGUI.getFullNodeOwners());
            endPeerOwners.addAll(networkInitiatorGUI.getEndPeerOwners());
        }
        saveArtifacts();
        configFinal();
    }

    private void configureNetworkByFile() {
        try {
            String string;
            BufferedReader reader = new BufferedReader(new FileReader(ConstantStrings.PATH_networkConfigurationPath));
            while ((string = reader.readLine()) != null) {
                String[] strings = string.split(ConstantStrings.commaString);
                switch (strings[0]) {
                    case ConstantStrings.CONFIG_LABEL_fullNodeLabel:
                        fullNodeIPRange.add(strings[1]);
                        fullNodeCapacities.add(Integer.parseInt(strings[2]));
                        fullNodeOwners.add(strings[3]);
                        break;
                    case ConstantStrings.CONFIG_LABEL_endPeerLabel:
                        endPeerIPRange.add(strings[1]);
                        endPeerOwners.add(strings[2]);
                        break;
                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        configFinal();
    }

    private void saveArtifacts() {
        try {
            FileWriter writer = new FileWriter(ConstantStrings.PATH_networkConfigurationPath);
            writer.write(ConstantStrings.CONFIG_LABEL_CSVIdentifier + ConstantStrings.newLineString);
            for (int i = 0; i < fullNodeIPRange.size(); i++) {
                writer.write(ConstantStrings.CONFIG_LABEL_fullNodeLabel + ConstantStrings.commaString +
                        fullNodeIPRange.get(i) +
                        ConstantStrings.commaString + fullNodeCapacities.get(i) + ConstantStrings.commaString +
                        fullNodeOwners.get(i) + ConstantStrings.newLineString);
                writer.flush();
            }
            for (int i = 0; i < endPeerIPRange.size(); i++) {
                writer.write(ConstantStrings.CONFIG_LABEL_endPeerLabel + ConstantStrings.commaString +
                        endPeerIPRange.get(i) +
                        ConstantStrings.commaString + endPeerOwners.get(i));
                if (i < endPeerIPRange.size() - 1)
                    writer.write(ConstantStrings.newLineString);
                writer.flush();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String IPGenerator() {
        String ipString;
        do {
            ipString = InetAddresses.fromInteger(new Random().nextInt()).getHostAddress();
        } while (!CheckValidIP.checkValidIP(ipString) || endPeerIPRange.contains(ipString) &&
                fullNodeIPRange.contains(ipString) ||
                ipString.equals(ConstantStrings.localHostIP));
        return ipString;
    }

    private class NewConfigListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (networkInitiatorGUI.getFullNodesAddresses().size() != 0 &&
                    networkInitiatorGUI.getEndPeerAddresses().size() != 0) {
                networkInitiatorGUI.dispose();
                configureNetwork(false);

            } else
                networkInitiatorGUI.notEnoughInput();
        }
    }

    private class AutoGenerateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            networkInitiatorGUI.dispose();
            configureNetwork(true);
        }
    }

    private class ConfigInquiryPositiveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            networkInitiatorGUI.dispose();
            networkInitiatorGUI.getNetworkInitiaters(new NewConfigListener(), new AutoGenerateListener());
        }
    }

    private class ConfigInquiryNegativeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            networkInitiatorGUI.dispose();
            configureNetworkByFile();
        }
    }
}
