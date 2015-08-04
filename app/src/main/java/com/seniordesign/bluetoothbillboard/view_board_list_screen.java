package com.seniordesign.bluetoothbillboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;


public class view_board_list_screen extends AppCompatActivity {

    Vector<Board> board_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_board_list_screen);
        android.support.v7.app.ActionBar title_Bar = getSupportActionBar();
        assert getSupportActionBar() != null;
        title_Bar.setTitle("Board List");

        Set<String> defaultSet = new HashSet<>();
        SharedPreferences found_boards = view_board_list_screen.this.getSharedPreferences("found_beacons", Context.MODE_PRIVATE);
        ArrayList<String> type_list = new ArrayList<>(found_boards.getStringSet("boards", defaultSet));
        board_list = new Vector<>();
        for (int i = 0; i < type_list.size(); i++){
            Board fill_me = Dynamo_Interface.getSingle_board_information(Long.parseLong(type_list.get(i)));
            board_list.add(fill_me);
        }

        @SuppressWarnings("unchecked")
        ArrayAdapter board_adapter = new ArrayAdapter(view_board_list_screen.this, android.R.layout.simple_list_item_2, android.R.id.text1, board_list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(board_list.get(position).getBoard_Name());
                text2.setText(board_list.get(position).getInstructions());
                text2.setSingleLine();
                return view;
            }
        };
        ListView list_view = (ListView) findViewById(R.id.lstBoards);
        list_view.setAdapter(board_adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Board selected_board = (board_list.get(position));
                Dynamo_Interface.setCurrent_board(Long.toString(selected_board.getBoard_ID()));
                startActivity(new Intent(view_board_list_screen.this, view_post_list_screen.class));
            }
        });
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Board info_board = board_list.get(position);
                new AlertDialog.Builder(view_board_list_screen.this)
                        .setTitle(info_board.getBoard_Name())
                        .setMessage("Organization\n" + info_board.getOrganization() + "\nInstructions\n" + info_board.getInstructions())
                        .setPositiveButton("OK",null)
                        .setNegativeButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Device_Interface device_link = new Device_Interface(view_board_list_screen.this);
                                device_link.save_Board(info_board);
                            }
                        })
                        .show();
                return true;
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_board_list_screen, menu);
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
