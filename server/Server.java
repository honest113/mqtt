package server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import common.HelperData;
import common.PublisherData;
import common.SubscriberData;
import common.Topic;
import common.TransferMessage;

/**
 * Server as Broker
 */

public class Server {
    private static final int PORT = 8080;

    private ServerSocket serverSocket = null;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port, 20);
            while (true) {
                System.out.println("Waiting for client connect...");
                Socket socket = serverSocket.accept();
                new HandleThread(socket);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("==============");
        System.out.println("Start Broker");
        new Server(PORT);
    }
}

class HandleThread extends Thread {
    final String REQ_LIST_TOPIC = "GET LIST TOPIC";
    final String REQ_SET_TOPIC = "SET TOPIC";
    final String REQ_QUIT = "QUIT";
    final String REQ_START_PUB = "START PUB";
    final String REQ_STOP_PUB = "STOP PUB";
    final String REQ_SUB_TOPIC = "SUB TOPIC";
    final String REQ_UNSUB_TOPIC = "UNSUB TOPIC";
    final String REQ_GET_MY_TOPIC = "GET MY TOPIC";

    final String RES_HELLO = "200 Hello ";
    final String RES_LIST_TOPIC = "201 LIST TOPIC OK: ";
    final String RES_SET_TOPIC = "202 SET TOPIC OK";
    final String RES_SUB_TOPIC = "212 SUB TOPIC OK";
    final String RES_UNSUB_TOPIC = "213 UNSUB TOPIC OK";
    final String RES_REGISTER_DONE = "221 REGISTER DONE";
    final String RES_START_PUB_OK = "203 START PUB OK";
    final String RES_STOP_PUB = "204 STOP PUB OK";
    final String RES_RCV_MESSAGE_OK = "205 RECEIVE MESSAGE OK";
    final String RES_SUB_UPDATE_TOPIC = "206 UPDATE TOPIC SUCCESSFUL";
    final String RES_SUB_GET_MY_TOPIC = "208 TOPIC: ";
    
    final String RES_START_PUB_FAIL = "412 START PUB FAIL - REGISTER REQUIRE";
    final String RES_BAD_REQ = "400 BAD REQUEST";
    final String RES_SET_TOPIC_ERROR = "411 SET TOPIC ERROR";
    final String RES_TOPIC_NOT_FOUND = "413 TOPIC NOT FOUND";
    final String RES_BYE = "500 BYE";

    // private static final int BUFFER_SIZE = 1024;
    private static final int PUBLISHER_ROLE = 1;
    private static final int SUBSCRIBER_ROLE = 2;

    private static ArrayList<Topic> listTopics = new ArrayList<Topic>();
    private static ArrayList<PublisherData> lPublisherDatas = new ArrayList<PublisherData>();
    private static ArrayList<SubscriberData> lSubscriberDatas = new ArrayList<SubscriberData>();

    private Socket socket;

    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;

    public HandleThread(Socket _socket) {
        try {
            socket = _socket;
            inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            outputStream = new DataOutputStream(socket.getOutputStream());
            start();
            System.out.println("Client connect: " + socket.getRemoteSocketAddress());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public String getListTopic() {
        String res = RES_LIST_TOPIC;
        for (Topic topic : listTopics) {
            res += "\n--- id: " + topic.getTopicId() + " --- name: " + topic.getTopicName();
        }
        return res;
    }

    public Topic getTopicById(int id) {
        for (Topic topic : listTopics) {
            if (topic.getTopicId() == id) {
                return topic;
            }
        }
        return null;
    }

    /**
     * 
     * @param topicName
     * @return id of topic in list-topic
     */
    public int handleTopicName(String topicName) {
        int res = -1;
        boolean isExistTopic = false;
        for (Topic topic : listTopics) {
            if (topic.getTopicName().equals(topicName)) {
                isExistTopic = true;
                res = topic.getTopicId();
                break;
            }
        }
        if (!isExistTopic) {
            Topic addTopic = new Topic(topicName);
            listTopics.add(addTopic);
            res = addTopic.getTopicId();
        }
        return res;
    }

    public Topic getTopicByName(String topicName) {
        Topic resTopic = null;
        for (Topic iTopic : listTopics) {
            if (iTopic.getTopicName().equals(topicName)) {
                resTopic = iTopic;
                break;
            }
        }
        return resTopic;
    }

    public void TransferMessageToSubscriber(Topic _topic, String _message) {
        for (SubscriberData sData : lSubscriberDatas) {
            if (sData.getSubscriberTopicByName(_topic.getTopicName()) != null) {
                try {
                    sData.outputStream.writeUTF(_message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            String name = "";
            name = inputStream.readUTF();
            int role = inputStream.readInt();
            String resHello = RES_HELLO + name;
            outputStream.writeUTF(resHello);

            if (role == PUBLISHER_ROLE) {
                PublisherData publisherData = new PublisherData(name);
                lPublisherDatas.add(publisherData);
                try {
                    String msgFromPublisher = "";
                    boolean isSetupSuccessful = false;
                    boolean isSetTopic = false;
                    boolean isStartPub = false;

                    while (true) {
                        msgFromPublisher = inputStream.readUTF();
                        System.out.println("Publisher -- " + name + " : " + msgFromPublisher);

                        if (msgFromPublisher.equals(REQ_QUIT)) {
                            outputStream.writeUTF(RES_BYE);
                            break;
                        } else if (msgFromPublisher.equals(REQ_START_PUB)) {
                            if (isStartPub) {
                                continue;
                            }
                            if (isSetupSuccessful) {
                                isStartPub = true;
                                outputStream.writeUTF(RES_START_PUB_OK);
                            } else {
                                isStartPub = false;
                                outputStream.writeUTF(RES_START_PUB_FAIL);
                            }
                            continue;
                        } else if (msgFromPublisher.equals(REQ_STOP_PUB)) {
                            isStartPub = false;
                            outputStream.writeUTF(RES_STOP_PUB);
                            continue;
                        }

                        if (isSetTopic) {
                            isSetTopic = false;
                            try {
                                HelperData helperData = new HelperData(msgFromPublisher, true);
                                int topicId = handleTopicName(helperData.getName());
                                Topic topic = getTopicById(topicId);
                                if (topic == null) {
                                    outputStream.writeUTF(RES_SET_TOPIC_ERROR);
                                    continue;
                                }
                                publisherData.setTopic(topic);
                                outputStream.writeUTF(RES_REGISTER_DONE);
                                isSetupSuccessful = true;
                            } catch (Exception e) {
                                System.err.println(e);
                            }
                            continue;
                        }

                        if (isStartPub) {
                            try {
                                TransferMessage transferMessage = new TransferMessage(msgFromPublisher, publisherData.getName(), publisherData.getTopic());
                                
                                System.out.println("*** tf msg: " + transferMessage.toString());
                                TransferMessageToSubscriber(publisherData.getTopic(), transferMessage.toString());
                                outputStream.writeUTF(RES_RCV_MESSAGE_OK);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            continue;
                        }

                        if (msgFromPublisher.equals(REQ_LIST_TOPIC)) {
                            outputStream.writeUTF(getListTopic());
                            continue;
                        } else if (msgFromPublisher.equals(REQ_SET_TOPIC)) {
                            outputStream.writeUTF(RES_SET_TOPIC);
                            isSetTopic = true;
                            continue;
                        } else {
                            outputStream.writeUTF(RES_BAD_REQ);
                        }
                    }
                } catch (Exception e) {
                    // IOException
                    System.err.println("excute when client ctrl + c");
                    e.printStackTrace();
                }

                lPublisherDatas.remove(publisherData);
                inputStream.close();
                outputStream.close();
                socket.close();
                return;

            } else if (role == SUBSCRIBER_ROLE) {
                /**
                 *  handle subscriber
                 */
                SubscriberData subscriberData = new SubscriberData(name);
                subscriberData.setOutputStream(outputStream);
                lSubscriberDatas.add(subscriberData);
                try {
                    String msgFromSubscriber = "";
                    boolean isSubTopic = false;
                    boolean isUnsubTopic = false;

                    while (true) {
                        msgFromSubscriber = inputStream.readUTF();
                        System.out.println("Subscriber -- " + name + " : " + msgFromSubscriber);
                        
                        if (msgFromSubscriber.equals(REQ_QUIT)) {
                            outputStream.writeUTF(RES_BYE);
                            break;
                        } else if (msgFromSubscriber.equals(REQ_LIST_TOPIC)) {
                            outputStream.writeUTF(getListTopic());
                            continue;
                        } else if (msgFromSubscriber.equals(REQ_SUB_TOPIC)) {
                            outputStream.writeUTF(RES_SUB_TOPIC);
                            isSubTopic = true;
                            isUnsubTopic = false;
                            continue;
                        } else if (msgFromSubscriber.equals(REQ_GET_MY_TOPIC)) {
                            String res = RES_SUB_GET_MY_TOPIC + subscriberData.getListTopics();
                            outputStream.writeUTF(res);
                            continue;
                        } else if (msgFromSubscriber.equals(REQ_UNSUB_TOPIC)) {
                            outputStream.writeUTF(RES_UNSUB_TOPIC);
                            isSubTopic = false;
                            isUnsubTopic = true;
                            continue;
                        }

                        if (isSubTopic) {
                            isSubTopic = false;
                            try {
                                HelperData helperData = new HelperData(msgFromSubscriber, true);
                                Topic subTopic = getTopicByName(helperData.getName());
                                if (subTopic == null) {
                                    outputStream.writeUTF(RES_TOPIC_NOT_FOUND);
                                    continue;
                                } else {
                                    subscriberData.addTopic(subTopic);
                                    outputStream.writeUTF(RES_SUB_UPDATE_TOPIC);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            continue;
                        }

                        if (isUnsubTopic) {
                            isUnsubTopic = false;
                            try {
                                HelperData helperData = new HelperData(msgFromSubscriber, true);
                                Topic rmTopic = subscriberData.getSubscriberTopicByName(helperData.getName());
                                subscriberData.removeTopic(rmTopic);
                                outputStream.writeUTF(RES_SUB_UPDATE_TOPIC);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            continue;
                        }

                        outputStream.writeUTF(RES_BAD_REQ);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                lSubscriberDatas.remove(subscriberData);
                inputStream.close();
                outputStream.close();
                socket.close();
                return;

            } else {
                System.err.println("Error role");
                inputStream.close();
                outputStream.close();
                socket.close();
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
