package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;

/**
 *  REST msg between pub <-> broker <-> sub
 */
public class TransferMessage {
    private String message;
    private String sourceName;
    private Topic topic;
    private String datetime;

    public TransferMessage(String _message, String _srcName, Topic _topic) {
        this.message = _message;
        this.sourceName = _srcName;
        this.topic = _topic;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.datetime = dtf.format(now);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
