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
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://livequery.b4a.io/"));
        } catch (URISyntaxException e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if(parseLiveQueryClient!=null){
            Toast.makeText(this, "Client established", Toast.LENGTH_SHORT).show();
            //Subscribe
            ParseQuery<ParseObject> parseQuery = new ParseQuery("Dogs");
            SubscriptionHandling<ParseObject> subscriptionHandling =
                    parseLiveQueryClient.subscribe(parseQuery);

            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    textView.setText("Created "+object.getObjectId());
                    Toast.makeText(MainActivity.this, "Created", Toast.LENGTH_SHORT).show();
                    Log.d("custom","created");
                });

            });

            subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE, (query, object) -> {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                textView.setText("Updated "+object.getObjectId());
                Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                Log.d("custom","updated");
                });

            });
        }

    }

//    public void signUp(View view) {
//        ParseUser user = new ParseUser();
//        user.setUsername("01714251278");
//        user.setPassword("6693");
//        user.signUpInBackground(e -> {
//            if (e==null){
//                Toast.makeText(MainActivity.this, "Compete", Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void logOut(View view) {
//        ParseUser.logOutInBackground(e -> {
//            if (e==null){
//                Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void login(View view) {
//        ParseUser.logInInBackground("01714251278", "8940", (user, e) -> {
//            if (e==null){
//                Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void fetch(View view) {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Dogs");
//
//        // The query will search for a ParseObject, given its objectId.
//        // When the query finishes running, it will invoke the GetCallback
//        // with either the object, or the exception thrown
//        query.getInBackground("TJAXLGFZSp", (result, e) -> {
//            if (e == null) {
//                Toast.makeText(this, "Dog "+result.getString("name"), Toast.LENGTH_SHORT).show();
//            } else {
//                // something went wrong
//                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}