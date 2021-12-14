package clients;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import common.FinalData;
import common.HelperData;
import common.TransferMessage;

public class Subscriber {
    private static final int PORT = 8080;
    private Socket socket = null;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;

    private static Scanner sc = new Scanner(System.in);

    public Subscriber(String host, int port) {
        try {
            socket = new Socket(host, port);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String rcvLine = "";
        try {
            System.out.print("Enter name: ");
            String name = sc.next();
            outputStream.writeUTF(name);
            int role = FinalData.SUBSCRIBER_ROLE; // subscriber
            outputStream.writeInt(role);
            rcvLine = inputStream.readUTF();
            System.out.println("Server: " + rcvLine);

            new SubHandleReceiveThread(socket);
            new SubHandleSendThread(socket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String host = "";
        if (args.length < 1) {
            System.out.print("Nhap dia chi ip cua server: ");
            host = sc.next();
        } else {
            host = args[0];
        }
        new Subscriber(host, PORT);
    }

    class SubHandleReceiveThread extends Thread {
        private Socket socket;
        private DataInputStream inputStream = null;

        public SubHandleReceiveThread(Socket _socket) {
            try {
                socket = _socket;
                inputStream = new DataInputStream(socket.getInputStream());
                start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String serverMsg = "";
            while (true) {
                try {
                    serverMsg = inputStream.readUTF();
                    try {
                        TransferMessage transferMessage = new TransferMessage(serverMsg, true);
                        System.out.println("json " + transferMessage.toString());
                    } catch (Exception e) {
                        System.out.println("Server: " + serverMsg);
                    }

                    if (serverMsg.equals(FinalData.RES_BYE)) {
                        break;
                    } else if (serverMsg.equals(FinalData.RES_SUB_TOPIC)) {
                        FinalData.checkSubcribeTopic = true;
                        FinalData.checkUnSubscribeTopic = false;
                    } else if (serverMsg.equals(FinalData.RES_UNSUB_TOPIC)) {
                        FinalData.checkSubcribeTopic = false;
                        FinalData.checkUnSubscribeTopic = true;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

            try {
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    class SubHandleSendThread extends Thread {
        private Socket socket;
        private DataOutputStream outputStream = null;
        private BufferedReader bufferedReader = null;

        public SubHandleSendThread(Socket _socket) {
            try {
                socket = _socket;
                bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                outputStream = new DataOutputStream(socket.getOutputStream());
                start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String inpLine = "";
            while (true) {
                try {
                    inpLine = bufferedReader.readLine();
                    if (inpLine.length() == 0) {
                        continue;
                    }
                    if (inpLine.equals(FinalData.REQ_QUIT)) {
                        outputStream.writeUTF(inpLine);
                        break;
                    }

                    if (FinalData.checkSubcribeTopic) {
                        FinalData.checkSubcribeTopic = false;
                        HelperData helperData = new HelperData(inpLine);
                        outputStream.writeUTF(helperData.toString());
                        continue;
                    }

                    if (FinalData.checkUnSubscribeTopic) {
                        FinalData.checkUnSubscribeTopic = false;
                        HelperData helperData = new HelperData(inpLine);
                        outputStream.writeUTF(helperData.toString());
                        continue;
                    }

                    outputStream.writeUTF(inpLine);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            try {
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
