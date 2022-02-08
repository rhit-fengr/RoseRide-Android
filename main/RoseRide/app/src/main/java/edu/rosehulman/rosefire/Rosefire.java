package edu.rosehulman.rosefire;

import android.content.Context;
import android.content.Intent;

import edu.rosehulman.rosefire.RosefireResult;
import edu.rosehulman.rosefire.WebLoginActivity;

public final class Rosefire {

    static boolean DEBUG = false;

    public static Intent getSignInIntent(Context context, String registryToken) {
        Intent intent = new Intent(context, WebLoginActivity.class);
        intent.putExtra(WebLoginActivity.REGISTRY_TOKEN, registryToken);
        return intent;
    }

    public static RosefireResult getSignInResultFromIntent(Intent data) {
        String token = data != null ? data.getStringExtra(WebLoginActivity.JWT_TOKEN) : null;
        return new RosefireResult(token);
    }

}
