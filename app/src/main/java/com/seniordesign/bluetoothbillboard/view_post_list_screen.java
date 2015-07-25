package com.seniordesign.bluetoothbillboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

import java.util.Vector;

@SuppressWarnings("unused")
public class view_post_list_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_list_screen);
        android.support.v7.app.ActionBar title_Bar = getSupportActionBar();
        assert getSupportActionBar() != null;
        title_Bar.setTitle("Post List");
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(), // Context
                "us-east-1:ed50d9e9-fd87-4188-b4e2-24a974ee68e9", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        Dynamo_Interface.setApplication_context(getApplicationContext());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Vector<Board> boards = Dynamo_Interface.getAll_board_information();
        Vector<String> board_names = new Vector<>();
        for (int i = 0; i < boards.size(); i++){
            board_names.add(boards.get(i).getBoard_Name());
        }
        ListAdapter postAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, board_names);
        ListView list_view = (ListView) findViewById(R.id.listView);
        list_view.setAdapter(postAdapter);


        /*
        //Dynamo_Interface Tests  (all successful so far)
        Dynamo_Interface.setCurrent_board("213411");
        Board my_board = Dynamo_Interface.getCurrent_board_info();
        Board my_posts = Dynamo_Interface.getFiltered_posts(Long.parseLong(Dynamo_Interface.getCurrent_board()), "Posted");
        Post my_post = Dynamo_Interface.getSingle_post(my_posts.getPosts().firstElement().getPost_ID());
        //Device_Interface Tests
        Device_Interface device_database = new Device_Interface(getApplicationContext());
        device_database.save_Post(my_posts.getPosts().firstElement());
        device_database.get_Posts();
        device_database.delete_Post(my_posts.getPosts().firstElement());
        device_database.save_Board(Dynamo_Interface.full_board);
        device_database.get_Boards();
        device_database.delete_Board(Dynamo_Interface.full_board);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_post_list_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        switch (item.getItemId()) {
            case R.id.view_post_action:
                startActivity(new Intent(this, view_post_list_screen.class));
                return true;
            case R.id.submit_post_action:
                startActivity(new Intent(this, post_screen.class));
                return true;
            case R.id.saved_post_action:
                startActivity(new Intent(this, saved_posts_screen.class));
                return true;
            case R.id.saved_board_action:
                startActivity(new Intent(this, saved_boards_screen.class));
                return true;
            case R.id.board_list_action:
                startActivity(new Intent(this, view_board_list_screen.class));
                return true;
            case R.id.login_action:
                startActivity(new Intent(this, login_screen.class));
                return true;
            case R.id.search_action:
                startActivity(new Intent(this, search_screen.class));
                return true;
            case R.id.ban_list_action:
                startActivity(new Intent(this, bans_screen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
