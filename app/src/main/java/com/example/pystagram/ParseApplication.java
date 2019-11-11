package com.example.pystagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Register your parse models
        ParseObject.registerSubclass(Post.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                 .applicationId("py-gram") // should correspond to APP_ID env variable
                 .clientKey("19991030qaz")  // set explicitly unless clientKey is explicitly configured on Parse server
                 // I cannot use http to login, but https works!
                 .server("https://py-gram.herokuapp.com/parse").build());

    }
}
