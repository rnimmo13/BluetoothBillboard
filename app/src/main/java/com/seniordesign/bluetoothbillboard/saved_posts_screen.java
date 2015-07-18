package com.seniordesign.bluetoothbillboard;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class saved_posts_screen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts_screen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saved_posts_screen, menu);
        return true;
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
