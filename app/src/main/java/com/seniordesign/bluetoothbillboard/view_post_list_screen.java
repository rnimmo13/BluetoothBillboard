package com.seniordesign.bluetoothbillboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class view_post_list_screen extends AppCompatActivity {

    Board my_board;

    @Override@SuppressWarnings("unused")
    protected void onCreate(Bundle savedInstanceState) {
        //view initializations
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
        Dynamo_Interface.setCurrent_board("000000");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Set<String> defaultSet = new HashSet<>();
        SharedPreferences blocked_types = Dynamo_Interface.application_context.getSharedPreferences("blocked_types", Context.MODE_PRIVATE);
        ArrayList<String> type_list = new ArrayList<>(blocked_types.getStringSet("types", defaultSet));
        SharedPreferences blocked_hosts = Dynamo_Interface.application_context.getSharedPreferences("blocked_hosts", Context.MODE_PRIVATE);
        ArrayList<String> host_list = new ArrayList<>(blocked_hosts.getStringSet("hosts", defaultSet));

        my_board = Dynamo_Interface.getFiltered_posts(Dynamo_Interface.getCurrent_board_info().getBoard_ID(), "Posted");
        for (int i = 0; i < my_board.getPosts().size(); i++){
            if (type_list.contains(my_board.getPosts().get(i).getPost_Type()) || host_list.contains(my_board.getPosts().get(i).getHost())){
                my_board.getPosts().removeElementAt(i);
            }
        }
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
        ListView list_view = (ListView) findViewById(R.id.listView);
        list_view.setAdapter(postAdapter);
        final Intent postView_intent = new Intent(this, view_post_screen.class);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Post selected_post = (my_board.getPosts().get(position));
                Dynamo_Interface.setSelected_post(selected_post);
                startActivity(postView_intent);
            }
        });
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id){
                final Post savable_post = my_board.getPosts().get(position);
                new AlertDialog.Builder(view_post_list_screen.this)
                        .setTitle("Save Post")
                        .setMessage("Would you like to save this post from " + savable_post.getHost() + "?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Device_Interface device_link = new Device_Interface(view_post_list_screen.this);
                                device_link.save_Post(savable_post);
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();


                return true;
            }

        });
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

    public void onClick_btnSaveBoard(View view){
        Device_Interface device_link = new Device_Interface(view_post_list_screen.this);
        device_link.save_Board(Dynamo_Interface.getCurrent_board_info());
    }
}
