package com.example.back4apptester;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import com.parse.livequery.LiveQueryException;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        //Subscribe to live query
        // Init Live Query Client


        ParseLiveQueryClient parseLiveQueryClient = null;

        try {
            // newapptest.back4app.io / newapptest.b4a.io
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://newapptest.back4app.io/"));
        } catch (URISyntaxException e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (parseLiveQueryClient != null) {
            Toast.makeText(this, "Client established", Toast.LENGTH_SHORT).show();
            //Subscribe
            ParseQuery<ParseObject> parseQuery = new ParseQuery("Category");
            SubscriptionHandling<ParseObject> subscriptionHandling =
                    parseLiveQueryClient.subscribe(parseQuery);

            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    textView.setText("Created " + object.getObjectId());
                    Toast.makeText(MainActivity.this, "Created", Toast.LENGTH_SHORT).show();
                    Log.d("custom", "created");
                });

            });

            subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE, (query, object) -> {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    textView.setText("Updated " + object.getObjectId());
                    Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    Log.d("custom", "updated");
                });

            });
        }

    }
}