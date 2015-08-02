package com.seniordesign.bluetoothbillboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

public class view_queue_list_screen extends AppCompatActivity {

    Board my_board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_queue_list_screen);
        android.support.v7.app.ActionBar title_Bar = getSupportActionBar();
        assert getSupportActionBar() != null;
        title_Bar.setTitle("Queue List");

        my_board = Dynamo_Interface.getFiltered_posts(Dynamo_Interface.getCurrent_board_info().getBoard_ID(), "Queued");

        @SuppressWarnings("unchecked")
        ArrayAdapter postAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, my_board.getPosts()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(my_board.getPosts().get(position).getHost());
                text2.setText(my_board.getPosts().get(position).getInformation());
                text2.setSingleLine();
                return view;
            }
        };
        ListView list_view = (ListView) findViewById(R.id.lstQueued);
        list_view.setAdapter(postAdapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Post selected_post = (my_board.getPosts().get(position));
                Dynamo_Interface.setSelected_post(selected_post);
                startActivity(new Intent(view_queue_list_screen.this, view_queued_post_screen.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_queue_list_screen, menu);
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
