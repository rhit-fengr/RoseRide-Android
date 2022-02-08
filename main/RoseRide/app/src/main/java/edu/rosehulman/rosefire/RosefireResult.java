package edu.rosehulman.rosefire;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public final class RosefireResult {

    private String username;
    private String group;
    private String name;
    private String email;
    private String token;

    public RosefireResult(String token) {
        this.token = token;
        if (token == null) {
           return;
        }
        String payload = token.split("\\.")[1];
        try {
            JSONObject data = new JSONObject(new String(Base64.decode(payload, Base64.DEFAULT)));
            if (data.has("d")) {
                // Old format
                data = data.getJSONObject("d");
            } else {
                username = data.getString("uid");
                data = data.getJSONObject("claims");
                data.put("uid", username);
            }
            this.username = data.getString("uid");
            this.group = data.getString("group");
            this.email = data.getString("email");
            this.name = data.getString("name");
        } catch (JSONException e) {
            Log.e("Rosefire", "Could not decode data", e);
        }
    }

    public boolean isSuccessful() {
        return token != null;
    }

    public String getUsername() {
        return username;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
