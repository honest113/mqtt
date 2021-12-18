package common;

import java.util.ArrayList;

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

    public static ArrayList<String> regexByChar(String s, char c) {
        ArrayList<String> res = new ArrayList<String>();
        String tmp = "";
        for(int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                if (!tmp.equals(""))
                    res.add(tmp);
                tmp = "";
            }
            else {
                tmp += s.charAt(i);
            }
        }
        if (!tmp.equals(""))
            res.add(tmp);
        return res;
    }
}
