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

    /** getter - setter */

    public void setMessage(String _message) {
        this.message = _message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setSourceName(String _srcName) {
        this.sourceName = _srcName;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public void setTopic(Topic _topic) {
        this.topic = _topic;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public void setDateTime(String _datetime) {
        this.datetime = _datetime;
    }

    public String getDateTime() {
        return this.datetime;
    }

    public TransferMessage(String _message, String _srcName, Topic _topic) {
        this.message = _message;
        this.sourceName = _srcName;
        this.topic = _topic;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.datetime = dtf.format(now);
    }

    public TransferMessage(String _json, boolean _isJson) {
        Gson gson = new Gson();
        TransferMessage transferMessage = gson.fromJson(_json, TransferMessage.class);
        this.message = transferMessage.message;
        this.sourceName = transferMessage.sourceName;
        this.topic = transferMessage.topic;
        this.datetime = transferMessage.datetime;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
