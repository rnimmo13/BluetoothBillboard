package com.seniordesign.bluetoothbillboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

public class home_screen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(), // Context
                "us-east-1:ed50d9e9-fd87-4188-b4e2-24a974ee68e9", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        Dynamo_Interface.setApplication_context(getApplicationContext());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void intentViewPostListScreen(View view) {
        Intent i = new Intent(this, view_post_list_screen.class);
        startActivity(i);
    }

    public void intentPost(View view) {
        Intent i = new Intent(this, post_screen.class);
        startActivity(i);
    }

    public void intentSavedBoards(View view) {
        Intent i = new Intent(this, saved_boards_screen.class);
        startActivity(i);
    }

    public void intentSavedPosts(View view) {
        Intent i = new Intent(this, saved_posts_screen.class);
        startActivity(i);
    }

    public void intentSearch(View view) {
        Intent i = new Intent(this, search_screen.class);
        startActivity(i);
    }

    public void intentLogin(View view) {
        Intent i = new Intent(this, login_screen.class);
        startActivity(i);
    }

    public void intentViewBoardList(View view) {
        Intent i = new Intent(this, view_board_list_screen.class);
        startActivity(i);
    }

    public void intentBans(View view) {
        Intent i = new Intent(this, bans_screen.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
