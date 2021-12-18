package common;

import com.google.gson.Gson;

public class PublisherData {
    private static int seed = 1;
    private int id;
    private String name;
    private Topic topic;

    public PublisherData(String _name) {
        this.id = seed++;
        this.name = _name;
    }

    public void setTopic(Topic _topic) {
        this.topic = _topic;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public String getName() {
        return this.name;
    }

    public String getAllInfo() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
