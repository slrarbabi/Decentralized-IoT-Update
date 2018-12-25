package aut.isec.arbabi.salar.P2PSimulator.Graphics;

import aut.isec.arbabi.salar.P2PSimulator.Constants.CheckValidIP;
import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantNumbers;
import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantStrings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class NetworkInitiatorGUI {
    private final Font font;
    private JFrame networkInitiaterFrame;
    private JLabel addEndPeerLabelErrorHandler;
    private JLabel addFullNodeLabelErrorHandler;
    private JLabel networkCreationErrorHandler;
    private ArrayList<String> fullNodesAddresses;
    private ArrayList<Integer> fullNodesCapacities;
    private ArrayList<String> endPeerAddresses;
    private ArrayList<String> fullNodeOwners;
    private ArrayList<String> endPeerOwners;

    public NetworkInitiatorGUI() {
        this.fullNodesAddresses = new ArrayList<>();
        this.fullNodesCapacities = new ArrayList<>();
        this.endPeerAddresses = new ArrayList<>();
        this.fullNodeOwners = new ArrayList<>();
        this.endPeerOwners = new ArrayList<>();
        this.networkInitiaterFrame = null;
        this.font = new Font(ConstantStrings.FONT_timesFont, Font.ITALIC, 13);
    }

    public void newConfigInquiry(ActionListener positiveListener,
                                 ActionListener negativeListener) {
        networkInitiaterFrame = new JFrame();
        networkInitiaterFrame.setBounds(400, 200, 390, 200);
        networkInitiaterFrame.setTitle(ConstantStrings.NETWORK_INITIATOR_GUI_networkNewConfigInquiryFrameTitle);
        networkInitiaterFrame.setResizable(false);

        final JLabel newConfigInquiryQuestion =
                new JLabel(ConstantStrings.NETWORK_INITIATOR_GUI_networkNewConfigInquiryQuestion);
        newConfigInquiryQuestion.setBounds(20, 10, 350, 100);

        final JButton newConfigInquiryPisitiveAnswerButton =
                new JButton(ConstantStrings.NETWORK_INITIATOR_GUI_yesString);
        newConfigInquiryPisitiveAnswerButton.setBounds(80, 90, 100, 40);
        newConfigInquiryPisitiveAnswerButton.addActionListener(positiveListener);

        final JButton newConfigInquiryNegativeAnswerButton =
                new JButton(ConstantStrings.NETWORK_INITIATOR_GUI_noString);
        newConfigInquiryNegativeAnswerButton.setBounds(190, 90, 100, 40);
        newConfigInquiryNegativeAnswerButton.addActionListener(negativeListener);

        networkInitiaterFrame.add(newConfigInquiryQuestion);
        networkInitiaterFrame.add(newConfigInquiryPisitiveAnswerButton);
        networkInitiaterFrame.add(newConfigInquiryNegativeAnswerButton);

        networkInitiaterFrame.setLayout(null);
        networkInitiaterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        networkInitiaterFrame.pack();
        networkInitiaterFrame.setVisible(true);
    }

    public void getNetworkInitiaters(ActionListener confirmListener, ActionListener autoGenerateListener) {
        networkInitiaterFrame = new JFrame();
        networkInitiaterFrame.setBounds(300, 100, 600, 550);
        networkInitiaterFrame.setResizable(false);
        networkInitiaterFrame.setTitle(ConstantStrings.NETWORK_INITIATOR_GUI_networkInitiaterFrameTitle);

//Full Node Section{
        final JLabel enterFullNodeLabel = new JLabel(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesLabel);
        enterFullNodeLabel.setBounds(34, 35, 70, 20);
        enterFullNodeLabel.setFont(font);

        addFullNodeLabelErrorHandler = new JLabel(ConstantStrings.emptyString);
        addFullNodeLabelErrorHandler.setBounds(120, 35, 200, 20);
        addFullNodeLabelErrorHandler.setFont(font);
        addFullNodeLabelErrorHandler.setForeground(Color.RED);

        final JTextField fullNodeAdressEntry =
                new JTextField(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesAddressPalceHolder);
        fullNodeAdressEntry.setFont(font);
        fullNodeAdressEntry.setBounds(30, 60, 230, 50);
        fullNodeAdressEntry.setForeground(Color.GRAY);
        fullNodeAdressEntry
                .addFocusListener(
                        new EntryTextFocusListener(fullNodeAdressEntry,
                                ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesAddressPalceHolder));

        final JTextField fullNodesCapacityEntry =
                new JTextField(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesCapacityPlaceHolder);
        fullNodesCapacityEntry.setFont(font);
        fullNodesCapacityEntry.setBounds(270, 60, 120, 50);
        fullNodesCapacityEntry.setForeground(Color.GRAY);
        fullNodesCapacityEntry
                .addFocusListener(new EntryTextFocusListener(fullNodesCapacityEntry,
                        ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesCapacityPlaceHolder));

        final JLabel fullNodeOwnerSelectorLabel = new JLabel(ConstantStrings.NETWORK_INITIATOR_GUI_OwnerSelector);
        fullNodeOwnerSelectorLabel.setBounds(425, 60, 100, 20);
        fullNodeOwnerSelectorLabel.setFont(font);

        final JComboBox fullNodeOwnerSelectorBox =
                new JComboBox(ConstantStrings.NETWORK_INITIATOR_GUI_FULL_NODE_SELECTOR_OPTIONS);
        fullNodeOwnerSelectorBox.setBounds(400, 67, 100, 50);
        fullNodeOwnerSelectorBox.setFont(font);

        final JButton addFullNodeButton = new JButton(ConstantStrings.NETWORK_INITIATOR_GUI_plusString);
        addFullNodeButton.setBounds(535, 65, 18, 18);
        addFullNodeButton.setForeground(Color.GREEN);
        addFullNodeButton.setForeground(Color.GREEN);

        final JButton removeFullNodeButton = new JButton(ConstantStrings.NETWORK_INITIATOR_GUI_minusString);
        removeFullNodeButton.setBounds(535, 85, 18, 18);
        removeFullNodeButton.setForeground(Color.RED);
        removeFullNodeButton.setBackground(Color.RED);

        final JTextArea fullNodesContainer = new JTextArea();
        fullNodesContainer.setEditable(false);
        fullNodesContainer.setBounds(130, 130, 300, 60);
        fullNodesContainer.setFont(font);
        JScrollPane fullNodeScrollPane = new JScrollPane(fullNodesContainer);
        fullNodeScrollPane.setBounds(130, 130, 300, 60);
        fullNodeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        addFullNodeButton.addActionListener(e -> {
            String addressEntry = fullNodeAdressEntry.getText();
            String capacityEntry = fullNodesCapacityEntry.getText();
            if (CheckValidIP.checkValidIP(addressEntry) && checkValidCapacity(capacityEntry) &&
                    !fullNodesAddresses.contains(addressEntry) && !endPeerAddresses.contains(addressEntry) &&
                    !addressEntry.equals(ConstantStrings.localHostIP)) {
                String container = fullNodesContainer.getText();
                String ownerSelected = String.valueOf(
                        fullNodeOwnerSelectorBox.getItemAt(fullNodeOwnerSelectorBox.getSelectedIndex()));
                container += (addressEntry + ConstantStrings.commaString + capacityEntry + ConstantStrings.commaString +
                        ownerSelected +
                        ConstantStrings.newLineString);
                fullNodesContainer.setText(container);
                fullNodeAdressEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesAddressPalceHolder);
                fullNodesCapacityEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesCapacityPlaceHolder);
                fullNodesAddresses.add(addressEntry);
                fullNodesCapacities.add(Integer.parseInt(capacityEntry));
                fullNodeOwners.add(ownerSelected);
                addFullNodeLabelErrorHandler.setVisible(false);
            } else if (!CheckValidIP.checkValidIP(addressEntry)) {
                addFullNodeLabelErrorHandler.setText(ConstantStrings.NETWORK_INITIATOR_GUI_ERROR_invalidIP);
                addFullNodeLabelErrorHandler.setVisible(true);
                fullNodeAdressEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesAddressPalceHolder);
            } else if (!checkValidCapacity(capacityEntry)) {
                addFullNodeLabelErrorHandler.setText(ConstantStrings.NETWORK_INITIATOR_GUI_ERROR_invalidCapacity);
                addFullNodeLabelErrorHandler.setVisible(true);
                fullNodesCapacityEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesCapacityPlaceHolder);
            } else if (addressEntry.equals(ConstantStrings.localHostIP)) {
                addFullNodeLabelErrorHandler.setText(ConstantStrings.NETWORK_INITIATOR_GUI_ERROR_IPEqualsLocalHost);
                addFullNodeLabelErrorHandler.setVisible(true);
                fullNodesCapacityEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesCapacityPlaceHolder);
            } else {
                addFullNodeLabelErrorHandler.setText(ConstantStrings.NETWORK_INITIATOR_GUI_ERROR_IPAlreadyExists);
                addFullNodeLabelErrorHandler.setVisible(true);
                fullNodeAdressEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesAddressPalceHolder);
            }
        });

        removeFullNodeButton.addActionListener(e -> {
            String newContainer = ConstantStrings.emptyString;
            String toRemove = fullNodeAdressEntry.getText();
            if (fullNodesAddresses.contains(toRemove)) {
                for (int i = fullNodesAddresses.size() - 1; i >= 0; i--)
                    if (fullNodesAddresses.get(i).equals(toRemove)) {
                        fullNodesAddresses.remove(i);
                        fullNodesCapacities.remove(i);
                        fullNodeOwners.remove(i);
                        break;
                    }
                for (int i = 0; i < fullNodesAddresses.size(); i++) {
                    newContainer +=
                            (fullNodesAddresses.get(i) + ConstantStrings.commaString + fullNodesCapacities.get(i) +
                                    ConstantStrings.commaString + fullNodesAddresses.get(i) +
                                    ConstantStrings.newLineString);
                }
                fullNodeAdressEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesAddressPalceHolder);
                fullNodesCapacityEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesCapacityPlaceHolder);
                fullNodesContainer.setText(newContainer);
                addFullNodeLabelErrorHandler.setVisible(false);
            } else {
                fullNodeAdressEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesAddressPalceHolder);
                addFullNodeLabelErrorHandler.setText(ConstantStrings.NETWORK_INITIATOR_GUI_ERROR_IPDoesNotExist);
                addFullNodeLabelErrorHandler.setVisible(true);
            }
        });
//-----------------------------------------------------------}

//End Peers Section
        final JLabel enterEndPeerLabel = new JLabel(ConstantStrings.NETWORK_INITIATOR_GUI_endPeersLabel);
        enterEndPeerLabel.setBounds(84, 240, 70, 20);
        enterEndPeerLabel.setFont(font);

        addEndPeerLabelErrorHandler = new JLabel(ConstantStrings.emptyString);
        addEndPeerLabelErrorHandler.setBounds(160, 240, 200, 20);
        addEndPeerLabelErrorHandler.setFont(font);
        addEndPeerLabelErrorHandler.setForeground(Color.RED);
        addEndPeerLabelErrorHandler.setVisible(false);

        final JTextField endPeerAdressEntry =
                new JTextField(ConstantStrings.NETWORK_INITIATOR_GUI_endPeersAddressPlaceHolder);
        endPeerAdressEntry.setBounds(80, 265, 240, 50);
        endPeerAdressEntry
                .addFocusListener(
                        new EntryTextFocusListener(endPeerAdressEntry,
                                ConstantStrings.NETWORK_INITIATOR_GUI_endPeersAddressPlaceHolder));
        endPeerAdressEntry.setFont(font);

        final JLabel endPeerOwnerSelectorLabel = new JLabel(ConstantStrings.NETWORK_INITIATOR_GUI_OwnerSelector);
        endPeerOwnerSelectorLabel.setBounds(355, 265, 100, 20);
        endPeerOwnerSelectorLabel.setFont(font);

        final JComboBox endPeerOwnerSelectorBox =
                new JComboBox(ConstantStrings.NETWORK_INITIATOR_GUI_END_PEER_SELECTOR_OPTIONS);
        endPeerOwnerSelectorBox.setBounds(330, 272, 100, 50);
        endPeerOwnerSelectorBox.setFont(font);
        //TODO sadsa

        JButton addEndPeerButton = new JButton(ConstantStrings.NETWORK_INITIATOR_GUI_plusString);
        addEndPeerButton.setBounds(455, 270, 18, 18);
        addEndPeerButton.setForeground(Color.GREEN);
        addEndPeerButton.setForeground(Color.GREEN);

        JButton removeEndPeerButton = new JButton(ConstantStrings.NETWORK_INITIATOR_GUI_minusString);
        removeEndPeerButton.setBounds(455, 290, 18, 18);
        removeEndPeerButton.setForeground(Color.RED);
        removeEndPeerButton.setBackground(Color.RED);

        final JTextArea endPeerContainer = new JTextArea();
        endPeerContainer.setEditable(false);
        endPeerContainer.setBounds(130, 335, 300, 60);
        endPeerContainer.setFont(font);
        JScrollPane endPeerScrollPane = new JScrollPane(endPeerContainer);
        endPeerScrollPane.setBounds(130, 335, 300, 60);
        endPeerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        addEndPeerButton.addActionListener(e -> {
            String addressEntry = endPeerAdressEntry.getText();
            if (CheckValidIP.checkValidIP(addressEntry) && !fullNodesAddresses.contains(addressEntry) &&
                    !endPeerAddresses.contains(addressEntry) && !addressEntry.equals(ConstantStrings.localHostIP)) {
                String container = endPeerContainer.getText();
                String ownerSelected =
                        String.valueOf(endPeerOwnerSelectorBox.getItemAt(endPeerOwnerSelectorBox.getSelectedIndex()));
                container +=
                        (addressEntry + ConstantStrings.commaString + ownerSelected + ConstantStrings.newLineString);
                endPeerContainer.setText(container);
                endPeerAddresses.add(addressEntry);
                endPeerOwners.add(ownerSelected);
                addEndPeerLabelErrorHandler.setVisible(false);
            } else if (!CheckValidIP.checkValidIP(addressEntry)) {
                addEndPeerLabelErrorHandler.setText(ConstantStrings.NETWORK_INITIATOR_GUI_ERROR_invalidIP);
                addEndPeerLabelErrorHandler.setVisible(true);

            } else if (addressEntry.equals(ConstantStrings.localHostIP)) {
                addEndPeerLabelErrorHandler.setText(ConstantStrings.NETWORK_INITIATOR_GUI_ERROR_IPEqualsLocalHost);
                addEndPeerLabelErrorHandler.setVisible(true);
            } else {
                addEndPeerLabelErrorHandler.setText(ConstantStrings.NETWORK_INITIATOR_GUI_ERROR_IPAlreadyExists);
                addEndPeerLabelErrorHandler.setVisible(true);
            }
            endPeerAdressEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_endPeersAddressPlaceHolder);
        });

        removeEndPeerButton.addActionListener(e -> {
            String newContainer = ConstantStrings.emptyString;
            String toRemove = endPeerAdressEntry.getText();
            if (endPeerAddresses.contains(toRemove)) {
                for (int i = endPeerAddresses.size() - 1; i >= 0; i--)
                    if (endPeerAddresses.get(i).equals(toRemove)) {
                        endPeerAddresses.remove(i);
                        endPeerOwners.remove(i);
                        break;
                    }
                for (int i = 0; i < endPeerAddresses.size(); i++) {
                    newContainer +=
                            (endPeerAddresses.get(i) + ConstantStrings.commaString + endPeerOwners.get(i) + "\n");
                }
                endPeerAdressEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_endPeersAddressPlaceHolder);
                endPeerContainer.setText(newContainer);
                addEndPeerLabelErrorHandler.setVisible(false);
            } else {
                addEndPeerLabelErrorHandler.setText(ConstantStrings.NETWORK_INITIATOR_GUI_ERROR_IPDoesNotExist);
                addEndPeerLabelErrorHandler.setVisible(true);
                endPeerAdressEntry.setText(ConstantStrings.NETWORK_INITIATOR_GUI_endPeersAddressPlaceHolder);
            }
        });
//-----------------------------------------------------------


        networkCreationErrorHandler = new JLabel(ConstantStrings.NETWORK_INITIATOR_GUI_ERROR_notEnoughInput);
        networkCreationErrorHandler.setBounds(120, 370, 200, 20);
        networkCreationErrorHandler.setFont(font);
        networkCreationErrorHandler.setForeground(Color.RED);
        networkCreationErrorHandler.setVisible(false);

        JButton confirmButton = new JButton(ConstantStrings.NETWORK_INITIATOR_GUI_confirmString);
        confirmButton.setFont(font);
        confirmButton.setBounds(150, 450, 100, 40);

        JButton autoGenerateButton = new JButton(ConstantStrings.NETWORK_INITIATOR_GUI_autoGenerateString);
        autoGenerateButton.setFont(font);
        autoGenerateButton.setBounds(320, 450, 100, 40);

        networkInitiaterFrame.add(enterFullNodeLabel);
        networkInitiaterFrame.add(addFullNodeLabelErrorHandler);
        networkInitiaterFrame.add(fullNodeAdressEntry);
        networkInitiaterFrame.add(fullNodesCapacityEntry);
        networkInitiaterFrame.add(fullNodeOwnerSelectorLabel);
        networkInitiaterFrame.add(fullNodeOwnerSelectorBox);
        networkInitiaterFrame.add(addFullNodeButton);
        networkInitiaterFrame.add(removeFullNodeButton);
        networkInitiaterFrame.getContentPane().add(fullNodeScrollPane);
        networkInitiaterFrame.add(enterEndPeerLabel);
        networkInitiaterFrame.add(addEndPeerLabelErrorHandler);
        networkInitiaterFrame.add(endPeerAdressEntry);
        networkInitiaterFrame.add(addEndPeerButton);
        networkInitiaterFrame.add(removeEndPeerButton);
        networkInitiaterFrame.add(endPeerScrollPane);
        networkInitiaterFrame.add(endPeerOwnerSelectorLabel);
        networkInitiaterFrame.add(endPeerOwnerSelectorBox);
        networkInitiaterFrame.add(networkCreationErrorHandler);
        networkInitiaterFrame.add(confirmButton);
        networkInitiaterFrame.add(autoGenerateButton);

        confirmButton.addActionListener(confirmListener);
        autoGenerateButton.addActionListener(autoGenerateListener);

        networkInitiaterFrame.setLayout(null);
        networkInitiaterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        networkInitiaterFrame.pack();
        networkInitiaterFrame.setVisible(true);
    }

    private boolean checkValidCapacity(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void notEnoughInput() {
        networkCreationErrorHandler.setVisible(true);
        addEndPeerLabelErrorHandler.setVisible(false);
        addFullNodeLabelErrorHandler.setVisible(false);
    }

    public ArrayList<String> getFullNodesAddresses() {
        return fullNodesAddresses;
    }

    public ArrayList<Integer> getFullNodesCapacities() {
        return fullNodesCapacities;
    }

    public ArrayList<String> getEndPeerAddresses() {
        return endPeerAddresses;
    }

    public ArrayList<String> getFullNodeOwners() {
        return fullNodeOwners;
    }

    public ArrayList<String> getEndPeerOwners() {
        return endPeerOwners;
    }

    public void dispose() {
        networkInitiaterFrame.setVisible(false);
        networkInitiaterFrame.removeAll();
        networkInitiaterFrame.dispose();
    }

    private class EntryTextFocusListener implements FocusListener {
        private JTextField textField;
        private String placeHolder;

        public EntryTextFocusListener(JTextField textField, String placeHolder) {
            this.textField = textField;
            this.placeHolder = placeHolder;
        }

        @Override
        public void focusGained(FocusEvent e) {
            networkCreationErrorHandler.setVisible(false);
            String text = textField.getText();
            if (text.equals(ConstantStrings.NETWORK_INITIATOR_GUI_endPeersAddressPlaceHolder))
                addEndPeerLabelErrorHandler.setVisible(false);
            else if (text.equals(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesAddressPalceHolder) ||
                    text.equals(ConstantStrings.NETWORK_INITIATOR_GUI_fullNodesCapacityPlaceHolder))
                addFullNodeLabelErrorHandler.setVisible(false);
            if (text.equals(placeHolder)) {
                textField.setForeground(Color.BLACK);
                textField.setText(ConstantStrings.emptyString);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (textField.getText().isEmpty()) {
                textField.setForeground(Color.GRAY);
                textField.setText(placeHolder);
            }
        }
    }
}
