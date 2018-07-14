package me.braedonvillano.simpleinstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.braedonvillano.simpleinstagram.models.Post;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("braedont_ask")
                .clientKey("this-is-the-secret-string")
                .server("http://braedonvillano-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
