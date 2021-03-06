package common;

import java.util.ArrayList;

import com.google.gson.Gson;

public class Topic {
    private static int seed = 1;
    private int id;
    private String topicName;

    public Topic(String _name) {
        this.id = seed++;
        this.topicName = _name;
    }

    public Topic(String _json, boolean _isJson) {
        Gson gson = new Gson();
        Topic topic = gson.fromJson(_json, Topic.class);
        this.id = topic.id;
        this.topicName = topic.topicName;
    }

    public void setTopicName(String _topicName) {
        this.topicName = _topicName;
    }

    public String getTopicName() {
        return this.topicName;
    }

    public int getTopicId() {
        return this.id;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public boolean compatibleWithName(String name) {
        ArrayList<String> current = HelperData.regexByChar(this.topicName, '/');
        ArrayList<String> other = HelperData.regexByChar(name, '/');

        if (current.size() != other.size())
            return false;

        for (int i = 0; i < other.size(); i++) {
            if ( current.get(i).equals("*") || other.get(i).equals("*") || current.get(i).equals(other.get(i)) )
                continue;
            else
                return false;
        }

        return true;
    }
}
