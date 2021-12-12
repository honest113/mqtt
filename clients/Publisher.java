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

public class Publisher {
    private static final int PORT = 8080;
    // private static final int BUFFER_SIZE = 1024;
    private Socket socket = null;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;

    private static Scanner sc = new Scanner(System.in);

    public Publisher(String host, int port) {
        try {
            socket = new Socket(host, port);

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.err.println(e);
        }

        String rcvLine = "";

        try {
            System.out.print("Enter name: ");
            String name = sc.next();
            outputStream.writeUTF(name);
            int role = FinalData.PUBLISHER_ROLE; // publisher
            outputStream.writeInt(role);
            rcvLine = inputStream.readUTF();
            System.out.println("Server: " + rcvLine);

            new HandleReceiveThread(socket);
            new HandleSendThread(socket);

        } catch (Exception e) {
            System.err.println(e);
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

        new Publisher(host, PORT);
    }

    class HandleReceiveThread extends Thread {
        private Socket socket;
        private DataInputStream inputStream = null;

        public HandleReceiveThread(Socket _socket) {
            try {
                socket = _socket;
                inputStream = new DataInputStream(socket.getInputStream());
                start();
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        @Override
        public void run() {
            String serverMsg = "";
            while (true) {
                try {
                    serverMsg = inputStream.readUTF();
                    System.out.println("Server: " + serverMsg);

                    if (serverMsg.equals(FinalData.RES_BYE)) {
                        break;
                    } else if (serverMsg.equals(FinalData.RES_SET_TOPIC)) {
                        FinalData.checkSetTopic = true;
                    } else if (serverMsg.equals(FinalData.RES_START_PUB_OK)) {
                        FinalData.checkStartPub = true;
                    } else if (serverMsg.equals(FinalData.RES_START_PUB_FAIL) || serverMsg.equals(FinalData.RES_STOP_PUB)) {
                        FinalData.checkStartPub = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

            try {
                inputStream.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class HandleSendThread extends Thread {
        private Socket socket;
        private DataOutputStream outputStream = null;
        private BufferedReader bufferedReader = null;

        public HandleSendThread(Socket _socket) {
            try {
                socket = _socket;
                bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                outputStream = new DataOutputStream(socket.getOutputStream());
                start();
            } catch (Exception e) {
                System.err.println(e);
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

                    if (FinalData.checkSetTopic) {
                        FinalData.checkSetTopic = false;
                        HelperData helperData = new HelperData(inpLine);
                        outputStream.writeUTF(helperData.toString());
                        continue;
                    }

                    // if (FinalData.checkStartPub) {}


                    outputStream.writeUTF(inpLine);

                    
                } catch (IOException e) {
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
