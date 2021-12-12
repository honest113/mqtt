package common;

import java.io.DataOutputStream;
import java.util.ArrayList;

public class SubscriberData {
    private static int seed = 1;
    private int id;
    private String name;
    private ArrayList<Topic> listTopics;
    public DataOutputStream outputStream = null;

    public SubscriberData(String _name) {
        this.id = seed++;
        this.name = _name;
        listTopics = new ArrayList<Topic>();
    }

    public void setOutputStream(DataOutputStream _outputStream) {
        this.outputStream = _outputStream;
    }

    public void addTopic(Topic _topic) {
        this.listTopics.add(_topic);
    }

    public void removeTopic(Topic _topic) {
        this.listTopics.remove(_topic);
    }

    public Topic getSubscriberTopicByName(String name) {
        Topic resTopic = null;
        for (Topic t : listTopics) {
            if (t.getTopicName().equals(name)) {
                resTopic = t;
                break;
            }
        }
        return resTopic;
    }

    public String getListTopics() {
        String res = "";
        for (Topic t : listTopics) {
            res += "\n--- " + t.getTopicName();
        }
        return res;
    }

    public String getName() {
        return this.name;
    }

}
