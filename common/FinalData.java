package common;

public class FinalData {
    public static final int PUBLISHER_ROLE = 1;
    public static final int SUBSCRIBER_ROLE = 2;
    public static final String REQ_LIST_TOPIC = "GET LIST TOPIC";
    public static final String REQ_SET_TOPIC = "SET TOPIC";
    public static final String REQ_QUIT = "QUIT";
    public static final String REQ_START_PUB = "START PUB";
    public static final String REQ_STOP_PUB = "STOP PUB";
    public static final String REQ_SUB_TOPIC = "SUB TOPIC";

    public static final String RES_HELLO = "200 Hello ";
    public static final String RES_LIST_TOPIC = "201 LIST TOPIC OK: ";
    public static final String RES_SET_TOPIC = "202 SET TOPIC OK";
    public static final String RES_REGISTER_DONE = "221 REGISTER DONE";
    public static final String RES_START_PUB_OK = "203 START PUB OK";
    public static final String RES_STOP_PUB = "204 STOP PUB OK";
    public static final String RES_RCV_MESSAGE_OK = "205 RECEIVE MESSAGE OK";
    public static final String RES_SUB_TOPIC = "212 SUB TOPIC OK";
    public static final String RES_UNSUB_TOPIC = "213 UNSUB TOPIC OK";

    public static final String RES_BAD_REQ = "400 BAD REQUEST";
    public static final String RES_SET_TOPIC_ERROR = "411 SET TOPIC ERROR";
    public static final String RES_START_PUB_FAIL = "412 START PUB FAIL - REGISTER REQUIRE";
    public static final String RES_BYE = "500 BYE";

    public static boolean checkSetTopic = false;
    public static boolean checkStartPub = false;

    public static boolean checkSubcribeTopic = false;
    public static boolean checkUnSubscribeTopic = false;
}
