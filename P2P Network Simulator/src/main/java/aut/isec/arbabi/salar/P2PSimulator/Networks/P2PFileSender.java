package aut.isec.arbabi.salar.P2PSimulator.Networks;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class P2PFileSender extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int port;
    private P2PFileReceiver receiver;
    private IoTDeviceUpdate update;

    public P2PFileSender(int port, IoTDeviceUpdate update, P2PFileReceiver receiver) {
        this.port = port;
        this.receiver = receiver;
        this.update = update;
    }

    public void run() {
        System.out.println("sender running");
        try {
            serverSocket = new ServerSocket(port);
            receiver.start();
            clientSocket = serverSocket.accept();

        } catch (IOException e) {
            e.printStackTrace();
        }
        sendFile();
        try {
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFile() {
        try {
            System.out.println("sender sending files");
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            InputStream fileInputStream = new ByteArrayInputStream(update.getUpdateFileContent());
            byte[] buffer = new byte[update.getBandWidth()];

            while (fileInputStream.read(buffer) > 0) {
                dataOutputStream.write(buffer);
            }
            fileInputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}