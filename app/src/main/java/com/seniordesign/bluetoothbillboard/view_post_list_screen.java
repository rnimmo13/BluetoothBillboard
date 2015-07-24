package com.seniordesign.bluetoothbillboard;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Vector;

public class view_post_list_screen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_list_screen);
        //Dynamo_Interface Tests  (all successful so far)
        Dynamo_Interface.setCurrent_board("213411");
        Vector<Board> boards = Dynamo_Interface.getAll_board_information();
        Board my_board = Dynamo_Interface.getCurrent_board_info();
        Board my_posts = Dynamo_Interface.getFiltered_posts(Long.parseLong(Dynamo_Interface.getCurrent_board()), "Posted");
        Post my_post = Dynamo_Interface.getSingle_post(my_posts.getPosts().firstElement().getPost_ID());

        ListAdapter postAdapter = new ArrayAdapter<Board> (this, android.R.layout.simple_list_item_1, boards);
        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(postAdapter);

        /*//Device_Interface Tests
        Device_Interface device_database = new Device_Interface(getApplicationContext());
        device_database.save_Post(my_posts.getPosts().firstElement());
        device_database.get_Posts();
        device_database.delete_Post(my_posts.getPosts().firstElement());
        device_database.save_Board(Dynamo_Interface.full_board);
        device_database.get_Boards();
        device_database.delete_Board(Dynamo_Interface.full_board);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_post_list_screen, menu);
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
