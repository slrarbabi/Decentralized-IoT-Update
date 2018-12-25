package aut.isec.arbabi.salar.P2PSimulator.Graphics;

import aut.isec.arbabi.salar.P2PSimulator.Constants.ConstantStrings;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class UpdateHandlerGUI extends Thread {
    private final String[] pendingColumnNames =
            {"Device IP", "Status", "File Name", "File Hash", "Request Time", "Sender", "Update Price(szabo)", "Band Width(kBps)",
                    "Transfer Price(szabo)"};
    private final String[] doneColumnNames =
            {"Device IP", "Request Time", "Finished Time", "Sender", "File Name", "File Hash", "Update Price(szabo)",
                    "Transfer Price(szabo)"};
    private final String[] cancelColumnNames = {"Device IP", "Request Time", "Cancel Time"};

    private Object[][] pendingData = {};
    private Object[][] canceledData = {};
    private Object[][] doneData = {};
    private StateType currentStateType;
    private Font font;

    private JRadioButton pendingRadioButton;
    private JRadioButton doneRadioButton;
    private JRadioButton canceledRadioButton;
    private JTable table;
    private JFrame updateHandlerGUIFrame;
    private JScrollPane scrollPane;

    public UpdateHandlerGUI() {
        this.updateHandlerGUIFrame = new JFrame();
        this.font = new Font(ConstantStrings.FONT_timesFont, Font.ITALIC, 13);
        loadBasicComponents();
        updateHandlerGUIFrame.setVisible(true);
    }

    public void run() {
        pendingRadioButton.addActionListener(e -> {
            if (currentStateType == StateType.PENDING)
                return;
            refresh(pendingData, pendingColumnNames);
            currentStateType = StateType.PENDING;
        });
        doneRadioButton.addActionListener(e -> {
            if (currentStateType == StateType.DONE)
                return;
            refresh(doneData, doneColumnNames);
            currentStateType = StateType.DONE;
        });
        canceledRadioButton.addActionListener(e -> {
            if (currentStateType == StateType.CANCELED)
                return;
            refresh(canceledData, cancelColumnNames);
            currentStateType = StateType.CANCELED;
        });
    }

    private void refresh(Object[][] content, String[] columns) {
        updateHandlerGUIFrame.remove(scrollPane);
        table = new JTable(content, columns);
        table.setBounds(5, 90, 825, 430);
        table.setFillsViewportHeight(true);
        table.setFont(font);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 80, 830, 440);
        updateHandlerGUIFrame.add(scrollPane);
        updateHandlerGUIFrame.repaint();
    }

    private void loadBasicComponents() {
        updateHandlerGUIFrame.setBounds(200, 100, 850, 600);
        JLabel label = new JLabel("Pick the radio buttons below to see updates with all status");
        label.setBounds(250, 0, 500, 30);
        label.setFont(font);
        pendingRadioButton = new JRadioButton("Pending", true);
        pendingRadioButton.setFont(font);
        doneRadioButton = new JRadioButton("Done");
        doneRadioButton.setFont(font);
        canceledRadioButton = new JRadioButton("Canceled");
        canceledRadioButton.setFont(font);
        pendingRadioButton.setBounds(140, 40, 100, 30);
        doneRadioButton.setBounds(390, 40, 100, 30);
        canceledRadioButton.setBounds(600, 40, 100, 30);
        ButtonGroup group = new ButtonGroup();


        table = new JTable(pendingData, pendingColumnNames);
        table.setBounds(5, 90, 825, 430);
        table.setFillsViewportHeight(true);
        table.setFont(font);

        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 80, 830, 440);

        currentStateType = StateType.PENDING;

        group.add(pendingRadioButton);
        group.add(doneRadioButton);
        group.add(canceledRadioButton);
        updateHandlerGUIFrame.add(label);
        updateHandlerGUIFrame.add(pendingRadioButton);
        updateHandlerGUIFrame.add(doneRadioButton);
        updateHandlerGUIFrame.add(canceledRadioButton);
        updateHandlerGUIFrame.add(scrollPane);

        updateHandlerGUIFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateHandlerGUIFrame.setLayout(null);
        updateHandlerGUIFrame.setResizable(false);
    }

//    private Icon getIcon(Update_Type type) {
//        try {
//            return new ImageIcon(ImageIO.read(new File(type.path)));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public void addNewData(String[] newData, StateType newDataType) {
        switch (newDataType) {
            case PENDING:
                pendingData = Arrays.copyOf(pendingData, pendingData.length + 1);
                pendingData[pendingData.length - 1] = newData;
                break;
            case DONE:
                doneData = Arrays.copyOf(doneData, doneData.length + 1);
                doneData[doneData.length - 1] = newData;
                break;
            case CANCELED:
                canceledData = Arrays.copyOf(canceledData, canceledData.length + 1);
                canceledData[canceledData.length - 1] = newData;
                break;
        }
        switch (currentStateType) {
            case CANCELED:
                refresh(canceledData, cancelColumnNames);
                break;
            case DONE:
                refresh(doneData, doneColumnNames);
                break;
            case PENDING:
                refresh(pendingData, pendingColumnNames);
                break;
        }
    }

    public void removePendingData(String deviceIPtoRemove) {
        Object[][] newPendingData = new Object[pendingData.length - 1][pendingData[0].length];
        int indexCounter = 0;
        for (int i = 0; i < pendingData.length; i++)
            if (!deviceIPtoRemove.equals(pendingData[i][0])) {
                newPendingData[indexCounter] = pendingData[i];
                indexCounter++;
            }
        pendingData = newPendingData;
        if (currentStateType == StateType.PENDING)
            refresh(pendingData, pendingColumnNames);
    }

    public void setTransferingMode(String deviceIP, String sender, int bandWidth, String transferPrice) {
        for (int i = 0; i < pendingData.length; i++) {
            if (deviceIP.equals(pendingData[i][0])) {
                pendingData[i][PendingDataTemplate.Status.index] = "Transferring";
                pendingData[i][PendingDataTemplate.Sender.index] = sender;
                pendingData[i][PendingDataTemplate.BW.index] = String.valueOf(bandWidth);
                pendingData[i][PendingDataTemplate.TransferPrice.index] = transferPrice;
                break;
            }
        }
        if (currentStateType == StateType.PENDING)
            refresh(pendingData, pendingColumnNames);
    }

    public int getPendingDataSize() {
        return pendingColumnNames.length;
    }

    public int getDoneDataSize() {
        return doneColumnNames.length;
    }

    public int getCanceledDataSize() {
        return cancelColumnNames.length;
    }

    public enum StateType {
        PENDING, CANCELED, DONE
    }

    public enum PendingDataTemplate {
        DeviceIP(0), Status(1), FileName(2), FileHash(3), ReqTime(4), Sender(5), UpdatePrice(6), BW(7), TransferPrice(
                8);
        public final int index;

        PendingDataTemplate(int index) {
            this.index = index;
        }
    }

    public enum DoneDataTemplate {
        DeviceIP(0), ReqTime(1), FinishedTime(2), Sender(3), FileName(4), FileHash(5), UpdatePrice(6), TransferPrice(7);
        public final int index;

        DoneDataTemplate(int index) {
            this.index = index;
        }
    }

    public enum CanceledDataTemplate {
        DeviceIP(0), ReqTime(1), CancelTime(2);
        public final int index;

        CanceledDataTemplate(int index) {
            this.index = index;
        }
    }
}
