package common;

import com.google.gson.Gson;

public class HelperData {
    private String name;

    public HelperData(String _name) {
        this.name = _name;
    }

    public HelperData(String _json, boolean _isJson) {
        Gson gson = new Gson();
        HelperData helperData = gson.fromJson(_json, HelperData.class);
        this.name = helperData.name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
